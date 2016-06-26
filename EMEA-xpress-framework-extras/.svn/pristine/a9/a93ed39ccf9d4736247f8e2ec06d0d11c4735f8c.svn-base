package com.fnis.xes.framework.ext.processxbo.subject.xbo;

import java.util.HashMap;

public class IFX2MessageInfoProvider {
    private static HashMap<String, IFX2MessageInfo> cachedObjects;

    static {
        cachedObjects = new HashMap<String, IFX2MessageInfo>();
    }

    public static IFX2MessageInfo provide(String messageId) {
        synchronized (cachedObjects) {
            IFX2MessageInfo entry = cachedObjects.get(messageId);
            if (entry == null) {
                entry = new IFX2MessageInfo(messageId);
                cachedObjects.put(messageId, entry);
            }
            return entry;
        }
    }
}
