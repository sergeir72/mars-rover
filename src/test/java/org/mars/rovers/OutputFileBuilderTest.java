package org.mars.rovers;

import org.mars.rovers.components.util.OutputFileBuilder;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests file name forming
 */
public class OutputFileBuilderTest {
	@Test
	public void testFormingFileNameNormal() {
		Assert.assertEquals("hereandthere/everywhere/2012-01-02/r4r4.jpg", OutputFileBuilder.build(RoverPullingTaskConfiguration.builder().baseOutputDirectory("hereandthere/everywhere").build(), "/r4r4r4/r4r4.jpg", "2012-01-02"));
	}
	@Test
	public void testCornerCaseWithShortURL() {
		assertEquals("hereandthere/everywhere/2012-01-02/r4r4.jpg", OutputFileBuilder.build(RoverPullingTaskConfiguration.builder().baseOutputDirectory("hereandthere/everywhere/").build(), "r4r4.jpg", "2012-01-02"));
	}
	@Test
	public void testCornerCaseWithDirectoryHavingEndingSeparator() {
		assertEquals("hereandthere/everywhere/2012-01-02/r4r4.jpg", OutputFileBuilder.build(RoverPullingTaskConfiguration.builder().baseOutputDirectory("hereandthere/everywhere/").build(), "/r4r4r4/r4r4.jpg", "2012-01-02"));
	}
}
