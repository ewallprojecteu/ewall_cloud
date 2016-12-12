package eu.ewall.platform.idss.utils.log;

import org.slf4j.Logger;
import org.slf4j.Marker;

/**
 * This is a wrapper around another logger. It can prepend extra tags to each
 * log message. Subclasses need only implement {@link #tagMessage(String)
 * tagMessage()}.
 * 
 * @author Dennis Hofs (RRD)
 */
public abstract class ExtraTagLogger implements Logger {
	private Logger logger;
	
	public ExtraTagLogger(Logger logger) {
		this.logger = logger;
	}
	
	/**
	 * Prepends tags to the specified message. This is done before formatting
	 * anchors {} have been replaced, so you might need to escape characters in
	 * the tag.
	 * 
	 * @param msg the message
	 * @return the tagged message
	 */
	protected abstract String tagMessage(String msg);

	@Override
	public void debug(String msg) {
		logger.debug(tagMessage(msg));
	}

	@Override
	public void debug(String format, Object... argArray) {
		logger.debug(tagMessage(format), argArray);
	}

	@Override
	public void debug(String format, Object arg) {
		logger.debug(tagMessage(format), arg);
	}

	@Override
	public void debug(String format, Object arg1, Object arg2) {
		logger.debug(tagMessage(format), arg1, arg2);
	}

	@Override
	public void debug(String msg, Throwable t) {
		logger.debug(tagMessage(msg), t);
	}

	@Override
	public void debug(Marker marker, String msg) {
		logger.debug(marker, tagMessage(msg));
	}

	@Override
	public void debug(Marker marker, String format, Object... argArray) {
		logger.debug(marker, tagMessage(format), argArray);
	}

	@Override
	public void debug(Marker marker, String format, Object arg) {
		logger.debug(marker, tagMessage(format), arg);
	}

	@Override
	public void debug(Marker marker, String format, Object arg1, Object arg2) {
		logger.debug(marker, tagMessage(format), arg1, arg2);
	}

	@Override
	public void debug(Marker marker, String format, Throwable t) {
		logger.debug(marker, tagMessage(format), t);
	}

	@Override
	public void error(String msg) {
		logger.error(tagMessage(msg));
	}

	@Override
	public void error(String format, Object... argArray) {
		logger.error(tagMessage(format), argArray);
	}

	@Override
	public void error(String format, Object arg) {
		logger.error(tagMessage(format), arg);
	}

	@Override
	public void error(String format, Object arg1, Object arg2) {
		logger.error(tagMessage(format), arg1, arg2);
	}

	@Override
	public void error(String msg, Throwable t) {
		logger.error(tagMessage(msg), t);
	}

	@Override
	public void error(Marker marker, String msg) {
		logger.error(marker, tagMessage(msg));
	}

	@Override
	public void error(Marker marker, String format, Object... argArray) {
		logger.error(marker, tagMessage(format), argArray);
	}

	@Override
	public void error(Marker marker, String format, Object arg) {
		logger.error(marker, tagMessage(format), arg);
	}

	@Override
	public void error(Marker marker, String format, Object arg1, Object arg2) {
		logger.error(marker, tagMessage(format), arg1, arg2);
	}

	@Override
	public void error(Marker marker, String msg, Throwable t) {
		logger.error(marker, tagMessage(msg), t);
	}

	@Override
	public String getName() {
		return logger.getName();
	}

	@Override
	public void info(String msg) {
		logger.info(tagMessage(msg));
	}

	@Override
	public void info(String format, Object... argArray) {
		logger.info(tagMessage(format), argArray);
	}

	@Override
	public void info(String format, Object arg) {
		logger.info(tagMessage(format), arg);
	}

	@Override
	public void info(String format, Object arg1, Object arg2) {
		logger.info(tagMessage(format), arg1, arg2);
	}

	@Override
	public void info(String msg, Throwable t) {
		logger.info(tagMessage(msg), t);
	}

	@Override
	public void info(Marker marker, String msg) {
		logger.info(marker, tagMessage(msg));
	}

	@Override
	public void info(Marker marker, String format, Object... argArray) {
		logger.info(marker, tagMessage(format), argArray);
	}

	@Override
	public void info(Marker marker, String format, Object arg) {
		logger.info(marker, tagMessage(format), arg);
	}

	@Override
	public void info(Marker marker, String format, Object arg1, Object arg2) {
		logger.info(marker, tagMessage(format), arg1, arg2);
	}

	@Override
	public void info(Marker marker, String msg, Throwable t) {
		logger.info(marker, tagMessage(msg), t);
	}

	@Override
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}

	@Override
	public boolean isDebugEnabled(Marker marker) {
		return logger.isDebugEnabled(marker);
	}

	@Override
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}

	@Override
	public boolean isErrorEnabled(Marker marker) {
		return logger.isErrorEnabled(marker);
	}

	@Override
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}

	@Override
	public boolean isInfoEnabled(Marker marker) {
		return logger.isInfoEnabled(marker);
	}

	@Override
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}

	@Override
	public boolean isTraceEnabled(Marker marker) {
		return logger.isTraceEnabled(marker);
	}

	@Override
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}

	@Override
	public boolean isWarnEnabled(Marker marker) {
		return logger.isWarnEnabled(marker);
	}

	@Override
	public void trace(String msg) {
		logger.trace(tagMessage(msg));
	}

	@Override
	public void trace(String format, Object... argArray) {
		logger.trace(tagMessage(format), argArray);
	}

	@Override
	public void trace(String format, Object arg) {
		logger.trace(tagMessage(format), arg);
	}

	@Override
	public void trace(String format, Object arg1, Object arg2) {
		logger.trace(tagMessage(format), arg1, arg2);
	}

	@Override
	public void trace(String msg, Throwable t) {
		logger.trace(tagMessage(msg), t);
	}

	@Override
	public void trace(Marker marker, String msg) {
		logger.trace(marker, tagMessage(msg));
	}

	@Override
	public void trace(Marker marker, String format, Object... argArray) {
		logger.trace(marker, tagMessage(format), argArray);
	}

	@Override
	public void trace(Marker marker, String format, Object arg) {
		logger.trace(marker, tagMessage(format), arg);
	}

	@Override
	public void trace(Marker marker, String format, Object arg1, Object arg2) {
		logger.trace(marker, tagMessage(format), arg1, arg2);
	}

	@Override
	public void trace(Marker marker, String msg, Throwable t) {
		logger.trace(marker, tagMessage(msg), t);
	}

	@Override
	public void warn(String msg) {
		logger.warn(tagMessage(msg));
	}

	@Override
	public void warn(String format, Object... argArray) {
		logger.warn(tagMessage(format), argArray);
	}

	@Override
	public void warn(String format, Object arg) {
		logger.warn(tagMessage(format), arg);
	}

	@Override
	public void warn(String format, Object arg1, Object arg2) {
		logger.warn(tagMessage(format), arg1, arg2);
	}

	@Override
	public void warn(String msg, Throwable t) {
		logger.warn(tagMessage(msg), t);
	}

	@Override
	public void warn(Marker marker, String msg) {
		logger.warn(marker, tagMessage(msg));
	}

	@Override
	public void warn(Marker marker, String format, Object... argArray) {
		logger.warn(marker, tagMessage(format), argArray);
	}

	@Override
	public void warn(Marker marker, String format, Object arg) {
		logger.warn(marker, tagMessage(format), arg);
	}

	@Override
	public void warn(Marker marker, String format, Object arg1, Object arg2) {
		logger.warn(marker, tagMessage(format), arg1, arg2);
	}

	@Override
	public void warn(Marker marker, String msg, Throwable t) {
		logger.warn(marker, tagMessage(msg), t);
	}
}
