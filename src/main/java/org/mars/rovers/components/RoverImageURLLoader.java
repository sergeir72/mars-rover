package org.mars.rovers.components;

import javax.validation.constraints.NotNull;
import lombok.extern.apachecommons.CommonsLog;
import org.mars.rovers.RoverPullingTaskConfiguration;
import org.mars.rovers.api.data.RoverPhoto;
import org.mars.rovers.components.util.OutputFileBuilder;
import org.mars.rovers.RoverPullingTaskApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Thread that listens to queue to pull up date(task)/url(image) pairs.
 * Loads images to local files and goes on.
 * Thread quits and decremenets latch once EOS discovered on queue
 *
 * @see RoverPullingTaskApp
 * @see RoverPhoto
 * @see Thread
 * @see CountDownLatch
 * @see RoverPullingTaskConfiguration
 * @see BlockingQueue
 */
@CommonsLog
public class RoverImageURLLoader extends Thread {
	public static final String EOS = "EOS";
	private final BlockingQueue<String> collectingURLs;
	private final CountDownLatch latch;
	private final RoverPullingTaskConfiguration configuration;

	public RoverImageURLLoader(final RoverPullingTaskConfiguration configuration,
	                           final BlockingQueue<String> collectingURLs,
	                           final CountDownLatch latch) {
		this.collectingURLs = collectingURLs;
		this.latch = latch;
		this.configuration = configuration;
	}

	@Override
	public void run() {
		String urlanddate = null;
		for (;;) {
			try {
				urlanddate = collectingURLs.take();
				if (urlanddate.equals(EOS)) {
					latch.countDown();
					break;
				}
				loadImageToLocalFile(configuration, urlanddate);
			} catch (Exception e) {
				log.error(e.getMessage(), e);
			}

		}
	}

	/**
	 * Loads file from given [date;url] pair.
	 *
	 * exposed for tests to be public.
	 * @param urlanddate
	 * @return fullOutputFileName
	 * @throws IOException
	 */
	public String loadImageToLocalFile(@NotNull final RoverPullingTaskConfiguration configuration,
	                                   @NotNull final String urlanddate) throws IOException {
		String[] split = urlanddate.split(";");
		String outputFile = null;
		if (split.length == 2) {
			String date = split[0];
			String url = split[1];
			outputFile = OutputFileBuilder.build(configuration, url, date);
			String outputDirectory = outputFile.substring(0, outputFile.lastIndexOf(File.separator));
			Path outputDirectoryPath = Paths.get(outputDirectory);
			if (!Files.exists(outputDirectoryPath)) {
				Files.createDirectories(outputDirectoryPath);
			}
			FileOutputStream out = new FileOutputStream(outputFile);
			InputStream in = new URL(url).openStream();
			byte[] buffer = new byte[100000];
			int    bytesRead = 0;

			while((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
			out.close();
			in.close();
		}
		return outputFile;
	}
}
