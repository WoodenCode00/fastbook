package com.acme.fastbook.persistence.model;

/**
 * Enum constants to describe {@link Reservation}'s status
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
