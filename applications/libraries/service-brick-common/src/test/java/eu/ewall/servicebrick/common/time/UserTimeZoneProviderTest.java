package eu.ewall.servicebrick.common.time;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.joda.time.DateTimeZone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;

public class UserTimeZoneProviderTest {

	private ProfilingServerDao profilingServerDao;
	private UserTimeZoneProvider userTimeZoneProvider;
	
	@Before
	public void setUp() {
		profilingServerDao = mock(ProfilingServerDao.class);
		userTimeZoneProvider = new UserTimeZoneProvider(profilingServerDao);
	}
	
	private User buildUser(String username, String timeZoneId) {
		VCardSubProfile vcard = new VCardSubProfile();
		vcard.setTimezoneid(timeZoneId);
		UserProfile profile = new UserProfile(vcard);
		return new User("test", "test", username, profile, UserRole.PRIMARY_USER);
	}
	
	@Test
	public void testUserNotFound() {
		// Tests when the requested user does not have a profile
		String userId = "user1";
		when(profilingServerDao.getUser(userId)).thenReturn(null);
		DateTimeZone timeZone = userTimeZoneProvider.getUserTimeZone(userId);
		Assert.assertNull(timeZone);
	}
	
	@Test
	public void testTimeZoneNotSet() {
		// Tests when the requested user does not have a time zone set in the profile
		String userId = "user1";
		User user = buildUser(userId, null);
		when(profilingServerDao.getUser(userId)).thenReturn(user);
		DateTimeZone timeZone = userTimeZoneProvider.getUserTimeZone(userId);
		Assert.assertEquals("Europe/Rome", timeZone.getID());
	}
	
	@Test
	public void testCacheHit() {
		// Tests when the time zone for the requested user has already been cached.
		// First call goes to the profiling server, second call to the cache.
		String userId = "user1";
		String timeZoneId = "Europe/Sofia";
		User user = buildUser(userId, timeZoneId);
		when(profilingServerDao.getUser(userId)).thenReturn(user);
		DateTimeZone timeZone = userTimeZoneProvider.getUserTimeZone(userId);
		Assert.assertEquals(timeZoneId, timeZone.getID());
		timeZone = userTimeZoneProvider.getUserTimeZone(userId);
		Assert.assertEquals(timeZoneId, timeZone.getID());
		// Check that profiling server has been invoked only once
		verify(profilingServerDao).getUser(userId);
	}
	
	@Test
	public void testCacheExpiration() throws InterruptedException {
		// Tests when a cached time zone expires before the second request happens.
		String userId = "user1";
		String timeZoneId = "Europe/Sofia";
		userTimeZoneProvider.setCacheTtl(1);
		User user = buildUser(userId, timeZoneId);
		when(profilingServerDao.getUser(userId)).thenReturn(user);
		DateTimeZone timeZone = userTimeZoneProvider.getUserTimeZone(userId);
		Assert.assertEquals(timeZoneId, timeZone.getID());
		Thread.sleep(1100);
		timeZone = userTimeZoneProvider.getUserTimeZone(userId);
		Assert.assertEquals(timeZoneId, timeZone.getID());
		// Check that profiling server has been invoked twice
		verify(profilingServerDao, times(2)).getUser(userId);
	}
	
}
