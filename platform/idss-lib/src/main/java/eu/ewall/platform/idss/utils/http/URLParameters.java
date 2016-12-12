package eu.ewall.platform.idss.utils.http;

import eu.ewall.platform.idss.utils.exception.ParseException;

import java.io.UnsupportedEncodingException;

import java.net.URLDecoder;
import java.net.URLEncoder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Methods for creating and parsing URL-encoded parameter strings.
 *
 * @author Dennis Hofs (RRD)
 */
public class URLParameters {

	/**
	 * Creates a URL-encoded parameter string for the specified parameters.
	 * They will first be sorted by key. It uses UTF-8.
	 *
	 * @param params the parameters
	 * @return the parameter string
	 */
	public static String getSortedParameterString(Map<String,String> params) {
		Map<String,String> sortedMap = new LinkedHashMap<String,String>();
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			sortedMap.put(key, params.get(key));
		}
		return getParameterString(sortedMap);
	}

	/**
	 * Creates a URL-encoded parameter string for the specified parameters. It
	 * uses UTF-8.
	 *
	 * @param params the parameters
	 * @return the parameter string
	 */
	public static String getParameterString(Map<String,String> params) {
		StringBuilder builder = new StringBuilder();
		for (String key : params.keySet()) {
			if (builder.length() > 0)
				builder.append("&");
			try {
				builder.append(key + "=" + URLEncoder.encode(params.get(key),
						"UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException("UTF-8 not supported: " +
						ex.getMessage(), ex);
			}
		}
		return builder.toString();
	}

	/**
	 * Parses a URL-encoded parameter string. It uses UTF-8.
	 *
	 * @param paramStr the parameter string
	 * @return the parameters
	 * @throws ParseException if the parameter string is invalid
	 */
	public static Map<String,String> parseParameterString(String paramStr)
			throws ParseException {
		Map<String,String> params = new LinkedHashMap<String,String>();
		String[] paramList = paramStr.split("&");
		for (String keyValStr : paramList) {
			String[] keyVal = keyValStr.split("=");
			if (keyVal.length != 2) {
				throw new ParseException("Invalid parameter string: " +
						paramStr);
			}
			try {
				if (keyVal[0].length() == 0) {
					throw new ParseException(
							"Empty key in parameter string: " + paramStr);
				}
				params.put(keyVal[0], URLDecoder.decode(keyVal[1], "UTF-8"));
			} catch (UnsupportedEncodingException ex) {
				throw new RuntimeException("UTF-8 not supported: " +
						ex.getMessage(), ex);
			}
		}
		return params;
	}
}
