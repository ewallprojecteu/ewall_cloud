package eu.ewall.platform.idss.utils.log;

import eu.ewall.platform.idss.utils.datetime.VirtualClock;

import org.slf4j.Logger;

/**
 * This logger prepends every log message with the time of the {@link
 * VirtualClock VirtualClock} if it's running in virtual mode.
 * 
 * @author Dennis Hofs (RRD)
 */
public class VirtualClockAwareLogger extends ExtraTagLogger {

	/**
	 * Constructs a new logger.
	 * 
	 * @param logger the underlying logger
	 */
	public VirtualClockAwareLogger(Logger logger) {
		super(logger);
	}

	@Override
	protected String tagMessage(String msg) {
		VirtualClock clock = VirtualClock.getInstance();
		if (!clock.isVirtualMode())
			return msg;
		return String.format("[Virtual time: %s] %s",
				clock.getTime().toString("yyyy-MM-dd HH:mm:ss.SSS"), msg);
	}
}
