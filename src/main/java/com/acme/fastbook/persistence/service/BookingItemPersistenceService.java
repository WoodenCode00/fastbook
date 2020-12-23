package com.acme.fastbook.persistence.service;

import java.util.List;
import java.util.UUID;

import com.acme.fastbook.model.BookingItem;

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
	 * Finds {@link BookingItem} by provided UUID
	 * 
	 * @param id BookingItem id to be searched for
	 * 
	 * @return {@link BookingItem} object
	 */
	BookingItem findById(UUID id);

	/**
	 * Finds all entries in DB
	 * 
	 * @return List of all {@link BookingItem}-s
	 */
	List<BookingItem> findAll();

}
