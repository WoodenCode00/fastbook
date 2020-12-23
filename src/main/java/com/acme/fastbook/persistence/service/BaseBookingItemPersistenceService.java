package com.acme.fastbook.persistence.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.fastbook.exception.BookingItemNotFoundException;
import com.acme.fastbook.model.BookingItem;
import com.acme.fastbook.model.BookingItemModelMapper;
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
	
	@Autowired
	private BookingItemModelMapper modelMapper;
	
	/** {@link BookingItemRepository} bean */
	@Autowired
	private BookingItemRepository bookingItemRepository;
	
	@Override
	public BookingItem create(final BookingItem bookingItem) {
		final BookingItemEntity entityDb = bookingItemRepository.save(modelMapper.mapToDbEntity(bookingItem));
		return modelMapper.mapToBookingItem(entityDb);
	}
	
	@Override
	public BookingItem findById(UUID id) {
		
		final BookingItemEntity bookingItemFromDb = bookingItemRepository.findById(id)
		    .orElseThrow(() -> new BookingItemNotFoundException(
		    		String.format("Booking item with id = [%s] is not found in DB.", id.toString())));
		return modelMapper.mapToBookingItem(bookingItemFromDb);
	}
	
	@Override
	public List<BookingItem> findAll(){
		return modelMapper.mapToBookingItemList(bookingItemRepository.findAll());
	}

}
