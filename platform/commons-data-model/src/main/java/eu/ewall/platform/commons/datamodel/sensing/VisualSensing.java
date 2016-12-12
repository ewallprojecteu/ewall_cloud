/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/


package eu.ewall.platform.commons.datamodel.sensing;

import eu.ewall.platform.commons.datamodel.profile.EmotionalStateCategory;
import eu.ewall.platform.commons.datamodel.profile.VCardGenderType;


/**
 * The Class VisualSensing.
 * 
 * @author EMIRMOS
 */
public class VisualSensing extends Sensing {
	
	/** The person_id. */
	private int track_id;
	
	/** The x. */
	private int x;
	
	/** The y. */
	private int y;
	
	/** The width. */
	private int width;
	
	/** The height. */
	private int height;
	
	/** The position conf. */
	private double positionConf;
	
	/** The gender. */
	private VCardGenderType gender;
	
	/** The gender conf. */
	private double genderConf;
	
	/** The age. */
	private int age;
	
	/** The age conf. */
	private double ageConf;
	
	/** The emotion. */
	private EmotionalStateCategory emotion;
	
	/** The emotion conf. */
	private double emotionConf;
	
	/**
	 * Gets the track_id.
	 *
	 * @return the track_id
	 */
	public int getTrack_id() {
		return track_id;
	}
	
	/**
	 * Sets the track_id.
	 *
	 * @param track_id the track_id to set
	 */
	public void setTrack_id(int track_id) {
		this.track_id = track_id;
	}
	
	/**
	 * Gets the x.
	 *
	 * @return the x
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Sets the x.
	 *
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}
	
	/**
	 * Gets the y.
	 *
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Sets the y.
	 *
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}
	
	/**
	 * Gets the width.
	 *
	 * @return the width
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Sets the width.
	 *
	 * @param width the width to set
	 */
	public void setWidth(int width) {
		this.width = width;
	}
	
	/**
	 * Gets the height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Sets the height.
	 *
	 * @param height the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}
	
	/**
	 * Gets the position conf.
	 *
	 * @return the positionConf
	 */
	public double getPositionConf() {
		return positionConf;
	}
	
	/**
	 * Sets the position conf.
	 *
	 * @param positionConf the positionConf to set
	 */
	public void setPositionConf(double positionConf) {
		this.positionConf = positionConf;
	}
	
	/**
	 * Gets the gender.
	 *
	 * @return the gender
	 */
	public VCardGenderType getGender() {
		return gender;
	}
	
	/**
	 * Sets the gender.
	 *
	 * @param gender the gender to set
	 */
	public void setGender(VCardGenderType gender) {
		this.gender = gender;
	}
	
	/**
	 * Gets the gender conf.
	 *
	 * @return the genderConf
	 */
	public double getGenderConf() {
		return genderConf;
	}
	
	/**
	 * Sets the gender conf.
	 *
	 * @param genderConf the genderConf to set
	 */
	public void setGenderConf(double genderConf) {
		this.genderConf = genderConf;
	}
	
	/**
	 * Gets the age.
	 *
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
	
	/**
	 * Sets the age.
	 *
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
	/**
	 * Gets the emotion.
	 *
	 * @return the emotion
	 */
	public EmotionalStateCategory getEmotion() {
		return emotion;
	}
	
	/**
	 * Sets the emotion.
	 *
	 * @param emotion the emotion to set
	 */
	public void setEmotion(EmotionalStateCategory emotion) {
		this.emotion = emotion;
	}
	
	/**
	 * Gets the emotion conf.
	 *
	 * @return the emotionConf
	 */
	public double getEmotionConf() {
		return emotionConf;
	}
	
	/**
	 * Sets the emotion conf.
	 *
	 * @param emotionConf the emotionConf to set
	 */
	public void setEmotionConf(double emotionConf) {
		this.emotionConf = emotionConf;
	}

	/**
	 * Gets the age conf.
	 *
	 * @return the ageConf
	 */
	public double getAgeConf() {
		return ageConf;
	}

	/**
	 * Sets the age conf.
	 *
	 * @param ageConf the ageConf to set
	 */
	public void setAgeConf(double ageConf) {
		this.ageConf = ageConf;
	}
	
	
	

}
