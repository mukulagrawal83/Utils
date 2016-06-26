package com.fisglobal.emea.xpress.filter.router;

import com.fnis.ifx.xbo.v1_1.base.ContextRqHdr;
import com.fnis.xes.framework.spring.RouteContainer;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author morel
 */
public class RouteContainerLocatorImplementation implements RouteContainerLocator {

   private Map<String, RouteContainer> messageRoutes = new HashMap();
   private String sepatorString = "_";

   public RouteContainerLocatorImplementation(Map<String, RouteContainer> messageRoutes) {
      this.messageRoutes = messageRoutes;
   }
   
   public RouteContainer locate(String messageId, String spName, String actionName, ContextRqHdr contextRequestHeader) {

      String version = null;
      StringBuilder versionStringBuilder = new StringBuilder();

      if (contextRequestHeader != null && contextRequestHeader.getInterface() != null) {
         if ((version = contextRequestHeader.getInterface().getInterfaceVersion()) != null) {
            versionStringBuilder.append(version);
            if ((version = contextRequestHeader.getInterface().getSvcVersion()) != null) {
               versionStringBuilder.append(sepatorString).append(version);
               if ((version = contextRequestHeader.getInterface().getSvcAdapterVersion()) != null) {
                  versionStringBuilder.append(sepatorString).append(version);
               }
            }
            version = versionStringBuilder.toString();
         }
      }

      RouteContainer msgRoute = null;

      if (StringUtils.isNotEmpty(version) && (msgRoute = messageRoutes.get(messageId + sepatorString + spName + sepatorString + ((actionName != null) ? (actionName + sepatorString) : "") + version)) != null) {
      } else if (StringUtils.isNotEmpty(actionName) && (msgRoute = messageRoutes.get(messageId + sepatorString + spName + sepatorString + actionName)) != null) {
      } else if (StringUtils.isNotEmpty(spName) && (msgRoute = messageRoutes.get(messageId + sepatorString + spName)) != null) {
      } else if ((msgRoute = messageRoutes.get(messageId)) != null) {
      } else {
         // no route found, sorry no bonus
      }

      return msgRoute;
   }

   /**
    * @return the sepatorString
    */
   public String getSepatorString() {
      return sepatorString;
   }

   /**
    * @param sepatorString the sepatorString to set
    */
   public void setSepatorString(String sepatorString) {
      this.sepatorString = sepatorString;
   }

   /**
    * @return the messageRoutes
    */
   public Map<String, RouteContainer> getMessageRoutes() {
      return messageRoutes;
   }

   /**
    * @param messageRoutes the messageRoutes to set
    */
   public void setMessageRoutes(Map<String, RouteContainer> messageRoutes) {
      this.messageRoutes = messageRoutes;
   }
}
