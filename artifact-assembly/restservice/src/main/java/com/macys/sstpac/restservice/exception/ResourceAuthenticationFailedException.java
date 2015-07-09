package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

/**
 * Thrown for 401
 */
@SuppressWarnings("serial")
public class ResourceAuthenticationFailedException extends
		RestResourceInvocationException {

	static int statusCode = 401;

	public ResourceAuthenticationFailedException(
			RestServiceResponse restResponse) {
		super(statusCode);
		super.restResponse = restResponse;
	}
}
