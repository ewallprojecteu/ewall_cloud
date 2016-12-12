package eu.ewall.platform.idss.utils.log;

import eu.ewall.platform.idss.utils.datetime.VirtualClock;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This factory creates loggers that include the time of the {@link
 * VirtualClock VirtualClock} if it's running in virtual mode.
 * 
 * @author Dennis Hofs (RRD)
 */
public class VirtualClockAwareLoggerFactory implements ILoggerFactory {

	@Override
	public Logger getLogger(String name) {
		Logger base = LoggerFactory.getLogger(name);
		return new VirtualClockAwareLogger(base);
	}
}
