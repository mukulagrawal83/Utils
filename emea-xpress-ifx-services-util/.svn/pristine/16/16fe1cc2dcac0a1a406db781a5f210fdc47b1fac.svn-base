package com.fnis.xes.services.filter.router;

import com.fnf.jef.boc.ResponseMessage;
import com.fnf.xes.framework.ServiceException;
import com.fnis.ifx.xbo.v1_1.ResponseImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.XBO;
import com.fnis.ifx.xbo.v1_1.base.RecCtrlOut;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.template.XBOStatus;
import com.fnis.xes.services.util.StatusHelper;
import java.util.List;
import java.util.Vector;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author morel (copy paste with slight amendments)
 */
public abstract class SuperPayloadRouter extends PayloadRouter {
   
   protected boolean hasWarnings = false;
   
   /**
    *
    * @param svcRq - ServiceRequest object
    * @param rsMsg - ResponseMessage object
    *
    * @throws Exception - if error occurs while processing the response
    */
   @Override
   protected void populateMsgRs(MsgRequest svcRq, ResponseMessage rsMsg) throws ServiceException {
   
      Status status = null;
      bServiceHasErrors = false;
      hasWarnings = false;

      ComponentContext ctx = svcRq.getComponentContext();
      String spName = (String) ctx.getAttribute(ComponentContext.SP_NAME);
      spName = StringUtils.replace(spName, "fnis", "fnf");
      Service svcMsg = svcRq.getMsg();
      ResponseImpl svcRes = (ResponseImpl) svcMsg.getResponse();
      if (svcRes == null) {
         svcRes = new ResponseImpl();
      }
      List<XBOStatus> beanStatusList = getXBOStatus(svcRq);
      boolean isOverride = false;
      if (beanStatusList != null) {
         XBOStatus restStatus = new XBOStatus();
         status = StatusHelper.addStatus(svcMsg.getStatus(),
                 beanStatusList, errorMappingAbility, restStatus, spName);
         isOverride = restStatus.isOverride();
         if (StatusHelper.hasErrors(status)) {
            bServiceHasErrors = true;
         } else if (StatusHelper.hasWarnings(status)) {
            hasWarnings = true;
         }
         svcMsg.setStatus(status);
      }
      if (rsMsg.getObject() == null || (bServiceHasErrors && !isOverride)) {
         svcMsg.setResponse(svcRes);
         if (!bServiceHasErrors) {
            String errMsg = errWarnInfoMessage.format(IdConstants.IFX_ERR_NO_RESPONSE_MESSAGE_FROM_SERVICE);
            Status errStatus = StatusHelper.createStatus(
                    IdConstants.IFX_ERR_NO_RESPONSE_MESSAGE_FROM_SERVICE,
                    StatusHelper.IFX_SEV_ERROR, errMsg);
            svcMsg.setStatus(errStatus);
         }
         return;
      } else if (rsMsg.getObject() instanceof List) {// Check if the response
         // is a list of XBO;
         List<XBO> xboList = (List) rsMsg.getObject();
         svcRes.getXBO().addAll(xboList);
         svcRq.getMsg().setResponse(svcRes);
      } else {
         XBO xbo = (XBO) rsMsg.getObject();
         Vector<XBO> xboList = new Vector<XBO>();
         xboList.add(xbo);
         svcRes.getXBO().addAll(xboList);
         svcRq.getMsg().setResponse(svcRes);
      }

      //Check to see if service has RecCtrlOut object populated
      RecCtrlOut recCtrlOut = (RecCtrlOut) ctx.getAttribute(RecCtrlOut.class.getSimpleName());
      if (recCtrlOut != null) {
         svcMsg.setRecCtrlOut(recCtrlOut);
      }

   }
   
	protected List<XBOStatus> getXBOStatus(MsgRequest svcRq){
		ComponentContext ctx = svcRq.getComponentContext();
		List<XBOStatus> beanStatusList = (List<XBOStatus>) ctx.getAttribute("XBOStatusList");
		return beanStatusList;
	}   
}
