package com.fnis.xes.framework.filter.callbacks;

import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;
import com.fnis.xes.framework.util.SubtokenSelectionMapperWrapper;
import org.apache.log4j.Logger;

public class RecSelectTagTransformCallback implements TransformCallback {
    private static final Logger log = Logger.getLogger(RecSelectTagTransformCallback.class);
    private SubtokenSelectionMapperWrapper mapper;

    public Object transform(Object obj, Object ctx) {
        log.debug("Entered transform()");
        String recSelect = (String) obj;

        if(recSelect != null) {
            if("".equals(recSelect.trim())) {
                log.debug("Could not map blank RecSelect entry");
                throw new RuntimeException("Could not map blank RecSelect entry");
            }

            String originalValue = recSelect;
            log.debug("Found original RecSelect=" + originalValue);
            String mappedValue = mapper.map(originalValue);
            if(mappedValue != null) {
                log.debug("Substituting value of RecSelect=" + mappedValue + " with " + mappedValue);
                obj = mappedValue;
            } else {
                log.debug("Could not find corresponding value for RecSelect=" + originalValue);
                throw new RuntimeException("Could not find corresponding value for RecSelect=" + originalValue);
            }
        }

        log.debug("Leaving transform()");
        return obj;
    }

    public Boolean handles(Class clazz, Class ctxClazz) {
        if(clazz.equals(String.class))
            return true;
        else
            return false;
    }

    public void setMapper(SubtokenSelectionMapperWrapper mapper) {
        this.mapper = mapper;
    }

    public SubtokenSelectionMapperWrapper getMapper() {
        return mapper;
    }
}
