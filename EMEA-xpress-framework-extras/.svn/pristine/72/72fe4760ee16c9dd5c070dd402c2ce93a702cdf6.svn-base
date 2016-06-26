package com.fnis.xes.framework.ext.processxbo.cache;

import java.lang.reflect.Method;

// TODO: MethodMap should use consistent ENUM for getter and setter instead method names (/set/get/)
public class BeanMethodCache {
    private static final String GETTER_METHOD_PREFIX = "get";
    private static final String SETTER_METHOD_PREFIX = "set";

    private MethodMap methodMap;

    public BeanMethodCache() {
        methodMap = new MethodMap();
    }

    public void put(Method method) {
        methodMap.put(method);
    }

    public void put(Class clazz, Method method) {
        methodMap.put(clazz, method);
    }

    public void put(Class clazz, Method method, Class... arguments) {
        methodMap.put(clazz, method, arguments);
    }

    public Method findByName(String className, String methodName, Class... parameters) {
        return methodMap.findByName(className, methodName, parameters);
    }

    public Method findGetter(String className, String attributeName) {
        return methodMap.findByName(className, GETTER_METHOD_PREFIX + attributeName);
    }

    public Method findSetter(String className, String attributeName, Class parameter) {
        return methodMap.findByName(className, SETTER_METHOD_PREFIX + attributeName, parameter);
    }

    public String dump() {
        StringBuffer sb = new StringBuffer();
        for (MethodKey key : methodMap.keySet()) {
            sb.append("[");
            sb.append(key);
            sb.append("]=");
            sb.append(methodMap.find(key));
            sb.append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "BeanMethodCache{" +
                "methodMap=" + methodMap +
                '}';
    }
}
