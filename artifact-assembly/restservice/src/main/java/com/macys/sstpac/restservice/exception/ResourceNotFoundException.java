package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 404
 */
@SuppressWarnings("serial")
public class ResourceNotFoundException extends RestResourceInvocationException {

	static int statusCode = 404;

	public ResourceNotFoundException(RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
