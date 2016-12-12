package eu.ewall.platform.idss.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>This class can resolve escaped characters in a string and it can replace
 * special characters with escaped characters. It supports the following
 * escapes:</p>
 * 
 * <ul>
 * <li>\n -&gt; new line</li>
 * <li>\r -&gt; carriage return</li>
 * <li>\t -&gt; tab</li>
 * <li>\<i>c</i> (where <i>c</i> is any other character including \ and ")
 * -&gt; <i>c</i></li>
 * </ul>
 * 
 * @author Dennis Hofs
 */
public class StringEscape {
	
	/**
	 * Replaces escaped characters in the specified string. This method throws
	 * an exception if it finds a lone \.
	 * 
	 * @param s a string
	 * @return the string without escapes
	 * @throws Exception if a lone \ is found
	 */
	public static String resolveEscapes(String s) throws Exception {
		return resolveEscapes(s, new char[0]);
	}

	/**
	 * Replaces escaped characters in the specified string. This method throws
	 * an exception if it finds a lone \ or if one of the specified illegal
	 * characters is not escaped.
	 * 
	 * @param s a string
	 * @param illegals characters that must be escaped
	 * @return the string without escapes
	 * @throws Exception if a lone \ is found or if one of the illegal
	 * characters is not escaped
	 */
	public static String resolveEscapes(String s, char[] illegals) throws Exception {
		StringBuffer result = new StringBuffer();
		String patStr = "\\\\";
		for (char c : illegals) {
			patStr += "|" + c;
		}
		Pattern pattern = Pattern.compile(patStr);
		Matcher m = pattern.matcher(s);
		int start = 0;
		while (start < s.length() && m.find(start)) {
			int index = m.start();
			char c = s.charAt(index);
			if (c != '\\' || index == s.length() - 1)
				throw new Exception("Invalid escaped string: \"" + s + "\"");
			c = s.charAt(index + 1);
			result.append(s, start, index);
			switch (c) {
			case 'n':
				c = '\n';
				break;
			case 'r':
				c = '\r';
				break;
			case 't':
				c = '\t';
				break;
			}
			result.append(c);
			start = index + 2;
		}
		if (start < s.length())
			result.append(s.substring(start));
		return result.toString();
	}
	
	/**
	 * Replaces special characters with an escape sequence. It escapes the
	 * characters listed at the top of this page.
	 * 
	 * @param s a string
	 * @return the escaped string
	 */
	public static String escape(String s) {
		return escape(s, new char[0]);
	}

	/**
	 * Replaces special characters with an escape sequence. It escapes the
	 * characters listed at the top of this page as well as the specified
	 * illegal characters (which are considered illegal if they are not
	 * escaped).
	 * 
	 * @param s a string
	 * @param illegals characters that must be escaped
	 * @return the escaped string
	 */
	public static String escape(String s, char[] illegals) {
		String escaped = s.replaceAll("\\\\", "\\\\\\\\");
		escaped = escaped.replaceAll("\n", "\\\\n");
		escaped = escaped.replaceAll("\r", "\\\\r");
		escaped = escaped.replaceAll("\t", "\\\\t");
		for (char c : illegals) {
			escaped = escaped.replaceAll(Character.toString(c), "\\\\" + c);
		}
		return escaped;
	}
}
