package com.acme.fastbook.persistence.repository;

import java.time.ZonedDateTime;
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
	 * Finds all {@link ReservationEntity} related related to {@code bookingItemId}
	 * 
	 * @param bookingItemId {@link BookingItemEntity} to search for
	 * 
	 * @return list of ReservationEntity
	 */
	List<ReservationEntity> findByBookingItemId(BookingItemEntity bookingItemId);

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
	@Query(SqlConstant.RESERVATIONS_SELECT_CLAUSE + SqlConstant.WITHIN_DATE_RANGE_WHERE_CLAUSE)
	List<ReservationEntity> findAllForBookingItemIdAndWithinDateRange(
			@Param("bookingItemId") UUID bookingItemId,
			@Param("startRange") ZonedDateTime startRange,
			@Param("endRange") ZonedDateTime endRange,
			@Param("excludedStatuses") List<ReservationStatus> excludedStatuses
	);

	/**
	 * Gets the number of reservations for the provided date range
	 * 
	 * @param bookingItemId ID of {@link BookingItemEntity}
	 * @param startRange start of the search range
	 * @param endRange end of the search range
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return number of reservations within the provided range
	 */
	@Query(SqlConstant.COUNT_SELECT_CLAUSE + SqlConstant.WITHIN_DATE_RANGE_WHERE_CLAUSE)
	long getNumberOfReservations(
	    @Param("bookingItemId") UUID bookingItemId,
	    @Param("startRange") ZonedDateTime startRange,
	    @Param("endRange") ZonedDateTime endRange,
	    @Param("excludedStatuses") List<ReservationStatus> excludedStatuses
	);

}
