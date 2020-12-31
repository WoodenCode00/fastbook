package com.acme.fastbook.exception;

/**
 * Exception to be thrown when Reservation can not be created
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class ReservationCreationException extends RuntimeException {

  /** Serial version UID */
  private static final long serialVersionUID = 1999645728143827926L;

  /**
   * Constructor
   * 
   * @param message descriptive message related to the exception
   */
  public ReservationCreationException(final String message) {
    super(message);
  }

  /**
   * Constructor
   * 
   * @param message descriptive message related to the exception
   * @param cause   original exception wrapped into this one
   */
  public ReservationCreationException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
