package com.acme.fastbook.utils;

import java.util.function.Predicate;

import com.acme.fastbook.exception.ProcessingException;

/**
 * Interface extends from Java {@link Predicate} interface
 * to allow re-throwing checked exceptions as RuntimeExceptions from within
 * the lambda expression
 * 
 * @author Mykhaylo Symulyk
 *
 * @param <T> the type of the input to the predicate
 */
@FunctionalInterface
public interface ThrowingPredicate<T> extends Predicate<T> {
	
	/**
	 * Default implementation of Java {@link Predicate} functional interface
	 * which allows processing of lambda expressions that can throw an exception,
	 * particularly a checked exception. If exception is thrown it will be converted 
	 * to {@link RuntimeException} and re-thrown.
	 * 
	 * @param arg the input argument
	 * 
	 * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}. If exception is thrown, it will be captured,
     * converted to RuntimeException and re-thrown.	 
     */
	@Override
	default boolean test(T arg) {
		boolean result = false;
		
		try {
			result = testOrThrow(arg);
		} catch (Exception e) {
			throw new ProcessingException(e);
		}
		
		return result;
	}
	
	/**
     * Evaluates this predicate on the given argument. Underlying
     * lambda expression can throw a checked exception.
     *
     * @param arg the input argument
     * 
     * @return {@code true} if the input argument matches the predicate,
     * otherwise {@code false}
     * 
	 * @throws Exception
	 */
	boolean testOrThrow(T arg) throws Exception;
}
