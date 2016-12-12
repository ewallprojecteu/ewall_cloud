/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d., Hewlett Packard Italiana SRL
 ******************************************************************************/
package eu.ewall.platform.commons.datamodel.service;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.data.mongodb.core.index.Indexed;
import eu.ewall.platform.commons.datamodel.core.ieeesumo.Abstract;

/**
 * The Class Application.
 * 
 * @author eandgrg
 */
public class Application extends Abstract {

  /** The name. */
  // @Indexed(unique = true)
  private String name;

  /** The type. */
  private ApplicationType type;

  /** The services. */
  private List<String> encompassingServiceNames;

  /**
   * Initilize encompassing service names list.
   */
  private void initilizeEncompassingServiceNamesList() {
    if (encompassingServiceNames == null)
      encompassingServiceNames = new ArrayList<String>();
  }

  /**
   * Sets the encompassing service names.
   *
   * @param encompassingServiceNames
   *          the new encompassing service names
   */
  public void setEncompassingServiceNames(List<String> encompassingServiceNames) {
    this.encompassingServiceNames = encompassingServiceNames;
  }

  /**
   * Default constructor.
   */
  public Application() {
    initilizeEncompassingServiceNamesList();
  }

  /**
   * Instantiates a new application.
   *
   * @param name
   *          the name
   */
  public Application(String name) {
    initilizeEncompassingServiceNamesList();
    this.name = name;
  }

  /**
   * Instantiates a new application.
   *
   * @param name
   *          the name
   * @param type
   *          the type
   */
  public Application(String name, ApplicationType type) {
    initilizeEncompassingServiceNamesList();
    this.name = name;
    this.type = type;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public ApplicationType getType() {
    return type;
  }

  /**
   * Sets the type.
   *
   * @param type
   *          the new type
   */
  public void setType(ApplicationType type) {
    this.type = type;
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

  /**
   * Gets the encompassing service names.
   *
   * @return the encompassing service names
   */
  public List<String> getEncompassingServiceNames() {
    return encompassingServiceNames;
  }

  /**
   * Adds the service name to the list of service names belonging to the application.
   *
   * @param serviceNameToAdd
   *          the service to add
   * @return true, if service was added to the services list
   */
  public boolean addServiceNameToEncompassingServiceNamesList(String serviceNameToAdd) {
    return encompassingServiceNames.add(serviceNameToAdd);
  }

  /**
   * Removes the service name from encompassing service names list.
   *
   * @param serviceNameToRemove
   *          the service name to remove
   * @return true, if successful
   */
  public boolean removeServiceNameFromEncompassingServiceNamesList(String serviceNameToRemove) {
    return encompassingServiceNames.remove(serviceNameToRemove);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("Application name: ");
    buffer.append(name);
    buffer.append("\n");

    buffer.append("Application type: ");
    buffer.append(type);
    buffer.append("\n");

    StringBuilder rString = new StringBuilder();
    rString.append(buffer);
    if (!encompassingServiceNames.isEmpty()) {
      rString.append("******Application " + name + " service list start******\n");
      for (String each : encompassingServiceNames) {
        rString.append(each.toString()).append("\n");
      }
      rString.append("******Application " + name + " service list end******");
    }
    return rString.toString();

  }
}
