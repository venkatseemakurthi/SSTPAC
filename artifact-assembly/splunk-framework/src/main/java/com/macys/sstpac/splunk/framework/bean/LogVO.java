package com.macys.sstpac.splunk.framework.bean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashMap;

public class LogVO implements Serializable {

	private static final long serialVersionUID = -8815262868826704047L;

	private int userId;
	private String logType;
	private HashMap<String, String> attributeMap = new HashMap<String, String>();
	private Timestamp logTime ;
	private String logQuery;
	
	public String getLogQuery() {
		return logQuery;
	}
	public void setLogQuery(String logQuery) {
		this.logQuery = logQuery;
	}
	public Timestamp getLogTime() {
		return logTime;
	}
	public void setLogTime(Timestamp logTime) {
		this.logTime = logTime;
	}

	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public HashMap<String, String> getAttributeMap() {
		return attributeMap;
	}
	public void setAttributeMap(HashMap<String, String> attributeMap) {
		this.attributeMap = attributeMap;
	}
    
	
	
}
