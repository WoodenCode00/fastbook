package com.acme.fastbook.controller;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.acme.fastbook.model.Reservation;
import com.acme.fastbook.persistence.service.ReservationPersistenceService;
import com.acme.fastbook.test.config.TestRequestConstant;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Integration test to test endpoints in {@link BookingItemController}
 * 
 * @author Mykhaylo Symulyk
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BookingItemControllerIT {
	
	/** ReservationPersistenceService from the application */
	@Autowired
	ReservationPersistenceService reservationPersistenceService;
	
	/** Jackson's ObjectMapper */
	@Autowired
	private ObjectMapper objectMapper;
	
	/** Mock Spring MVC bean */
	@Autowired
	private MockMvc mockMvc;

	/**
	 * Integration test to test processing path of {@link BookingItemController#newReservation(UUID, Reservation)) endpoint.
	 * When request is received, then it is expected to be saved in DB
	 * 
	 * @throws Exception
	 */
	@Test
	public void testNewReservation() throws Exception {
		
		MvcResult mvcResult = mockMvc
			   .perform(
				  post("/fastbook/booking-item/{id}/book", "a42d22e0-42fb-11eb-b378-0242ac130002") // UUID from /src/test/resources/insert-booking-item.sql
	                .content(TestRequestConstant.CREATE_RESERVATION_REQUEST_JSON)
	                .contentType(MediaType.APPLICATION_JSON)
	                .accept(MediaType.APPLICATION_JSON))
	           .andExpect(status().is2xxSuccessful())
	           .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
	           .andReturn();
		
		String responseJson = mvcResult.getResponse().getContentAsString();
		Reservation reservation = objectMapper.readValue(responseJson, Reservation.class);
		UUID reservationId = reservation.getId();
		Reservation reservationFromDb = reservationPersistenceService.getReservation(reservationId);
		
		assertNotNull("Request is expected to write an entry in DB.",
				reservationFromDb);
		
	}

}

