package com.acme.fastbook.persistence.service;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.acme.fastbook.exception.InvalidRequestException;
import com.acme.fastbook.exception.ReservationNotFoundException;
import com.acme.fastbook.exception.ReservationUpdateException;
import com.acme.fastbook.model.BookingItem;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationModelMapper;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.persistence.model.BookingItemEntity;
import com.acme.fastbook.persistence.model.ReservationEntity;
import com.acme.fastbook.persistence.repository.ReservationRepository;
import com.acme.fastbook.utils.CopyUtils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Basic implementation of {@link ReservationPersistenceService} interface
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Service
@Slf4j
public class BaseReservationPersistenceService implements ReservationPersistenceService {
	
	/** Error message to use when reservation is not found */
	private static final String RESERVATION_NOT_FOUND_ERROR_MSG = "Reservation with ID = [%s] is not found.";
	
	/** Mapper for {@link Reservation} class */
	@Autowired
	private ReservationModelMapper modelMapper;
	
	/** {@link ReservationRepository} bean */
	@Autowired
	private ReservationRepository reservationRepository;

	@Transactional
	@Override
	public synchronized Reservation checkDatesAndCreate(final @NonNull Reservation reservation, final List<ReservationStatus> excludedStatuses) {

		long nbReservations = getNumberOfReservations(
				reservation.getBookingItemId(),
				reservation.getDateRange().getStartDate(), 
				reservation.getDateRange().getEndDate(),
				excludedStatuses);
		
		if (nbReservations == 0L) {
			final ReservationEntity reservationEntity = modelMapper.mapToDbEntity(reservation);
			return write(reservationEntity);
		} else {
			throw new InvalidRequestException("Reservation can no be created: dates are not available.");
		}
	}

	/**
	 * @implSpec In this implementation, it is acceptable to not provide new reservation dates. 
	 * In that case the dates check will not be performed thus dates of the existing Reservation will not be updated 
	 * and only semantic properties will be updated.
	 */
	@Transactional
	@Override
	public synchronized Reservation checkDatesAndUpdate(final @NonNull Reservation newReservation, final List<ReservationStatus> excludedStatuses) {

		final UUID reservationId = newReservation.getId();
		final Reservation reservationFromDb = getReservation(reservationId);
		// throw exception if reservation.status is in statusesToExclude list
		final ReservationStatus currentStatus = reservationFromDb.getReservationStatus();
		
		List<ReservationStatus> statuses = Optional.ofNullable(excludedStatuses)
				.orElse(Collections.emptyList());

		if (statuses.contains(currentStatus)) {
			throw new ReservationUpdateException(
					String.format("Reservation with id = [%s] can not be updated because it has status [%s].", 
							reservationId.toString(), currentStatus));
		}

		// Reservations overlapping with new dates. If there is anything else beside this same reservation (with old dates) -> exception
		List<Reservation> overlappingReservations = Collections.emptyList();

		// Check dates if new reservation has new dates provided, otherwise just skip and update semantic information only
		if (newReservation.getDateRange() != null) {
			overlappingReservations = findAllForBookingItemIdAndWithinDateRange(
					newReservation.getBookingItemId(), 
					newReservation.getDateRange().getStartDate(),
					newReservation.getDateRange().getEndDate(), 
					excludedStatuses);
			
			overlappingReservations.removeIf(res -> res.getId().equals(reservationId));
		}
		
		if (overlappingReservations.isEmpty()) {
			CopyUtils.copyNonNullProperties(newReservation, reservationFromDb);
			return write(modelMapper.mapToDbEntity(reservationFromDb));
		} else {
			throw new InvalidRequestException("Reservation can no be updated: dates are not available.");
		}
		
	}
	
	@Transactional(readOnly = true)
	@Override
	public Reservation getReservation(final UUID id) {
		ReservationEntity reservationEntity = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(
				    String.format(RESERVATION_NOT_FOUND_ERROR_MSG, id.toString())));
		
		return modelMapper.mapToReservation(reservationEntity);
	
	}

	@Transactional(readOnly = true)
	@Override
	public List<Reservation> findAllForBookingItemIdAndWithinDateRange(
			final UUID bookingItemId,
			final ZonedDateTime startRange,
			final ZonedDateTime endRange,
			final List<ReservationStatus> excludedStatuses) {
		
		List<com.acme.fastbook.persistence.model.ReservationStatus> statusesAsDbEntities = modelMapper.mapListOfStatusesToDbEntityStatuses(excludedStatuses);
		
		final List<ReservationEntity> entitiesDb = reservationRepository.findAllForBookingItemIdAndWithinDateRange(
				bookingItemId, startRange, endRange, statusesAsDbEntities);
		
		return modelMapper.mapListOfReservationEntitiesToReservations(entitiesDb);
	}
	
	@Transactional(readOnly = true)
	@Override
	public List<Reservation> getAllReservationsForBookingItemId(final UUID bookingItemId) {
		
		final BookingItemEntity bookingItemEntity = modelMapper.presentUuidAsBookingItemEntity(bookingItemId);
		List<ReservationEntity> reservationsFromDb = reservationRepository.findByBookingItemId(bookingItemEntity);
		
		return modelMapper.mapListOfReservationEntitiesToReservations(reservationsFromDb);
	}

	/**
	 * Writes new or updates an existing Reservation
	 * 
	 * @param reservation {@link Reservation} object to be added/updated in DB
	 * 
	 * @return saved {@link Reservation} object
	 */
	private Reservation write(final ReservationEntity reservationEntity) {
	
		final ReservationEntity resultEntity = reservationRepository.save(reservationEntity);
		
		log.info("Reservation with ID = [{}] was created/updated in DB.", reservationEntity.getId().toString());
		return modelMapper.mapToReservation(resultEntity);
	}

	/**
	 * Gets the number of reservations for the provided date range. Reservations in excluded statuses do not
	 * affect the final result. 
	 * 
	 * @param bookingItemId ID of {@link BookingItem}
	 * @param startRange start of the search range
	 * @param endRange end of the search range
	 * @param excludedStatuses List of excluded statuses
	 * 
	 * @return number of reservations within the provided range
	 */
	private long getNumberOfReservations(
			final UUID bookingItemId,
			final ZonedDateTime startRange,
			final ZonedDateTime endRange,
			final List<ReservationStatus> excludedStatuses) {
		
		List<com.acme.fastbook.persistence.model.ReservationStatus> statusesAsDbEntities = modelMapper.mapListOfStatusesToDbEntityStatuses(excludedStatuses);
	
		return reservationRepository.getNumberOfReservations(
				bookingItemId, startRange, endRange, statusesAsDbEntities);
	}

}
