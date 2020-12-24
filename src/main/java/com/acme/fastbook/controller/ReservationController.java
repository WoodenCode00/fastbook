package com.acme.fastbook.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.fastbook.exception.InvalidRequestException;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;
import com.acme.fastbook.validation.Validation;
import com.acme.fastbook.validation.ValidationRunner;

@RestController
@RequestMapping(ControllerConstants.BASE_APP_PATH + "/reservation")
public class ReservationController {
	
	/** {@link ReservationPersistenceService} object */
	@Autowired
	private ReservationPersistenceService reservationPersistenceService;
	
	/**
	 * Endpoint to cancel the reservation
	 * 
	 * @param reservationId reservation id
	 * 
	 * @return updated Reservation
	 */
	@PutMapping(value = "/{reservationId}/cancel", produces = "application/json")
	public Reservation cancelReservation(@PathVariable UUID reservationId) {
		
		final Reservation reservation = new Reservation();
		reservation.setId(reservationId);
		reservation.setReservationStatus(ReservationStatus.CANCELLED);
		
		return reservationPersistenceService.update(reservation);
	}
	
	/**
	 * Endpoint to update the reservation. Currently only dateRange can be updated.
	 * 
	 * @param reservationId reservation ID
	 * @param reservation {@link Reservation} object
	 * 
	 * @return updated Reservation object
	 */
	@PutMapping(value = "/{reservationId}/update", consumes = "application/json", produces = "application/json")
	public Reservation updateReservation(@PathVariable UUID reservationId, @RequestBody Reservation reservation) {
		
		validateReservationForUpdateOrThrow(reservation);
		reservation.setId(reservationId);
		
		return reservationPersistenceService.update(reservation);
	}

	/**
	 * Validates constraints of Reservation object related to updating request
	 * 
	 * @param reservation reservation to be validated
	 */
	private void validateReservationForUpdateOrThrow(Reservation reservation) {
		
        List<Validation<Reservation>> validations = new ArrayList<>();
		
		// Add validations to the list
        validations.add(new Validation<>(
        		res -> Objects.isNull(res.getId()), 
        		"Property reservation.id is not allowed to be updated. It must be Null."));
        validations.add(new Validation<>(
        		res -> Objects.isNull(res.getBookingItemId()), 
        		"Property reservation.bookingItemId is not allowed to be updated. It must be Null."));
        validations.add(new Validation<>(
        		res -> Objects.isNull(res.getReservationStatus()), 
        		"Property reservation.reservationStatus is not allowed to be updated. It must be Null."));
		validations.add(new Validation<>(
				res -> Objects.isNull(res.getCustomerName()), 
				"Property reservation.customerName is not allowed to be updated. It must be Null."));
		validations.add(new Validation<>(
				res -> Objects.isNull(res.getCustomerEmail()), 
				"Property reservation.customerEmail is not allowed to be updated. It must be Null."));
		validations.add(new Validation<>(
				res -> Objects.isNull(res.getDailyCost()), 
				"Property reservation.dailyCost is not allowed to be updated. It must be Null."));

		// Run all validations
		final String errorMessages = ValidationRunner.runValidations(reservation, validations);
		
		if (!errorMessages.isEmpty()) {
			throw new InvalidRequestException(errorMessages);
		}
		
	}

}