package eu.ewall.servicebrick.physicalactivity.dao;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import eu.ewall.servicebrick.common.AggregationPeriod;
import eu.ewall.servicebrick.common.AggregationUnit;
import eu.ewall.servicebrick.common.dao.TimeZoneDaoSupport;
import eu.ewall.servicebrick.common.time.TimeZoneContext;
import eu.ewall.servicebrick.common.time.UserTimeZoneProvider;
import eu.ewall.servicebrick.physicalactivity.model.Movement;
import eu.ewall.servicebrick.physicalactivity.model.FitBit_Movement;

/**
 * DAO that reads and writes movement data in Mongo DB.
 */
@Component
public class MovementDao extends TimeZoneDaoSupport {
	
	@Autowired
	public MovementDao(MongoOperations mongoOps, TimeZoneContext timeZoneCtx, 
			UserTimeZoneProvider userTimeZoneProvider) {
		super(mongoOps, timeZoneCtx, userTimeZoneProvider);
	}

	/**
	 * Reads the list of movements for the given user in the given time interval, with the given aggregation period.
	 * Supported aggregation periods are 1h, 1d, 1wk and 1mo. Any other value causes an exception.
	 * Dates are returned in the time zone associated to the user profile.
	 * 
	 * @param username the ID of the user to get data for
	 * @param from the start of the time interval
	 * @param to the end of the time interval
	 * @param aggregation the aggregation period
	 * @return the list of movement data aggregated on the given period or an empty list if no data is found
	 */
	public <T> List<T> readMovements(String username, DateTime from, DateTime to, AggregationPeriod aggregation,  Class<T> clazz ) {
		int aggregationLength = aggregation.getLength();
		AggregationUnit aggregationUnit = aggregation.getUnit();
		if (aggregationLength != 1 
				&& !(aggregationUnit.equals(AggregationUnit.HOUR) 
				     || aggregationUnit.equals(AggregationUnit.DAY)
				     || aggregationUnit.equals(AggregationUnit.WEEK)
				     || aggregationUnit.equals(AggregationUnit.MONTH))) {
			throw new IllegalArgumentException("Unsupported aggregation period: " + aggregation);
		}
		Query query = query(where("username").is(username).and("from").gte(from).and("to").lte(to)
				.and("aggregation").is(aggregation)).with(new Sort(Sort.Direction.ASC, "from"));
		return findWithUserTimeZone(query, clazz, username);
	}
	
/*	public List<Movement> readMovements(String username, DateTime from, DateTime to, AggregationPeriod aggregation) {
		int aggregationLength = aggregation.getLength();
		AggregationUnit aggregationUnit = aggregation.getUnit();
		if (aggregationLength != 1 
				&& !(aggregationUnit.equals(AggregationUnit.HOUR) 
				     || aggregationUnit.equals(AggregationUnit.DAY)
				     || aggregationUnit.equals(AggregationUnit.WEEK)
				     || aggregationUnit.equals(AggregationUnit.MONTH))) {
			throw new IllegalArgumentException("Unsupported aggregation period: " + aggregation);
		}
		Query query = query(where("username").is(username).and("from").gte(from).and("to").lte(to)
				.and("aggregation").is(aggregation)).with(new Sort(Sort.Direction.ASC, "from"));
		return findWithUserTimeZone(query, Movement.class, username);
	}*/
	/**
	 * Same as readMovements but expects to find at most one movement; an exception is thrown if multiple movements are
	 * found.
	 * 
	 * @return the requested movement or null if not found
	 */
	public Movement readMovement(String username, DateTime from, DateTime to, AggregationPeriod aggregation) {
		List<Movement> movements = readMovements(username, from, to, aggregation, Movement.class);
		if (movements.size() == 0) {
			return null;
		} else if (movements.size() == 1) {
			return movements.get(0);
		} else {
			log.error("Multiple movements found for user = " + username + ", from = " + from + ", to = " + to + ", aggregation = " + aggregation);
			throw new RuntimeException("Multiple movements found");
		}
	}
	
	/**
	 * Inserts a new movement in the database.
	 * 
	 * @param movement the movement to insert
	 */
	public void insertMovement(Movement movement) {
		log.debug("Inserting new Movement: " + movement);
		mongoOps.insert(movement);
	}
	
	/**
	 * Inserts a collection of new movements in the database.
	 * 
	 * @param movements the movements to insert
	 */
	public void insertMovements(ArrayList<Movement> movements) {
		log.debug("Inserting " + movements.size() + " new movements");
		mongoOps.insert(movements, Movement.class);
	}
	
	/**
	 * Updates an existing movement with the specified data.
	 * 
	 * @param movement the movement to update
	 */
	public void updateMovement(Movement movement) {
		log.debug("Updating Movement: " + movement);
		mongoOps.save(movement);
	}
	
}
