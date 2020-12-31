package com.acme.fastbook.controller;

import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;
import com.acme.fastbook.test.config.TestRequestConstant;
import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;

/**
 * Integration test to test concurrency when creating new reservations
 * for the same dates
 * 
 * @author Mykhaylo Symulyk
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConcurrencyCreateNewReservationsIT {
	
	/** Controller object */
	@Autowired
	private BookingItemController bookingItemController;
	
	/**
	 * ReservationPersistenceService from the application.
	 * This is static because itis used in the static context
	 * 
	 */
	private static ReservationPersistenceService reservationPersistenceService;
	
	/**
	 * Setter to autowire bean to private static field
	 * 
	 * @param reservationPersistenceService
	 */
	@Autowired
	public void setReservationPersistenceService(ReservationPersistenceService reservationPersistenceService) {
		ConcurrencyCreateNewReservationsIT.reservationPersistenceService = reservationPersistenceService;
	}
	
	/** tempus-fugit rule to run the tests concurrently */
    @Rule
    public ConcurrentRule concurrently = new ConcurrentRule();
    
    /** tempus-fugit rule to run the tests repetitively */
    @Rule
    public RepeatingRule rule = new RepeatingRule();
    
    /** Counter used to keep track of the number of test runs */
    private static final AtomicInteger COUNTER = new AtomicInteger(1);
    
	/**
	 * Tests {@link BookingItemController#newReservation(UUID, Reservation)} method under the load. 
	 * Test case: when submitting new reservations for the same dates, only one should succeed, 
	 * all others should throw an exception
	 */
    @Test
    @Concurrent(count = 100) // number of threads
    @Repeating(repetition = 10) // number of repetitions in each thread
    public void testConcurrency() {
    	try {
    		bookingItemController.newReservation(UUID.fromString("a42d22e0-42fb-11eb-b378-0242ac130002"), TestRequestConstant.RESERVATION_OBJ);
    	} catch (Exception ex) {
    		// Exception is expected to be thrown by application if dates are not available
    		System.out.println("Exception: " + ex.getMessage());
    	}
    	System.out.println("Test execution number: " + COUNTER.getAndIncrement());
    }

    /**
     * In the context of tempusfugit test, this is a convenient place to verify final constraints that should hold
     * after concurrent execution of the test.
     */
    @AfterClass
    public static void finalization() {
    	int nbRows = reservationPersistenceService.getAllReservationsForBookingItemId(
    			UUID.fromString("a42d22e0-42fb-11eb-b378-0242ac130002")).size();
    	assertTrue(String.format("Actual number of rows = [%d]. Only a single row is expected to be written to DB. %nAll other writes with the same dates should be rejected by application.", nbRows),
    			nbRows == 1);
    }

}
