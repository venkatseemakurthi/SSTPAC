package com.macys.sstpac.restservice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;


/**
 * Class to hold request details to invoke a REST resource.
 */
public class RestServiceRequest {

	/**
	 * List to hold path parameters <br>
	 * In case of sub-resource, specify the sub resource path in this list<br>
	 * Example: for .../products/1234/upcs/1234567 <br>
	 * Arrays.asList(new String[]{"1234", "upcs", "1234567"})
	 */
	private List<String> pathParams;

	/**
	 * Map to hold query string
	 */
	private Map<String, String> queryParams;

	/**
	 * Map to hold header info
	 */
	private Map<String, String> headers;

	/**
	 * Holds domain object that will be marshaled to specified content type<br>
	 * depending on the marshaler configured for adapter<br>
	 * This will be set as request body while updating REST resource
	 */
	private Object entity;

	/**
	 * Holds request entity content type<br>
	 */
	private String contentType;

	/**
	 * HTTP Method type
	 */
	private HTTP_METHOD type;

	/**
	 * Request ID, random UUID per request
	 */
	private final String requestId = UUID.randomUUID().toString();
	
	/**
	 * Enum represents HTTP Method
	 */
	public static enum HTTP_METHOD {
		GET, POST, PUT, DELETE, OPTIONS, HEAD, TRACE, CONNECT
	}

	/**
	 * Generic constructor to build request object for HTTP method provided
	 * 
	 * @param type
	 *            - HTTP_METHOD
	 */
	public RestServiceRequest(HTTP_METHOD type) {
		this.type = type;
	}

	/**
	 * Convenience method to get request object for GET request
	 * 
	 * @return instance of this class configured for GET request
	 */
	public static RestServiceRequest forGet() {
		return new RestServiceRequest(HTTP_METHOD.GET);
	}

	/**
	 * Convenience method to get request object for POST request
	 * 
	 * @return instance of this class configured for POST request
	 */
	public static RestServiceRequest forPost() {
		return new RestServiceRequest(HTTP_METHOD.POST);
	}

	public List<String> getPathParams() {
		if(pathParams == null) {
			pathParams = new ArrayList<String>();
		}
		return pathParams;
	}

	public Map<String, String> getQueryParams() {
		if(queryParams == null) {
			queryParams = new LinkedHashMap<String, String>();
		}
		return queryParams;
	}

	public Map<String, String> getHeaders() {
		if(headers == null) {
			headers = new LinkedHashMap<String, String>();
		}
		return headers;
	}

	public boolean hasQueryParam() {
		return (queryParams != null && !queryParams.isEmpty());
	}

	public boolean hasHeaders() {
		return (headers != null && !headers.isEmpty());
	}

	public boolean hasPathParams() {
		return CollectionUtils.isNotEmpty(pathParams);
	}
	
	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public HTTP_METHOD getType() {
		return type;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getRequestId() {
		return requestId;
	}

	@Override
	public String toString() {
		return "RestServiceRequest{requestId = "+ requestId + " operation=" + type + ", pathParam="
				+ pathParams + ", queryParam=" + queryParams + ", headers="
				+ headers + " }";
	}

}
