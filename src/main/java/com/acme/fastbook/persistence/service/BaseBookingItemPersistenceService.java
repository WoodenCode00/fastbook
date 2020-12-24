package com.acme.fastbook.persistence.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	public Optional<BookingItem> findById(UUID id) {
		
		Optional<BookingItemEntity> bookingItemEntityOpt = bookingItemRepository.findById(id);
		
		return bookingItemEntityOpt.isPresent() ? 
				Optional.ofNullable(modelMapper.mapToBookingItem(bookingItemEntityOpt.get())) : Optional.empty();
	}
	
	@Override
	public List<BookingItem> findAll(){
		return modelMapper.mapToBookingItemList(bookingItemRepository.findAll());
	}

}
