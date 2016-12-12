package eu.ewall.platform.lr.physicalactivity;

import java.util.List;

import org.joda.time.LocalDate;
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

import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;
import eu.ewall.platform.idss.service.model.common.DataUpdatedResponse;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.physicalactivity.service.LRPhysicalActivityService;

/**
 * Spring service wrapper around {@link LRPhysicalActivityService
 * LRPhysicalActivityService} for eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
@Configuration
@EnableScheduling
@RestController
@Service("LRPhysicalActivityService")
public class LRPhysicalActivitySpringService implements
ApplicationListener<ContextClosedEvent> {
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	private Object lock = new Object();
	private LRPhysicalActivityService service = null;
	private String activityServiceBrickURL;
	private String profilingServerURL;
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	public LRPhysicalActivitySpringService() {
	}
	
	@Value("${service.activity.serviceBrickURL}")
	public void setActivityServiceBrickURL(String url) {
		this.activityServiceBrickURL = url;
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
	
	private void initService() throws Exception {
		MongoDatabaseFactory dbFactory =
				(MongoDatabaseFactory)AppComponents.getInstance().getComponent(
				DatabaseFactory.class);
		dbFactory.setHost(mongoHost);
		dbFactory.setPort(mongoPort);
		service = new LRPhysicalActivityService(mongoDbName);
		LRSpringPullInputProvider inputProvider =
				new LRSpringPullInputProvider(ewallClient,
				activityServiceBrickURL, profilingServerURL);
		service.setPullInputProvider(inputProvider);
	}
	
	@Scheduled(fixedRate=300000)
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(LRPhysicalActivityService.LOGTAG);
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
	
	@RequestMapping(value="/lastupdate", method=RequestMethod.GET)
	public DataUpdatedResponse getLastUpdate(
			@RequestParam(value="userid", required=true)
			String userid)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return new DataUpdatedResponse(service.getLastUpdate(userid));
	}

	@RequestMapping(value="/activityweekpattern",
			method=RequestMethod.GET)
	public List<AverageActivity> getActivityWeekPattern(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="date", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
			LocalDate date)
			throws Exception {
		return getActivityWeekPatternSteps(userid, date);
	}
	
	@RequestMapping(value="/activityweekpattern_steps",
			method=RequestMethod.GET)
	public List<AverageActivity> getActivityWeekPatternSteps(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="date", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
			LocalDate date)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return service.getActivityWeekPattern(ActivityMeasure.STEPS, userid,
				date);
	}
	
	@RequestMapping(value="/activityweekpattern_calories",
			method=RequestMethod.GET)
	public List<AverageActivity> getActivityWeekPatternCalories(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="date", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
			LocalDate date)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return service.getActivityWeekPattern(ActivityMeasure.CALORIES, userid,
				date);
	}
	
	@RequestMapping(value="/activityweekpattern_distance",
			method=RequestMethod.GET)
	public List<AverageActivity> getActivityWeekPatternDistance(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="date", defaultValue="")
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
			LocalDate date)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return service.getActivityWeekPattern(ActivityMeasure.DISTANCE, userid,
				date);
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (service != null)
				service.close();
		}
	}
}
