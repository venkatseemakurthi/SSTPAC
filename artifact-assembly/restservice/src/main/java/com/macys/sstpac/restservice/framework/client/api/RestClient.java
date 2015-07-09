package com.macys.sstpac.restservice.framework.client.api;

import com.macys.sstpac.restservice.framework.client.exception.RestClientException;

public interface RestClient {

	public <U,V> RestResponse execute(RestRequest restRequest,String serviceName, Class<U> outputClass,Class<V> errorOutputClass) throws RestClientException;

	public <U,V> RestResponse execute(RestRequest restRequest,String serviceName) throws RestClientException;


}
