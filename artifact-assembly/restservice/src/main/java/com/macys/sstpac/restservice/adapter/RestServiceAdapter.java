package com.macys.sstpac.restservice.adapter;

import com.macys.sstpac.restservice.RestServiceRequest;
import com.macys.sstpac.restservice.RestServiceResponse;
import com.macys.sstpac.restservice.exception.RestServiceAdapterException;



public interface RestServiceAdapter {

	public RestServiceResponse invoke(RestServiceRequest request)
			throws RestServiceAdapterException;

	public String getResourceUrl(RestServiceRequest request);

}
