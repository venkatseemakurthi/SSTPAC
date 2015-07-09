package com.macys.sstpac.spring.nested.exception;

public abstract class SystemException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = -5514570805807996901L;

	public SystemException() {
        super();
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystemException(Throwable cause) {
        super(cause);
    }
}
