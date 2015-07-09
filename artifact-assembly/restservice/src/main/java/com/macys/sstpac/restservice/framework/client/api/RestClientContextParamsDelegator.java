package com.macys.sstpac.restservice.framework.client.api;

public class RestClientContextParamsDelegator {

    public static ThreadLocal<RestClientContextParams> rccParams = new ThreadLocal<RestClientContextParams>();
}
