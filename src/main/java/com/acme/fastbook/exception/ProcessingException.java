package com.acme.fastbook.exception;

/**
 * Generic exception used to convert checked exception into RuntimeException.
 * The exception should be used only if more specific one can not be determined.
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class ProcessingException extends RuntimeException {

    /** Serial version UID */
	private static final long serialVersionUID = 2782122738390786878L;
	
	/**
	 * Constructor
	 * 
	 * @param cause original exception wrapped into this one
	 */
	public ProcessingException(final Throwable cause) {
		super(cause);
	}
	
	/**
	 * Constructor
	 * 
	 * @param message descriptive message related to the exception
	 * @param cause original exception wrapped into this one
	 */
	public ProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
