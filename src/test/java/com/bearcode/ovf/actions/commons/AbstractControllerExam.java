/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.web.servlet.HandlerAdapter;

import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;

/**
 * Abstract integration test for annotated controllers.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of controller to test.
 * @since Apr 13, 2012
 * @version Jul 28, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "applicationContext_test.xml" })
@DirtiesContext
public abstract class AbstractControllerExam<C> extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the controller to test.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private C controller;

	/**
	 * the handler adapter.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 20, 2012
	 */
	@Autowired
	private HandlerAdapter handlerAdapter;

	/**
	 * Sets up to test the controller.
	 * 
	 * @author IanBrown
	 * @throws Exception if there is a problem setting up the controller.
	 * @since Apr 13, 2012
	 * @version Jul 28, 2012
	 */
	@Before
	public final void setUpController() throws Exception {
		setUpForController();
		setController(createController());
	}

	/**
	 * Tears down the controller after testing.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	@After
	public final void tearDownController() {
		setController(null);
		tearDownForController();
	}

	/**
	 * Creates a controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the controller.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	protected abstract C createController();

	/**
	 * Gets the controller.
	 * 
	 * @author IanBrown
	 * @return the controller.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	protected final C getController() {
		return controller;
	}

	/**
	 * Gets the handler adapter.
	 * 
	 * @author IanBrown
	 * @return the handler adapter.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	protected final HandlerAdapter getHandlerAdapter() {
		return handlerAdapter;
	}

	/**
	 * Sets up to test the specific type of controller.
	 * 
	 * @author IanBrown
	 * @throws Exception if there is a problem setting up for the controller.
	 * @since Apr 13, 2012
	 * @version Jul 28, 2012
	 */
	protected abstract void setUpForController() throws Exception;

	/**
	 * Tears down the set up for testing the specific type of controller.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	protected abstract void tearDownForController();

	/**
	 * Sets the controller.
	 * 
	 * @author IanBrown
	 * @param controller
	 *            the controller to set.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private void setController(final C controller) {
		this.controller = controller;
	}
}
