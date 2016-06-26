package com.fnis.xes.services.adapter.cortex.utils;

import org.apache.commons.lang.RandomStringUtils;

import java.util.UUID;

/**
 * This program contains trade secrets that belong to Fidelity Information
 * Services, Inc. and is licensed by an agreement.  Any unauthorized access,
 * use, duplication, or disclosure is unlawful.
 * <p/>
 * Copyright (c) Fidelity Information Services, Inc.
 * 2006, All right reserved.
 * <p/>
 * User: Satheesh Kumar G - e1011705
 * Date: 04/04/14
 * Time: 13:30
 */
public class SimpleMessageIdProvider implements MessageIdProvider {

    private int counter = 0;
    private static SimpleMessageIdProvider instance;

    private SimpleMessageIdProvider() {
    }

    public static synchronized SimpleMessageIdProvider getInstance() {
        if ( instance == null ) {
            instance = new SimpleMessageIdProvider();
        }
        return instance;
    }

    public synchronized int nextVal() {
        return counter++;
    }

    public String nextUUID() {
        return UUID.randomUUID().toString();
    }

    public String getRecordId() {
        return RandomStringUtils.randomNumeric(15);
    }
}
