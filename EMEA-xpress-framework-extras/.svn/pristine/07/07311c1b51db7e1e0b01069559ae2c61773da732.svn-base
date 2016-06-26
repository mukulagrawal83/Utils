package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fisglobal.emea.xpress.service.errorhandlers.exceptions.GenericMessageErrorException;
import com.fisglobal.emea.xpress.service.errorhandlers.exceptions.GenericMessageInfoException;
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
public class GenericMessageExceptionHandler extends DefaultExceptionHandler {

   private Long errorStatusCode = 100L;
   private String errorSeverity = "Error";
   private Long infoStatusCode = 0L;
   private String infoSeverity = "Info";

   @Override
   public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage response, List<ServiceContext> serviceContextList) {

      if (throwable instanceof GenericMessageErrorException) {
         GenericMessageErrorException e = (GenericMessageErrorException) throwable;
         addAdditionalStatus(serviceContextList, errorSeverity, errorStatusCode, e.getMessage());
         if (e.getErrorCode() != null) {
            addAdditionalStatus(serviceContextList, errorSeverity, errorStatusCode, e.getErrorCode());
         }
         return isContinueProcessing();
      }
      if (throwable instanceof GenericMessageInfoException) {
         GenericMessageInfoException e = (GenericMessageInfoException) throwable;
         addAdditionalStatus(serviceContextList, infoSeverity, infoStatusCode, e.getMessage());
         if (e.getErrorCode() != null) {
            addAdditionalStatus(serviceContextList, infoSeverity, infoStatusCode, e.getErrorCode());
         }
         return isContinueProcessing();
      }
      return true;
   }

   private void addAdditionalStatus(List<ServiceContext> serviceContextList, String severity, Long statusCode, String statusDesc) {
      Status additionalStatus = (Status) Factory.create(Status.class);
      XBOStatus status = createXBOStatus(serviceContextList);
      addStatus(status, serviceContextList.get(0).getComponentContext());

      additionalStatus.setSeverity(severity);
      additionalStatus.setStatusCode(statusCode);
      additionalStatus.setStatusDesc(statusDesc);

      status.setSeverity(getDefaultSeverity());
      status.setStatusCode(getDefaultStatusCode());
      status.setHostAppId(getDefaultSpName());
      status.setStatus(additionalStatus);
   }

   /**
    * @return the errorStatusCode
    */
   public Long getErrorStatusCode() {
      return errorStatusCode;
   }

   /**
    * @param errorStatusCode the errorStatusCode to set
    */
   public void setErrorStatusCode(Long errorStatusCode) {
      this.errorStatusCode = errorStatusCode;
   }

   /**
    * @return the errorSeverity
    */
   public String getErrorSeverity() {
      return errorSeverity;
   }

   /**
    * @param errorSeverity the errorSeverity to set
    */
   public void setErrorSeverity(String errorSeverity) {
      this.errorSeverity = errorSeverity;
   }

   /**
    * @return the infoStatusCode
    */
   public Long getInfoStatusCode() {
      return infoStatusCode;
   }

   /**
    * @param infoStatusCode the infoStatusCode to set
    */
   public void setInfoStatusCode(Long infoStatusCode) {
      this.infoStatusCode = infoStatusCode;
   }

   /**
    * @return the infoSeverity
    */
   public String getInfoSeverity() {
      return infoSeverity;
   }

   /**
    * @param infoSeverity the infoSeverity to set
    */
   public void setInfoSeverity(String infoSeverity) {
      this.infoSeverity = infoSeverity;
   }
}
