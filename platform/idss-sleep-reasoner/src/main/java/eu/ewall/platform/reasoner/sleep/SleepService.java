package eu.ewall.platform.reasoner.sleep;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import org.slf4j.ILoggerFactory;
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
import org.springframework.context.annotation.PropertySource;

import eu.ewall.platform.idss.dao.DatabaseException;
import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.reasoner.sleep.service.PSQIDataResponse;
import eu.ewall.platform.reasoner.sleep.service.SleepAnomalyService;

import org.springframework.data.mongodb.core.MongoOperations;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import com.mongodb.MongoClient;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;

import eu.ewall.platform.reasoner.sleep.FitBitEvent;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;

@Configuration
@EnableScheduling
@RestController
@PropertySource("classpath:/idss-sleep-${ewall.env:local}.properties")
@Service("SleepService")


public class SleepService implements
ApplicationListener<ContextClosedEvent>{

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;
	protected MongoOperations mongoOps;
	private static final Logger log = LoggerFactory.getLogger(SleepService.class);
	private Object lock = new Object();
	private SleepAnomalyService service = null;
	private String activityServiceBrickURL;
	private String functioningServiceBrickURL;
	private String profilingServerURL;
	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	@Value("${mongo.host}")
	private String host;
	
	@Value("${mongo.port}")
	private int port;
	
	@Value("${mongo.db2name}")
	private String db2name;
	
	public SleepService() {
	}
	
	@Value("${service.activity.serviceBrickURL}")
	public void setActivityServiceBrickURL(String url) {
		this.activityServiceBrickURL = url;
	}
	
	@Value("${service.functioning.serviceBrickURL}")
	public void setFunctioningServiceBrickURL(String url) {
		this.functioningServiceBrickURL = url;
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
		service = new SleepAnomalyService(mongoDbName);
		SpringInputProvider inputProvider = new SpringInputProvider(
				ewallClient, activityServiceBrickURL, functioningServiceBrickURL, profilingServerURL);
		service.setInputProvider(inputProvider);
	}

	@Scheduled(cron="10 0 0 * * *")
//	@Scheduled(fixedRate=60000)
	public void runTask() throws Exception {
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = logFactory.getLogger(SleepAnomalyService.LOGTAG);
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
	
	@RequestMapping(value="/psqi", method=RequestMethod.GET)
	public PSQIDataResponse getPSQI(
			@RequestParam(value="userid", required=true)
			String userid,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime to)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		return service.getPSQI(userid,from,to);
	}
	
	@RequestMapping(value="/sleepdata", method=RequestMethod.GET)
	public ArrayList<SleepDataResponse> getSleepData(
			@RequestParam(value="username", required=true)
			String username,
			@RequestParam(value="from", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime from,
			@RequestParam(value="to", required=true)
			@DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
			DateTime to)
			throws Exception {
		synchronized (lock) {
			if (service == null)
				initService();
		}
		ArrayList<SleepDataResponse> sleepData = new ArrayList<SleepDataResponse>();
		
		DateTime time_begin = new DateTime(from);
		DateTime time_end = new DateTime(to);
		DateTimeZone time_zone;
        time_zone = from.getZone();

		 // SleepHistorySummary sleepData = new SleepHistorySummary(username,from.toString(),to.toString(),null);
			List<FitBitEvent> slpData = new ArrayList<FitBitEvent>();
			try
			{
			 MongoClient mongo = new MongoClient(host, port);
	         mongoOps = new MongoTemplate(mongo, db2name);
		
	        String begin = from.toString();
	        String end = to.toString();
			Query query = query(where("user").is(username).andOperator(Criteria.where("date").gte(begin), Criteria.where("date").lt(end))).with(new Sort(Sort.Direction.ASC, "date"));
		
			slpData = mongoOps.find(query, FitBitEvent.class,"sleep_summaries");
			 
			       mongo.close();
			}
			catch (UnknownHostException e) {
	           
	        }
			
		
			long minutes;
			int i;
			long time;
			for(i=0;i<slpData.size();i++)
			{
			SleepDataResponse sData = new SleepDataResponse(username,null, 0, 0, 0, 0, 0, 0, new PSQIDataResponse(), 0, 0,0,0);
			sData.setUsername(username);
			sData.setTimestamp(slpData.get(i).getDate().toString());
			sData.setTotalSleepTime(slpData.get(i).getDuration()/1000/60);
			sData.setSleepEfficiency(slpData.get(i).getEfficiency());
			sData.setSleepLatency(slpData.get(i).getMinutesToFallAsleep());
			sData.setFrequencyWakingUp(slpData.get(i).getAwakeCount());
			sData.setTimeInBed(slpData.get(i).getTimeInBed());
			
			
			sData.setBedOnTime(slpData.get(i).getStartTime());
			
		
			
			minutes = slpData.get(i).getStartTime();

			
			time= slpData.get(i).getTimeInBed();
			time*=60;
			time*=1000;

			
			minutes+=time;
			sData.setBedOffTime(minutes);
			sleepData.add(sData);
			}
				
			return sleepData;
	}
	
	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (service != null)
				service.close();
		}
		
	}
}
