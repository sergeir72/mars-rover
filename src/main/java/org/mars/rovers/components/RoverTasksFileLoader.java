package org.mars.rovers.components;

import javax.validation.constraints.NotNull;
import lombok.extern.apachecommons.CommonsLog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Performs operation of loading strings from file or from resource in classpath,
 * calling methods on LineCallback object for each line, if line
 * is read or file failed to load or failed to be found.
 */
@CommonsLog
public class RoverTasksFileLoader {
	/**
	 * Consumes file line by line, call callback with date for each line
	 *
	 * File can either be file or included in resource bundle (through class path).
	 * If file exists and resource exists - file will take priority.
	 * If both file and resource misssing - noFile will be called on call back
	 * If exception happens during loadsin - failure will be called on callback
	 *
	 * @param file file name - must never be null
	 * @param callback callback object implementing LineCallback - must never be null
	 */
	public void loadFile(@NotNull  final String file, @NotNull  final LineCallback callback) {

		boolean fileFound = Files.exists(Paths.get(file));
		boolean resourceFound = this.getClass().getClassLoader().getResource(file) != null;
		// file takes priority
		if (fileFound) {
			try (Stream<String> stream = Files.lines(Paths.get(file) )) {
				stream.parallel().forEach(callback::process);
			} catch (IOException e) {
				callback.failure(e.getMessage());
			}
		}
		else
		// second fallback - classpath resource
		if (resourceFound){
			try (BufferedReader buffer = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(file)))) {
				buffer.lines().forEach(callback::process);
			}
			catch (Exception ex) {
				callback.failure(ex.getMessage());
			}
		}
		// we give up and call
		else {
			callback.noFile(file);
		}
	}
	/**
	 * Interface that gets called for every
	 * line in file and does async polling
	 * or if errors are found
	 */
	public  interface LineCallback {
		void process(String date);
		void failure(String message);
		void noFile(String s);
	}
}
