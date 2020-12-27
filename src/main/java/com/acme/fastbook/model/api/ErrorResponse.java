package com.acme.fastbook.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Error response model class
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Data
@AllArgsConstructor
public class ErrorResponse {
	
	/** Error status */
	private ErrorStatus errorStatus;
	
	/** Descriptive message */
	private String message;

}
