package com.fnis.xes.framework.ext.processxbo.subject;

import com.fnis.xes.framework.ext.processxbo.path.BeanEntityPath;

import java.util.List;

public abstract class TransformSubject {

    public static final String IS_VALID_METHOD_NAME = "validate";

    public static void validate(String key) {
        throw new UnsupportedOperationException("Validation routine called for abstract class...");
    }

    public abstract List<TransformSubjectTarget> get(String key);

    public abstract List<TransformSubjectTarget> get(BeanEntityPath path);

    public abstract void set(String key, Object value);

    public abstract void set(BeanEntityPath path, Object value);

    public String dump() {
        throw new UnsupportedOperationException("dump() method for the class has not been implemented! " + this.getClass().getName());
    }
}
