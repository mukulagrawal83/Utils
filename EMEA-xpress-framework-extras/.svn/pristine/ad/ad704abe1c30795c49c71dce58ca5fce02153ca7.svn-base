package com.fisglobal.emea.xpress.service;

import com.fnis.ifx.xbo.v1_1.XBO;
import com.fnis.ifx.xbo.v1_1.XQO;
import com.fnis.xes.framework.component.ComponentContext;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author morel
 */
public class ServiceContext {

   private XBO requestXbo = null;
   private XQO requestXqo = null;
   private List<XBO> responseXboList = new ArrayList<XBO>();
   private ComponentContext componentContext;

   public ServiceContext() {
   }

   public ServiceContext(Object toBeWrapped) {
      setWrappedRequestObject(toBeWrapped);
   }

   public ServiceContext(Object toBeWrapped, ComponentContext componentContext) {
      setWrappedRequestObject(toBeWrapped);
      this.componentContext = componentContext;
   }

   public XBO getRequestXbo() {
      return requestXbo;
   }

   /**
    * @param xbo the xbo to set
    */
   public void setRequestXbo(XBO xbo) {
      this.requestXbo = xbo;
   }

   /**
    * @return the xqo
    */
   public XQO getRequestXqo() {
      return requestXqo;
   }

   /**
    * @param xqo the xqo to set
    */
   public void setRequestXqo(XQO xqo) {
      this.requestXqo = xqo;
   }

   /**
    * @return the componentContext
    */
   public ComponentContext getComponentContext() {
      return componentContext;
   }

   /**
    * @param componentContext the componentContext to set
    */
   public void setComponentContext(ComponentContext componentContext) {
      this.componentContext = componentContext;
   }

   public boolean isXQOWrapper() {
      return requestXqo != null;
   }

   public boolean isXBOWrapper() {
      return requestXbo != null;
   }

   public Object getWrappedRequestObject() {
      return isXQOWrapper() ? requestXqo : (isXBOWrapper() ? requestXbo : null);
   }

   public final void setWrappedRequestObject(Object toBewrapped) {
      if (toBewrapped instanceof XQO) {
         setRequestXqo((XQO) toBewrapped);
      } else if (toBewrapped instanceof XBO) {
         setRequestXbo((XBO) toBewrapped);
      } else {
         throw new IllegalArgumentException("Either XQO or XBO instance was expected, instead got " + toBewrapped != null ? toBewrapped.getClass().getName() : " null");
      }
   }

   public void addResponseXbo(XBO xbo) {
      responseXboList.add(xbo);
   }
   
   /**
    * @return the responseXboList
    */
   public List<XBO> getResponseXboList() {
      return responseXboList;
   }

   /**
    * @param responseXboList the responseXboList to set
    */
   
   public void setResponseXboList(List<XBO> responseXboList) {
      this.responseXboList = responseXboList;
   }
}
