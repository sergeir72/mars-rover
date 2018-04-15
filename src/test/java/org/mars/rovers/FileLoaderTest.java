package org.mars.rovers;

import lombok.Getter;
import org.mars.rovers.components.RoverTasksFileLoader;
import org.junit.Test;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests how file is loaded and callback is called
 */
public class FileLoaderTest {
	@Test
	public void testLoading() {
		RoverTasksFileLoader fl = new RoverTasksFileLoader();
		SimpleCallback callback = new SimpleCallback();
		fl.loadFile("TestDates.txt", callback);
		assertEquals(5, callback.getLines());
		assertNull(callback.getError());
	}

	@Test
	public void testLoadingFail() {
		RoverTasksFileLoader fl = new RoverTasksFileLoader();
		SimpleCallback callback = new SimpleCallback();
		fl.loadFile("TestDates2.txt", callback);
		assertFalse(callback.getLines() > 0);
		assertNotNull(callback.getError());
	}

	@Getter
	private static class SimpleCallback implements RoverTasksFileLoader.LineCallback {
		int lines = 0;
		String error = null;
		@Override
		public void process(String date) {
			lines++;
			System.out.println(date);
		}

		@Override
		public void failure(String message) {
			error = message;
		}

		@Override
		public void noFile(String s) {
			error = s;
		}
	}
}
