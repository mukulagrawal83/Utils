package com.fnis.xes.framework.filter.subchain;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import org.apache.log4j.Logger;

import java.util.Iterator;
import java.util.List;

public class SubchainFilterVisitor implements RequestLink {
    private static final Logger log = Logger.getLogger(SubchainFilterVisitor.class.getName());

    private List filterList;
    private RequestLink parentRequestLink;
    private Iterator<? extends RequestFilter> iterator;


    public SubchainFilterVisitor(List<? extends RequestFilter> filterList, RequestLink parentRequestLink) {
        this.filterList = filterList;
        this.iterator = filterList.iterator();
        this.parentRequestLink = parentRequestLink;
    }

    public void doFilter(RequestMessage request, ResponseMessage response)
            throws Throwable {
        try {
            if (iterator.hasNext()) {
                RequestFilter filter = iterator.next();
                filter.doFilter(request, response, this);
            } else {
                parentRequestLink.doFilter(request, response);
            }
        } finally {
            iterator = filterList.iterator();
        }
    }

    public void executeFilter(RequestMessage requestMessage, ResponseMessage responseMessage) throws Throwable {
        iterator = filterList.iterator();
        try {
            doFilter(requestMessage, responseMessage);
        } finally {
            iterator = null;
        }
    }

}
