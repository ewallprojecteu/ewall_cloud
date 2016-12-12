/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/

package eu.ewall.platform.commons.datamodel.sensing;

/**
 * The Class SpeakerSensing. From on  T3.5 description: This class represents "the output of the speaker diarisation algorithm. 
 * This algorithm processes segments of some predefined duration and returns the number of different speakers. 
 * The  elements are as follows:
 * - timestamp: The string with the time the segment begins, including time zone information
 * - duration: The duration of the audio segment in ms.
 * - speakerNo: The number of speakers detected
 * - microphone: A string describing the microphone utilised
Such a message is sent asynchronously, every time the system processes a segment of audio."
 * 
 * @author EMIRMOS
 */
public class SpeakerSensing extends Sensing {

	private long duration;
	
	private int speakerNo;
	
	private String microphone;

	
	
	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the speakerNo
	 */
	public int getSpeakerNo() {
		return speakerNo;
	}

	/**
	 * @param speakerNo the speakerNo to set
	 */
	public void setSpeakerNo(int speakerNo) {
		this.speakerNo = speakerNo;
	}

	/**
	 * @return the microphone
	 */
	public String getMicrophone() {
		return microphone;
	}

	/**
	 * @param microphone the microphone to set
	 */
	public void setMicrophone(String microphone) {
		this.microphone = microphone;
	}
	
	

}
