package com.acme.fastbook.model;

import java.util.List;

import org.mapstruct.Mapper;

import com.acme.fastbook.persistence.model.BookingItemEntity;

/**
 * Mapper used to map to/from {@link BookingItem}
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Mapper(componentModel = "spring")
public interface BookingItemModelMapper {

  /**
   * Maps from com.acme.fastbook.model.BookingItem to
   * com.acme.fastbook.persistence.model.BookingItemEntity
   * 
   * @param source {@link BookingItem} source object
   * 
   * @return {@link BookingItemEntity} object
   */
  BookingItemEntity mapToDbEntity(BookingItem source);

  /**
   * Maps from com.acme.fastbook.persistence.model.BookingItemEntity to
   * com.acme.fastbook.model.BookingItem
   * 
   * @param source {@link BookingItemEntity} source object
   * 
   * @return {@link BookingItem} object
   */
  BookingItem mapToBookingItem(BookingItemEntity source);

  /**
   * Maps from the list of com.acme.fastbook.persistence.model.BookingItemEntity
   * objects to the list of com.acme.fastbook.model.BookingItem objects
   * 
   * @param source list of input
   *               com.acme.fastbook.persistence.model.BookingItemEntity objects
   * 
   * @return list of com.acme.fastbook.model.BookingItem objects
   */
  List<BookingItem> mapToBookingItemList(List<BookingItemEntity> source);
}
