package com.macys.sstpac.spring.nested.helpers;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import com.macys.sstpac.spring.nested.beans.ContextInfo;
import com.macys.sstpac.spring.nested.beans.ExportRef;
import com.macys.sstpac.spring.nested.beans.Registry;
import com.macys.sstpac.spring.nested.util.PropertyCollectingPostProcessor;
import com.macys.sstpac.spring.nested.util.TransactionalRegistry;

public class ContextParentBean implements InitializingBean,
		ApplicationContextAware, Registry, DisposableBean, SpringNestedAdmin {

	public static final class SingleResourceWebChildContext extends
			XmlWebApplicationContext {

		private Resource res;

		SingleResourceWebChildContext(Resource res, ApplicationContext parent) {
			this(res, parent, null);
		}

		SingleResourceWebChildContext(Resource res, ApplicationContext parent,
				BeanFactoryPostProcessor postProcessor) {
			this.res = res;
			setParent(parent);
			if (postProcessor != null) {
				addBeanFactoryPostProcessor(postProcessor);
			}
			refresh();
		}

		protected void loadBeanDefinitions(XmlBeanDefinitionReader reader)
				throws BeansException, IOException {
			reader.loadBeanDefinitions(res);
		}
	}

	private static final Logger log = Logger.getLogger(ContextParentBean.class);
	private Map<String, Exception> nestedContextsExceptions = new LinkedHashMap<String, Exception>();

	protected ApplicationContext context;
	private List<ContextInfo> children = new ArrayList<ContextInfo>();

	private String[] configLocations;
	private TransactionalRegistry registry;

	private boolean strictErrorHandling = false;
	private String childApplicationContextClassName = null;

	public void setStrictErrorHandling(boolean strictErrorHandling) {
		this.strictErrorHandling = strictErrorHandling;
	}

	public Registry getRegistry() {
		return registry;
	}

	public void setChildApplicationContextClassName(
			String childApplicationContextClassName) {
		this.childApplicationContextClassName = childApplicationContextClassName;
	}

	public void setRegistry(Registry registry) {
		this.registry = new TransactionalRegistry(registry);
	}

	public void afterPropertiesSet() throws Exception {
		for (String loc : configLocations) {
			// attempt to resolve classpath*:
			try {
				Resource[] resources = context.getResources(loc);

				for (final Resource res : resources) {
					try {
						PropertyCollectingPostProcessor propertyCollectingPostProcessor = new PropertyCollectingPostProcessor();
						ConfigurableApplicationContext child = createChildContext(
								res, context, propertyCollectingPostProcessor);
						ContextInfo contextInfo = new ContextInfo();
						contextInfo.setLocation(loc);
						contextInfo.setUrl(res.getURL().toString());
						contextInfo.setContext(child);
						contextInfo
								.setUsedProperties(propertyCollectingPostProcessor
										.getUsedProperties());
						children.add(contextInfo);
					} catch (Exception e) {
						log.error(
								String.format(
										"Failed to process resource [%s] from location [%s] ",
										res.getURL(), loc), e);
						if (strictErrorHandling) {
							throw e;
						}
						nestedContextsExceptions.put(loc, e);
					}
				}
			} catch (IOException e) {
				log.error(String.format(
						"Failed to process configuration from [%s]", loc), e);
				if (strictErrorHandling) {
					throw e;
				}
			}
		}
	}

	private ConfigurableApplicationContext createChildContext(Resource res,
			ApplicationContext parent, BeanFactoryPostProcessor postProcessor)
			throws Exception {
		if (childApplicationContextClassName != null
				&& childApplicationContextClassName.length() > 0) {
			try {
				Class applicationContextClass = Class
						.forName(childApplicationContextClassName);
				Constructor constructor = applicationContextClass
						.getConstructor(new Class[] { String[].class,
								boolean.class });
				ConfigurableApplicationContext result = (ConfigurableApplicationContext) constructor
						.newInstance(new String[] { res.getURL().toString() },
								false);
				result.setParent(parent);
				if (postProcessor != null) {
					result.addBeanFactoryPostProcessor(postProcessor);
				}
				result.refresh();
				return result;
			} catch (Exception e) {
				log.warn("Can not initialize ApplicationContext "
						+ childApplicationContextClassName
						+ " with configuration location " + res.getURL(), e);
			}
		}

		return new SingleResourceWebChildContext(res, parent, postProcessor);
	}

	public Map<String, Exception> getNestedContextsExceptions() {
		return nestedContextsExceptions;
	}

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		context = arg0;
	}

	public void setConfigLocation(String list) {
		String[] locations = StringUtils.tokenizeToStringArray(list,
				ConfigurableApplicationContext.CONFIG_LOCATION_DELIMITERS);

		Assert.noNullElements(locations, "Config locations must not be null");
		configLocations = new String[locations.length];
		for (int i = 0; i < locations.length; i++) {
			this.configLocations[i] = (SystemPropertyUtils
					.resolvePlaceholders(locations[i])).trim();
		}
	}

	public void setConfigLocations(String[] list) {
		configLocations = list;
	}

	public List<ConfigurableApplicationContext> getChildren() {
		List<ConfigurableApplicationContext> result = new ArrayList<ConfigurableApplicationContext>();
		for (ContextInfo contextInfo : children) {
			result.add(contextInfo.getContext());
		}
		return result;
	}

	public Void export(ExportRef ref) {
		if (log.isDebugEnabled()) {
			log.debug("exporting bean '" + ref.getTarget()
					+ "' with interface '"
					+ ref.getInterfaceClass().getSimpleName() + "'");
		}
		return registry.export(ref);
	}

	public <T> T lookup(String name, Class<T> clazz) {
		if (log.isDebugEnabled()) {
			log.debug("looking up bean '" + name + "' with interface '"
					+ clazz.getSimpleName() + "'");
		}
		return registry.lookup(name, clazz);
	}

	public <T> Collection<String> lookupByInterface(Class<T> clazz) {
		return registry.lookupByInterface(clazz);
	}

	public void destroy() throws Exception {
		Collections.reverse(children);
		for (ContextInfo child : children) {
			child.getContext().close();
		}
	}

	public String[] getConfigLocations() {
		return configLocations;
	}

	public Set<String> getContextLocations() {
		return getContextLocations(children);
	}

	public void resetContexts(Collection<String> locations) {
		Collection<ContextInfo> contexts = getContextsByLocations(locations);
		reloadContexts(contexts, null);
	}

	public Set<String> overrideProperties(Map<String, String> properties) {
		// log.info("Overriding properties {}", properties);
		Collection<ContextInfo> contexts = getContextsByProperties(properties
				.keySet());
		overridePropertiesInternal(properties, contexts);
		return getContextLocations(contexts);
	}

	public void overrideProperties(Map<String, String> properties,
			Collection<String> contextLocations) {
		// log.info("Overriding properties {} for context locations {}",
		// properties, contextLocations);
		Collection<ContextInfo> contexts = getContextsByLocations(contextLocations);
		overridePropertiesInternal(properties, contexts);
	}

	private void overridePropertiesInternal(Map<String, String> properties,
			Collection<ContextInfo> contexts) {
		PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
		propertyPlaceholderConfigurer
				.setProperties(mapToProperties(properties));
		propertyPlaceholderConfigurer.setIgnoreUnresolvablePlaceholders(true);
		reloadContexts(contexts, propertyPlaceholderConfigurer);
	}

	private synchronized void reloadContexts(Collection<ContextInfo> contexts,
			BeanFactoryPostProcessor postProcessor) {
		// log.info("Reloading contexts: {}", contexts);
		registry.openTransaction();
		try {
			Map<ContextInfo, ConfigurableApplicationContext> newContexts = new HashMap<ContextInfo, ConfigurableApplicationContext>(
					contexts.size());
			for (ContextInfo contextInfo : contexts) {
				Resource resource = new UrlResource(contextInfo.getUrl());

				ConfigurableApplicationContext newContext = createChildContext(
						resource, context, postProcessor);
				newContexts.put(contextInfo, newContext);
			}
			try {
				registry.commitTransaction();
				for (ContextInfo contextInfo : contexts) {
					ConfigurableApplicationContext oldContext = contextInfo
							.getContext();
					contextInfo.setContext(newContexts.get(contextInfo));
					oldContext.close();
				}
			} catch (Exception e) {
				log.error("Error while committing transaction, contexts may be left in a bad state. Application restart is advised");
				throw e;
			}
			log.info("Reload completed successfully");
		} catch (Exception e) {
			registry.rollbackTransaction();
			log.error("Error while reloading contexts", e);
			throw new RuntimeException(e.getMessage());
		}
	}

	private Collection<ContextInfo> getContextsByProperties(
			Set<String> propertyNames) {
		List<ContextInfo> result = new ArrayList<ContextInfo>();
		for (ContextInfo contextInfo : children) {
			if (!Collections.disjoint(propertyNames,
					contextInfo.getUsedProperties())) {
				result.add(contextInfo);
			}
		}
		return result;
	}

	private Collection<ContextInfo> getContextsByLocations(
			Collection<String> locations) {
		Set<String> locationsSet = new HashSet<String>(locations);
		List<ContextInfo> result = new ArrayList<ContextInfo>();
		for (ContextInfo contextInfo : children) {
			if (locationsSet.contains(contextInfo.getLocation())) {
				result.add(contextInfo);
			}
		}
		return result;
	}

	private Set<String> getContextLocations(Collection<ContextInfo> contextInfos) {
		Set<String> result = new HashSet<String>();
		for (ContextInfo contextInfo : contextInfos) {
			result.add(contextInfo.getLocation());
		}
		return result;
	}

	private Properties mapToProperties(Map<String, String> map) {
		Properties p = new Properties();
		Set<Map.Entry<String, String>> set = map.entrySet();
		for (Map.Entry<String, String> entry : set) {
			p.put(entry.getKey(), entry.getValue());
		}
		return p;
	}
}