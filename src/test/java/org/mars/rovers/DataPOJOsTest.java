package org.mars.rovers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mars.rovers.api.data.RoverPhoto;
import org.mars.rovers.api.data.RoverResponse;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

/**
 * Tests POJO objects we use to decode responses
 */
public class DataPOJOsTest {
	/**
	 * test that we can read and convert json that is coming back
	 */
	@Test
	public void testPOJOs() {
		String content = load("rover-photo-20150222.json");
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		RoverResponse roverResponse =  gson.fromJson(content, RoverResponse.class);
		assertNotNull(roverResponse);
		assertNotNull(roverResponse.getPhotos());
		assertEquals(120, roverResponse.getPhotos().size());

		for (RoverPhoto roverPhoto : roverResponse.getPhotos()) {
			assertNotNull(roverPhoto.getImgSrc());
			System.out.println(roverPhoto.getImgSrc());
		}
	}

	private String load(final String resource) {
		String result = null;
		try (BufferedReader buffer = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(resource)))) {
			result = buffer.lines().collect(Collectors.joining("\n"));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			fail("shouldnt have exception");
		}
		return result;
	}
}
