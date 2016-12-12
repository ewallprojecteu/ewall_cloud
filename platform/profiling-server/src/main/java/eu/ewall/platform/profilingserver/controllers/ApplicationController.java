/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import eu.ewall.platform.commons.datamodel.service.Application;
import eu.ewall.platform.commons.datamodel.service.ApplicationType;
import eu.ewall.platform.profilingserver.services.ApplicationServiceImpl;

/**
 * The Class ApplicationController.
 *
 * @author eandgrg, emirmos
 */
@RestController
@RequestMapping(value = "/applications")
public class ApplicationController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(ApplicationController.class);

	/** The env. */
	@Autowired
	private Environment env;

	/** The application. */
	@Autowired
	private ApplicationServiceImpl applicationService;

	/**
	 * Instantiates a new application controller.
	 */
	public ApplicationController() {
	}

	/**
	 * Adds the new application.
	 *
	 * @param applicationAsContent
	 *            the application as content
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<String> addNewApplication(
			@RequestBody Application applicationAsContent) {

		LOG.debug("addingNewApplication");

		try {
			if (applicationAsContent != null) {

				// if the application already exists report conflict (409)
				if (applicationService
						.addEWallApplication(applicationAsContent) == false) {
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
	 * Gets the e wall applications.
	 *
	 * @return the e wall applications
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Application>> getAllApplications() {

		LOG.debug("Getting all ewall applications ");

		return new ResponseEntity<List<Application>>(
				applicationService.getAllEWallApplications(), HttpStatus.OK);

	}

	/**
	 * Gets the e wall application by application name.
	 *
	 * @param applicationName
	 *            the application name
	 * @return the e wall application by applicationName
	 */
	@RequestMapping(value = "/{applicationName}", method = RequestMethod.GET)
	public ResponseEntity<Application> getApplicationByApplicationname(
			@PathVariable("applicationName") String applicationName) {

		LOG.debug("Getting application with application name: "
				+ applicationName);

		try {
			Application application = applicationService
					.getEWallApplicationByApplicationName(applicationName);

			if (application != null) {
				return new ResponseEntity<Application>(application,
						HttpStatus.OK);
			} else {
				return new ResponseEntity<Application>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Application>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Modify application by application name.
	 *
	 * @param applicationAsContent
	 *            the application as content
	 * @param applicationName
	 *            the application name
	 * @return the response entity
	 */
	@RequestMapping(value = "/{applicationName}", method = RequestMethod.POST)
	public ResponseEntity<String> modifyApplicationByApplicationName(
			@RequestBody Application applicationAsContent,
			@PathVariable("applicationName") String applicationName) {

		LOG.debug("Modify application with application name: "
				+ applicationName);

		try {

			if (applicationAsContent != null && applicationName != null) {
				if (applicationService.modifyEWallApplicationWithName(
						applicationName, applicationAsContent) == true) {
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
	 * Delete application by application name.
	 *
	 * @param applicationName
	 *            the application name
	 * @return the response entity
	 */
	@RequestMapping(value = "/{applicationName}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteApplicationByApplicationName(
			@PathVariable("applicationName") String applicationName) {

		LOG.debug("Delete application with applicationname: " + applicationName);

		try {
			if (applicationService.deleteApplicationWithName(applicationName))

				return new ResponseEntity<String>(HttpStatus.OK);
			else {
				return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Inits the deafult data.
	 */
	@PostConstruct
	public void initDeafultData() {

		try {
			String[] applicationsNames = env.getProperty("applications.names")
					.split(",");
			String applicationsUrl = env.getProperty("applications.url");

			List<Application> applications = new ArrayList<Application>();
			if (applicationsNames != null) {
				for (String applicationNameProp : applicationsNames) {
					String appName = applicationNameProp.replace('_', ' ');
					Application app = new Application(appName);

					ApplicationType appType = ApplicationType.valueOf(env
							.getProperty(applicationNameProp + ".type").trim()
							.toUpperCase());
					String[] services = env.getProperty(
							applicationNameProp + ".services").split(",");

					app.setType(appType);
					if (services.length > 0 && services[0] != null
							&& !services[0].trim().isEmpty()) {
						app.setEncompassingServiceNames(Arrays.asList(services));
					}

					String href = env
							.getProperty(applicationNameProp + ".href").trim();
					if (href.startsWith("[applications.url]")) {
						href = href.replace("[applications.url]",
								applicationsUrl);
					}

					Link link = getLinkWithoutTemplateAttribute(href);

					app.add(link);
					applications.add(app);
				}

				applicationService.initDeafultData(applications);
			}

		} catch (Exception e) {
			LOG.error("Error in initDefaultData for applications."
					+ e);
		}
	}

	/**
	 * Gets the link without template attribute.<br>
	 * Manually creating Link object to avoid adding 'template' attribute.
	 *
	 * @param href
	 *            the href
	 * @return the link without template attribute
	 * @throws Exception
	 *             the exception
	 */
	private Link getLinkWithoutTemplateAttribute(String href) throws Exception {
		ObjectMapper objMapper = new ObjectMapper();
		ObjectNode objNode = objMapper.createObjectNode();
		objNode.put("href", href);
		Link link = objMapper.readValue(objNode.toString(), Link.class);
		return link;
	}
}
