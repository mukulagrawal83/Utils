package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.jef.boc.filter.RequestFilter;
import com.fnf.jef.boc.filter.RequestLink;
import com.fnf.xes.framework.ServiceException;
import com.fnis.ifx.xbo.v1_1.Payload;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.ext.processxbo.builder.Transformer;
import com.fnis.xes.framework.ext.processxbo.builder.TransformerBuilder;
import com.fnis.xes.framework.ext.processxbo.subject.xbo.XBOTransformSubject;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GenericXBOTransformationFilter implements RequestFilter {

    private static final String CLASS_NAME = GenericXBOTransformationFilter.class.getName();
    private static Logger log = Logger.getLogger(CLASS_NAME);

    private String[] requestPathSelectors;
    private String[] responsePathSelectors;

    private Transformer rqTransformer;
    private Transformer rsTransformer;

    private TransformCallback rqCallback;
    private TransformCallback rsCallback;

    private List<? extends ErrorHandler<Exception,ServiceException>> errorHandlers = new ArrayList();

    public void doFilter(RequestMessage rq, ResponseMessage rs, RequestLink requestLink) throws Throwable {
        Object context = null;

        log.debug("Entering XBOTransformationFilter");

        if (rqTransformer != null) {
            context = handleRequest(rq, context);
        } else {
            log.debug("Request transform processing list is empty - skipping!");
        }

        requestLink.doFilter(rq, rs);

        if (rsTransformer != null) {
            context = handleResponse(rs, context);
        } else {
            log.debug("Response transform processing list is empty - skipping!");
        }
    }

    public void initializeFilter(Properties props) throws com.fnf.jef.boc.BocException {
        log.info("Initializing GenericXBO transformation filter.");
        initializeRequestTransformer();
        initializeResponseTransformer();
    }

    public String[] getRequestPathSelectors() {
        return requestPathSelectors;
    }

    public void setRequestPathSelectors(String[] requestPathSelectors) {
        this.requestPathSelectors = requestPathSelectors;
    }

    public String[] getResponsePathSelectors() {
        return responsePathSelectors;
    }

    public void setResponsePathSelectors(String[] responsePathSelectors) {
        this.responsePathSelectors = responsePathSelectors;
    }

    public TransformCallback getRequestCallback() {
        return rqCallback;
    }

    public void setRequestCallback(TransformCallback rqCallback) {
        this.rqCallback = rqCallback;
    }

    public TransformCallback getResponseCallback() {
        return rsCallback;
    }

    public void setResponseCallback(TransformCallback rsCallback) {
        this.rsCallback = rsCallback;
    }

    public Transformer getRqTransformer() {
        return rqTransformer;
    }

    public Transformer getRsTransformer() {
        return rsTransformer;
    }

    public TransformCallback getRqCallback() {
        return rqCallback;
    }

    public TransformCallback getRsCallback() {
        return rsCallback;
    }


    protected List<? extends ErrorHandler<Exception, ServiceException>> getErrorHandlers() {
        return errorHandlers;
    }

    public void setErrorHandlers(List<? extends ErrorHandler<Exception, ServiceException>> errorHandlers){
        this.errorHandlers = errorHandlers;
    }

    protected Object handleRequest(RequestMessage rq, Object ctx) throws ServiceException {
        String myContext = "REQUEST";
        XBOTransformSubject transformSubject;

        Object obj = rq.getObject();

        log.debug("Received request object payload: " + obj.getClass().getName());

        if (rq.getObject() instanceof Payload) {
            log.debug("Preparing to transform: " + obj.getClass().getName());
            try {
                transformSubject = new XBOTransformSubject((Payload) rq.getObject(), ctx);
                log.debug("Preparing to transform: " + transformSubject);
                log.debug("Processing with transformer: " + rqTransformer);
                rqTransformer.transform(transformSubject);
            } catch (Exception e) {
                for (ErrorHandler<Exception,ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e,rq);
                }
            }
        } else {
            log.debug("Skipping object " + obj.getClass());
        }

        return myContext;
    }

    protected Object handleResponse(ResponseMessage rs, Object ctx) throws ServiceException {
        XBOTransformSubject transformSubject;

        log.debug("Received response object payload: " + rs.getObject().getClass().getName());

        if (rs.getObject() instanceof Payload) {
            try {
                transformSubject = new XBOTransformSubject((Payload) rs.getObject(), ctx);
                rsTransformer.transform(transformSubject);
            } catch (Exception e) {
                for (ErrorHandler<Exception,ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e,rs);
                }
            }
        }

        return ctx;
    }

    private void initializeRequestTransformer() {
        if (rqCallback == null) {
            log.debug("Request callback is not initialized. Can't initialize request XBO transformation.");
            return;
        }

        if (requestPathSelectors != null && requestPathSelectors.length > 0) {
            TransformerBuilder transformerBuilderRq = TransformerBuilder.create();

            for (String pathSelector : requestPathSelectors) {
                log.debug("Adding path [" + pathSelector + "] for REQUEST processing queue");
                transformerBuilderRq = transformerBuilderRq.transform(pathSelector, rqCallback);
            }

            rqTransformer = transformerBuilderRq.createTransformer();
            rqTransformer.validate(XBOTransformSubject.class);
        }
    }

    private void initializeResponseTransformer() {
        if (rsCallback == null) {
            log.debug("Response callback is not initialized. Can't initialize response XBO transformation.");
            return;
        }

        if (responsePathSelectors != null && responsePathSelectors.length > 0) {
            TransformerBuilder transformerBuilderRs = TransformerBuilder.create();

            for (String pathSelector : responsePathSelectors) {
                log.debug("Adding path [" + pathSelector + "] for RESPONSE processing queue");
                transformerBuilderRs = transformerBuilderRs.transform(pathSelector, rsCallback);
            }

            rsTransformer = transformerBuilderRs.createTransformer();
            rsTransformer.validate(XBOTransformSubject.class);
        }
    }

}
