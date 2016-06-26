package com.fisglobal.emea.xpress.router.corewrapper;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.jef.logging.Level;
import com.fnf.jef.logging.Logger;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.ResponseImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.util.ErrWarnInfoMessage;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.XESServicesConstants;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.errormapping.XesHostMessageWrapper;
import com.fnis.xes.services.filter.router.MsgContainer;
import com.fnis.xes.services.filter.router.MsgRequest;
import com.fnis.xes.services.filter.router.SuperMsgRequest;
import com.fnis.xes.services.filter.router.SuperPayloadRouter;
import com.fnis.xes.services.util.StatusHelper;

/**
 *
 * @author morel
 */
public class MsgRouterCoreWrapper extends SuperPayloadRouter {

   protected Logger logger = Logger.getLogger(this.getClass().getName());
   
	public MsgRouterCoreWrapper(ErrWarnInfoMessage ewiMessage, ErrorMappingAbility errMappingAbility) {
		errorMappingAbility = errMappingAbility;
		errWarnInfoMessage = ewiMessage;
		bServiceHasErrors = false;
	}   
   
   public MsgRouterCoreWrapper() {
   }
   
   /**
    * @return the errorMappingAbility
    */
   public ErrorMappingAbility getErrorMappingAbility() {
      return errorMappingAbility;
   }

   /**
    * @param errorMappingAbility the errorMappingAbility to set
    */
   public void setErrorMappingAbility(ErrorMappingAbility errorMappingAbility) {
      this.errorMappingAbility = errorMappingAbility;
   }

   /**
    * @return the errWarnInfoMessage
    */
   public ErrWarnInfoMessage getErrWarnInfoMessage() {
      return errWarnInfoMessage;
   }

   /**
    * @param errWarnInfoMessage the errWarnInfoMessage to set
    */
   public void setErrWarnInfoMessage(ErrWarnInfoMessage errWarnInfoMessage) {
      this.errWarnInfoMessage = errWarnInfoMessage;
   }

   @Override
	public void process(RequestMessage rqMsg, ResponseMessage rsMsg,
			RequestLink rqLink) throws Exception {
		long st = System.currentTimeMillis();
		// Extract the IFX Service Message to be processed
		Service svcMsg = (Service) ((Payload) rqMsg.getObject()).getProcess();

		try {
		  
			// Get the service specific component context object
			ComponentContext ctx = (ComponentContext) rqMsg
							.getProperty(ComponentContext.class.getName());
			
			//Build the service request object containing all the service
			//specific information
			MsgRequest svcRq = new SuperMsgRequest(svcMsg, ctx, 0);

			//Add it to the message container
			MsgContainer msgContainer = new MsgContainer();
			msgContainer.addServiceRequest(svcRq);

			//pre-process the request message 
			processMsgRq(svcRq, rqMsg);
			//route it to the service
			routeMsgRq(rqMsg, rsMsg, rqLink);
			//build the service response to be returned to the client
			populateMsgRs(svcRq, rsMsg);
			rqMsg.setObject(svcMsg);
			if(bServiceHasErrors){
				errorMappingAbility.setChannel(svcMsg.getRequest().getMsgRqHdr().getContextRqHdr().getClientApp().getName());
				Status rootStatus = errorMappingAbility.mapStatusXBO(XESServicesConstants.XES_SPNAME,
						new XesHostMessageWrapper(IdConstants.IFX_ERR_SERVICE_ERRORS_OCCURRED, errWarnInfoMessage.format(
                                IdConstants.IFX_ERR_SERVICE_ERRORS_OCCURRED,
                                svcMsg.getRqUID())),null);
				StatusHelper.addAdditionalStatus(rootStatus, svcMsg.getStatus());
				svcMsg.setStatus(rootStatus);
			} else if (hasWarnings) {
				Status w = StatusHelper.createWarningStatus(svcMsg.getStatus().getStatusCode());
				StatusHelper.addAdditionalStatus(w, svcMsg.getStatus());
				svcMsg.setStatus(w);
			} else {
				Status successStatus = StatusHelper.createSuccessStatus();
				StatusHelper.addAdditionalStatus(successStatus, svcMsg.getStatus());
				svcMsg.setStatus(successStatus);
			}

		} catch (Throwable t) {
         String errMsg = errWarnInfoMessage.format(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED, t.getMessage(), t.getCause() != null ? t.getCause().toString() : "");
			Status rootStatus = errorMappingAbility.mapStatusXBO(XESServicesConstants.XES_SPNAME,
					new XesHostMessageWrapper(IdConstants.IFX_ERR_SERVICE_EXCEPTION_OCCURRED,errMsg),null);
			svcMsg.setStatus(rootStatus);
			ResponseImpl res = new ResponseImpl();
			svcMsg.setResponse(res);
		}finally{
        	if ( logger.isLoggable(Level.INFO)&& svcMsg != null){
        		long et = System.currentTimeMillis();
            	logger.info( "Time to process "+svcMsg.getName()+"#"+svcMsg.getAction()+" = "+(et-st) + " ms");
        	}
        }
	}
 
   
}
