# fastbook
Application provides the ability to book a place on a campsite.

Description
An underwater volcano formed a new small island in the Pacific Ocean last month. All the conditions on the island seems perfect and it wasdecided to open it up for the general public to experience the pristine uncharted territory.The island is big enough to host a single campsite so everybody is very excited to visit. In order to regulate the number of people on the island, itwas decided to come up with an online web application to manage the reservations. You are responsible for design and development of a RESTAPI service that will manage the campsite reservations.

To streamline the reservations a few constraints need to be in place:
- The campsite will be free for all.
- The campsite can be reserved for max 3 days.
- The campsite can be reserved minimum 1 day(s) ahead of arrival and up to 1 month in advance.
- Reservations can be cancelled anytime.
- For sake of simplicity assume the check-in & check-out time is 12:00 AM

**System Requirements**
- The users will need to find out when the campsite is available. So the system should expose an API to provide information of theavailability of the campsite for a given date range with the default being 1 month.
- Provide an end point for reserving the campsite. The user will provide his/her email & full name at the time of reserving the campsitealong with intended arrival date and departure date. Return a unique booking identifier back to the caller if the reservation is successful.
- The unique booking identifier can be used to modify or cancel the reservation later on. Provide appropriate end point(s) to allowmodification/cancellation of an existing reservation.
- Due to the popularity of the island, there is a high likelihood of multiple users attempting to reserve the campsite for the same/overlappingdate(s). Demonstrate with appropriate test cases that the system can gracefully handle concurrent requests to reserve the campsite.
- Provide appropriate error messages to the caller to indicate the error cases.
- In general, the system should be able to handle large volume of requests for getting the campsite availability.
- There are no restrictions on how reservations are stored as as long as system constraints are not violated.

**Implementation Details**

The application is a REST web service built with Spring Boot 2.4.1 and Java 8. Project includes Maven wrapper so we can run the application directly from project root folder using provided Maven installation, no need to install Maven in local environment. To do so, use  _./mvnw_  command instead of  _mvn_ . Data are persisted in in-memory DB H2. During the application startup, DB will be prepopulated with one row that represents a campsite (script /fastbook/src/main/resources/data.sql). Application thread safety is implemented using pessimistic locking in Java code. Since the only unsafe part is the communication with DB, to improve the application concurrency we can apply an adequate locking on DB level, depending on the isolation level acceptable for the business. It might require changes in DB schema. 

**List of endpoints**
- create new reservation:
  * POST fastbook/booking-item/{property-id}/book
  
```
{
  "customer-name": "John Doe",
  "customer-email": "john.doe@world.com",
  "date-range": {
    "start-date": "2021-01-21T10:15:30.123Z",
    "end-date": "2021-01-22T10:15:30.1Z"
  }
}
```
- cancel reservation
  * PUT fastbook/reservation/{{reservation_id}}/cancel
  
```
  no request body
```
- update reservation (currently only dates can be updated)
  * PUT fastbook/reservation/{{reservation_id}}/update
  
```
  {
    "date-range": {
        "start-date": "2021-01-20T21:34:55Z",
        "end-date": "2021-01-22T21:34:55Z"
    }
}
```

- get availability dates for the given property
  * GET fastbook/booking-item/{property-id}/get-availability-dates
  
```
  {
    "date-range":{
        "start-date": "2021-01-20T10:15:30.123Z",
        "end-date": "2021-01-23T10:15:30.123Z"
    }
}
```

- get all reservations for a given property ID
  * GET fastbook/booking-item/{property-id}/get-reservations
  
```
no request body
```
  
As property ID, we can use the one that is populated to DB during the startup from /fastbook/src/main/resources/data.sql script, which is a42d22e0-42fb-11eb-b378-0242ac130002 . Example of the URL:  _http://localhost:8080/fastbook/booking-item/a42d22e0-42fb-11eb-b378-0242ac130002/book_

**Integration Tests**

There is one functional integration test and one to test the application under the load. To run IT tests:

```
./mvnw clean verify -DskipITs=false
```

**Run Application**

To run the application on the default port 8080:

```
./mvnw spring-boot:run
```



