package com.fnis.xes.framework.ext.processxbo.cache;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

public class MethodMap {
    private static final Class[] EMPTY_CLASS_ARRAY = {};

    Map<MethodKey, Method> methodMap;

    public MethodMap() {
        this.methodMap = new Hashtable<MethodKey, Method>();
    }

    public void put(Method method) {
        Class declaringClass = method.getDeclaringClass();
        put(declaringClass, method, BeanMethodType.NOT_DEFINED);
    }

    public void put(Class clazz, Method method, Class... providedParameterTypes) {
        put(clazz, method, BeanMethodType.NOT_DEFINED, providedParameterTypes);
    }

    public void put(Class clazz, Method method, BeanMethodType methodType, Class... providedParameterTypes) {
        String className = clazz.getCanonicalName();
        String methodName = method.getName();

        Class[] parameterTypes = providedParameterTypes;
        if (providedParameterTypes == null || providedParameterTypes.length == 0) {
            parameterTypes = method.getParameterTypes();
        }

        Class returnType = method.getReturnType();

        MethodKey methodKey = new MethodKey(className, methodName, parameterTypes);
        methodMap.put(methodKey, method);
    }

    public Set<MethodKey> keySet() {
        return methodMap.keySet();
    }

    public Method findByName(String className, String methodName, Class... parameters) {
        MethodKey methodKey = new MethodKey(className, methodName, BeanMethodType.NOT_DEFINED, parameters);
        return methodMap.get(methodKey);
    }

    public Method findByName(String className, String methodName, BeanMethodType beanMethodType, Class... parameters) {
        MethodKey methodKey = new MethodKey(className, methodName, beanMethodType, parameters);
        return methodMap.get(methodKey);
    }

    public Method find(MethodKey key) {
        return methodMap.get(key);
    }

    @Override
    public String toString() {
        return "MethodMap{" +
                "methodMap=" + methodMap +
                '}';
    }
}
