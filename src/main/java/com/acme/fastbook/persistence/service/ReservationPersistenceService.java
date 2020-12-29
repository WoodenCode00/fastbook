package com.acme.fastbook.persistence.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

import com.acme.fastbook.model.BookingItem;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationStatus;

/**
 * Service interface to perform DB operations on {@link Reservation} object used 
 * to decouple the system from underlying persistence implementation
 *  
 * @author Mykhaylo Symulyk
 *
 */
public interface ReservationPersistenceService {

	/**
	 * Checks if reservation dates are available for the corresponding BookingItem ID and writes new/updated reservation
	 * to DB. Reservations with statuses from the supplied list of {@code excludedStatuses} are excluded from the search logic.
	 * 
	 * @param reservation {@link Reservation} object to write/update in DB
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return Reservation wrote to DB. It might be different from the original reservation object.
	 */
	Reservation checkDatesAndCreate(Reservation reservation, List<ReservationStatus> excludedStatuses);

	/**
	 * Checks if reservation dates are available for the Reservation being updated and updates existing reservation
	 * in DB. Reservations with statuses from the supplied list of {@code excludedStatuses} are excluded from the search logic.
	 * 
	 * @param reservation {@link Reservation} object to write/update in DB.
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return Reservation updated in DB.
	 */
	Reservation checkDatesAndUpdate(Reservation reservation, List<ReservationStatus> excludedStatuses);

	/**
	 * Gets {@link Reservation} object from DB for the provided {@code id}
	 * 
	 * @param id id of Reservation
	 * 
	 * @return {@link Reservation} object from DB
	 */
	Reservation getReservation(UUID id);
	
	/**
	 * Finds all active reservations for provided Booking Item ID and range of dates. Reservations which
	 * are partially covered by provided range of dates will also be included in result.
	 * Status of the Reservation should not be in the list of {@code excludedStatuses}.  
	 * 
	 * @param bookingItemId ID of {@link BookingItem}
	 * @param startRange start of the search range
	 * @param endRange end of the search range
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return List of active reservations
	 */
	List<Reservation> findAllForBookingItemIdAndWithinDateRange(
			UUID bookingItemId, 
			ZonedDateTime startRange,
			ZonedDateTime endRange,
			List<ReservationStatus> excludedStatuses);

	/**
	 * Gets all {@link Reservation}-s for the given {@code bookingItemId}
	 * 
	 * @param bookingItemId booking item ID to be searched for
	 * 
	 * @return .list of Reservation
	 */
	List<Reservation> getAllReservationsForBookingItemId(UUID bookingItemId);

}
