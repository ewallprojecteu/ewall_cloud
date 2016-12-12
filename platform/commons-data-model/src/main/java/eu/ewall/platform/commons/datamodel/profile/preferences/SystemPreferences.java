/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile.preferences;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * The Class SystemPreferences.
 *
 * @author eandgrg
 */
public class SystemPreferences extends Preferences {

    /** The preferred language. */
    private String preferredLanguage;

    /** The secondary language. */
    private String secondaryLanguage;

    /** The preferred unit system. */
    private UnitSystem preferredUnitSystem;

    /**
     * The Constructor.
     */
    public SystemPreferences() {

	// set default locale to english
	preferredLanguage = "en";
	
	//set default unit system to Metric
	preferredUnitSystem=UnitSystem.METRIC;
    }

    /**
     * Gets the preferred language.
     *
     * @return the preferredLanguage
     */
    public String getPreferredLanguage() {
	return preferredLanguage;
    }

    /**
     * Sets the preferred language only if the language code can be found in
     * 2-letter language codes defined in ISO 639.
     *
     * @param preferredLanguage
     *            the preferred language
     * @return true, if successful
     * @see <a href="http://en.wikipedia.org/wiki/ISO_639">http://en.wikipedia.
     *      org/wiki/ISO_639</a>
     */
    public boolean setPreferredLanguage(String preferredLanguage) {
	if (isValidISOLanguage(preferredLanguage)) {
	    this.preferredLanguage = preferredLanguage;
	    return true;
	} else
	    return false;
    }

    /**
     * Gets the secondary language.
     *
     * @return the secondaryLanguage
     */
    public String getSecondaryLanguage() {
	return secondaryLanguage;
    }

    /**
     * Sets the secondary language only if the language code can be found in
     * 2-letter language codes defined in ISO 639.
     *
     * @param secondaryLanguage
     *            the secondary language
     * @return true, if successful
     * @see <a href="http://en.wikipedia.org/wiki/ISO_639">http://en.wikipedia.
     *      org/wiki/ISO_639</a>
     */
    public boolean setSecondaryLanguage(String secondaryLanguage) {
	if (isValidISOLanguage(secondaryLanguage)) {
	    this.secondaryLanguage = secondaryLanguage;
	    return true;
	} else
	    return false;
    }

    /**
     * Checks if is valid ISO language.
     *
     * @param languageCodeToCheck
     *            the language code to check
     * @return true, if is valid iso language
     */
    private boolean isValidISOLanguage(String languageCodeToCheck) {
	List<String> isoLanguagesList = Arrays.asList(Locale.getISOLanguages());
	return isoLanguagesList.contains(languageCodeToCheck);
    }

    /**
     * Gets the preferred unit system.
     *
     * @return the preferred unit system
     */
    public UnitSystem getPreferredUnitSystem() {
	return preferredUnitSystem;
    }

    /**
     * Sets the preferred unit system.
     *
     * @param preferredUnitSystem
     *            the new preferred unit system
     */
    public void setPreferredUnitSystem(UnitSystem preferredUnitSystem) {
	this.preferredUnitSystem = preferredUnitSystem;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("preferredLanguage: ");
	buffer.append(preferredLanguage);
	buffer.append("\n");
	buffer.append("secondaryLanguage: ");
	buffer.append(secondaryLanguage);
	buffer.append("\n");
	buffer.append("preferredUnitSystem: ");
	buffer.append(preferredUnitSystem);
	buffer.append("\n");
	return buffer.toString();
    }

}
