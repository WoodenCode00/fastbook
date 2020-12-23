package com.acme.fastbook.model;

import java.time.LocalDateTime;

import lombok.Data;

/**
 * Class represents a range of dates
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
public class DateRange {
	
	/** Start of the date range */
	private final LocalDateTime startDate;
	
	/** End of the date range */
	private final LocalDateTime endDate;
}
