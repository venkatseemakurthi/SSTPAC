package com.macys.sstpac.restservice.framework.client.api;

public interface RestResponse {

	public boolean isErrorOccured();

	public int getStatusCode();

	public <U> U getOutput();

	public <V> V getError();

}
