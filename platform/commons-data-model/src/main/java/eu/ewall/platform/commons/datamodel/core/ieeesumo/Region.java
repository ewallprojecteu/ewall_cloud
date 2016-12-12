package eu.ewall.platform.commons.datamodel.core.ieeesumo;

//import org.springframework.data.mongodb.core.index.Indexed;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 * Copyright 2015 Hewlett Packard Italiana SRL
 ******************************************************************************/

/**
 * A topographic location. Regions encompass surfaces of Objects, imaginary places, and
 * GeographicAreas. Note that a Region is the only kind of Object which can be located at itself.
 * Note too that Region is not a subclass of SelfConnectedObject, because some Regions, e.g.
 * archipelagos, have parts which are not connected with one another.
 * 
 * In the context of eWALL Region comprises very basic information to define an operational area. We
 * assume for eWALL a geographical area, but in principle the meaning might be different (e.g. might
 * define organizational boundaries, etc.).
 * 
 * @author eandgrg
 */
public class Region extends Object {

  /** The region (unique) name. */
  // @Indexed(unique = true)
  private String regionName;

  /** The country. */
  private String country;

  /**
   * The Constructor.
   */
  public Region() {

  }

  /**
   * Gets the region (unique) name.
   *
   * @return the region name
   */
  public String getRegionName() {
    return regionName;
  }

  /**
   * Sets the region (unique) name.
   *
   * @param regionName
   *          the new region name
   */
  public void setRegionName(String regionName) {
    this.regionName = regionName;
  }

  /**
   * Gets the country.
   *
   * @return the country
   */
  public String getCountry() {
    return country;
  }

  /**
   * Sets the country.
   *
   * @param country
   *          the new country
   */
  public void setCountry(String country) {
    this.country = country;
  }

  /**
   * Instantiates a new region.
   *
   * @param regionName
   *          the region name
   * @param country
   *          the country
   */
  public Region(String regionName, String country) {
    this.regionName = regionName;
    this.country = country;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    StringBuffer buffer = new StringBuffer();
    buffer.append("------Region start------\n");
    buffer.append("regionName: ");
    buffer.append(regionName);
    buffer.append("\n");
    buffer.append("country: ");
    buffer.append(country);
    buffer.append("\n");
    buffer.append("------Region end------\n");
    return buffer.toString();
  }

}