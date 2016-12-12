/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import eu.ewall.platform.commons.datamodel.ewallsystem.LongPollMessage;
import eu.ewall.platform.commons.datamodel.ewallsystem.PointOfContact;
import eu.ewall.platform.commons.datamodel.ewallsystem.ProxyStatus;
import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.marshalling.json.DM2JsonObjectMapper;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.preferences.SystemPreferences;
import eu.ewall.platform.middleware.cloudgateway.services.DevicesServiceImpl;
import eu.ewall.platform.middleware.cloudgateway.services.RemoteProxyLongPollService;
import eu.ewall.platform.middleware.cloudgateway.services.SensingEnvironmentsServiceImpl;
import eu.ewall.platform.middleware.datamanager.dao.ewallsystem.SensingEnvironmentDao;
import eu.ewall.platform.middleware.datamanager.dao.profile.UserDao;

/**
 * The Class SensingEnvironmentsController.
 *
 * @author emirmos
 */
@RestController
@RequestMapping("/sensingenvironments")
public class SensingEnvironmentsController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(SensingEnvironmentsController.class);

	/** The env. */
	@Autowired
	private Environment env;

	/** The e wall system management service. */
	@Autowired
	private SensingEnvironmentsServiceImpl sensingEnvironmentsService;

	/** The user dao. */
	private UserDao userDao;

	/** The sensing environment dao. */
	private SensingEnvironmentDao sensingEnvironmentDao;

	/** The devices service. */
	@Autowired
	private DevicesServiceImpl devicesService;

	/** The long poll service. */
	@Autowired
	private RemoteProxyLongPollService longPollService;

	/**
	 * Instantiates a new sensing environments controller.
	 */
	public SensingEnvironmentsController() {
		userDao = new UserDao();
		sensingEnvironmentDao = new SensingEnvironmentDao();
	}

	/**
	 * Gets the sensing environments.
	 *
	 * @param regionName
	 *            the region name
	 * @return the sensing environments
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/")
	public ResponseEntity<List<SensingEnvironment>> getSensingEnvironments(
			@RequestParam(value = "regionName", required = false) String regionName) {

		if (regionName == null || regionName.isEmpty()) {
			LOG.debug("Getting all sensing environments");

			try {
				List<SensingEnvironment> sensingEnvironments = sensingEnvironmentsService
						.getSensingEnvironments();
				if (sensingEnvironments != null) {
					return new ResponseEntity<List<SensingEnvironment>>(
							sensingEnvironments, HttpStatus.OK);
				} else
					return new ResponseEntity<List<SensingEnvironment>>(
							HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				LOG.warn(e.toString());
				return new ResponseEntity<List<SensingEnvironment>>(
						HttpStatus.BAD_REQUEST);
			}

		} else {
			LOG.debug("Getting sensing environments related to region: "
					+ regionName);

			try {
				List<SensingEnvironment> sensingEnvironments = getSensEnvsForRegionName(regionName);
				if (sensingEnvironments != null) {
					return new ResponseEntity<List<SensingEnvironment>>(
							sensingEnvironments, HttpStatus.OK);
				} else
					return new ResponseEntity<List<SensingEnvironment>>(
							HttpStatus.BAD_REQUEST);
			} catch (Exception e) {
				LOG.warn(e.toString());
				return new ResponseEntity<List<SensingEnvironment>>(
						HttpStatus.BAD_REQUEST);
			}
		}

	}

	/**
	 * Gets the sens envs for region name.
	 *
	 * @param regionName
	 *            the region name
	 * @return the sens envs for region name
	 * @throws Exception
	 *             the exception
	 */
	private List<SensingEnvironment> getSensEnvsForRegionName(String regionName)
			throws Exception {
		List<SensingEnvironment> includedSensEnvs = new ArrayList<SensingEnvironment>();

		List<SensingEnvironment> sensingEnvs = sensingEnvironmentsService
				.getSensingEnvironments();
		List<User> users = userDao.getAllEWallUsers();

		for (SensingEnvironment sensEnv : sensingEnvs) {
			for (User user : users) {
				if (user.getUsername().equals(sensEnv.getPrimaryUser())
						&& user.getUserProfile().geteWallSubProfile()
								.getRegionName().equals(regionName)) {
					includedSensEnvs.add(sensEnv);
					break;
				}
			}
		}

		return includedSensEnvs;
	}

	/**
	 * Gets the sensing environment.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the sensing environment
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{sensing_environment_id}")
	public ResponseEntity<SensingEnvironment> getSensingEnvironment(
			@PathVariable String sensing_environment_id) {
		LOG.debug("getSensingEnvironmentInfo");

		try {
			SensingEnvironment environment = sensingEnvironmentsService
					.getSensingEnvironment(UUID
							.fromString(sensing_environment_id));

			if (environment != null)
				return new ResponseEntity<SensingEnvironment>(environment,
						HttpStatus.OK);
			else
				return new ResponseEntity<SensingEnvironment>(
						HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<SensingEnvironment>(
					HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Add new sensing environment.
	 *
	 * @param sensingEnvironmentAsContent
	 *            the sensing environment as content
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<String> addNewSensingEnvironment(
			@RequestBody SensingEnvironment sensingEnvironmentAsContent) {

		LOG.debug("addNewSensingEnvironment");

		try {
			if (sensingEnvironmentAsContent != null) {
				// if the sensingEnvironment (with this uuid) already exist,
				// report conflict (409)
				if (sensingEnvironmentsService
						.addSensingEnvironment(sensingEnvironmentAsContent) == false) {
					return new ResponseEntity<String>(HttpStatus.CONFLICT);
				}

			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.CREATED);
	}

	/**
	 * Register sensing environment.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{sensing_environment_id}/register")
	public ResponseEntity<String> registerSensingEnvironment(
			@RequestBody String content,
			@PathVariable String sensing_environment_id) {

		LOG.debug("registerSensingEnvironment with content " + content);

		try {
			PointOfContact poc = null;
			if (content != null) {
				poc = (PointOfContact) DM2JsonObjectMapper.readValueAsString(
						content, PointOfContact.class);
			}

			SensingEnvironment sensingEnvironment = sensingEnvironmentDao
					.getSensingEnvironmentByUUID(sensing_environment_id);

			if (sensingEnvironment == null) {
				LOG.warn("registerSensingEnvironment: Sensing environment "
						+ sensing_environment_id + " does not exist.");
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
			}

			if (!localPlatformVerRequired(poc)) {
				LOG.warn("registerSensingEnvironment: Version of ewall home software is older than minimum required set in CGW props.");
				return new ResponseEntity<String>(HttpStatus.NOT_ACCEPTABLE);
			}

			if (!sensingEnvironment.isEnabled()) {
				LOG.warn("registerSensingEnvironment not done: Sensing environment "
						+ sensing_environment_id + " not enabled.");
				return new ResponseEntity<String>(HttpStatus.METHOD_NOT_ALLOWED);
			}

			if (sensingEnvironmentsService.registerSensingEnvironment(
					sensingEnvironment, poc))
				return new ResponseEntity<String>(HttpStatus.OK);
			else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Local platform ver required.
	 *
	 * @param poc
	 *            the poc
	 * @return true, if successful
	 */
	private boolean localPlatformVerRequired(PointOfContact poc) {
		try {
			String platformReqVerString = env
					.getProperty("local-platform.requiredVersion");

			Version platformReqVer = new Version(platformReqVerString);
			Version localVer = new Version(poc.getLocalPlatformVersion());
			LOG.debug("Version of ewall home software is "
					+ poc.getLocalPlatformVersion());
			LOG.debug("Minimum required version of ewall home software is "
					+ platformReqVerString);
			if (localVer.compareTo(platformReqVer) < 0)
				return false;
			else
				return true;
		} catch (Exception e) {
			LOG.warn("localPlatformVerRequired error: " + e.toString());
			return false;
		}

	}

	/**
	 * Register sensing environment.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{sensing_environment_id}/deregister")
	public ResponseEntity<String> deRegisterSensingEnvironment(
			@RequestBody String content,
			@PathVariable String sensing_environment_id) {

		LOG.debug("deRegisterSensingEnvironment with content " + content);

		try {
			PointOfContact poc = null;
			if (content != null) {
				poc = (PointOfContact) DM2JsonObjectMapper.readValueAsString(
						content, PointOfContact.class);

			}

			if (sensingEnvironmentsService.deRegisterSensingEnvironment(
					UUID.fromString(sensing_environment_id), poc))
				return new ResponseEntity<String>(HttpStatus.OK);
			else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Adds the poc.
	 *
	 * @param content
	 *            the content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{sensing_environment_id}/poc")
	public ResponseEntity<String> addPoc(@RequestBody String content,
			@PathVariable String sensing_environment_id) {

		LOG.debug("Adding poc to :" + sensing_environment_id);

		try {
			PointOfContact poc = null;
			if (content != null) {
				poc = (PointOfContact) DM2JsonObjectMapper.readValueAsString(
						content, PointOfContact.class);

			}

			if (sensingEnvironmentsService.savePoC(
					UUID.fromString(sensing_environment_id), poc))
				return new ResponseEntity<String>(HttpStatus.OK);
			else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Update poc.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.PUT, value = "/{sensing_environment_id}/poc")
	public ResponseEntity<PointOfContact> updatePoc(
			@PathVariable String sensing_environment_id) {

		LOG.debug("Updating poc " + sensing_environment_id);

		return new ResponseEntity<PointOfContact>(HttpStatus.METHOD_NOT_ALLOWED);
	}

	/**
	 * Delete poc.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.DELETE, value = "/{sensing_environment_id}/poc")
	public ResponseEntity<String> deletePoc(
			@PathVariable String sensing_environment_id) {

		LOG.debug("Deleting poc " + sensing_environment_id);

		try {
			sensingEnvironmentsService.deletePoC(UUID
					.fromString(sensing_environment_id));

			return new ResponseEntity<String>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the new ewall requests for remote proxy status on configuration.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param request
	 *            the request
	 * @param remoteProxyRequestMessage
	 *            the remote proxy request message
	 * @return the update
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{sensing_environment_id}/getUpdate")
	@ResponseBody
	public DeferredResult<LongPollMessage> getUpdate(
			@PathVariable String sensing_environment_id,
			HttpServletRequest request,
			@RequestBody LongPollMessage remoteProxyRequestMessage) {
		// LOG.debug("getUpdate");
		DeferredResult<LongPollMessage> defResult = longPollService.getUpdate(
				sensing_environment_id, remoteProxyRequestMessage);

		return defResult;
	}

	/**
	 * Gets the sensing environment configuration.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the sensing environment configuration
	 */
	@RequestMapping(method = RequestMethod.GET, value = "/{sensing_environment_id}/configuration", headers = "Accept=application/json")
	public @ResponseBody DeferredResult<ResponseEntity<String>> getSensingEnvironmentConfiguration(
			@PathVariable String sensing_environment_id) {
		LOG.debug("getSensingEnvironmentConfiguration");

		DeferredResult<ResponseEntity<String>> result = null;

		try {
			SensingEnvironment environment = sensingEnvironmentsService
					.getSensingEnvironment(UUID
							.fromString(sensing_environment_id));

			if (environment == null) {
				DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
				def.setResult(new ResponseEntity<String>(
						HttpStatus.INTERNAL_SERVER_ERROR));
				return def;
			}

			ProxyStatus proxyStatus = environment.getPointOfContact()
					.getProxyStatus();

			if (proxyStatus == ProxyStatus.ONLINE) {

				// GET remote proxy configuration
				result = longPollService
						.setRPConfigRequest(sensing_environment_id);

			} else {
				DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
				def.setResult(new ResponseEntity<String>(
						HttpStatus.GATEWAY_TIMEOUT));
				return def;
			}
		} catch (Exception e) {
			LOG.warn(e.toString());
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
			return def;
		}

		return result;
	}

	/**
	 * Sets the sensing environment configuration.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param config
	 *            the config
	 * @return the response entity
	 */
	@RequestMapping(method = RequestMethod.POST, value = "/{sensing_environment_id}/configuration")
	public DeferredResult<ResponseEntity<String>> setSensingEnvironmentConfiguration(
			@PathVariable String sensing_environment_id,
			@RequestBody Map<String, String> config) {

		LOG.debug("setSensingEnvironmentConfiguration" + sensing_environment_id
				+ ", " + config);

		DeferredResult<ResponseEntity<String>> result = null;

		try {
			SensingEnvironment environment = sensingEnvironmentsService
					.getSensingEnvironment(UUID
							.fromString(sensing_environment_id));

			if (environment == null) {
				DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
				def.setResult(new ResponseEntity<String>(
						HttpStatus.INTERNAL_SERVER_ERROR));
			}

			// POST configuration to remote proxy
			result = longPollService.setRPConfigData(sensing_environment_id,
					config);

		} catch (Exception e) {
			LOG.warn(e.toString());
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
		}

		return result;
	}

	/**
	 * Update sensing environment.
	 *
	 * @param sensingEnvironmentAsContent
	 *            the sensing environment as content
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(value = "/{sensing_environment_id}", method = RequestMethod.POST)
	public ResponseEntity<String> updateSensingEnvironment(
			@RequestBody SensingEnvironment sensingEnvironmentAsContent,
			@PathVariable("sensing_environment_id") String sensing_environment_id) {

		LOG.debug("updateSensingEnvironment:" + sensing_environment_id);

		try {
			if (sensingEnvironmentAsContent != null
					&& sensing_environment_id != null) {
				if (sensingEnvironmentsService
						.updateSensingEnvironment(sensingEnvironmentAsContent) == true) {
					return new ResponseEntity<String>(HttpStatus.OK);
				} else
					return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * Delete sensing environment with associated devices.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the response entity
	 */
	@RequestMapping(value = "/{sensing_environment_id}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteSensingEnvironmentWithAssociatedDevices(
			@PathVariable("sensing_environment_id") String sensing_environment_id) {

		try {
			if (sensing_environment_id != null) {
				LOG.debug("deleteSensingEnvironment:" + sensing_environment_id);

				/* Deleting devices associated with sensing environment */
				devicesService
						.deleteDevicesForSensingEnvironment(sensing_environment_id);

				if (sensingEnvironmentsService
						.deleteSensingEnvironment(sensing_environment_id) == true) {
					return new ResponseEntity<String>(HttpStatus.OK);
				} else
					return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED);

			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Gets the user system preferences.
	 *
	 * @param sensingEnvironmentId
	 *            the sensing environment id
	 * @return the user system preferences
	 */
	@RequestMapping(value = "/{sensing_environment_id}/systempreferences", method = RequestMethod.GET)
	public ResponseEntity<SystemPreferences> getUserSystemPreferences(
			@PathVariable("sensing_environment_id") String sensingEnvironmentId) {

		LOG.debug("Getting system preferences for user in sensing environment id: "
				+ sensingEnvironmentId);

		SensingEnvironment environment = sensingEnvironmentDao
				.getSensingEnvironmentByUUID(sensingEnvironmentId);

		if (environment == null) {
			LOG.debug("Sensing enviroment with id " + sensingEnvironmentId
					+ " not found.");
			return new ResponseEntity<SystemPreferences>(HttpStatus.NOT_FOUND);
		}

		User user = userDao
				.getEWallUserByUsername(environment.getPrimaryUser());

		if (user == null) {
			LOG.debug("User with username " + environment.getPrimaryUser()
					+ " not found.");
			return new ResponseEntity<SystemPreferences>(HttpStatus.NOT_FOUND);
		}

		try {
			SystemPreferences preferences = (SystemPreferences) user
					.getUserProfile().getUserPreferencesSubProfile()
					.getSystemPreferences();

			if (preferences != null) {
				return new ResponseEntity<SystemPreferences>(preferences,
						HttpStatus.OK);
			}

			return new ResponseEntity<SystemPreferences>(HttpStatus.NOT_FOUND);

		} catch (Exception e) {
			LOG.warn(e.toString());
			return new ResponseEntity<SystemPreferences>(HttpStatus.NOT_FOUND);
		}

	}

	/**
	 * The Class Version. Based on solution from:
	 * http://stackoverflow.com/questions
	 * /198431/how-do-you-compare-two-version-strings-in-java/11024200#11024200
	 */
	private class Version implements Comparable<Version> {

		/** The version. */
		private String version;

		/**
		 * Gets the.
		 *
		 * @return the string
		 */
		public final String get() {
			return this.version;
		}

		/**
		 * Instantiates a new version.
		 *
		 * @param version
		 *            the version
		 */
		public Version(String version) {
			if (version == null)
				throw new IllegalArgumentException("Version is null");
			if (!version.matches("[0-9]+(\\.[0-9]+)*"))
				throw new IllegalArgumentException(
						"Version format is not valid");
			this.version = version;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(Version that) {
			if (that == null)
				return 1;
			String[] thisParts = this.get().split("\\.");
			String[] thatParts = that.get().split("\\.");
			int length = Math.max(thisParts.length, thatParts.length);
			for (int i = 0; i < length; i++) {
				int thisPart = i < thisParts.length ? Integer
						.parseInt(thisParts[i]) : 0;
				int thatPart = i < thatParts.length ? Integer
						.parseInt(thatParts[i]) : 0;
				if (thisPart < thatPart)
					return -1;
				if (thisPart > thatPart)
					return 1;
			}
			return 0;
		}

		@Override
		public boolean equals(Object that) {
			if (that == null) {
				return false;
			}
			
			 if (this.getClass() != that.getClass()) {
				    return false;
			 }
			
			if (this.compareTo((Version)that) == 0) {
				return true;
			}
			return false;

		}

		@Override
		public int hashCode() {
			return super.hashCode();
		}

	}

}
