package com.acme.fastbook.test.config;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.acme.fastbook.model.DateRange;
import com.acme.fastbook.model.Reservation;

/**
 * Collection of JSON requests
 * 
 * @author Mykhaylo Symulyk
 *
 */
public class TestRequestConstant {
	
	/** Input Reservation request */
	public final static Reservation RESERVATION_OBJ = new Reservation(null, null, null, "John Doe", "john.doe@world.com", 
			new DateRange(ZonedDateTime.of(2021, 1, 21, 10, 15, 30, 123, ZoneOffset.UTC), ZonedDateTime.of(2021, 1, 22, 10, 15, 30, 1, ZoneOffset.UTC)), null);

	
	/** Request to create a new Reservation */
	public final static String CREATE_RESERVATION_REQUEST_JSON = 
			            "{"
	      		       +  "\"customer-name\": \"John Doe\","
	      		       +  "\"customer-email\": \"john.doe@world.com\","
	      		       +  "\"date-range\": {"
	      		       +  "  \"start-date\": \"2021-01-21T10:15:30.123Z\","
	      		       +  "  \"end-date\": \"2021-01-22T10:15:30.1Z\""
	      		       +  " }"
	      		       + "}";

}
