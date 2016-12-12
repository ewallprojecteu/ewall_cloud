package eu.ewall.platform.commons.datamodel.profile;

import java.util.ArrayList;
import java.util.List;

//import org.springframework.data.mongodb.core.index.Indexed;
import eu.ewall.platform.commons.datamodel.core.ieeesumo.Agent;

/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
/**
 * Representation of a eWall user. Contains a connection with user role and user profile containing
 * different profiles user might have including vcard profile for most common representation of the
 * user specific data.
 * 
 * @author eandgrg
 */
public class User extends Agent {

  /** The first name. */
  private String firstName;

  /** The last name. */
  private String lastName;

  /** unique username in the system. */
  // @Indexed(unique = true)
  private String username;

  /** The has profile. */
  private UserProfile userProfile;

  /** The role. */
  private UserRole userRole;

  /** The applications. */
  // @DBRef
  private List<String> applicationNamesList;

  /** The caregivers list. */
  // @DBRef
  private List<String> caregiversUsernamesList;

  /**
   * The default Constructor.
   */
  public User() {
    this.applicationNamesList = new ArrayList<String>();
    this.caregiversUsernamesList = new ArrayList<String>();

  }

  /**
   * Instantiates a new user.
   *
   * @param firstName
   *          the first name
   * @param lastName
   *          the last name
   * @param username
   *          the username
   * @param userProfile
   *          the user profile
   * @param userRole
   *          the user role
   */
  public User(String firstName, String lastName, String username, UserProfile userProfile,
      UserRole userRole) {
    this();
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.userProfile = userProfile;
    this.userRole = userRole;

  }

  /**
   * Gets the application names list.
   *
   * @return the application names list
   */
  public List<String> getApplicationNamesList() {
    return applicationNamesList;
  }

  /**
   * Sets the application names list.
   *
   * @param applicationNamesList the new application names list
   */
  public void setApplicationNamesList(List<String> applicationNamesList) {
    this.applicationNamesList = applicationNamesList;
  }

  /**
   * Adds the application name to application names list.
   *
   * @param applicationName
   *          the application name
   */
  public void addApplicationNameToApplicationNamesList(String applicationName) {
    this.applicationNamesList.add(applicationName);
  }

  /**
   * Gets the caregivers usernames list.
   *
   * @return the caregivers usernames list
   */
  public List<String> getCaregiversUsernamesList() {
    return caregiversUsernamesList;
  }

/**
 * Sets the caregivers usernames list.
 *
 * @param caregiversUsernamesList the new caregivers usernames list
 */
  public void setCaregiversUsernamesList(List<String> caregiversUsernamesList) {
    this.caregiversUsernamesList = caregiversUsernamesList;
  }

  /**
   * Adds the caregiver username to caregivers usernames list.
   *
   * @param caregiverUsername
   *          the caregiver username
   */
  public void addCaregiverUsernameToCaregiversUsernamesList(String caregiverUsername) {
    this.caregiversUsernamesList.add(caregiverUsername);
  }
  
  /**
   * Removes caregiver name from encompassing caregivers names list.
   *
   * @param caregiverNameToRemove
   *          the caregiver name to remove
   * @return true, if successful
   */
  public boolean removeCaregiverNameFromEncompassingCaregiverNamesList(
      String caregiverNameToRemove) {
    return caregiversUsernamesList.remove(caregiverNameToRemove);
  }

  /**
   * Removes the application name from encompassing application names list.
   *
   * @param applicationNameToRemove
   *          the application name to remove
   * @return true, if successful
   */
  public boolean removeApplicationNameFromEncompassingApplicationNamesList(
      String applicationNameToRemove) {
    return applicationNamesList.remove(applicationNameToRemove);
  }

  /**
   * Gets the first name.
   *
   * @return the first name
   */
  public String getFirstName() {
    return firstName;
  }

  /**
   * Sets the first name.
   *
   * @param firstName
   *          the first name
   */
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * Gets the last name.
   *
   * @return the last name
   */
  public String getLastName() {
    return lastName;
  }

  /**
   * Sets the last name.
   *
   * @param lastName
   *          the last name
   */
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * Gets the username.
   *
   * @return the username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username.
   *
   * @param username
   *          the username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the user profile.
   *
   * @return the userProfile
   */
  public UserProfile getUserProfile() {
    return userProfile;
  }

  /**
   * Sets the user profile.
   *
   * @param userProfile
   *          the userProfile to set
   */
  public void setUserProfile(UserProfile userProfile) {
    this.userProfile = userProfile;
  }

  /**
   * Gets the UserRole.
   *
   * @return the checks for role
   */
  public UserRole getUserRole() {
    return userRole;
  }

  /**
   * Sets the UserRole.
   *
   * @param hasRole
   *          the user role
   */
  public void setUserRole(UserRole hasRole) {
    this.userRole = hasRole;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {

    StringBuffer buffer = new StringBuffer();
    buffer.append("firstName: ");
    buffer.append(firstName);
    buffer.append("\n");
    buffer.append("lastName: ");
    buffer.append(lastName);
    buffer.append("\n");
    buffer.append("username: ");
    buffer.append(username);
    buffer.append("\n");
    buffer.append("userProfile:\n ");
    if (userProfile != null)
      buffer.append(userProfile.toString());
    else
      buffer.append("empty");
    buffer.append("\n");
    buffer.append("userRole: ");
    buffer.append(userRole);
    buffer.append("\n");

    StringBuilder rString = new StringBuilder();
    rString.append(buffer);
    if (!applicationNamesList.isEmpty()) {
      rString.append("******User's applications list start******\n");
      for (String each : applicationNamesList) {
        rString.append(each.toString()).append("\n");
      }
      rString.append("******User's applications list end******");
    }

    if (!caregiversUsernamesList.isEmpty()) {
      rString.append("******User's caregivers list start******\n");
      for (String each : caregiversUsernamesList) {
        rString.append(each.toString()).append("\n");
      }
      rString.append("******User's caregivers list end******");
    }
    return rString.toString();
  }

}