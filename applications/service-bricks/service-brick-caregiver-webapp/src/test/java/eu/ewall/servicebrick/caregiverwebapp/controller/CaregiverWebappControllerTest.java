package eu.ewall.servicebrick.caregiverwebapp.controller;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.servicebrick.caregiverwebapp.dao.CaregiverWebappDao;
import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.model.CWAUser;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;
import eu.ewall.servicebrick.physicalactivity.dao.MovementDao;
import eu.ewall.servicebrick.physicalactivity.model.Movement;
import eu.ewall.servicebrick.physicalactivity.model.MovementHistory;

public class CaregiverWebappControllerTest {

	private static final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();
	
	private ServiceBrickInputValidator mockValidator;
	private ProfilingServerDao mockProfilingServerDao;
	private CaregiverWebappDao mockCaregiverWebappDao;
	private CaregiverWebappController controller;
	
	@Before
	public void setUp() {
		mockValidator = mock(ServiceBrickInputValidator.class);
		mockProfilingServerDao = mock(ProfilingServerDao.class);
		mockCaregiverWebappDao = mock(CaregiverWebappDao.class);
		controller = new CaregiverWebappController(mockProfilingServerDao, mockCaregiverWebappDao);
	}
	
	private DateTime getDateTime(String dateTime) {
		return DateTime.parse(dateTime, dateTimeFormatter);
	}
	
	private void addPrimaryUser(List<CWAUser> primaryUsers, String username, String firstName, String lastName, boolean favourite, DateTime lastViewed) {
		CWAUser user = new CWAUser();
		user.setUsername(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setFavourite(favourite);
		user.setLastViewed(lastViewed);
		primaryUsers.add(user);
	}
	
	private List<CWAUser> getCWAUsers(){
		List<CWAUser> primaryUsers = new ArrayList<CWAUser>();
		addPrimaryUser(primaryUsers, "primaryUser1", "firstName", "lastName", false, new DateTime());
		addPrimaryUser(primaryUsers, "primaryUser2", "firstName", "lastName", true, new DateTime());
		addPrimaryUser(primaryUsers, "primaryUser3", "firstName", "lastName", true, new DateTime());
		return primaryUsers;
	}
	
	private void checkResult(List<CWAUser> expected, List<CWAUser> actual) {
		Assert.assertTrue(expected.size() == actual.size());
		for(CWAUser user : expected){
			Assert.assertTrue(actual.contains(user));
		}
	}
	
	@Test
	public void testGetPrimaryUsersExtendedProfile() {
		// Tests when there is no data in the DB for the given range
		String caregiverUsername = "caregiver1";
		when(mockProfilingServerDao.getPrimaryUsersExtendedProfile(caregiverUsername)).thenReturn(getCWAUsers());
		List<CWAUser> expected = getCWAUsers();
		List<CWAUser> actual = controller.getPrimaryUsersExtendedProfile(caregiverUsername);
		checkResult(expected, actual);
	}

}
