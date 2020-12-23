package com.acme.fastbook.exception;

/**
 * Exception to be thrown when {@link ReservationEntity} can not be found
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class ReservationNotFoundException extends RuntimeException {
	
    /** Serial version UID */
	private static final long serialVersionUID = 6829479769711380881L;

	/**
	 * Constructor
	 * 
	 * @param message descriptive message
	 */
	public ReservationNotFoundException(final String message) {
		super(message);
	}
	
	/**
	 * Constructor to wrap original exception into {@link ReservationNotFoundException}
	 * 
	 * @param message descriptive message
	 * @param cause original exception
	 */
	public ReservationNotFoundException(final String message, final Throwable cause) {
		super(message, cause);
	}

}
