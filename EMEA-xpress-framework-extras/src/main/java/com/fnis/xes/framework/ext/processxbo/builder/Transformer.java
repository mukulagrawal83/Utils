package com.fnis.xes.framework.ext.processxbo.builder;

import com.fnis.xes.framework.ext.processxbo.action.AbstractTransformer;
import com.fnis.xes.framework.ext.processxbo.exceptions.TransformException;
import com.fnis.xes.framework.ext.processxbo.exceptions.ValidationException;
import com.fnis.xes.framework.ext.processxbo.subject.TransformSubject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class Transformer {
    private List<AbstractTransformer> actionList;

    public Transformer(List<AbstractTransformer> actionList) {
        this.actionList = new LinkedList<AbstractTransformer>(actionList);
    }

    public void transform(TransformSubject subject) {
        for (AbstractTransformer action : actionList) {
            action.transform(subject);
        }
    }

    public void validate(Class transformSubjectClass) {
        Method canHandleMethod = getStaticCanHandleMethod(transformSubjectClass);

        for (AbstractTransformer action : actionList) {
            String transformTarget = action.getTransformSelector();
            try {
                callStaticMethod(canHandleMethod, transformTarget);
            } catch (ValidationException e) {
                throw new RuntimeException(transformTarget + " is not handled by provided class: " + transformSubjectClass.getName());
            }
        }
    }

    @Override
    public String toString() {
        return "Transformer{" +
                "actionList=" + actionList +
                '}';
    }

    private Method getStaticCanHandleMethod(Class<TransformSubject> clazz) {
        try {
            Method m = clazz.getMethod(TransformSubject.IS_VALID_METHOD_NAME, String.class);
            if (!Modifier.isStatic(m.getModifiers())) {
                throw new TransformException("validation method is not declared as static");
            }
            return m;
        } catch (NoSuchMethodException e) {
            throw new TransformException("validation method is not available", e);
        }
    }

    private Object callStaticMethod(Method staticMethod, String transformTarget) {
        try {
            return staticMethod.invoke(null, transformTarget);
        } catch (IllegalAccessException e) {
            throw new TransformException("can't invoke validate(String) static method", e);
        } catch (InvocationTargetException e) {
            throw new TransformException("validation error occurred", e);
        }
    }
}
