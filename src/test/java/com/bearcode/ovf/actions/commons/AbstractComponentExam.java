/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import org.junit.After;
import org.junit.Before;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;

/**
 * Abstract test for Spring components.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of component.
 * @since Aug 6, 2012
 * @version Aug 6, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "applicationContext_test.xml" })
@DirtiesContext
public abstract class AbstractComponentExam<C> extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the component to test.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	private C component;

	/**
	 * Sets up the component to test.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	@Before
	public final void setUpComponent() {
		setUpForComponent();
		setComponent(createComponent());
	}

	/**
	 * Tears down the component after testing.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	@After
	public final void tearDownComponent() {
		setComponent(null);
		tearDownForComponent();
	}

	/**
	 * Creates a component of the type to test.
	 * 
	 * @author IanBrown
	 * @return the component.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	protected abstract C createComponent();

	/**
	 * Gets the component.
	 * 
	 * @author IanBrown
	 * @return the component.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	protected final C getComponent() {
		return component;
	}

	/**
	 * Sets up to test the specific type of component.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	protected abstract void setUpForComponent();

	/**
	 * Tears down the set up for testing the specific type of component.
	 * 
	 * @author IanBrown
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	protected abstract void tearDownForComponent();

	/**
	 * Sets the component.
	 * 
	 * @author IanBrown
	 * @param component
	 *            the component to set.
	 * @since Aug 6, 2012
	 * @version Aug 6, 2012
	 */
	private void setComponent(final C component) {
		this.component = component;
	}

}
