/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile;

/**
 * The Enum EmotionalStateCategory based on 24 FSRE categories that were used in
 * the study by Fontaine, Scherer, Roesch and Ellsworth (Fontaine et al., 2007,
 * p. 1055) investigating the dimensionality of emotion space.
 * 
 * and in addition some new ones (specifically added for eWall):
 * SOCIAL_AVOIDANCE, FEAR_OF_FALLING
 * 
 * Also published in W3C working group note on 1 April 2014
 * <a href="http://www.w3.org/TR/emotion-voc/#fsre-categories" >http://www.w3.
 * org/TR/emotion-voc/#fsre-categories</a> as 1 of possible 4 vocabulary
 * categories for Emotion Markup Language (EmotionML) published as W3C
 * Recommendation 22 May 2014
 * <a href="http://www.w3.org/TR/emotionml/" >http://www.w3.org/TR/emotionml/
 * </a>
 * 
 * @author eandgrg, emirmos
 */
public enum EmotionalStateCategory {

	/** The anger. */
	ANGER,
	/** The anxiety. */
	ANXIETY,
	/** The being hurt. */
	BEING_HURT,
	/** The compassion. */
	COMPASSION,
	/** The contempt. */
	CONTEMPT,
	/** The contentment. */
	CONTENTMENT,
	/** The despair. */
	DESPAIR,
	/** The disappointment. */
	DISAPPOINTMENT,
	/** The disgust. */
	DISGUST,
	/** The fear. */
	FEAR,
	/**
	 * The fear of falling was not part of the 24 FSRE categories but was added
	 * upon request.
	 */
	FEAR_OF_FALLING,
	/** The guilt. */
	GUILT,
	/** The happiness. */
	HAPPINESS,
	/** The hate. */
	HATE,
	/** The interest. */
	INTEREST,
	/** The irritation. */
	IRRITATION,
	/** The jealousy. */
	JEALOUSY,
	/** The joy. */
	JOY,
	/** The love. */
	LOVE,
	/** The pleasure. */
	PLEASURE,
	/** The pride. */
	PRIDE,
	/** The sadness. */
	SADNESS,
	/** The shame. */
	SHAME,
	/**
	 * The social avoidance was not part of the 24 FSRE categories but was added
	 * upon request.
	 */
	SOCIAL_AVOIDANCE,
	/** The stress. */
	STRESS,
	/** The surprise. */
	SURPRISE,
	/** The neutral. */
	NEUTRAL,
	/** The unknown. */
	UNKNOWN

}
