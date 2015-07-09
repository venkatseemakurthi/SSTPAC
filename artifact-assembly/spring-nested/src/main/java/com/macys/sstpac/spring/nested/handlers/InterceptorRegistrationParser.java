package com.macys.sstpac.spring.nested.handlers;

public class InterceptorRegistrationParser extends AbstractHandlerMappingRegistrationParser {
    @Override
    protected String getIdSuffix() {
        return "interceptor";
    }

    @Override
    protected String getFactoryMethodName() {
        return "registerInterceptor";
    }
}
