package eu.ewall.servicebrick.common.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.ewall.servicebrick.common.dao.SensorEventDao;
import eu.ewall.servicebrick.common.model.SensorEvent;
import eu.ewall.servicebrick.common.model.SensorEventHistory;
import eu.ewall.servicebrick.common.validation.ServiceBrickInputValidator;

/**
 * Parent class with common logic for controllers that read sensor events.
 */
public abstract class SensorEventController {
	
	protected final Logger log;
	protected ServiceBrickInputValidator inputValidator;
	protected SensorEventDao sensorEventDao;
	
	public SensorEventController(ServiceBrickInputValidator inputValidator, SensorEventDao sensorEventDao) {
		log = LoggerFactory.getLogger(this.getClass());
		this.inputValidator = inputValidator;
		this.sensorEventDao = sensorEventDao;
	}

	public <E extends SensorEvent, H extends SensorEventHistory> H getEvents(String method, 
			Class<E> eventClass, Class<H> historyClass, String username, String location, DateTime from, DateTime to,
			Integer latestEvents) {
		if (from != null) {
			return getEventsInTimeInterval(method, eventClass, historyClass, username, location, from, to);
		} else {
			return getLatestEvents(method, eventClass, historyClass, username, location, latestEvents);
		}
	}
	
	public <E extends SensorEvent, H extends SensorEventHistory> H getEventsInTimeInterval(String method, 
			Class<E> eventClass, Class<H> historyClass, String username, String location, DateTime from, DateTime to) {
		long startMillis = 0;
		if (log.isDebugEnabled()) {
			startMillis = System.currentTimeMillis();
			String msgTemplate = "%s invoked with arguments: username = %s, location = %s, from = %s, to = %s";
			log.debug(String.format(msgTemplate, method, username, location, from, to));
		}
		inputValidator.validateTimeInterval(username, from, to);
		List<E> events = sensorEventDao.readEvents(eventClass, username, location, from, to);
		normalizeEvents(events);
		Constructor<H> constructor = getConstructor(historyClass, String.class, String.class, DateTime.class, 
				DateTime.class, List.class);
		H history = newInstance(constructor, username, location, from, to, events);
		if (log.isDebugEnabled()) {
			log.debug(method + " took " + (System.currentTimeMillis() - startMillis) + "ms");
		}
		return history;
	}
	
	
	public <E extends SensorEvent, H extends SensorEventHistory> H getEventsDomotics(String method, 
			Class<E> eventClass, Class<H> historyClass, String username, String location, DateTime from, DateTime to, String aggregation,
			Integer latestEvents) {
		if (from != null) {
			return getEventsInTimeIntervalDomotics(method, eventClass, historyClass, username, location, from, to, aggregation);
		} else {
			return getLatestDomoticsEvents(method, eventClass, historyClass, username, location, aggregation, latestEvents);
		}
	}
	
	public <E extends SensorEvent, H extends SensorEventHistory> H getEventsInTimeIntervalDomotics(String method, 
			Class<E> eventClass, Class<H> historyClass, String username, String location, DateTime from, DateTime to, String aggregation) {
		long startMillis = 0;
		if (log.isDebugEnabled()) {
			startMillis = System.currentTimeMillis();
			String msgTemplate = "%s invoked with arguments: username = %s, location = %s, from = %s, to = %s";
			log.debug(String.format(msgTemplate, method, username, location, from, to));
		}
		//inputValidator.validateTimeInterval(username, from, to);
		List<E> events = sensorEventDao.readDomoticsEvents(eventClass, username, location, from, to, aggregation);
		normalizeEvents(events);
		Constructor<H> constructor = getConstructor(historyClass, String.class, String.class, DateTime.class, 
				DateTime.class, List.class);
		H history = newInstance(constructor, username, location, from, to, events);
		if (log.isDebugEnabled()) {
			log.debug(method + " took " + (System.currentTimeMillis() - startMillis) + "ms");
		}
		return history;
	}
	
	public <E extends SensorEvent, H extends SensorEventHistory> H getLatestEvents(String method, 
			Class<E> eventClass, Class<H> historyClass, String username, String location, int latestEvents) {
		long startMillis = 0;
		if (log.isDebugEnabled()) {
			startMillis = System.currentTimeMillis();
			String msgTemplate = "%s invoked with arguments: username = %s, location = %s, latestEvents = %d";
			log.debug(String.format(msgTemplate, method, username, location, latestEvents));
		}
		inputValidator.validateLatestEvents(username, latestEvents);
		List<E> events = sensorEventDao.readLatestEvents(eventClass, username, location, latestEvents);
		normalizeEvents(events);
		Constructor<H> constructor = getConstructor(historyClass, String.class, String.class, Integer.TYPE,
				List.class);
		H history = newInstance(constructor, username, location, latestEvents, events);
		if (log.isDebugEnabled()) {
			log.debug(method + " took " + (System.currentTimeMillis() - startMillis) + "ms");
		}
		return history;
	}
	
	public <E extends SensorEvent, H extends SensorEventHistory> H getLatestDomoticsEvents(String method, 
			Class<E> eventClass, Class<H> historyClass, String username, String location,String aggregation, int latestEvents) {
		long startMillis = 0;
		if (log.isDebugEnabled()) {
			startMillis = System.currentTimeMillis();
			String msgTemplate = "%s invoked with arguments: username = %s, location = %s, latestEvents = %d";
			log.debug(String.format(msgTemplate, method, username, location, latestEvents));
		}
		inputValidator.validateLatestEvents(username, latestEvents);
		List<E> events = sensorEventDao.readLatestDomoticsEvents(eventClass, username, location, aggregation, latestEvents);
		normalizeEvents(events);
		Constructor<H> constructor = getConstructor(historyClass, String.class, String.class, Integer.TYPE,
				List.class);
		H history = newInstance(constructor, username, location, latestEvents, events);
		if (log.isDebugEnabled()) {
			log.debug(method + " took " + (System.currentTimeMillis() - startMillis) + "ms");
		}
		return history;
	}
	
	private <T> Constructor<T> getConstructor(Class<T> clazz, Class<?>... parameterTypes) {
		try {
			return clazz.getConstructor(parameterTypes);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}
	
	private <T> T newInstance(Constructor<T> constructor, Object... args) {
		try {
			return constructor.newInstance(args);
		} catch (InstantiationException | IllegalAccessException
				| InvocationTargetException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void normalizeEvents(List<? extends SensorEvent> events) {
		// Unset the username so that it's not repeated for every event in the JSON output
		for (SensorEvent event : events) {
			event.setUsername(null);
		}
	}
	
}
