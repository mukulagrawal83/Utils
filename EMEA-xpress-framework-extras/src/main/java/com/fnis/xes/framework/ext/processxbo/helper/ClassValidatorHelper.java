package com.fnis.xes.framework.ext.processxbo.helper;

public class ClassValidatorHelper {
    private static final ClassLoader CLASS_LOADER = ClassValidatorHelper.class.getClassLoader();

    public static Class findClass(String packageName, String className) {
        Class clazz = null;

        String fullClassName = packageName + "." + className;

        try {
            clazz = CLASS_LOADER.loadClass(fullClassName);
        } catch (ClassNotFoundException e) {
            // do nothing
        }

        return clazz;
    }

    public static Boolean isInternalJavaType(Class clazz) {
        return (clazz.getName().startsWith("java.lang."));
    }

    public static Boolean isJavaCollection(Class clazz) {
        return (clazz.getName().startsWith("java.util."));
    }

}
