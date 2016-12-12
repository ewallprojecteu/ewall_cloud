/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.controllers;

import java.util.ArrayList;
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

import eu.ewall.platform.commons.datamodel.service.Service;
import eu.ewall.platform.profilingserver.services.ServiceServiceImpl;

/**
 * The Class ServiceController.
 *
 * @author eandgrg, emirmos
 */
@RestController
@RequestMapping(value = "/services")
public class ServiceController {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(ServiceController.class);

	/** The env. */
	@Autowired
	private Environment env;

	/** The service service. */
	@Autowired
	private ServiceServiceImpl serviceService;

	/**
	 * Instantiates a new service controller.
	 */
	public ServiceController() {
	}

	/**
	 * Adds the new service.
	 *
	 * @param serviceAsContent
	 *            the service as content
	 * @return the response entity
	 */
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<String> addNewService(
			@RequestBody Service serviceAsContent) {

		LOG.debug("addingNewService");

		try {
			if (serviceAsContent != null) {

				// if the service already exist report
				// conflict (409)
				if (serviceService.addEWallService(serviceAsContent) == false) {
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
	 * Gets the e wall services.
	 *
	 * @return the e wall services
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Service>> getAllServices() {

		LOG.info("Getting all ewall services ");

		return new ResponseEntity<List<Service>>(
				serviceService.getAllEWallServices(), HttpStatus.OK);

	}

	/**
	 * Gets the e wall service by service name.
	 *
	 * @param serviceName
	 *            the service name
	 * @return the e wall service by serviceName
	 */
	@RequestMapping(value = "/{serviceName}", method = RequestMethod.GET)
	public ResponseEntity<Service> getServiceByServicename(
			@PathVariable("serviceName") String serviceName) {

		LOG.debug("Getting service with servicename: " + serviceName);

		try {
			Service service = serviceService
					.getEWallServiceByServiceName(serviceName);

			if (service != null) {
				return new ResponseEntity<Service>(service, HttpStatus.OK);
			} else {
				return new ResponseEntity<Service>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<Service>(HttpStatus.BAD_REQUEST);
		}

	}

	/**
	 * Modify service by service name.
	 *
	 * @param serviceAsContent
	 *            the service as content
	 * @param serviceName
	 *            the service name
	 * @return the response entity
	 */
	@RequestMapping(value = "/{serviceName}", method = RequestMethod.POST)
	public ResponseEntity<String> modifyServiceByServiceName(
			@RequestBody Service serviceAsContent,
			@PathVariable("serviceName") String serviceName) {

		LOG.debug("Modify service with service name: " + serviceName);

		try {

			if (serviceAsContent != null && serviceName != null) {
				serviceService.modifyEWallServiceWithName(serviceName,
						serviceAsContent);
			} else
				return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

		} catch (Exception e) {
			LOG.warn(e.getMessage());
			return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Delete service by service name.
	 *
	 * @param serviceName
	 *            the service name
	 * @return the response entity
	 */
	@RequestMapping(value = "/{serviceName}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteServiceByServiceName(
			@PathVariable("serviceName") String serviceName) {

		LOG.debug("Delete service with servicename: " + serviceName);

		try {
			if (serviceService.deleteServiceWithName(serviceName))

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
			String[] servicesPlatformNames = env.getProperty(
					"services.platform.names").split(",");
			String[] servicesApplicationsNames = env.getProperty(
					"services.applications.names").split(",");
			String platformUrl = env.getProperty("platform.url");
			String applicationsUrl = env.getProperty("applications.url");

			List<Service> services = new ArrayList<Service>();
			if (servicesPlatformNames != null) {
				for (String serviceNameProp : servicesPlatformNames) {
					String serviceName = serviceNameProp.replace('_', ' ');
					Service service = new Service(serviceName);

					String path = env.getProperty(
							serviceNameProp + ".href.path").trim();

					Link link = getLinkWithoutTemplateAttribute(platformUrl,
							path);

					service.add(link);
					services.add(service);
				}
			}
			if (servicesApplicationsNames != null) {
				for (String serviceNameProp : servicesApplicationsNames) {
					String serviceName = serviceNameProp.replace('_', ' ');
					Service service = new Service(serviceName);

					String path = env.getProperty(
							serviceNameProp + ".href.path").trim();

					Link link = getLinkWithoutTemplateAttribute(
							applicationsUrl, path);

					service.add(link);
					services.add(service);
				}
			}

			serviceService.initDeafultData(services);

		} catch (Exception e) {
			LOG.error("Error in initDefaultData for services."
					+ e);
		}

	}

	/**
	 * Gets the link without template attribute.<br>
	 * Manually creating Link object to avoid adding 'template' attribute.
	 *
	 * @param host
	 *            the host
	 * @param path
	 *            the path
	 * @return the link without template attribute
	 * @throws Exception
	 *             the exception
	 */
	private Link getLinkWithoutTemplateAttribute(String host, String path)
			throws Exception {
		ObjectMapper objMapper = new ObjectMapper();
		ObjectNode objNode = objMapper.createObjectNode();
		objNode.put("href", host + path);
		Link link = objMapper.readValue(objNode.toString(), Link.class);
		return link;
	}
}
