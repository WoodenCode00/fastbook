package com.acme.fastbook.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.fastbook.exception.BookingItemNotFoundException;
import com.acme.fastbook.exception.InvalidRequestException;
import com.acme.fastbook.model.BookingItem;
import com.acme.fastbook.model.DateRange;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.model.helper.DateRangeHelper;
import com.acme.fastbook.persistence.service.BookingItemPersistenceService;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;
import com.acme.fastbook.validation.Validation;
import com.acme.fastbook.validation.ValidationRunner;

@RestController
@RequestMapping(ControllerConstants.BASE_APP_PATH + "/reservation")
public class ReservationController {
	
	/** {@link ReservationPersistenceService} object */
	@Autowired
	private ReservationPersistenceService reservationPersistenceService;
	
	/** {@link BookingItemPersistenceService} object */
	@Autowired
	private BookingItemPersistenceService bookingItemPersistenceService;
	
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
		
		return reservationPersistenceService.checkDatesAndUpdate(reservation, Collections.emptyList());
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
		
		final List<ReservationStatus> statusesToExclude = Arrays.asList(ReservationStatus.CANCELLED);
		
		validateReservationForUpdateOrThrow(reservation);
		
		// adjust new DateRange based on BookingItem checkin/checkout DB configured values
		final UUID bookingItemId = reservationPersistenceService.getReservation(reservationId).getBookingItemId();
		final BookingItem bookingItem = bookingItemPersistenceService.findById(bookingItemId)
				.orElseThrow(() -> new BookingItemNotFoundException(
						String.format("Update for Reservation with ID = [%s] failed: related BookingItem with ID = [%s] is not found.",
								reservationId.toString(), bookingItemId.toString())));
		
		final DateRange adjustedDateRange = DateRangeHelper.adjustToCheckinCheckoutConfiguredTime(
				bookingItem, reservation.getDateRange().getStartDate(), reservation.getDateRange().getEndDate());
		
		reservation.setId(reservationId);
		reservation.setDateRange(adjustedDateRange);
		
		return reservationPersistenceService.checkDatesAndUpdate(reservation, statusesToExclude);
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
