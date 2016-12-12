package eu.ewall.platform.login;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import eu.ewall.common.authentication.jwt.TokenUtils;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.commons.datamodel.service.Application;
import eu.ewall.platform.login.response.LoginResponse;

@RestController
public class LoginController {

	private static final Logger log = LoggerFactory
			.getLogger(LoginController.class);

	public static final String GET_USER_PROFILE = "/users/";
	public static final String GET_SERVICES_FOR_APP = "/applications/";
	public static final String AUTHENTICATE = "/users/%s/authenticate/";
	public static final String POST_HEARTBEAT = "/users/%s/heartbeat/";

	@Value("${profilingServer.url}")
	private String profilingServerUrl;

	@Value("${ewall.authentication.enabled}")
	private String ewallAuthenticationEnabled;

	@Value("${service.name}")
	private String serviceName;

	@Value("${secret}")
	private String secret;

	@Value("${token.duration.primaryuser}")
	private String tokenDurationPrimaryUserStr;

	@Value("${token.duration.default}")
	private String tokenDurationDefaultStr;

	@Autowired
	@Qualifier("ewall")
	private RestOperations ewallClient;

	@RequestMapping(value = "/v1/users/authenticate/", method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> authenticate(HttpServletRequest request, 
			@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam(value="tokenUpdateFrequencySecs", required=false) Integer tokenUpdateFrequencySecs) {
		try {
			log.info("Received 'users/authenticate' request");
			boolean isAuthenticated = authenticated(username, password);

			if (username.isEmpty() || password.isEmpty()) {
				log.info("Received bad parameters");
				return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
			}

			if (!isAuthenticated) {
				return new ResponseEntity<LoginResponse>(
						HttpStatus.UNAUTHORIZED);
			}

			User user = getUser(username);
			ArrayList<String> applications = new ArrayList<String>();
			ArrayList<String> services = new ArrayList<String>();
			ArrayList<String> roles = new ArrayList<String>();
			roles.add(user.getUserRole().name());
			for (String appName : user.getApplicationNamesList()) {
				applications.add(appName);
				List<String> appServices = getServicesByAppName(appName);
				for (String service : appServices) {
					services.add(service);
				}
			}
			int tokenDurationMins = 2;
			try {
				if (user.getUserRole().equals(UserRole.PRIMARY_USER)) {
					tokenDurationMins = Integer
							.parseInt(tokenDurationPrimaryUserStr);
				} else {
					tokenDurationMins = Integer
							.parseInt(tokenDurationDefaultStr);
				}
			} catch (Exception e) {
				log.error(
						"Error in parsing tokenDuration config parameters. Setting default to "
								+ tokenDurationMins + " minutes", e);
			}

			String ip = getUSerIp(request);
			if(tokenUpdateFrequencySecs!=null && tokenUpdateFrequencySecs.intValue()>0){
				boolean result = postHeartbeat(username, ip, tokenUpdateFrequencySecs);
				if(!result) {
					log.error("Error in posting the heartbeat for user " + username);
				}
			}

			LoginResponse response = new LoginResponse(TokenUtils.createToken(
					user.getUsername(), roles, applications, services,
					tokenDurationMins, secret));
			log.info("Returning valid token");
			return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception", e);
			return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/v1/users/authenticateAdmin/", method = RequestMethod.POST)
	public ResponseEntity<LoginResponse> authenticateAdmin(HttpServletRequest request, 
			@RequestParam("username") String username, @RequestParam("password") String password, @RequestParam(value="tokenUpdateFrequencySecs", required=false) Integer tokenUpdateFrequencySecs) {
		try {
			log.info("Received 'users/authenticateAdmin' request");
			boolean isAuthenticated = authenticated(username, password);

			if (username.isEmpty() || password.isEmpty()) {
				log.info("Received bad parameters");
				return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
			}

			if (!isAuthenticated) {
				return new ResponseEntity<LoginResponse>(
						HttpStatus.UNAUTHORIZED);
			}

			User user = getUser(username);

			if (user.getUserRole() != UserRole.ADMINISTRATOR && user.getUserRole() != UserRole.REGION_ADMINISTRATOR) {
				return new ResponseEntity<LoginResponse>(
						HttpStatus.UNAUTHORIZED);
			}

			ArrayList<String> applications = new ArrayList<String>();
			ArrayList<String> services = new ArrayList<String>();
			ArrayList<String> roles = new ArrayList<String>();
			roles.add(user.getUserRole().name());
			for (String appName : user.getApplicationNamesList()) {
				applications.add(appName);
				List<String> appServices = getServicesByAppName(appName);
				for (String service : appServices) {
					services.add(service);
				}
			}
			int tokenDurationMins = 2;
			try {
				tokenDurationMins = Integer.parseInt(tokenDurationDefaultStr);
			} catch (Exception e) {
				log.error(
						"Error in parsing tokenDuration config parameters. Setting default to "
								+ tokenDurationMins + " minutes", e);
			}
			
			String ip = getUSerIp(request);
			if(tokenUpdateFrequencySecs!=null && tokenUpdateFrequencySecs.intValue()>0){
				boolean result = postHeartbeat(username, ip, tokenUpdateFrequencySecs);
				if(!result) {
					log.error("Error in posting the heartbeat for user " + username);
				}
			}

			LoginResponse response = new LoginResponse(TokenUtils.createToken(
					user.getUsername(), roles, applications, services,
					tokenDurationMins, secret));
			log.info("Returning valid token");
			return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception", e);
			return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/v1/users/renew/", method = RequestMethod.GET)
	public ResponseEntity<LoginResponse> renewToken(HttpServletRequest request, @RequestParam(value="tokenUpdateFrequencySecs", required=false) Integer tokenUpdateFrequencySecs) {
		try {
			log.info("Received 'users/renew' request");
			String token = request.getHeader("X-Auth-Token");
			if (token.isEmpty()) {
				log.info("Received empty token");
				return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
			}
			if (!TokenUtils.verifyToken(token, serviceName, secret)) {
				return new ResponseEntity<LoginResponse>(
						HttpStatus.UNAUTHORIZED);
			}
			String username = TokenUtils.getUserNameFromToken(token, secret);
			User user = getUser(username);
			if (user == null) {
				log.info("User specified in token not found: " + username);
				return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
			}
			ArrayList<String> applications = new ArrayList<String>();
			ArrayList<String> services = new ArrayList<String>();
			ArrayList<String> roles = new ArrayList<String>();
			roles.add(user.getUserRole().name());
			for (String appName : user.getApplicationNamesList()) {
				applications.add(appName);
				List<String> appServices = getServicesByAppName(appName);
				for (String service : appServices) {
					services.add(service);
				}
			}
			int tokenDurationMins = 2;
			try {
				if (user.getUserRole().equals(UserRole.PRIMARY_USER)) {
					tokenDurationMins = Integer
							.parseInt(tokenDurationPrimaryUserStr);
				} else {
					tokenDurationMins = Integer
							.parseInt(tokenDurationDefaultStr);
				}
			} catch (Exception e) {
				log.error(
						"Error in parsing tokenDuration config parameters. Setting default to "
								+ tokenDurationMins + " minutes", e);
			}

			String ip = getUSerIp(request);
			if(tokenUpdateFrequencySecs!=null && tokenUpdateFrequencySecs.intValue()>0){
				boolean result = postHeartbeat(username, ip, tokenUpdateFrequencySecs);
				if(!result) {
					log.error("Error in posting the heartbeat for user " + username);
				}
			}
			
			LoginResponse response = new LoginResponse(TokenUtils.createToken(
					user.getUsername(), roles, applications, services,
					tokenDurationMins, secret));
			return new ResponseEntity<LoginResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			log.error("Exception", e);
			return new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
		}
	}

	private String getUSerIp(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		if (ipAddress == null) {
			ipAddress = request.getRemoteAddr();
		}
		return ipAddress;
	}

	public User getUser(String username) {
		try {
			log.info("Getting profile for user " + username);
			User user = null;
			try {
				log.info("URL: " + profilingServerUrl + GET_USER_PROFILE
						+ username);
				user = ewallClient.getForObject(profilingServerUrl
						+ GET_USER_PROFILE + username, User.class);
			} catch (RestClientException e) {
				log.error("RestClientException", e);
				return null;
			}

			return user;
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return null;

	}

	public boolean postHeartbeat(String username, String ip, int tokenUpdateFrequencySecs) {
		try {
			log.info("Posting heartbeat for user " + username);
			log.info("URL: " + profilingServerUrl
					+ String.format(POST_HEARTBEAT, username));
			

			ResponseEntity res = ewallClient.postForEntity(profilingServerUrl+ String.format(POST_HEARTBEAT, username)+"?ip="+ip+"&updateFrequencySecs="+String.valueOf(tokenUpdateFrequencySecs), null, ResponseEntity.class);
			log.info("Response: " + res.getStatusCode());
			if ((res.getStatusCode() == HttpStatus.OK) || (res.getStatusCode() == HttpStatus.NO_CONTENT)) {
				return true;
			}
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
		return false;
	}

	public List<String> getServicesByAppName(String appName) {
		Application application = new Application();
		try {
			log.info("Getting services for app " + appName);
			try {
				log.info("URL: " + profilingServerUrl + GET_SERVICES_FOR_APP
						+ appName);
				application = ewallClient.getForObject(profilingServerUrl
						+ GET_SERVICES_FOR_APP + appName, Application.class);
			} catch (RestClientException e) {
				log.error("RestClientException", e);
				return null;
			}
		} catch (Exception e) {
			log.error("Exception", e);
		}
		return application.getEncompassingServiceNames();

	}

	public boolean authenticated(String username, String password) {
		try {
			if (!Boolean.parseBoolean(ewallAuthenticationEnabled)) {
				log.info("Skipping ewall password check (disabled by configuration)");
				return true;
			}
			log.info("Verifying credentials for user " + username);
			log.info("URL: " + profilingServerUrl
					+ String.format(AUTHENTICATE, username));
			ResponseEntity res = ewallClient.postForEntity(profilingServerUrl
					+ String.format(AUTHENTICATE, username), password,
					ResponseEntity.class);
			log.info("Response: " + res.getStatusCode());
			if ((res.getStatusCode() == HttpStatus.OK)
					|| (res.getStatusCode() == HttpStatus.NO_CONTENT)) {
				return true;
			}
		} catch (Exception e) {
			log.error("Exception: ", e);
		}
		return false;
	}

}
