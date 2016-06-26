package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.*;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.jef.logging.Logger;
import com.fnf.xes.framework.ServiceException;
import com.fnis.xes.framework.IdConstants;
import com.fnis.xes.framework.component.ComponentContext;
import com.fnis.xes.services.adapter.HostAdapterFactoryAbility;
import com.fnis.xes.services.adapter.XSORAdapter;

import java.sql.ResultSet;
import java.util.Properties;
import java.util.Set;

/**
 * User: e1049528 (Filip Pravica)
 * Date: 1/14/13
 */
public class SimpleDelegationFilter implements RequestFilter {
    private Logger logger = Logger.getLogger(SimpleDelegationFilter.class.getName());
    static private final String BUS_COMPONENT_NAME = "BusinessComponentName";
    private String baseBusinessComponentName;
    private Set configParamSet;
    /**
     * The BOC container in which this filter is executing.
     */
    private MutableContainer container;
    /**
     * An instance of the business component associated with this filter
     * instance.
     */
    private Object busComponent;
    /*
	 * Host AdapterFactory ability to route the request to appropriate host
	 * adapter routine
	 */
    public HostAdapterFactoryAbility _hafaAbility;



    public SimpleDelegationFilter(MutableContainer container, HostAdapterFactoryAbility hafa) {
        this.container = container;
        this._hafaAbility = hafa;
    }

    public void doFilter(RequestMessage request, ResponseMessage response, RequestLink requestLink) throws Throwable {
        processMessage(request, response);
        requestLink.doFilter(request, response);
    }

    public void initializeFilter(Properties configProperties) throws BocException {
        baseBusinessComponentName = configProperties.getProperty(BUS_COMPONENT_NAME);
        configParamSet = configProperties.entrySet();
        if (baseBusinessComponentName == null) {
            throw new ConfigException(IdConstants.DMR_NULL_BUS_COMPONENT_NAME, null, null);
        }
    }

    protected void processMessage(RequestMessage requestMessage, ResponseMessage responseMessage)
            throws Exception {
        //String businessComponentName;
        String serviceVersion = "01_01";
        Object msgPayload = requestMessage.getObject();
        // get the ComponentContext, this should have been set in a previous filter
        ComponentContext ctx = (ComponentContext) requestMessage.getProperty(ComponentContext.class.getName());

        //ctx.setAttribute("HostAdapterVersion", "01_01_01");
        //businessComponentName = baseBusinessComponentName + "_"	+ serviceVersion;
        //busComponent = registerBusinessComponent(businessComponentName);

        _hafaAbility.setHostAdapterVersion("01_01_01"); //((String)context.getAttribute("HostAdapterVersion"));
        XSORAdapter sorAdapter = (XSORAdapter) _hafaAbility.getAdapter(
        requestMessage.getMsgId(), (String) (ctx.getAttribute(ComponentContext.SP_NAME)));

        if (sorAdapter != null) {
            Object result = sorAdapter.execute(requestMessage, ctx) ;
            //busComponent.execute(requestMessage,ctx);
            responseMessage.setObject(result);
        } else {
            String msg = "Host adapter object not defined for service";
            throw new ServiceException(com.fnis.xes.services.IdConstants.ERR_HOST_ADAPTER_OBJECT, msg);
        }
    }

}
