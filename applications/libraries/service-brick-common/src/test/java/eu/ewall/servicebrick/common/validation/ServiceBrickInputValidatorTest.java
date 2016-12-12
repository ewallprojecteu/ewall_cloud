package eu.ewall.servicebrick.common.validation;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

public class ServiceBrickInputValidatorTest {

	private UserTimeZoneProvider mockTimeZoneProvider;
	private ServiceBrickInputValidator inputValidator;
	
	@Before
	public void setUp() {
		mockTimeZoneProvider = mock(UserTimeZoneProvider.class);
		inputValidator = new ServiceBrickInputValidator(mockTimeZoneProvider);
	}
	
	@Test
	public void testCorrectZone() {
		// Checks that validation passes when the input zone and the user zone match
		DateTimeZone userZone = DateTimeZone.forID("Europe/Berlin");
		// Parsing an ISO datetime string only allows to determine the offset (not the full time zone) so use the same
		// configuration in the test
		DateTimeZone fromZone = DateTimeZone.forOffsetHours(1);
		DateTimeZone toZone = DateTimeZone.forOffsetHours(2);
		// from and to are across a daylight savings change to test for different expected offsets while staying in the
		// same time zone
		DateTime from = new DateTime(2015, 1, 1, 0, 0, fromZone);
		DateTime to = new DateTime(2015, 4, 1, 0, 0, toZone);
		String user = "user1";
		when(mockTimeZoneProvider.getUserTimeZone(user)).thenReturn(userZone);
		inputValidator.validateTimeInterval(user, from, to);
	}
	
	@Test
	public void testDifferentZone() {
		// Checks that validation fails when the input zone and the user zone do not match
		DateTimeZone userZone = DateTimeZone.forID("Europe/Berlin");
		DateTimeZone fromZone = DateTimeZone.forOffsetHours(1);
		DateTimeZone toZone = DateTimeZone.forOffsetHours(1);
		DateTime from = new DateTime(2015, 4, 1, 0, 0, fromZone);
		DateTime to = new DateTime(2015, 4, 2, 0, 0, toZone);
		String user = "user1";
		when(mockTimeZoneProvider.getUserTimeZone(user)).thenReturn(userZone);
		try {
			inputValidator.validateTimeInterval(user, from, to);
			Assert.fail("Validation passed unexpectedly");
		} catch (IllegalArgumentException ex) {}
	}
	
}
