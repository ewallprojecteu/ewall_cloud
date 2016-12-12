package eu.ewall.platform.idss;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import eu.ewall.platform.commons.datamodel.profile.HealthDiagnosisType;
import eu.ewall.platform.commons.datamodel.profile.HealthSubProfile;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.commons.datamodel.profile.UserPreferencesSubProfile;
import eu.ewall.platform.commons.datamodel.profile.UserProfile;
import eu.ewall.platform.commons.datamodel.profile.VCardSubProfile;
import eu.ewall.platform.commons.datamodel.profile.preferences.SystemPreferences;
import eu.ewall.platform.idss.service.model.IDSSUserProfile;
import eu.ewall.platform.idss.utils.AppComponents;

/**
 * This class can read selected data from a {@link User User} object from the
 * eWall commons and return a {@link IDSSUserProfile IDSSUserProfile} object.
 * It will read the time zone of the user and fall back to the default time
 * zone if the time zone ID is missing or unknown. And it will read the
 * preferred locales and fall back to English if not defined. In case of a
 * fallback, it will log a debug message. The User object will be set as the
 * source in the IDSSUserProfile object.
 * 
 * @author Dennis Hofs (RRD)
 */
public class CommonsUserReader {
	
	/**
	 * Reads the specified User object.
	 * 
	 * @param eWallCommonUser the User object
	 * @return the IDSSUserProfile object
	 */
	public static IDSSUserProfile readUser(User eWallCommonUser) {
		ILoggerFactory factory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = factory.getLogger(CommonsUserReader.class.getName());
		IDSSUserProfile idssUserProfile = new IDSSUserProfile();
		
		// Sets the list of available applications for this user (application names).
		idssUserProfile.setApplicationNames(eWallCommonUser.getApplicationNamesList());
		
		// Set the creation time of the IDSSUserprofile object.
		idssUserProfile.setUpdateTime(new DateTime());
		
		// Sets the generic source object in the IDSSUserProfile
		idssUserProfile.setSource(eWallCommonUser);
		
		// Sets the username
		idssUserProfile.setUsername(eWallCommonUser.getUsername());
		
		// Sets the timezone for the user
		String timezoneid = getTimezoneid(eWallCommonUser);
		DateTimeZone tz = DateTimeZone.getDefault();
		if (timezoneid == null) {
			logger.debug(
					"Time zone not defined for user {}: falling back to {}",
					eWallCommonUser.getUsername(), tz.getID());
		}
		try {
			if (timezoneid != null)
				tz = DateTimeZone.forID(timezoneid);
		} catch (Exception ex) {
			logger.debug(
					"Unknown time zone {} for user {}: falling back to {}",
					timezoneid, eWallCommonUser.getUsername(), tz.getID());
		}
		idssUserProfile.setTimeZone(tz);
		
		// Sets the locale(s), the first one being the preferred
		List<String> languages = getLanguages(eWallCommonUser);
		List<Locale> locales = new ArrayList<Locale>();
		for (String lang : languages) {
			locales.add(new Locale(lang));
		}
		if (locales.isEmpty()) {
			locales.add(Locale.ENGLISH);
			logger.debug(
					"Language not defined for user {}: falling back to English",
					eWallCommonUser.getUsername());
		}
		idssUserProfile.setLocales(locales);
		
		// Sets the first name of the user
		idssUserProfile.setFirstName(eWallCommonUser.getFirstName());
		
		// Sets the last name of the user
		idssUserProfile.setLastName(eWallCommonUser.getLastName());
		
		// Sets the role of the user
		idssUserProfile.setRole(eWallCommonUser.getUserRole().toString());
		
		// Sets the preferred unit system (metric, imperial, standard) of the user
		eu.ewall.platform.commons.datamodel.profile.preferences.UnitSystem eWallCommonsUnitSystem = null;
		
		UserProfile eWallCommonsUserProfile = eWallCommonUser.getUserProfile();
		if(eWallCommonsUserProfile != null) {
			UserPreferencesSubProfile eWallCommonsUserPreferencesSubProfile = eWallCommonsUserProfile.getUserPreferencesSubProfile();
			if(eWallCommonsUserPreferencesSubProfile != null) {
				SystemPreferences systemPreferences = (SystemPreferences)eWallCommonsUserPreferencesSubProfile.getSystemPreferences();
				if(systemPreferences != null) {
					eWallCommonsUnitSystem = systemPreferences.getPreferredUnitSystem();
				}
			}
		}
			
		eu.ewall.platform.idss.service.model.type.UnitSystem idssUnitSystem;
		
		if(eWallCommonsUnitSystem == null) {
			idssUnitSystem = eu.ewall.platform.idss.service.model.type.UnitSystem.METRIC;
		} else if(eWallCommonsUnitSystem == eu.ewall.platform.commons.datamodel.profile.preferences.UnitSystem.METRIC) {
			idssUnitSystem = eu.ewall.platform.idss.service.model.type.UnitSystem.METRIC;
		} else if(eWallCommonsUnitSystem == eu.ewall.platform.commons.datamodel.profile.preferences.UnitSystem.IMPERIAL) {
			idssUnitSystem = eu.ewall.platform.idss.service.model.type.UnitSystem.IMPERIAL;
		} else if(eWallCommonsUnitSystem == eu.ewall.platform.commons.datamodel.profile.preferences.UnitSystem.SI_STANDARD) {
			idssUnitSystem = eu.ewall.platform.idss.service.model.type.UnitSystem.STANDARD;
		} else {
			idssUnitSystem = eu.ewall.platform.idss.service.model.type.UnitSystem.METRIC;
		}
		
		idssUserProfile.setPreferredUnitSystem(idssUnitSystem);
		
		idssUserProfile.setAge(getAge(eWallCommonUser));
		idssUserProfile.setDiagnosis(getDiagnosis(eWallCommonUser));
		
		return idssUserProfile;
	}
	
	/**
	 * Retrieves the timezoneid for the specified user. If the timezoneid is
	 * not defined, this method returns null.
	 * 
	 * @param user the user
	 * @return the timezoneid or null
	 */
	private static String getTimezoneid(User user) {
		eu.ewall.platform.commons.datamodel.profile.UserProfile profile =
				user.getUserProfile();
		if (profile == null)
			return null;
		VCardSubProfile vcard = profile.getvCardSubProfile();
		if (vcard == null)
			return null;
		return vcard.getTimezoneid();
	}
	
	/**
	 * Retrieves the preferred and possible secondary language for the
	 * specified user. If no language is defined, this method returns an empty
	 * list.
	 * 
	 * @param user the user
	 * @return the languages or an empty list
	 */
	private static List<String> getLanguages(User user) {
		List<String> result = new ArrayList<String>();
		eu.ewall.platform.commons.datamodel.profile.UserProfile profile =
				user.getUserProfile();
		if (profile == null)
			return result;
		UserPreferencesSubProfile userPrefs =
				profile.getUserPreferencesSubProfile();
		if (userPrefs == null)
			return result;
		SystemPreferences sysPrefs = (SystemPreferences)userPrefs.getSystemPreferences();
		if (sysPrefs == null)
			return result;
		if (sysPrefs.getPreferredLanguage() != null)
			result.add(sysPrefs.getPreferredLanguage());
		if (sysPrefs.getSecondaryLanguage() != null)
			result.add(sysPrefs.getSecondaryLanguage());
		return result;
	}
	
	/**
	 * Retrieves the age. If the age is not defined, this method returns null.
	 * 
	 * @param user the user
	 * @return the age or null
	 */
	private static Integer getAge(User user) {
		eu.ewall.platform.commons.datamodel.profile.UserProfile profile =
				user.getUserProfile();
		if (profile == null)
			return null;
		VCardSubProfile vcard = profile.getvCardSubProfile();
		if (vcard == null)
			return null;
		if (vcard.getAge() > 0)
			return vcard.getAge();
		return null;
	}
	
	/**
	 * Retrieves the diagnosis list for the specified user. If no diagnosis is
	 * defined, this method returns an empty list.
	 * 
	 * @param user the user
	 * @return the diagnosis list (can be empty)
	 */
	private static List<String> getDiagnosis(User user) {
		List<String> result = new ArrayList<String>();
		eu.ewall.platform.commons.datamodel.profile.UserProfile profile =
				user.getUserProfile();
		if (profile == null)
			return result;
		HealthSubProfile healthProfile = profile.getHealthSubProfile();
		if (healthProfile == null)
			return result;
		List<HealthDiagnosisType> diagnosis =
				healthProfile.getHealthDiagnosisType();
		if (diagnosis == null)
			return result;
		for (HealthDiagnosisType item : diagnosis) {
			if (item != HealthDiagnosisType.UNKNOWN)
				result.add(item.toString());
		}
		return result;
	}
}
