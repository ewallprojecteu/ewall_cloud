package eu.ewall.platform.idss.utils;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class can format maps, lists and primitives (boolean, number, string).
 * 
 * @author Dennis Hofs (RRD)
 */
public class DataFormatter {
	private static final String INDENT = "    ";

	/**
	 * Returns a string representation of the specified value (map, list or
	 * primitive). If "human" is true, the returned string will have a friendly
	 * formatting, possibly spanning multiple lines.
	 *
	 * @param value the value to format
	 * @param human true for friendly formatting, false for single-line
	 * formatting
	 * @return the string
	 */
	public String format(Object value, boolean human) {
		return valueToString(value, human, 0);
	}
	
	/**
	 * Returns a string representation of the specified value (map, list or
	 * primitive). If "human" is true, the returned string will have a friendly
	 * formatting, possibly spanning multiple lines.
	 * 
	 * @param value the value to format
	 * @param human true for friendly formatting, false for single-line
	 * formatting
	 * @param indent the number of times to indent each line except the first
	 * line
	 * @return the string
	 */
	private String valueToString(Object value, boolean human, int indent) {
		if (value == null) {
			return "null";
		} else if (value instanceof List) {
			return listToString((List<?>)value, human, indent);
		} else if (value instanceof Map) {
			return mapToString((Map<?,?>)value, human, indent);
		} else {
			return value.toString();
		}
	}

	/**
	 * Returns a string representation of the specified map. If "human" is
	 * true, the returned string will have a friendly formatting, possibly
	 * spanning multiple lines.
	 * 
	 * @param map the map to format
	 * @param human true for friendly formatting, false for single-line
	 * formatting
	 * @param indent the number of times to indent each line except the first
	 * line
	 * @return the string
	 */
	private String mapToString(Map<?,?> map, boolean human, int indent) {
		Set<?> keys = map.keySet();
		if (keys.isEmpty())
			return "{}";
		StringBuffer buffer = new StringBuffer();
		String newline = System.getProperty("line.separator");
		buffer.append("{");
		if (human && keys.size() > 1) {
			buffer.append(newline);
			indent(buffer, indent + 1);
		} else if (human) {
			buffer.append(" ");
		}
		boolean first = true;
		for (Object key : keys) {
			if (!first) {
				buffer.append(",");
				if (human) {
					buffer.append(newline);
					indent(buffer, indent + 1);
				}
			}
			first = false;
			buffer.append(key + ":");
			if (human)
				buffer.append(" ");
			Object val = map.get(key);
			String valStr = valueToString(val, human, indent + 1);
			buffer.append(valStr);
		}
		if (human && keys.size() > 1) {
			buffer.append(newline);
			indent(buffer, indent);
		} else if (human) {
			buffer.append(" ");
		}
		buffer.append("}");
		return buffer.toString();
	}
	
	/**
	 * Returns a string representation of the specified list. If "human" is
	 * true, the returned string will have a friendly formatting, possibly
	 * spanning multiple lines.
	 * 
	 * @param list the list to format
	 * @param human true for friendly formatting, false for single-line
	 * formatting
	 * @param indent the number of times to indent each line except the first
	 * line
	 * @return the string
	 */
	private String listToString(List<?> list, boolean human, int indent) {
		if (list.isEmpty())
			return "[]";
		StringBuffer buffer = new StringBuffer();
		String newline = System.getProperty("line.separator");
		buffer.append("[");
		if (human && list.size() > 1) {
			buffer.append(newline);
			indent(buffer, indent + 1);
		} else if (human) {
			buffer.append(" ");
		}
		boolean first = true;
		for (Object item : list) {
			if (!first) {
				buffer.append(",");
				if (human) {
					buffer.append(newline);
					indent(buffer, indent + 1);
				}
			}
			first = false;
			String itemStr = valueToString(item, human, indent + 1);
			buffer.append(itemStr);
		}
		if (human && list.size() > 1) {
			buffer.append(newline);
			indent(buffer, indent);
		} else if (human) {
			buffer.append(" ");
		}
		buffer.append("]");
		return buffer.toString();
	}
	
	/**
	 * Appends a specified number of indentations to a buffer.
	 * 
	 * @param buffer the string buffer
	 * @param indent the number of indentations to append
	 */
	private void indent(StringBuffer buffer, int indent) {
		for (int i = 0; i < indent; i++) {
			buffer.append(INDENT);
		}
	}
}
