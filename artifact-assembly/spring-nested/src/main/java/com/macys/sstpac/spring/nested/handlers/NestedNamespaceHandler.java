package com.macys.sstpac.spring.nested.handlers;


import org.springframework.beans.factory.xml.NamespaceHandlerSupport;


public class NestedNamespaceHandler extends NamespaceHandlerSupport {
    public void init() {
        super.registerBeanDefinitionParser("export", new NestedBeanDefinitionParser() );
        super.registerBeanDefinitionParser("import", new NestedBeanDefinitionParser() );
        super.registerBeanDefinitionParser("register-handler", new HandlerRegistrationParser());
        super.registerBeanDefinitionParser("register-interceptor", new InterceptorRegistrationParser());
    }
}
