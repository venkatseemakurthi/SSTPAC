package com.macys.sstpac.spring.nested.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;


public class ContextParentAnnotationHandlerMapping extends
		DefaultAnnotationHandlerMapping implements HandlersRegistry {

	private final Map<Class, RequestMapping> cachedMappings = new HashMap<Class, RequestMapping>();
	private final List<Object> interceptors = new LinkedList<Object>();

	
	protected String[] determineUrlsForHandlerByName(String beanName) {
		List urls = new ArrayList();
		if (beanName.startsWith("/")) {
			urls.add(beanName);
		}
		/*
		 * does nothing due to lack of String[] aliases =
		 * getApplicationContext().getAliases(beanName); for (int i = 0; i <
		 * aliases.length; i++) { if (aliases[i].startsWith("/")) {
		 * urls.add(aliases[i]); } }
		 */
		return StringUtils.toStringArray(urls);
	}

	public Void registerByName(String url, Object controller) {
		String[] urls = determineUrlsForHandlerByName(url);
		register(urls, controller);
		return null;
	}

	private void register(String[] urls, Object handler) {
		if (!ObjectUtils.isEmpty(urls)) {
			// URL paths found: Let's consider it a handler.
			Assert.notNull(urls, "URL path array must not be null");
			for (int j = 0; j < urls.length; j++) {
				registerHandler(urls[j], handler);
			}
		} else {
			if (logger.isDebugEnabled()) {
				logger.debug("Rejected bean '" + handler
						+ "': no URL paths identified");
			}
		}
	}

	public Void registerByAnnotation(Object handler) {
		register(determineUrlsByAnnotations(handler), handler);
		return null;
	}

	public Void registerInterceptor(Object interceptor) {
		interceptors.add(interceptor);
		setInterceptors(interceptors.toArray());
		initInterceptors();
		logger.info("Adding handler interceptor " + interceptor);
		return null;
	}

	protected String[] determineUrlsByAnnotations(Object handler) {
		Class<? extends Object> handlerType = handler.getClass();
		RequestMapping mapping = AnnotationUtils.findAnnotation(handlerType,
				RequestMapping.class);

		if (mapping != null) {
			// @RequestMapping found at type level
			this.cachedMappings.put(handlerType, mapping);
			Set<String> urls = new LinkedHashSet<String>();
			String[] paths = mapping.value();
			if (paths.length > 0) {
				// @RequestMapping specifies paths at type level
				for (String path : paths) {
					addUrlsForPath(urls, path);
				}
				return StringUtils.toStringArray(urls);
			} else {
				// actual paths specified by @RequestMapping at method level
				return determineUrlsForHandlerMethods(handlerType);
			}
		} else if (AnnotationUtils
				.findAnnotation(handlerType, Controller.class) != null) {
			// @RequestMapping to be introspected at method level
			return determineUrlsForHandlerMethods(handlerType);
		} else {
			return null;
		}
	}

	protected void validateHandler(Object handler, HttpServletRequest request)
			throws Exception {
		RequestMapping mapping = this.cachedMappings.get(handler.getClass());
		if (mapping == null) {
			mapping = AnnotationUtils.findAnnotation(handler.getClass(),
					RequestMapping.class);
		}
		if (mapping != null) {
			validateMapping(mapping, request);
		}
	}
}
