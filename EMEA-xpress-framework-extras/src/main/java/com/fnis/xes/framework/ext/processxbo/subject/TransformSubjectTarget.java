package com.fnis.xes.framework.ext.processxbo.subject;

public class TransformSubjectTarget {
    String key;
    Object element;
    Object context;

    public TransformSubjectTarget(String key, Object object, Object context) {
        this.key = key;
        this.element = object;
        this.context = context;
    }

    public TransformSubjectTarget(String key, Object object) {
        this(key, object, null);
    }

    public String getKey() {
        return key;
    }

    public Object getElement() {
        return element;
    }

    public Object getContext() {
        return context;
    }
}
