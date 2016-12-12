package eu.ewall.servicebrick.calendar.controller;

import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.calendar.CalendarTask;
import eu.ewall.servicebrick.calendar.dao.CalendarDao;
import eu.ewall.servicebrick.common.dao.ProfilingServerDao;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;

/**
 * The Class CalendarController.
 */
@RestController
public class CalendarController {

	/** The Constant log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(CalendarController.class);

	/** The calendar dao. */
	private CalendarDao calendarDao;
	
	/** The user time zone provider. */
	private UserTimeZoneProvider userTimeZoneProvider;

	/**
	 * Instantiates a new calendar controller.
	 *
	 * @param profilingServerDao
	 *            the profiling server dao
	 * @param caregiverWebappDao
	 *            the caregiver webapp dao
	 */
	@Autowired
	public CalendarController(UserTimeZoneProvider userTimeZoneProvider,
			CalendarDao calendarDao) {
		this.calendarDao = calendarDao;
		this.userTimeZoneProvider = userTimeZoneProvider;
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
	 * Gets the all users tasks.
	 *
	 * @param username
	 *            the username
	 * @return the all users tasks
	 */
	@RequestMapping(value = "/task", method = RequestMethod.GET)
	public List<CalendarTask> getUsersTasksByDate(
			@RequestParam String username,
			@RequestParam(value = "date", required = false) Long date) {
		
		DateTimeZone userZone = getUserTimeZone(username);

		// if date is not defined, it will return all events
		if (date == null)
			return calendarDao.readTasks(username);

		DateTime dateTime = new DateTime(date);

		DateTime from = dateTime.withZoneRetainFields(userZone)
				.withTimeAtStartOfDay();
		DateTime to = dateTime.withZoneRetainFields(userZone).plusDays(1)
				.withTimeAtStartOfDay();
		return calendarDao.readUserTasksByDate(username, from.toDate(),
				to.toDate());
		

	}

	/**
	 * Delete tasks.
	 *
	 * @param username
	 *            the username
	 * @param id
	 *            the id
	 * @return the response entity
	 */
	@RequestMapping(value = "/task", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteTasks(
			@RequestParam(value = "id", required = true) String id) {
		LOG.debug("Delete task with id: "
				+ id);

		if (calendarDao.deleteTask(id))
			return new ResponseEntity<String>(HttpStatus.OK);

		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

	}

	/**
	 * Adds the task.
	 *
	 * @param task
	 *            the task
	 * @return the response entity
	 */
	@RequestMapping(value = "/task", method = RequestMethod.POST)
	public ResponseEntity<String> addTask(@RequestBody CalendarTask task) {
		LOG.debug("adding task: " + task.toString());

		if (task.getId() == null || task.getId().isEmpty()) {
			task.setId(UUID.randomUUID().toString());
		}
		if (calendarDao.insertTask(task))
			return new ResponseEntity<String>(HttpStatus.OK);

		return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

	}

}