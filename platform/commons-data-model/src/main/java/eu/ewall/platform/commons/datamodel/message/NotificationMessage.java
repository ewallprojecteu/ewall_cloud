package eu.ewall.platform.commons.datamodel.message;

public class NotificationMessage {
	
	private String user;
	private String date;
	private String time;
	private String type;
	private String title;
	private NotificationContentMsg content;
	private double priority;
	private String source;
	private String notificationID;
	
	public String getUser()
	{
		return user;
	}
	
	public void setUser(String user)
	{
		this.user = user;
	}
	
	public String getDate()
	{
		return date;
	}
	
	public void setDate(String date)
	{
		this.date = date;
	}
	
	public String getTime()
	{
		return time;
	}
	
	public void setTime(String time)
	{
		this.time = time;
	}
	
	public String getType()
	{
		return type;
	}
	
	public void setType(String type)
	{
		this.type = type;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public NotificationContentMsg getContent()
	{
		return content;
	}
	
	public void setContent(NotificationContentMsg content)
	{
		this.content = content;
	}
	
	public double getPriority()
	{
		return priority;
	}
	
	public void setPriority(double priority)
	{
		this.priority = priority;
	}
	
	public String getSource()
	{
		return source;
	}
	
	public void setSource(String source)
	{
		this.source = source;
	}
	
	public String getNotificationID()
	{
		return notificationID;
	}
	
	public void setNotificationID(String notificationID)
	{
		this.notificationID = notificationID;
	}

}
