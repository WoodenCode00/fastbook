package com.acme.fastbook.exception;

/**
 * Exception to be thrown when {@link BookingItemEntity} can not be found
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class BookingItemNotFoundException extends RuntimeException {

    /** Serial version UID */
	private static final long serialVersionUID = 2873659375555713597L;

	/**
	 * Constructor
	 * 
	 * @param message descriptive message
	 */
	public BookingItemNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Constructor to wrap original exception into {@link ReservationNotFoundException}
	 * 
	 * @param message descriptive message
	 * @param cause original exception
	 */
	public BookingItemNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
