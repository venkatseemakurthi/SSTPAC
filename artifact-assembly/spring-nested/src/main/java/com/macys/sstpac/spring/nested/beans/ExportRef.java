package com.macys.sstpac.spring.nested.beans;


import java.rmi.registry.Registry;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;


public class ExportRef implements BeanFactoryAware{

	private String target;
	private Class<?> interfaceClass;
	private BeanFactory beanFactory;
	
	public ExportRef() {
	}
	public ExportRef(String target) {
		this.target = target;
	}

	public ExportRef(String target, Class<?> interfaceClass) {
		this.target = target;
		this.interfaceClass = interfaceClass;
	}
	
	public void setTarget(String arg0) {
		target = arg0;
	}
	/**
	 * Used by Spring to inject beanFactory which will be used to resolve reference
	 * */
	public void setBeanFactory(BeanFactory arg0) throws BeansException {
		beanFactory = arg0;
	}

	public String getTarget() {
		return target;
	}

	public BeanFactory getBeanFactory() {
		return beanFactory;
	}

	public Class<?> getInterfaceClass() {
		return interfaceClass;
	}
	public void setInterfaceClass(Class<?> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}
	@Override
	public String toString() {
		return "ExportRef [target=" + target + ", interfaceClass="
				+ interfaceClass + ", beanFactory=" + beanFactory + "]";
	}
	
	
}
