/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.dao;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.service.model.common.ActivityGoalResponse;

/**
 * The Class ActivityGoalDAO.
 */
@Component
public class ActivityGoalDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(ActivityGoalDAO.class);

	@Value("${idss-automatic-goal-setting.url}")
	private String idssAutomaticGoalSetting;

	/** The ewall client. */
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	/** The Constant dateTimeFormatter. */
	private static final DateTimeFormatter dateTimeFormatter = ISODateTimeFormat.dateTimeParser();

	/**
	 * Gets the steps goal for current day.
	 *
	 * @param username the username
	 * @param date the date
	 * @return the steps goal for current day
	 */
	public ActivityGoalResponse getStepsGoalForCurrentDay(String username, LocalDate date) {
		String url = idssAutomaticGoalSetting + "/activitygoal?userid={username}&date="+dateTimeFormatter.print(date);
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, ActivityGoalResponse.class, username, date);
		} catch (Exception e) {
			log.error("Error in getting steps goal from automatic goal setting idss reasoner", e);
			return null;
		}
	
	}
	
}
