package com.acme.fastbook.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.acme.fastbook.persistence.model.BookingItem;

/**
 * DB Repository implementation to manage DB data for {@link BookingItem}. It inherits methods from
 * Spring's {@link CrudRepository} interface.
 *  
 * @author Mykhaylo Symulyk
 *
 */
public interface BookingItemRepository extends CrudRepository<BookingItem, UUID> {
	
	@Override
	List<BookingItem> findAll();

}
