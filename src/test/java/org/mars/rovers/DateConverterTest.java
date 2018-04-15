package org.mars.rovers;

import org.mars.rovers.components.util.DateConverter;
import org.junit.Test;

import java.time.format.DateTimeParseException;

import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertEquals;

/**
 * Tests date conversions
 */
public class DateConverterTest {
//15-Dec-17
//1-Mar-16
//22-Feb-15
//3-Jul-17
//5-May-19

	@Test
	public void requireNotNull() {
		try {
			DateConverter.convert(null);
			fail("shouldnt accept null, but die here");
		}
		catch (NullPointerException ex) {
			// this is pass, we expect it to not accept null
		}
	}


	@Test
	public void acceptGibberish() {
		try {
			DateConverter.convert("foofooo");
			fail("shouldnt accept gibberish");
		}
		catch (DateTimeParseException ex) {
			// should fail formatting
		}
	}


	@Test
	public void convert() {
		assertEquals("2017-12-15", DateConverter.convert("15-Dec-17"));
		assertEquals("2016-03-01", DateConverter.convert("1-Mar-16"));
	}

}
