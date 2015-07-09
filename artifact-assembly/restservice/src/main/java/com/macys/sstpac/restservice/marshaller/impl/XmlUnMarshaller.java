package com.macys.sstpac.restservice.marshaller.impl;


import com.macys.sstpac.restservice.RestServiceResponse;
import com.macys.sstpac.restservice.exception.MarshalUnMarshalException;
import com.macys.sstpac.restservice.marshaller.ResponseUnMarshaller;

public class XmlUnMarshaller implements ResponseUnMarshaller {

	@SuppressWarnings("unused")
	private Class<?> clazz;

	@Override
	public Object unmarshal(RestServiceResponse response)
			throws MarshalUnMarshalException {
		return null;
	}

	@Override
	public Object unmarshalError(RestServiceResponse response)
			throws MarshalUnMarshalException {
		return null;
	}

	public void setClassName(String className) throws ClassNotFoundException {
		this.clazz = Class.forName(className);
	}

	@SuppressWarnings("rawtypes")
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
}
