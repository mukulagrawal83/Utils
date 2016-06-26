package com.fisglobal.emea.xpress.service;

import com.fnis.ifx.xbo.v1_1.XBO;
import com.fnis.ifx.xbo.v1_1.XQO;
import com.fnis.xes.framework.component.ComponentContext;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class XpressServiceInvokingWrapper implements XpressServiceInvokingEndpoint {

    protected AbstractXpressServiceBase service;
    protected Logger log = Logger.getLogger(this.getClass().getName());
    
    private Method executeInternalMethod;
    private Class executeInternalParameterType;

    public XpressServiceInvokingWrapper() {
    }

    public XpressServiceInvokingWrapper(AbstractXpressServiceBase wrappedService) {
        setService(wrappedService);
    }

    public AbstractXpressServiceBase getService() {
        return service;
    }

   public final void setService(AbstractXpressServiceBase service) {
      try {
         this.service = service;
         executeInternalMethod = service.getClass().getDeclaredMethod("executeInternal", List.class);
         executeInternalMethod.setAccessible(true);
         executeInternalParameterType = List.class;
      } catch (NoSuchMethodException ex) {
         // fallback to the executeInternal(ServiceContext) signature
         try {            
            executeInternalMethod = service.getClass().getDeclaredMethod("executeInternal", ServiceContext.class);
            executeInternalMethod.setAccessible(true);
            executeInternalParameterType = ServiceContext.class;
         } catch (Exception e) {
            // at this point sorry no bonus, but ony log an warning
            log.warn(e);
         }
      } catch (Exception ex) {
         // should not happen
         // only log an warning
         log.warn(ex);
      }
   }

    public void invokeExternally(List<ServiceContext> serviceContextList) throws Exception {
        if (executeInternalParameterType.equals(List.class)) {
          executeInternalMethod.invoke(service, serviceContextList);
        } else if (executeInternalParameterType.equals((ServiceContext.class))) {
           executeInternalMethod.invoke(service, serviceContextList.get(0));
        } else {
           log.warn("No matching parameter type found, expected List<ServiceContext> or a ServiceContext parameter");
        }
    }

    public List<XBO> invokeExternally(XQO xqo) throws Exception {
        ServiceContext sc = new ServiceContext(xqo);
        invokeExternally(Collections.singletonList(sc));
        return sc.getResponseXboList();
    }

    public List<XBO> invokeExternally(XBO xbo) throws Exception {
        ServiceContext sc = new ServiceContext(xbo);
        invokeExternally(Collections.singletonList(sc));
        return sc.getResponseXboList();
    }

    public List<XBO> invokeExternally(XQO xqo, ComponentContext context) throws Exception {
        ServiceContext sc = new ServiceContext(xqo, context);
        invokeExternally(Collections.singletonList(sc));
        return sc.getResponseXboList();
    }

    public List<XBO> invokeExternally(XBO xbo, ComponentContext context) throws Exception {
        ServiceContext sc = new ServiceContext(xbo, context);
        invokeExternally(Collections.singletonList(sc));
        return sc.getResponseXboList();
    }
}
