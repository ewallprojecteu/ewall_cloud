package eu.ewall.platform.lr.mood;



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
import eu.ewall.platform.idss.service.model.common.MoodDataResponse;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.mood.service.LRMoodService;

@Configuration
@EnableScheduling
@RestController
@Service("LRMoodService")
public class LRMoodSpringService implements
ApplicationListener<ContextClosedEvent>{

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	private Object lock = new Object();
	private LRMoodService service = null;
	private String activityServiceBrickURL;
	private String profilingServerURL;
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	public LRMoodSpringService() {
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
		service = new LRMoodService(mongoDbName);
		LRSpringInputProvider inputProvider = new LRSpringInputProvider(
				ewallClient, activityServiceBrickURL, profilingServerURL);
		service.setInputProvider(inputProvider);
	}

	@Scheduled(cron="0 15 0 * * *")
//	@Scheduled(fixedRate=60000)
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(LRMoodService.LOGTAG);
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
	
	@RequestMapping(value="/mood4period", method=RequestMethod.GET)
	public MoodDataResponse getLastUpdate(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
			LocalDate from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
			LocalDate to)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return service.getMood4Period(userid,from,to);
	}
	
	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (service != null)
				service.close();
		}
		
	}
}
