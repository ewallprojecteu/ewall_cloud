package eu.ewall.platform.commons.datamodel.message;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * The Class Notification.
 */
public class Notification {


	/** The creation time. */
	private long creationTime;
	
	/** The username. */
	private String primaryUser;
	
	/** The caregiver. */
	private String caregiver;
	
	/** The notification type. */
	private NotificationType notificationType;
	
	/** The priority. */
	private NotificationPriority priority;
	
	/** The title. */
	private String title;
	
	/** The content. */
	private String content;

	
	/**
	 * Instantiates a new notification.
	 */
	public Notification() {
		super();
	}
	
	/**
	 * Gets the creation time.
	 *
	 * @return the creationTime
	 */
	public long getCreationTime() {
		return creationTime;
	}
	
	/**
	 * Sets the creation time.
	 *
	 * @param creationTime the creationTime to set
	 */
	public void setCreationTime(long creationTime) {
		this.creationTime = creationTime;
	}
	
	
	/**
	 * Gets the notification type.
	 *
	 * @return the notificationType
	 */
	public NotificationType getNotificationType() {
		return notificationType;
	}
	
	/**
	 * Sets the notification type.
	 *
	 * @param notificationType the notificationType to set
	 */
	public void setNotificationType(NotificationType notificationType) {
		this.notificationType = notificationType;
	}
	
	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public NotificationPriority getPriority() {
		return priority;
	}
	
	/**
	 * Sets the priority.
	 *
	 * @param priority the priority to set
	 */
	public void setPriority(NotificationPriority priority) {
		this.priority = priority;
	}

	/**
	 * Instantiates a new notification.
	 *
	 * @param notificationType the notification type
	 * @param priority the priority
	 * @param title the title
	 * @param content the content
	 */
	public Notification(NotificationType notificationType,
			NotificationPriority priority, String title, String content) {
		super();
		this.notificationType = notificationType;
		this.priority = priority;
		this.title = title;
		this.content = content;
		this.creationTime = System.currentTimeMillis();
	}
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}
	
	/**
	 * Sets the title.
	 *
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	
	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return content;
	}
	
	/**
	 * Sets the content.
	 *
	 * @param content the content to set
	 */
	public void setContent(String content) {
		try {
			//encoding to url is needed because Notification Manager receives content via url parameter (this needs to be changed) and thus unsupported characters may appear
			this.content = URLEncoder.encode(content, "UTF-8");;
		} catch (UnsupportedEncodingException e) {
			this.content="";
		}
	}
	

	/**
	 * Gets the caregiver.
	 *
	 * @return the caregiver
	 */
	public String getCaregiver() {
		return caregiver;
	}

	/**
	 * Sets the caregiver.
	 *
	 * @param caregiver the caregiver to set
	 */
	public void setCaregiver(String caregiver) {
		this.caregiver = caregiver;
	}

	/**
	 * @return the primaryUser
	 */
	public String getPrimaryUser() {
		return primaryUser;
	}

	/**
	 * @param primaryUser the primaryUser to set
	 */
	public void setPrimaryUser(String primaryUser) {
		this.primaryUser = primaryUser;
	}
	
	
	
	
}
