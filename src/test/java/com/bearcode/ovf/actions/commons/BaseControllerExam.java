package com.bearcode.ovf.actions.commons;

import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.HandlerAdapter;

import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;

/**
 * Abstract integration test base class for testing {@link BaseController} implementations.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of base controller to test.
 * @since Dec 15, 2011
 * @version Aug 16, 2012
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "BaseController-context.xml" })
@DirtiesContext
public abstract class BaseControllerExam<C extends BaseController> extends AbstractTransactionalJUnit4SpringContextTests {

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	@Autowired
	private StateService stateService;

	/**
	 * the faces service.
	 * 
	 * @author IanBrown
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	@Autowired
	private FacesService facesService;

	/**
	 * the base controller to test.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	private C baseController;

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
	 * Sets up to test the base controller.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Jan 4, 2012
	 */
	@Before
	public final void setUpBaseController() {
		setUpForBaseController();
		setBaseController(createBaseController());
		ReflectionTestUtils.setField(getBaseController(), "facesService", getFacesService());
		ReflectionTestUtils.setField(getBaseController(), "stateService", stateService);
	}

	/**
	 * Tears down the base controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	@After
	public final void tearDownBaseController() {
		setBaseController(null);
		tearDownForBaseController();
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	/**
	 * Creates a base controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the base controller.
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	protected abstract C createBaseController();

	/**
	 * Gets the base controller.
	 * 
	 * @author IanBrown
	 * @return the base controller.
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	protected final C getBaseController() {
		return baseController;
	}

	/**
	 * Gets the faces service.
	 * 
	 * @author IanBrown
	 * @return the faces service.
	 * @since Mar 28, 2012
	 * @version Mar 28, 2012
	 */
	protected final FacesService getFacesService() {
		return facesService;
	}

	/**
	 * Gets the handler adapter.
	 * 
	 * @author IanBrown
	 * @return the handler adapter.
	 * @since Jan 19, 2012
	 * @version Jan 20, 2012
	 */
	protected final HandlerAdapter getHandlerAdapter() {
		return handlerAdapter;
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
		final Authentication authentication = EasyMock.createMock("Authentication", Authentication.class);
		EasyMock.expect(authentication.getPrincipal()).andReturn(user).atLeastOnce();
		EasyMock.replay(authentication);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return authentication;
	}

	/**
	 * Sets up to test the specific type of base controller.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	protected abstract void setUpForBaseController();

	/**
	 * Tears down the base controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	protected abstract void tearDownForBaseController();

	/**
	 * Sets the base controller.
	 * 
	 * @author IanBrown
	 * @param baseController
	 *            the base controller to set.
	 * @since Dec 15, 2011
	 * @version Jan 3, 2012
	 */
	private void setBaseController(final C baseController) {
		this.baseController = baseController;
	}
}