package com.acme.fastbook.persistence.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.acme.fastbook.persistence.model.BookingItemEntity;
import com.acme.fastbook.persistence.model.ReservationEntity;
import com.acme.fastbook.persistence.model.ReservationStatus;

/**
 * Service interface to perform DB operations on {@link ReservationEntity} object used 
 * to decouple the system from underlying persistence implementation
 *  
 * @author Mykhaylo Symulyk
 *
 */
public interface ReservationPersistenceService {
	
	/**
	 * Create new entry in DB
	 * 
	 * @param reservation {@link ReservationEntity} object to be added to DB
	 * 
	 * @return saved {@link ReservationEntity} object
	 */
	ReservationEntity create(ReservationEntity reservation);
	
	/**
	 * Gets {@link ReservationEntity} object from DB for the provided {@code id}
	 * 
	 * @param id id of Reservation
	 * 
	 * @return {@link ReservationEntity} object from DB
	 */
	ReservationEntity getReservation(UUID id);
	
	
	/**
	 * Finds all active reservations for provided Booking Item ID and range of dates. Reservations which
	 * are partially covered by provided range of dates will also be included in result.
	 * Status of the Reservation should not be in the list of {@code excludedStatuses}.  
	 * 
	 * @param bookingItemId ID of {@link BookingItemEntity}
	 * @param startRange start of the search range
	 * @param endRange end of the search range
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return List of active reservations
	 */
	List<ReservationEntity> findAllForBookingItemIdAndWithinDateRange(
			UUID bookingItemId, 
			LocalDateTime startRange,
			LocalDateTime endRange,
			List<ReservationStatus> excludedStatuses);

	/**
	 * Updates reservation in DB if entry is found with the same reservation.id 
	 * 
	 * @param reservation source {@link ReservationEntity} object to be used to update an entry in DB
	 * 
	 * @return updated Reservation object from DB
	 */
	ReservationEntity update(ReservationEntity reservation);


}
