package eu.ewall.platform.lr.habits;

import java.util.ArrayList;
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
import eu.ewall.platform.idss.dao.DatabaseTableDef;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.lr.habits.service.LRHabitsService;
import eu.ewall.platform.lr.habits.model.Habits;

@Configuration
@EnableScheduling
@RestController
@Service("LRHabitsService")
public class LRHabitsSpringService implements
ApplicationListener<ContextClosedEvent>{
	
	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	private Object lock = new Object();
	private LRHabitsService service = null;
	private String habitsServiceBrickURL;
	private String profilingServerURL;
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	public LRHabitsSpringService() {
		
	}
	
	@Value("${service.habits.serviceBrickURL}")
	public void setHabitsServiceBrickURL(String url) {
		this.habitsServiceBrickURL = url;
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
		service = new LRHabitsService(mongoDbName);
		LRSpringInputProvider inputProvider = new LRSpringInputProvider(
				ewallClient, habitsServiceBrickURL, profilingServerURL);
		service.setInputProvider(inputProvider);
	}

	@Scheduled(cron="23 45 0 * * *")
//	@Scheduled(fixedRate=60000)
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(LRHabitsService.LOGTAG);
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
	
	@RequestMapping(value="/habits", method=RequestMethod.GET)
	public List<Habits> getHabits(
			@RequestParam(value="username", required=true)
			String username
			)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		
		List<Habits> ntf = new ArrayList<Habits>();
		 ntf = service.getHabits(username);
		 
			return ntf;
	
		
	}
}
