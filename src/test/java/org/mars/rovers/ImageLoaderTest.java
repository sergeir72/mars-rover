package org.mars.rovers;

import org.mars.rovers.components.RoverImageURLLoader;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.fail;

/**
 * Tests retrieval of images from url
 */
public class ImageLoaderTest {
	static String tempRoot;
	@Before
	public void init() {
		try {
			File tempFile = File.createTempFile("rover-test", "fii");
			tempRoot = tempFile.getAbsolutePath();
			tempRoot = tempRoot.substring(0, tempRoot.lastIndexOf(File.separator));
			tempFile.delete();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Test
	public void testImageLoadingFromURL() {
		String url = "http://mars.jpl.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/00906/opgs/edr/ncam/NRB_477933531EDR_M0450450NCAM00543M_.JPG";
		String date = "2015-01-02";
		RoverPullingTaskConfiguration conf = RoverPullingTaskConfiguration.builder().baseOutputDirectory(tempRoot).build();
		RoverImageURLLoader loader = new RoverImageURLLoader(conf, null, null);

		try {
			String file = loader.loadImageToLocalFile(conf, date + ";" + url);
			assertTrue("should exist" , Files.exists(Paths.get(file)));
			assertTrue("should be not empty", Files.size(Paths.get(file)) > 0);
			// clean up results of test
			Files.delete(Paths.get(file));
		} catch (IOException e) {
			fail("shouldnt happen. Either outoing connections are blocked, or test failing");
		}


	}
}
