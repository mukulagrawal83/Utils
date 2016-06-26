package com.fnis.xes.framework.filter.callbacks;

import com.fnis.ifx.xbo.v1_1.base.AcctKeys;
import com.fnis.ifx.xbo.v1_1.base.AcctKeysImpl;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.util.Mapper;
import org.apache.log4j.Logger;

public class AcctIdTagTransformCallback implements TransformCallback {
    private static final Logger log = Logger.getLogger(AcctIdTagTransformCallback.class);

    private Mapper mapper;

    public Object transform(Object obj, Object ctx) {
        log.debug("Entered transform()");
        AcctKeys acctKeys = (AcctKeys) obj;

        if(acctKeys.getAcctId() != null) {
            if("".equals(acctKeys.getAcctId().trim())) {
                log.debug("Could not map blank AcctId");
                throw new RuntimeException("Could not map blank AcctId");
            }

            String originalValue = acctKeys.getAcctId();
            log.debug("Found original AcctId=" + originalValue);
            String mappedValue = mapper.map(acctKeys.getAcctId());
            if(mappedValue != null) {
                log.debug("Substituting value of AcctId=" + mappedValue + " with " + mappedValue);
                acctKeys.setAcctId(mappedValue);
            } else {
                log.debug("Could not find corresponding value for AcctId=" + originalValue);
                throw new RuntimeException("Could not find corresponding value for AcctId=" + originalValue);
            }
        }

        log.debug("Leaving transform()");
        return obj;
    }

    public Boolean handles(Class clazz, Class ctxClazz) {
        if(clazz.equals(AcctKeys.class) || clazz.equals(AcctKeysImpl.class))
            return true;
        else
            return false;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public void setMapper(Mapper mapper) {
        this.mapper = mapper;
    }

}
