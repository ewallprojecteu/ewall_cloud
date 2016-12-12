/*******************************************************************************
 * Copyright 2016 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.dao;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.service.model.common.MoodDataResponse;

/**
 * The Class MoodDAO.
 */
@Component
public class MoodDAO {

	/** The log. */
	Logger log = LoggerFactory.getLogger(MoodDAO.class);

	/** The ld mood. */
	@Value("${lr-mood.url}")
	private String ldMood;

	/** The ewall client. */
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	/**
	 * Gets the last mood.
	 *
	 * @param userid the userid
	 * @param from the from
	 * @param to the to
	 * @return the last mood
	 */
	public MoodDataResponse getLastMood(String userid, LocalDate from, LocalDate to) {
		String url = ldMood + "/mood4period?userid={userid}&from={from}&to={to}";
		log.debug("Invoking endpoint: " + url);
		try {
			return ewallClient.getForObject(url, MoodDataResponse.class, userid, from, to);
		} catch (Exception e) {
			log.error("Error in getting mood from Lifestyle Reasoner Mood", e);
			return null;
		}
	
	}
	
}
