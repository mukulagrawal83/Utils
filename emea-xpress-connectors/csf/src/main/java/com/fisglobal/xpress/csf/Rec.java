/*
 * ===================================================================
 * Copyright Notice!
 * -------------------------------------------------------------------
 * This document is protected under the trade secret and copyright
 * laws as the property of Fidelity National Information Services, Inc.
 * Copying, reproduction or distribution should be limited and only to
 * employees with a �need to know� to do their job. 
 * Any disclosure of this document to third parties 
 * is strictly prohibited.
 *
 * �  Fidelity National Information Services
 * All rights reserved worldwide.
 * ===================================================================
 */
package com.fisglobal.xpress.csf;

import java.util.List;

public class Rec {

	private List<Fld> fileds;
	
	private Rec record;

	public Rec getRec() {
		return record;
	}

	/**
	 * @param rec
	 */
	public void setRec(Rec rec) {
		this.record = rec;
	}

	/**
	 * @return
	 */
	public List<Fld> getFileds() {
		return fileds;
	}

	/**
	 * @param fileds
	 */
	public void setFileds(List<Fld> fileds) {
		this.fileds = fileds;
	}
}
