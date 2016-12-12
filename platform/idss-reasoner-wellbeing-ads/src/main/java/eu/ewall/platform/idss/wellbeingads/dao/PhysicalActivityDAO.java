/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.dao;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.wellbeingads.model.physicalactivity.MovementHistory;

/**
 * The Class PhysicalActivityDAO.
 */
@Component
public class PhysicalActivityDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(PhysicalActivityDAO.class);

	/** The service brick physical activity. */
	@Value("${service-brick-physicalactivity.url}")
	private String serviceBrickPhysicalActivity;

	/** The ewall client. */
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	/** The Constant dateTimeFormatter. */
	private static final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();

	/**
	 * Gets the steps for current day.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return the steps for current day
	 */
	public MovementHistory getStepsForCurrentDay(String username, DateTime dateTimeFrom, DateTime dateTimeTo) {

		String url = serviceBrickPhysicalActivity + "/v1/" + username + "/movement?from="
				+ dateTimeFormatter.print(dateTimeFrom) + "&to=" + dateTimeFormatter.print(dateTimeTo)
				+ "&aggregation=1d";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, MovementHistory.class, username, dateTimeFrom, dateTimeTo);
		} catch (Exception e) {
			log.error("Error in getting steps from physical activity service brick", e);
			return null;
		}

	}

}
