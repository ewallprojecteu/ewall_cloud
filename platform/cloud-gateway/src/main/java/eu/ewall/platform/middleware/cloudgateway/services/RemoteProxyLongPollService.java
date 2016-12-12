package eu.ewall.platform.middleware.cloudgateway.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.async.DeferredResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import eu.ewall.platform.commons.datamodel.ewallsystem.ActuatorCommand;
import eu.ewall.platform.commons.datamodel.ewallsystem.LongPollMessage;
import eu.ewall.platform.commons.datamodel.ewallsystem.LongPollMessageType;

/**
 * The Class RemoteProxyLongPollService.
 */
@Service("remoteProxyStatusService")
public class RemoteProxyLongPollService {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(RemoteProxyLongPollService.class);

	/** The last id of ewall portal request. */
	private static long lastRequestID = 0L;

	/** The Constant LONG_POLL_TIMEOUT. */
	private static final long LONG_POLL_TIMEOUT = 1 * 60 * 1000; // 3min

	/** The Constant CLEAR_OBSOLETE_DEFFERED_RESULTS. */
	private static final long CLEAR_OBSOLETE_DEFFERED_RESULTS = 30 * 60 * 1000; // 30min

	/**
	 * The deferred remote proxy result map that stores remote proxy's deferred
	 * results (map of deferred responses to remote proxy from cloud gateway).
	 */
	private static volatile HashMap<String, DeferredResult<LongPollMessage>> deferredRemoteProxyResultMap = new HashMap<String, DeferredResult<LongPollMessage>>();

	/** The deferred remote proxy result map with sensing environment actuators. */
	private static volatile HashMap<ActuatorCommand, DeferredResult<LongPollMessage>> deferredRemoteProxyActuatorResultMap = new HashMap<ActuatorCommand, DeferredResult<LongPollMessage>>();

	/**
	 * The deferred ewall system result map that stores deferred ewall portal
	 * requests to remote proxy.
	 */
	private static volatile HashMap<Long, DeferredResult<ResponseEntity<String>>> deferredEwallSystemResultMap = new HashMap<Long, DeferredResult<ResponseEntity<String>>>();

	/**
	 * Method is called when remote proxy is checking for new status/config
	 * requests from ewall portal (requests are forward via cloud gateway).
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param remoteProxyRequestMessage
	 *            the remote proxy request message
	 * @return the deferred result that can contain new ewall request
	 */
	public DeferredResult<LongPollMessage> getUpdate(
			String sensing_environment_id,
			LongPollMessage remoteProxyRequestMessage) {

		LOG.info(
				"deferredEwallSystemResultMap size = {}, deferredRemoteProxyActuatorResultMap size = {}, deferredRemoteProxyResultMap size = {}", deferredEwallSystemResultMap.size(),
				deferredRemoteProxyActuatorResultMap.size(),
				deferredRemoteProxyResultMap.size());
		// if RP's getUpdate message contains no data, send empty deferred
		// response
		if (remoteProxyRequestMessage.getActionType() == LongPollMessageType.REQUEST_WITHOUT_DATA) {

		}

		// if RP's getUpdate message contains data, forward data to
		// corresponding ewall portal
		else if (remoteProxyRequestMessage.getActionType() == LongPollMessageType.REQUEST_WITH_DATA) {
			DeferredResult<ResponseEntity<String>> deferredEwallPortalResult = deferredEwallSystemResultMap
					.get(remoteProxyRequestMessage.getRequestID());
			if (deferredEwallPortalResult != null) {
				deferredEwallPortalResult.setResult(new ResponseEntity<String>(
						remoteProxyRequestMessage.getMessageData(),
						HttpStatus.OK));
				deferredEwallSystemResultMap.remove(deferredEwallPortalResult);
			}
			// ewall portal is missing
			else {
				LOG.error("getUpdate() - missing deferred result in deferredEwallSystemResultMap");
			}
		}

		// if RP's getUpdate message contains error, forward error to
		// corresponding ewall portal
		else if (remoteProxyRequestMessage.getActionType() == LongPollMessageType.REQUEST_WITH_ERROR) {
			DeferredResult<ResponseEntity<String>> deferredEwallPortalResult = deferredEwallSystemResultMap
					.get(remoteProxyRequestMessage.getRequestID());
			if (deferredEwallPortalResult != null) {
				deferredEwallPortalResult.setResult(new ResponseEntity<String>(
						HttpStatus.INTERNAL_SERVER_ERROR));
				deferredEwallSystemResultMap.remove(deferredEwallPortalResult);
			}
			// ewall portal is missing
			else {
				LOG.error("getUpdate() - missing deferred result with error in deferredEwallSystemResultMap");
			}
		}

		// respond to remote proxy with deferred result with LONG_POLL_TIMEOUT
		DeferredResult<LongPollMessage> RPDeferredResult = new DeferredResult<LongPollMessage>(
				LONG_POLL_TIMEOUT, new LongPollMessage(null,
						LongPollMessageType.NO_NEW_REQUESTS, ""));
		deferredRemoteProxyResultMap.put(sensing_environment_id,
				RPDeferredResult);

		return RPDeferredResult;
	}

	/**
	 * Saves the ewall portal's request for remote-proxy config.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @return the deferred result
	 */
	public DeferredResult<ResponseEntity<String>> setRPConfigRequest(
			String sensing_environment_id) {
		LOG.debug("setRPConfigRequest");

		DeferredResult<LongPollMessage> deferredRemoteProxyResult = deferredRemoteProxyResultMap
				.get(sensing_environment_id);

		/*
		 * if remote proxy is not listening (long polling failed) - reason: long
		 * polling disabled in local-platform properties
		 */
		if (deferredRemoteProxyResult == null) {
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.METHOD_NOT_ALLOWED));
			return def;
		}

		// set result data to remote proxy long polling request
		LongPollMessage lpmessage = new LongPollMessage(lastRequestID,
				LongPollMessageType.GET_CONFIG, "");
		deferredRemoteProxyResult.setResult(lpmessage);

		DeferredResult<ResponseEntity<String>> deferredEwallPortalResult = new DeferredResult<ResponseEntity<String>>(
				null, new ResponseEntity<String>(HttpStatus.GATEWAY_TIMEOUT));
		deferredEwallSystemResultMap.put(lastRequestID,
				deferredEwallPortalResult);

		// increment lastRequestID
		increaseLastRequestID();
		return deferredEwallPortalResult;
	}

	synchronized static void increaseLastRequestID() {
		// increment lastRequestID
		lastRequestID++;
	}

	/**
	 * Saves the ewall portal's action for setting remote-proxy configuration.
	 *
	 * @param sensing_environment_id
	 *            the sensing_environment_id
	 * @param config
	 *            the new remote proxy config
	 * @return the deferred result
	 */
	public DeferredResult<ResponseEntity<String>> setRPConfigData(
			String sensing_environment_id, Map<String, String> config) {
		LOG.debug("setRPConfigData");

		DeferredResult<LongPollMessage> deferredRemoteProxyResult = deferredRemoteProxyResultMap
				.get(sensing_environment_id);

		/*
		 * if remote proxy is not listening (long polling failed) - reason: long
		 * polling disabled in local-platform properties
		 */
		if (deferredRemoteProxyResult == null) {
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.METHOD_NOT_ALLOWED));
			return def;
		}

		// set result data to remote proxy long polling request
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		String configInJson;
		try {
			configInJson = ow.writeValueAsString(config);
			LongPollMessage lpmessage = new LongPollMessage(lastRequestID,
					LongPollMessageType.SET_CONFIG, configInJson);
			deferredRemoteProxyResult.setResult(lpmessage);
		} catch (JsonProcessingException e) {
			LOG.error(e.getMessage());
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
			return def;
		}

		DeferredResult<ResponseEntity<String>> deferredEwallPortalResult = new DeferredResult<ResponseEntity<String>>(
				null, new ResponseEntity<String>(HttpStatus.GATEWAY_TIMEOUT));
		deferredEwallSystemResultMap.put(lastRequestID,
				deferredEwallPortalResult);

		// increment lastRequestID
		increaseLastRequestID();
		return deferredEwallPortalResult;
	}

	/**
	 * Sets the rp actuator command.
	 *
	 * @param actuatorCommand
	 *            the actuator command
	 * @param sensEnvId
	 *            the sens env id
	 * @return the deferred result
	 */
	public DeferredResult<ResponseEntity<String>> setRPActuatorCommand(
			ActuatorCommand actuatorCommand, String sensEnvId) {
		LOG.debug("setRPActuatorControl");

		// logic may be problematic, must be something unique for
		// actuator in sens env here,
		// because if is listening for status, it will be conflict and something
		// may be lost
		DeferredResult<LongPollMessage> deferredRemoteProxyResult = deferredRemoteProxyResultMap
				.get(sensEnvId);

		/*
		 * if remote proxy is not listening (long polling failed) - reason: long
		 * polling disabled in local-platform properties
		 */
		if (deferredRemoteProxyResult == null) {
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.METHOD_NOT_ALLOWED));
			return def;
		}

		String actuatorCommandJson;
		try {
			ObjectWriter ow = new ObjectMapper().writer()
					.withDefaultPrettyPrinter();
			actuatorCommandJson = ow.writeValueAsString(actuatorCommand);
		} catch (Exception e) {
			DeferredResult<ResponseEntity<String>> def = new DeferredResult<ResponseEntity<String>>();
			def.setResult(new ResponseEntity<String>(
					HttpStatus.INTERNAL_SERVER_ERROR));
			return def;
		}

		// set result data to remote proxy long polling request
		LongPollMessage lpmessage = new LongPollMessage(lastRequestID,
				LongPollMessageType.UPDATE_ACTUATOR_COMMAND,
				actuatorCommandJson);
		deferredRemoteProxyResult.setResult(lpmessage);

		DeferredResult<ResponseEntity<String>> deferredEwallPortalResult = new DeferredResult<ResponseEntity<String>>(
				null, new ResponseEntity<String>(HttpStatus.GATEWAY_TIMEOUT));
		deferredEwallSystemResultMap.put(lastRequestID,
				deferredEwallPortalResult);

		// increment lastRequestID
		increaseLastRequestID();
		return deferredEwallPortalResult;
	}

	/**
	 * Clear setted od expired deferred results from hashMaps.
	 */
	@Scheduled(fixedDelay = CLEAR_OBSOLETE_DEFFERED_RESULTS)
	public void clearObsoletedDefferedResults() {
		LOG.debug("clearObsoletedDefferedResults");

		clearRPKeys();
		clearRPActuatorKeys();
		clearEWallKeys();
	}

	/**
	 * Clear e wall keys.
	 */
	private void clearEWallKeys() {
		// get all ewall portal's setted or expired deferred results ..
		List<Long> EwallKeysToDelete = new ArrayList<Long>();

		for (long key : deferredEwallSystemResultMap.keySet()) {
			DeferredResult<ResponseEntity<String>> def = deferredEwallSystemResultMap
					.get(key);
			if (def.isSetOrExpired()) {
				EwallKeysToDelete.add(key);
			}
		}

		// ... and clear them
		for (long key : EwallKeysToDelete) {
			deferredEwallSystemResultMap.remove(key);
		}
	}

	/**
	 * Clear rp actuator keys.
	 */
	private void clearRPActuatorKeys() {
		// get all remote proxy actuator commands setted or expired deferred
		// results ...
		// overridden ActuatorCommand equals method
		List<ActuatorCommand> RPActuatorkeysToDelete = new ArrayList<ActuatorCommand>();

		for (ActuatorCommand actuatorCommand : deferredRemoteProxyActuatorResultMap
				.keySet()) {
			DeferredResult<LongPollMessage> def = deferredRemoteProxyResultMap
					.get(actuatorCommand);
			if (def.isSetOrExpired()) {
				RPActuatorkeysToDelete.add(actuatorCommand);
			}
		}
		// ... and clear them
		for (ActuatorCommand actuatorCommand : RPActuatorkeysToDelete) {
			deferredRemoteProxyResultMap.remove(actuatorCommand);
		}
	}

	/**
	 * Clear rp keys.
	 */
	private void clearRPKeys() {
		// get all remote proxy setted or expired deferred results ...
		List<String> RPkeysToDelete = new ArrayList<String>();

		for (String key : deferredRemoteProxyResultMap.keySet()) {
			DeferredResult<LongPollMessage> def = deferredRemoteProxyResultMap
					.get(key);
			if (def.isSetOrExpired()) {
				RPkeysToDelete.add(key);
			}
		}
		// ... and clear them
		for (String key : RPkeysToDelete) {
			deferredRemoteProxyResultMap.remove(key);
		}
	}
}