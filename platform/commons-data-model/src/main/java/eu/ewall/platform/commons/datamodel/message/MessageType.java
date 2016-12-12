/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.message;

/**
 * The Enum MessageType.
 *
 * @author eandgrg
 */
public enum MessageType {

	/** The normal. */
	NORMAL,

	/** The motivational. */
	MOTIVATIONAL,

	/** The error. */
	ERROR,

	/** The headline. */
	HEADLINE,

	/** The reminder. */
	REMINDER,

	/** The educational. */
	EDUCATIONAL,

	/** The explanation. */
	EXPLANATION,

	/** The inquiry. */
	INQUIRY,

	/** The questionnaire. */
	QUESTIONNAIRE,

	/** The chat. */
	CHAT,

	/** The facebook. */
	FACEBOOK,

	/** The twitter. */
	TWITTER,

	/** The email. */
	EMAIL,

	/** The groupchat. */
	GROUPCHAT,
	
	
	// NOTIFICATION RELATED TYPES
	
	/** Eating reminder. */
	REMINDER_EAT,
	
	/** Drinking reminder. */
	REMINDER_DRINK,
	
	/** Sleeping reminder. */
	REMINDER_SLEEP,
	
	/** Showering reminder. */
	REMINDER_SHOWER,
	
	/** Physical exercise reminder. */
	REMINDER_PHYSICALEXERCISE,
	
	/** Cognitive exercise reminder. */
	REMINDER_COGNITIVEEXERCISE,
	
	/** Inactivity reminder. */
	REMINDER_INACTIVITY,
	
	/** Medicine intakes reminder. */
	REMINDER_MEDICINEINTAKE,
	
	/** Vital signs measurement reminder. */
	REMINDER_VITALSIGNS,
	
	/** Home environment reminder. */
	REMINDER_HOME,

	/** Eating recommendation. */
	RECOMMENDATION_EAT,
	
	/** Drinking recommendation. */
	RECOMMENDATION_DRINK,
	
	/** Sleeping recommendation. */
	RECOMMENDATION_SLEEP,
	
	/** Showering recommendation. */
	RECOMMENDATION_SHOWER,
	
	/** Physical exercise recommendation. */
	RECOMMENDATION_PHYSICALEXERCISE,
	
	/** Cognitive exercise recommendation. */
	RECOMMENDATION_COGNITIVEEXERCISE,
	
	/** Vital signs measurement recommendation. */
	RECOMMENDATION_VITALSIGNS,
	
	/** Home environment recommendation. */
	RECOMMENDATION_HOME,
	
	/** Socializing recommendation. */
	RECOMMENDATION_SOCIAL,
	
	/** Progress report recommendation. */
	RECOMMENDATION_PROGRESSREPORT,

	/** Medical appointment. */
	APPOINTMENT_MEDICAL,
	
	/** Social recommendation. */
	APPOINTMENT_SOCIAL,

}
