package com.fisglobal.emea.xpress.router;

import com.fisglobal.emea.xpress.filter.router.RouteContainerLocator;
import com.fnf.jef.boc.RequestMessage;
import com.fnis.ifx.xbo.v1_1.Operation;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.base.ContextRqHdr;
import com.fnis.ifx.xbo.v1_1.base.OperRqHdr;

/**
 *
 * @author morel
 */
public class OperRouteContainerLocatorOperationAcceptor implements IFXOperationAcceptor {

   private String msgIdPrefix = "";
   private RouteContainerLocator routeContainerLocator;

   public boolean accepts(RequestMessage requestMessage) {

      if (!(requestMessage.getObject() instanceof Payload)
              || !(((Payload) requestMessage.getObject()).getProcess() instanceof Operation)) {
         return false;
      }

      String messageId = msgIdPrefix + ((Operation) ((Payload) requestMessage.getObject()).getProcess()).getName();
      String spName = ((Operation) ((Payload) requestMessage.getObject()).getProcess()).getOperRqHdr().getContextRqHdr().getSPName();
      ContextRqHdr contextRequestHeader = ((OperRqHdr) requestMessage.getProperty(OperRqHdr.class.getName())).getContextRqHdr();

      return routeContainerLocator.locate(messageId, spName, null, contextRequestHeader) != null;
   }

   /**
    * @return the routeContainerLocator
    */
   public RouteContainerLocator getRouteContainerLocator() {
      return routeContainerLocator;
   }

   /**
    * @param routeContainerLocator the routeContainerLocator to set
    */
   public void setRouteContainerLocator(RouteContainerLocator routeContainerLocator) {
      this.routeContainerLocator = routeContainerLocator;
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
}
