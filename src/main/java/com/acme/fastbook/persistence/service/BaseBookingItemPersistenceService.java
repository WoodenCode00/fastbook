package com.acme.fastbook.persistence.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.fastbook.persistence.model.BookingItemEntity;
import com.acme.fastbook.persistence.repository.BookingItemRepository;

/**
 * Basic implementation of {@link BookingItemPersistenceService} interface
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Service
public class BaseBookingItemPersistenceService implements BookingItemPersistenceService {
	
	/** {@link BookingItemRepository} bean */
	@Autowired
	private BookingItemRepository bookingItemRepository;
	
	@Override
	public BookingItemEntity create(final BookingItemEntity bookingItem) {
		return bookingItemRepository.save(bookingItem);
	}
	
	@Override
	public List<BookingItemEntity> findAll(){
		return bookingItemRepository.findAll();
	}

}
