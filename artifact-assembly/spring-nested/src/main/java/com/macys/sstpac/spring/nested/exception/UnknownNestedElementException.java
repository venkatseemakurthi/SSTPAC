package com.macys.sstpac.spring.nested.exception;


public class UnknownNestedElementException extends InvalidConfigurationException {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4264855432630144155L;

	public UnknownNestedElementException(String message) {
        super(message);
    }
}
