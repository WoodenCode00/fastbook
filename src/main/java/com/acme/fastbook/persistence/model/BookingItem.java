package com.acme.fastbook.persistence.model;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Item that can be booked
 * 
 * @author Mykhaylo Symulyk
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingItem {
	
	/** Unique ID */
	@Id
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
