package eu.ewall.platform.idss.dao;

import java.util.Map;

import eu.ewall.platform.idss.dao.DatabaseCriteria.And;
import eu.ewall.platform.idss.dao.DatabaseCriteria.Equal;
import eu.ewall.platform.idss.dao.DatabaseCriteria.GreaterEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.GreaterThan;
import eu.ewall.platform.idss.dao.DatabaseCriteria.LessEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.LessThan;
import eu.ewall.platform.idss.dao.DatabaseCriteria.NotEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.Or;

/**
 * This class can check whether a database object matches specified criteria.
 * 
 * @author Dennis Hofs (RRD)
 */
public class DatabaseCriteriaMatcher {
	
	/**
	 * Returns whether a database object matches the specified criteria. 
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	public boolean matches(DatabaseObject object, DatabaseCriteria criteria) {
		DatabaseObjectMapper mapper = new DatabaseObjectMapper();
		return matches(mapper.objectToMap(object), criteria);
	}
	
	/**
	 * Returns whether a database object matches the specified criteria. The
	 * object should be specified as a map that can be obtained from {@link
	 * DatabaseObjectMapper DatabaseObjectMapper}.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	public boolean matches(Map<String,Object> object,
			DatabaseCriteria criteria) {
		if (criteria instanceof Equal) {
			return matchesEqual(object, (Equal)criteria);
		} else if (criteria instanceof NotEqual) {
			return matchesNotEqual(object, (NotEqual)criteria);
		} else if (criteria instanceof LessThan) {
			return matchesLessThan(object, (LessThan)criteria);
		} else if (criteria instanceof GreaterThan) {
			return matchesGreaterThan(object, (GreaterThan)criteria);
		} else if (criteria instanceof LessEqual) {
			return matchesLessEqual(object, (LessEqual)criteria);
		} else if (criteria instanceof GreaterEqual) {
			return matchesGreaterEqual(object, (GreaterEqual)criteria);
		} else if (criteria instanceof And) {
			return matchesAnd(object, (And)criteria);
		} else if (criteria instanceof Or) {
			return matchesOr(object, (Or)criteria);
		} else {
			throw new RuntimeException(
					"Subclass of DatabaseCriteria not supported: " +
					criteria.getClass().getName());
		}
	}
	
	/**
	 * Returns whether a database object matches the specified Equal criteria.
	 * 
	 * @param record the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesEqual(Map<String,Object> object, Equal criteria) {
		Object val1 = object.get(criteria.getColumn());
		Object val2 = criteria.getValue();
		if (val1 == null && val2 == null)
			return true;
		if (val1 == null || val2 == null)
			return false;
		return val1.equals(val2);
	}

	/**
	 * Returns whether a database record matches the specified NotEqual
	 * criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesNotEqual(Map<String,Object> object,
			NotEqual criteria) {
		Object val1 = object.get(criteria.getColumn());
		Object val2 = criteria.getValue();
		if (val1 == null && val2 == null)
			return false;
		if (val1 == null || val2 == null)
			return true;
		return !val1.equals(val2);
	}

	/**
	 * Returns whether a database record matches the specified LessThan
	 * criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesLessThan(Map<String,Object> object,
			LessThan criteria) {
		Object val1 = object.get(criteria.getColumn());
		Object val2 = criteria.getValue();
		if (val1 == null && val2 == null)
			return false;
		if (val1 == null)
			return false;
		if (val2 == null)
			return true;
		if (isInt(val1) && isInt(val2)) {
			long n1 = ((Number)val1).longValue();
			long n2 = ((Number)val2).longValue();
			return n1 < n2;
		} else if (val1 instanceof Number && val2 instanceof Number) {
			double n1 = ((Number)val1).doubleValue();
			double n2 = ((Number)val2).doubleValue();
			return n1 < n2;
		} else {
			String s1 = val1.toString();
			String s2 = val2.toString();
			return s1.compareTo(s2) < 0;
		}
	}

	/**
	 * Returns whether a database record matches the specified GreaterThan
	 * criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesGreaterThan(Map<String,Object> object,
			GreaterThan criteria) {
		Object val1 = object.get(criteria.getColumn());
		Object val2 = criteria.getValue();
		if (val1 == null && val2 == null)
			return false;
		if (val1 == null)
			return true;
		if (val2 == null)
			return false;
		if (isInt(val1) && isInt(val2)) {
			long n1 = ((Number)val1).longValue();
			long n2 = ((Number)val2).longValue();
			return n1 > n2;
		} else if (val1 instanceof Number && val2 instanceof Number) {
			double n1 = ((Number)val1).doubleValue();
			double n2 = ((Number)val2).doubleValue();
			return n1 > n2;
		} else {
			String s1 = val1.toString();
			String s2 = val2.toString();
			return s1.compareTo(s2) > 0;
		}
	}

	/**
	 * Returns whether a database record matches the specified LessEqual
	 * criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesLessEqual(Map<String,Object> object,
			LessEqual criteria) {
		Object val1 = object.get(criteria.getColumn());
		Object val2 = criteria.getValue();
		if (val1 == null && val2 == null)
			return true;
		if (val1 == null)
			return false;
		if (val2 == null)
			return true;
		if (isInt(val1) && isInt(val2)) {
			long n1 = ((Number)val1).longValue();
			long n2 = ((Number)val2).longValue();
			return n1 <= n2;
		} else if (val1 instanceof Number && val2 instanceof Number) {
			double n1 = ((Number)val1).doubleValue();
			double n2 = ((Number)val2).doubleValue();
			return n1 <= n2;
		} else {
			String s1 = val1.toString();
			String s2 = val2.toString();
			return s1.compareTo(s2) <= 0;
		}
	}

	/**
	 * Returns whether a database record matches the specified GreaterEqual
	 * criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesGreaterEqual(Map<String,Object> object,
			GreaterEqual criteria) {
		Object val1 = object.get(criteria.getColumn());
		Object val2 = criteria.getValue();
		if (val1 == null && val2 == null)
			return true;
		if (val1 == null)
			return true;
		if (val2 == null)
			return false;
		if (isInt(val1) && isInt(val2)) {
			long n1 = ((Number)val1).longValue();
			long n2 = ((Number)val2).longValue();
			return n1 >= n2;
		} else if (val1 instanceof Number && val2 instanceof Number) {
			double n1 = ((Number)val1).doubleValue();
			double n2 = ((Number)val2).doubleValue();
			return n1 >= n2;
		} else {
			String s1 = val1.toString();
			String s2 = val2.toString();
			return s1.compareTo(s2) >= 0;
		}
	}

	/**
	 * Returns whether a database record matches the specified And criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesAnd(Map<String,Object> object, And criteria) {
		DatabaseCriteria[] ops = criteria.getOperands();
		for (DatabaseCriteria op : ops) {
			if (!matches(object, op))
				return false;
		}
		return true;
	}

	/**
	 * Returns whether a database record matches the specified Or criteria.
	 * 
	 * @param object the database object
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesOr(Map<String,Object> object, Or criteria) {
		DatabaseCriteria[] ops = criteria.getOperands();
		for (DatabaseCriteria op : ops) {
			if (matches(object, op))
				return true;
		}
		return false;
	}

	/**
	 * Returns whether the specified object is an integer object (Byte, Short,
	 * Integer or Long).
	 * 
	 * @param obj the object
	 * @return true if it is an integer object, false otherwise
	 */
	private boolean isInt(Object obj) {
		return obj instanceof Byte || obj instanceof Short ||
				obj instanceof Integer || obj instanceof Long;
	}
}
