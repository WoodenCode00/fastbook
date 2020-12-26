package com.acme.fastbook.model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.acme.fastbook.persistence.model.BookingItemEntity;
import com.acme.fastbook.persistence.model.ReservationEntity;

/**
 * Mapper used to map to/from {@link Reservation}
 * 
 * @author Mykhaylo Symulyk
 *
 */
@Mapper(componentModel = "spring")
public abstract class ReservationModelMapper {

	/** 
	 * Maps from com.acme.fastbook.model.Reservation
	 * to com.acme.fastbook.persistence.model.ReservationEntity
	 * 
	 * @param source source {@link Reservation} object
	 * 
	 * @return {@link ReservationEntity} object mapped from {@link Reservation} source object
	 */
	public ReservationEntity mapToDbEntity(Reservation source) {
		// 1 - partially map properties 
		final ReservationEntity result = partialMappingToDbEntity(source);
		
		// 2 - map remaining DateRange object to start and end dates
		Optional.ofNullable(source.getDateRange())
		    .ifPresent(dateRange -> {
		    	result.setStartDate(dateRange.getStartDate());
		    	result.setEndDate(dateRange.getEndDate());
		    });
		
		return result;
	}
	
	/** 
	 * Partially maps from com.acme.fastbook.model.Reservation
	 * to com.acme.fastbook.persistence.model.ReservationEntity
	 * 
	 * @param source source {@link Reservation} object
	 * 
	 * @return {@link ReservationEntity} object mapped from {@link Reservation} source object
	 */
	@Mapping(target = "startDate", ignore = true)
	@Mapping(target = "endDate", ignore = true)
	protected abstract ReservationEntity partialMappingToDbEntity(Reservation source);

	/**
	 * Maps from com.acme.fastbook.persistence.model.ReservationEntity
	 * to com.acme.fastbook.model.Reservation
	 * 
	 * @param source source {@link ReservationEntity} object
	 * 
	 * @return {@link Reservation} object mapped from {@link ReservationEntity} source object
	 */
	@Named(value = "mainMapperToReservation")
	public Reservation mapToReservation(ReservationEntity source) {
		// 1 - partially map properties 
		final Reservation result = partialMappingToReservation(source);
		
		// 2 - map remaining start and end dates to DateRange object
		final DateRange dateRange = new DateRange(source.getStartDate(), source.getEndDate());
		result.setDateRange(dateRange);
		
		return result;
	}

	/**
	 * Partially maps from com.acme.fastbook.persistence.model.ReservationEntity
	 * to com.acme.fastbook.model.Reservation
	 * 
	 * @param source source {@link ReservationEntity} object
	 * 
	 * @return {@link Reservation} object mapped from {@link ReservationEntity} source object
	 */
	@Mapping(target = "dateRange", ignore = true)
	protected abstract Reservation partialMappingToReservation(ReservationEntity source);
	
	/**
	 * Maps from the list of com.acme.fastbook.persistence.model.ReservationEntity objects
	 * to the list of com.acme.fastbook.model.Reservation objects
	 * 
	 * @param source list of input com.acme.fastbook.persistence.model.ReservationEntity objects
	 * 
	 * @return list of com.acme.fastbook.model.Reservation objects
	 */
	@IterableMapping(qualifiedByName = "mainMapperToReservation")
	public abstract List<Reservation> mapListOfReservationEntitiesToReservations(List<ReservationEntity> source);
	
	@Named(value = "reservationToDateRange")
	@Mapping(target = "startDate", source = "reservation.dateRange.startDate")
	@Mapping(target = "endDate", source = "reservation.dateRange.endDate")
	public abstract DateRange extractDateRange(Reservation reservation);
	
	@IterableMapping(qualifiedByName = "reservationToDateRange")
	public abstract List<DateRange> extractDateRanges(List<Reservation> reservations);
	
	/**
	 * Maps com.acme.fastbook.model.ReservationStatus to
	 * com.acme.fastbook.persistence.model.ReservationStatus
	 * 
	 * @param source com.acme.fastbook.model.ReservationStatus
	 * 
	 * @return com.acme.fastbook.persistence.model.ReservationStatus
	 */
	public abstract com.acme.fastbook.persistence.model.ReservationStatus mapReservationStatusToDbEntity(ReservationStatus source);
	
	/**
	 * Maps list of com.acme.fastbook.model.ReservationStatus
	 * to the list of com.acme.fastbook.persistence.model.ReservationStatus
	 * 
	 * @param source list of com.acme.fastbook.model.ReservationStatus
	 * 
	 * @return list of com.acme.fastbook.persistence.model.ReservationStatus
	 */
	public abstract List<com.acme.fastbook.persistence.model.ReservationStatus> mapListOfStatusesToDbEntityStatuses(List<ReservationStatus> source);
	
	/**
	 * Presents java.util.UUID 
	 * as com.acme.fastbook.persistence.model.BookingItemEntity.
	 * This method is used for conversions in entity class for the field
	 * representing foreign key definition (annotated by ManyToOne).
	 * 
	 * @param id {@link UUID} object
	 * 
	 * @return BookingItemEntity representation of UUID
	 */
    public BookingItemEntity presentUuidAsBookingItemEntity(final UUID id) {

        if (id == null) { return null; }

        final BookingItemEntity bookingItemEntity=new BookingItemEntity();
        bookingItemEntity.setId(id);

        return bookingItemEntity;
    }
    
    /**
     * Presents com.acme.fastbook.persistence.model.BookingItemEntity
     * as java.util.UUID object.
     * This method is used for conversions in entity class for the field
     * representing foreign key definition (annotated by ManyToOne).
     * 
     * @param bookingItemEntity {@link BookingItemEntity} object
     * 
     * @return UUID representation of BookingItemEntity
     */
    protected UUID presentBookingItemEntityAsUuid(final BookingItemEntity bookingItemEntity) {
    	
    	if (bookingItemEntity == null) { return null; }
    	return bookingItemEntity.getId();
    }
	
}
