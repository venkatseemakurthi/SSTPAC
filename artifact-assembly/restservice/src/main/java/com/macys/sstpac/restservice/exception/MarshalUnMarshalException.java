package com.macys.sstpac.restservice.exception;


import com.macys.sstpac.restservice.RestServiceResponse;

@SuppressWarnings("serial")
public class MarshalUnMarshalException extends RestServiceAdapterException {

	public MarshalUnMarshalException(RestServiceResponse restResponse) {
		super(restResponse);
	}

	public MarshalUnMarshalException(RestServiceResponse restResponse,
			Throwable t) {
		super(restResponse, t);
	}

}
