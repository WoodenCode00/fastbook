package com.acme.fastbook.model;

/**
 * Constants to represent Reservation statuses
 * 
 * @author Mykhaylo Symulyk
 *
 */
public enum ReservationStatus {

  /** End date of the reservation has not been reached */
  ACTIVE,

  /** Reservation has been cancelled */
  CANCELLED,

  /** End date of the reservation has already been reached */
  COMPLETED
}
