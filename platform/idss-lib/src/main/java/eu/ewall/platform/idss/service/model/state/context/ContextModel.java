package eu.ewall.platform.idss.service.model.state.context;

import org.joda.time.DateTime;

import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.state.StateModel;

/**
 * This model defines the state of the context for a user. When you set an
 * attribute, you should also set the source reliability and update time. This
 * can be done at once using {@link
 * #setAttribute(String, Object, Double, DateTime) setAttribute()} from {@link
 * StateModel StateModel}.
 * 
 * @author Dennis Hofs (RRD)
 */
public class ContextModel extends StateModel {
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private Weather currentWeather = null;

	@DatabaseField(value=DatabaseType.OBJECT)
	private Weather todayWeather = null;
	
	@DatabaseField(value=DatabaseType.OBJECT)
	private Location location = null;
	
	@DatabaseField(value=DatabaseType.INT)
	private Boolean isHoliday = null;

	/**
	 * Returns the current weather.
	 * 
	 * @return the current weather
	 */
	public Weather getCurrentWeather() {
		return currentWeather;
	}

	/**
	 * Sets the current weather.
	 * 
	 * @param currentWeather the current weather
	 */
	public void setCurrentWeather(Weather currentWeather) {
		this.currentWeather = currentWeather;
	}

	/**
	 * Returns the weather forecast for today.
	 * 
	 * @return the weather forecast for today
	 */
	public Weather getTodayWeather() {
		return todayWeather;
	}

	/**
	 * Sets the weather forecast for today.
	 * 
	 * @param todayWeather the weather forecast for today
	 */
	public void setTodayWeather(Weather todayWeather) {
		this.todayWeather = todayWeather;
	}

	/**
	 * Returns the location.
	 * 
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Sets the location.
	 * 
	 * @param location the location
	 */
	public void setLocation(Location location) {
		this.location = location;
	}

	/**
	 * Returns whether today is a public holiday.
	 * 
	 * @return true if today is a public holiday
	 */
	public Boolean getIsHoliday() {
		return isHoliday;
	}

	/**
	 * Sets whether today is a public holiday.
	 * 
	 * @param isHoliday true if today is a public holiday
	 */
	public void setIsHoliday(Boolean isHoliday) {
		this.isHoliday = isHoliday;
	}
}
