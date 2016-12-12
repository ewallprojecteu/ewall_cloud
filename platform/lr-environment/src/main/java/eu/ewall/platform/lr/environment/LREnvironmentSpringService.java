package eu.ewall.platform.lr.environment;

import org.joda.time.LocalDate;
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

import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.environment.model.IlluminanceNotification;
import eu.ewall.platform.lr.environment.service.LREnvironmentService;

@Configuration
@EnableScheduling
@RestController
@Service("LREnvironmentService")
public class LREnvironmentSpringService implements
ApplicationListener<ContextClosedEvent>{
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	private Object lock = new Object();
	private LREnvironmentService service = null;
	private String environmentServiceBrickURL;
	private String profilingServerURL;
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	public LREnvironmentSpringService() {
		
	}
	
	@Value("${service.environment.serviceBrickURL}")
	public void setEnvironmentServiceBrickURL(String url) {
		this.environmentServiceBrickURL = url;
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
		service = new LREnvironmentService(mongoDbName);
		LRSpringInputProvider inputProvider = new LRSpringInputProvider(
				ewallClient, environmentServiceBrickURL, profilingServerURL);
		service.setInputProvider(inputProvider);
	}

//	@Scheduled(cron="0 0 0 * * MON")
	@Scheduled(fixedRate=60000)
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(LREnvironmentService.LOGTAG);
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
	
	@RequestMapping(value="/illuminanceNotifications", method=RequestMethod.GET)
	public IlluminanceNotification getLastUpdate(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="type", required=true)
			String type)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		
		IlluminanceNotification ntf = service.getNotification(username, type);
		
		if(ntf.getSendDate1().isAfter(new LocalDate()) && ntf.getTimesSent() == 0) {
			//send notification	and update timesSent
			service.notificationSent(ntf);
			return ntf;
		} else if(ntf.getSendDate2().isAfter(new LocalDate()) && ntf.getTimesSent() == 1) {
			//send notification and delete notification
			service.deleteNotification(ntf);
			return ntf;
		} else {
			//send null
			return null;
		}
		
	}
}
