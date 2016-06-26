package com.fnis.xes.framework.ext.processxbo.cache;

import java.util.Arrays;

// TODO: it is recommended to provide dedicated enum for setter and getter and use it along with attribute name
public class MethodKey {
    private String fullClassName;
    private String methodName;
    private BeanMethodType methodType;
    private Class[] parameterTypes;

    public MethodKey(String fullClassName, String methodName, BeanMethodType methodType, Class[] parameterTypes) {
        this.fullClassName = fullClassName;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.methodType = methodType;
    }

    public MethodKey(String fullClassName, String methodName, Class[] parameterTypes) {
        this(fullClassName, methodName, BeanMethodType.NOT_DEFINED, parameterTypes);
    }

    @Override
    public String toString() {
        return "MethodKey{" +
                "fullClassName='" + fullClassName + '\'' +
                ", methodName='" + methodName + '\'' +
                ", methodType=" + methodType +
                ", parameterTypes=" + (parameterTypes == null ? null : Arrays.asList(parameterTypes)) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MethodKey)) return false;

        MethodKey methodKey = (MethodKey) o;

        if (fullClassName != null ? !fullClassName.equals(methodKey.fullClassName) : methodKey.fullClassName != null)
            return false;
        if (methodName != null ? !methodName.equals(methodKey.methodName) : methodKey.methodName != null) return false;
        if (methodType != methodKey.methodType) return false;
        if (!Arrays.equals(parameterTypes, methodKey.parameterTypes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = fullClassName != null ? fullClassName.hashCode() : 0;
        result = 31 * result + (methodName != null ? methodName.hashCode() : 0);
        result = 31 * result + (methodType != null ? methodType.hashCode() : 0);
        result = 31 * result + (parameterTypes != null ? Arrays.hashCode(parameterTypes) : 0);
        return result;
    }
}
