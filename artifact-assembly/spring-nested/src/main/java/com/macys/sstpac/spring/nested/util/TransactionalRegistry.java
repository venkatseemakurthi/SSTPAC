package com.macys.sstpac.spring.nested.util;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.macys.sstpac.spring.nested.beans.ExportRef;
import com.macys.sstpac.spring.nested.beans.Registry;


public class TransactionalRegistry implements Registry {

    private final Registry delegate;

    private boolean transactionOpen;
    private List<ExportRef> dirtyExports;

    public TransactionalRegistry(Registry delegate) {
        this.delegate = delegate;
    }

    public void openTransaction() {
        transactionOpen = true;
        dirtyExports = new ArrayList<ExportRef>();
    }

    public void commitTransaction() {
        for (ExportRef exportRef : dirtyExports) {
            delegate.export(exportRef);
        }
        transactionOpen = false;
        dirtyExports = null;
    }

    public void rollbackTransaction() {
        transactionOpen = false;
        dirtyExports = null;
    }

    public Void export(ExportRef ref) {
        if (transactionOpen) {
            dirtyExports.add(ref);
        } else {
            delegate.export(ref);
        }
        return null;
    }

    public <T> T lookup(String name, Class<T> clazz) {
        return delegate.lookup(name, clazz);
    }

    public <T> Collection<String> lookupByInterface(Class<T> clazz) {
        return delegate.lookupByInterface(clazz);
    }
}
