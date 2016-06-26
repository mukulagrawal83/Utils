package com.fisglobal.emea.xpress.router;

import com.fisglobal.emea.xpress.router.MessageProcessor;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.FilterVisitor;
import com.fnis.xes.framework.spring.RouteContainer;
import com.fnis.xes.services.filter.router.PayloadRouter;

/**
 *
 * @author morel
 */
public abstract class LegacyRouterWrapper implements MessageProcessor {
   
   private RouteContainer nonAggregatedMessagesMessageRoute;
   private PayloadRouter wrappedRouter;

   public void process(RequestMessage req, ResponseMessage res) throws Exception {
       FilterVisitor fv = new FilterVisitor(nonAggregatedMessagesMessageRoute.getFilters());
       wrappedRouter.process(req, res, fv);
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

   /**
    * @return the wrappedRouter
    */
   public PayloadRouter getWrappedRouter() {
      return wrappedRouter;
   }

   /**
    * @param wrappedRouter the wrappedRouter to set
    */
   public void setWrappedRouter(PayloadRouter wrappedRouter) {
      this.wrappedRouter = wrappedRouter;
   }


   
}
