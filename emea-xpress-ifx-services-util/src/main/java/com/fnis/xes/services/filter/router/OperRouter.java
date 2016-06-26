package com.fnis.xes.services.filter.router;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.bulkinteraction.BeginBulkInteractionPostProcessing;
import com.fnf.xes.framework.bulkinteraction.BeginBulkInteractionPreProcessing;
import com.fnf.xes.framework.bulkinteraction.EndBulkInteractionPostProcessing;
import com.fnf.xes.framework.concurrent.MultiTaskUtil;
import com.fnf.xes.framework.concurrent.ThreadPoolExecutor;
import com.fnf.xes.framework.event.EventBroker;
import com.fnf.xes.framework.pa.usc.UscMessage;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.Operation;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.ResponseImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.MsgRqHdr;
import com.fnis.ifx.xbo.v1_1.base.OperRqHdr;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.aggregate.AggregateConstants;
import com.fnis.xes.services.aggregate.AgrregateMsgConfigAbility;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.template.XBOStatus;
import com.fnis.xes.services.util.StatusHelper;

/**
 * Operation Router handles all IFX messages which is part of an operation.
 * 
 * @author e0101486, e0101806
 * @since March 17th 2009
 */
public class OperRouter extends SuperPayloadRouter {

	public static final String OPERRULE_Abort = "Abort";
	public static final String OPERRULE_Continue = "Continue";
	
	protected AgrregateMsgConfigAbility aggregateMsgConfigAbility;
	protected EventBroker aggregateEventBroker;
	private static final String EJBNAME="java:comp/env/ejb/xes/TransportLocalHomeIfx2Services";
	
	/**
	 * Constructor containing AggregateMsgConfigAbility, if configured in
	 * BOC Container the application will support message level aggregation
	 * @param eventBroker 
	 *  
	 * @param svcBulkConfigAbility
	 */
	public OperRouter(AgrregateMsgConfigAbility msgConfigAbility, EventBroker eventBroker, 
			ErrWarnInfoMessage ewiMessage, ErrorMappingAbility errMappingAbility){
		aggregateMsgConfigAbility = msgConfigAbility;
		aggregateEventBroker = eventBroker;
		errorMappingAbility = errMappingAbility;
		errWarnInfoMessage = ewiMessage;
		bServiceHasErrors = false;
	}
	
	/**
	 * This method processes all the messages which is part of an IFX Operation
	 * 
	 * First it checks if the operation participates in message level aggregation.
	 * if it does then it builds aggregate message list and non aggregate message list.
	 * 
	 * After which it processes all the messages
	 * 
	 * @param RequestMessage - Request object containing IFX Message 
	 * @param ResponseMessage - Response object containing the response after processing the request
	 * @param RequestLink - List of filters in the chain
	 * 
	 * @throws Exception - Exception while processing the IFX Operation 
	 */
	public void process(RequestMessage req, ResponseMessage res,
			RequestLink link){
		// Get the payload object from the requestMessage object
		Payload origPayLoad = (Payload) req.getObject();

		// Retrieve operation message
		Operation oprMsg = (Operation) ((Payload) req.getObject()).getProcess();		
		
		MsgContainer msgContainer = null;
		try {
			// Get the list of componentContext from the requestMessage
			@SuppressWarnings("unchecked")
			List<ComponentContext> compCtxList = (List) req
					.getProperty(ComponentContext.class.getName() + "List");
			msgContainer = new MsgContainer(aggregateMsgConfigAbility);
			
			//set operSPName to MsgContainer
			if(oprMsg.getOperRqHdr() != null && oprMsg.getOperRqHdr().getContextRqHdr() != null)
				msgContainer.setOperSPName(oprMsg.getOperRqHdr().getContextRqHdr().getSPName());

			// Iterate through list of service message which is part of an operation 
			// and process it
			for (int i = 0; i < oprMsg.getServices().size(); i++) {
				Service svcMsg = (Service) oprMsg.getServices().get(i);
				
				// Set the service message to process as a root of request message
				ComponentContext ctx = compCtxList.get(i);
				ctx.setAttribute(OperRqHdr.class.getName(), oprMsg.getOperRqHdr());
				ctx.setAttribute(ComponentContext.SEQ_NUMBER, i+1);
				MsgRequest svcRq = new MsgRequest(svcMsg, compCtxList
						.get(i), i, oprMsg.getName());				
				msgContainer.addServiceRequest(svcRq);
			}

			// First process all the non bulk services
			if(msgContainer.getNonAggregatedMsgList().size() > 0)
				processNonAggregateMsgs(msgContainer.getNonAggregatedMsgList(), req,
					res, link, oprMsg);

			// Check if the there are more than one service which can be process otherwise
			// process immediate
			if (msgContainer.getAggregatedMsgList().size() > 1 && 				
					msgContainer.getAggregatedMsgList().size() == oprMsg.getServices().size()) {
				processAggregateMsgs(msgContainer.getAggregatedMsgList(), req,
						res, link, oprMsg);
			}
			/* removed by jv as this is redundant on 6/11/2010. TODO:: remove the code and comment later.
			else
				processNonAggregateMsgs(msgContainer.getNonAggregatedMsgList(),
						req, res, link, oprMsg);
			*/
			req.setObject(origPayLoad);
			req.setObject(oprMsg);
			
			//Set operation level status here
			if(msgContainer.getNonAggregatedMsgList().size() == 0 && 
					(msgContainer.getAggregatedMsgList().size() == 0
					|| msgContainer.getAggregatedMsgList().size() != oprMsg.getServices().size())){
				String errorMsg = msgContainer.getOperErrorMsg();				
				if(errorMsg == null || errorMsg.equals(""))
					errorMsg = "Operation: " + oprMsg.getName() + " not configured in aggregate message config file.";
				Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_UNKNOWN_CONFIG_ERROR, 
						StatusHelper.IFX_SEV_ERROR, errorMsg);
				oprMsg.setStatus(errStatus);				
			}else if(msgContainer.getAggregatedMsgList().size() == 1){
				
				String errorMsg = "Operation: " + oprMsg.getName() + " should contain more than one Service Request.";
				Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_UNKNOWN_CONFIG_ERROR, 
						StatusHelper.IFX_SEV_ERROR, errorMsg);
				oprMsg.setStatus(errStatus);
				populateNotProcessedMsgStatus(msgContainer.getAggregatedMsgList());
			}else if(bServiceHasErrors){
				Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED, 
						StatusHelper.IFX_SEV_ERROR, "Error occured while process the service message");
				oprMsg.setStatus(errStatus);
			}else
				oprMsg.setStatus(StatusHelper.createSuccessStatus());

		} catch (Throwable t) {
			Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED, 
					StatusHelper.IFX_SEV_ERROR, t.getMessage());
			oprMsg.setStatus(errStatus);
			populateNotProcessedMsgStatus(msgContainer.getAggregatedMsgList());
		}
	}

	/**
	 * Processes all the message which does not participates in message aggregation
	 * 
	 * @param svcRqList - List containing ServiceRequest Object
	 * @param req - RequestMessage object
	 * @param res - ResponseMessage object
	 * @param link - RequestLink object
	 * @param oprMsg - Operation object containing all the Service
	 * 
	 * @throws Throwable - Exception while processing a message
	 */
	public void processNonAggregateMsgsAtParrallel(List<MsgRequest> svcRqList, RequestMessage req,
			ResponseMessage res,RequestLink link, Operation oprMsg) throws Throwable{

		Iterator<MsgRequest> it = svcRqList.iterator();
		MultiTaskUtil tasks = new MultiTaskUtil();
		while(it.hasNext()){
			MsgRequest r = it.next();
			UscMessage uscMessage = buildUscMessage(r, oprMsg);
			tasks.addTask(EJBNAME, uscMessage);
		}
		
		List<UscMessage> results  = tasks.executeTasks();
		
		it = svcRqList.iterator();
		int resIndex = 0;
		while (it.hasNext()){
			MsgRequest r = it.next();
			UscMessage ucsResp = results.get(resIndex++);
			Payload payload = (Payload)ucsResp.getPayload();
			oprMsg.getServices().set(r.getPosition(), (Service)payload.getProcess());
		}
	}
	
	public void processNonAggregateMsgs(List<MsgRequest> svcRqList, RequestMessage req,
			ResponseMessage res,RequestLink link, Operation oprMsg) throws Throwable{
		boolean isProcessConcurrent = false;
		String onError = null;
		
		//check to see if Operation is set to concurrent process.
		if(aggregateMsgConfigAbility.isProcessConcurrent(oprMsg.getName()))
			isProcessConcurrent = true;
		else if (oprMsg.getOperRqHdr() != null &&
				oprMsg.getOperRqHdr().getOperRules() != null &&
				oprMsg.getOperRqHdr().getOperRules().getProcessConcurrent() != null &&				
				oprMsg.getOperRqHdr().getOperRules().getProcessConcurrent().booleanValue() == true)
			isProcessConcurrent = true;
		
		if (ThreadPoolExecutor.threadOnFlag && isProcessConcurrent) {
			processNonAggregateMsgsAtParrallel(svcRqList, req, res,link,oprMsg);
			return;
		}
		
		//check to see if Operation is set to OnError.
		if(aggregateMsgConfigAbility.getOperOnError(oprMsg.getName()) != null)
			onError = aggregateMsgConfigAbility.getOperOnError(oprMsg.getName());		
		if(oprMsg.getOperRqHdr() != null && 
				oprMsg.getOperRqHdr().getOperRules() != null && 
				oprMsg.getOperRqHdr().getOperRules().getOnError() != null)
			onError = oprMsg.getOperRqHdr().getOperRules().getOnError(); //get the OnError from Oper rules
		
		Iterator<MsgRequest> it = svcRqList.iterator();
		while(it.hasNext()){
			MsgRequest r = it.next();
			processMsgRq(r,req);
			routeMsgRq(req,res,link);
			populateMsgRs(r, res);
			oprMsg.getServices().set(r.getPosition(), r.getMsg());
			
			// To set success status
         if (!bServiceHasErrors) {
            if (hasWarnings) {
               Status w = StatusHelper.createWarningStatus(r.getMsg().getStatus().getStatusCode());
               StatusHelper.addAdditionalStatus(w, r.getMsg().getStatus());
               r.getMsg().setStatus(w);
            } else {
               Status successStatus = StatusHelper.createSuccessStatus();
               StatusHelper.addAdditionalStatus(successStatus, r.getMsg().getStatus());
               r.getMsg().setStatus(successStatus);
            }
         }
			// Implement the Oper rule onError
			if(bServiceHasErrors && 
					onError != null && onError.equals(OPERRULE_Abort)){
				
				// Set Not Processed status to the rest of the messages
				while(it.hasNext()){
					MsgRequest msgRq = it.next();
					if(msgRq.getMsg().getStatus() == null){
						msgRq.getMsg().setStatus(StatusHelper.createNotProcessedStatus());
						ResponseImpl svcRes = new ResponseImpl();
						msgRq.getMsg().setResponse(svcRes);
					}
				}
				break;
			}// continue for everything else			
		}
	}
	
	@SuppressWarnings("unchecked")
	protected UscMessage buildUscMessage(MsgRequest r, Operation oprMsg)
	{	
		Payload payload = (Payload)Factory.create(Payload.class);
		payload.setProcess(r.getMsg());
		
		((Service)payload.getProcess()).setRqUID(oprMsg.getRqUID());
		((Service)payload.getProcess()).getRequest().setMsgRqHdr((MsgRqHdr)Factory.create(MsgRqHdr.class));
		((Service)payload.getProcess()).getRequest().getMsgRqHdr().setContextRqHdr(oprMsg.getOperRqHdr().getContextRqHdr());
		List list = ((Service)payload.getProcess()).getRequest().getMsgRqHdr().getCredentialsRqHdr();
		List listOri =  oprMsg.getOperRqHdr().getCredentialsRqHdr();
		
		for (int i=0; i<listOri.size(); i++)
		{
			list.add(listOri.get(i));
		}
		UscMessage uscMessage = new UscMessage(payload);
		return uscMessage;
	}

	/**
	 * Processes all the message which participates in message aggregation
	 * 
	 * @param svcRqList - List containing ServiceRequest Object
	 * @param req - RequestMessage object
	 * @param res - ResponseMessage object
	 * @param link - RequestLink object
	 * @param oprMsg - Operation object containing all the Service
	 * 
	 * @throws Throwable - Exception while processing a message
	 */
	public void processAggregateMsgs(List<MsgRequest> svcRqList, RequestMessage req,
			ResponseMessage res,RequestLink link, Operation oprMsg) throws Throwable{
		try{
	        // pre-process all message request which participates in service aggregation 
			aggregateEventBroker.dispatchEvent(new BeginBulkInteractionPreProcessing());
			Iterator<MsgRequest> it = svcRqList.iterator();
			MsgRequest msgRq = null;
	
			//To share the data across the services in the same operation 
			Map<String, Object> sharedData = new HashMap<String, Object>();
	
			while(it.hasNext()){
				msgRq = it.next();
				msgRq.getComponentContext().setAttribute(AggregateConstants.AGGREGATE_MSG_PHASE, AggregateConstants.AGGREGATE_MSG_PRE_PHASE);
				//Check for Operation shared interaction
				msgRq.getComponentContext().setAttribute(AggregateConstants.SHARED_INTERACTION, aggregateMsgConfigAbility.isOperShared(oprMsg.getName()));
				msgRq.getComponentContext().setAttribute(AggregateConstants.SHARED_DATA, sharedData);
				msgRq.getComponentContext().setAttribute(AggregateConstants.COUNT, svcRqList.size());
				
				processMsgRq(msgRq,req);
				routeMsgRq(req,res,link);
				
				sharedData = (Map)msgRq.getComponentContext().getAttribute(AggregateConstants.SHARED_DATA);
				
				List<XBOStatus> beanStatusList = (List<XBOStatus>) msgRq.getComponentContext().getAttribute("XBOStatusList");
				if(beanStatusList != null && hasErrors(beanStatusList)){
					msgRq.getMsg().setStatus(StatusHelper.addStatus(msgRq.getMsg().getStatus(), beanStatusList, errorMappingAbility));
					ResponseImpl svcRes = new ResponseImpl();
					msgRq.getMsg().setResponse(svcRes);
					bServiceHasErrors = true;
					break;
				}	
			}
			if(bServiceHasErrors){
				populateNotProcessedMsgStatus(svcRqList);
				return;
			}
	        // post-process all message request which participates in service aggregation
			
			aggregateEventBroker.dispatchEvent(
					new BeginBulkInteractionPostProcessing(AggregateConstants.ALL_OR_NOTHING));
			Iterator<MsgRequest> it1 = svcRqList.iterator();
			while(it1.hasNext()){
				msgRq = it1.next();
				msgRq.getComponentContext().setAttribute(AggregateConstants.AGGREGATE_MSG_PHASE, AggregateConstants.AGGREGATE_MSG_POST_PHASE);			
				processMsgRq(msgRq,req);
				routeMsgRq(req,res,link);	
				populateMsgRs(msgRq, res);
				oprMsg.getServices().set(msgRq.getPosition(), msgRq.getMsg());				
			}
		}finally{
//			XSGT-52 - EndInteraction must be called. (Venubabu .T)
	        // fire end-aggregation processing event to cleanup the resources utilized in
	        // executing the Service Aggregation transaction.
			aggregateEventBroker.dispatchEvent(new EndBulkInteractionPostProcessing());		
		}
	}
	
	private void populateNotProcessedMsgStatus(List<MsgRequest> svcRqList){
		Iterator<MsgRequest> it = svcRqList.iterator();
		MsgRequest msgRq = null;

		while(it.hasNext()){
			msgRq = it.next();
			if(msgRq.getMsg().getStatus() == null){
				msgRq.getMsg().setStatus(StatusHelper.createNotProcessedStatus());
				ResponseImpl svcRes = new ResponseImpl();
				msgRq.getMsg().setResponse(svcRes);
			}
		}
	}
	
	/**
	 * Method used to check bean status list has error
	 * 	returns true if the list has other than info severity or status code other than zero
	 * 	otherwise returns false 
	 * @param beanStatusList
	 * @return true/false
	 */
	private boolean hasErrors(List<XBOStatus> beanStatusList ){
		String severity = null;
		long statusCode = 0;
		for(XBOStatus xboStatus : beanStatusList){
			severity = xboStatus.getSeverity();
			statusCode = xboStatus.getStatusCode();
			if((severity != null && !severity.trim().equals("")
					&& !severity.trim().equalsIgnoreCase("Info"))
					|| statusCode != 0){
				return true;
			}
		}
		return false;
	}

}
