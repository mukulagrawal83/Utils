package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.ifx.xbo.v1_1.PayloadImpl;
import com.fnis.ifx.xbo.v1_1.Service;
import com.fnis.ifx.xbo.v1_1.base.Interface;
import com.fnis.ifx.xbo.v1_1.base.InterfaceImpl;
import org.apache.log4j.Logger;

import java.util.Properties;

public class HostAdapterVersionInjectorFilter implements RequestFilter {

    private static final String CLASS_NAME = HostAdapterVersionInjectorFilter.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);
    private Properties adapterProperties;

    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {
        log.debug("Changing HostAdapterVersion");
        if (adapterProperties != null && requestMessage.getObject() instanceof Payload) {
            PayloadImpl payload = (PayloadImpl) requestMessage.getObject();
            if(payload.getProcess() instanceof Service){
                Service service = (Service) payload.getProcess();

                String interfaceVersion = service.getName() + ".interfaceVersion";
                String svcVersion = service.getName() + ".svcVersion";
                String svcAdapterVersion = service.getName() + ".svcAdapterVersion";
                String interfaceVersionWithAction = service.getName() + "." + service.getAction() + ".interfaceVersion";
                String svcVersionWithAction = service.getName() + "." + service.getAction() + ".svcVersion";
                String svcAdapterVersionWithAction = service.getName() + "." + service.getAction() + ".svcAdapterVersion";

                if(service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface() == null) {
                    Interface interfaceImpl = new InterfaceImpl();
                    service.getRequest().getMsgRqHdr().getContextRqHdr().setInterface(interfaceImpl);
                }

                if (adapterProperties.containsKey(interfaceVersionWithAction)) {
                    service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface().setInterfaceVersion(adapterProperties.getProperty(interfaceVersionWithAction));
                } else if (adapterProperties.containsKey(interfaceVersion)) {
                    service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface().setInterfaceVersion(adapterProperties.getProperty(interfaceVersion));
                }
                if (adapterProperties.containsKey(svcVersionWithAction)) {
                    service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface().setSvcVersion(adapterProperties.getProperty(svcVersionWithAction));
                } else if (adapterProperties.containsKey(svcVersion)) {
                    service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface().setSvcVersion(adapterProperties.getProperty(svcVersion));
                }
                if (adapterProperties.containsKey(svcAdapterVersionWithAction)) {
                    service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface().setSvcAdapterVersion(adapterProperties.getProperty(svcAdapterVersionWithAction));
                } else if (adapterProperties.containsKey(svcAdapterVersion)) {
                    service.getRequest().getMsgRqHdr().getContextRqHdr().getInterface().setSvcAdapterVersion(adapterProperties.getProperty(svcAdapterVersion));
                }
            }

        }
        log.debug("End of changing HostAdapterVersion");
        requestLink.doFilter(requestMessage, responseMessage);
    }

    public void initializeFilter(Properties props) throws BocException {
    }

    public void setAdapterProperties(Properties properties) {
        this.adapterProperties = properties;
    }
}
