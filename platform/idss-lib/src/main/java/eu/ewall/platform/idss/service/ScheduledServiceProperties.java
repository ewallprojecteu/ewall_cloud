package eu.ewall.platform.idss.service;

import eu.ewall.platform.idss.utils.AppComponent;

import eu.ewall.platform.idss.utils.datetime.VirtualClock;

/**
 * This class defines default properties for a {@link ScheduledService
 * ScheduledService}. It can be used as an {@link AppComponent AppComponent},
 * so you can configure the properties for all scheduled services at once.
 * A scheduled service should use these properties unless their individual
 * configuration overrides them.
 * 
 * @author Dennis Hofs (RRD)
 */
@AppComponent
public class ScheduledServiceProperties {
	private static final int DEFAULT_INTERVAL = 300000; // ms
	
	private static ScheduledServiceProperties instance = null;
	private static Object lock = new Object();
	
	/**
	 * Returns the scheduled service properties.
	 * 
	 * @return the scheduled service properties
	 */
	public static ScheduledServiceProperties getInstance() {
		synchronized (lock) {
			if (instance == null)
				instance = new ScheduledServiceProperties();
			return instance;
		}
	}
	
	private int taskInterval = DEFAULT_INTERVAL;
	
	/**
	 * This private constructor is used in {@link #getInstance()
	 * getInstance()}.
	 */
	private ScheduledServiceProperties() {
	}
	
	/**
	 * Returns the interval in milliseconds to wait between running the task of
	 * a scheduled service. The default is 5 minutes.
	 * 
	 * <p>If a {@link VirtualClock VirtualClock} is used, the task interval is
	 * interpreted as system time, not virtual time. This way the virtual
	 * clock can run at high speed, without causing high CPU load through
	 * very frequent task runs.</p>
	 * 
	 * @return the task interval
	 */
	public int getTaskInterval() {
		return taskInterval;
	}
	
	/**
	 * Sets the interval in milliseconds to wait between running the task of a
	 * scheduled service. The default is 5 minutes.
	 * 
	 * <p>If a {@link VirtualClock VirtualClock} is used, the task interval is
	 * interpreted as system time, not virtual time. This way the virtual
	 * clock can run at high speed, without causing high CPU load through
	 * very frequent task runs.</p>
	 * 
	 * @param taskInterval the task interval
	 */
	public void setTaskInterval(int taskInterval) {
		this.taskInterval = taskInterval;
	}
}
