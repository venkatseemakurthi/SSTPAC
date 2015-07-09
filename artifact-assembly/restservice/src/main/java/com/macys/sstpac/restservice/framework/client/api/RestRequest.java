package com.macys.sstpac.restservice.framework.client.api;
import java.util.List;
import java.util.Map;

public interface RestRequest {

	public void setPlaceHoldersMap(Map<String, Object> placeHolderMap);

	public Map<String, Object> getPlaceHoldersMap();

	public RestRequest setEntity(Object object);

	public Object getEntity();

	public RestRequest setRequestMethodType(RestClientMethodType methodtype);

	public RestClientMethodType getRequestMethodType();

	public RestRequest setSucessOutputType(Class<?> sucessClassType);

	public RestRequest setErrorOutputType(Class<?> errorClassType);

	public Class<?> getSucessOutputType();

	public Class<?> getErrorOutputType();

	public RestRequest addHTTPSuccessCode(Integer sucessCode);

	public List<Integer> getSucessHTTPCodes();

	public RestRequest addPlaceHolder(String name, String value);

}
