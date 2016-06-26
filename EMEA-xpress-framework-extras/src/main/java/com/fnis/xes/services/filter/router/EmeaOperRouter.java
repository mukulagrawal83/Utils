package com.fnis.xes.services.filter.router;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.event.EventBroker;
import com.fnis.ifx.xbo.v1_1.Operation;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.ResponseImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.OperRqHdr;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.aggregate.AgrregateMsgConfigAbility;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.template.XBOStatus;
import com.fnis.xes.services.util.StatusHelper;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: e1049528
 * Date: 1/8/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmeaOperRouter extends PayloadRouter {
    private Logger logger = Logger.getLogger(EmeaOperRouter.class);
    private static final String OPER_SUFFIX = "Oper";
    public static final String OPERRULE_Abort = "Abort";
    public static final String OPERRULE_Continue = "Continue";

    private AgrregateMsgConfigAbility aggregateMsgConfigAbility;
    private EventBroker aggregateEventBroker;

    private OperRouter defaultOperRouter;
    private List<String> emeaOpers;


    /**
     * Constructor containing AggregateMsgConfigAbility, if configured in
     * BOC Container the application will support message level aggregation
     *
     */
    public EmeaOperRouter(AgrregateMsgConfigAbility msgConfigAbility, EventBroker eventBroker,
                          ErrWarnInfoMessage ewiMessage, ErrorMappingAbility errMappingAbility) {
        aggregateMsgConfigAbility = msgConfigAbility;
        aggregateEventBroker = eventBroker;
        errorMappingAbility = errMappingAbility;
        errWarnInfoMessage = ewiMessage;
        bServiceHasErrors = false;
    }

    /**
     * This method processes all the messages which is part of an IFX Operation
     * <p/>
     * First it checks if the operation participates in message level aggregation.
     * if it does then it builds aggregate message list and non aggregate message list.
     * <p/>
     * After which it processes all the messages
     *
     * @param req  - Request object containing IFX Message
     * @param res - Response object containing the response after processing the request
     * @param link     - List of filters in the chain*
     * @throws Exception - Exception while processing the IFX Operation
     */
    @Override
    public void process(RequestMessage req, ResponseMessage res, RequestLink link) throws Exception {
        bServiceHasErrors = false;
        // Get the payload object from the requestMessage object
        Payload origPayLoad = (Payload) req.getObject();

        // Retrieve operation message
        Operation operMsg = (Operation) ((Payload) req.getObject()).getProcess();
        String operName = operMsg.getName();
        Service  svcMsg = (Service) operMsg.getServices().get(0);
        operMsg.setRqUID(svcMsg.getRqUID());


        if (!emeaOpers.contains(operName)) {
            // default Oper route will be used if operName not in emeaOpers list
            defaultOperRouter = new OperRouter(aggregateMsgConfigAbility, aggregateEventBroker, errWarnInfoMessage, errorMappingAbility);
            defaultOperRouter.process(req, res, link);
            return;
        }

        // EMEA Oper route processing
        List<ComponentContext> ctxList = (List) req.getProperty(ComponentContext.class.getName() + "List");
        ComponentContext ctx = ctxList.get(0);
        svcMsg = (Service) operMsg.getServices().get(0);
        ctx.setAttribute(Service.class.getName(), svcMsg);

        req.setProperty(ComponentContext.class.getName(), ctx); //this is for SelectCPAFilter (just SP_NAME is important)
        String xboClass = svcMsg.getRequest().getXBO().getClass().getName();
        req.setMsgId(xboClass.substring(0, xboClass.lastIndexOf(".") + 1) + operName + OPER_SUFFIX);
        req.setProperty("IFX_SERVICE_NAME", operName);
        MsgContainer msgContainer= null;

        try {

            //msgContainer = processOperation(operMsg, ctxList);
            routeMsgRq(req, res, link);
            svcMsg.setAction("Inq");
            MsgRequest msgRq = new MsgRequest(svcMsg, ctx, 0, operMsg.getName());
            populateMsgRs(msgRq, res);

            operMsg.getServices().clear();
            operMsg.getServices().add(msgRq.getMsg());//set(msgRq.getPosition(), msgRq.getMsg());
            operMsg.setRqUID(svcMsg.getRqUID());
            req.setObject(origPayLoad);
            req.setObject(operMsg);

            //Set operation level status here

            /*    String errorMsg = msgContainer.getOperErrorMsg();
                if(errorMsg == null || errorMsg.equals(""))
                    errorMsg = "Operation: " + oprMsg.getName() + " not configured in aggregate message config file.";
                Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_UNKNOWN_CONFIG_ERROR,
                        StatusHelper.IFX_SEV_ERROR, errorMsg);
                oprMsg.setStatus(errStatus); */
            if(bServiceHasErrors){
                Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED,
                        StatusHelper.IFX_SEV_ERROR, "Error occured while process the service message");
                operMsg.setStatus(errStatus);
            }else
                operMsg.setStatus(StatusHelper.createSuccessStatus());

        } catch (Throwable throwable) {
            logger.error(throwable.getMessage(), throwable);
            // clear services from Oper as we will have just one Inq in response
            operMsg.getServices().clear();
            svcMsg.setAction("Inq");
            Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED,
                    StatusHelper.IFX_SEV_ERROR, throwable.getMessage());

            operMsg.setStatus(errStatus);
            operMsg.setRqUID(svcMsg.getRqUID());
            // set Inq service in Oper response
            operMsg.getServices().add(svcMsg);
            req.setObject(operMsg);
        }

    }

    private MsgContainer processOperation(Operation operMsg, List<ComponentContext> ctxList) {
        MsgContainer msgContainer = null;
        try{

            msgContainer = new MsgContainer(aggregateMsgConfigAbility);
            //set operSPName to MsgContainer
            if(operMsg.getOperRqHdr() != null && operMsg.getOperRqHdr().getContextRqHdr() != null)
                msgContainer.setOperSPName(operMsg.getOperRqHdr().getContextRqHdr().getSPName());

            // Iterate through list of service message which is part of an operation
            // and process it
            for (int i = 0; i < operMsg.getServices().size(); i++) {
                Service svcMsg = (Service) operMsg.getServices().get(i);

                // Set the service message to process as a root of request message
                ComponentContext ctx = ctxList.get(i);
                ctx.setAttribute(OperRqHdr.class.getName(), operMsg.getOperRqHdr());
                ctx.setAttribute(ComponentContext.SEQ_NUMBER, i+1);
                MsgRequest svcRq = new MsgRequest(svcMsg, ctxList
                        .get(i), i, operMsg.getName());
                msgContainer.addServiceRequest(svcRq);
            }



        }catch (Throwable t) {
            Status errStatus = StatusHelper.createStatus(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED,
                    StatusHelper.IFX_SEV_ERROR, t.getMessage());
            operMsg.setStatus(errStatus);
            populateNotProcessedMsgStatus(msgContainer.getAggregatedMsgList());
        }
        return msgContainer;
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

    public void setEmeaOpers(List<String> emeaOpers) {
        this.emeaOpers = emeaOpers;
    }
}
