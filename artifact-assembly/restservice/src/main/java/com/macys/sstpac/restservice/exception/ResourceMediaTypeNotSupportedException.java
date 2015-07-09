package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 415
 */
@SuppressWarnings("serial")
public class ResourceMediaTypeNotSupportedException extends RestResourceInvocationException {

	static int statusCode = 415;

	public ResourceMediaTypeNotSupportedException(RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
