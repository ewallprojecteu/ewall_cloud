package eu.ewall.platform.idss.automaticgoalsetting;

import java.net.URL;
import java.util.List;

import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import eu.ewall.platform.idss.automaticgoalsetting.service.IDSSAutomaticGoalSettingService;
import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.service.model.common.ActivityGoal;
import eu.ewall.platform.idss.service.model.common.ActivityGoalResponse;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.utils.AppComponents;

/**
 * Spring service wrapper around {@link IDSSAutomaticGoalSettingService
 * IDSSAutomaticGoalSettingService} for eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
@Configuration
@EnableScheduling
@RestController
@Service("IDSSAutomaticGoalSettingService")
public class IDSSAutomaticGoalSettingSpringService implements
ApplicationListener<ContextClosedEvent> {
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	
	private Object lock = new Object();
	private IDSSAutomaticGoalSettingService service = null;
	private String lrPhysicalActivityURL;
	private String lrSleepURL;
	private String profilingServerURL;
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	@Value("${service.lr.physicalactivity.url}")
	public void setLRPhysicalActivityURL(String url) {
		this.lrPhysicalActivityURL = url;
	}
	
	@Value("${service.lr.sleep.url}")
	public void setLRSLeepURL(String url) {
		this.lrSleepURL = url;
	}
	
	@Value("${profilingServer.url}")
	public void setProfilingServerURL(String url) {
		this.profilingServerURL = url;
	}
	
	@Value("${mongo.host}")
	public void setMongoHost(String host) {
		this.mongoHost = host;
	}
	
	@Value("${mongo.port}")
	public void setMongoPort(int port) {
		this.mongoPort = port;
	}
	
	@Value("${mongo.dbname}")
	public void setMongoDbName(String name) {
		this.mongoDbName = name;
	}
	
	@RequestMapping(value="/activitygoal", method=RequestMethod.GET)
	public ActivityGoalResponse getActivityGoal(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="date", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date)
			throws Exception {
		return getActivityGoal(user, ActivityMeasure.STEPS, date);
	}
	
	@RequestMapping(value="/activitygoal_steps", method=RequestMethod.GET)
	public ActivityGoalResponse getActivityGoalSteps(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="date", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date)
			throws Exception {
		return getActivityGoal(user, ActivityMeasure.STEPS, date);
	}
	
	@RequestMapping(value="/activitygoal_calories", method=RequestMethod.GET)
	public ActivityGoalResponse getActivityGoalCalories(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="date", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date)
			throws Exception {
		return getActivityGoal(user, ActivityMeasure.CALORIES, date);
	}
	
	@RequestMapping(value="/activitygoal_distance", method=RequestMethod.GET)
	public ActivityGoalResponse getActivityGoalDistance(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="date", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate date)
			throws Exception {
		return getActivityGoal(user, ActivityMeasure.DISTANCE, date);
	}
	
	private ActivityGoalResponse getActivityGoal(String user,
			ActivityMeasure measure, LocalDate date) throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return new ActivityGoalResponse(service.getActivityGoal(user, measure,
				date));
	}
	
	@RequestMapping(value="/activitygoals", method=RequestMethod.GET)
	public List<ActivityGoal> getActivityGoals(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to)
			throws Exception {
		return getActivityGoals(user, ActivityMeasure.STEPS, from, to);
	}
	
	@RequestMapping(value="/activitygoals_steps", method=RequestMethod.GET)
	public List<ActivityGoal> getActivityGoalsSteps(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to)
			throws Exception {
		return getActivityGoals(user, ActivityMeasure.STEPS, from, to);
	}

	@RequestMapping(value="/activitygoals_calories", method=RequestMethod.GET)
	public List<ActivityGoal> getActivityGoalsCalories(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to)
			throws Exception {
		return getActivityGoals(user, ActivityMeasure.CALORIES, from, to);
	}

	@RequestMapping(value="/activitygoals_distance", method=RequestMethod.GET)
	public List<ActivityGoal> getActivityGoalsDistance(
			@RequestParam(value="userid", required=true) String user,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate to)
			throws Exception {
		return getActivityGoals(user, ActivityMeasure.DISTANCE, from, to);
	}
	
	private List<ActivityGoal> getActivityGoals(String user,
			ActivityMeasure measure, LocalDate from, LocalDate to)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return service.getActivityGoals(user, measure, from, to);
	}

	private void initService() throws Exception {
		MongoDatabaseFactory dbFactory =
				(MongoDatabaseFactory)AppComponents.getInstance().getComponent(
				DatabaseFactory.class);
		dbFactory.setHost(mongoHost);
		dbFactory.setPort(mongoPort);
		service = new IDSSAutomaticGoalSettingService(mongoDbName);
		URL configUrl = getClass().getResource("config.xml");
		if (configUrl == null)
			throw new RuntimeException("Resource \"config.xml\" not found");
		eu.ewall.platform.idss.automaticgoalsetting.service.Configuration config =
				eu.ewall.platform.idss.automaticgoalsetting.service.Configuration
				.parse(configUrl);
		service.setConfig(config);
		AGSSpringPullInputProvider inputProvider =
				new AGSSpringPullInputProvider(ewallClient,
				lrPhysicalActivityURL, lrSleepURL, profilingServerURL);
		service.setPullInputProvider(inputProvider);
	}
	
	@Scheduled(fixedRateString="${runTaskScheduleRateString:300000}")
	public void runTask() throws Exception {
		Logger logger = LoggerFactory.getLogger(
				IDSSAutomaticGoalSettingService.class);
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

	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (service != null)
				service.close();
		}
	}
}
