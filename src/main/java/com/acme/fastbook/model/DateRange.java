package com.acme.fastbook.model;

import java.time.ZonedDateTime;

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
  private final ZonedDateTime startDate;

  /** End of the date range */
  private final ZonedDateTime endDate;
}
