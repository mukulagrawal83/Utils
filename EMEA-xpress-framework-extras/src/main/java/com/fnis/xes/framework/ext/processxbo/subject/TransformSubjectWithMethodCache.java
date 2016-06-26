package com.fnis.xes.framework.ext.processxbo.subject;

import com.fnis.xes.framework.ext.processxbo.cache.BeanMethodCache;

public abstract class TransformSubjectWithMethodCache extends TransformSubject {
    private static final BeanMethodCache beanMethodCache;

    static {
        beanMethodCache = new BeanMethodCache();
    }

    public static BeanMethodCache getBeanMethodCache() {
        return beanMethodCache;
    }

}
