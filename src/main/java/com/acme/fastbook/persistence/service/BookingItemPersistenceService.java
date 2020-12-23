package com.acme.fastbook.persistence.service;

import java.util.List;

import com.acme.fastbook.persistence.model.BookingItemEntity;

/**
 * Service interface to perform DB operations on {@link BookingItemEntity} object used 
 * to decouple the system from underlying persistence implementation
 *  
 * @author Mykhaylo Symulyk
 *
 */
public interface BookingItemPersistenceService {
	
	/**
	 * Saves {@link BookingItemEntity} into DB 
	 * 
	 * @param bookingItem {@link BookingItemEntity} object to be saved
	 * 
	 * @return saved {@link BookingItemEntity} object
	 */
	BookingItemEntity create(BookingItemEntity bookingItem);

	/**
	 * Finds all entries in DB
	 * 
	 * @return List of all {@link BookingItemEntity}-s
	 */
	List<BookingItemEntity> findAll();

}
