package com.acme.fastbook.model.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * Class to hold the application properties
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "fastbook-config")
public class FastBookConfig {
	
	/** {@link BookingItemConfig} properties */
	private BookingItemConfig bookingItemConfig;
	
	/** {@link ReservationConfig} properties */
	private ReservationConfig reservationConfig;
	
	/** {@link PromotionConfig} properties */
	private PromotionConfig promotionConfig;
}
