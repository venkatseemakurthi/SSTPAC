package com.macys.sstpac.splunk.framework.parser;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.stereotype.Component;

import com.macys.sstpac.splunk.framework.bean.AccessLogVO;
import com.macys.sstpac.splunk.framework.bean.ApplicationLogVO;
import com.macys.sstpac.splunk.framework.bean.D2CLogVO;


@Component
public class SplunkLogParser {

	public AccessLogVO parseAccessLog(StringBuffer xmlResponse)
			throws Exception {
		AccessLogVO accessLogVO = null;
		try {
			accessLogVO = new AccessLogVO();
			accessLogVO.setCookiesMap(parseCookie(""));
			accessLogVO.setAttributeMap(parseAttributes(""));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return accessLogVO;
	}

	public ApplicationLogVO parseApplicationLog(StringBuffer xmlResponse)
			throws Exception {
		ApplicationLogVO applicationLogVO = null;
		try {
			applicationLogVO = new ApplicationLogVO();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return applicationLogVO;
	}

	public D2CLogVO parseD2CLog(StringBuffer xmlResponse) throws Exception {
		D2CLogVO d2cLogVO = null;
		try {
			d2cLogVO = new D2CLogVO();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return d2cLogVO;
	}

	private HashMap<String, String> parseCookie(String cookie) throws Exception {
		HashMap<String, String> cookieMap = new HashMap<String, String>();
		HttpCookie httpCookie = null;
		List<HttpCookie> cookies = null;
		try {
			cookies = HttpCookie.parse(cookie);
			for (Iterator<HttpCookie> iterator = cookies.iterator(); iterator
					.hasNext();) {
				httpCookie = iterator.next();
				cookieMap.put(httpCookie.getName(), httpCookie.getValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return cookieMap;
	}

	private HashMap<String, String> parseAttributes(String attribute)
			throws Exception {
		HashMap<String, String> attributeMap = new HashMap<String, String>();
		try {

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return attributeMap;
	}
}
