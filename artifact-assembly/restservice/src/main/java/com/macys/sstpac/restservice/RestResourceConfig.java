package com.macys.sstpac.restservice;

import org.apache.commons.lang.StringUtils;



/**
 * Class to hold REST Resource details
 */
public class RestResourceConfig {

	/**
	 * Holds REST Resource path <br>
	 * example: /checkout in http://localhost:8080/api/order/v1/checkout
	 * example: /products in http://localhost:8080/api/catalog/v2/products
	 */
	private String resourcePath = StringUtils.EMPTY;

	/**
	 * Holds media type of response content<br>
	 * example: application/json<br>
	 * 			application/xml<br>
	 */
	private String acceptType;

	public String getResourcePath() {
		return resourcePath;
	}

	public void setResourcePath(String resourcePath) {
		this.resourcePath = resourcePath;
	}
	
	public String getAcceptType() {
		return acceptType;
	}

	public void setAcceptType(String acceptType) {
		this.acceptType = acceptType;
	}
}
