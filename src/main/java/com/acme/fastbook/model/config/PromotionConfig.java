package com.acme.fastbook.model.config;

import lombok.Data;

/**
 * Configuration properties related to a promotion
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
public class PromotionConfig {
	
	/** Percentage of the reduction to be applied to a base price */
	private float reductionPercentage; 
}
