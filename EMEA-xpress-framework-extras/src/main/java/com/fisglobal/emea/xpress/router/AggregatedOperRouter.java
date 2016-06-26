package com.fisglobal.emea.xpress.router;

import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.FilterVisitor;
import com.fnis.ifx.xbo.v1_1.Operation;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.ResponseImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.RecCtrlIn;
import com.fnis.ifx.xbo.v1_1.base.Status;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.spring.RouteContainer;
import com.fnis.xes.services.IdConstants;
import com.fnis.xes.services.errormapping.ErrorMappingAbility;
import com.fnis.xes.services.template.XBOStatus;
import com.fnis.xes.services.util.StatusHelper;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;

/**
 *
 * @author morel
 */
public class AggregatedOperRouter implements MessageProcessor {

   protected Logger log = Logger.getLogger(this.getClass().getName());
   private String msgIdPrefix = "";
   private RouteContainer nonAggregatedMessagesMessageRoute;
   private ErrorMappingAbility errorMappingAbility;
   private List<IFXOperationAcceptor> supportedOperations = new ArrayList();

   public AggregatedOperRouter(List<IFXOperationAcceptor> supportedOperations) {
      this.supportedOperations = supportedOperations;
   }

   public void process(RequestMessage requestMessage, ResponseMessage responseMessage) throws Throwable {

      Payload originalPayload = (Payload) requestMessage.getObject();

      prepareOperRequest(requestMessage, responseMessage);

      delegateToServiceViaMessageRoute(requestMessage, responseMessage);

      processResponse(requestMessage, responseMessage, originalPayload);

   }

   protected void processResponse(RequestMessage requestMessage, ResponseMessage responseMessage, Payload originalPayload) {

      List<ServiceContext> serviceContextList = (List<ServiceContext>) responseMessage.getObject();
      Operation oprMsg = (Operation) originalPayload.getProcess();
      boolean hasErrors = false;

      List<XBOStatus> lumpedXboStatuses = new ArrayList();

      for (ServiceContext currentServiceContext : serviceContextList) {
         List<XBOStatus> xboStatusList = (List<XBOStatus>) currentServiceContext.getComponentContext().getAttribute("XBOStatusList");
         if (xboStatusList != null) {
            for (XBOStatus s : xboStatusList) {
               hasErrors |= (s.getStatusCode() != 0 && StatusHelper.IFX_SEV_ERROR.equals(s.getSeverity()));
            }
            lumpedXboStatuses.addAll(xboStatusList);
         }
      }

      // main oper status
      Status rootStatus = hasErrors ? 
              StatusHelper.createStatus(IdConstants.IFX_GENERAL_ERROR_NO_DATA, StatusHelper.IFX_SEV_ERROR, "Operation failed") :
              StatusHelper.createSuccessStatus();
      rootStatus = StatusHelper.addStatus(rootStatus, lumpedXboStatuses, errorMappingAbility);
      oprMsg.setRqUID(UUID.randomUUID().toString());
      oprMsg.setStatus(rootStatus);

      // idividual statuses for each xbo - not processed or success status for each one
      for (int i = 0; i < oprMsg.getServices().size(); i++) {
         ResponseImpl responseImpl = new ResponseImpl();
         ServiceContext serviceContext = serviceContextList.get(i);
         responseImpl.getXBO().addAll(serviceContext.getResponseXboList());
         Service svcMsg = (Service) oprMsg.getServices().get(i);
         svcMsg.setStatus(hasErrors ? StatusHelper.createNotProcessedStatus() : StatusHelper.createSuccessStatus());
         svcMsg.setResponse(responseImpl);
      }
   }

   protected void prepareOperRequest(RequestMessage requestMessage, ResponseMessage responseMessage) {

      String operName = getOperName(requestMessage);
      requestMessage.setMsgId(msgIdPrefix + operName);

      Operation oprMsg = (Operation) ((Payload) requestMessage.getObject()).getProcess();
      List<ServiceContext> serviceContextList = new LinkedList();

      List<ComponentContext> compCtxList =
              (List<ComponentContext>) requestMessage.getProperty(ComponentContext.class.getName() + "List");

      for (int i = 0; i < oprMsg.getServices().size(); i++) {
         Service svcMsg = (Service) oprMsg.getServices().get(i);
         ComponentContext ctx = compCtxList.get(i);
         ctx.setAttribute(Service.class.getName(), svcMsg);
         RecCtrlIn recCtrlIn = svcMsg.getRecCtrlIn();
         if (recCtrlIn != null) {
            ctx.setAttribute(RecCtrlIn.class.getSimpleName(), recCtrlIn);
         }
         serviceContextList.add(new ServiceContext(getXBOorXQOFromService(svcMsg), ctx));
      }
      requestMessage.setObject(serviceContextList);
      requestMessage.setProperty("IFX_SERVICE_NAME", operName);
   }

   public boolean accepts(RequestMessage requestMessage) {

      if (supportedOperations.isEmpty()) {
         log.warn("There are no supported operations configured!");
         return false;
      }

      if ((requestMessage.getObject() instanceof Payload)
              && (((Payload) requestMessage.getObject()).getProcess() instanceof Operation)) {

         for (IFXOperationAcceptor metaData : supportedOperations) {
            if (metaData.accepts(requestMessage)) {
               return true;
            }
         }
      }
      return false;
   }

   protected void delegateToServiceViaMessageRoute(RequestMessage requestMessage, ResponseMessage responseMessage) throws Throwable {
      FilterVisitor fv = new FilterVisitor(nonAggregatedMessagesMessageRoute.getFilters());
      fv.doFilter(requestMessage, responseMessage);
   }

   protected String getOperName(RequestMessage requestMessage) {
      return ((Operation) ((Payload) requestMessage.getObject()).getProcess()).getName();
   }

   protected String getSpName(RequestMessage requestMessage) {
      return ((Operation) ((Payload) requestMessage.getObject()).getProcess()).getOperRqHdr().getContextRqHdr().getSPName();
   }

   /**
    * @return the msgIdPrefix
    */
   public String getMsgIdPrefix() {
      return msgIdPrefix;
   }

   /**
    * @param msgIdPrefix the msgIdPrefix to set
    */
   public void setMsgIdPrefix(String msgIdPrefix) {
      this.msgIdPrefix = msgIdPrefix;
   }

   /**
    * @return the nonAggregatedMessagesMessageRoute
    */
   public RouteContainer getNonAggregatedMessagesMessageRoute() {
      return nonAggregatedMessagesMessageRoute;
   }

   /**
    * @param nonAggregatedMessagesMessageRoute the nonAggregatedMessagesMessageRoute to set
    */
   public void setNonAggregatedMessagesMessageRoute(RouteContainer nonAggregatedMessagesMessageRoute) {
      this.nonAggregatedMessagesMessageRoute = nonAggregatedMessagesMessageRoute;
   }

   protected Object getXBOorXQOFromService(Service svcMsg) {

      if (svcMsg.getRequest() != null) {
         if (svcMsg.getRequest().getXBO() != null) {
            return svcMsg.getRequest().getXBO();
         } else {
            return svcMsg.getRequest().getXQO();
         }
      }
      return null;
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
    * @return the supportedOperations
    */
   public List<IFXOperationAcceptor> getSupportedOperations() {
      return supportedOperations;
   }

   /**
    * @param supportedOperations the supportedOperations to set
    */
   public void setSupportedOperations(List<IFXOperationAcceptor> supportedOperations) {
      this.supportedOperations = supportedOperations;
   }
}
