package eu.ewall.platform.idss.utils.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class has a toMap() method that uses Jackson to convert the object to a
 * map and a toString() method that returns the simple class name and the map
 * string. It also implements hashCode() and equals() using the map. Extending
 * this class is an easy way to get a meaningful toString(). If extending is not
 * possible, you may use the static toString() method in this class.
 * 
 * @author Dennis Hofs (RRD)
 */
public class JsonObject {
	@Override
	public int hashCode() {
		return toMap().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj.getClass() != getClass())
			return false;
		JsonObject other = (JsonObject)obj;
		return toMap().equals(other.toMap());
	}

	@Override
	public String toString() {
		return toString(this);
	}

	/**
	 * Returns this object as a map.
	 *
	 * @return the map
	 */
	public Map<?,?> toMap() {
		return toMap(this);
	}

	/**
	 * Returns the string representation for the specified object.
	 * 
	 * @param obj the object
	 * @return the string representation
	 */
	public static String toString(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		LinkedHashMap<?,?> map = mapper.convertValue(obj, LinkedHashMap.class);
		return obj.getClass().getSimpleName() + " " + map;
	}

	/**
	 * Returns the specified object as a map.
	 *
	 * @param obj the object
	 * @return the map
	 */
	public static Map<?,?> toMap(Object obj) {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.convertValue(obj, LinkedHashMap.class);
	}
}
