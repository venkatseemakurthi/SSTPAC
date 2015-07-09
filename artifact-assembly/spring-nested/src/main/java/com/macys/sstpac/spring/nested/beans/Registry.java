package com.macys.sstpac.spring.nested.beans;

import java.util.Collection;

public interface Registry {

	Void export(ExportRef ref);

	<T> T lookup(final String name, final Class<T> clazz);

	<T> Collection<String> lookupByInterface(Class<T> clazz);
}