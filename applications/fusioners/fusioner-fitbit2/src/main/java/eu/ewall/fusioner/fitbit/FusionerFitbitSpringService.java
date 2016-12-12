package eu.ewall.fusioner.fitbit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.idss.dao.DatabaseFactory;
import eu.ewall.platform.idss.dao.mongodb.MongoDatabaseFactory;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;

@Configuration
@EnableScheduling
@RestController
@Service("FusionerFitbitService")
public class FusionerFitbitSpringService implements
ApplicationListener<ContextClosedEvent> {

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	private String serviceBaseUrl;

	private String profilingServerURL;

	private String fitbitClientId;
	private String fitbitSecret;

	private String mongoHost;
	private int mongoPort;
	private String mongoDbName;
	
	private final Object lock = new Object();
	private boolean closed = false;
	private FusionerFitbitService service = null;
	
	@Value("${service.baseUrl}")
	public void setServiceBaseURL(String url) {
		this.serviceBaseUrl = url;
	}
	
	@Value("${profilingServer.url}")
	public void setProfilingServerURL(String url) {
		this.profilingServerURL = url;
	}
	
	@Value("${fusioner.fitbit.clientId}")
	public void setFitbitClientId(String fitbitClientId) {
		this.fitbitClientId = fitbitClientId;
	}
	
	@Value("${fusioner.fitbit.secret}")
	public void setFitbitSecret(String fitbitSecret) {
		this.fitbitSecret = fitbitSecret;
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
	
	@RequestMapping(value="/authorize", method=RequestMethod.GET)
	public String authorize(
			@RequestParam(value="user", required=true)
			String user) throws Exception {
		FusionerFitbitService service = initService();
		if (service == null)
			throw new Exception("Service closed");
		return service.authorize(user, getUsers());
	}
	
	@RequestMapping(value="/authorize_callback", method=RequestMethod.GET)
	public String authorizeCallback(
			@RequestParam(value="state", required=false)
			String state,
			@RequestParam(value="code", required=false)
			String code,
			@RequestParam(value="error", required=false)
			String error,
			@RequestParam(value="error_description", required=false)
			String errorDescription) throws Exception {
		FusionerFitbitService service = initService();
		if (service == null)
			throw new Exception("Service closed");
		return service.authorizeCallback(state, code, error, errorDescription);
	}
	
	@Scheduled(fixedRate=300000)
	public void runTask() throws Exception {
		FusionerFitbitService service = initService();
		if (service == null)
			return;
		service.runTask(getUsers());
	}

	private List<IDSSUserProfile> getUsers() throws IOException,
			RemoteMethodException, Exception {
		String url = profilingServerURL + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		List<IDSSUserProfile> users = new ArrayList<IDSSUserProfile>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			Map<?,?> user = mapper.convertValue(obj, Map.class);
			users.add(CommonsUserReader.readUser(user));
		}
		return users;
	}

	@Override
	public void onApplicationEvent(ContextClosedEvent ev) {
		synchronized (lock) {
			if (closed)
				return;
			closed = true;
			if (service != null)
				service.close();
		}
	}
	
	private FusionerFitbitService initService() {
		synchronized (lock) {
			if (closed || service != null)
				return service;
			MongoDatabaseFactory dbFactory =
					(MongoDatabaseFactory)AppComponents.getInstance()
					.getComponent(DatabaseFactory.class);
			dbFactory.setHost(mongoHost);
			dbFactory.setPort(mongoPort);
			String fitbitCallbackUrl = serviceBaseUrl + "/authorize_callback";
			service = new FusionerFitbitService(mongoDbName, fitbitClientId,
					fitbitSecret, fitbitCallbackUrl);
			return service;
		}
	}
}
