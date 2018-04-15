package org.mars.rovers.components;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.validation.constraints.NotNull;
import lombok.extern.apachecommons.CommonsLog;
import org.mars.rovers.RoverPullingTaskConfiguration;
import org.mars.rovers.api.data.RoverPhoto;
import org.mars.rovers.api.data.RoverResponse;
import org.mars.rovers.components.util.DateConverter;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

/**
 * Object that encompasses functionality of communication to Rover API
 */
@CommonsLog
public class RoverCommunicationCallback implements RoverTasksFileLoader.LineCallback {
	private final RoverPullingTaskConfiguration configuration;
	private final BlockingQueue<String> urlsCollectionQueue;

	/**
	 * creates instance
	 * @param configuration
	 * @param urlsCollectionQueue
	 */
	public RoverCommunicationCallback(@NotNull final RoverPullingTaskConfiguration configuration,
	                                  @NotNull final BlockingQueue<String>  urlsCollectionQueue) {

		this.configuration = configuration;
		this.urlsCollectionQueue = urlsCollectionQueue;
	}

	/**
	 * Communicates to Rover API using given data.
	 * Gets Json response back, extracts URLs of photos from
	 * that Json, pushes them to urlsCollectionQueue
	 *
	 * @param date
	 */
	public void process(@NotNull final String date) {
		final String roverDate = DateConverter.convert(date);
		final String urlToCall = configuration.getRoverURL().replace("{DATE}", roverDate).replace("{KEY}", configuration.getKey());
		try (CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault()) {
			final CountDownLatch latch = new CountDownLatch(1);
			httpclient.start();
			final HttpGet request = new HttpGet(urlToCall);
			httpclient.execute(request, new FutureCallback<HttpResponse>() {
				public void completed(final HttpResponse response) {
					processResponse(response, roverDate, latch, request);
				}
				public void failed(final Exception ex) {
					latch.countDown();
					log.error(request.getRequestLine() + "->" + ex);
				}

				public void cancelled() {
					latch.countDown();
					log.error(request.getRequestLine() + " cancelled");
				}

			});
			latch.await();
		}
		catch (Exception ex) {
			log.error(ex.getMessage(), ex);
		}
	}

	/**
	 * loads respons body,
	 * transforms it to JSON,
	 * collects URLs from response
	 * dumps it if debugging is required,
	 * decrements latch to stop waiting
	 *
	 * @param response
	 * @param roverDate
	 * @param latch
	 * @param request
	 */
	private void processResponse(final HttpResponse response,
	                             final String roverDate,
	                             final CountDownLatch latch,
	                             final HttpGet request) {
		try {
			final String         destinationDir    = configuration.getBaseOutputDirectory().substring(configuration.getBaseOutputDirectory().lastIndexOf(File.separator));
			final String         json = retrieveJSON(response);
			if (configuration.isDumpJSON()) {
				final String target = configuration.getBaseOutputDirectory() + "." + roverDate.replace("-", "") + ".json";
				Files.write(Paths.get(target), json.getBytes());
			}
			Gson          gson          = new GsonBuilder().setPrettyPrinting().create();
			RoverResponse roverResponse =  gson.fromJson(json, RoverResponse.class);
			if (roverResponse != null) {
				for (RoverPhoto roverPhoto : roverResponse.getPhotos()) {
					String urlanddate = roverDate + ";" + roverPhoto.getImgSrc();
					System.out.println(urlanddate);
					urlsCollectionQueue.add(urlanddate);
				}
			}
		}
		catch (Exception exx) {
			exx.printStackTrace();
		}
		latch.countDown();
		log.debug(request.getRequestLine() + "->" + response.getStatusLine());
	}

	/**
	 * loads response entity as string
	 *
	 * @param response
	 * @return
	 * @throws IOException
	 */
	private String retrieveJSON(final HttpResponse response) throws IOException {
		final StringWriter out = new StringWriter();
		final BufferedReader in  = new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 100000);
		String l;
		while ((l = in.readLine()) != null) {
			out.write(l + "\n");
		}
		out.flush();
		out.close();
		in.close();
		return out.toString();
	}

	@Override
	public void failure(String message) {
		log.error(message);
	}

	@Override
	public void noFile(String s) {
		log.fatal("no file found: " + s);
	}
}
