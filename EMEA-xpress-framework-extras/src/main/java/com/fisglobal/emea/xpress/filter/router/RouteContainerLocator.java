package com.fisglobal.emea.xpress.filter.router;

import com.fnis.ifx.xbo.v1_1.base.ContextRqHdr;
import com.fnis.xes.framework.spring.RouteContainer;

/**
 *
 * @author morel
 */
public interface RouteContainerLocator {
   
   public RouteContainer locate(String messageId, String spName, String actionName, ContextRqHdr contextRequestHeader);
   
}
