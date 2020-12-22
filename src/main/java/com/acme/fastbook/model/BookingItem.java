package com.acme.fastbook.model;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.UUID;

import lombok.Data;

/**
 * Model POJO to represent a booking item
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
public class BookingItem {
	
	/** Unique ID */
	private UUID id;
	
	/** Item title */
	private String title;
	
	/** Item description */
	private String description;
	
	/** Address where bookable item is located */
	private String address;
	
	/** Base daily cost. It can be adjusted by applying promotions during reservation time. */
	private BigInteger baseDailyCost;
	
	/** Check-in time */
	private LocalTime checkinTime;
	
	/** Check-out time */
	private LocalTime checkoutTime;
}
