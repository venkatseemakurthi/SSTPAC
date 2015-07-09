package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 405
 */
@SuppressWarnings("serial")
public class ResourceOperationNotAllowedException extends RestResourceInvocationException {

	static int statusCode = 405;

	public ResourceOperationNotAllowedException(RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
