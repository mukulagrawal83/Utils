package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author morel
 */
public class XBOResponseSettingExceptionHandler implements ExceptionHandler {

   private Logger log = Logger.getLogger(this.getClass().getName());
   private String opersMsgIdPrefix = "opers.";
   
   public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage responseMessage, List<ServiceContext> serviceContextList) {
      
      if (serviceContextList.isEmpty()) {
         log.warn("There are no ServiceContext objects, can't do responseMessage.setObject()!");
         return true;
      }
      
      if (requestMessage.getMsgId() == null) {
         log.error("requestMessage.getMsgId() is null, impossible!");
         throw new RuntimeException("requestMessage.getMsgId() is null, impossible!");
      }
      
      List responseList = null;
      // this service call must have originated via core MsgRouter because there is only one ServiceContext object
      // even if there's just one element, do second check for the opers prefix
      if (serviceContextList.size() == 1 && !requestMessage.getMsgId().startsWith(opersMsgIdPrefix)) {
         responseList = serviceContextList.get(0).getResponseXboList();
      } else {
      // and this is for opers - this is routed via new AggregatedOperRouter, so return List<ServiceContext>   
         responseList = serviceContextList;
      }
      responseMessage.setObject(responseList);
      return true;
   }

   /**
    * @return the opersMsgIdPrefix
    */
   public String getOpersMsgIdPrefix() {
      return opersMsgIdPrefix;
   }

   /**
    * @param opersMsgIdPrefix the opersMsgIdPrefix to set
    */
   public void setOpersMsgIdPrefix(String opersMsgIdPrefix) {
      this.opersMsgIdPrefix = opersMsgIdPrefix;
   }
}
