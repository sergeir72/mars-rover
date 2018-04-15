package org.mars.rovers;


import lombok.extern.apachecommons.CommonsLog;
import org.mars.rovers.components.RoverCommunicationCallback;
import org.mars.rovers.components.RoverImageURLLoader;
import org.mars.rovers.components.RoverTasksFileLoader;

import javax.validation.constraints.NotNull;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * API loads photos from Mars Rover(s) for given dates and
 * puts them in corresponding local directories.
 *
 */
@CommonsLog
public class RoverPullingTaskApp {
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length < 2 || (args.length == 1 && args[1].equalsIgnoreCase("help"))) {
			log.info("requires two parameters - task file path and output directory");
		}
		else {
			RoverPullingTaskConfiguration configuration =
					RoverPullingTaskConfiguration.buildDefaultConfiguration(
							args[0],
							args[1]);
			RoverPullingTaskApp app = new RoverPullingTaskApp();
			app.execute(configuration);
		}
	}

	/**
	 * executes configured task file and puts collected images into configured location
	 * @see RoverPullingTaskConfiguration
	 *
	 * @param configuration
	 */
	public void execute(@NotNull final RoverPullingTaskConfiguration configuration) {

		try {
			LinkedBlockingQueue<String> collectingURLs = new LinkedBlockingQueue<String>(100000);
			CountDownLatch              latch = new CountDownLatch(configuration.getNumberOfImageLoaders());

			for (int i = 0; i < configuration.getNumberOfImageLoaders(); i++) {
				RoverImageURLLoader loader = new RoverImageURLLoader(configuration, collectingURLs, latch);
				loader.start();
			}

			// first wave of calls - get onto dates file, load up json, extract all urls
			RoverTasksFileLoader roverTasksFileLoader = new RoverTasksFileLoader();
			RoverCommunicationCallback RoverCommunicationCallback = new RoverCommunicationCallback(configuration, collectingURLs);
			roverTasksFileLoader.loadFile(configuration.getTaskFile(), RoverCommunicationCallback);
			// let system cool down
			log.info("..done with collecting: total urls to load still... " + collectingURLs.size());

			for (int i = 0; i < configuration.getNumberOfImageLoaders(); i++) {
				collectingURLs.put(RoverImageURLLoader.EOS);
			}

			if (!latch.await(configuration.getTimeoutInSeconds(), TimeUnit.SECONDS)) {
				log.warn("was unable to complete full set of tasks within time limit. ");
			}

			log.info("collection of images from " + configuration.getTaskFile() + " + is completed. Please see files in " + configuration.getBaseOutputDirectory());

		} catch (InterruptedException e) {
			log.error(e.getMessage(), e);
		}
	}
}
