package com.acme.fastbook.validation;

import java.util.List;

import lombok.experimental.UtilityClass;

/**
 * Class encapsulates the methods related to {@link Validation} execution
 * 
 * @author Mykhaylo Symulyk
 *
 */
@UtilityClass
public class ValidationRunner {
	
	/**
	 * Runs the list of validations which represent a set of constraints upon the input {@code object}.
	 * Type of the {@code object} and generic type for the list of Validation must be the same.
	 * 
	 * @param <T> type of the object to be validated
	 * @param object object to be validated
	 * @param validations list of {@link Validation}
	 * 
	 * @return String that contains all error messages generated during validation process. 
	 *         If no messages were generated, an empty String will be returned.
	 */
	public static <T> String runValidations(T object, List<Validation<T>> validations) {
		final StringBuilder errorMessages = new StringBuilder();
		
		validations.stream()
		    .forEach(validation -> errorMessages
		    		.append(validation.validateAndGetErrorMessage(object))
		    		.append("\n"));
		
		return errorMessages.toString().trim();
	}

}
