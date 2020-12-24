package com.acme.fastbook.service;

import java.util.Arrays;

import com.acme.fastbook.exception.ReservationCreationException;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;

/**
 * Class encapsulates the logic related to Reservation
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class ReservationService {

	/**
	 * Checks if dates are still available and creates a reservation
	 * 
	 * @param reservationPersistenceService Reservation DB service
	 * @param reservation {@link Reservation} object
	 * 
	 * @return Reservation object saved to DB
	 */
	public static Reservation checkAndCreate(
			final ReservationPersistenceService reservationPersistenceService,
			final Reservation reservation) {
		
		long nbReservations = reservationPersistenceService.getNumberOfReservations(
				reservation.getBookingItemId(),
				reservation.getDateRange().getStartDate(), 
				reservation.getDateRange().getEndDate(),
				Arrays.asList(ReservationStatus.CANCELLED));
		
		if (nbReservations == 0L) {
			return reservationPersistenceService.create(reservation);
		} else {
			throw new ReservationCreationException("Reservation can no be created: dates are not available.");
		}
	}

}
