package eu.ewall.platform.reasoner.activitycoach;

import java.util.List;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.HttpNotFoundException;
import eu.ewall.platform.idss.SpringStateModelAccessPoint;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.service.model.state.StateModelAccessPoint;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.activitycoach.service.PullInputProvider;
import eu.ewall.platform.reasoner.activitycoach.service.VCConfiguration;
import eu.ewall.platform.reasoner.activitycoach.service.VCService;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessage;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageAvailable;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageContent;
import eu.ewall.platform.reasoner.activitycoach.service.protocol.VCMessageError;

@Configuration
@EnableScheduling
@RestController
@Service("IDSSActivityCoachService")
public class ActivityCoachSpringService implements
ApplicationListener<ContextClosedEvent> {
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	private static final String LOGTAG = "ActivityCoachSpringService";
	public VCConfiguration config = new VCConfiguration();
	private VCService virtualCoachService = null;
	private Object lock = new Object();
	private Logger logger = AppComponents.getLogger(LOGTAG);
	
	@Value("${service.baseUrl}")
	public void setBaseUrl(String serviceBaseUrl) {
		config.setServiceBaseUrl(serviceBaseUrl);
	}
	
	@Value("${profilingServerUrl}")
	public void setProfilingServerUrl(String profilingServerUrl) {
		config.setProfilingServerUrl(profilingServerUrl);
	}
	
	@Value("${idssCoreUrl}")
	public void setIDSSCoreUrl(String idssCoreUrl) {
		config.setIDSSCoreUrl(idssCoreUrl);
	}
	
	@Value("${eWalletUrl}")
	public void setEWalletUrl(String eWalletUrl) {
		config.setEWalletUrl(eWalletUrl);
	}

	@Value("${mongoDBHost}")
	public void setMongoDBHost(String mongoDBHost) {
		config.setMongoDBHost(mongoDBHost);
	}
	
	@Value("${mongoDBPort}")
	public void setMongoPort(int mongoDBPort) {
		config.setMongoDBPort(mongoDBPort);
	}
	
	@Value("${mongoDBName}")
	public void setMongoDBName(String mongoDBName) {
		config.setMongoDBName(mongoDBName);
	}
	
	@Value("${smartTimingEnabled}")
	public void enableSmartTiming(String smartTimingEnabled) {
		config.setSmartTimingEnabled(new Boolean(smartTimingEnabled).booleanValue());
	}
	
	@Value("${pammExpirationMinutes}")
	public void setPAMMExpirationMinutes(int pammExpirationMinutes) {
		config.setPAMMExpirationMinutes(pammExpirationMinutes);
	}
	
	@Value("${dialogueExpirationMinutes}")
	public void setDialogueExpirationMinutes(int dialogueExpirationMinutes) {
		config.setDialogueExpirationMinutes(dialogueExpirationMinutes);
	}
	
	@Value("${defaultDayStartTime}")
	public void setDefaultDayStartTime(String defaultDayStartTime) {
		config.setDefaultDayStartTime(defaultDayStartTime);
	}
	
	@Value("${defaultDayEndTime}")
	public void setDefaultDayEndTime(String defaultDayEndTime) {
		config.setDefaultDayEndTime(defaultDayEndTime);
	}
	
	@Value("${fixedTimingPAMMIntervalMinutes}")
	public void setFixedTimingPAMMIntervalMinutes(int fixedTimingPAMMIntervalMinutes) {
		config.setFixedTimingPAMMIntervalMinutes(fixedTimingPAMMIntervalMinutes);
	}
	
	@Value("${allowedGoalDeviation}")
	public void setAllowedGoalDeviation(double allowedGoalDeviation) {
		config.setAllowedGoalDeviation(allowedGoalDeviation);
	}
	
	@Value("${extremeGoalDeviation}")
	public void setExtremeGoalDeviation(double extremeGoalDeviation) {
		config.setExtremeGoalDeviation(extremeGoalDeviation);
	}
	
	@Value("${interactionReminderMinimumAllowedMinutes}")
	public void setInteractionReminderMinimumAllowedMinutes(int interactionReminderMinimumAllowedMinutes) {
		config.setInteractionReminderMinimumAllowedMinutes(interactionReminderMinimumAllowedMinutes);
	}
	
	@Value("${interactionReminderMaximumAllowedMinutes}")
	public void setInteractionReminderMaximumAllowedMinutes(int interactionReminderMaximumAllowedMinutes) {
		config.setInteractionReminderMaximumAllowedMinutes(interactionReminderMaximumAllowedMinutes);
	}
	
	@Value("${interactionReminderCoolDownFactor}")
	public void setInteractionReminderCoolDownFactor(double interactionReminderCoolDownFactor) {
		config.setInteractionReminderCoolDownFactor(interactionReminderCoolDownFactor);
	}
	
	@Value("${attributeUpdateTime}")
	public void setAttributeUpdateTime(int attributeUpdateTime) {
		config.setAttributeUpdateTime(attributeUpdateTime);
	}
	
	@Value("${maximumAllowedPollingFrequency}")
	public void setMaximumAllowedPollingFrequency(long maximumAllowedPollingFrequency) {
		config.setMaximumAllowedPollingFrequency(maximumAllowedPollingFrequency);
	}

	/**
	 * Initializes the {@link VCService} upon first start, or after
	 * a server reset.
	 * @throws Exception
	 */
	private void initService() throws Exception {
		MongoDatabaseFactory dbFactory =
				(MongoDatabaseFactory)AppComponents.getInstance().getComponent(
				DatabaseFactory.class);
		dbFactory.setHost(config.getMongoDBHost());
		dbFactory.setPort(config.getMongoDBPort());
		virtualCoachService = new VCService(config, ewallClient);
		StateModelAccessPoint stateModelAP = new SpringStateModelAccessPoint(
				ewallClient,config.getIDSSCoreUrl());
		virtualCoachService.setStateModelAccessPoint(stateModelAP);
		PullInputProvider inputProvider =
				new ActivityCoachSpringPullInputProvider(ewallClient,config.getProfilingServerUrl());
		virtualCoachService.setPullInputProvider(inputProvider);
	}

	@Scheduled(fixedRateString="${runTaskScheduleRateString:300000}")
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(VCService.LOGTAG);
		try {
			synchronized (lock) {
				if (virtualCoachService == null)
					initService();
			}
			virtualCoachService.runTask();
		} catch (DatabaseException ex) {
			logger.warn("Database error: {}: Trying again at next run",ex.getMessage());
		}
	}
	
	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (virtualCoachService != null)
				virtualCoachService.close();
		}
	}
	
	// ----------
	// ---------- REST END-POINTS ----------
	// ----------
	
	/**
	 * Called by User Interface element to check if the Virtual Coach has something to say.
	 * @param username the username for which to check if a communication is available.
	 * @return a {@link VCMessageAvailable} object if a communication is available, or {@code null} 
	 * (an empty response) if the Virtual Coach has nothing to say.
	 * @throws Exception in case of any network or internal error in the communication 
	 * generation process.
	 */
	@RequestMapping(value="/poll", method=RequestMethod.GET)
	public List<VCMessage> poll(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		
		// Make sure the Virtual Coach service is initialized (after e.g. server-restart).
		synchronized (lock) {
			if (virtualCoachService == null) {
				initService();
			}
		}
		
		// Returns the list of available communications, or null.
		return virtualCoachService.poll(username);
	}
	
	/**
	 * Called by User Interface element to 'complete' a previously generated physical activity
	 * motivational message (pamm) notification for the given user
	 * @param username the username for which to complete its pamm.
	 * @return a {@link VCMessageContent} object containing the actual content of the pamm that was generated.
	 * @throws Exception in case of any network or internal error in the communication
	 * or message generation process.
	 */
	@RequestMapping(value="/generatePAMM", method=RequestMethod.GET)
	public VCMessageContent generatePAMM(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		
		// Make sure the Virtual Coach service is initialized (after e.g. server-restart).
		synchronized (lock) {
			if (virtualCoachService == null) {
				initService();
			}
		}
		
		return virtualCoachService.generatePAMM(username);
	}
	
	/**
	 * Called by User Interface element to indicate that the given message for the given
	 * user has been observed by the user.
	 * @param username the username for which to indicate pamm observed.
	 * @param messageID the unique identifier of the pamm.
	 * @throws HttpNotFoundException if the given user is unknown in the system.
	 * @throws Exception in case of any other internal error.
	 */
	@RequestMapping(value="/observedPAMM", method=RequestMethod.GET)
	public VCMessage observedPAMM(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="id", required=true)
			String messageID) {
		
		// Make sure the Virtual Coach service is initialized (after e.g. server-restart).
		synchronized (lock) {
			if (virtualCoachService == null) {
				try {
					initService();
				} catch (Exception e) {
					logger.error("Failed to initialize Virtual Coach Service.",e);
					return new VCMessageError("Error initializing the Virtual Coach service.");
				}
			}
		}

		return virtualCoachService.observedPAMM(username,messageID);
	}
	
	/**
	 * Called by User Interface element to start a dialogue for the given {@code username}
	 * of the given {@code dialogueTypeId}.
	 * @param username the username for which to start the dialogue.
	 * @param dialogueTypeId the identifier of the type of dialogue to start.
	 * @return a {@code VCMessage} containing either the first step of the dialogue or an error message.
	 */
	@RequestMapping(value="/startDialogue", method=RequestMethod.GET)
	public VCMessage startDialogue(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="dialogueTypeId", required=true)
			String dialogueTypeId) {
		
		// Make sure the Virtual Coach service is initialized (after e.g. server-restart).
		synchronized (lock) {
			if (virtualCoachService == null) {
				try {
					initService();
				} catch (Exception e) {
					logger.error("Failed to initialize Virtual Coach Service.",e);
					return new VCMessageError("Error initializing the Virtual Coach service.");
				}
			}
		}
		
		return virtualCoachService.startDialogue(username,dialogueTypeId);	
	}
	
	@RequestMapping(value="/progressDialogue", method=RequestMethod.GET)
	public VCMessage progressDialogue(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="replyId", required=true)
			String replyId) {
		
		// Make sure the Virtual Coach service is initialized (after e.g. server-restart).
		synchronized (lock) {
			if (virtualCoachService == null) {
				try {
					initService();
				} catch (Exception e) {
					logger.error("Failed to initialize Virtual Coach Service.",e);
					return new VCMessageError("Error initializing the Virtual Coach service.");
				}
			}
		}
				
		return virtualCoachService.progressDialogue(username,replyId);	
	}
	
	// ----------
	// ---------- TESTING REST END-POINTS ----------
	// ----------
	
	@RequestMapping(value="/resetUser", method=RequestMethod.GET)
	public VCMessage resetUser(
			@RequestParam(value="username", required=true)
			String username) throws Exception {
		
		// Make sure the Virtual Coach service is initialized (after e.g. server-restart).
		synchronized (lock) {
			if (virtualCoachService == null) {
				initService();
			}
		}
				
		// Returns the first type of available communication, or null.
		return virtualCoachService.resetUser(username);
	}
}
