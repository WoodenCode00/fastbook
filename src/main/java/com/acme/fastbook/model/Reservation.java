package com.acme.fastbook.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import com.acme.fastbook.persistence.model.ReservationStatus;

import lombok.Data;

/**
 * Model POJO to represent a reservation
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
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
	
	/** Start date of reservation */
	private LocalDateTime startDate;
	
	/** End date of reservation */
	private LocalDateTime endDate;
	
	/** Cost per day */
	private BigInteger dailyCost;

}
