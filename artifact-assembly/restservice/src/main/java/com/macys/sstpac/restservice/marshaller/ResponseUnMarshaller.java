package com.macys.sstpac.restservice.marshaller;

import com.macys.sstpac.restservice.RestServiceResponse;
import com.macys.sstpac.restservice.exception.MarshalUnMarshalException;

public interface ResponseUnMarshaller {

	public Object unmarshal(RestServiceResponse response) throws MarshalUnMarshalException;

	public Object unmarshalError(RestServiceResponse response) throws MarshalUnMarshalException;
}
