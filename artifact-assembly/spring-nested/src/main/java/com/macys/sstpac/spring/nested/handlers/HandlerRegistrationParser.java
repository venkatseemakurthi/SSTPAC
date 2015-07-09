package com.macys.sstpac.spring.nested.handlers;



public class HandlerRegistrationParser extends AbstractHandlerMappingRegistrationParser {
    @Override
    protected String getIdSuffix() {
        return "handler";
    }

    @Override
    protected String getFactoryMethodName() {
        return "registerByAnnotation";
    }
}
