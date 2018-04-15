package org.mars.rovers;

import lombok.Builder;
import lombok.Data;

/**
 * Simple configuration container.
 * Has configurable options for API
 */
@Data
@Builder
public class RoverPullingTaskConfiguration {
	private String key;
	private String taskFile;
	private String baseOutputDirectory;
	private String roverURL;
	private int numberOfImageLoaders;
	private boolean dumpJSON;
	private int timeoutInSeconds;

	public static RoverPullingTaskConfiguration buildDefaultConfiguration(final String taskFile, final String baseOutputDirectory) {
		return RoverPullingTaskConfiguration.builder().
				key("DEMO_KEY").
				numberOfImageLoaders(8).
				dumpJSON(false).
				taskFile(taskFile).
				baseOutputDirectory(baseOutputDirectory).
				timeoutInSeconds(300).
				roverURL("https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date={DATE}&api_key={KEY}").build();
	}
}
