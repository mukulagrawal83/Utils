package com.fnis.xes.services.util;

import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.MsgRqHdr;
import com.fnis.xes.framework.component.ComponentContext;

public class ContextUtil {

    private ContextUtil() {
    }
    
    /**
     * @param context
     * @return SvcAdapterVersion for a context, returns null if not present
     */
    public static String getSvcAdapterVersion(ComponentContext context){
        String adapterVersion = null;
        
        MsgRqHdr msgRqHdr = ((Service) context.getAttribute(Service.class.getName())).getRequest().getMsgRqHdr();
        
        if(msgRqHdr != null && msgRqHdr.getContextRqHdr() != null && msgRqHdr.getContextRqHdr().getInterface() != null){
            adapterVersion = msgRqHdr.getContextRqHdr().getInterface().getSvcAdapterVersion();
        }
        
        return adapterVersion;
    }
}
