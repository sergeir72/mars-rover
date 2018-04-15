package org.mars.rovers.components.util;

import javax.validation.constraints.NotNull;
import lombok.extern.apachecommons.CommonsLog;
import org.mars.rovers.RoverPullingTaskApp;

import java.time.format.DateTimeFormatter;

/**
 * Helper for data conversions from one format to another
 * expected input dates format is d-MMM-yy , while produced output is yyyy-MM-dd
 *
 * this helps us to consume task file
 * @see RoverPullingTaskApp
 */
@CommonsLog
public class DateConverter {
	static final DateTimeFormatter incomingPattern = DateTimeFormatter.ofPattern("d-MMM-yy");
	static final DateTimeFormatter outgoingPattern = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	/**
	 * transforms one type of date formatting into another
	 * from dd-MMM-yyyy to yyyy-mm-dd
	 * e.g 5-May-19 becomes 2019-5-5
	 * @param formattedDate1
	 * @return
	 */
	public static String convert(@NotNull final String formattedDate1) {
		return outgoingPattern.format(incomingPattern.parse(formattedDate1));
	}
}
