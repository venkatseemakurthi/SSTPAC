package com.macys.sstpac.spring.nested.beans;

import org.springframework.context.ConfigurableApplicationContext;

import java.util.Set;

public class ContextInfo {
	private String location;
	private String url;
	private ConfigurableApplicationContext context;
	private Set<String> usedProperties;

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ConfigurableApplicationContext getContext() {
		return context;
	}

	public void setContext(ConfigurableApplicationContext context) {
		this.context = context;
	}

	public Set<String> getUsedProperties() {
		return usedProperties;
	}

	public void setUsedProperties(Set<String> usedProperties) {
		this.usedProperties = usedProperties;
	}

	@Override
	public String toString() {
		return "ContextInfo{" + "location='" + location + '\'' + ", url='"
				+ url + '\'' + '}';
	}
}
