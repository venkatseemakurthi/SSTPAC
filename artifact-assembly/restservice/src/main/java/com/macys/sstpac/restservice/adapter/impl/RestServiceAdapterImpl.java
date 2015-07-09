package com.macys.sstpac.restservice.adapter.impl;

import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringUtils;

import com.macys.sstpac.restservice.RestResourceConfig;
import com.macys.sstpac.restservice.RestServiceRequest;
import com.macys.sstpac.restservice.RestServiceResponse;
import com.macys.sstpac.restservice.exception.MarshalUnMarshalException;
import com.macys.sstpac.restservice.exception.RestResourceInvocationException;
import com.macys.sstpac.restservice.exception.RestServiceAdapterException;
import com.macys.sstpac.restservice.marshaller.RequestMarshaller;
import com.macys.sstpac.restservice.marshaller.ResponseUnMarshaller;

public class RestServiceAdapterImpl extends AbstractRestServiceAdapter {

	private RestResourceConfig resourceConfig;

	private ResponseUnMarshaller unmarshaller;

	private RequestMarshaller marshaller;

	@Override
	public RestServiceResponse invoke(RestServiceRequest request)
			throws RestServiceAdapterException {
		RestServiceResponse response = null;

		try {
			// build URL with path/query parameters set
			final String resolvedResourceUrl = getResourceUrl(request);

			// get client from pool and set target url
			WebTarget webTarget = getClient().target(resolvedResourceUrl);
			Invocation.Builder resource = webTarget.request();
			if (StringUtils.isNotBlank(resourceConfig.getAcceptType())) {
				resource.accept(resourceConfig.getAcceptType());
			}

			// set request headers
			if (request.hasHeaders()) {
				setHeader(resource, request.getHeaders());
			}

			// set X-Macys-RequestId in headers
			setHeader(resource, REQUEST_ID, request.getRequestId());

			// invoking rest service
			Response clientResponse = invokeService(resource, request);

			if (clientResponse != null) {

				response = buildResponse(clientResponse);

				if (isSuccess(clientResponse)) {
					setEntity(response);
				} else {
					setError(response);
					RestResourceInvocationException e = getDefinedException(response);
					if (e == null) {
						throw new RestResourceInvocationException(
								clientResponse.getStatus(), response);
					}
					throw e;
				}
			}
		} catch (RestResourceInvocationException e) {
			throw e;
		} catch (MarshalUnMarshalException e) {
			throw e;
		} catch (Exception e) {
			throw new RestServiceAdapterException(e);
		}
		return response;
	}

	public String getResourceUrl(RestServiceRequest request) {
		StringBuilder urlBuilder = new StringBuilder();
		urlBuilder.append(restClientPool.getHostName());
		urlBuilder.append(restClientPool.getBasePath());
		urlBuilder.append(resourceConfig.getResourcePath());
		if (request != null) {
			// path params
			if (request.hasPathParams()) {
				if (urlBuilder.toString().lastIndexOf(PATH_SEPERATOR) != urlBuilder
						.toString().length()) {
					urlBuilder.append(PATH_SEPERATOR);
				}
				urlBuilder.append(buildPath(request.getPathParams()));
			}

			// query params
			if (request.hasQueryParam()) {
				urlBuilder.append(buildQueryParam(request.getQueryParams()));
			}

			return urlBuilder.toString();
		}
		return null;
	}

	protected String buildRequestBody(RestServiceRequest request) {
		String reqBody = null;
		if (marshaller != null) {
			reqBody = marshaller.marshal(request);
		} else {
			reqBody = request.getEntity() != null ? request.getEntity()
					.toString() : null;
		}
		return reqBody;
	}

	private void setEntity(RestServiceResponse response)
			throws MarshalUnMarshalException {
		if (unmarshaller != null) {
			response.setEntity(unmarshaller.unmarshal(response));
		} else {
			response.setEntity(response.getRaw());
		}
	}

	private void setError(RestServiceResponse response)
			throws MarshalUnMarshalException {
		if (unmarshaller != null) {
			response.setError(unmarshaller.unmarshalError(response));
		} else {
			response.setError(response.getRaw());
		}
	}

	public void setResourceConfig(RestResourceConfig resourceConfig) {
		this.resourceConfig = resourceConfig;
	}

	public void setUnmarshaller(ResponseUnMarshaller unmarshaller) {
		this.unmarshaller = unmarshaller;
	}

	public void setMarshaller(RequestMarshaller marshaller) {
		this.marshaller = marshaller;
	}

}
