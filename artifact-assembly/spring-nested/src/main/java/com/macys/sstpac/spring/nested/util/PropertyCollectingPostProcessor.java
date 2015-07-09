package com.macys.sstpac.spring.nested.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;


public class PropertyCollectingPostProcessor extends PropertyPlaceholderConfigurer {

    private Set<String> usedProperties;

    public PropertyCollectingPostProcessor() {
        setIgnoreUnresolvablePlaceholders(true);
        setValueSeparator(null);
        this.usedProperties = new HashSet<String>();
    }

    @Override
    protected String resolvePlaceholder(String placeholder, Properties props, int systemPropertiesMode) {
        usedProperties.add(placeholder);
        int separatorIndex = placeholder.indexOf(DEFAULT_VALUE_SEPARATOR);
        if (separatorIndex != -1) {
            String actualPlaceholder = placeholder.substring(0, separatorIndex);
            usedProperties.add(actualPlaceholder);
        }
        return null;
    }

    public Set<String> getUsedProperties() {
        return Collections.unmodifiableSet(usedProperties);
    }
}
