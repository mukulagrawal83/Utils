package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fisglobal.emea.xpress.service.errorhandlers.exceptions.InputValidationException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.services.template.XBOStatus;
import java.util.List;
import java.util.Map;

/**
 *
 * @author morel
 */
public class InputValidationExceptionHandler extends DefaultExceptionHandler {

   @Override
   public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage response, List<ServiceContext> serviceContextList) {

      if (throwable instanceof InputValidationException) {

         InputValidationException e = (InputValidationException) throwable;

         Status additionalStatus = (Status) Factory.create(Status.class);
         additionalStatus.setSeverity(getDefaultSeverity());
         additionalStatus.setStatusCode(getDefaultStatusCode());
         additionalStatus.setStatusDesc(e.getMessage());
         
         XBOStatus status = createXBOStatus(serviceContextList);
         addStatus(status, serviceContextList.get(0).getComponentContext());

         status.setSeverity(getDefaultSeverity());
         status.setStatusCode(getDefaultStatusCode());
         status.setHostAppId(getDefaultSpName());
         status.setStatus(additionalStatus);
         
         return isContinueProcessing();
      }
      return true;
   }
}
