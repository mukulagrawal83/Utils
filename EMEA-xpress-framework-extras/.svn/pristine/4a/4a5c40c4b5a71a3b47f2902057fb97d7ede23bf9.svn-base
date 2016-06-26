package com.fnis.xes.framework.filter.callbacks;

import com.fnis.ifx.xbo.v1_1.base.FromAcctKeys;
import com.fnis.ifx.xbo.v1_1.base.FromAcctKeysImpl;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;
import com.fnis.xes.framework.filter.converters.*;
import org.apache.log4j.Logger;

import java.util.List;

public class ProfileFromAcctKeyTransformCallback implements TransformCallback {
    private static final Logger log = Logger.getLogger(ProfileFromAcctKeyTransformCallback.class);
    private FromAcctKeysFormatDetector fromAcctKeysFormatDetector;
    private List<FromAcctKeysConverter> fromAcctKeysConverters;

    public Object transform(Object obj, Object providedCtx) {
        log.debug("Entered transform()");

        FromAcctKeysConversionContext ctx = (FromAcctKeysConversionContext) providedCtx;
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

    private FromAcctKeysConverter findFirstRegisteredConverter(AcctKeysFormatType from, AcctKeysFormatType to) {
        for(FromAcctKeysConverter converter : fromAcctKeysConverters) {
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
        if(clazz.equals(FromAcctKeys.class) || clazz.equals(FromAcctKeysImpl.class) )
            return true;
        else
            return false;
    }

    private Object processResponse(Object obj, FromAcctKeysConversionContext ctx) {
        AcctKeysFormatType formatType = ctx.getIncomingAcctKeysFormat();

        if(formatType != null) {
            FromAcctKeysConverter converter = findFirstRegisteredConverter(AcctKeysFormatType.SORTCODEPROFILECID, formatType);

            if (converter != null) {
                obj = converter.convert((FromAcctKeys) obj);
            } else {
                log.debug("Could not find any converter (" + AcctKeysFormatType.SORTCODEPROFILECID + " -> " + formatType + ")" + "- passing through");
            }
        }

        return obj;
    }

    private Object processRequest(Object obj, FromAcctKeysConversionContext ctx) {
        AcctKeysFormatType format;
        format = fromAcctKeysFormatDetector.detect((FromAcctKeys) obj);

        if (format.equals(AcctKeysFormatType.UNKNOWN)) {
            throw new TransformException("Could not parse AcctKeys entry. Can't perform conversion to Profile format.");
        }

        // only first format occurrence is meaningful
        if (ctx.getIncomingAcctKeysFormat() == null) {
            ctx.setIncomingAcctKeysFormat(format);
        }

        FromAcctKeysConverter converter = findFirstRegisteredConverter(format, AcctKeysFormatType.SORTCODEPROFILECID);

        if (converter != null) {
            log.debug("Could not find any converter (" + format + " -> " + AcctKeysFormatType.SORTCODEPROFILECID + ")" + "- passing through");
            obj = converter.convert((FromAcctKeys) obj);
        }

        return obj;
    }

    public FromAcctKeysFormatDetector getFromAcctKeysFormatDetector() {
        return fromAcctKeysFormatDetector;
    }

    public void setFromAcctKeysFormatDetector(FromAcctKeysFormatDetector fromAcctKeysFormatDetector) {
        this.fromAcctKeysFormatDetector = fromAcctKeysFormatDetector;
    }

    public List<FromAcctKeysConverter> getFromAcctKeysConverters() {
        return fromAcctKeysConverters;
    }

    public void setFromAcctKeysConverters(List<FromAcctKeysConverter> fromAcctKeysConverters) {
        this.fromAcctKeysConverters = fromAcctKeysConverters;
    }
}
