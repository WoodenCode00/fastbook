package com.acme.fastbook.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.fastbook.exception.InvalidRequestException;
import com.acme.fastbook.model.BookingItem;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.model.config.FastBookConfig;
import com.acme.fastbook.persistence.service.BookingItemPersistenceService;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;
import com.acme.fastbook.validation.Validation;
import com.acme.fastbook.validation.ValidationRunner;

/**
 * Controller related to Booking Item requests
 * 
 * @author Mykhaylo Symulyk
 *
 */
@RestController
@RequestMapping(ControllerConstants.BASE_APP_PATH + "/booking-item")
public class BookingItemController {
	
	/** Hundred (100) as BigDecimal */
	private static final BigDecimal HUNDRED = new BigDecimal("100");
	
	/** {@link ReservationPersistenceService} object */
	@Autowired
	private ReservationPersistenceService reservationPersistenceService;
	
	/** {@link BookingItemPersistenceService} object */
	@Autowired
	private BookingItemPersistenceService bookingItemPersistenceService;
	
	/** Application configuration object */
	@Autowired
	private FastBookConfig fastBookConfig;
	
	/**
	 * Endpoint to submit a new reservation
	 * 
	 * @return {@link Reservation} object
	 */
	@PostMapping(value = "/{bookingItemId}/book", consumes = "application/json", produces = "application/json")
	public Reservation newReservation(@PathVariable UUID bookingItemId, @RequestBody Reservation reservation) {
		
		final Optional<BookingItem> bookingItemOpt = bookingItemPersistenceService.findById(bookingItemId);
		
		if (!bookingItemOpt.isPresent()) {
			throw new InvalidRequestException(String.format("BookingItem with id = [%s] is not found.", bookingItemId.toString()));
		}
		
		validateReservationOrThrow(reservation);
		
		final BookingItem bookingItem = bookingItemOpt.get();
		
		// Calculate dailyCostWithReduction
		// Formula: reduction = (reductionPercentage / 100) * baseDailyCost
		final BigDecimal reduction = new BigDecimal(String.valueOf(fastBookConfig.getPromotionConfig().getReductionPercentage()))
				.divide(HUNDRED)
				.multiply(bookingItem.getBaseDailyCost());
		
		final BigDecimal dailyCostWithReduction = bookingItem.getBaseDailyCost().subtract(reduction);
		
		reservation.setId(UUID.randomUUID());
		reservation.setBookingItemId(bookingItemId);
		reservation.setReservationStatus(ReservationStatus.ACTIVE);
		reservation.setDailyCost(dailyCostWithReduction.setScale(2, RoundingMode.DOWN));
		
		return reservationPersistenceService.create(reservation);
	}

	/**
	 * Validates constraints on {@link Reservation} object and throws an exception
	 * if at least one of the validation failed.
	 * 
	 * @param reservation Reservation object to be validated
	 */
	private void validateReservationOrThrow(final Reservation reservation) {
		
		List<Validation<Reservation>> validations = new ArrayList<>();
		
		// Add validations to the list
		validations.add(new Validation<>(
				res -> Objects.nonNull(res.getCustomerName()), 
				"Property reservation.customerName must not be Null."));
		validations.add(new Validation<>(
				res -> Objects.nonNull(res.getCustomerEmail()),
				"Property reservation.customerEmail must not be Null."));
		validations.add(new Validation<>(
				res -> Objects.nonNull(res.getDateRange().getStartDate()),
				"Property reservation.startDate must not be Null."));
		validations.add(new Validation<>(
				res -> Objects.nonNull(res.getDateRange().getEndDate()),
				"Property reservation.endDate must not be Null."));
		
		// check if startDate is not in the past
		if (Objects.nonNull(reservation.getDateRange().getStartDate())) {
			validations.add(new Validation<>(
					res -> res.getDateRange().getStartDate().isAfter(LocalDateTime.now()),
					"Property reservation.startDate must not be in the past."));
		}
		
		// check if startDate is not in the past
		if (Objects.nonNull(reservation.getDateRange().getEndDate())) {
			validations.add(new Validation<>(
					res -> res.getDateRange().getEndDate().isAfter(LocalDateTime.now()),
					"Property reservation.endDate must not be in the past."));
		}
		
		// check if stay period is not beyond the max value
		if (Objects.nonNull(reservation.getDateRange().getStartDate()) && Objects.nonNull(reservation.getDateRange().getEndDate())) {
			final int maxStayPeriod = fastBookConfig.getReservationConfig().getMaxPeriodDays();
			
			validations.add(new Validation<>(
					res -> ChronoUnit.DAYS.between(res.getDateRange().getStartDate(), res.getDateRange().getEndDate()) <= maxStayPeriod,
					"Property reservation.endDate is beyond maximum stay period " + maxStayPeriod + " days."));
		}
		
		// Run all validations
		final String errorMessages = ValidationRunner.runValidations(reservation, validations);
		
		if (!errorMessages.isEmpty()) {
			throw new InvalidRequestException(errorMessages);
		}
	}

}
