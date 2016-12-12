package eu.ewall.platform.middleware.datamanager.dao.ewallsystem;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import eu.ewall.platform.commons.datamodel.ewallsystem.Heartbeat;
import eu.ewall.platform.middleware.datamanager.dao.config.dbtype.EWallDBType;
import eu.ewall.platform.middleware.datamanager.dao.config.factory.MongoDBFactory;

/**
 * The Class HeartbeatDao.
 */
public class HeartbeatDao {

	/** The log. */
	private static final Logger LOG = LoggerFactory
			.getLogger(HeartbeatDao.class);

	/** The Constant HEARTBEATS_COLLECTION. */
	public static final String HEARTBEATS_COLLECTION = "heartbeat";

	/** The mongo ops. */
	private MongoOperations mongoOps;

	/**
	 * Instantiates a new sensing environment dao.
	 */
	public HeartbeatDao() {
		mongoOps = MongoDBFactory
				.getMongoOperationsForDBType(EWallDBType.DATA);
	}

	/**
	 * Gets the all sensing environments.
	 *
	 * @return the all sensing environments
	 */
	public List<Heartbeat> getHeartbeats(String username, int last) {
		LOG.debug("Retrieveing last " + last + " heartbeats from mongodb for user " + username);
		Query query = query(where("username").is(username)).with(new Sort(Sort.Direction.DESC, "date")).limit(last);
		List<Heartbeat> heartbeats = (List<Heartbeat>) mongoOps.find(query, Heartbeat.class, HEARTBEATS_COLLECTION);
		return heartbeats;
	}

	public List<Heartbeat> getHeartbeats(String username, Date from, Date to) {
		LOG.debug("Retrieveing heartbeats by time range from mongodb for user " + username);
		Criteria criteria = where("username").is(username);
		criteria = criteria.andOperator(Criteria.where("date").gte(from), Criteria.where("date").lt(to));
		Query query = query(criteria).with(new Sort(Sort.Direction.ASC, "date"));
		List<Heartbeat> heartbeats = (List<Heartbeat>) mongoOps.find(query, Heartbeat.class, HEARTBEATS_COLLECTION);
		return heartbeats;
	}

	/**
	 * Adds the sensing environment.
	 *
	 * @param sensingEnvironment
	 *            the sensing environment
	 * @return true, if successful
	 */
	public boolean addHeartbeat(Heartbeat heartbeat) {
		mongoOps.insert(heartbeat, HEARTBEATS_COLLECTION);

		return true;
	}


}
