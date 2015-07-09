package com.macys.sstpac.commonutilities.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import static java.lang.String.valueOf;
import static org.springframework.util.StringUtils.hasText;

import org.apache.log4j.Logger;

public class RequestLoggingFilter implements Filter {

	private static final Logger log = Logger
			.getLogger(RequestLoggingFilter.class);

	private static final char horizontalSeparator = '|';

	public void init(FilterConfig filterConfig) throws ServletException {
		log.debug("RequestLoggingFilter has been initialized");
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (log.isDebugEnabled()) {
			log.debug(extractRequest(request));
		}
		chain.doFilter(request, response);
	}

	public static String extractRequest(ServletRequest request) {
		StringBuilder str = new StringBuilder("Request received: ");

		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

			str.append(httpRequest.getMethod()).append(horizontalSeparator);
			str.append(httpRequest.getRequestURL()).append(horizontalSeparator);
			String queryString = httpRequest.getQueryString();
			if (hasText(queryString)) {
				str.append("Query string - ").append(queryString)
						.append(horizontalSeparator);
			}
			str.append("Content length - ")
					.append(httpRequest.getContentLength())
					.append(horizontalSeparator);
			str.append("Parameters - ")
					.append(extractParameters(httpRequest.getParameterMap()))
					.append(horizontalSeparator);
			str.append("Headers - ").append(extractHeaders(httpRequest))
					.append(horizontalSeparator);
		}

		return str.toString();
	}

	private static String extractHeaders(HttpServletRequest request) {

		Enumeration<String> headers = request.getHeaderNames();

		StringBuilder str = new StringBuilder();
		str.append('{');
		while (headers.hasMoreElements()) {
			String headerName = valueOf(headers.nextElement());
			str.append(headerName).append(':').append(' ');
			Enumeration<String> content = request.getHeaders(headerName);
			while (content.hasMoreElements()) {
				str.append(valueOf(content.nextElement())).append(' ');
			}
			if (headers.hasMoreElements()) {
				str.append(',').append(' ');
			}
		}
		str.append("}");

		return str.toString();
	}

	private static String ENCRYPTED_STRING = "********";
	private static String PASSWORD = "password";

	private static String extractParameters(Map map) {
		if (map == null)
			return "null";

		StringBuffer str = new StringBuffer();

		str.append('{');
		for (Iterator<Map.Entry<String,String>> it = map.entrySet().iterator(); it.hasNext();) {
			Map.Entry<String,String> entry = (Map.Entry<String,String>) it.next();

			String key = value(entry.getKey());
			str.append(key).append('=');

			if (PASSWORD.equalsIgnoreCase(key)) {
				str.append(ENCRYPTED_STRING);
			} else {
				str.append(value(entry.getValue()));
			}

			if (it.hasNext()) {
				str.append(',').append(' ');
			}
		}
		str.append('}');

		return str.toString();
	}

	private static String value(Object o) {
		return o.getClass().isArray() ? valueOfArray((Object[]) o) : valueOf(o);
	}

	private static String valueOfArray(Object[] o) {
		if (o.length == 1)
			return valueOf(o[0]);
		return Arrays.toString(o);
	}

	public void destroy() {
		log.debug("RequestLoggingFilter has been destroyed");
	}

}
