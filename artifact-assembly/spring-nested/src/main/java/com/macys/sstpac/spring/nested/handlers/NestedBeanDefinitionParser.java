package com.macys.sstpac.spring.nested.handlers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.CannotLoadBeanClassException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Element;

import com.macys.sstpac.spring.nested.beans.ExportRef;
import com.macys.sstpac.spring.nested.exception.UnknownNestedElementException;

public class NestedBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {
    private static final Logger log = Logger.getLogger(NestedBeanDefinitionParser.class);

    private final static String ROOT_DEFAULT_NAME = "root";
    private final static String EXPORT = "export";
    private final static String IMPORT = "import";

    private final static String ID = "id";
    private final static String INTERFACE = "interface";
    private final static String REF = "ref";
    private final static String NAME = "name";
    private final static String ROOT = "root";


    @Override
    protected String getBeanClassName(Element element) {
        String localName = element.getLocalName();
        if(EXPORT.equals(localName) ) {
            return Void.class.getCanonicalName();
        } else if( IMPORT.equals(localName) ) {
            return element.getAttribute(INTERFACE);
        }

        throw new UnknownNestedElementException("Unknown element '" + localName + "'");
    }

    @Override
    protected String resolveId(Element element, AbstractBeanDefinition definition, ParserContext parserContext) throws BeanDefinitionStoreException {
        String localName = element.getLocalName();
        if(EXPORT.equals(localName) ) {
            return element.getAttribute(REF)
                    + "$export"
                    + BeanFactoryUtils.GENERATED_BEAN_NAME_SEPARATOR
                    + ObjectUtils.getIdentityHexString(definition);
        } else if(IMPORT.equals(localName) ) {
            return super.resolveId(element, definition, parserContext);
        }

        throw new UnknownNestedElementException("Unknown element '" + localName + "'");

    }

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String localName = element.getLocalName();
        if( EXPORT.equals(localName) ) {
            parseExport(element, parserContext);
        } else if( IMPORT.equals(localName) ) {
            parseImport(element, parserContext, builder);
        }
    }

    private void parseImport(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
        String exportInterface = element.getAttribute(INTERFACE);
        if( isNullOrEmpty(exportInterface) ) {
            return;
        }

        String externalName = element.getAttribute(ID);
        if( isNullOrEmpty(externalName) ) {
            return;
        }

        String rootName = element.getAttribute(ROOT);
        if( isNullOrEmpty(rootName) ) {
            rootName = ROOT_DEFAULT_NAME;
        }

        AbstractBeanDefinition beanDefinition = builder.getRawBeanDefinition();
        beanDefinition.setFactoryBeanName(rootName);
        beanDefinition.setFactoryMethodName("lookup");
        beanDefinition.setLazyInit(true);
        beanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);

        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(externalName);
        constructorArgumentValues.addGenericArgumentValue(findClass(
            exportInterface,
            element.getAttribute(ID),
            parserContext.getReaderContext().getResource().getDescription()
        ));

        beanDefinition.setConstructorArgumentValues(constructorArgumentValues);
    }

    private void parseExport(Element element, ParserContext parserContext) {
        String exportInterface = element.getAttribute(INTERFACE);
        if( isNullOrEmpty(exportInterface) ) {
            return;
        }

        String exportBeanRef = element.getAttribute(REF);
        if( isNullOrEmpty(exportBeanRef) ) {
            return;
        }

        String rootName = element.getAttribute(ROOT);
        if( isNullOrEmpty(rootName) ) {
            rootName = ROOT_DEFAULT_NAME;
        }

        String exportName = element.getAttribute(NAME);
        if( isNullOrEmpty(exportName) ) {
            exportName = exportBeanRef;
        }

        String exportRefName = exportName + "-export-ref";

        BeanDefinitionRegistry registry = parserContext.getRegistry();
        if( registry.containsBeanDefinition(exportRefName) ) {
            throw new BeanCreationException("Registry already contains bean with name " + exportRefName);
        }

        BeanDefinitionBuilder voidBuilder = BeanDefinitionBuilder.rootBeanDefinition(Void.class);
        AbstractBeanDefinition voidBeanDefinition = voidBuilder.getRawBeanDefinition();
        voidBeanDefinition.setFactoryBeanName(rootName);
        voidBeanDefinition.setFactoryMethodName("export");
        voidBeanDefinition.setLazyInit(false);
        voidBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
//        voidBeanDefinition.setDependsOn(new String[] { exportBeanRef });

        BeanDefinitionBuilder exportRefBuilder = BeanDefinitionBuilder.rootBeanDefinition(ExportRef.class);
        AbstractBeanDefinition exportBeanDefinition = exportRefBuilder.getRawBeanDefinition();
        ConstructorArgumentValues exportRefConstructorArgumentValues = new ConstructorArgumentValues();
        exportRefConstructorArgumentValues.addGenericArgumentValue(exportName);
        exportRefConstructorArgumentValues.addGenericArgumentValue(findClass(
                exportInterface,
                exportBeanRef,
                parserContext.getReaderContext().getResource().getDescription()
        ));
        exportBeanDefinition.setConstructorArgumentValues(exportRefConstructorArgumentValues);

        ConstructorArgumentValues constructorArgumentValues = new ConstructorArgumentValues();
        constructorArgumentValues.addGenericArgumentValue(exportBeanDefinition, ExportRef.class.getName() );
        voidBeanDefinition.setConstructorArgumentValues(constructorArgumentValues);

        registry.registerBeanDefinition(exportRefName, voidBeanDefinition);
    }

    private Class findClass(String className, String beanName, String resourceDescription) {
        Class clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException ex) {
            throw new CannotLoadBeanClassException(resourceDescription, beanName, className, ex);
        }
        return clazz;
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }
}
