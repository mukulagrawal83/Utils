package com.fnis.xes.framework.ext.processxbo.builder;

import com.fnis.xes.framework.ext.processxbo.action.AbstractTransformer;
import com.fnis.xes.framework.ext.processxbo.action.DefaultTransformer;
import com.fnis.xes.framework.ext.processxbo.action.TransformCallback;

import java.util.LinkedList;
import java.util.List;

public class TransformerBuilder {

    private List<AbstractTransformer> transformActions;

    public TransformerBuilder() {
        transformActions = new LinkedList<AbstractTransformer>();
    }

    public static TransformerBuilder create() {
        return (new TransformerBuilder());
    }

    public TransformerBuilder setInternalContext() {
        throw new UnsupportedOperationException("This feature has not been implemented yet!");
    }

    public TransformerBuilder transform(String path, TransformCallback callback) {
        DefaultTransformer defaultTransformAction = new DefaultTransformer(path, callback);
        transformActions.add(defaultTransformAction);
        return this;
    }

    public String dump() {
        StringBuilder sb = new StringBuilder();

        for (AbstractTransformer transformAction : transformActions) {
            sb.append(transformAction).append("\n");
        }

        return sb.toString();
    }

    public Transformer createTransformer() {
        return new Transformer(transformActions);
    }
}
