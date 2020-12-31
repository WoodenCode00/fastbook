package com.acme.fastbook.exception;

/**
 * Exception to be thrown when {@link Reservation} update failed
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class ReservationUpdateException extends RuntimeException {

  /** Serial version UID */
  private static final long serialVersionUID = 5985133798406619139L;

  /**
   * Constructor
   * 
   * @param message descriptive message
   */
  public ReservationUpdateException(final String message) {
    super(message);
  }

  /**
   * Constructor to wrap original exception into
   * {@link ReservationUpdateException}
   * 
   * @param message descriptive message
   * @param cause   original exception
   */
  public ReservationUpdateException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
