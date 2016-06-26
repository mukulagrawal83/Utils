package com.fisglobal.emea.xpress.filter.router;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.ConfigException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.StatusMsgConstants;
import com.fnf.jef.boc.filter.FilterVisitor;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.ContextRqHdr;
import com.fnis.ifx.xbo.v1_1.base.OperRqHdr;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.framework.spring.RouteContainer;
import java.util.Properties;

/**
 *
 * @author morel
 */
public class BetterDelegatedRouteProcessor implements RequestFilter {

   private RouteContainerLocator routeContainerLocator;

   public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {

      String trgMsgId = requestMessage.getMsgId();
      String ifxActionName = null;
      String spName = "";
      Service svcMsg = null;

      ComponentContext ctx = (ComponentContext) requestMessage.getProperty(ComponentContext.class.getName());

      ContextRqHdr contextRqHeader = null;

      if (ctx != null) {
         spName = (String) ctx.getAttribute(ComponentContext.SP_NAME);
         svcMsg = (Service) (ctx.getAttribute(Service.class.getName()));
         contextRqHeader = (ContextRqHdr) svcMsg.getRequest().getMsgRqHdr().getContextRqHdr();
         ifxActionName = svcMsg.getAction().toLowerCase();
      } else {
         contextRqHeader = ((OperRqHdr) requestMessage.getProperty(OperRqHdr.class.getName())).getContextRqHdr();
         spName = contextRqHeader.getSPName();
      }

      RouteContainer msgroute =
              routeContainerLocator.locate(trgMsgId, spName, ifxActionName, contextRqHeader);
      if (msgroute == null) {
         throw new ConfigException(StatusMsgConstants.CONFIG_BOC_NO_MSG_ROUTE, requestMessage.getMsgId(), null);
      }

      FilterVisitor fv = new FilterVisitor(msgroute.getFilters());
      fv.doFilter(requestMessage, responseMessage);
      requestLink.doFilter(requestMessage, responseMessage);
   }

   public void initializeFilter(Properties prprts) throws BocException {
      // ignored, empty on purpose
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
}
