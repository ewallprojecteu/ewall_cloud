/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.idss.wellbeingads.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import eu.ewall.platform.commons.datamodel.profile.HealthDiagnosisType;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserRole;
import eu.ewall.platform.idss.service.model.common.ActivityGoalResponse;
import eu.ewall.platform.idss.service.model.common.MoodData;
import eu.ewall.platform.idss.service.model.common.MoodDataResponse;
import eu.ewall.platform.idss.service.model.type.MoodCategory;
import eu.ewall.platform.idss.wellbeingads.adtype.WellbeingAdCategory;
import eu.ewall.platform.idss.wellbeingads.dao.ActivityGoalDAO;
import eu.ewall.platform.idss.wellbeingads.dao.MoodDAO;
import eu.ewall.platform.idss.wellbeingads.dao.PhysicalActivityDAO;
import eu.ewall.platform.idss.wellbeingads.dao.ProfilingServerDAO;
import eu.ewall.platform.idss.wellbeingads.dao.UserInteractionLoggerDAO;
import eu.ewall.platform.idss.wellbeingads.dao.WeatherDAO;
import eu.ewall.platform.idss.wellbeingads.model.lastinteraction.LastIntercationResponse;
import eu.ewall.platform.idss.wellbeingads.model.physicalactivity.Movement;
import eu.ewall.platform.idss.wellbeingads.model.physicalactivity.MovementHistory;
import eu.ewall.platform.idss.wellbeingads.model.weather.WeatherCurrentResponse;
import eu.ewall.platform.idss.wellbeingads.model.weather.owm.Weather;

/**
 * The Class WellbeingAdsController.
 */
@RestController
@EnableScheduling
public class WellbeingAdsController {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(WellbeingAdsController.class);

	/** 30 minutes in milliseconds. */
	private static final int TIME_WINDOW_TO_DETERMINE_CURRENT_RECOMMENDED_AD_FOR_USERS = 30 * 60 * 1000;

	/** The Constant INITIAL_DELAY. */
	private static final int INITIAL_DELAY = 1000; // 1 second

	/** The last shown morning ad for some user. */
	private final HashMap<String, LastShownAd> lastShownMorningAdForCOPDuserMap = new HashMap<String, LastShownAd>();

	/** The next ad to recommend. */
	private final ConcurrentMap<String, WellbeingAdCategory> nextAdToRecommend = new ConcurrentHashMap<String, WellbeingAdCategory>();

	/** The profiling server dao. */
	@Autowired
	private ProfilingServerDAO profilingServerDAO;

	/** The user interaction logger dao. */
	@Autowired
	private UserInteractionLoggerDAO userInteractionLoggerDAO;

	/** The weather dao. */
	@Autowired
	private WeatherDAO weatherDAO;

	/** The physical activity dao. */
	@Autowired
	private PhysicalActivityDAO physicalActivityDAO;

	/** The activity goal dao. */
	@Autowired
	private ActivityGoalDAO activityGoalDAO;

	/** The mood dao. */
	@Autowired
	private MoodDAO moodDAO;

	/**
	 * Gets the current wellbeing ad category.
	 *
	 * @param username
	 *            the username
	 * @return the current wellbeing ad category
	 */
	@RequestMapping(value = "{username}/get", method = RequestMethod.GET)
	public WellbeingAdCategory getCurrentWellbeingAdCategory(@PathVariable String username) {
		log.debug("User {} does not have recommeded ad so default ad (NONE) is returned.");
		if (!nextAdToRecommend.containsKey(username)) {
			return WellbeingAdCategory.NONE;
		} else {
			log.debug("Returning {} ad for User {}.", nextAdToRecommend.get(username), username);
			return nextAdToRecommend.get(username);
		}
	}

	/**
	 * Sets the currently recommended ads for all (primary) users.
	 */
	@Scheduled(fixedDelay = TIME_WINDOW_TO_DETERMINE_CURRENT_RECOMMENDED_AD_FOR_USERS, initialDelay = INITIAL_DELAY)
	public void setCurrentlyRecommendedAdsForAllUsers() {
		List<User> users = profilingServerDAO.getAllUsers();
		if (users != null) {
			for (User user : users) {
				if (user.getUserRole() == UserRole.PRIMARY_USER) {
					setCurrentlyRecommendedAdForUser(user);
					// emulateCurrentlyRecommendedAdForUser(user);
				}
			}
		}
	}

	/**
	 * Test method to emulate currently recommended ads for user and (disabling all
	 * scheduled calls to services: Weather, Physical Activity, Mood, Activity
	 * Goal.)
	 *
	 * @param user
	 *            the user
	 */
	public void emulateCurrentlyRecommendedAdForUser(User user) {

		Random random = new Random();

		WellbeingAdCategory[] wellbeingAdsArray = WellbeingAdCategory.values();

		WellbeingAdCategory randomAd = wellbeingAdsArray[random.nextInt(wellbeingAdsArray.length)];

		nextAdToRecommend.put(user.getUsername(), randomAd);

		log.info("Returning {} ad for User {}.", nextAdToRecommend.get(user.getUsername()), user.getUsername());

	}

	/**
	 * Sets the currently recommended ad for user.
	 *
	 * @param user
	 *            the new currently recommended ad for user
	 */
	public void setCurrentlyRecommendedAdForUser(User user) {

		String username = user.getUsername();
		String timezoneid = user.getUserProfile().getvCardSubProfile().getTimezoneid();
		DateTime now = DateTime.now(DateTimeZone.forID(timezoneid));
		
		//initializing recommended add to none
		nextAdToRecommend.put(username, WellbeingAdCategory.NONE);

		log.debug("Setting currently recommended Ad For User with username: " + username + " with timezone id: "
				+ timezoneid + " (hour: " + now.getHourOfDay() + ")");

		// ******** breakfast ********
		// TODO: take outOfBed average from sleep reasoner for usual breakfast
		// time (and 8:30 - 9:30, not 8 - 9), do not use hour of day, but
		// compare times
		if (now.getHourOfDay() >= 8 && now.getHourOfDay() < 9) {
			nextAdToRecommend.put(username, WellbeingAdCategory.BREAKFAST_AD);
			log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
		}

		// ******** video, bike cognitive in the morning period ********
		if (now.getHourOfDay() >= 10 && now.getHourOfDay() < 11) {
			if (isCOPD(user)) {
				if (isWeatherNice(username)) {
					lastShownMorningAdForCOPDuserMap.put(username, new LastShownAd(now, WellbeingAdCategory.BIKE_AD));
					nextAdToRecommend.put(username, WellbeingAdCategory.BIKE_AD);
					log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
				}

				// weather is not nice
				LastIntercationResponse lastVideoTraining = userInteractionLoggerDAO
						.getLastInteractionVideoExercise(username);
				if (lastVideoTraining != null) {
					if (now.minusDays(1).withTimeAtStartOfDay().isAfter(lastVideoTraining.getDateTimeValue())) {
						// user did not used video exercise app since yesterday
						log.debug("User did not use video app since yesterday. For username: " + username
								+ " nextAdToRecommend: " + nextAdToRecommend.get(username));
						lastShownMorningAdForCOPDuserMap.put(username,
								new LastShownAd(now, WellbeingAdCategory.VIDEO_AD));
						nextAdToRecommend.put(username, WellbeingAdCategory.VIDEO_AD);
					}
				}

				// user has used video training app yesterday
				// DateTimeFormatter dateTimeFormatterForPhysicalActivity =
				// DateTimeFormat
				// .forPattern("yyyy-MM-dd HH:mm:ss");
				// DateTimeFormatter dateTimeFormatterForActivityGoal =
				// DateTimeFormat.forPattern("yyyy-MM-dd");
				if (isUserPerformingWell(username, now.withTimeAtStartOfDay(), now, now.toLocalDate())) {
					lastShownMorningAdForCOPDuserMap.put(username,
							new LastShownAd(now, WellbeingAdCategory.COGNITIVE_AD));
					nextAdToRecommend.put(username, WellbeingAdCategory.COGNITIVE_AD);
					log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
				}

				lastShownMorningAdForCOPDuserMap.put(username, new LastShownAd(now, WellbeingAdCategory.VIDEO_AD));
				nextAdToRecommend.put(username, WellbeingAdCategory.VIDEO_AD);
				log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
			}

			// based on spec, if user is not COPD, we use MCI logic for all
			// (FRAIL, UNKNOWN, etc)
			if (isWeatherNice(username)) {
				LastIntercationResponse lastCognitiveTraining = userInteractionLoggerDAO
						.getLastInteractionCognitiveExercise(username);
				if (lastCognitiveTraining != null) {
					if (now.withTimeAtStartOfDay().isAfter(lastCognitiveTraining.getDateTimeValue())) {
						// user did not used cognitive training today
						log.debug("User {} did not used cognitive training today. Recommending cognitive ad.",
								username);
						nextAdToRecommend.put(username, WellbeingAdCategory.COGNITIVE_AD);
					}
				}
				nextAdToRecommend.put(username, WellbeingAdCategory.BIKE_AD);
				log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
			}

			// weather is not nice
			LastIntercationResponse lastVideoTraining = userInteractionLoggerDAO
					.getLastInteractionVideoExercise(username);
			if (lastVideoTraining != null) {
				if (now.minusDays(3).withTimeAtStartOfDay().isAfter(lastVideoTraining.getDateTimeValue())) {
					// user did not use video exercise app in last 3 days
					log.debug("User {} did not use video training for more than 3 days. Recommending video ad.",
							username);
					nextAdToRecommend.put(username, WellbeingAdCategory.VIDEO_AD);
				}
			}
			nextAdToRecommend.put(username, WellbeingAdCategory.COGNITIVE_AD);
			log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
		}

		// ******** between 11 and 16 recommend socialize ad or listen to music.
		// Added to increase the number of recommended ads ********
		if (now.getHourOfDay() >= 11 && now.getHourOfDay() < 16) {

			Random random = new Random();
			WellbeingAdCategory[] wellbeingAdsSubset = new WellbeingAdCategory[] { WellbeingAdCategory.MUSIC_AD,
					WellbeingAdCategory.SOCIAL_AD };
			WellbeingAdCategory randomAd = wellbeingAdsSubset[random.nextInt(wellbeingAdsSubset.length)];

			nextAdToRecommend.put(user.getUsername(), randomAd);
			log.debug("Returning {} ad for User {}.", nextAdToRecommend.get(user.getUsername()), user.getUsername());

		}

		// ******** video, bike cognitive in the afternoon period ********
		if (now.getHourOfDay() >= 16 && now.getHourOfDay() < 18) {

			if (isCOPD(user)) {
				if ((now.getDayOfWeek() == 6) || (now.getDayOfWeek() == 7)) {
					// is weekend
					if (isWeatherNice(username)) {
						// weather is nice
						nextAdToRecommend.put(username, WellbeingAdCategory.BIKE_AD);
						log.debug(
								"For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
					} else {
						// weather is not nice
						nextAdToRecommend.put(username,
								checkIfBikeOrVideoAdWereShownThisMorningAndRecommendAd(username, now));
						log.debug(
								"For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
					}
				}
				// is not weekend
				nextAdToRecommend.put(username, checkIfBikeOrVideoAdWereShownThisMorningAndRecommendAd(username, now));
				log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));

			}
			// is not COPD
			if ((now.getDayOfWeek() == 6) || (now.getDayOfWeek() == 7)) {
				// is weekend
				if (isWeatherNice(username)) {
					nextAdToRecommend.put(username, WellbeingAdCategory.BIKE_AD);
					log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
				}
				LastIntercationResponse lastCognitiveTraining = userInteractionLoggerDAO
						.getLastInteractionCognitiveExercise(username);
				if (lastCognitiveTraining != null) {
					if (now.withTimeAtStartOfDay().isAfter(lastCognitiveTraining.getDateTimeValue())) {
						// user did not used cognitive training today
						log.debug("User {} did not used cognitive training today. Recommeding cognitive ad.", username);
						nextAdToRecommend.put(username, WellbeingAdCategory.COGNITIVE_AD);
					}
				}

				if (now.getHourOfDay() < 17) {
					nextAdToRecommend.put(username, WellbeingAdCategory.VIDEO_AD);
					log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
				}
				nextAdToRecommend.put(username, WellbeingAdCategory.COGNITIVE_AD);
				log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
			}

		}

		// ******** music ********
		if (now.getDayOfWeek() == 7) {
			// is Sunday
			if (now.getHourOfDay() >= 17 && now.getHourOfDay() < 20) {
				nextAdToRecommend.put(username, WellbeingAdCategory.MUSIC_AD);
				log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
			}

			// not sunday
		} else {
			// is user in bad mood (old way by looking into visual sensing data)
			// DateTime oneHoursEarlier = now.minusHours(1);
			// VisualSensing lastVisualData =
			// profilingServerDAO.getLastVisualSensingData(username,
			// oneHoursEarlier.toDate(), now.toDate(), 1);
			// if (lastVisualData != null &&
			// lastVisualData.getEmotion().equals(EmotionalStateCategory.SADNESS))
			// {
			// if (now.getHourOfDay() >= 18 && now.getHourOfDay() < 20) {
			// log.debug("User seems in sad mood. Returning music ad.");
			// return WellbeingAdCategory.MUSIC_AD;
			// }
			// }

			// is user in bad mood (new way by contacting LR Mood)
			DateTime fourDaysEarlier = now.minusDays(4);
			if (isUserInABadMood(username, fourDaysEarlier.toLocalDate(), now.toLocalDate())) {
				if (now.getHourOfDay() >= 18 && now.getHourOfDay() < 20) {
					nextAdToRecommend.put(username, WellbeingAdCategory.MUSIC_AD);
					log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
				}

			}
			// else user is not in a bad mood
			else {
				// if it is between 18:30 and 19:30
				if ((now.getHourOfDay() == 18 && now.getMinuteOfHour() >= 30)
						|| (now.getHourOfDay() == 19 && now.getMinuteOfHour() <= 30)) {
					nextAdToRecommend.put(username, WellbeingAdCategory.MUSIC_AD);
					log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
				}
			}

		}

		// ******** sleep ********
		// TODO: take inBed average from sleep reasoner for sleep time
		if (now.getHourOfDay() >= 21 && now.getHourOfDay() < 23) {
			nextAdToRecommend.put(username, WellbeingAdCategory.SLEEP_AD);
			log.debug("For username: " + username + " nextAdToRecommend: " + nextAdToRecommend.get(username));
		}

		// ******** apps usage ********
		LastIntercationResponse lastInteractionReponse = userInteractionLoggerDAO
				.getLastInteractionMyActivity(username);
		if (lastInteractionReponse != null) {
			if (now.minusDays(1).isAfter(lastInteractionReponse.getDateTimeValue())) {
				// is more than 1 day since it accessed activity book
				log.debug("User {} did not use My Activity Book for more than 1 day. Recommending book activity ad.");
				nextAdToRecommend.put(username, WellbeingAdCategory.BOOK_ACTIVITY_AD);
			}
		}

		lastInteractionReponse = userInteractionLoggerDAO.getLastInteractionHealth(username);
		if (lastInteractionReponse != null) {
			if (now.minusDays(1).isAfter(lastInteractionReponse.getDateTimeValue())) {
				// is more than 1 day since it accessed activity book
				log.debug("User {} did not use My Helath Book for more than 1 day. Recommending book health ad.");
				nextAdToRecommend.put(username, WellbeingAdCategory.BOOK_HEALTH_AD);
			}
		}

		lastInteractionReponse = userInteractionLoggerDAO.getLastInteractionSleep(username);
		if (lastInteractionReponse != null) {
			if (now.minusDays(1).isAfter(lastInteractionReponse.getDateTimeValue())) {
				// is more than 1 day since it accessed activity book
				log.debug("User {} did not use My Sleep Book for more than 1 day. Recommenging book sleep ad.");
				nextAdToRecommend.put(username, WellbeingAdCategory.BOOK_SLEEP_AD);
			}
		}

		lastInteractionReponse = userInteractionLoggerDAO.getLastInteractionDailyLife(username);
		if (lastInteractionReponse != null) {
			if (now.minusDays(1).isAfter(lastInteractionReponse.getDateTimeValue())) {
				// is more than 1 day since it accessed activity book
				log.debug(
						"User {} did not use My Daily Life Book for more than 1 day. Recommending book daily life ad.");
				nextAdToRecommend.put(username, WellbeingAdCategory.BOOK_DAY_AD);
			}
		}

		lastInteractionReponse = userInteractionLoggerDAO.getLastInteractionHelp(username);
		if (lastInteractionReponse != null) {
			if (now.minusDays(1).isAfter(lastInteractionReponse.getDateTimeValue())) {
				// is more than 1 day since it accessed activity book
				log.debug("User {} did not use Help Book for more than 1 day. Recommending book daily life ad.");
				nextAdToRecommend.put(username, WellbeingAdCategory.BOOK_HELP_AD);
			}
		}

		log.info("Currently recommended ad for User with username: {} is: {}.", user.getUsername(),
				nextAdToRecommend.get(user.getUsername()));
	}

	// TODO: what parameters define "nice" weather (for biking in particular)?
	/**
	 * Checks if is weather nice.
	 *
	 * @param username
	 *            the username
	 * @return true, if is weather nice
	 */
	private boolean isWeatherNice(String username) {

		try {
			WeatherCurrentResponse response = weatherDAO.getCurrentWeather(username);
			if (response != null) {
				float temperature = response.getMain().getTemp();

				// temps below 10C and above 30C are not considered as nice for
				// biking
				if (temperature < 10 || temperature > 30) {
					log.debug("Weather for user {} is not considered suitable for biking (temperature = {})", username,
							temperature);
					return false;
				}

				// humidity below 25 and above 80 is not considered as nice for
				// biking
				float humidity = response.getMain().getHumidity();
				if (humidity < 25 || humidity > 80) {
					log.debug("Weather for user {} is not considered suitable for biking (humidity = {})", username,
							humidity);
					return false;
				}

				List<Weather> weathers = response.getWeather();
				for (Weather weather : weathers) {
					// codes 8xx mean cloudy, all other mean rain, snow.....
					if (weather.getId() < 800 || weather.getId() >= 900) {
						log.debug("Weather for user {} is not considered suitable for biking (weather id = {})",
								username, weather.getDescription());
						return false;
					}
				}
				log.debug("Current weather for user {} is cosidered as nice.", username);
				return true;
			}
		} catch (Exception e) {
			log.error("Error in determining if the weather is nice.", e);
			return false;
		}
		return false;
	}

	/**
	 * Checks if the user is performing well physically.
	 *
	 * @param username
	 *            the username
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @param date
	 *            the date
	 * @return true, if the user is performing well
	 */
	private boolean isUserPerformingWell(String username, DateTime from, DateTime to, LocalDate date) {
		try {
			MovementHistory movementHistoryResponse = physicalActivityDAO.getStepsForCurrentDay(username, from, to);

			ActivityGoalResponse activityGoalResponse = activityGoalDAO.getStepsGoalForCurrentDay(username, date);

			if ((movementHistoryResponse != null) && (activityGoalResponse != null)) {
				long sumOfTheSteps = 0;
				if (movementHistoryResponse.getMovements() != null)
					for (Movement movement : movementHistoryResponse.getMovements()) {
						sumOfTheSteps += movement.getSteps();
					}

				// if user has reached the goal (number of steps) for this day
				// he is performing well
				if (sumOfTheSteps >= activityGoalResponse.getValue().getGoal())
					return true;
			}

		} catch (Exception e) {
			log.error("Error in determining if the user is performing well physically.", e);
			return false;
		}
		return false;

	}

	/**
	 * Checks if is user in a bad mood.
	 *
	 * @param userid
	 *            the userid
	 * @param from
	 *            the from
	 * @param to
	 *            the to
	 * @return true, if is user in a bad mood
	 */
	private boolean isUserInABadMood(String userid, LocalDate from, LocalDate to) {
		try {
			MoodDataResponse moodDataResponse = moodDAO.getLastMood(userid, from, to);

			if (moodDataResponse != null) {
				List<MoodData> mdlist = moodDataResponse.getValue();

				if (mdlist != null && !mdlist.isEmpty()) {
					MoodData lastMood = mdlist.get(mdlist.size() - 1);
					if (lastMood.getMood() == MoodCategory.NEGATIVE)
						return true;
				}
			}

		} catch (Exception e) {
			log.error("Error in determining if the user is in a bad mood.", e);
			return false;
		}
		return false;

	}

	/**
	 * Check if bike or video ad were shown this morning and recommend ad.
	 *
	 * @param username
	 *            the username
	 * @param dt
	 *            the dt
	 * @return the wellbeing ad category
	 */
	private WellbeingAdCategory checkIfBikeOrVideoAdWereShownThisMorningAndRecommendAd(String username, DateTime dt) {
		Interval today = new Interval(dt.withTimeAtStartOfDay(), Days.ONE);
		if (!lastShownMorningAdForCOPDuserMap.containsKey(username))
			return WellbeingAdCategory.VIDEO_AD;

		LastShownAd lastShownAdForUser = lastShownMorningAdForCOPDuserMap.get(username);

		if (today.contains(lastShownAdForUser.getTimeOfTheAd())
				&& (lastShownAdForUser.getAdCategory() == WellbeingAdCategory.BIKE_AD
						|| lastShownAdForUser.getAdCategory() == WellbeingAdCategory.VIDEO_AD)) {
			// BikeOrVideoAdWereShownThisMorning (and the hours are between 16
			// and 18 already)
			if (dt.getHourOfDay() < 17) {
				log.debug("Returning video ad.");
				return WellbeingAdCategory.VIDEO_AD;
			} else {
				log.debug("Returning cognitive ad.");
				return WellbeingAdCategory.COGNITIVE_AD;
			}
		} else {
			// Bike Or Video Ad Were now Shown This Morning (and the hours are
			// between 16 and 18 already)
			log.debug("Returning video ad.");
			return WellbeingAdCategory.VIDEO_AD;
		}

	}

	/**
	 * Checks if is copd.
	 *
	 * @param user
	 *            the user
	 * @return true, if is copd
	 */
	private boolean isCOPD(User user) {
		try {
			List<HealthDiagnosisType> diagnosisList = user.getUserProfile().getHealthSubProfile()
					.getHealthDiagnosisList();
			if (diagnosisList.contains(HealthDiagnosisType.COPD_GOLD1)
					|| diagnosisList.contains(HealthDiagnosisType.COPD_GOLD2)
					|| diagnosisList.contains(HealthDiagnosisType.COPD_GOLD3)
					|| diagnosisList.contains(HealthDiagnosisType.COPD_GOLD4)) {
				log.debug("User {} is considered as COPD", user.getUsername());
				return true;
			}
		} catch (Exception e) {
			log.error("Error in determening if user has COPD type of diagnosis");
			return false;
		}
		log.debug("User {} is NOT considered as COPD", user.getUsername());
		return false;
	}

}