package com.fnis.xes.framework.filter.callbacks;

import com.fnis.ifx.xbo.v1_1.base.AcctIdent;
import com.fnis.ifx.xbo.v1_1.base.AcctIdentImpl;
import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.ifx.xbo.v1_1.base.AcctKeysImpl;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;
import com.fnis.xes.framework.filter.converters.AcctKeysConverter;
import com.fnis.xes.framework.filter.converters.AcctKeysConversionContext;
import com.fnis.xes.framework.filter.converters.AcctKeysFormatDetector;
import com.fnis.xes.framework.filter.converters.AcctKeysFormatType;
import org.apache.log4j.Logger;

import java.util.List;

public class CanonicalAcctKeyTransformCallback implements TransformCallback {
    private static final Logger log = Logger.getLogger(CanonicalAcctKeyTransformCallback.class);
    private AcctKeysFormatDetector acctKeysFormatDetector;
    private List<AcctKeysConverter> acctKeysConverters;

    public Object transform(Object obj, Object providedCtx) {
        log.debug("Entered transform()");

        AcctKeysConversionContext ctx = (AcctKeysConversionContext) providedCtx;
        log.debug("Provided context message: " + ctx.getMessageType());

        switch(ctx.getMessageType()) {
            case REQUEST:
                obj = processRequest(obj, ctx);
                break;
            case RESPONSE:
                obj = processResponse(obj, ctx);
                break;
            default:
                throw new UnsupportedOperationException("Unknown message type in provided context.");
        }

        log.debug("Leaving transform()");
        return obj;
    }

    private AcctKeysConverter findFirstRegisteredConverter(AcctKeysFormatType from, AcctKeysFormatType to) {
        for(AcctKeysConverter converter : acctKeysConverters) {
            if(converter.handles(from, to)) {
                return converter;
            }
        }
        log.debug("Could not find any AcctKeys converter do handle conversion: " + from + " -> " + to);
        return null;
    }

    public Boolean handles(Class clazz, Class ctx) {
        if(clazz == null || ctx == null) {
            log.debug("Either value class or ctx class is null. So callback does not handle this situation.");
            return false;
        }
        // TODO: better to use clazz.isAssignableFrom for proper context handling
        if(clazz.equals(AcctKeys.class) || clazz.equals(AcctKeysImpl.class) )
            return true;
        else
            return false;
    }

    public void setAcctKeysFormatDetector(AcctKeysFormatDetector acctKeysFormatDetector) {
        this.acctKeysFormatDetector = acctKeysFormatDetector;
    }

    public AcctKeysFormatDetector getAcctKeysFormatDetector() {
        return acctKeysFormatDetector;
    }

    private Object processResponse(Object obj, AcctKeysConversionContext ctx) {
        AcctKeysFormatType formatType = ctx.getIncomingAcctKeysFormat();

        if(formatType != null) {
            AcctKeysConverter converter = findFirstRegisteredConverter(AcctKeysFormatType.CANONICAL, formatType);

            if (converter != null) {
                obj = converter.convert((AcctKeys) obj);
            } else {
                log.debug("Could not find any converter (" + AcctKeysFormatType.CANONICAL + " -> " + formatType + ")" + "- passing through");
            }
        }

        return obj;
    }

    private Object processRequest(Object obj, AcctKeysConversionContext ctx) {
        AcctKeysFormatType format;
        format = acctKeysFormatDetector.detect((AcctKeys) obj);

        if (format.equals(AcctKeysFormatType.UNKNOWN)) {
            throw new TransformException("Could not parse AcctKeys entry. Can't perform conversion to canonical format.");
        }

        // only first format occurrence is meaningful
        if (ctx.getIncomingAcctKeysFormat() == null) {
            ctx.setIncomingAcctKeysFormat(format);
        }

        AcctKeysConverter converter = findFirstRegisteredConverter(format, AcctKeysFormatType.CANONICAL);

        if (converter != null) {
            log.debug("Could not find any converter (" + format + " -> " + AcctKeysFormatType.CANONICAL + ")" + "- passing through");
            obj = converter.convert((AcctKeys) obj);
        }

        return obj;
    }

    public void setAcctKeysConverters(List<AcctKeysConverter> acctKeysConverters) {
        this.acctKeysConverters = acctKeysConverters;
    }

    public List<AcctKeysConverter> getAcctKeysConverters() {
        return acctKeysConverters;
    }
}
