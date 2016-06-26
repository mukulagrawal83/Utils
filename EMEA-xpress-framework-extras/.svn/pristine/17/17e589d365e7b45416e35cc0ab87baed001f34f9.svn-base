package com.fnis.xes.framework.filter.callbacks;

import com.fnis.ifx.xbo.v1_1.base.PartyKeys;
import com.fnis.ifx.xbo.v1_1.base.PartyKeysImpl;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;
import com.fnis.xes.framework.filter.converters.*;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * @author e1050475
 */
public class CanonicalPartyKeyTransformCallback implements TransformCallback {
    private static final Logger log = Logger.getLogger(CanonicalPartyKeyTransformCallback.class);
    private PartyKeysFormatDetector partyKeysFormatDetector;
    private List<PartyKeysConverter> partyKeysConverters;

    public Object transform(Object obj, Object providedCtx) {
        log.debug("Entered transform()");

        PartyKeysConversionContext ctx = (PartyKeysConversionContext) providedCtx;
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

    private PartyKeysConverter findFirstRegisteredConverter(PartyKeysFormatType from, PartyKeysFormatType to) {
        for(PartyKeysConverter converter : partyKeysConverters) {
            if(converter.handles(from, to)) {
                return converter;
            }
        }
        log.debug("Could not find any PartyKeys converter do handle conversion: " + from + " -> " + to);
        return null;
    }

    public Boolean handles(Class clazz, Class ctx) {
        if(clazz == null || ctx == null) {
            log.debug("Either value class or ctx class is null. So callback does not handle this situation.");
            return false;
        }
        // TODO: better to use clazz.isAssignableFrom for proper context handling
        if(clazz.equals(PartyKeys.class) || clazz.equals(PartyKeysImpl.class) )
            return true;
        else
            return false;
    }

    public void setPartyKeysFormatDetector(PartyKeysFormatDetector partyKeysFormatDetector) {
        this.partyKeysFormatDetector = partyKeysFormatDetector;
    }

    public PartyKeysFormatDetector getPartyKeysFormatDetector() {
        return partyKeysFormatDetector;
    }

    private Object processResponse(Object obj, PartyKeysConversionContext ctx) {
        PartyKeysFormatType formatType = ctx.getIncomingPartyKeysFormat();

        if(formatType != null) {
            PartyKeysConverter converter = findFirstRegisteredConverter(PartyKeysFormatType.CANONICAL, formatType);

            if (converter != null) {
                obj = converter.convert(ctx, (PartyKeys) obj);
            } else {
                log.debug("Could not find any converter (" + PartyKeysFormatType.CANONICAL + " -> " + formatType + ")" + "- passing through");
            }
        }

        return obj;
    }

    private Object processRequest(Object obj, PartyKeysConversionContext ctx) {
        PartyKeysFormatType format;
        format = partyKeysFormatDetector.detect(ctx, (PartyKeys) obj);

        if (format.equals(PartyKeysFormatType.UNKNOWN)) {
            throw new TransformException("Could not parse PartyKeys entry. Can't perform conversion to canonical format.");
        }

        // only first format occurrence is meaningful
        if (ctx.getIncomingPartyKeysFormat() == null) {
            ctx.setIncomingPartyKeysFormat(format);
        }

        PartyKeysConverter converter = findFirstRegisteredConverter(format, PartyKeysFormatType.CANONICAL);

        if (converter != null) {
            log.debug("Could not find any converter (" + format + " -> " + PartyKeysFormatType.CANONICAL + ")" + "- passing through");
            obj = converter.convert(ctx, (PartyKeys) obj);
        }

        return obj;
    }

    public void setPartyKeysConverters(List<PartyKeysConverter> partyKeysConverters) {
        this.partyKeysConverters = partyKeysConverters;
    }

    public List<PartyKeysConverter> getPartyKeysConverters() {
        return partyKeysConverters;
    }
}