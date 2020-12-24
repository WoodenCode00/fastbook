package com.acme.fastbook.persistence.repository;

/** 
 * Class with SQL statements to be used within the persistence package
 * 
 * @author Mykhaylo Symulyk
 *
 */
class SqlConstant {

	/** Select all reservations */
	static final String RESERVATIONS_SELECT_CLAUSE = "SELECT r FROM ReservationEntity r";

	/** Count the number of reservations */
	static final String COUNT_SELECT_CLAUSE = "SELECT COUNT(r) FROM ReservationEntity r";

	/** Where clause to select reservations within the date range excluding some ReservationStatuses */
	static final String WITHIN_DATE_RANGE_WHERE_CLAUSE = 
			" WHERE "
					+ "r.bookingItemId.id = :bookingItemId "
					+ "AND r.reservationStatus NOT IN :excludedStatuses "
					+ "AND ("
					+         "(r.startDate <= :startRange AND r.endDate >= :startRange) "
					+       "OR "
					+         "(r.startDate >= :startRange AND r.endDate <= :endRange) "
					+       "OR "
					+         "(r.startDate <= :endRange AND r.endDate >= :endRange) "
					+ ")";

}
