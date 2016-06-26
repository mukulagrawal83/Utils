package com.fnis.xes.framework.filter.subchain;

import com.fnf.jef.boc.BocException;
import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnis.xes.framework.spring.RouteContainer;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Properties;

public class SubchainFilter implements RequestFilter {
    private static final Logger log = Logger.getLogger(SubchainFilter.class.getName());

    private RouteContainer routeContainer;

    public SubchainFilter() {
    }

    public void setSubchainRouteContainer(RouteContainer routeContainer) {
        this.routeContainer = routeContainer;
    }

    public void doFilter(RequestMessage requestMessage, ResponseMessage responseMessage, RequestLink requestLink) throws Throwable {
        if(routeContainer != null) {
            List<RequestFilter> requestFilterList = routeContainer.getFilters();
            SubchainFilterVisitor filterVisitor = new SubchainFilterVisitor(requestFilterList, requestLink);
            filterVisitor.executeFilter(requestMessage, responseMessage);
        }
    }

    public void initializeFilter(Properties properties) throws BocException {
        // do nothing - on purpose, left compatibility with legacy initialisation
    }
}