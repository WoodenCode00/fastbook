package com.acme.fastbook.model.helper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.acme.fastbook.model.DateRange;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Helper class for {@link DateRange} class
 * 
 * @author Mykhaylo Symulyk
 *
 */
@UtilityClass
public class DateRangeHelper {

	/**
	 * Transforms the list of reserved date ranges into the list of available date ranges
	 * 
	 * @param startRange beginning of the search range
	 * @param endRange end of the search range
	 * @param reservedRanges list of reserved ranges
	 * 
	 * @return list of available date ranges
	 */
	public static List<DateRange> transformReservedRangesIntoAvailableRanges(
			final @NonNull ZonedDateTime startRange,
			final @NonNull ZonedDateTime endRange,
			final @NonNull List<DateRange> reservedRanges) {
		
		List<DateRange> availabilityRanges = new ArrayList<>();
		
		if (reservedRanges.isEmpty()) { // if empty, then all days are available
			final DateRange dateRange = new DateRange(startRange, endRange);
			availabilityRanges.add(dateRange);
		} else if (reservedRanges.size() == 1 // one DateRange that occupies the whole search range, that is, no days available
				&& (reservedRanges.get(0).getStartDate().isBefore(startRange) || reservedRanges.get(0).getStartDate().equals(startRange))
				&& (reservedRanges.get(0).getEndDate().isAfter(endRange) || reservedRanges.get(0).getEndDate().equals(endRange))) {
			availabilityRanges = new ArrayList<>();
		} else {
			availabilityRanges = transformeAdvancedCase(startRange, endRange, reservedRanges);
		}
		
		return availabilityRanges;
	}
	
	/**
	 * Transforms a list reserved date ranges into the list of available date ranges
	 * 
	 * @param startRange beginning of the search range
	 * @param endRange end of the search range
	 * @param reservedRanges list of reserved ranges
	 * 
	 * @return list of available date ranges
	 */
	private static List<DateRange> transformeAdvancedCase(
			ZonedDateTime startRange,
			ZonedDateTime endRange,
			List<DateRange> reservedRanges) {
		
		List<DateRange> availabilityRanges = new ArrayList<>();
		List<DateRange> reservedRangesExtended = new ArrayList<>(reservedRanges);

		// if first DateRange starts after startRange
		if (reservedRanges.get(0).getStartDate().isAfter(startRange)) {
			// add a synthetic range that ends at startRange. Its startDate is not important
			final DateRange firstSyntheticDateRange = new DateRange(startRange.minusDays(1), startRange);
			reservedRangesExtended.add(0, firstSyntheticDateRange);
		}
		
		// if last DateRange ends before endRange
		if (reservedRanges.get(reservedRanges.size()-1).getEndDate().isBefore(endRange)) {
			// add a synthetic range that starts at endRange. Its endDate is not important
			final DateRange lastSyntheticDateRange = new DateRange(endRange, endRange.plusDays(1));
			reservedRangesExtended.add(lastSyntheticDateRange);
		}
		
        Iterator<DateRange> iterator = reservedRangesExtended.iterator();
        DateRange current = null;
        DateRange next = null;

		while (iterator.hasNext()) {

			// On the first run, take the value from iterator.next(), on consecutive runs, take the value from 'next' variable
			current = (current == null) ? iterator.next() : next; 

			ZonedDateTime startDate = current.getEndDate();

			if (iterator.hasNext()) {
				next = iterator.next();
				ZonedDateTime endDate = next.getStartDate();
				DateRange availabilityDate = new DateRange(startDate, endDate);
				availabilityRanges.add(availabilityDate);
			}
		}

		return availabilityRanges;
	}

}
