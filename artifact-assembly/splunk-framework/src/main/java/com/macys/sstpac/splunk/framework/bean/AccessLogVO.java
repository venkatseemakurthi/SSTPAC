package com.macys.sstpac.splunk.framework.bean;

import java.io.Serializable;
import java.util.HashMap;

public class AccessLogVO extends LogVO implements Serializable {
	
	private static final long serialVersionUID = -8813211358655813326L;
	
    private String url;
    private HashMap<String, String> cookiesMap = new HashMap<String, String>();
  
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, String> getCookiesMap() {
		return cookiesMap;
	}

	public void setCookiesMap(HashMap<String, String> cookiesMap) {
		this.cookiesMap = cookiesMap;
	}
    
	public AccessLogVO() {
		super.setLogType("ACL");
	}
    

}
