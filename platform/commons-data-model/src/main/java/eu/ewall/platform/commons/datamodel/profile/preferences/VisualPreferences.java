/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.profile.preferences;

import eu.ewall.platform.commons.datamodel.profile.Intensity;

/**
 * The Class VisualPreferences.
 *
 * @author eandgrg
 */
public class VisualPreferences extends Preferences {

	/** The content contrast. */
	private Intensity contentContrast;

	/** The flashing resources. */
	private Status flashingResources;

	/** The font size. */
	private Size fontSize;

	/** The screen resolution. */
	private Intensity screenResolution;

	/** The brightness. */
	private Intensity brightness;

	/** The component spacing. */
	private Intensity componentSpacing;

	/**
	 * The Constructor.
	 */
	public VisualPreferences() {

	}

	/**
	 * The Constructor.
	 *
	 * @param contentContrast
	 *            the content contrast
	 * @param flashingResources
	 *            the flashing resources
	 * @param fontSize
	 *            the font size
	 * @param screenResolution
	 *            the screen resolution
	 * @param brightness
	 *            the brightness
	 * @param componentSpacing
	 *            the component spacing
	 */
	public VisualPreferences(Intensity contentContrast,
			Status flashingResources, Size fontSize,
			Intensity screenResolution, Intensity brightness,
			Intensity componentSpacing) {
		this.contentContrast = contentContrast;
		this.flashingResources = flashingResources;
		this.fontSize = fontSize;
		this.screenResolution = screenResolution;
		this.brightness = brightness;
		this.componentSpacing = componentSpacing;
	}

	/**
	 * Gets the content contrast.
	 *
	 * @return the contentContrast
	 */
	public Intensity getContentContrast() {
		return contentContrast;
	}

	/**
	 * Sets the content contrast.
	 *
	 * @param contentContrast
	 *            the contentContrast to set
	 */
	public void setContentContrast(Intensity contentContrast) {
		this.contentContrast = contentContrast;
	}

	/**
	 * Gets the flashing resources.
	 *
	 * @return the flashingResources
	 */
	public Status getFlashingResources() {
		return flashingResources;
	}

	/**
	 * Sets the flashing resources.
	 *
	 * @param flashingResources
	 *            the flashingResources to set
	 */
	public void setFlashingResources(Status flashingResources) {
		this.flashingResources = flashingResources;
	}

	/**
	 * Gets the font size.
	 *
	 * @return the fontSize
	 */
	public Size getFontSize() {
		return fontSize;
	}

	/**
	 * Sets the font size.
	 *
	 * @param fontSize
	 *            the fontSize to set
	 */
	public void setFontSize(Size fontSize) {
		this.fontSize = fontSize;
	}

	/**
	 * Gets the screen resolution.
	 *
	 * @return the screenResolution
	 */
	public Intensity getScreenResolution() {
		return screenResolution;
	}

	/**
	 * Sets the screen resolution.
	 *
	 * @param screenResolution
	 *            the screenResolution to set
	 */
	public void setScreenResolution(Intensity screenResolution) {
		this.screenResolution = screenResolution;
	}

	/**
	 * Gets the brightness.
	 *
	 * @return the brightness
	 */
	public Intensity getBrightness() {
		return brightness;
	}

	/**
	 * Sets the brightness.
	 *
	 * @param brightness
	 *            the brightness to set
	 */
	public void setBrightness(Intensity brightness) {
		this.brightness = brightness;
	}

	/**
	 * Gets the component spacing.
	 *
	 * @return the componentSpacing
	 */
	public Intensity getComponentSpacing() {
		return componentSpacing;
	}

	/**
	 * Sets the component spacing.
	 *
	 * @param componentSpacing
	 *            the componentSpacing to set
	 */
	public void setComponentSpacing(Intensity componentSpacing) {
		this.componentSpacing = componentSpacing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("contentContrast: ");
		buffer.append(contentContrast);
		buffer.append("\n");
		buffer.append("flashingResources: ");
		buffer.append(flashingResources);
		buffer.append("\n");
		buffer.append("fontSize: ");
		buffer.append(fontSize);
		buffer.append("\n");
		buffer.append("screenResolution: ");
		buffer.append(screenResolution);
		buffer.append("\n");
		buffer.append("brightness: ");
		buffer.append(brightness);
		buffer.append("\n");
		buffer.append("componentSpacing: ");
		buffer.append(componentSpacing);
		buffer.append("\n");

		return buffer.toString();
	}

}
