package com.macys.sstpac.spring.nested.exception;

public class InvalidConfigurationException extends SystemException{
    /**
	 * 
	 */
	private static final long serialVersionUID = -2901676439891937788L;

	public InvalidConfigurationException() {
        super();
    }

    public InvalidConfigurationException(String message) {
        super(message);
    }

    public InvalidConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidConfigurationException(Throwable cause) {
        super(cause);
    }
}
