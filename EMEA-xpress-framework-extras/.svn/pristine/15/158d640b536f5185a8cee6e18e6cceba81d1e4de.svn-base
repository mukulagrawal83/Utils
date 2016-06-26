package com.fnis.xes.framework.filter;

import com.fnf.jef.boc.RequestMessage;
import com.fnf.jef.boc.ResponseMessage;
import com.fnf.xes.framework.ServiceException;
import com.fnis.ifx.xbo.v1_1.*;
import com.fnis.ifx.xbo.v1_1.Process;
import com.fnis.xes.framework.errorhandlers.ErrorHandler;
import com.fnis.xes.framework.ext.processxbo.subject.xbo.XBOTransformSubject;
import com.fnis.xes.framework.filter.converters.ContextMessageType;
import com.fnis.xes.framework.filter.converters.ExternalLookupException;
import com.fnis.xes.framework.filter.converters.PartyKeysConversionContext;
import com.fnis.xes.framework.filter.converters.PartyKeysFormatType;
import org.apache.log4j.Logger;

/**
 * @author e1050475
 */
public class PartyKeysXBOTransformationFilter extends GenericXBOTransformationFilter {

    private static final Logger log = Logger.getLogger(PartyKeysXBOTransformationFilter.class);
    private PartyKeysFormatType defaultResponsePartyKeysFormat;

    public void setDefaultResponsePartyKeysFormat(PartyKeysFormatType defaultResponsePartyKeysFormat) {
        this.defaultResponsePartyKeysFormat = defaultResponsePartyKeysFormat;
    }

    public PartyKeysFormatType getDefaultResponsePartyKeysFormat() {
        return defaultResponsePartyKeysFormat;
    }

    @Override
    protected Object handleRequest(RequestMessage rq, Object passedCtx) throws ServiceException {
        PartyKeysConversionContext ctx = new PartyKeysConversionContext();
        ctx.setMessageType(ContextMessageType.REQUEST);

        XBOTransformSubject transformSubject;

        Object obj = rq.getObject();

        log.debug("Received request object payload: " + obj.getClass().getName());

        if (rq.getObject() instanceof Payload) {
            log.debug("Preparing to transform: " + obj.getClass().getName());
            try {
                Process payloadProcess  = ((Payload)rq.getObject()).getProcess();
                if(payloadProcess instanceof Operation) {
                    for(Service service : ((Operation)payloadProcess).getServices()){
                        PayloadImpl payload = new PayloadImpl();
                        payload.setProcess(service);
                        transformSubject = new XBOTransformSubject(payload, ctx);
                        log.debug("Preparing to transform: " + transformSubject);
                        log.debug("Processing with transformer: " + getRqTransformer());
                        getRqTransformer().transform(transformSubject);
                    }
                } else {
                    transformSubject = new XBOTransformSubject((Payload) rq.getObject(), ctx);
                    log.debug("Preparing to transform: " + transformSubject);
                    log.debug("Processing with transformer: " + getRqTransformer());
                    getRqTransformer().transform(transformSubject);
                }
                //} catch (ExternalLookupException e) {
                // TODO: should be implemented as ErrorHandler. Currently implemented to provide backward compatibility!
                //throw new ServiceException(2300, "External lookup exception occurred...", e);
            } catch (Exception e) {
                for (ErrorHandler<Exception,ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e,rq);
                }
            }
        } else {
            log.debug("Skipping object " + obj.getClass());
        }

        return ctx;
    }

    @Override
    protected Object handleResponse(ResponseMessage rs, Object passedCtx) throws ServiceException {
        XBOTransformSubject transformSubject;
        PartyKeysConversionContext ctx = (PartyKeysConversionContext) passedCtx;

        setDefaultIncomingPartyKeysFormatIfNotDefined(ctx);

        ctx.setMessageType(ContextMessageType.RESPONSE);

        log.debug("Received response object payload: " + rs.getObject().getClass().getName());

        if (rs.getObject() instanceof Payload) {
            try {
                Process payloadProcess  = ((Payload)rs.getObject()).getProcess();
                if(payloadProcess instanceof Operation) {
                    for(Service service : ((Operation)payloadProcess).getServices()){
                        PayloadImpl payload = new PayloadImpl();
                        payload.setProcess(service);
                        transformSubject = new XBOTransformSubject(payload, ctx);
                        getRsTransformer().transform(transformSubject);
                    }
                } else {
                    transformSubject = new XBOTransformSubject((Payload) rs.getObject(), ctx);
                    getRsTransformer().transform(transformSubject);
                }
            } catch (Exception e) {
                for (ErrorHandler<Exception,ServiceException> eh : getErrorHandlers()) {
                    eh.handle(e,rs);
                }
            }
        }

        return ctx;
    }

    private void setDefaultIncomingPartyKeysFormatIfNotDefined(PartyKeysConversionContext ctx) {
        if(ctx.getIncomingPartyKeysFormat() == null)
            ctx.setIncomingPartyKeysFormat(defaultResponsePartyKeysFormat);
    }

}
