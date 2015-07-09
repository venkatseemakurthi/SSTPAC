package com.macys.sstpac.restservice;
/**
 * Class to hold response details after invoking REST resource
 */
public class RestServiceResponse {

	/**
	 * Holds domain object that will be un-marshaled from response content<br/>
	 * depending on UnMarshaller configured for adapter
	 */
	private Object entity;

	/**
	 * Holds PlatformErrorsBinding object or <br/>
	 * object returned by unmarshalError method in UnMarshaller<br/>
	 * configured for adapter
	 */
	private Object error;

	/**
	 * Holds response content in string format
	 */
	private String raw;

	/**
	 * Holds HTTP response code
	 */
	private int responseCode;

	public Object getEntity() {
		return entity;
	}

	public void setEntity(Object entity) {
		this.entity = entity;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getRaw() {
		return raw;
	}

	public void setRaw(String raw) {
		this.raw = raw;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "RestServiceResponse{responseCode=" + responseCode + ", content=" + raw +" }";
	}
}
