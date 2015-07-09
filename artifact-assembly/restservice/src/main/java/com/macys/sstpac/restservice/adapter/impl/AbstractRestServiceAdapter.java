package com.macys.sstpac.restservice.adapter.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.macys.sstpac.restservice.RestServiceRequest;
import com.macys.sstpac.restservice.RestServiceResponse;
import com.macys.sstpac.restservice.adapter.RestServiceAdapter;
import com.macys.sstpac.restservice.exception.ResourceAuthenticationFailedException;
import com.macys.sstpac.restservice.exception.ResourceForbiddenException;
import com.macys.sstpac.restservice.exception.ResourceMediaTypeNotSupportedException;
import com.macys.sstpac.restservice.exception.ResourceNotFoundException;
import com.macys.sstpac.restservice.exception.ResourceOperationNotAllowedException;
import com.macys.sstpac.restservice.exception.ResourceThrottledException;
import com.macys.sstpac.restservice.exception.ResourceUnavailableException;
import com.macys.sstpac.restservice.exception.RestResourceInvocationException;
import com.macys.sstpac.restservice.framework.client.api.RestClientFactory;
import com.macys.sstpac.restservice.framework.client.exception.RestClientException;

public abstract class AbstractRestServiceAdapter implements RestServiceAdapter {

	protected final Logger log = Logger.getLogger(this.getClass());

	protected static final int SUCCESS = 200;

	protected static final int DEFAULT_TIMEOUT = 5000;

	protected int readTimeout = DEFAULT_TIMEOUT;

	protected int connectionTimeout = DEFAULT_TIMEOUT;

	protected static final String PATH_SEPERATOR = "/";

	protected static final String QUERY_PARAM_SEPERATOR = "&";

	protected static final String QUERY_PARAM_START = "?";

	protected static final String QUERY_PARAM_ASSIGN = "=";

	protected static final String REQUEST_ID = "X-Macys-RequestId";

	protected abstract String buildRequestBody(RestServiceRequest request);

	protected String restClientPoolName;

	protected RestClientFactory.JaxRSClientPool restClientPool;

	protected Response invokeService(Invocation.Builder service,
			RestServiceRequest request) {
		switch (request.getType()) {
		case GET:
			return service.get();
		case POST:
			return service.post(Entity.entity(buildRequestBody(request),
					MediaType.valueOf(request.getContentType())));
		case PUT:
			return service.put(Entity.entity(buildRequestBody(request),
					MediaType.valueOf(request.getContentType())));
		case DELETE:
			return service.delete();
		case OPTIONS:
			return service.options();
		case HEAD:
			return service.head();
		}
		// TRACE, CONNECT
		throw new ResourceOperationNotAllowedException(null);
	}

	protected String buildPath(List<String> pathParams) {
		StringBuilder pathBuilder = new StringBuilder();
		for (String path : pathParams) {
			if (StringUtils.isNotBlank(pathBuilder.toString())) {
				pathBuilder.append(PATH_SEPERATOR);
			}
			pathBuilder.append(path);
		}
		return pathBuilder.toString();
	}

	protected String buildQueryParam(Map<String, String> queryparam) {
		StringBuilder queryBuilder = new StringBuilder();
		for (Entry<String, String> query : queryparam.entrySet()) {
			if (StringUtils.isBlank(queryBuilder.toString())) {
				queryBuilder.append(QUERY_PARAM_START);
			} else {
				queryBuilder.append(QUERY_PARAM_SEPERATOR);
			}
			queryBuilder.append(query.getKey()).append(QUERY_PARAM_ASSIGN)
					.append(query.getValue());
		}
		return queryBuilder.toString();
	}

	protected void setHeader(Invocation.Builder resource,
			Map<String, String> headers) {
		if (headers != null && !headers.isEmpty()) {
			for (Entry<String, String> header : headers.entrySet()) {
				setHeader(resource, header.getKey(), header.getValue());
			}
		}
	}

	protected void setHeader(Invocation.Builder resource, String headerName,
			String headerValue) {
		resource.header(headerName, headerValue);
	}

	protected RestResourceInvocationException getDefinedException(
			RestServiceResponse response) {
		switch (response.getResponseCode()) {
		case 401:
			return new ResourceAuthenticationFailedException(response);
		case 403:
			return new ResourceForbiddenException(response);
		case 404:
			return new ResourceNotFoundException(response);
		case 405:
			return new ResourceOperationNotAllowedException(response);
		case 415:
			return new ResourceMediaTypeNotSupportedException(response);
		case 429:
			return new ResourceThrottledException(response);
		case 204:
			return new ResourceUnavailableException(response);
		}
		return null;
	}

	protected Client getClient() throws RestClientException {
		return restClientPool.getClient(restClientPoolName);
	}

	protected boolean isSuccess(Response response) {
		return response != null ? response.getStatus() == SUCCESS : false;
	}

	protected RestServiceResponse buildResponse(Response clientResponse) {
		RestServiceResponse response = new RestServiceResponse();
		response.setResponseCode(clientResponse.getStatus());
		response.setRaw(clientResponse.readEntity(String.class));
		return response;
	}

	public void setReadTimeout(int readTimeout) {
		this.readTimeout = readTimeout;
	}

	public void setConnectionTimeout(int connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}

	public void setRestClientPoolName(String restClientPoolName) {
		this.restClientPoolName = restClientPoolName;
	}

	public void setRestClientPool(
			RestClientFactory.JaxRSClientPool restClientPool) {
		this.restClientPool = restClientPool;
	}

}
