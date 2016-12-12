package eu.ewall.platform.idss;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.client.RestOperations;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.ewall.platform.commons.datamodel.profile.RewardType;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.core.service.IDSSCoreConfiguration;
import eu.ewall.platform.idss.core.service.PullInputProvider;
import eu.ewall.platform.idss.response.ewall.activity.ActivityServiceBrickResponse;
import eu.ewall.platform.idss.response.ewall.sleep.ResponseWakeUpRoutine;
import eu.ewall.platform.idss.response.ewall.weather.WeatherServiceBrickResponse;
import eu.ewall.platform.idss.service.RemoteMethodException;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.service.model.common.ActivityGoal;
import eu.ewall.platform.idss.service.model.common.ActivityGoalResponse;
import eu.ewall.platform.idss.service.model.common.DateTimeServiceResponse;
import eu.ewall.platform.idss.service.model.state.context.ContextModel;
import eu.ewall.platform.idss.service.model.state.context.Weather;
import eu.ewall.platform.idss.service.model.state.domain.ActivityState;
import eu.ewall.platform.idss.service.model.state.domain.PhysicalActivityStateModel;
import eu.ewall.platform.idss.service.model.state.interaction.InteractionModel;
import eu.ewall.platform.idss.service.model.state.user.DailyRoutine;
import eu.ewall.platform.idss.service.model.state.user.UserModel;
import eu.ewall.platform.idss.service.model.state.user.WeeklyRoutine;
import eu.ewall.platform.idss.service.model.type.UnitSystem;
import eu.ewall.platform.idss.utils.AppComponents;
import eu.ewall.platform.idss.utils.datetime.VirtualClock;

/**
 * Implementation of {@link PullInputProvider PullInputProvider} for eWALL.
 * 
 * @author Dennis Hofs (RRD)
 * @author Harm op den Akker (RRD)
 */
public class IDSSCoreSpringPullInputProvider implements PullInputProvider {
	private static final String LOGTAG = "IDSSCoreSpringPullInputProvider";
	private RestOperations ewallClient;
	private IDSSCoreConfiguration config;
	private Logger logger;
	
	/**
	 * Constructs a new instance.
	 * 
	 * @param ewallClient the eWall client
	 * @param profilingServerUrl the base URL to the profiling server (without trailing slash).
	 * @param idssAutomaticGoalSettingUrl the base URL to the automatic goal setting IDSS component (without trailing slash).
	 * @param serviceBrickPhysicalActivityUrl the base URL to the physical activity service brick (without trailing slash).
	 * @param serviceBrickWeatherUrl the base URL to the weather service brick (without trailing slash).
	 */
	public IDSSCoreSpringPullInputProvider(RestOperations ewallClient,
			IDSSCoreConfiguration config) {
		this.ewallClient = ewallClient;
		this.config = config;
		ILoggerFactory logFactory = AppComponents.getInstance().getComponent(ILoggerFactory.class);
		logger = logFactory.getLogger(LOGTAG);
	}

	@Override
	public List<IDSSUserProfile> getUsers() throws IOException, RemoteMethodException, Exception {
		String url = config.getProfilingServerUrl() + "/users/";
		List<?> objList = ewallClient.getForObject(url, List.class);
		List<IDSSUserProfile> users = new ArrayList<IDSSUserProfile>();
		ObjectMapper mapper = new ObjectMapper();
		for (Object obj : objList) {
			User user = mapper.convertValue(obj, User.class);
			if (user.getUserRole() == UserRole.PRIMARY_USER)
				users.add(CommonsUserReader.readUser(user));
		}
		return users;
	}

	@Override
	public void getUserModelAttribute(IDSSUserProfile idssUserProfile, String attribute,
			UserModel userModel) throws IOException, RemoteMethodException,
			Exception {
		
		String username = idssUserProfile.getUsername();
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		// Set attributes directly from the IDSSUserProfile
		
		if (attribute.equals("firstName")) {
			String firstName = idssUserProfile.getFirstName();			
			userModel.setAttribute(attribute,firstName,1.0d,now);
			logger.info("["+username+"] Set UserModel parameter 'firstName' to '"+firstName+"'.");
			return;
		}
		
		if(attribute.equals("lastName")) {
			String lastName = idssUserProfile.getLastName();
			userModel.setAttribute(attribute,lastName,1.0d,now);
			logger.info("["+username+"] Set UserModel parameter 'lastName' to '"+lastName+"'.");
			return;
		}
			
		if(attribute.equals("preferredUnitSystem")) {
			UnitSystem preferredUnitSystem = idssUserProfile.getPreferredUnitSystem();
			userModel.setAttribute(attribute,preferredUnitSystem,1.0d,now);
			logger.info("["+username+"] Set UserModel parameter 'preferredUnitSystem' to '"+preferredUnitSystem+"'.");
			return;
		}
		
			
		if(attribute.equals("unlockedRewards")) {
			List<String> unlockedRewards = new ArrayList<String>();
			User eWallUser = (User)idssUserProfile.getSource();			
			List<RewardType> rewardTypes = eWallUser.getUserProfile().geteWallSubProfile().getRewardsList();
			for(RewardType rt : rewardTypes) {
				unlockedRewards.add(rt.toString());
			}
			userModel.setAttribute(attribute, unlockedRewards, 1.0d, now);
			logger.info("["+username+"] Set UserModel parameter 'unlockedRewards' to '"+unlockedRewards.toString()+"'.");
			return;
		}
		
		if(attribute.equals("weeklyRoutine")) {
			
			String url = config.getLifestyleReasonerSleepUrl() + "/wakeuproutine?userid={userid}";			
			ResponseWakeUpRoutine response = ewallClient.getForObject(url, ResponseWakeUpRoutine.class, username);
			
			if(response != null) {
				WeeklyRoutine weeklyRoutine = userModel.getWeeklyRoutine();
				if(weeklyRoutine == null) weeklyRoutine = new WeeklyRoutine();
				
				DailyRoutine mondayRoutine = weeklyRoutine.getMondayRoutine();
				if(mondayRoutine == null) {
					mondayRoutine = new DailyRoutine();
					weeklyRoutine.setMondayRoutine(mondayRoutine);
				}
				mondayRoutine.setWakeUpTime(response.getWakeUpTimeMonday());
				
				DailyRoutine tuesdayRoutine = weeklyRoutine.getTuesdayRoutine();
				if(tuesdayRoutine == null) {
					tuesdayRoutine = new DailyRoutine();
					weeklyRoutine.setTuesdayRoutine(tuesdayRoutine);
				}
				tuesdayRoutine.setWakeUpTime(response.getWakeUpTimeTuesday());
				
				DailyRoutine wednesdayRoutine = weeklyRoutine.getWednesdayRoutine();
				if(wednesdayRoutine == null) {
					wednesdayRoutine = new DailyRoutine();
					weeklyRoutine.setWednesdayRoutine(wednesdayRoutine);
				}
				wednesdayRoutine.setWakeUpTime(response.getWakeUpTimeWednesday());
				
				DailyRoutine thursdayRoutine = weeklyRoutine.getThursdayRoutine();
				if(thursdayRoutine == null) {
					thursdayRoutine = new DailyRoutine();
					weeklyRoutine.setThursdayRoutine(thursdayRoutine);
				}
				thursdayRoutine.setWakeUpTime(response.getWakeUpTimeThursday());
				
				DailyRoutine fridayRoutine = weeklyRoutine.getFridayRoutine();
				if(fridayRoutine == null) {
					fridayRoutine = new DailyRoutine();
					weeklyRoutine.setFridayRoutine(fridayRoutine);
				}
				fridayRoutine.setWakeUpTime(response.getWakeUpTimeFriday());
				
				DailyRoutine saturdayRoutine = weeklyRoutine.getSaturdayRoutine();
				if(saturdayRoutine == null) {
					saturdayRoutine = new DailyRoutine();
					weeklyRoutine.setSaturdayRoutine(saturdayRoutine);
				}
				saturdayRoutine.setWakeUpTime(response.getWakeUpTimeSaturday());
				
				DailyRoutine sundayRoutine = weeklyRoutine.getSundayRoutine();
				if(sundayRoutine == null) {
					sundayRoutine = new DailyRoutine();
					weeklyRoutine.setSundayRoutine(sundayRoutine);
				}
				sundayRoutine.setWakeUpTime(response.getWakeUpTimeSunday());
				
				userModel.setAttribute(attribute, weeklyRoutine, 1.0d, now);
				logger.info("["+username+"] Set UserModel parameter 'weeklyRoutine' to '"+weeklyRoutine.toString()+"'.");
			}
			
			return;
		}
		
		if(attribute.equals("coinTotal")) {
			
			String url = config.getEWalletServiceUrl() + "/getCoinTotal?username={username}"; 			
			Double response = ewallClient.getForObject(url, Double.class, username);
			
			if(response != null) {
				Integer coinTotal = response.intValue();
				userModel.setAttribute(attribute,coinTotal,1.0d,now);
				logger.info("["+username+"] Set UserModel parameter 'coinTotal' to '"+coinTotal+"'.");
			}
			return;
		}
		
	}

	@Override
	public void getContextModelAttribute(IDSSUserProfile idssUserProfile, String attribute,
			ContextModel model) throws IOException, RemoteMethodException,
			Exception {
		
		String username = idssUserProfile.getUsername();
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if (attribute.equals("currentWeather")) {
			
			// Get updated weather information from service brick.
			String url = config.getServiceBrickWeatherUrl() + 
					"/weather?userid={userid}&lang={lang}&units={units}";
			String lang = idssUserProfile.getLocales().get(0).getLanguage();
			UnitSystem preferredUnitsOfMeasure = idssUserProfile.getPreferredUnitSystem();
			String units = preferredUnitsOfMeasure.toString();
					
			WeatherServiceBrickResponse response = ewallClient.getForObject(
					url, WeatherServiceBrickResponse.class,
					username, lang, units);
			
			response.setUnitsOfMeasure(preferredUnitsOfMeasure);
						
			// Fill the context model with relevant parameters.
			Weather weather = new Weather();
			
			Double temperatureCelsius = null;
			
			if(preferredUnitsOfMeasure == UnitSystem.METRIC) {
				temperatureCelsius = response.getWeatherMain().getTemperature();
				logger.info("["+username+"] temperatureCelsius = "+temperatureCelsius+".");
			} else if(preferredUnitsOfMeasure == UnitSystem.IMPERIAL) {
				Double temperatureFahrenheit = response.getWeatherMain().getTemperature();
				temperatureCelsius = FahrenheitToCelsius(temperatureFahrenheit);
				logger.info("["+username+"] temperatureCelsius = "+temperatureCelsius+" (from "+temperatureFahrenheit+" degrees Fahrenheit).");
			} else {
				Double temperatureKelvin = response.getWeatherMain().getTemperature();
				temperatureCelsius = KelvinToCelsius(temperatureKelvin);
				logger.info("["+username+"] temperatureCelsius = "+temperatureCelsius+" (from "+temperatureKelvin+" degrees Kelvin).");
			}			
			weather.setTemperatureCelsius(temperatureCelsius);
			model.setAttribute(attribute,weather,null,now);
			logger.info("["+username+"] Updated ContextModel parameter '"+attribute+"', values: '(temperatureCelcius:"+temperatureCelsius+")");
			
		}	
	}
	
	/**
	 * Returns the temperature in Celsius, given a temperature value in Fahrenheit.
	 * @param temperatureFahrenheit the temperature value in degrees Fahrenheit.
	 * @return the temperature in degrees Celsius.
	 */
	private Double FahrenheitToCelsius(Double temperatureFahrenheit) {
		//TODO: Write unit test for this.
		//TODO: Perform rounding (XX.X).
		Double temperatureCelsius = ((temperatureFahrenheit - 32) / 1.8);
		return temperatureCelsius;
	}
	
	/**
	 * Returns the temperature in Celsius, given a temperature value in Kelvin.
	 * @param temperatureKelvin the temperature value in degrees Kelvin.
	 * @return the temperature in degrees Celsius.
	 */
	private Double KelvinToCelsius(Double temperatureKelvin) {
		//TODO: Write unit test for this.
		//TODO: Perform rounding (XX.X).
		Double temperatureCelsius = temperatureKelvin - 273.15;
		return temperatureCelsius;
	}

	@Override
	public void getPhysicalActivityStateModelAttribute(IDSSUserProfile idssUserProfile,
			String attribute, PhysicalActivityStateModel model)
			throws IOException, RemoteMethodException, Exception {
		
		String username = idssUserProfile.getUsername();
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if (attribute.equals("currentActivityState")) {
			String url = config.getServiceBrickPhysicalActivityUrl() +
					"/v1/{username}/movement?from={from}&to={to}&aggregation=1d";
			LocalDate date = now.toLocalDate();
			DateTime from = date.toDateTimeAtStartOfDay(idssUserProfile.getTimeZone());
			DateTime to = date.plusDays(1).toDateTimeAtStartOfDay(
					idssUserProfile.getTimeZone());
			String timeFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSZZ";
			ActivityServiceBrickResponse response = ewallClient.getForObject(
					url, ActivityServiceBrickResponse.class,
					username, from.toString(timeFormat),
					to.toString(timeFormat));
			
			ActivityState currentActivityState = new ActivityState(
					response.getTotalSteps(),
					response.getTotalBurnedCalories(),
					response.getTotalKilometers());
			
			model.setAttribute(attribute, currentActivityState, null, now);
			logger.info("["+username+"] Updated PhysicalActivityStateModel parameter '"+attribute+"', values: '(steps:"+currentActivityState.getSteps()+",burnedCalories:"+currentActivityState.getBurnedCalories()+",kilometers:"+currentActivityState.getKilometers()+").");

		} else if (attribute.equals("goalActivityState")) {
			String url = config.getAutomaticGoalSettingUrl() +
					"/activitygoal_steps?userid={userid}&date={date}";
			LocalDate date = now.toLocalDate();
			ActivityGoal response = ewallClient.getForObject(url,
					ActivityGoalResponse.class, username,
					date.toString("yyyy-MM-dd")).getValue();
			if (response == null)
				return;
			
			ActivityState goalActivityState = new ActivityState();
			goalActivityState.setSteps(response.getGoal());		

			model.setAttribute(attribute, goalActivityState, null, now);
			logger.info("["+username+"] Updated PhysicalActivityStateModel parameter '"+attribute+"', values: '(steps:"+goalActivityState.getSteps()+",burnedCalories:"+goalActivityState.getBurnedCalories()+",kilometers:"+goalActivityState.getKilometers()+").");
		}
	}

	@Override
	public void getInteractionModelAttribute(IDSSUserProfile idssUserProfile,
			String attribute, InteractionModel interactionModel) throws IOException,
			RemoteMethodException, Exception {
				
		String username = idssUserProfile.getUsername();
		VirtualClock clock = VirtualClock.getInstance();
		DateTime now = clock.getTime().withZone(idssUserProfile.getTimeZone());
		
		if(attribute.equals("lastInteractionCalendarApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionCalendarApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionCognitiveExerciseApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionCognitiveExerciseApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionDomoticsApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionDomoticsApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionMainScreen")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionMainScreen?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionMyActivityApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionMyActivityApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionMyHealthApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionMyHealthApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionSleepApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionSleepApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute,lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
		if(attribute.equals("lastInteractionVideoExerciseApplication")) {
			String url = config.getUserInteractionLoggerUrl() +
					"/lastInteractionVideoExerciseApplication?username={username}";
			DateTimeServiceResponse response = ewallClient.getForObject(url,
					DateTimeServiceResponse.class, username);
			
			if (response == null) return;
			
			DateTime lastInteraction = response.getValue();
			
			if(lastInteraction == null) return;
			
			interactionModel.setAttribute(attribute, lastInteraction, 1.0d, now);
			logger.info("["+username+"] Updated InteractionModel parameter '"+attribute+"', value: '"+lastInteraction.toString()+"'.");
		}
		
	}
}
