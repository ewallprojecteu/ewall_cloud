package eu.ewall.platform.idss.automaticgoalsetting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.CommonsUserReader;
import eu.ewall.platform.idss.automaticgoalsetting.service.PullInputProvider;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.ActivityMeasure;
import eu.ewall.platform.idss.service.model.common.AverageActivity;
import eu.ewall.platform.idss.service.model.common.DataUpdated;
import eu.ewall.platform.idss.service.model.common.DataUpdatedResponse;
import eu.ewall.platform.idss.service.model.common.LRSleepWeekPattern;
import eu.ewall.platform.idss.service.model.common.SleepGoalMeasure;

/**
 * Implementation of {@link PullInputProvider PullInputProvider} for eWall.
 * 
 * @author Dennis Hofs (RRD)
 */
public class AGSSpringPullInputProvider implements PullInputProvider {
	private static final int WEEK_START = 1; // Monday

	private static final int ULTIMATE_GOAL_STEPS = 10000;
	private static final int ULTIMATE_GOAL_CALORIES = 2500;
	private static final int ULTIMATE_GOAL_DISTANCE = 8000;

	private static final int ULTIMATE_GOAL_MINIMUM_MINUTES_SLEPT = 720; // 12 hours
	private static final int ULTIMATE_GOAL_MAXIMUM_MINUTES_SLEPT = 360; // 6 hours
	private static final int ULTIMATE_GOAL_SLEEP_EFFICIENCY = 60;
	private static final int ULTIMATE_GOAL_GOING_TO_SLEEP_TIME = 1350; // 22:30
	private static final int ULTIMATE_GOAL_WAKING_UP_TIME = 480; // 8:00
	
	private RestOperations ewallClient;
	private String lrPhysicalActivityURL;
	private String lrSleepURL;
	private String profilingServerURL;

	/**
	 * Constructs a new instance.
	 * 
	 * @param ewallClient the eWall client
	 * @param lrPhysicalActivityURL base URL to lr-physical-activity, without
	 * trailing slash
	 * @param lrSleepURL base URL to lr-sleep-monitor, without trailing slash
	 * @param profilingServerURL base URL to profiling-server, without trailing
	 * slash
	 */
	public AGSSpringPullInputProvider(RestOperations ewallClient,
			String lrPhysicalActivityURL, String lrSleepURL,
			String profilingServerURL) {
		this.ewallClient = ewallClient;
		this.lrPhysicalActivityURL = lrPhysicalActivityURL;
		this.lrSleepURL = lrSleepURL;
		this.profilingServerURL = profilingServerURL;
	}

	@Override
	public List<IDSSUserProfile> getUsers() throws IOException, RemoteMethodException,
			Exception {
		String url = profilingServerURL + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		List<IDSSUserProfile> users = new ArrayList<IDSSUserProfile>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			User user = mapper.convertValue(obj, User.class);
			if(user.getUserRole() == UserRole.PRIMARY_USER) {
				users.add(CommonsUserReader.readUser(user));
			}
		}
		return users;
	}

	@Override
	public int getWeekStart(String user) throws IOException,
			RemoteMethodException, Exception {
		return WEEK_START;
	}

	@Override
	public LocalDate getLastActivityWeekPatternUpdate(String user)
			throws IOException, RemoteMethodException, Exception {
		String url = lrPhysicalActivityURL + "/lastupdate?userid={userid}";
		DataUpdatedResponse resp = ewallClient.getForObject(url,
				DataUpdatedResponse.class, user);
		DataUpdated dataUpdated = resp.getValue();
		if (dataUpdated == null)
			return null;
		else
			return dataUpdated.getDate();
	}

	@Override
	public List<AverageActivity> getActivityWeekPattern(String user,
			ActivityMeasure measure, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		String url = lrPhysicalActivityURL + "/activityweekpattern_" +
			measure.toString().toLowerCase() + "?userid={userid}";
		if (date != null)
			url += "&date={date}";
		List<Object> vars = new ArrayList<Object>();
		vars.add(user);
		if (date != null)
			vars.add(date.toString("yyyy-MM-dd"));
		List<?> objList = ewallClient.getForObject(url, List.class,
				vars.toArray());
		List<AverageActivity> weekPattern = new ArrayList<AverageActivity>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			AverageActivity day = mapper.convertValue(obj,
					AverageActivity.class);
			weekPattern.add(day);
		}
		return weekPattern;
	}

	@Override
	public int getUltimateActivityGoal(String user, ActivityMeasure measure)
			throws IOException, RemoteMethodException, Exception {
		switch (measure) {
		case STEPS:
			return ULTIMATE_GOAL_STEPS;
		case CALORIES:
			return ULTIMATE_GOAL_CALORIES;
		case DISTANCE:
			return ULTIMATE_GOAL_DISTANCE;
		default:
			throw new RuntimeException("Activity measure " + measure +
					" not supported");
		}
	}

	@Override
	public LocalDate getLastSleepWeekPatternUpdate(String user) throws IOException, RemoteMethodException, Exception {
		String url = lrSleepURL + "/lastupdate?userid={userid}";
		DataUpdatedResponse resp = ewallClient.getForObject(url,
				DataUpdatedResponse.class, user);
		DataUpdated dataUpdated = resp.getValue();
		if (dataUpdated == null)
			return null;
		else
			return dataUpdated.getDate();
	}

	@Override
	public LRSleepWeekPattern getSleepWeekPattern(String user, LocalDate date)
			throws IOException, RemoteMethodException, Exception {
		String url = lrSleepURL + "/sleepweekpattern?userid={userid}";
		if (date != null)
			url += "&date={date}";
		List<Object> vars = new ArrayList<Object>();
		vars.add(user);
		if (date != null)
			vars.add(date.toString("yyyy-MM-dd"));
		return ewallClient.getForObject(url, LRSleepWeekPattern.class,
				vars.toArray());
	}

	@Override
	public int getUltimateSleepGoal(String user, SleepGoalMeasure measure)
			throws IOException, RemoteMethodException, Exception {
		switch (measure) {
		case MINIMUM_MINUTES_SLEPT:
			return ULTIMATE_GOAL_MINIMUM_MINUTES_SLEPT;
		case MAXIMUM_MINUTES_SLEPT:
			return ULTIMATE_GOAL_MAXIMUM_MINUTES_SLEPT;
		case SLEEP_EFFICIENCY:
			return ULTIMATE_GOAL_SLEEP_EFFICIENCY;
		case GOING_TO_SLEEP_TIME:
			return ULTIMATE_GOAL_GOING_TO_SLEEP_TIME;
		case WAKING_UP_TIME:
			return ULTIMATE_GOAL_WAKING_UP_TIME;
		default:
			throw new RuntimeException("Sleep goal measure " + measure +
					" not supported");
		}
	}
}
