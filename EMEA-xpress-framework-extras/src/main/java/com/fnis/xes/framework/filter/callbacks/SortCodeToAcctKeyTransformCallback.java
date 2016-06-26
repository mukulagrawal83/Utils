package com.fnis.xes.framework.filter.callbacks;

import com.fnis.ifx.xbo.v1_1.base.ToAcctKeys;
import com.fnis.ifx.xbo.v1_1.base.ToAcctKeysImpl;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;
import com.fnis.xes.framework.filter.converters.ToAcctKeysConversionContext;
import com.fnis.xes.framework.filter.converters.ToAcctKeysConverter;
import com.fnis.xes.framework.filter.converters.ToAcctKeysFormatDetector;
import com.fnis.xes.framework.filter.converters.AcctKeysFormatType;
import org.apache.log4j.Logger;

import java.util.List;

public class SortCodeToAcctKeyTransformCallback implements TransformCallback {
    private static final Logger log = Logger.getLogger(SortCodeToAcctKeyTransformCallback.class);
    private ToAcctKeysFormatDetector toAcctKeysFormatDetector;
    private List<ToAcctKeysConverter> toAcctKeysConverters;

    public Object transform(Object obj, Object providedCtx) {
        log.debug("Entered transform()");

        ToAcctKeysConversionContext ctx = (ToAcctKeysConversionContext) providedCtx;
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

    private ToAcctKeysConverter findFirstRegisteredConverter(AcctKeysFormatType from, AcctKeysFormatType to) {
        for(ToAcctKeysConverter converter : toAcctKeysConverters) {
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
        if(clazz.equals(ToAcctKeys.class) || clazz.equals(ToAcctKeysImpl.class) )
            return true;
        else
            return false;
    }

    public void setToAcctKeysFormatDetector(ToAcctKeysFormatDetector toAcctKeysFormatDetector) {
        this.toAcctKeysFormatDetector = toAcctKeysFormatDetector;
    }

    public ToAcctKeysFormatDetector getToAcctKeysFormatDetector() {
        return toAcctKeysFormatDetector;
    }

    private Object processResponse(Object obj, ToAcctKeysConversionContext ctx) {
        AcctKeysFormatType formatType = ctx.getIncomingAcctKeysFormat();

        if(formatType != null) {
            ToAcctKeysConverter converter = findFirstRegisteredConverter(AcctKeysFormatType.SORTCODEPROFILECID, formatType);

            if (converter != null) {
                obj = converter.convert((ToAcctKeys) obj);
            } else {
                log.debug("Could not find any converter (" + AcctKeysFormatType.SORTCODEPROFILECID + " -> " + formatType + ")" + "- passing through");
            }
        }

        return obj;
    }

    private Object processRequest(Object obj, ToAcctKeysConversionContext ctx) {
        AcctKeysFormatType format;
        format = toAcctKeysFormatDetector.detect((ToAcctKeys) obj);

        if (format.equals(AcctKeysFormatType.UNKNOWN)) {
            throw new TransformException("Could not parse AcctKeys entry. Can't perform conversion to Profile format.");
        }

        // only first format occurrence is meaningful
        if (ctx.getIncomingAcctKeysFormat() == null) {
            ctx.setIncomingAcctKeysFormat(format);
        }

        ToAcctKeysConverter converter = findFirstRegisteredConverter(format, AcctKeysFormatType.SORTCODEPROFILECID);

        if (converter != null) {
            log.debug("Could not find any converter (" + format + " -> " + AcctKeysFormatType.SORTCODEPROFILECID + ")" + "- passing through");
            obj = converter.convert((ToAcctKeys) obj);
        }

        return obj;
    }

    public void setToAcctKeysConverters(List<ToAcctKeysConverter> toAcctKeysConverters) {
        this.toAcctKeysConverters = toAcctKeysConverters;
    }

    public List<ToAcctKeysConverter> getToAcctKeysConverters() {
        return toAcctKeysConverters;
    }
}
