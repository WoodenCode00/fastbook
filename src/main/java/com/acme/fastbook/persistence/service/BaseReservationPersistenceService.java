package com.acme.fastbook.persistence.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.fastbook.exception.ReservationNotFoundException;
import com.acme.fastbook.persistence.model.Reservation;
import com.acme.fastbook.persistence.model.ReservationStatus;
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
	
	/** {@link ReservationRepository} bean */
	@Autowired
	private ReservationRepository reservationRepository;

	@Override
	public Reservation create(final Reservation reservation) {
		return reservationRepository.save(reservation);
	}

	@Override
	public List<Reservation> findAllForBookingItemIdAndWithinDateRange(UUID bookingItemId, LocalDateTime startRange,
			LocalDateTime endRange, List<ReservationStatus> excludedStatuses) {
		
		return reservationRepository.findAllForBookingItemIdAndWithinDateRange(bookingItemId, startRange, endRange, excludedStatuses);
	}
	
	@Override
	public Reservation update(final Reservation reservation) {
		
		Reservation reservationToUpdate = reservationRepository.findById(reservation.getId())
				.orElseThrow(() -> new ReservationNotFoundException(
					String.format(RESERVATION_NOT_FOUND_ERROR_MSG, reservation.getId().toString())));

		CopyUtils.copyNonNullProperties(reservation, reservationToUpdate);
		
		return reservationRepository.save(reservationToUpdate);
	}

	@Override
	public Reservation getReservation(UUID id) {
		return reservationRepository.findById(id)
				.orElseThrow(() -> new ReservationNotFoundException(
				    String.format(RESERVATION_NOT_FOUND_ERROR_MSG, id.toString())));

	}

}
