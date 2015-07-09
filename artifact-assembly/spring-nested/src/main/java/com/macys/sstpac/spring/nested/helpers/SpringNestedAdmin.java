package com.macys.sstpac.spring.nested.helpers;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


public interface SpringNestedAdmin {

    /**
     * @return all locations of loaded contexts
     */
    Set<String> getContextLocations();

    /**
     * Resets specific contexts to original state and reloads them
     * @param locations locations that identify target contexts
     */
    void resetContexts(Collection<String> locations);

    /**
     * Overrides specific properties in all contexts that use them
     * @param properties properties to override
     * @return context locations of affected contexts to use with {@link #resetContexts}
     */
    Set<String> overrideProperties(Map<String, String> properties);

    /**
     * Overrides specific properties in specific contexts
     * @param properties properties to override
     * @param contextLocations locations that identify target contexts
     */
    void overrideProperties(Map<String, String> properties, Collection<String> contextLocations);
}
