package eu.ewall.fusioner.fitbit;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.joda.time.DateTimeZone;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

import eu.ewall.platform.commons.datamodel.profile.User;
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
	public static IDSSUserProfile readUser(Map<?,?> eWallCommonUser) {
		ILoggerFactory factory = AppComponents.getInstance().getComponent(
				ILoggerFactory.class);
		Logger logger = factory.getLogger(CommonsUserReader.class.getName());
		IDSSUserProfile idssUserProfile = new IDSSUserProfile();
		
		// Sets the generic source object in the IDSSUserProfile
		idssUserProfile.setSource(eWallCommonUser);
		
		// Sets the username
		idssUserProfile.setUsername((String)eWallCommonUser.get("username"));
		
		// Sets the timezone for the user
		String timezoneid = getTimezoneid(eWallCommonUser);
		DateTimeZone tz = DateTimeZone.getDefault();
		if (timezoneid == null) {
			logger.debug(
					"Time zone not defined for user {}: falling back to {}",
					eWallCommonUser.get("username"), tz.getID());
		}
		try {
			if (timezoneid != null)
				tz = DateTimeZone.forID(timezoneid);
		} catch (Exception ex) {
			logger.debug(
					"Unknown time zone {} for user {}: falling back to {}",
					timezoneid, eWallCommonUser.get("username"), tz.getID());
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
					eWallCommonUser.get("username"));
		}
		idssUserProfile.setLocales(locales);
		
		// Sets the first name of the user
		idssUserProfile.setFirstName((String)eWallCommonUser.get("firstName"));
		
		// Sets the last name of the user
		idssUserProfile.setLastName((String)eWallCommonUser.get("lastName"));
		
		// Sets the role of the user
		idssUserProfile.setRole(eWallCommonUser.get("userRole").toString());
		
		return idssUserProfile;
	}
	
	/**
	 * Retrieves the timezoneid for the specified user. If the timezoneid is
	 * not defined, this method returns null.
	 * 
	 * @param user the user
	 * @return the timezoneid or null
	 */
	private static String getTimezoneid(Map<?,?> user) {
		Map<?,?> profile = (Map<?,?>)user.get("userProfile");
		if (profile == null)
			return null;
		Map<?,?> vcard = (Map<?,?>)profile.get("vCardSubProfile");
		if (vcard == null)
			return null;
		return (String)vcard.get("timezoneid");
	}
	
	/**
	 * Retrieves the preferred and possible secondary language for the
	 * specified user. If no language is defined, this method returns an empty
	 * list.
	 * 
	 * @param user the user
	 * @return the languages or an empty list
	 */
	private static List<String> getLanguages(Map<?,?> user) {
		List<String> result = new ArrayList<String>();
		Map<?,?> profile = (Map<?,?>)user.get("userProfile");
		if (profile == null)
			return result;
		Map<?,?> userPrefs = (Map<?,?>)profile.get("userPreferencesSubProfile");
		if (userPrefs == null)
			return result;
		Map<?,?> sysPrefs = (Map<?,?>)userPrefs.get("systemPreferences");
		if (sysPrefs == null)
			return result;
		if (sysPrefs.get("preferredLanguage") != null)
			result.add((String)sysPrefs.get("preferredLanguage"));
		if (sysPrefs.get("secondaryLanguage") != null)
			result.add((String)sysPrefs.get("secondaryLanguage"));
		return result;
	}
}
