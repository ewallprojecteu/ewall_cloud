/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d., Hewlett Packard Italiana SRL
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.service;

//import org.springframework.data.mongodb.core.index.Indexed;

import eu.ewall.platform.commons.datamodel.core.ieeesumo.Abstract;

/**
 * The Class Service.
 * 
 * @author eandgrg
 */
public class Service extends Abstract {

  /** The name. */
//  @Indexed(unique = true)
  private String name;

  /**
   * Instantiates a new service.
   */
  private Service() {
  }

  /**
   * Instantiates a new service.
   *
   * @param name
   *          the name
   */
  public Service(String name) {
    this.name = name;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Service name: ");
    buffer.append(name);
    buffer.append("\n");
    return buffer.toString();
  }

}
