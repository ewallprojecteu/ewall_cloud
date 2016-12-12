/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.unit.test;

import static org.junit.Assert.assertNotEquals;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.ewall.platform.commons.datamodel.location.IndoorPlace;
import eu.ewall.platform.commons.datamodel.location.IndoorPlaceArea;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardGenderType;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;
import junit.framework.TestCase;

/**
 * The Class UserTest.
 *
 * @author eandgrg
 */
public class UserTest extends TestCase {

	/** The test user. */
	private User testUser;

	/**
	 * Sets the up.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Before
	public void setUp() throws Exception {
		testUser = new User();

		testUser.setFirstName("TestUserName");
		testUser.setLastName("TestLastName");
		testUser.setUsername("testUsername");

		@SuppressWarnings("deprecation")
		VCardSubProfile vCardSubProfile = new VCardSubProfile("testNickname",
				"testAddress", "testCity", "testState", "testCountry", "Europe/Zagreb", "10000",
				new Date(2014, 6, 6), 63, new Date(2014, 6, 6),
				"testBirthPlace", "testEmail", VCardGenderType.MALE,
				"00385911231231", "0038515675675", new IndoorPlace(
						"testIndoorPlaceName", IndoorPlaceArea.LIVING_ROOM,
						System.currentTimeMillis()));
		UserProfile userProfile = new UserProfile(vCardSubProfile);

		testUser.setUserProfile(userProfile);
		testUser.setUserRole(UserRole.PRIMARY_USER);
	}

	/**
	 * Tear down.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#User()}.
	 */
	@Test
	public void testUser() {
		User myUser = new User();
		assertNotNull(myUser);

	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#getUserID()}.
	 */
	@Test
	public void testGetUserID() {
		assertTrue("testUsername" .equals( testUser.getUsername()));
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#setUserID(java.lang.Integer)}
	 * .
	 */
	@Test
	public void testSetUserID() {
		testUser.setUsername("usrname");
		assertTrue("usrname" .equals(testUser.getUsername()));
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#getFirstName()}.
	 */
	@Test
	public void testGetFirstName() {
		assertEquals(testUser.getFirstName(), "TestUserName");
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#setFirstName(java.lang.String)}
	 * .
	 */
	@Test
	public void testSetFirstName() {
		testUser.setFirstName("someFirstName");
		assertEquals(testUser.getFirstName(), "someFirstName");
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#getLastName()}.
	 */
	@Test
	public void testGetLastName() {
		assertEquals(testUser.getLastName(), "TestLastName");
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#setLastName(java.lang.String)}
	 * .
	 */
	@Test
	public void testSetLastName() {
		testUser.setLastName("someLastName");
		assertEquals(testUser.getLastName(), "someLastName");
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#getUsername()}.
	 */
	@Test
	public void testGetUsername() {
		assertEquals(testUser.getUsername(), "testUsername");
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#setUsername(java.lang.String)}
	 * .
	 */
	@Test
	public void testSetUsername() {
		testUser.setUsername("someUserName");
		assertNotEquals(testUser.getUsername(), "otherUserName");
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#getUserProfile()}
	 * .
	 */
	@Test
	public void testGetUserProfile() {
		VCardSubProfile vCardSubProfile = new VCardSubProfile();
		vCardSubProfile.setAge(63);
		UserProfile up = new UserProfile();
		up.setvCardSubProfile(vCardSubProfile);
		assertTrue(up.getvCardSubProfile().getAge() == testUser
				.getUserProfile().getvCardSubProfile().getAge());
	}

	/**
	 * Test method for
	 * {@link eu.ewall.platform.commons.datamodel.profile.User#getUserRole()}.
	 */
	@Test
	public void testGetUserRole() {
		assertEquals(testUser.getUserRole(), UserRole.PRIMARY_USER);
	}

}
