package com.acme.fastbook.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.acme.fastbook.persistence.model.BookingItemEntity;
import com.acme.fastbook.persistence.model.ReservationEntity;
import com.acme.fastbook.persistence.model.ReservationStatus;

/**
 * DB Repository implementation to manage DB data for {@link ReservationEntity}. It inherits methods from
 * Spring's {@link CrudRepository} interface.
 *  
 * @author Mykhaylo Symulyk
 *
 */
public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID> {
	
	/**
	 * Finds all reservations for a given booking item ID and with both booking dates
	 * beyond or equal to the provided range of dates
	 *   
	 * @param bookingItemId id of {@link BookingItemEntity}
	 * @param startRange start of the search range
	 * @param endRange end of the search range
	 * 
	 * @return list of reservations
	 */
	List<ReservationEntity> findAllByBookingItemIdAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
			UUID bookingItemId,
			LocalDateTime startRange,
			LocalDateTime endRange
	);
	
	/**
	 * Finds and returns all Reservations for the provided bookingItemId and within
	 * the provided time period denoted by {@code startRange} and {@code endRange}. 
	 * Status of the Reservation should not be in the list of {@code excludedStatuses}. 
	 * 
	 * @param bookingItemId id of {@link BookingItemEntity}
	 * @param startRange start of the search range
	 * @param endRange end of the search range
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return list of Reservations
	 */
	@Query("SELECT r FROM ReservationEntity r WHERE "
			+ "r.bookingItemId = :bookingItemId "
			+ "AND r.reservationStatus NOT IN :excludedStatuses "
			+ "AND ("
			+         "(r.startDate <= :startRange AND r.endDate >= :startRange) "
			+       "OR "
			+         "(r.startDate >= :startRange AND r.endDate <= :endRange) "
			+       "OR "
			+         "(r.startDate <= :endRange AND r.endDate >= :endRange) "
			+ ")"
	)
	List<ReservationEntity> findAllForBookingItemIdAndWithinDateRange(
			@Param("bookingItemId") UUID bookingItemId,
			@Param("startRange") LocalDateTime startRange,
			@Param("endRange") LocalDateTime endRange,
			@Param("excludedStatuses") List<ReservationStatus> excludedStatuses
	);
	
}
