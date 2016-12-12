package eu.ewall.platform.idss.dao.memdb;

import eu.ewall.platform.idss.dao.DatabaseCriteria.And;
import eu.ewall.platform.idss.dao.DatabaseCriteria.Equal;
import eu.ewall.platform.idss.dao.DatabaseCriteria.GreaterEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.GreaterThan;
import eu.ewall.platform.idss.dao.DatabaseCriteria.LessEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.LessThan;
import eu.ewall.platform.idss.dao.DatabaseCriteria.NotEqual;
import eu.ewall.platform.idss.dao.DatabaseCriteria.Or;

import eu.ewall.platform.idss.dao.DatabaseCriteria;

/**
 * This class can check whether a database record matches specified criteria.
 * 
 * @author Dennis Hofs (RRD)
 */
public class CriteriaMatcher {
	
	/**
	 * Returns whether a database record matches the specified criteria. 
	 * 
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	public boolean matches(MemoryDatabaseRecord record, DatabaseCriteria criteria) {
		if (criteria instanceof Equal) {
			return matchesEqual(record, (Equal)criteria);
		} else if (criteria instanceof NotEqual) {
			return matchesNotEqual(record, (NotEqual)criteria);
		} else if (criteria instanceof LessThan) {
			return matchesLessThan(record, (LessThan)criteria);
		} else if (criteria instanceof GreaterThan) {
			return matchesGreaterThan(record, (GreaterThan)criteria);
		} else if (criteria instanceof LessEqual) {
			return matchesLessEqual(record, (LessEqual)criteria);
		} else if (criteria instanceof GreaterEqual) {
			return matchesGreaterEqual(record, (GreaterEqual)criteria);
		} else if (criteria instanceof And) {
			return matchesAnd(record, (And)criteria);
		} else if (criteria instanceof Or) {
			return matchesOr(record, (Or)criteria);
		} else {
			throw new RuntimeException(
					"Subclass of DatabaseCriteria not supported: " +
					criteria.getClass().getName());
		}
	}
	
	/**
	 * Returns whether a database record matches the specified Equal criteria.
	 * 
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesEqual(MemoryDatabaseRecord record, Equal criteria) {
		Object val1 = record.getField(criteria.getColumn());
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
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesNotEqual(MemoryDatabaseRecord record,
			NotEqual criteria) {
		Object val1 = record.getField(criteria.getColumn());
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
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesLessThan(MemoryDatabaseRecord record,
			LessThan criteria) {
		Object val1 = record.getField(criteria.getColumn());
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
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesGreaterThan(MemoryDatabaseRecord record,
			GreaterThan criteria) {
		Object val1 = record.getField(criteria.getColumn());
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
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesLessEqual(MemoryDatabaseRecord record,
			LessEqual criteria) {
		Object val1 = record.getField(criteria.getColumn());
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
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesGreaterEqual(MemoryDatabaseRecord record,
			GreaterEqual criteria) {
		Object val1 = record.getField(criteria.getColumn());
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
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesAnd(MemoryDatabaseRecord record, And criteria) {
		DatabaseCriteria[] ops = criteria.getOperands();
		for (DatabaseCriteria op : ops) {
			if (!matches(record, op))
				return false;
		}
		return true;
	}

	/**
	 * Returns whether a database record matches the specified Or criteria.
	 * 
	 * @param record the record
	 * @param criteria the criteria
	 * @return true if the record matches the criteria, false otherwise
	 */
	private boolean matchesOr(MemoryDatabaseRecord record, Or criteria) {
		DatabaseCriteria[] ops = criteria.getOperands();
		for (DatabaseCriteria op : ops) {
			if (matches(record, op))
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
