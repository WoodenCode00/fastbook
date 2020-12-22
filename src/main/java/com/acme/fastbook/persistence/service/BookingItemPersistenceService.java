package com.acme.fastbook.persistence.service;

import java.util.List;

import com.acme.fastbook.persistence.model.BookingItem;

/**
 * Service interface to perform DB operations on {@link BookingItem} object used 
 * to decouple the system from underlying persistence implementation
 *  
 * @author Mykhaylo Symulyk
 *
 */
public interface BookingItemPersistenceService {
	
	/**
	 * Saves {@link BookingItem} into DB 
	 * 
	 * @param bookingItem {@link BookingItem} object to be saved
	 * 
	 * @return saved {@link BookingItem} object
	 */
	BookingItem create(BookingItem bookingItem);

	/**
	 * Finds all entries in DB
	 * 
	 * @return List of all {@link BookingItem}-s
	 */
	List<BookingItem> findAll();

}
