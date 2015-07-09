package com.macys.sstpac.restservice.framework.client.exception;

public class RestClientException extends Exception{

	private static final long serialVersionUID = 1L;

	
	public RestClientException(String message , Exception ex)  {
		super(message,ex);
	}
}
