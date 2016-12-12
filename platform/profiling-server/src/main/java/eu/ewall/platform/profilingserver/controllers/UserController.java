/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mongodb.gridfs.GridFSDBFile;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.location.IndoorPlace;
import eu.ewall.platform.commons.datamodel.profile.HealthDiagnosisType;
import eu.ewall.platform.commons.datamodel.profile.HealthSubProfile;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;
import eu.ewall.platform.commons.datamodel.resource.Base64Image;
import eu.ewall.platform.commons.datamodel.service.Application;
import eu.ewall.platform.middleware.datamanager.dao.device.DeviceDao;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.profilingserver.authentication.UserAuthenticatorService;
import eu.ewall.platform.profilingserver.filter.UsersRelationWithSensEnvEnum;
import eu.ewall.platform.profilingserver.services.ApplicationServiceImpl;
import eu.ewall.platform.profilingserver.services.UserServiceImpl;

/**
 * The Class UserController.
 *
 * @author eandgrg, emirmos
 */
@RestController
@RequestMapping(value = "/users")
public class UserController {

    /** The log. */
    private static Logger LOG = LoggerFactory.getLogger(UserController.class);

    /** The env. */
    @Autowired
    private Environment env;

    /** The user service. */
    @Autowired
    private UserServiceImpl userService;

    /** The user authenticator service. */
    @Autowired
    private UserAuthenticatorService userAuthenticatorService;

    /** The application service. */
    @Autowired
    private ApplicationServiceImpl applicationService;

    /** The sens environment dao. */
    private SensingEnvironmentDao sensEnvironmentDao;

    /** The device dao. */
    private DeviceDao deviceDao;

    /**
     * Instantiates a new user controller.
     */
    public UserController() {
	sensEnvironmentDao = new SensingEnvironmentDao();
	deviceDao = new DeviceDao();
    }

    /**
     * Adds the new user.
     *
     * @param userAsContent
     *            the content
     * @return the response entity
     */
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> addNewUser(@RequestBody User userAsContent) {

	LOG.debug("addingNewUser");

	try {
	    if (userAsContent != null) {

		// if the user (with this username) already exist report
		// conflict (409)
		if (userService.addEWallUser(userAsContent) == false) {
		    return new ResponseEntity<String>(HttpStatus.CONFLICT);
		}

	    } else
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

	return new ResponseEntity<String>(HttpStatus.CREATED);

    }

    /**
     * Gets the all e wall users by given filters.
     *
     * @param firstName
     *            the first name
     * @param lastName
     *            the last name
     * @param associatedWithSensEnvFilter
     *            the associated with sens env filter
     * @return the all e wall users by given filters
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getAllEWallUsersByGivenFilters(
	    @RequestParam(value = "firstName", required = false) String firstName,
	    @RequestParam(value = "lastName", required = false) String lastName,
	    @RequestParam(value = "associatedWithSensEnvFilter", required = false, defaultValue = "ALL_USERS") UsersRelationWithSensEnvEnum associatedWithSensEnvFilter) {

	if ((firstName == null || lastName == null)
		&& associatedWithSensEnvFilter == UsersRelationWithSensEnvEnum.ALL_USERS) {
	    LOG.debug("Getting all ewall users ");

	    try {
		List<User> userList = userService.getAllEWallUsers();

		if (userList != null) {
		    return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
		} else {
		    return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	    } catch (Exception e) {
		LOG.warn(e.getMessage());
		return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
	    }

	} else if ((firstName == null || lastName == null)
		&& associatedWithSensEnvFilter == UsersRelationWithSensEnvEnum.ONLY_ASSOCIATED_USERS) {
	    LOG.debug("Getting ewall users associated with sensing environment");

	    try {
		List<User> associatedUsers = getEWallUsersAssociatedWithSensEnv();
		if (associatedUsers != null) {
		    return new ResponseEntity<List<User>>(associatedUsers, HttpStatus.OK);
		} else {
		    return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}

	    } catch (Exception e) {
		LOG.warn(e.getMessage());
		return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
	    }

	} else if ((firstName == null || lastName == null)
		&& associatedWithSensEnvFilter == UsersRelationWithSensEnvEnum.ONLY_NOT_ASSOCIATED_USERS) {
	    LOG.debug("Getting ewall users not associated with sensing environment");

	    try {
		List<User> notAssociatedUsers = getEWallUsersNotAssociatedWithSensEnv();
		if (notAssociatedUsers != null) {
		    return new ResponseEntity<List<User>>(notAssociatedUsers, HttpStatus.OK);
		} else {
		    return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}

	    } catch (Exception e) {
		LOG.warn(e.getMessage());
		return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
	    }

	} else {
	    LOG.info("Getting user with firstname: " + firstName + " and lastname: " + lastName);

	    try {
		List<User> userList = userService.getEWallUserByFirstAndLastname(firstName, lastName);

		if (userList != null) {
		    return new ResponseEntity<List<User>>(userList, HttpStatus.OK);
		} else {
		    return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	    } catch (Exception e) {
		LOG.warn(e.getMessage());
		return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
	    }
	}
    }

    /**
     * Gets the e wall users associated with sens env.
     *
     * @return the e wall users associated with sens env
     * @throws Exception
     *             the exception
     */
    private List<User> getEWallUsersAssociatedWithSensEnv() throws Exception {
	List<User> associatedUsers = new ArrayList<User>();

	List<User> users = userService.getAllEWallUsers();
	List<SensingEnvironment> sensingEnvs = sensEnvironmentDao.getAllSensingEnvironments();

	for (User user : users) {
	    for (SensingEnvironment sensEnv : sensingEnvs) {
		if (user.getUsername().equals(sensEnv.getPrimaryUser())) {
		    associatedUsers.add(user);
		    break;
		}
	    }
	}

	return associatedUsers;
    }

    /**
     * Gets the e wall users not associated with sens env.
     *
     * @return the e wall users not associated with sens env
     * @throws Exception
     *             the exception
     */
    private List<User> getEWallUsersNotAssociatedWithSensEnv() throws Exception {
	List<User> notAssociatedUsers = new ArrayList<User>();

	List<User> users = userService.getAllEWallUsers();
	List<SensingEnvironment> sensingEnvs = sensEnvironmentDao.getAllSensingEnvironments();

	for (User user : users) {
	    boolean associatedUser = false;
	    for (SensingEnvironment sensEnv : sensingEnvs) {
		if (user.getUsername().equals(sensEnv.getPrimaryUser())) {
		    associatedUser = true;
		    break;
		}
	    }
	    if (!associatedUser) {
		notAssociatedUsers.add(user);
	    }
	}

	return notAssociatedUsers;
    }

    /**
     * Gets the e wall user by username.
     *
     * @param username
     *            the username
     * @return the e wall user by username
     */
    @RequestMapping(value = "/{username}", method = RequestMethod.GET)
    public ResponseEntity<User> getEWallUserByUsername(@PathVariable("username") String username) {

	LOG.debug("Getting user with username: " + username);

	try {
	    User user = userService.getEWallUserByUsername(username);

	    if (user != null) {
		return new ResponseEntity<User>(user, HttpStatus.OK);
	    } else {
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	    }
	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Get the set of primary users associated to given (formal) caregiver.
     *
     * @param username
     *            the username of a caregiver
     * @return primary users
     */
    @RequestMapping(value = "/{username}/primaryusers", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getPrimaryUsersAssociatedWithFormalCaregiver(
	    @PathVariable("username") String username) {

	LOG.debug("Checking if user with username: " + username + " is formal caregiver.");
	if (userService.checkIfUserIsFormalCaregiver(username) == false) {

	    // user is not a formal caregiver so the request for obtaining
	    // associated
	    // (primary) users is not accepted (returned status 412 -
	    // precondition failed)
	    return new ResponseEntity<List<User>>(HttpStatus.PRECONDITION_FAILED);
	}

	else {
	    // user is a formal caregiver so the request for obtaining
	    // associated (primary) users is accepted
	    try {
		List<User> primaryUsers = userService.getPrimaryUsersAssociatedWithCaregiver(username);

		if (primaryUsers != null) {
		    return new ResponseEntity<List<User>>(primaryUsers, HttpStatus.OK);
		} else {
		    return new ResponseEntity<List<User>>(HttpStatus.NOT_FOUND);
		}
	    } catch (Exception e) {
		LOG.warn(e.getMessage());
		return new ResponseEntity<List<User>>(HttpStatus.BAD_REQUEST);
	    }
	}
    }

    /**
     * Gets the user time zone id.
     *
     * @param username
     *            the username
     * @return the user time zone id
     */
    @RequestMapping(value = "/{username}/timezoneid", method = RequestMethod.GET)
    public ResponseEntity<String> getUserTimeZoneId(@PathVariable("username") String username) {

	LOG.debug("Getting user's timezone id for username: " + username);

	try {
	    User user = userService.getEWallUserByUsername(username);

	    if (user != null) {
		UserProfile profile = user.getUserProfile();
		if (profile != null) {
		    VCardSubProfile vcard = profile.getvCardSubProfile();
		    if (vcard != null) {
			String tz_id = vcard.getTimezoneid();
			if (tz_id != null) {
			    return new ResponseEntity<String>(tz_id, HttpStatus.OK);
			}
		    }
		}

	    }
	    return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Gets the applications assigned to the user, with details.
     *
     * @param username
     *            the username
     * @return the user applications
     */
    @RequestMapping(value = "/{username}/applications", method = RequestMethod.GET)
    public ResponseEntity<List<Application>> getUserApplications(@PathVariable("username") String username) {

	LOG.debug("Getting applications for username: " + username);
	List<Application> applications = new ArrayList<Application>();
	try {
	    User user = userService.getEWallUserByUsername(username);

	    if (user != null) {
		for (String appName : user.getApplicationNamesList()) {
		    Application app = applicationService.getEWallApplicationByApplicationName(appName);
		    if (app != null) {
			applications.add(app);
		    }
		}
		return new ResponseEntity<List<Application>>(applications, HttpStatus.OK);
	    }
	    return new ResponseEntity<List<Application>>(HttpStatus.NOT_FOUND);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<List<Application>>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Gets the user photo.
     *
     * @param username
     *            the username
     * @return the user photo
     */
    @RequestMapping(value = "/{username}/photo", method = RequestMethod.GET)
    public ResponseEntity<Base64Image> getBase64EncodedUserPhoto(@PathVariable("username") String username) {

	LOG.debug("Getting photo for username: " + username);
	try {
	    GridFSDBFile gridFsFile = userService.getPhotoByUsername(username);
	    if (gridFsFile == null) {
		return new ResponseEntity<Base64Image>(HttpStatus.NO_CONTENT);
	    }

	    String contentType = gridFsFile.getContentType();
	    String fileName = gridFsFile.getFilename();
	    InputStream is = gridFsFile.getInputStream();
	    byte[] binaryImageData = IOUtils.toByteArray(is);
	    byte[] encodedImage = Base64.encodeBase64(binaryImageData);
	    String encodedImageStr = new String(encodedImage);

	    Base64Image base64Image = new Base64Image(fileName, contentType, encodedImageStr);

	    is.close();
	    
	    return new ResponseEntity<Base64Image>(base64Image, HttpStatus.OK);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<Base64Image>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Gets the user photo upload date.
     *
     * @param username
     *            the username
     * @return the user photo upload date
     */
    @RequestMapping(value = "/{username}/photo/uploaddate", method = RequestMethod.GET)
    public ResponseEntity<Date> getUserPhotoUploadDate(@PathVariable("username") String username) {

	LOG.debug("Getting photo upload date  for username: " + username);
	try {
	    GridFSDBFile gridFsFile = userService.getPhotoByUsername(username);

	    if (gridFsFile != null) {
		return ResponseEntity.ok().body(gridFsFile.getUploadDate());
	    }
	    return new ResponseEntity<Date>(HttpStatus.NO_CONTENT);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<Date>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Upload user photo.
     *
     * @param username
     *            the username
     * @param base64Image
     *            the base64 image
     * @return the response entity
     */
    @RequestMapping(value = "/{username}/photo", method = RequestMethod.PUT)
    public ResponseEntity<String> uploadUserPhoto(@PathVariable("username") String username,
	    @RequestBody Base64Image base64Image) {

	try {
	    LOG.debug("Uploading photo named '" + base64Image.getName() + "' for username: " + username);

	    if (username != null) {
		if (userService.uploadUserPhoto(username, base64Image) == true) {
		    return new ResponseEntity<String>(HttpStatus.OK);
		} else
		    return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

	    } else
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Delete user photo.
     *
     * @param username
     *            the username
     * @return the response entity
     */
    @RequestMapping(value = "/{username}/photo", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserPhoto(@PathVariable("username") String username) {

	LOG.debug("Delete photo for username: " + username);

	try {
	    if (userService.deleteUserPhoto(username))

		return new ResponseEntity<String>(HttpStatus.OK);
	    else {
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	    }
	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

    }

    // /**
    // * Gets the e wall user by user id.
    // *
    // * @param userId
    // * the user id
    // * @return the e wall user by user id
    // */
    // @RequestMapping(value = "/}", method = RequestMethod.GET)
    // public ResponseEntity<User> getEWallUserByUserId(
    // @RequestParam("userId") Integer userId) {
    //
    // LOG.info("Getting user with userId: " + userId);
    //
    // try {
    // User user = userService.getEWallUserByUserId(userId);
    //
    // if (user != null) {
    // return new ResponseEntity<User>(user, HttpStatus.OK);
    // } else {
    // return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
    // }
    // } catch (Exception e) {
    // LOG.warn(e.getMessage());
    // return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
    // }
    //
    // }

    /**
     * Modify user username, userId, firstname, lastname and/or role.
     *
     * @param userAsContent
     *            the user as content
     * @param username
     *            the username
     * @return the response entity
     */
    @RequestMapping(value = "/{username}", method = RequestMethod.POST)
    public ResponseEntity<String> modifyUserWithUsername(@RequestBody User userAsContent,
	    @PathVariable("username") String username) {

	LOG.debug("Modify user with username: " + username);

	try {

	    if (userAsContent != null && username != null) {
		if (userService.modifyEWallUserWithUsername(username, userAsContent) == true) {
		    return new ResponseEntity<String>(HttpStatus.OK);
		} else
		    return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

	    } else
		return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Delete user with associated sens env and devices.
     *
     * @param username
     *            the username
     * @return the response entity
     */
    @RequestMapping(value = "/{username}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteUserWithAssociatedSensEnvAndDevices(@PathVariable("username") String username) {

	LOG.debug("Delete user with username: " + username);

	try {
	    /* Deleting devices and sensing environment associated with user */
	    SensingEnvironment sensingEnvironment = sensEnvironmentDao.getSensingEnvironmentByPrimaryUser(username);
	    if (sensingEnvironment != null) {
		deviceDao.deleteDevicesForSensingEnvironment(sensingEnvironment.getObjectId());
		sensEnvironmentDao.deleteSensingEnvironment(sensingEnvironment.getObjectId());
	    }

	    /*
	     * Deleting user credentials related to given username and user
	     * itself
	     */
	    if (userAuthenticatorService.deleteUserCredentialsForUsername(username)
		    && userService.deleteUserWithUsername(username)) {
		return new ResponseEntity<String>(HttpStatus.OK);
	    } else {
		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
	    }
	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Gets the rooms.
     *
     * @param username
     *            the username
     * @return the rooms
     */
    @RequestMapping(value = "/{username}/rooms", method = RequestMethod.GET)
    public ResponseEntity<List<IndoorPlace>> getRooms(@PathVariable("username") String username) {

	LOG.debug("Getting room (indoor place) list for username: " + username);
	List<IndoorPlace> rooms = new ArrayList<IndoorPlace>();
	try {
	    SensingEnvironment env = sensEnvironmentDao.getSensingEnvironmentByPrimaryUser(username);

	    if (env != null) {
		rooms = env.getIndoorPlaces();
		return new ResponseEntity<List<IndoorPlace>>(rooms, HttpStatus.OK);
	    }
	    return new ResponseEntity<List<IndoorPlace>>(HttpStatus.NOT_FOUND);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<List<IndoorPlace>>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Modify indoor place (room) details for given room name and given
     * username.
     *
     * @param roomAsContent
     *            the room as content
     * @param username
     *            the username
     * @return the response entity
     */
    @RequestMapping(value = "/{username}/rooms", method = RequestMethod.POST)
    public ResponseEntity<String> modifyRoomForUsername(@RequestBody IndoorPlace roomAsContent,
	    @PathVariable("username") String username) {

	LOG.debug("For username: " + username + " update details of indoor place (room):" + roomAsContent.getName());

	try {
	    if (roomAsContent != null && username != null && roomAsContent.getName() != null) {
		// get sensing env associated with the given user
		SensingEnvironment env = sensEnvironmentDao.getSensingEnvironmentByPrimaryUser(username);

		if (env != null) {
		    // if there is an indoor place that needs to be updated
		    if (env.getIndoorPlaceNames().contains(roomAsContent.getName())) {
			List<IndoorPlace> newIndoorPlacesList = new ArrayList<IndoorPlace>();

			// iterate through all indoor places belonging to the
			// sensing environment
			for (IndoorPlace indoorPlace : env.getIndoorPlaces())
			    if (indoorPlace.getName().equalsIgnoreCase(roomAsContent.getName()) == false) {
				// copy all others
				newIndoorPlacesList.add(indoorPlace);
			    } else
				// but update the designated
				newIndoorPlacesList.add(roomAsContent);
			// connect the new list (with updated indoor place) to the
			// sensing environment
			env.setIndoorPlaces(newIndoorPlacesList);
			
			//save the updates in the DB
			sensEnvironmentDao.updateSensingEnvironment(env);

			return new ResponseEntity<String>(HttpStatus.OK);
		    }

		    return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);
		}
	    }
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Gets the health diagnosis.
     *
     * @param username
     *            the username
     * @return the health diagnosis
     */
    @RequestMapping(value = "/{username}/diagnosis", method = RequestMethod.GET)
    public ResponseEntity<List<HealthDiagnosisType>> getHealthDiagnosis(@PathVariable("username") String username) {

	LOG.debug("Getting health diagnosis list for username: " + username);
	try {

	    User user = userService.getEWallUserByUsername(username);

	    if (user != null) {
		UserProfile profile = user.getUserProfile();
		if (profile != null) {
		    HealthSubProfile healthSubProfile = profile.getHealthSubProfile();
		    if (healthSubProfile != null) {
			List<HealthDiagnosisType> healthDiagnosisList = healthSubProfile.getHealthDiagnosisType();
			return new ResponseEntity<List<HealthDiagnosisType>>(healthDiagnosisList, HttpStatus.OK);

		    }
		}

	    }
	    return new ResponseEntity<List<HealthDiagnosisType>>(HttpStatus.NOT_FOUND);

	} catch (Exception e) {
	    LOG.warn(e.getMessage());
	    return new ResponseEntity<List<HealthDiagnosisType>>(HttpStatus.BAD_REQUEST);
	}

    }

    /**
     * Inits the deafult data.
     */
    @PostConstruct
    public void initDeafultData() {

	try {
	    String username = env.getProperty("platformAdmin.username");
	    if (username != null || username != "") {
		User pAdminDefault = new User();
		pAdminDefault.setUsername(username);
		pAdminDefault.setUserRole(UserRole.ADMINISTRATOR);
		String appName = env.getProperty("portalApp.name");
		pAdminDefault.addApplicationNameToApplicationNamesList(appName);
		userService.initDeafultData(pAdminDefault);
	    }
	} catch (Exception e) {
	    LOG.error("Error in initDefaultData for user. Message = " + e.getMessage());
	}

    }

}
