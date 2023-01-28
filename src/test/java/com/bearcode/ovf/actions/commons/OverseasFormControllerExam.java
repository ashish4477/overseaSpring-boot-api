package com.bearcode.ovf.actions.commons;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.actions.commons.OverseasFormController;
import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Abstract integration test base class for testing
 * {@link OverseasFormController} implementations.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of overseas form controller to test.
 * @since Dec 15, 2011
 * @version Dec 15, 2011
 */
/**
 * @author IanBrown
 * 
 * @param <C>
 * @since Dec 15, 2011
 * @version Dec 15, 2011
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
public abstract class OverseasFormControllerExam<C extends OverseasFormController>
		extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the overseas form controller to test.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	private C overseasFormController;

	/**
	 * Sets up to test the overseas form controller.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	@Before
	public final void setUpOverseasFormController() {
		setUpForOverseasFormController();
		setOverseasFormController(createOverseasFormController());
	}

	/**
	 * Tears down the overseas form controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	@After
	public final void tearDownOverseasFormController() {
		setOverseasFormController(null);
		tearDownForOverseasFormController();
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/**
	 * Creates an overseas form controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the overseas form controller.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	protected abstract C createOverseasFormController();

	/**
	 * Gets the overseas form controller.
	 * 
	 * @author IanBrown
	 * @return the overseas form controller.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	protected final C getOverseasFormController() {
		return overseasFormController;
	}

	/**
	 * Sets up authentication for the security context.
	 * 
	 * @author IanBrown
	 * @param user
	 *            the "authenticated" user.
	 * @return the authentication object.
	 * @since Dec 14, 2011
	 * @version Dec 14, 2011
	 */
	protected Authentication setUpAuthentication(final OverseasUser user) {
		final Authentication authentication = EasyMock.createMock(
				"Authentication", Authentication.class);
		EasyMock.expect(authentication.getPrincipal()).andReturn(user)
				.atLeastOnce();
		EasyMock.replay(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	/**
	 * Sets up to test the specific type of overseas form controller.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	protected abstract void setUpForOverseasFormController();

	/**
	 * Tears down the overseas form controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	protected abstract void tearDownForOverseasFormController();

	/**
	 * Sets the overseas form controller.
	 * 
	 * @author IanBrown
	 * @param overseasFormController
	 *            the overseas form controller to set.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	private void setOverseasFormController(final C overseasFormController) {
		this.overseasFormController = overseasFormController;
	}

}