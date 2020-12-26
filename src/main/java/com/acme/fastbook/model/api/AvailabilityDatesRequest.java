package com.acme.fastbook.model.api;

import com.acme.fastbook.model.DateRange;

import lombok.Data;

/**
 * Request to be used to get availability dates for a given booking item
 * within the given date range
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
public class AvailabilityDatesRequest {
	/** Search date range */
	private DateRange dateRange;
}
