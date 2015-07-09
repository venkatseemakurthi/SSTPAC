package com.macys.sstpac.restservice.marshaller.impl;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.mapped.Configuration;
import org.codehaus.jettison.mapped.MappedNamespaceConvention;
import org.codehaus.jettison.mapped.MappedXMLStreamReader;
import org.springframework.beans.factory.InitializingBean;

import com.macys.sstpac.restservice.RestServiceResponse;
import com.macys.sstpac.restservice.exception.MarshalUnMarshalException;
import com.macys.sstpac.restservice.framework.client.exception.ErrorsBinding;
import com.macys.sstpac.restservice.marshaller.ResponseUnMarshaller;

public class JsonUnMarshaller implements ResponseUnMarshaller, InitializingBean {

	private Class<?> clazz;
	
	JAXBContext jcNormal = null;
	JAXBContext jcUnmarshal = null;

	@Override
	public Object unmarshal(RestServiceResponse response)
			throws MarshalUnMarshalException {
		try {
			if (StringUtils.isBlank(response.getRaw())) {
				return null;
			}
			
			JSONObject obj = new JSONObject(response.getRaw());
			Configuration config = new Configuration();
			MappedNamespaceConvention con = new MappedNamespaceConvention(
					config);
			XMLStreamReader xmlStreamReader = new MappedXMLStreamReader(obj,
					con);
			Unmarshaller unmarshaller = jcNormal.createUnmarshaller();

			return unmarshaller.unmarshal(xmlStreamReader);
		} catch (Exception e) {
			throw new MarshalUnMarshalException(response, e);
		}
	}

	@Override
	public Object unmarshalError(RestServiceResponse response)
			throws MarshalUnMarshalException {
		try {
			if (StringUtils.isBlank(response.getRaw())) {
				return null;
			}

			// handle platform error from rest framework common
			
			JSONObject obj = new JSONObject(response.getRaw());
			Configuration config = new Configuration();
			Map<String, String> nss = new HashMap<String, String>(1);
			nss.put("http://www.macys.com/platform/error/v2", "");
			nss.put("http://www.w3.org/2001/XMLSchema-instance", "xsi");
			config.setXmlToJsonNamespaces(nss);
			MappedNamespaceConvention con = new MappedNamespaceConvention(
					config);
			XMLStreamReader xmlStreamReader = new MappedXMLStreamReader(obj,
					con);
			Unmarshaller unmarshaller = jcUnmarshal.createUnmarshaller();

			return unmarshaller.unmarshal(xmlStreamReader);
		} catch (Exception e) {
			throw new MarshalUnMarshalException(response, e);
		}
	}

	public void setClassName(String className) throws ClassNotFoundException {
		this.clazz = Class.forName(className);
	}

	@SuppressWarnings("rawtypes")
	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		jcNormal = JAXBContext.newInstance(clazz);
		jcUnmarshal = JAXBContext.newInstance(ErrorsBinding.class);
	}
}
