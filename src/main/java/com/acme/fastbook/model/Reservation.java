package com.acme.fastbook.model;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Model POJO to represent a reservation
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
@AllArgsConstructor
public class Reservation {

	/** Reservation unique id */
	private UUID id;
	
	/** Booking Item ID */
    private UUID bookingItemId;
	
	/** Status of the reservation */
	private ReservationStatus reservationStatus;
	
	/** Customer name */
	private String customerName;
	
	/** Customer email */
	private String customerEmail;
	
	/** Period of reservation */
	private DateRange dateRange;
	
	/** Cost per day */
	private BigDecimal dailyCost;

}
