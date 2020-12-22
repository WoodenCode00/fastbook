package com.acme.fastbook.model.config;

import lombok.Data;

/**
 * Configuration properties related to a reservation
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
public class ReservationConfig {
	
	/** Maximum reservation period */
	private int maxPeriodDays;
	
	/** Minimum number od days we can make the booking in advance */
	private int minAdvanceDays;
	
	/** Maximum number od days we can make the booking in advance */
	private int maxAdvanceDays;
}
