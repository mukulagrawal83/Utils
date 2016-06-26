package com.fnis.xes.framework.ext.processxbo.action;

import com.fnis.xes.framework.ext.processxbo.subject.TransformSubject;

public abstract class AbstractTransformer {
    private String transformSelector;

    public AbstractTransformer(String path) {
        this.transformSelector = path;
    }

    public String getTransformSelector() {
        return transformSelector;
    }

    public void transform(TransformSubject subject) {
        throw new UnsupportedOperationException("Called abstract transformer. It is not implemented in inheriting class.");
    }
}