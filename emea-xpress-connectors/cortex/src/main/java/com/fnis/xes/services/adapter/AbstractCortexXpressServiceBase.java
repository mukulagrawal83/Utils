package com.fnis.xes.services.adapter;

import com.fisglobal.emea.xpress.service.AbstractXpressServiceBase;
import com.fisglobal.emea.xpress.service.ServiceContext;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.xes.ifx.interfacepoint.cortex.CortexWebServiceClient;

import java.util.List;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 10/02/14
 * Time: 16:05
 */
public abstract class AbstractCortexXpressServiceBase extends AbstractXpressServiceBase {
    protected CortexWebServiceClient cortexWebServiceClient;
    //created for interim solution for the connectivity issue
    //TODO: Remove once the fix is in place
    protected String byPassCortex;

    public String getByPassCortex() {
        return byPassCortex;
    }

    public void setByPassCortex(String byPassCortex) {
        this.byPassCortex = byPassCortex;
    }

    public CortexWebServiceClient getCortexWebServiceClient() {
        return cortexWebServiceClient;
    }

    public void setCortexWebServiceClient(CortexWebServiceClient cortexWebServiceClient) {
        this.cortexWebServiceClient = cortexWebServiceClient;
    }


    protected abstract void executeInternal(List<ServiceContext> serviceContextList) throws Exception;

    public int getRequestUID(ServiceContext context) throws Exception{
        Service svcMsg = (Service) context.getComponentContext().getAttribute(Service.class.getName());
        return Integer.parseInt(svcMsg.getRqUID());
    }
}
