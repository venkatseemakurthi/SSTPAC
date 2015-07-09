package com.macys.sstpac.restservice.framework.client.exception;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "errors")
public class ErrorsBinding implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2838057970906188262L;

	/**
	 * HTTP status is set to HTTP Response and should not be serialized to
	 * JSON/XML response
	 */
	@XmlTransient
	private int httpStatus;

	private List<ErrorsBinding> error;

	public ErrorsBinding() {
	}

	public ErrorsBinding(int httpStatus, ErrorsBinding error) {
		this(httpStatus, Collections.singletonList(error));
	}

	public ErrorsBinding(int httpStatus,
			List<ErrorsBinding> error) {
		if (error.isEmpty()) {
			throw new IllegalArgumentException(
					"Error list should contain at least one error");
		}
		this.httpStatus = httpStatus;
		this.error = error;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public List<ErrorsBinding> getError() {
		return error;
	}

	public void setError(List<ErrorsBinding> error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return "PlatformErrorsBinding{" + "httpStatus=" + httpStatus
				+ ", error=" + error + '}';
	}
}
