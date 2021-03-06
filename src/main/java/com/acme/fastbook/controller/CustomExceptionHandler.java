package com.acme.fastbook.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.acme.fastbook.exception.BookingItemNotFoundException;
import com.acme.fastbook.exception.InvalidRequestException;
import com.acme.fastbook.exception.ProcessingException;
import com.acme.fastbook.exception.ReservationCreationException;
import com.acme.fastbook.exception.ReservationNotFoundException;
import com.acme.fastbook.exception.ReservationUpdateException;
import com.acme.fastbook.model.api.ErrorResponse;
import com.acme.fastbook.model.api.ErrorStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler.
 * <p>
 * Other exception handlers managed by Spring can be found in
 * {@link ResponseEntityExceptionHandler} and {@link BasicErrorController}
 * 
 * @author Mykhaylo Symulyk
 *
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

  /**
   * Exception handler class to handle exceptions of type 'resource not found'
   * 
   * @param exception exception object
   * 
   * @return error response to be returned to the client
   */
  @ExceptionHandler({ BookingItemNotFoundException.class, ReservationNotFoundException.class })
  public ResponseEntity<ErrorResponse> handleResourceNotFoundException(final RuntimeException exception) {

    final ErrorResponse errorResponse = new ErrorResponse(ErrorStatus.RESOURCE_NOT_FOUND, exception.getMessage());
    return handleException(errorResponse, HttpStatus.NOT_FOUND, exception);
  }

  /**
   * Exception handler class to handle exceptions of type 'bad request'
   * 
   * @param exception exception object
   * 
   * @return error response to be returned to the client
   */
  @ExceptionHandler({ InvalidRequestException.class })
  public ResponseEntity<ErrorResponse> handleBadRequestException(final RuntimeException exception) {

    final ErrorResponse errorResponse = new ErrorResponse(ErrorStatus.BAD_REQUEST, exception.getMessage());
    return handleException(errorResponse, HttpStatus.BAD_REQUEST, exception);
  }

  /**
   * Handles known 500 exceptions so we can safely return error message to the
   * client
   * 
   * @param exception exception object
   * 
   * @return error response to be returned to the client
   */
  @ExceptionHandler({ ProcessingException.class, ReservationUpdateException.class, ReservationCreationException.class })
  public ResponseEntity<ErrorResponse> handleKnown500Exception(final Exception exception) {

    final ErrorResponse errorResponse = new ErrorResponse(ErrorStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    return handleException(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR, exception);
  }

  /**
   * Handles not anticipated 500 exceptions so we provide a generic error response
   * to not lick the implementation details outside
   * 
   * @param exception exception object
   * 
   * @return error response to be returned to the client
   */
  @ExceptionHandler({ Exception.class })
  public ResponseEntity<ErrorResponse> handleUnknown500Exception(final Exception exception) {

    final ErrorResponse errorResponse = new ErrorResponse(ErrorStatus.INTERNAL_SERVER_ERROR,
        "Internal Server Error: Please contact support for more details.");
    return handleException(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR, exception);
  }

  /**
   * Helper method to build an error response
   * 
   * @param errorResponse application specific ErrorResponse
   * @param httpStatus    HTTP status
   * 
   * @return error response to be returned to the client
   */
  private ResponseEntity<ErrorResponse> handleException(final ErrorResponse errorResponse, final HttpStatus httpStatus,
      final Exception exception) {
    log.error(errorResponse.getMessage(), exception);
    return new ResponseEntity<ErrorResponse>(errorResponse, httpStatus);
  }

}
