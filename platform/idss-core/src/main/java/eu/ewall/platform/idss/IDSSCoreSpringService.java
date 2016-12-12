package eu.ewall.platform.idss;

import java.util.List;

import org.joda.time.DateTime;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import eu.ewall.platform.idss.core.service.IDSSCoreConfiguration;
import eu.ewall.platform.idss.core.service.IDSSCoreService;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.service.exception.UserNotFoundException;
import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.user.UserModel;
import eu.ewall.platform.idss.utils.AppComponents;

/**
 * Spring service wrapper around {@link IDSSCoreService IDSSCoreService} for
 * eWALL.
 * 
 * @author Dennis Hofs (Roessingh Research and Development)
 * @author Harm op den Akker (Roessingh Research and Development)
 */
@Configuration
@EnableScheduling
@RestController
@Service("IDSSCoreService")
public class IDSSCoreSpringService implements
ApplicationListener<ContextClosedEvent> {
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	private Object lock = new Object();
	public IDSSCoreConfiguration config = new IDSSCoreConfiguration();
	private IDSSCoreService service = null;
	
	@Value("${profilingServer.url}")
	public void setProfilingServerURL(String url) {
		config.setProfilingServerUrl(url);
	}
	
	@Value("${idss.automaticGoalSetting.url}")
	public void setIDSSAutomaticGoalSettingUrl(String url) {
		config.setAutomaticGoalSettingUrl(url);
	}
	
	@Value("${servicebrick.physicalactivity.url}")
	public void setServiceBrickPhysicalActivityUrl(String url) {
		config.setServiceBrickPhysicalActivityUrl(url);
	}
	
	@Value("${servicebrick.weather.url}")
	public void setServiceBrickWeatherUrl(String url) {
		config.setServiceBrickWeatherUrl(url);
	}
	
	@Value("${lr.sleep.url}")
	public void setLifestyleReasonerSleepUrl(String lifestyleReasonerSleepUrl) {
		config.setLifestyleReasonerSleepUrl(lifestyleReasonerSleepUrl);
	}
	
	@Value("${userInteractionLogger.url}")
	public void setUserInteractionLoggerUrl(String userInteractionLoggerUrl) {
		config.setUserInteractionLoggerUrl(userInteractionLoggerUrl);
	}
	
	@Value("${eWallet.url}")
	public void setEWalletServiceUrl(String eWalletServiceUrl) {
		config.setEWalletServiceUrl(eWalletServiceUrl);
	}
	
	@Value("${mongo.host}")
	public void setMongoHost(String host) {
		config.setMongoDBHost(host);
	}
	
	@Value("${mongo.port}")
	public void setMongoPort(int port) {
		config.setMongoDBPort(port);
	}
	
	@Value("${mongo.dbname}")
	public void setMongoDbName(String name) {
		config.setMongoDBName(name);
	}
	
	private void initService() throws Exception {
		MongoDatabaseFactory dbFactory =
				(MongoDatabaseFactory)AppComponents.getInstance().getComponent(
				DatabaseFactory.class);
		dbFactory.setHost(config.getMongoDBHost());
		dbFactory.setPort(config.getMongoDBPort());
		service = new IDSSCoreService(config.getMongoDBName());
		IDSSCoreSpringPullInputProvider inputProvider =
				new IDSSCoreSpringPullInputProvider(ewallClient,config);
		service.setPullInputProvider(inputProvider);
	}
	
	@Scheduled(fixedRateString="${runTaskScheduleRateString:3600000}")
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(IDSSCoreService.LOGTAG);
		try {
			synchronized (lock) {
				if (service == null)
					initService();
			}
			service.runTask();
		} catch (DatabaseException ex) {
			logger.warn("Database error: {}: Trying again at next run",
					ex.getMessage());
		}
	}

	@RequestMapping(value="/usermodel", method=RequestMethod.GET)
	public UserModel getUserModel(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="updateAttrs", defaultValue="")
			List<String> updateAttrs)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.getUserModel(userid, updateAttrs);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}
	
	@RequestMapping(value="/usermodel", method=RequestMethod.POST)
	public UserModel setUserModelAttribute(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="attr", required=true)
			String attr,
			@RequestParam(value="value", required=true)
			String value,
			@RequestParam(value="sourceReliability", defaultValue="")
			Double sourceReliability,
			@RequestParam(value="updated", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime updated)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.setUserModelAttribute(userid, attr, value,
					sourceReliability, updated);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}

	@RequestMapping(value="/contextmodel", method=RequestMethod.GET)
	public ContextModel getContextModel(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="updateAttrs", defaultValue="")
			List<String> updateAttrs)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.getContextModel(userid, updateAttrs);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}
	
	@RequestMapping(value="/contextmodel", method=RequestMethod.POST)
	public ContextModel setContextModelAttribute(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="attr", required=true)
			String attr,
			@RequestParam(value="value", required=true)
			String value,
			@RequestParam(value="sourceReliability", defaultValue="")
			Double sourceReliability,
			@RequestParam(value="updated", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime updated)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.setContextModelAttribute(userid, attr, value,
					sourceReliability, updated);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}

	@RequestMapping(value="/physactstatemodel", method=RequestMethod.GET)
	public PhysicalActivityStateModel getPhysicalActivityStateModel(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="updateAttrs", defaultValue="")
			List<String> updateAttrs)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.getPhysicalActivityStateModel(userid, updateAttrs);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}
	
	@RequestMapping(value="/physactstatemodel", method=RequestMethod.POST)
	public PhysicalActivityStateModel setPhysicalActivityStateModelAttribute(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="attr", required=true)
			String attr,
			@RequestParam(value="value", required=true)
			String value,
			@RequestParam(value="sourceReliability", defaultValue="")
			Double sourceReliability,
			@RequestParam(value="updated", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime updated)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.setPhysicalActivityStateModelAttribute(userid, attr,
					value, sourceReliability, updated);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}

	@RequestMapping(value="/interactionmodel", method=RequestMethod.GET)
	public InteractionModel getInteractionModel(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="updateAttrs", defaultValue="")
			List<String> updateAttrs)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.getInteractionModel(userid, updateAttrs);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}
	
	@RequestMapping(value="/interactionmodel", method=RequestMethod.POST)
	public InteractionModel setInteractionModelAttribute(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="attr", required=true)
			String attr,
			@RequestParam(value="value", required=true)
			String value,
			@RequestParam(value="sourceReliability", defaultValue="")
			Double sourceReliability,
			@RequestParam(value="updated", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime updated)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		try {
			return service.setInteractionModelAttribute(userid, attr, value,
					sourceReliability, updated);
		} catch (UserNotFoundException ex) {
			throw new HttpNotFoundException(ex.getMessage(), ex);
		}
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (service != null)
				service.close();
		}
	}
}
