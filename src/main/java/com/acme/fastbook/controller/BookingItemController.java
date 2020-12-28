package com.acme.fastbook.controller;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.fastbook.exception.InvalidRequestException;
import com.acme.fastbook.model.BookingItem;
import com.acme.fastbook.model.DateRange;
import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.model.ReservationModelMapper;
import com.acme.fastbook.model.ReservationStatus;
import com.acme.fastbook.model.api.AvailabilityDatesRequest;
import com.acme.fastbook.model.api.AvailabilityDatesResponse;
import com.acme.fastbook.model.config.FastBookConfig;
import com.acme.fastbook.model.helper.DateRangeHelper;
import com.acme.fastbook.persistence.service.BookingItemPersistenceService;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;
import com.acme.fastbook.service.ReservationService;
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
	
	/** Mapper for {@link Reservation} class */
	@Autowired
	private ReservationModelMapper reservationModelMapper;
	
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

		// Adjust start time and end time based on BookingItem checkin/checkout DB configured values
		final DateRange adjustedDateRange = DateRangeHelper.adjustToCheckinCheckoutConfiguredTime(
				bookingItem, reservation.getDateRange().getStartDate(), reservation.getDateRange().getEndDate());

		// Setup Reservation fields
		reservation.setId(UUID.randomUUID());
		reservation.setBookingItemId(bookingItemId);
		reservation.setDateRange(adjustedDateRange);
		reservation.setReservationStatus(ReservationStatus.ACTIVE);
		reservation.setDailyCost(dailyCostWithReduction.setScale(2, RoundingMode.DOWN));

		return ReservationService.checkAndCreate(reservationPersistenceService, reservation);
	}
	
	/**
	 * Gets a list of all {@link Reservation}-s for the given {@code bookingItemId}
	 * 
	 * @param bookingItemId booking item ID
	 * 
	 * @return list of found Reservations
	 */
	@GetMapping(value = "/{bookingItemId}/get-reservations", produces = "application/json")
	public List<Reservation> getAllReservations(@PathVariable UUID bookingItemId) {
		
		final BookingItem bookingItem = bookingItemPersistenceService.findById(bookingItemId)
				.orElseThrow(() -> new InvalidRequestException(String.format("BookingItem with id = [%s] is not found.", bookingItemId.toString())));
		
		return reservationPersistenceService.getAllReservationsForBookingItemId(bookingItem.getId());
	}
	
	/**
	 * Gets availability dates for a given bookingItemId
	 * 
	 * @param bookingItemId booking item ID
	 * @param availabilityDatesRequest request object
	 * 
	 * @return response with the list of available dates
	 */
	@GetMapping(value = "/{bookingItemId}/get-availability-dates", consumes = "application/json", produces = "application/json")
	public AvailabilityDatesResponse getAvailabilityDates(
			@PathVariable UUID bookingItemId,
			@RequestBody AvailabilityDatesRequest availabilityDatesRequest) {
		
		final Optional<BookingItem> bookingItemOpt = bookingItemPersistenceService.findById(bookingItemId);

		if (!bookingItemOpt.isPresent()) {
			throw new InvalidRequestException(String.format("BookingItem with id = [%s] is not found.", bookingItemId.toString()));
		}
		
		final BookingItem bookingItem = bookingItemOpt.get();

		int defaultSearchPeriod = fastBookConfig.getBookingItemConfig().getAvailabilityRangeDays();
		
		final ZonedDateTime startRange = availabilityDatesRequest.getDateRange().getStartDate();

		final ZonedDateTime endRange = Objects.nonNull(availabilityDatesRequest.getDateRange().getEndDate()) ?
				availabilityDatesRequest.getDateRange().getEndDate() : startRange.plusDays(defaultSearchPeriod);
				
		// Adjust search range based on BookingItem checkin/checkout DB configured values
		final DateRange adjustedDateRange = DateRangeHelper.adjustToCheckinCheckoutConfiguredTime(bookingItem, startRange, endRange);

		final List<ReservationStatus> excludeStatuses = Arrays.asList(ReservationStatus.CANCELLED);

		final List<Reservation> reservations = reservationPersistenceService.findAllForBookingItemIdAndWithinDateRange(
				bookingItemId, 
				adjustedDateRange.getStartDate(), 
				adjustedDateRange.getEndDate(), 
				excludeStatuses);

		final List<DateRange> reservedRanges = reservationModelMapper.extractDateRanges(reservations);

		final List<DateRange> availableRanges = DateRangeHelper.transformReservedRangesIntoAvailableRanges(
				adjustedDateRange.getStartDate(), adjustedDateRange.getEndDate(), reservedRanges);
		
		return new AvailabilityDatesResponse(bookingItemId, availableRanges);
	}

	/**
	 * Validates constraints on {@link Reservation} object and throws an exception
	 * if at least one of the validation failed.
	 * 
	 * @param reservation Reservation object to be validated
	 */
	private void validateReservationOrThrow(final Reservation reservation) {
		
		final ZonedDateTime currentTime = ZonedDateTime.now();
		
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
		
		// if startDate is present, do below validations
		if (Objects.nonNull(reservation.getDateRange().getStartDate())) {
			// check if startDate is not in the past
			validations.add(new Validation<>(
					res -> res.getDateRange().getStartDate().isAfter(currentTime),
					"Property reservation.startDate must not be in the past."));
			
			// check if startDate is within minimum advance reservation days
			final int minAdvanceDays = fastBookConfig.getReservationConfig().getMinAdvanceDays();
			validations.add(new Validation<>(
					res -> ChronoUnit.DAYS.between(currentTime, res.getDateRange().getStartDate()) >= minAdvanceDays,
					"Reservation.startDate must be at least " + minAdvanceDays + " day(s) in advance."));
			
			// check if startDate is within maximum advance reservation days
			final int maxAdvanceDays = fastBookConfig.getReservationConfig().getMaxAdvanceDays();
			validations.add(new Validation<>(
					res -> ChronoUnit.DAYS.between(currentTime, res.getDateRange().getStartDate()) <= maxAdvanceDays,
					"Reservation.startDate must not be more than " + maxAdvanceDays + " day(s) in advance."));
			
		}
		
		// check if startDate is not in the past
		if (Objects.nonNull(reservation.getDateRange().getEndDate())) {
			validations.add(new Validation<>(
					res -> res.getDateRange().getEndDate().isAfter(currentTime),
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
