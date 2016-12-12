package eu.ewall.servicebrick.calendar.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.data.mongodb.core.query.Criteria;

import eu.ewall.platform.commons.datamodel.calendar.CalendarTask;
import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;


/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class CalendarDao extends TimeZoneDaoSupport {

	/** The time. */
	long time = System.currentTimeMillis();

	/** The Constant log. */
	private static final Logger log = LoggerFactory
			.getLogger(CalendarDao.class);

	/**
	 * Instantiates a new calendar dao.
	 *
	 * @param mongoOps
	 *            the mongo ops
	 * @param timeZoneCtx
	 *            the time zone ctx
	 * @param userTimeZoneProvider
	 *            the user time zone provider
	 */
	@Autowired
	public CalendarDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx,
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}

	/**
	 * Read tasks.
	 *
	 * @param username
	 *            the username
	 * @return the list
	 */
	public List<CalendarTask> readTasks(String username) {
		Query query = query(where("username").is(username));
		List<CalendarTask> tasks = mongoOps.find(query,
				CalendarTask.class);
		return tasks;
	}

	/**
	 * Delete task.
	 *
	 * @param username
	 *            the username
	 * @param id
	 *            the id
	 * @return true, if successful
	 */
	public boolean deleteTask(String id) {
		if (id.isEmpty())
			return false;
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, CalendarTask.class);
		return true;

	}

	/**
	 * Insert task.
	 *
	 * @param task
	 *            the task
	 * @return true, if successful
	 */
	public boolean insertTask(CalendarTask task) {
		if (task == null)
			return false;

		Update taskUpdate = new Update();
		taskUpdate.set("id", task.getId());
		taskUpdate.set("username", task.getUsername());
		taskUpdate.set("title", task.getTitle());
		taskUpdate.set("allDay", task.getAllDay());
		taskUpdate.set("startDate", task.getStartDate());
		taskUpdate.set("endDate", task.getEndDate());

		mongoOps.insert(task);

		return true;
	}

	/**
	 * Read user tasks by date.
	 *
	 * @param username the username
	 * @param from the from
	 * @param to the to
	 * @return the list
	 */
	public List<CalendarTask> readUserTasksByDate(String username, Date from,
			Date to) {
		Criteria criteria = where("username").is(username);
		criteria.andOperator(Criteria.where("startDate").gte(from), Criteria
				.where("startDate").lt(to));

		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<CalendarTask> events = mongoOps.find(query, CalendarTask.class);
		return events;
	}

}