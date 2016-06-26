package com.fnis.xes.services.adapter.cortex.utils;

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
 * Time: 13:28
 */
public interface MessageIdProvider {

    public int nextVal();
    public String nextUUID();
    public String getRecordId();
}
