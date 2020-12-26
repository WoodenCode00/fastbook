package com.acme.fastbook.persistence.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.fastbook.exception.ReservationNotFoundException;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationModelMapper;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.persistence.model.BookingItemEntity;
import com.acme.fastbook.persistence.model.ReservationEntity;
import com.acme.fastbook.persistence.repository.ReservationRepository;
import com.acme.fastbook.utils.CopyUtils;

/**
 * Basic implementation of {@link ReservationPersistenceService} interface
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Service
public class BaseReservationPersistenceService implements ReservationPersistenceService {
	
	/** Error message to use when reservation is not found */
	private static final String RESERVATION_NOT_FOUND_ERROR_MSG = "Reservation with ID = [%s] is not found.";
	
	/** Mapper for {@link Reservation} class */
	@Autowired
	private ReservationModelMapper modelMapper;
	
	/** {@link ReservationRepository} bean */
	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public Reservation create(final Reservation reservation) {

		final ReservationEntity reservationEntity = modelMapper.mapToDbEntity(reservation);
		final ReservationEntity resultEntity = reservationRepository.save(reservationEntity);
		
		return modelMapper.mapToReservation(resultEntity);
	}

	@Override
	public List<Reservation> findAllForBookingItemIdAndWithinDateRange(UUID bookingItemId, LocalDateTime startRange,
			LocalDateTime endRange, List<ReservationStatus> excludedStatuses) {
		
		List<com.acme.fastbook.persistence.model.ReservationStatus> statusesAsDbEntities = modelMapper.mapListOfStatusesToDbEntityStatuses(excludedStatuses);
		
		final List<ReservationEntity> entitiesDb = reservationRepository.findAllForBookingItemIdAndWithinDateRange(
				bookingItemId, startRange, endRange, statusesAsDbEntities);
		
		return modelMapper.mapListOfReservationEntitiesToReservations(entitiesDb);
	}

	@Override
	public long getNumberOfReservations(UUID bookingItemId, LocalDateTime startRange, LocalDateTime endRange,
			List<ReservationStatus> excludedStatuses) {
		
		List<com.acme.fastbook.persistence.model.ReservationStatus> statusesAsDbEntities = modelMapper.mapListOfStatusesToDbEntityStatuses(excludedStatuses);

		return reservationRepository.getNumberOfReservations(
				bookingItemId, startRange, endRange, statusesAsDbEntities);
	}

	@Override
	public Reservation update(final Reservation reservation) {
		
		ReservationEntity reservationAsDbEntity = modelMapper.mapToDbEntity(reservation);
		
		ReservationEntity reservationToUpdate = reservationRepository.findById(reservationAsDbEntity.getId())
				.orElseThrow(() -> new ReservationNotFoundException(
					String.format(RESERVATION_NOT_FOUND_ERROR_MSG, reservationAsDbEntity.getId().toString())));

		CopyUtils.copyNonNullProperties(reservationAsDbEntity, reservationToUpdate);

		final ReservationEntity entity = reservationRepository.save(reservationToUpdate);
		
		return modelMapper.mapToReservation(entity);
	}

	@Override
	public Reservation getReservation(UUID id) {
		ReservationEntity reservationEntity = reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(
				    String.format(RESERVATION_NOT_FOUND_ERROR_MSG, id.toString())));
		
		return modelMapper.mapToReservation(reservationEntity);

	}

	@Override
	public List<Reservation> getAllReservationsForBookingItemId(UUID bookingItemId) {
		
		final BookingItemEntity bookingItemEntity = modelMapper.presentUuidAsBookingItemEntity(bookingItemId);
		List<ReservationEntity> reservationsFromDb = reservationRepository.findByBookingItemId(bookingItemEntity);
		
		return modelMapper.mapListOfReservationEntitiesToReservations(reservationsFromDb);
	}

}
