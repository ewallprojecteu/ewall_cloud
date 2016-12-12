package eu.ewall.platform.idss.service.model.state.context;

import eu.ewall.platform.idss.dao.AbstractDatabaseObject;
import eu.ewall.platform.idss.dao.DatabaseField;
import eu.ewall.platform.idss.dao.DatabaseType;
import eu.ewall.platform.idss.service.model.type.LocationType;

/**
 * This class models a location.
 * 
 * @author Dennis Hofs (RRD)
 */
public class Location extends AbstractDatabaseObject {

	@DatabaseField(value= DatabaseType.STRING)
	private LocationType type;
	
	@DatabaseField(value=DatabaseType.FLOAT)
	private double latitude;

	@DatabaseField(value=DatabaseType.FLOAT)
	private double longitude;

	/**
	 * Returns the location type.
	 * 
	 * @return the location type
	 */
	public LocationType getType() {
		return type;
	}

	/**
	 * Sets the location type.
	 * 
	 * @param type the location type
	 */
	public void setType(LocationType type) {
		this.type = type;
	}

	/**
	 * Returns the latitude in degrees.
	 * 
	 * @return the latitude in degrees
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * Sets the latitude in degrees.
	 * 
	 * @param latitude the latitude in degrees
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	/**
	 * Returns the longitude in degrees.
	 * 
	 * @return the longitude in degrees
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * Sets the longitude in degrees.
	 * 
	 * @param longitude the longitude in degrees
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
