package org.mars.rovers.components.util;

import javax.validation.constraints.NotNull;
import org.mars.rovers.RoverPullingTaskConfiguration;

import java.io.File;

/**
 * Normalized image url, base directory from configuration, date of request as
 * BASE_DIR/DATE/FILENAME
 */
public class OutputFileBuilder {
	/**
	 * builds string that represents localized destination
	 * @param configuration
	 * @param imageURL
	 * @param requestedDate
	 * @return
	 */
	public static String build(final @NotNull RoverPullingTaskConfiguration configuration,
	                           @NotNull final String imageURL,
	                           @NotNull final String requestedDate) {
		return configuration.getBaseOutputDirectory() +
				(configuration.getBaseOutputDirectory().endsWith(File.separator) ? "" : File.separator) +
				requestedDate +
				File.separator +
				(imageURL.lastIndexOf("/") == -1 ? imageURL : imageURL.substring(imageURL.lastIndexOf("/") + 1).toLowerCase());
	}
}
