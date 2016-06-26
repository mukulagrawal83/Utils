package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fisglobal.emea.xpress.service.errorhandlers.exceptions.NoRecordsFoundException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnis.xes.services.template.XBOStatus;
import java.util.List;
import java.util.Map;

/**
 *
 * @author morel
 */
public class NoRecordsFoundExceptionHandler extends DefaultExceptionHandler {

   @Override
   public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage responseMessage, List<ServiceContext> serviceContextList) {
      if (throwable instanceof NoRecordsFoundException) {
         XBOStatus status = createXBOStatus(serviceContextList);
         addStatus(status, serviceContextList.get(0).getComponentContext());

         status.setSeverity(getDefaultSeverity());
         status.setStatusCode(getDefaultStatusCode());
         status.setHostAppId(getDefaultSpName());

         return isContinueProcessing();
      }
      return true;
   }
}
