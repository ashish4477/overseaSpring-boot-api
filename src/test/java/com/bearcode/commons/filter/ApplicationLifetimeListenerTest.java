/**
 * 
 */
package com.bearcode.commons.filter;

import static org.junit.Assert.assertTrue;

import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * Test for {@link ApplicationLifefileListener}.
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 19, 2011
 */
public final class ApplicationLifetimeListenerTest extends EasyMockSupport {

	/**
	 * the application lifetime listener to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private ApplicationLifetimeListener applicationLifetimeListener;

	/**
	 * Sets up the application lifetime listener before testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Before
	public final void setUpApplicationLifetimeListener() {
		setApplicationLifetimeListener(createApplicationLifetimeListener());
	}

	/**
	 * Tears down the application lifetime listener after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@After
	public final void tearDownApplicationLifetimeListener() {
		setApplicationLifetimeListener(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.ApplicationLifetimeListener#onApplicationEvent(org.springframework.context.ApplicationEvent)}
	 * for the case where a context closed event is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @throws SQLException
	 *             if there is a problem registering a driver again.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testOnApplicationEvent_contextClosedEvent()
			throws SQLException {
		final ContextClosedEvent contextClosedEvent = createMock(
				"ContextClosedEvent", ContextClosedEvent.class);
		final Enumeration<Driver> drivers = DriverManager.getDrivers();

		getApplicationLifetimeListener().onApplicationEvent(contextClosedEvent);

		final Set<Driver> originalDrivers = new HashSet<Driver>();
		while (drivers.hasMoreElements()) {
			originalDrivers.add(drivers.nextElement());
		}
		final Enumeration<Driver> actualDrivers = DriverManager.getDrivers();
		final Set<Driver> updatedDrivers = new HashSet<Driver>();
		while (actualDrivers.hasMoreElements()) {
			updatedDrivers.add(actualDrivers.nextElement());
		}
		assertTrue("Some drivers have been deregistered",
				originalDrivers.size() != updatedDrivers.size());
		for (final Driver originalDriver : originalDrivers) {
			if (!updatedDrivers.contains(originalDriver)) {
				DriverManager.registerDriver(originalDriver);
			}
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.filter.ApplicationLifetimeListener#onApplicationEvent(org.springframework.context.ApplicationEvent)}
	 * for the case where a context refreshed event is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testOnApplicationEvent_contextRefreshedEvent() {
		final ContextRefreshedEvent contextRefreshedEvent = createMock(
				"ContextRefreshedEvent", ContextRefreshedEvent.class);

		getApplicationLifetimeListener().onApplicationEvent(
				contextRefreshedEvent);

		assertTrue("Nothing was done", true);
	}

	/**
	 * Creates an application lifetime listener.
	 * 
	 * @author IanBrown
	 * @return the application lifetime listener.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private ApplicationLifetimeListener createApplicationLifetimeListener() {
		return new ApplicationLifetimeListener();
	}

	/**
	 * Gets the application lifetime listener.
	 * 
	 * @author IanBrown
	 * @return the application lifetime listener.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private ApplicationLifetimeListener getApplicationLifetimeListener() {
		return applicationLifetimeListener;
	}

	/**
	 * Sets the application lifetime listener.
	 * 
	 * @author IanBrown
	 * @param applicationLifetimeListener
	 *            the application lifetime listener to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setApplicationLifetimeListener(
			final ApplicationLifetimeListener applicationLifetimeListener) {
		this.applicationLifetimeListener = applicationLifetimeListener;
	}

}
