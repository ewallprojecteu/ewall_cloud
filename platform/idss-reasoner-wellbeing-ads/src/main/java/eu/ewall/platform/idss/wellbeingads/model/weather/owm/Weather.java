package eu.ewall.platform.idss.wellbeingads.model.weather.owm;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The Class Weather.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weather implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2535218520671905459L;
	
	/** The Weather condition id. */
	long id;
	
	/** The Group of weather parameters (Rain, Snow, Extreme etc.) . */
	String main;
	
	/** The Weather condition within the group. */
	String description;
	
	/** The Weather icon id. More on: http://openweathermap.org/weather-conditions */
	String icon;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(long id) {
		this.id = id;
	}
	
	/**
	 * Gets the main.
	 *
	 * @return the main
	 */
	public String getMain() {
		return main;
	}
	
	/**
	 * Sets the main.
	 *
	 * @param main the new main
	 */
	public void setMain(String main) {
		this.main = main;
	}
	
	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * Gets the icon.
	 *
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	
	/**
	 * Sets the icon.
	 *
	 * @param icon the new icon
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
}
