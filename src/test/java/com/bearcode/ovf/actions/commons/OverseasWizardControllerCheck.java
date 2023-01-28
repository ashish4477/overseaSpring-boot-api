/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import static org.junit.Assert.fail;

import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Abstract test for implementations of {@link OverseasWizardController}.
 * 
 * @author IanBrown
 * 
 * @param <W>
 *            the type of overseas wizard controller to test.
 * @since Dec 21, 2011
 * @version Dec 21, 2011
 */
public abstract class OverseasWizardControllerCheck<W extends OverseasWizardController>
		extends EasyMockSupport {

	/**
	 * the overseas wizard controller to test.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private W overseasWizardController;

	/**
	 * Sets up to test the overseas wizard controller.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Before
	public final void setUpOverseasWizardController() {
		setUpForOverseasWizardController();
		setOverseasWizardController(createOverseasWizardController());
	}

	/**
	 * Tears down the overseas wizard controller after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@After
	public final void tearDownOverseasWizardController() {
		setOverseasWizardController(null);
		tearDownForOverseasWizardController();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testBuildReferences() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getContentBlock(javax.servlet.http.HttpServletRequest, java.lang.Object, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetContentBlock() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getFacesService()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetFacesService() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getPageTitle()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetPageTitle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getSectionCss()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetSectionCss() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getSectionName()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetSectionName() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getStateService()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetStateService() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getSuccessView()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetSuccessView() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getUser()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetUser() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#getViewName(javax.servlet.http.HttpServletRequest, java.lang.Object, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testGetViewNameHttpServletRequestObjectInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#isFormSubmission(javax.servlet.http.HttpServletRequest)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testIsFormSubmissionHttpServletRequest() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#referenceData(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testReferenceDataHttpServletRequestObjectErrorsInt() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setDeploymentEnv(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetDeploymentEnv() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setFacesService(com.bearcode.ovf.service.FacesService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetFacesService() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setPage(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetPage() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setPageTitle(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetPageTitle() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setSectionCss(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetSectionCss() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setSectionName(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetSectionName() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setStateService(com.bearcode.ovf.service.StateService)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetStateService() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.OverseasWizardController#setSuccessView(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	@Test
	public final void testSetSuccessView() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Creates an overseas wizard controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the overseas wizard controller.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	protected abstract W createOverseasWizardController();

	/**
	 * Sets up to test the specific type of overseas wizard controller.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	protected abstract void setUpForOverseasWizardController();

	/**
	 * Tears down the set up for testing the specific type of overseas wizard
	 * controller.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	protected abstract void tearDownForOverseasWizardController();

	/**
	 * Gets the overseas wizard controller.
	 * 
	 * @author IanBrown
	 * @return the overseas wizard controller.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private W getOverseasWizardController() {
		return overseasWizardController;
	}

	/**
	 * Sets the overseas wizard controller.
	 * 
	 * @author IanBrown
	 * @param overseasWizardController
	 *            the overseas wizard controller to set.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private void setOverseasWizardController(final W overseasWizardController) {
		this.overseasWizardController = overseasWizardController;
	}
}
