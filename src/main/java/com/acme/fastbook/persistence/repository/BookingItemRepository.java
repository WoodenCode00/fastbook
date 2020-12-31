package com.acme.fastbook.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.acme.fastbook.persistence.model.BookingItemEntity;

/**
 * DB Repository implementation to manage DB data for {@link BookingItemEntity}.
 * It inherits methods from Spring's {@link CrudRepository} interface.
 * 
 * @author Mykhaylo Symulyk
 *
 */
public interface BookingItemRepository extends CrudRepository<BookingItemEntity, UUID> {

  @Override
  List<BookingItemEntity> findAll();

}
