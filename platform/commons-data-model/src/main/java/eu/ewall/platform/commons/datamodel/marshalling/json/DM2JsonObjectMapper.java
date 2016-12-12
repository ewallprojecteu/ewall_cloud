/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.marshalling.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


/**
 * The Class DM2JsonObjectMapper.
 *
 * @author eandgrg
 */
public class DM2JsonObjectMapper {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(DM2JsonObjectMapper.class);
	
	/** The singleton. */
	private static DM2JsonObjectMapper singleton;

	/** The mapper. */
	private static ObjectMapper mapper;
	
	

	/**
	 * Instantiates a new json object mapper.
	 */
	private DM2JsonObjectMapper() {

	}

	/**
	 * Gets the single instance of DM2JsonObjectMapper.
	 *
	 * @return single instance of DM2JsonObjectMapper
	 */
	private static void getInstance() {
		if (singleton == null || mapper == null) {
			singleton = new DM2JsonObjectMapper();
			mapper = new ObjectMapper();
		}
	}

	/**
	 * Write value as string.
	 *
	 * @param object
	 *            the object
	 * @return the string
	 */
	public static String writeValueAsString(Object object) {
		getInstance();
		// enable pretty output
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		// Use class name as root key for JSON Jackson serialization
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, true);
		String returnString = null;
		try {
			returnString = mapper.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			LOG.error(e.getMessage());
		}

		return returnString;
	}

	/**
	 * Read value as string.
	 *
	 * @param jsonstring
	 *            the jsonstring
	 * @param objectClass
	 *            the object class
	 * @return the object
	 */
	public static Object readValueAsString(String jsonstring,
			Class<?> objectClass) {
		getInstance();

		// ignore parent tag from json
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		Object returnObject = null;

		try {
			returnObject = mapper.readValue(jsonstring, objectClass);
		} catch (JsonParseException e) {
			LOG.error(e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return returnObject;
	}

	/**
	 * Read values as string to array list.
	 *
	 * @param jsonstring
	 *            the jsonstring
	 * @param objectClass
	 *            the object class
	 * @return the array list
	 */
	public static ArrayList<Object> readValuesAsStringToArrayList(
			String jsonstring, Class<?> objectClass) {
		getInstance();

		// ignore parent tag from json
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		ArrayList<Object> returnObjectsList = new ArrayList<Object>();

		try {
			returnObjectsList = mapper.readValue(
					jsonstring,
					mapper.getTypeFactory().constructCollectionType(
							ArrayList.class, objectClass));
		} catch (JsonParseException e) {
			LOG.error(e.getMessage());;
		} catch (JsonMappingException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return returnObjectsList;
	}

	/**
	 * Read values as string.
	 *
	 * @param jsonstring
	 *            the jsonstring
	 * @param objectClass
	 *            the object class
	 * @return the list
	 */
	public static Set<Object> readValuesAsStringToSet(String jsonstring,
			Class<?> objectClass) {
		getInstance();

		// ignore parent tag from json
		mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, true);

		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
				false);

		Set<Object> returnObjectsSet = new HashSet<Object>();

		try {
			returnObjectsSet = mapper.readValue(
					jsonstring,
					mapper.getTypeFactory().constructCollectionType(
							HashSet.class, objectClass));
		} catch (JsonParseException e) {
			LOG.error(e.getMessage());
		} catch (JsonMappingException e) {
			LOG.error(e.getMessage());
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
		return returnObjectsSet;
	}

}
