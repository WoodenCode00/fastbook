package com.acme.fastbook.validation;

import java.util.function.Predicate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Class encapsulates validation logic
 * 
 * @author Mykhaylo Symulyk
 *
 * @param <T> type of the argument to be validated
 */
@Getter
@Setter
@AllArgsConstructor
public class Validation<T> {

	/** Rule to be used to perform the validation */
	private Predicate<T> rule;

	/** Error message that should be used if validation constraint has not been met */
	private String errorMessage;

	/**
	 * Validate an argument against the {@code rule}
	 * 
	 * @param arg argument to be validated
	 * 
	 * @return true, if rule is validated, false otherwise
	 */
	public boolean validate(T arg) {
		return rule.test(arg);
	}
	
	/**
	 * Validate an argument against the {@code rule} and return associated
	 * error message
	 * 
	 * @param arg argument to be validated
	 * 
	 * @return an associated error message if validation returned false, or an empty String,
	 *         if validation returned true 
	 */
	public String validateAndGetErrorMessage(T arg) {
		return rule.test(arg)? "" : this.errorMessage + "\n";
	}

}
