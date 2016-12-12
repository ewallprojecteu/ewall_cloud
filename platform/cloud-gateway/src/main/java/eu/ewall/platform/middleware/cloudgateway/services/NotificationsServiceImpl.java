/*******************************************************************************
 * Copyright 2014 Ericsson Nikola Tesla d.d.
 ******************************************************************************/
package eu.ewall.platform.middleware.cloudgateway.services;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import eu.ewall.platform.commons.datamodel.ewallsystem.SensingEnvironment;
import eu.ewall.platform.commons.datamodel.message.Notification;
import eu.ewall.platform.commons.datamodel.profile.User;
import eu.ewall.platform.middleware.cloudgateway.dao.NotificationDAO;
import eu.ewall.platform.middleware.datamanager.dao.profile.UserDao;

/**
 * The Class DevicesServiceImpl.
 *
 * @author emirmos
 */
@Service("notificationsService")
public class NotificationsServiceImpl {

	/** The log. */
	private static Logger LOG = LoggerFactory
			.getLogger(NotificationsServiceImpl.class);

	/** The notification dao. */
	@Autowired
	NotificationDAO notificationDAO;
	
	/** The sensing environments service. */
	@Autowired
	private SensingEnvironmentsServiceImpl sensingEnvironmentsService;
	
	/** The user dao. */
	private UserDao userDao;
	
	/**
	 * Instantiates a new notifications service impl.
	 */
	public NotificationsServiceImpl() {
		userDao = new UserDao();
	}

	/**
	 * Post notification.
	 *
	 * @param notification the notification
	 * @param sensingEnvironmentId the sensing environment id
	 * @return true, if successful
	 */
	public boolean postNotification(Notification notification, UUID sensingEnvironmentId) {
		
		if (notification.getPrimaryUser() == null) {
			SensingEnvironment sensingEnvironment = sensingEnvironmentsService.getSensingEnvironment(sensingEnvironmentId);
			
			if (sensingEnvironment != null) {
				notification.setPrimaryUser(sensingEnvironment.getPrimaryUser());
			} else {
				LOG.warn("Notification primary user is null and sensing enviroment with id "+sensingEnvironmentId+" not found. Notification not send!");
				return false;
			}
				
		}
	
		notificationDAO.postNotification(notification);
		return true;
	}
	
	/**
	 * Post caregiver notification.
	 *
	 * @param notification the notification
	 * @param sensingEnvironmentId the sensing environment id
	 * @return true, if successful
	 */
	public boolean postCaregiverNotification(Notification notification, UUID sensingEnvironmentId) {
		
		if (notification.getPrimaryUser() == null) {
			SensingEnvironment sensingEnvironment = sensingEnvironmentsService.getSensingEnvironment(sensingEnvironmentId);
			
			if (sensingEnvironment != null) {
				notification.setPrimaryUser(sensingEnvironment.getPrimaryUser());
				
				User user = userDao.getEWallUserByUsername(sensingEnvironment.getPrimaryUser());
				if (user != null) {
				for (String caregiver : user.getCaregiversUsernamesList()) {
					notification.setCaregiver(caregiver);
					notificationDAO.postCaregiverNotification(notification);
				}
				}
				else {
					LOG.warn("User object "+sensingEnvironment.getPrimaryUser()+" not found. Cannot obatin user caregivers list. Notifications to caregivers not send.");
					return false;
				}
					
				
				
			} else {
				LOG.warn("Notification primary user is null and sensing enviroment with id "+sensingEnvironmentId+" not found. Notification not send!");
				return false;
			}
				
		}
	
	
		return true;
	}
}
