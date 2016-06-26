package com.fisglobal.emea.xpress.service.errorhandlers;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fisglobal.emea.xpress.service.errorhandlers.exceptions.TooManyRecordsFoundException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnis.ifx.xbo.v1_1.Factory;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.services.template.XBOStatus;
import com.fnis.xes.services.util.StatusHelper;

import java.util.List;
import java.util.Map;

/**
 *
 * @author satheesh
 */
public class TooManyRecordsFoundExceptionHandler extends DefaultExceptionHandler {

   private Long additionalStatusCode = Long.valueOf(100L);
   private String additionalSeverity = "Error";


   public boolean handle(Throwable throwable, Map<String, Object> exceptionProcessingContext, RequestMessage requestMessage, ResponseMessage response, List<ServiceContext> serviceContextList) {
      if(throwable instanceof TooManyRecordsFoundException) {
         TooManyRecordsFoundException e = (TooManyRecordsFoundException)throwable;
         this.addAdditionalStatus(serviceContextList, this.additionalSeverity, this.additionalStatusCode, e.getMessage());
         return this.isContinueProcessing();
      } else {
         return true;
      }
   }

   private void addAdditionalStatus(List<ServiceContext> serviceContextList, String severity, Long statusCode, String statusDesc) {
      Status infoStatus = StatusHelper.createStatus(getAdditionalStatusCode().intValue(), "Info", "Info" );

      Status additionalStatus = (Status) Factory.create(Status.class);
      additionalStatus.setSeverity(severity);
      additionalStatus.setStatusCode(statusCode);
      additionalStatus.setStatusDesc(statusDesc);

      StatusHelper.addAdditionalStatus(infoStatus, additionalStatus);

      XBOStatus status = new XBOStatus(infoStatus);
      status.setHostAppId(this.getDefaultSpName());

      this.addStatus(status, ((ServiceContext)serviceContextList.get(0)).getComponentContext());

   }

   public Long getAdditionalStatusCode() {
      return this.additionalStatusCode;
   }

   public void setAdditionalStatusCode(Long additionalStatusCode) {
      this.additionalStatusCode = additionalStatusCode;
   }

   public String getAdditionalSeverity() {
      return this.additionalSeverity;
   }

   public void setAdditionalSeverity(String additionalSeverity) {
      this.additionalSeverity = additionalSeverity;
   }
}
