/*******************************************************************************
 * Copyright 2015 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.profilingserver.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class UserAuthenticatorController. Only allowed components should be able to call these
 * methods.
 *
 * @author eandgrg
 */
@RestController
@RequestMapping(value = "/users")
public class UserAuthenticatorController {

  /** The log. */
  private static Logger LOG = LoggerFactory.getLogger(UserAuthenticatorController.class);

  /** The userAuthenticator service. */
  @Autowired
  private UserAuthenticatorService userAuthenticatorService;

  /**
   * Instantiates a new user controller.
   */
  public UserAuthenticatorController() {
  }

  /**
   * Adds the new user credentials.
   *
   * @param username
   *          the username
   * @param rawPassword
   *          the rawPassword
   * @return the response entity
   */
  @RequestMapping(value = "/{username}/credentials", method = RequestMethod.POST)
  public ResponseEntity<String> addNewUserCredentialsOrModifyPasswordForExistingUsername(
      @PathVariable("username") String username, @RequestBody String rawPassword) {

    LOG.info("Adding or modifying User Credentials (username=" + username + ", password="
        + rawPassword + ")");

    try {
      if (username.isEmpty() || rawPassword.isEmpty()) {
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);

      } else {
        userAuthenticatorService.addNewUserCredentialsOrModifyPasswordForExistingUsername(username,
            rawPassword);
        return new ResponseEntity<String>(HttpStatus.CREATED);
      }
    } catch (Exception e) {
      LOG.warn(e.getMessage());
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * Check if credentials exist (meaning username and password exist) for username.
   *
   * @param username
   *          the username
   * @return the response entity (OK if yes, NOT_FOUND if not)
   */
  @RequestMapping(value = "/{username}/credentials", method = RequestMethod.GET)
  public ResponseEntity<String> checkIfCredentialsExistForUsername(
      @PathVariable("username") String username) {

    LOG.debug("Getting information if credentials exist for User with username: " + username);

    try {
      if (username.isEmpty()) {
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
      } else {
        if (userAuthenticatorService.doCredentialsExistForUsername(username)) {
          return new ResponseEntity<String>(HttpStatus.OK);
        } else {
          return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }

      }
    } catch (Exception e) {
      LOG.warn(e.getMessage());
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * Delete user credentials for username.
   *
   * @param username
   *          the username
   * @return the response entity
   */
  @RequestMapping(value = "/{username}/credentials", method = RequestMethod.DELETE)
  public ResponseEntity<String> deleteUserCredentialsForUsername(
      @PathVariable("username") String username) {

    LOG.info("Deleting user credentials with username: " + username);

    try {
      if (userAuthenticatorService.deleteUserCredentialsForUsername(username))

        return new ResponseEntity<String>(HttpStatus.OK);
      else {
        return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
      }
    } catch (Exception e) {
      LOG.warn(e.getMessage());
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }

  }

  /**
   * Checks if User with given username and password is authenticated.
   *
   * @param username
   *          the username
   * @param providedRawPassword
   *          the provided raw password
   * @return the response entity
   */
  @RequestMapping(value = "/{username}/authenticate", method = RequestMethod.POST)
  public ResponseEntity<String> isAuthenticated(@PathVariable("username") String username,
      @RequestBody String providedRawPassword) {

    LOG.info("Checking if user with username=" + username + " and provided password="
        + providedRawPassword + " are ok.");

    try {
      if (username != null && providedRawPassword != null) {
        if (userAuthenticatorService.isAuthenticated(username, providedRawPassword) == false) {
          return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
        } else {
          return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        }
      } else
        return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    } catch (Exception e) {
      LOG.warn(e.getMessage());
      return new ResponseEntity<String>(HttpStatus.BAD_REQUEST);
    }
  }

}
