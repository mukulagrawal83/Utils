package com.fisglobal.emea.xpress.service;

import com.fnis.ifx.xbo.v1_1.XBO;
import com.fnis.ifx.xbo.v1_1.XQO;
import com.fnis.xes.framework.component.ComponentContext;
import java.util.List;

public interface XpressServiceInvokingEndpoint {

    public void invokeExternally(List<ServiceContext> serviceContextList) throws Exception;

    public List<XBO> invokeExternally(XQO xqo) throws Exception;

    public List<XBO> invokeExternally(XBO xbo) throws Exception;

    public List<XBO> invokeExternally(XQO xqo, ComponentContext context) throws Exception;

    public List<XBO> invokeExternally(XBO xbo, ComponentContext context) throws Exception;
}
