package com.acme.fastbook.model.api;

import java.util.List;
import java.util.UUID;

import com.acme.fastbook.model.DateRange;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Availability Dates response with a list of availability dates
 * 
 * @author  Mykhaylo Symulyk
 *
 */
@Data
@AllArgsConstructor
public class AvailabilityDatesResponse {
	
	/** Booking item ID */
	private UUID bookingItemId;
	
	/** List of availability dates */
	private List<DateRange> availabilityDates;
}
