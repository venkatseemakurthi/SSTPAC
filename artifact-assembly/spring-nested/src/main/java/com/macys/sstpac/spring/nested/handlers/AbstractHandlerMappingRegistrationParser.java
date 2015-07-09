package com.macys.sstpac.spring.nested.handlers;

import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Element;


public abstract class AbstractHandlerMappingRegistrationParser extends AbstractSingleBeanDefinitionParser {
    private final static String ARG_REF = "ref";
    private final static String ARG_ROOT_HANDLER_MAPPING = "rootHandlerMapping";

    private final static String ROOT_HANDLER_MAPPING_DEFAULT_NAME = "root-handler-mapping";

    protected abstract String getIdSuffix();
    protected abstract String getFactoryMethodName();

    @Override
    protected String getBeanClassName(Element element) {
        return Void.class.getCanonicalName();
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        return element.getAttribute(ARG_REF)
                + "$" + getIdSuffix()
                + BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR
                + ObjectUtils.getIdentityHexString(definition);
    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {

        String rootHandlerMapping;
        if (element.hasAttribute(ARG_ROOT_HANDLER_MAPPING)) {
            rootHandlerMapping = element.getAttribute(ARG_ROOT_HANDLER_MAPPING);
        }
        else {
            rootHandlerMapping = ROOT_HANDLER_MAPPING_DEFAULT_NAME;
        }

        AbstractBeanDefinition beanDefinition = builder.getRawBeanDefinition();
        beanDefinition.setFactoryBeanName(rootHandlerMapping);
        beanDefinition.setFactoryMethodName(getFactoryMethodName());
        beanDefinition.setLazyInit(false);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        builder.addConstructorArgReference(element.getAttribute(ARG_REF));
    }
}