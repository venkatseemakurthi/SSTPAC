package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 403
 */
@SuppressWarnings("serial")
public class ResourceForbiddenException extends RestResourceInvocationException {

	static int statusCode = 403;

	public ResourceForbiddenException(RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
