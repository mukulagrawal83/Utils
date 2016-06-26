package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import com.fnis.xes.framework.ext.processxbo.exceptions.XBOInternalStructureException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class IntrospectionHelper {
    public static Method findGetter(Class clazz, String attributeName) {
        return findMethodIntrospection(clazz, "get" + attributeName);
    }

    public static Method findSetter(Class clazz, String attributeName, Class attributeClass) {
        return findMethodIntrospection(clazz, "set" + attributeName, attributeClass);
    }

    public static Method findMethodIntrospection(Class clazz, String methodName, Class... methodArgs) {
        try {
            return clazz.getMethod(methodName, methodArgs);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    public static Object invokeMethod(Method method, Object object, Object... parameters) {
        try {
            return method.invoke(object, parameters);
        } catch (IllegalAccessException e) {
            throw new XBOInternalStructureException("Can't invoke requested method: " + method, e);
        } catch (InvocationTargetException e) {
            throw new XBOInternalStructureException("Can't invoke requested method: " + method, e);
        }
    }

    ;
}
