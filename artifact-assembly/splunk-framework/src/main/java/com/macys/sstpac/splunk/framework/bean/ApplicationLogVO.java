package com.macys.sstpac.splunk.framework.bean;

import java.io.Serializable;

public class ApplicationLogVO extends LogVO  implements Serializable {

	private static final long serialVersionUID = 7473006112100283862L;
	
	private String logLevel ;
	private String applicationLog;

	public String getLogLevel() {
		return logLevel;
	}

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	public String getApplicationLog() {
		return applicationLog;
	}

	public void setApplicationLog(String applicationLog) {
		this.applicationLog = applicationLog;
	}
	
	public ApplicationLogVO() {
		super.setLogType("APL");
	}
}
