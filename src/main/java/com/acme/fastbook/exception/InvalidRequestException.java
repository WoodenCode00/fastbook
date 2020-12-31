package com.acme.fastbook.exception;

/**
 * Exception to be thrown mainly from the controller to indicate that request is
 * invalid
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class InvalidRequestException extends RuntimeException {

  /** Serial version UID */
  private static final long serialVersionUID = 49061389530479339L;

  /**
   * Constructor
   * 
   * @param message descriptive message
   */
  public InvalidRequestException(final String message) {
    super(message);
  }

  /**
   * Constructor to wrap original exception into
   * {@link ReservationNotFoundException}
   * 
   * @param message descriptive message
   * @param cause   original exception
   */
  public InvalidRequestException(final String message, final Throwable cause) {
    super(message, cause);
  }

}
