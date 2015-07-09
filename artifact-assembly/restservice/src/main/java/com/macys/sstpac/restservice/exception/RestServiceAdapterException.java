package com.macys.sstpac.restservice.exception;

import com.macys.sstpac.restservice.RestServiceResponse;

@SuppressWarnings("serial")
public class RestServiceAdapterException extends Exception {

	private RestServiceResponse restResponse;

	public RestServiceResponse getRestResponse() {
		return restResponse;
	}

	public RestServiceAdapterException(RestServiceResponse restResponse) {
		super();
		this.restResponse = restResponse;
	}

	public RestServiceAdapterException(Throwable t) {
		super(t);
	}

	public RestServiceAdapterException(RestServiceResponse restResponse,
			Throwable t) {
		super(t);
		this.restResponse = restResponse;
	}
}