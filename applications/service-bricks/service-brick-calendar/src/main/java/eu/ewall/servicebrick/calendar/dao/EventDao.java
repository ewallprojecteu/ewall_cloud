package eu.ewall.servicebrick.calendar.dao;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import eu.ewall.platform.commons.datamodel.calendar.ExerciseEvent;
import eu.ewall.platform.commons.datamodel.calendar.MedicalEvent;
import eu.ewall.platform.commons.datamodel.calendar.SelfcareEvent;
import eu.ewall.platform.commons.datamodel.calendar.SocialEvent;
import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * DAO that reads and writes inactivity data in Mongo DB.
 */
@Component
public class EventDao extends TimeZoneDaoSupport {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(EventDao.class);

	/**
	 * Instantiates a new event dao.
	 *
	 * @param mongoOps
	 *            the mongo ops
	 * @param timeZoneCtx
	 *            the time zone ctx
	 * @param userTimeZoneProvider
	 *            the user time zone provider
	 */
	@Autowired
	public EventDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx,
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}

	/**
	 * Insert medical event.
	 *
	 * @param medicalEvent
	 *            the medical event
	 */
	public void insertMedicalEvent(MedicalEvent medicalEvent) {

		mongoOps.insert(medicalEvent);
	}

	/**
	 * Read user medical events by date.
	 *
	 * @param username            the username
	 * @param from            the from
	 * @param to            the to
	 * @param groupId the group id
	 * @return the list
	 */
	public List<MedicalEvent> readUserMedicalEventsByDate(String username,
			long from, long to, String groupId) {
		Criteria criteria = where("username").is(username);
		criteria.andOperator(Criteria.where("startDate").gte(from), Criteria
				.where("startDate").lt(to));
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<MedicalEvent> events = mongoOps.find(query, MedicalEvent.class);
		return events;
	}

	/**
	 * Read all user medical events.
	 *
	 * @param username the username
	 * @param groupId the group id
	 * @return the list
	 */
	public List<MedicalEvent> readAllUserMedicalEvents(String username,
			String groupId) {
		Criteria criteria = where("username").is(username);
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<MedicalEvent> events = mongoOps.find(query, MedicalEvent.class);
		return events;
	}

	/**
	 * Insert exercise event.
	 *
	 * @param exerciseEvent
	 *            the exercise event
	 */
	public void insertExerciseEvent(ExerciseEvent exerciseEvent) {
		mongoOps.insert(exerciseEvent);
	}

	/**
	 * Read user exercise events by date.
	 *
	 * @param username            the username
	 * @param from            the from
	 * @param to            the to
	 * @param groupId the group id
	 * @return the list
	 */
	public List<ExerciseEvent> readUserExerciseEventsByDate(String username,
			long from, long to, String groupId) {
		Criteria criteria = where("username").is(username);
		criteria.andOperator(Criteria.where("startDate").gte(from), Criteria
				.where("startDate").lt(to));
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<ExerciseEvent> events = mongoOps.find(query, ExerciseEvent.class);
		return events;
	}

	/**
	 * Read all user exercise events.
	 *
	 * @param username the username
	 * @param groupId the group id
	 * @return the list
	 */
	public List<ExerciseEvent> readAllUserExerciseEvents(String username,
			String groupId) {
		Criteria criteria = where("username").is(username);
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<ExerciseEvent> events = mongoOps.find(query, ExerciseEvent.class);
		return events;
	}

	/**
	 * Insert social event.
	 *
	 * @param socialEvent
	 *            the social event
	 */
	public void insertSocialEvent(SocialEvent socialEvent) {
		mongoOps.insert(socialEvent);
	}

	/**
	 * Read user social events by date.
	 *
	 * @param username            the username
	 * @param from            the from
	 * @param to            the to
	 * @param groupId the group id
	 * @return the list
	 */
	public List<SocialEvent> readUserSocialEventsByDate(String username,
			long from, long to, String groupId) {
		Criteria criteria = where("username").is(username);
		criteria.andOperator(Criteria.where("startDate").gte(from), Criteria
				.where("startDate").lt(to));
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<SocialEvent> events = mongoOps.find(query, SocialEvent.class);
		return events;
	}

	/**
	 * Read all user social events.
	 *
	 * @param username the username
	 * @param groupId the group id
	 * @return the list
	 */
	public List<SocialEvent> readAllUserSocialEvents(String username,
			String groupId) {
		Criteria criteria = where("username").is(username);
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<SocialEvent> events = mongoOps.find(query, SocialEvent.class);
		return events;
	}

	/**
	 * Insert selfcare event.
	 *
	 * @param selfcareEvent
	 *            the selfcare event
	 */
	public void insertSelfcareEvent(SelfcareEvent selfcareEvent) {
		mongoOps.insert(selfcareEvent);
	}

	/**
	 * Read user selfcare events by date.
	 *
	 * @param username            the username
	 * @param from            the from
	 * @param to            the to
	 * @param groupId the group id
	 * @return the list
	 */
	public List<SelfcareEvent> readUserSelfcareEventsByDate(String username,
			long from, long to, String groupId) {
		Criteria criteria = where("username").is(username);
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		criteria.andOperator(Criteria.where("startDate").gte(from), Criteria
				.where("startDate").lt(to));
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<SelfcareEvent> events = mongoOps.find(query, SelfcareEvent.class);
		return events;
	}

	/**
	 * Read all user selfcare events.
	 *
	 * @param username the username
	 * @param groupId the group id
	 * @return the list
	 */
	public List<SelfcareEvent> readAllUserSelfcareEvents(String username,
			String groupId) {
		Criteria criteria = where("username").is(username);
		if (groupId != null) {
			criteria.andOperator(Criteria.where("groupID").is(groupId));
		}
		Query query = query(criteria).with(
				new Sort(Sort.Direction.ASC, "startDate"));
		List<SelfcareEvent> events = mongoOps.find(query, SelfcareEvent.class);
		return events;
	}

	/**
	 * Update medical event.
	 *
	 * @param medicalEvent
	 *            the medical event
	 */
	public void updateMedicalEvent(MedicalEvent medicalEvent) {
		Update eventUpdate = new Update();
		eventUpdate.set("startDate", medicalEvent.getStartDate());
		eventUpdate.set("endDate", medicalEvent.getEndDate());
		eventUpdate.set("title", medicalEvent.getTitle());
		eventUpdate.set("groupID", medicalEvent.getGroupID());
		eventUpdate.set("username", medicalEvent.getUsername());
		eventUpdate.set("repeatTimes", medicalEvent.getRepeatTimes());
		eventUpdate.set("repeatInterval", medicalEvent.getRepeatInterval());
		eventUpdate.set("reminderTime", medicalEvent.getReminderTime());
		eventUpdate.set("doctorName", medicalEvent.getDoctorName());
		eventUpdate.set("doctorPhone", medicalEvent.getDoctorPhone());
		eventUpdate.set("location", medicalEvent.getLocation());
		eventUpdate.set("medExam", medicalEvent.getMedExam());
		eventUpdate.set("insCard", medicalEvent.getInsCard());
		eventUpdate.set("otherDocs", medicalEvent.getOtherDocs());
		eventUpdate.set("transportation", medicalEvent.getTransportation());

		mongoOps.upsert(query(where("id").is(medicalEvent.getId())),
				eventUpdate, MedicalEvent.class);

	}

	/**
	 * Update exercise event.
	 *
	 * @param event
	 *            the event
	 */
	public void updateExerciseEvent(ExerciseEvent event) {
		Update eventUpdate = new Update();
		eventUpdate.set("startDate", event.getStartDate());
		eventUpdate.set("endDate", event.getEndDate());
		eventUpdate.set("title", event.getTitle());
		eventUpdate.set("groupID", event.getGroupID());
		eventUpdate.set("username", event.getUsername());
		eventUpdate.set("repeatTimes", event.getRepeatTimes());
		eventUpdate.set("repeatInterval", event.getRepeatInterval());
		eventUpdate.set("reminderTime", event.getReminderTime());
		eventUpdate.set("type", event.getType());
		eventUpdate.set("location", event.getLocation());
		eventUpdate.set("trainer", event.getTrainer());

		mongoOps.upsert(query(where("id").is(event.getId())), eventUpdate,
				ExerciseEvent.class);

	}

	/**
	 * Update selfcare event.
	 *
	 * @param event
	 *            the event
	 */
	public void updateSelfcareEvent(SelfcareEvent event) {
		Update eventUpdate = new Update();
		eventUpdate.set("startDate", event.getStartDate());
		eventUpdate.set("endDate", event.getEndDate());
		eventUpdate.set("title", event.getTitle());
		eventUpdate.set("groupID", event.getGroupID());
		eventUpdate.set("username", event.getUsername());
		eventUpdate.set("repeatTimes", event.getRepeatTimes());
		eventUpdate.set("repeatInterval", event.getRepeatInterval());
		eventUpdate.set("reminderTime", event.getReminderTime());
		eventUpdate.set("type", event.getType());
		eventUpdate.set("location", event.getLocation());

		mongoOps.upsert(query(where("id").is(event.getId())), eventUpdate,
				SelfcareEvent.class);

	}

	/**
	 * Update social event.
	 *
	 * @param event
	 *            the event
	 */
	public void updateSocialEvent(SocialEvent event) {
		Update eventUpdate = new Update();
		eventUpdate.set("startDate", event.getStartDate());
		eventUpdate.set("endDate", event.getEndDate());
		eventUpdate.set("title", event.getTitle());
		eventUpdate.set("groupID", event.getGroupID());
		eventUpdate.set("username", event.getUsername());
		eventUpdate.set("repeatTimes", event.getRepeatTimes());
		eventUpdate.set("repeatInterval", event.getRepeatInterval());
		eventUpdate.set("reminderTime", event.getReminderTime());
		eventUpdate.set("eventName", event.getEventName());
		eventUpdate.set("location", event.getLocation());
		eventUpdate.set("wallet", event.getWallet());
		eventUpdate.set("keys", event.getKeys());
		eventUpdate.set("otherBelongings", event.getOtherBelongings());
		eventUpdate.set("transportation", event.getTransportation());

		mongoOps.upsert(query(where("id").is(event.getId())), eventUpdate,
				SocialEvent.class);

	}

	/**
	 * Delete medical event.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteMedicalEvent(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, MedicalEvent.class);
	}

	/**
	 * Delete medical events.
	 *
	 * @param groupID the group id
	 */
	public void deleteMedicalEvents(String groupID) {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupID").is(groupID));
		mongoOps.findAllAndRemove(query, MedicalEvent.class);
	}

	/**
	 * Delete exercise event.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteExerciseEvent(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, ExerciseEvent.class);
	}

	/**
	 * Delete exercise events.
	 *
	 * @param groupID the group id
	 */
	public void deleteExerciseEvents(String groupID) {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupID").is(groupID));
		mongoOps.findAllAndRemove(query, ExerciseEvent.class);
	}

	/**
	 * Delete selfcare event.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteSelfcareEvent(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, SelfcareEvent.class);
	}

	/**
	 * Delete selfcare events.
	 *
	 * @param groupID the group id
	 */
	public void deleteSelfcareEvents(String groupID) {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupID").is(groupID));
		mongoOps.findAllAndRemove(query, SelfcareEvent.class);
	}

	/**
	 * Delete social event.
	 *
	 * @param id
	 *            the id
	 */
	public void deleteSocialEvent(String id) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(id));
		mongoOps.findAndRemove(query, SocialEvent.class);
	}

	/**
	 * Delete social events.
	 *
	 * @param groupID the group id
	 */
	public void deleteSocialEvents(String groupID) {
		Query query = new Query();
		query.addCriteria(Criteria.where("groupID").is(groupID));
		mongoOps.findAllAndRemove(query, SocialEvent.class);
	}

}