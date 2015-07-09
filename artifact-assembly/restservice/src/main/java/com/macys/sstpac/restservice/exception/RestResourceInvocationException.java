package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

@SuppressWarnings("serial")
public class RestResourceInvocationException extends RuntimeException {

	public static String HTTP_CODE_404 = "404";
	public static String HTTP_CODE_204 = "204";
	public static String HTTP_CODE_405 = "405";
	public static String HTTP_CODE_401 = "401";
	public static String HTTP_CODE_403 = "403";
	public static String HTTP_CODE_415 = "415";
	public static String HTTP_CODE_429 = "429";

	protected RestServiceResponse restResponse;

	public RestResourceInvocationException(int statusCode) {
		super("HTTP Error code " + statusCode + " received");
	}

	public RestResourceInvocationException(int statusCode,
			RestServiceResponse response) {
		this(statusCode);
		this.restResponse = response;
	}

	public RestServiceResponse getRestResponse() {
		return this.restResponse;
	}
}
