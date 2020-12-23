package com.acme.fastbook.persistence.model;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Reservation details
 * 
 * @author Mykhaylo Symulyk
 */
@Entity
@Table(name = "reservation")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservationEntity {
	
	/** Reservation unique id */
	@Id
	private UUID id;
	
	/** Booking Item ID */
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_item_id")
    private BookingItemEntity bookingItemId;
	
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
