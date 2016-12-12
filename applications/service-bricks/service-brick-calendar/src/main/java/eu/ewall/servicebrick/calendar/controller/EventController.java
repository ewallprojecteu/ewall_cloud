package eu.ewall.servicebrick.calendar.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.ReadableDuration;
import org.joda.time.ReadablePeriod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.calendar.Event;
import eu.ewall.platform.commons.datamodel.calendar.ExerciseEvent;
import eu.ewall.platform.commons.datamodel.calendar.MedicalEvent;
import eu.ewall.platform.commons.datamodel.calendar.RepeatInterval;
import eu.ewall.platform.commons.datamodel.calendar.SelfcareEvent;
import eu.ewall.platform.commons.datamodel.calendar.SocialEvent;
import eu.ewall.servicebrick.calendar.dao.EventDao;
import eu.ewall.servicebrick.calendar.validation.EventInputValidator;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

/**
 * The Class CalendarController.
 */
@RestController
public class EventController {

	/** The Constant log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(EventController.class);

	/** The event dao. */
	private EventDao eventDao;

	/** The event input validator. */
	private EventInputValidator eventInputValidator;

	/** The user time zone provider. */
	private UserTimeZoneProvider userTimeZoneProvider;

	/**
	 * Instantiates a new event controller.
	 *
	 * @param userTimeZoneProvider
	 *            the user time zone provider
	 * @param eventDao
	 *            the event dao
	 */
	@Autowired
	public EventController(UserTimeZoneProvider userTimeZoneProvider,
			EventDao eventDao) {
		this.eventDao = eventDao;
		this.userTimeZoneProvider = userTimeZoneProvider;
		this.eventInputValidator = new EventInputValidator();
	}

	/**
	 * Gets the user time zone.
	 *
	 * @param username
	 *            the username
	 * @return the user time zone
	 */
	private DateTimeZone getUserTimeZone(String username) {
		DateTimeZone zone = userTimeZoneProvider.getUserTimeZone(username);
		if (zone == null) {
			throw new RuntimeException("Undefined time zone for user "
					+ username);
		}
		return zone;
	}

	/**
	 * Gets the user medical events by date.
	 *
	 * @param username            the username
	 * @param date            the date
	 * @param groupID the group id
	 * @return the user medical events by date
	 */
	@RequestMapping(value = "/medicalCheckup", method = RequestMethod.GET)
	public List<MedicalEvent> getUserMedicalEventsByDate(
			@RequestParam String username,
			@RequestParam(value = "date", required = false) Long date,
			@RequestParam(required = false) String groupID) {

		DateTimeZone userZone = getUserTimeZone(username);

		// if date is not defined, it will return all events
		if (date == null)
			return eventDao.readAllUserMedicalEvents(username, groupID);

		DateTime dateTime = new DateTime(date);

		DateTime from = dateTime.withZoneRetainFields(userZone)
				.withTimeAtStartOfDay();
		DateTime to = dateTime.withZoneRetainFields(userZone).plusDays(1)
				.withTimeAtStartOfDay();
		return eventDao.readUserMedicalEventsByDate(username, from.getMillis(),
				to.getMillis(), groupID);
	}

	/**
	 * Adds the medical event.
	 *
	 * @param event the event
	 * @return the response entity
	 */
	@RequestMapping(value = "/medicalCheckup", method = RequestMethod.POST)
	public ResponseEntity<String> addMedicalEvents(
			@RequestBody MedicalEvent event) {
		eventInputValidator.validateEvent(event);

		int repeatTimes = event.getRepeatTimes().intValue();
		DateTime start = new DateTime(event.getStartDate());
		DateTime end = new DateTime(event.getEndDate());

		event.setGroupID(UUID.randomUUID().toString());

		for (int i = 0; i < repeatTimes; i++) {

			Period period = getPeriod(event.getRepeatInterval());

			event.setStartDate(start.plus(period.multipliedBy(i)).getMillis());
			event.setEndDate(end.plus(period.multipliedBy(i)).getMillis());

			event.setRepeatTimes(Integer.valueOf(repeatTimes - i));

			event.setId(UUID.randomUUID().toString());
			eventDao.insertMedicalEvent(event);
		}

		return new ResponseEntity<String>(HttpStatus.OK);
	}

	/**
	 * Update medical event.
	 *
	 * @param id
	 *            the id
	 * @param medicalEvent
	 *            the medical event
	 * @return the response entity
	 */
	@RequestMapping(value = "/medicalCheckup/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateMedicalEvent(@PathVariable String id,
			@RequestBody MedicalEvent medicalEvent) {

		eventInputValidator.validateEventId(medicalEvent, id);
		eventInputValidator.validateEvent(medicalEvent);

		eventDao.updateMedicalEvent(medicalEvent);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Delete medical event.
	 *
	 * @param id            the id
	 * @param groupID the group id
	 * @return the response entity
	 */
	@RequestMapping(value = "/medicalCheckup", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteMedicalEvent(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String groupID) {

		// TODO do a validation
		if (groupID != null)
			eventDao.deleteMedicalEvents(groupID);
		if (id != null)
			eventDao.deleteMedicalEvent(id);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Gets the user exercise events by date.
	 *
	 * @param username            the username
	 * @param date            the date
	 * @param groupID the group id
	 * @return the user exercise events by date
	 */
	@RequestMapping(value = "/exercises", method = RequestMethod.GET)
	public List<ExerciseEvent> getUserExerciseEventsByDate(
			@RequestParam String username,
			@RequestParam(value = "date", required = false) Long date,
			@RequestParam(required = false) String groupID) {

		DateTimeZone userZone = getUserTimeZone(username);

		// if date is not defined, it will return all events
		if (date == null)
			return eventDao.readAllUserExerciseEvents(username, groupID);

		DateTime dateTime = new DateTime(date);

		DateTime from = dateTime.withZoneRetainFields(userZone)
				.withTimeAtStartOfDay();
		DateTime to = dateTime.withZoneRetainFields(userZone).plusDays(1)
				.withTimeAtStartOfDay();
		return eventDao.readUserExerciseEventsByDate(username,
				from.getMillis(), to.getMillis(), groupID);
	}

	/**
	 * Adds the exercise event.
	 *
	 * @param event the event
	 * @return the response entity
	 */
	@RequestMapping(value = "/exercises", method = RequestMethod.POST)
	public ResponseEntity<String> addExerciseEvent(
			@RequestBody ExerciseEvent event) {

		eventInputValidator.validateEvent(event);

		int repeatTimes = event.getRepeatTimes().intValue();
		DateTime start = new DateTime(event.getStartDate());
		DateTime end = new DateTime(event.getEndDate());

		event.setGroupID(UUID.randomUUID().toString());

		for (int i = 0; i < repeatTimes; i++) {

			Period period = getPeriod(event.getRepeatInterval());

			event.setStartDate(start.plus(period.multipliedBy(i)).getMillis());
			event.setEndDate(end.plus(period.multipliedBy(i)).getMillis());

			event.setRepeatTimes(Integer.valueOf(repeatTimes - i));

			event.setId(UUID.randomUUID().toString());
			eventDao.insertExerciseEvent(event);
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Update exercise event.
	 *
	 * @param id
	 *            the id
	 * @param exerciseEvent
	 *            the exercise event
	 * @return the response entity
	 */
	@RequestMapping(value = "/exercises/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateExerciseEvent(@PathVariable String id,
			@RequestBody ExerciseEvent exerciseEvent) {

		eventInputValidator.validateEventId(exerciseEvent, id);
		eventInputValidator.validateEvent(exerciseEvent);

		eventDao.updateExerciseEvent(exerciseEvent);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Delete exercise event.
	 *
	 * @param id            the id
	 * @param groupID the group id
	 * @return the response entity
	 */
	@RequestMapping(value = "/exercises", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteExerciseEvent(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String groupID) {

		// TODO do a validation
		if (groupID != null)
			eventDao.deleteExerciseEvents(groupID);
		if (id != null)
			eventDao.deleteExerciseEvent(id);

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Gets the user selfcare events by date.
	 *
	 * @param username            the username
	 * @param date            the date
	 * @param groupID the group id
	 * @return the user selfcare events by date
	 */
	@RequestMapping(value = "/selfcare", method = RequestMethod.GET)
	public List<SelfcareEvent> getUserSelfcareEventsByDate(
			@RequestParam String username,
			@RequestParam(value = "date", required = false) Long date,
			@RequestParam(required = false) String groupID) {

		DateTimeZone userZone = getUserTimeZone(username);

		// if date is not defined, it will return all events
		if (date == null)
			return eventDao.readAllUserSelfcareEvents(username, groupID);

		DateTime dateTime = new DateTime(date);

		DateTime from = dateTime.withZoneRetainFields(userZone)
				.withTimeAtStartOfDay();
		DateTime to = dateTime.withZoneRetainFields(userZone).plusDays(1)
				.withTimeAtStartOfDay();

		return eventDao.readUserSelfcareEventsByDate(username,
				from.getMillis(), to.getMillis(), groupID);
	}

	/**
	 * Adds the selfcare event.
	 *
	 * @param event the event
	 * @return the response entity
	 */
	@RequestMapping(value = "/selfcare", method = RequestMethod.POST)
	public ResponseEntity<String> addSelfcareEvent(
			@RequestBody SelfcareEvent event) {

		eventInputValidator.validateEvent(event);

		int repeatTimes = event.getRepeatTimes().intValue();
		DateTime start = new DateTime(event.getStartDate());
		DateTime end = new DateTime(event.getEndDate());

		event.setGroupID(UUID.randomUUID().toString());

		for (int i = 0; i < repeatTimes; i++) {

			Period period = getPeriod(event.getRepeatInterval());

			event.setStartDate(start.plus(period.multipliedBy(i)).getMillis());
			event.setEndDate(end.plus(period.multipliedBy(i)).getMillis());

			event.setRepeatTimes(Integer.valueOf(repeatTimes - i));

			event.setId(UUID.randomUUID().toString());
			eventDao.insertSelfcareEvent(event);
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Update selfcare event.
	 *
	 * @param id
	 *            the id
	 * @param selfcareEvent
	 *            the selfcare event
	 * @return the response entity
	 */
	@RequestMapping(value = "/selfcare/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateSelfcareEvent(@PathVariable String id,
			@RequestBody SelfcareEvent selfcareEvent) {

		eventInputValidator.validateEventId(selfcareEvent, id);
		eventInputValidator.validateEvent(selfcareEvent);

		eventDao.updateSelfcareEvent(selfcareEvent);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Delete selfcare event.
	 *
	 * @param id            the id
	 * @param groupID the group id
	 * @return the response entity
	 */
	@RequestMapping(value = "/selfcare", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteSelfcareEvent(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String groupID) {
		// TODO do a validation
		if (groupID != null)
			eventDao.deleteSelfcareEvents(groupID);
		if (id != null)
			eventDao.deleteSelfcareEvent(id);

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Gets the user social events by date.
	 *
	 * @param username            the username
	 * @param date            the date
	 * @param groupID the group id
	 * @return the user social events by date
	 */
	@RequestMapping(value = "/socializing", method = RequestMethod.GET)
	public List<SocialEvent> getUserSocialEventsByDate(
			@RequestParam String username,
			@RequestParam(value = "date", required = false) Long date,
			@RequestParam(required = false) String groupID) {
		DateTimeZone userZone = getUserTimeZone(username);

		// if date is not defined, it will return all events
		if (date == null)
			return eventDao.readAllUserSocialEvents(username, groupID);

		DateTime dateTime = new DateTime(date);

		DateTime from = dateTime.withZoneRetainFields(userZone)
				.withTimeAtStartOfDay();
		DateTime to = dateTime.withZoneRetainFields(userZone).plusDays(1)
				.withTimeAtStartOfDay();

		return eventDao.readUserSocialEventsByDate(username, from.getMillis(),
				to.getMillis(), groupID);
	}

	/**
	 * Adds the social event.
	 *
	 * @param event the event
	 * @return the response entity
	 */
	@RequestMapping(value = "/socializing", method = RequestMethod.POST)
	public ResponseEntity<String> addSocialEvent(@RequestBody SocialEvent event) {

		eventInputValidator.validateEvent(event);

		int repeatTimes = event.getRepeatTimes().intValue();
		DateTime start = new DateTime(event.getStartDate());
		DateTime end = new DateTime(event.getEndDate());

		event.setGroupID(UUID.randomUUID().toString());

		for (int i = 0; i < repeatTimes; i++) {

			Period period = getPeriod(event.getRepeatInterval());

			event.setStartDate(start.plus(period.multipliedBy(i)).getMillis());
			event.setEndDate(end.plus(period.multipliedBy(i)).getMillis());

			event.setRepeatTimes(Integer.valueOf(repeatTimes - i));

			event.setId(UUID.randomUUID().toString());
			eventDao.insertSocialEvent(event);
		}

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Update social event.
	 *
	 * @param id
	 *            the id
	 * @param socialEvent
	 *            the social event
	 * @return the response entity
	 */
	@RequestMapping(value = "/socializing/{id}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateSocialEvent(@PathVariable String id,
			@RequestBody SocialEvent socialEvent) {

		eventInputValidator.validateEventId(socialEvent, id);
		eventInputValidator.validateEvent(socialEvent);

		eventDao.updateSocialEvent(socialEvent);
		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Delete social event.
	 *
	 * @param id            the id
	 * @param groupID the group id
	 * @return the response entity
	 */
	@RequestMapping(value = "/socializing", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteSocialEvent(
			@RequestParam(required = false) String id,
			@RequestParam(required = false) String groupID) {
		// TODO do a validation
		if (groupID != null)
			eventDao.deleteSocialEvents(groupID);
		if (id != null)
			eventDao.deleteSocialEvent(id);

		return new ResponseEntity<String>(HttpStatus.OK);

	}

	/**
	 * Gets the period.
	 *
	 * @param repeatInterval the repeat interval
	 * @return the period
	 */
	private Period getPeriod(RepeatInterval repeatInterval) {
		Period period = new Period();

		switch (repeatInterval) {
		case HOURLY:
			period = period.withHours(1);
			break;
		case DAILY:
			period = period.withDays(1);
			break;
		case WEEKLY:
			period = period.withWeeks(1);
			break;
		case MONTHLY:
			period = period.withMonths(1);
			break;
		case YEARLY:
			period = period.withYears(1);
			break;
		default:
			period = null;
			break;
		}

		return period;
	}

}