package com.macys.sstpac.restservice.marshaller;


import com.macys.sstpac.restservice.RestServiceRequest;

public interface RequestMarshaller {

	String marshal(RestServiceRequest request);
}
