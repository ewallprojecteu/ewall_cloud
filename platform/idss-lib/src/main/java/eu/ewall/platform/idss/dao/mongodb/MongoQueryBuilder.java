package eu.ewall.platform.idss.dao.mongodb;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import eu.ewall.platform.idss.dao.DatabaseCriteria.And;
import eu.ewall.platform.idss.dao.DatabaseCriteria.Equal;
import eu.ewall.platform.idss.dao.DatabaseCriteria.GreaterEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.GreaterThan;
import eu.ewall.platform.idss.dao.DatabaseCriteria.LessEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.LessThan;
import eu.ewall.platform.idss.dao.DatabaseCriteria.NotEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.Or;

import eu.ewall.platform.idss.dao.DatabaseCriteria;
import eu.ewall.platform.idss.dao.DatabaseSort;

import org.bson.types.ObjectId;

/**
 * This class can build Mongo query and sort documents from {@link
 * DatabaseCriteria DatabaseCriteria} and {@link DatabaseSort DatabaseSort}
 * objects.
 * 
 * @author Dennis Hofs (RRD)
 */
public class MongoQueryBuilder {
	
	/**
	 * Returns a Mongo query document for the specified criteria. If the
	 * criteria are null, it returns an empty document.
	 * 
	 * @param criteria the criteria or null
	 * @return the Mongo query document
	 */
	public DBObject buildCriteria(DatabaseCriteria criteria) {
		if (criteria == null)
			return new BasicDBObject();
		if (criteria instanceof Equal) {
			return buildEqual((Equal)criteria);
		} else if (criteria instanceof NotEqual) {
			return buildNotEqual((NotEqual)criteria);
		} else if (criteria instanceof LessThan) {
			return buildLessThan((LessThan)criteria);
		} else if (criteria instanceof GreaterThan) {
			return buildGreaterThan((GreaterThan)criteria);
		} else if (criteria instanceof LessEqual) {
			return buildLessEqual((LessEqual)criteria);
		} else if (criteria instanceof GreaterEqual) {
			return buildGreaterEqual((GreaterEqual)criteria);
		} else if (criteria instanceof And) {
			return buildAnd((And)criteria);
		} else if (criteria instanceof Or) {
			return buildOr((Or)criteria);
		} else {
			throw new RuntimeException(
					"Unknown subclass of DatabaseCriteria: " +
					criteria.getClass().getName());
		}
	}
	
	/**
	 * Builds a Mongo query document for the "equal" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildEqual(Equal criteria) {
		BasicDBObject query = new BasicDBObject();
		String key = criteria.getColumn();
		if (key.equals("id")) {
			query.append("_id", new ObjectId((String)criteria.getValue()));
		} else {
			query.append(key, criteria.getValue());
		}
		return query;
	}
	
	/**
	 * Builds a Mongo query document for the "not equal" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildNotEqual(NotEqual criteria) {
		return buildComparison(criteria.getColumn(), "$ne",
				criteria.getValue());
	}
	
	/**
	 * Builds a Mongo query document for the "less than" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildLessThan(LessThan criteria) {
		return buildComparison(criteria.getColumn(), "$lt",
				criteria.getValue());
	}
	
	/**
	 * Builds a Mongo query document for the "greater than" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildGreaterThan(GreaterThan criteria) {
		return buildComparison(criteria.getColumn(), "$gt",
				criteria.getValue());
	}
	
	/**
	 * Builds a Mongo query document for the "less or equal" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildLessEqual(LessEqual criteria) {
		return buildComparison(criteria.getColumn(), "$lte",
				criteria.getValue());
	}
	
	/**
	 * Builds a Mongo query document for the "greater or equal" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildGreaterEqual(GreaterEqual criteria) {
		return buildComparison(criteria.getColumn(), "$gte",
				criteria.getValue());
	}
	
	/**
	 * Builds a Mongo query document for the specified comparison operator.
	 * 
	 * @param col the column name ("id" will be converted to "_id")
	 * @param op the Mongo comparison operator
	 * @param val the comparison value
	 * @return the query document
	 */
	private DBObject buildComparison(String col, String op, Object val) {
		BasicDBObject cmp = new BasicDBObject();
		cmp.append(op, val);
		BasicDBObject query = new BasicDBObject();
		String key = col;
		if (key.equals("id"))
			key = "_id";
		query.append(key, cmp);
		return query;
	}

	/**
	 * Builds a Mongo query document for the "and" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildAnd(And criteria) {
		BasicDBList list = new BasicDBList();
		for (DatabaseCriteria arg : criteria.getOperands()) {
			list.add(buildCriteria(arg));
		}
		BasicDBObject query = new BasicDBObject();
		query.append("$and", list);
		return query;
	}

	/**
	 * Builds a Mongo query document for the "or" operator.
	 * 
	 * @param criteria the criteria
	 * @return the query document
	 */
	private DBObject buildOr(Or criteria) {
		BasicDBList list = new BasicDBList();
		for (DatabaseCriteria arg : criteria.getOperands()) {
			list.add(buildCriteria(arg));
		}
		BasicDBObject query = new BasicDBObject();
		query.append("$or", list);
		return query;
	}
	
	/**
	 * Returns a Mongo sort document for the specified sort properties.
	 * 
	 * @param sort the sort properties (at least length 1)
	 * @return the Mongo sort document
	 */
	public DBObject buildSort(DatabaseSort[] sort) {
		BasicDBObject doc = new BasicDBObject();
		for (DatabaseSort item : sort) {
			String key = item.getColumn();
			if (key.equals("id"))
				key = "_id";
			doc.append(key, item.isAscending() ? 1 : -1);
		}
		return doc;
	}
}
