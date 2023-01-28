/**
 * 
 */
package com.bearcode.ovf.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;

/**
 * Logging class that provides a block indentation for ease of reading.
 * 
 * @author IanBrown
 * 
 * @since Apr 13, 2012
 * @version Apr 18, 2012
 */
public final class BlockLogger {

	/**
	 * the logger to use.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private final Logger logger;

	/**
	 * the method to use to handle the logging.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private Method loggingMethod = null;

	/**
	 * the method used to check to see if logging is enabled.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private Method checkMethod = null;

	/**
	 * the current indent level.
	 * 
	 * @author IanBrown
	 * @since Mar 29, 2012
	 * @version Apr 13, 2012
	 */
	private String indent = "";

	/**
	 * should logging be performed (overrides the logger's settings).
	 * 
	 * @author IanBrown
	 * @since Apr 18, 2012
	 * @version Apr 18, 2012
	 */
	private boolean loggingOn = true;

	/**
	 * Constructs a block logger that simply prints to System.err.
	 * 
	 * @author IanBrown
	 * @since Apr 18, 2012
	 * @version Apr 18, 2012
	 */
	public BlockLogger() {
		logger = null;
		loggingMethod = null;
		checkMethod = null;
	}

	/**
	 * Constructs a block logger for the specified logging level.
	 * 
	 * @author IanBrown
	 * @param logger
	 *            the logger to use.
	 * @param loggingLevel
	 *            the logging level to use.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	public BlockLogger(final Logger logger, final String loggingLevel) {
		this.logger = logger;
		final Class<?> loggerClass = logger.getClass();
		try {
			loggingMethod = loggerClass.getMethod(loggingLevel, new Class<?>[] { String.class, Object[].class });
			checkMethod = loggerClass.getMethod("is" + loggingLevel.substring(0, 1).toUpperCase() + loggingLevel.substring(1)
					+ "Enabled");
		} catch (final SecurityException e) {
		} catch (final NoSuchMethodException e) {
		}
	}

	/**
	 * Ends an indented block.
	 * 
	 * @author IanBrown
	 * @param format
	 *            the message format string.
	 * @param objects
	 *            the objects to format.
	 * @since Mar 29, 2012
	 * @version Apr 13, 2012
	 */
	public final void endBlock(final String format, final Object... objects) {
		indent = indent.substring(0, indent.length() - 2);
		indentMessage(format, objects);
	}

	/**
	 * Gets the logger.
	 * 
	 * @author IanBrown
	 * @return the logger.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	public final Logger getLogger() {
		return logger;
	}

	/**
	 * Indents a message.
	 * 
	 * @author IanBrown
	 * @param format
	 *            the message format string.
	 * @param objects
	 *            the objects to format.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	public final void indentMessage(final String format, final Object... objects) {
		if (isLoggingEnabled()) {
			final String value = indent + String.format(format, objects);
			if ((logger == null) || (loggingMethod == null)) {
				//System.err.println(value);
				// Do NOT use system out at all. No logger, no output

			} else {
				try {
					loggingMethod.invoke(getLogger(), value);
				} catch (final IllegalArgumentException e) {
				} catch (final IllegalAccessException e) {
				} catch (final InvocationTargetException e) {
				}
			}
		}
	}

	/**
	 * Determines if logging is enabled for this block logger.
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if logging is enabled, <code>false</code> otherwise.
	 * @since Apr 13, 2012
	 * @version Apr 18, 2012
	 */
	public final boolean isLoggingEnabled() {
		if (checkMethod != null) {
			if (!isLoggingOn()) {
				return false;
			}

			Object status;
			try {
				status = checkMethod.invoke(getLogger());
				return (Boolean) status;
			} catch (final IllegalArgumentException e) {
			} catch (final IllegalAccessException e) {
			} catch (final InvocationTargetException e) {
			}
		}

		return isLoggingOn();
	}

	/**
	 * Is logging on?
	 * 
	 * @author IanBrown
	 * @return the logging on flag.
	 * @since Apr 18, 2012
	 * @version Apr 18, 2012
	 */
	public final boolean isLoggingOn() {
		return loggingOn;
	}

	/**
	 * Sets the logging on flag.
	 * 
	 * @author IanBrown
	 * @param loggingOn
	 *            the logging on flag to set.
	 * @since Apr 18, 2012
	 * @version Apr 18, 2012
	 */
	public final void setLoggingOn(final boolean loggingOn) {
		this.loggingOn = loggingOn;
	}

	/**
	 * Starts an indented block.
	 * 
	 * @author IanBrown
	 * @param format
	 *            the message format string.
	 * @param objects
	 *            the objects to format.
	 * @since Mar 29, 2012
	 * @version Mar 29, 2012
	 */
	public final void startBlock(final String format, final Object... objects) {
		indentMessage(format, objects);
		indent += "  ";
	}
}
