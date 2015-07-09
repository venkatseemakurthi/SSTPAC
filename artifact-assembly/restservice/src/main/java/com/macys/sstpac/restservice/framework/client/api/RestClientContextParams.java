package com.macys.sstpac.restservice.framework.client.api;

import java.util.HashMap;
import java.util.Map;

public class RestClientContextParams {
private String requestId;
private Map<String,String> otherHeaderParams;
private Map<String,String> otherQueryParams;


public String getRequestId() {
	return requestId;
}
public void setRequestId(String requestId) {
	this.requestId = requestId;
}

public Map<String, String> getOtherHeaderParams() {
	if ( otherHeaderParams==null )
		otherHeaderParams = new HashMap<String,String>();
	
		
	return otherHeaderParams;
}
 
public Map<String, String> getOtherQueryParams() {
	if ( otherQueryParams==null )
		otherQueryParams = new HashMap<String,String>();
	
	return otherQueryParams;
}
 
}
