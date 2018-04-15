package org.mars.rovers;

/**
 * System test to help to check whole thing with default configuration
 */
public class RoverPullingTaskAppSystemTest {
	public static void main(String[] args) {
		RoverPullingTaskConfiguration configuration =
				RoverPullingTaskConfiguration.buildDefaultConfiguration(
						"/Users/sergei/dev/mars-rovers-photos/src/test/resources/TestDates.txt",
						"/Users/sergei/dev/mars-rovers-photos/target/");
		RoverPullingTaskApp app = new RoverPullingTaskApp();
		app.execute(configuration);
	}
}
