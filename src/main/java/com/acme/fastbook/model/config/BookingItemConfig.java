package com.acme.fastbook.model.config;

import lombok.Data;

/**
 * Configuration properties related to a booking item
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
public class BookingItemConfig {

  /** Default value for the availability range in days */
  private int availabilityRangeDays;

}
