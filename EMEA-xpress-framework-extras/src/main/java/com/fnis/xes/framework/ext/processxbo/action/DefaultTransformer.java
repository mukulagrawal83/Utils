package com.fnis.xes.framework.ext.processxbo.action;

import com.fnis.xes.framework.ext.processxbo.subject.TransformSubject;
import com.fnis.xes.framework.ext.processxbo.subject.TransformSubjectTarget;

import java.util.List;

public class DefaultTransformer extends AbstractTransformer {
    private TransformCallback callback;

    public DefaultTransformer(String path, TransformCallback callback) {
        super(path);
        this.callback = callback;
    }

    @Override
    public void transform(TransformSubject subject) {
        List<TransformSubjectTarget> transformSubjectTargets = subject.get(this.getTransformSelector());

        for (TransformSubjectTarget transformSubjectTarget : transformSubjectTargets) {
            convertSingleTarget(subject, transformSubjectTarget);
        }
    }

    private void convertSingleTarget(TransformSubject subject, TransformSubjectTarget transformSubjectTarget) {
        Object oldObject = transformSubjectTarget.getElement();
        Object objectCtx = transformSubjectTarget.getContext();

        if (callback.handles(oldObject.getClass(), (objectCtx != null) ? objectCtx.getClass() : null)) {
            Object newObject = callback.transform(oldObject, objectCtx);
            subject.set(transformSubjectTarget.getKey(), newObject);
        }
    }

    @Override
    public String toString() {
        return "DefaultTransformer{" +
                "callback=" + callback +
                '}';
    }
}
