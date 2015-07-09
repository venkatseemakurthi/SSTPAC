package com.macys.sstpac.splunk.framework.bean;

import java.io.Serializable;

public class D2CLogVO extends LogVO implements Serializable {

	private static final long serialVersionUID = 7527274555620434996L;
    
	private String d2cLog ;

	public String getD2cLog() {
		return d2cLog;
	}

	public void setD2cLog(String d2cLog) {
		this.d2cLog = d2cLog;
	}
	
	public D2CLogVO() {
		super.setLogType("DCL");
	}
	
}
