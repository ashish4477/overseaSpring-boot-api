/**
 * Copyright 2013 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.actions;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;

/**
 * Extended {@link BaseControllerCheck} test for {@link FormChoiceController}.
 * @author Ian
 *
 */
public final class FormChoiceControllerTest extends
        BaseControllerCheck<FormChoiceController> {

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#requestFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where no voting location information is provided and there is no user.
	 */
	@Test
	public final void testRequestFormChoice_noUserNoVotingInformation() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = null;
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = null;
		final String vrName = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();
		
		final String actualModelAndView = getBaseController().requestFormChoice(request, vrState, vrName, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#requestFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where no voting location information is provided.
	 */
	@Test
	public final void testRequestFormChoice_userVotingInformation() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = null;
		final String vrName = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();
		
		final String actualModelAndView = getBaseController().requestFormChoice(request, vrState, vrName, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#requestFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where just a voting state is provided.
	 */
	@Test
	public final void testRequestFormChoice_votingState() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = "VR";
		final String vrName = null;
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "vrState", vrState);
		replayAll();
		
		final String actualModelAndView = getBaseController().requestFormChoice(request, vrState, vrName, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#requestFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where a voting region is provided.
	 */
	@Test
	public final void testRequestFormChoice_votingRegion() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = "VR";
		final String vrName = "Name";
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "vrState", vrState);
		addAttributeToModelMap(model, "vrName", vrName);
		replayAll();
		
		final String actualModelAndView = getBaseController().requestFormChoice(request, vrState, vrName, model);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#performFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where there is no voting location information.
	 */
	@Test
	public final void testPerformFormChoice_noVotingInformation() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = null;
		final String vrName = null;
		final String formType = "formType";
		final ModelMap model = createModelMap(user, request, null, true, false);
		replayAll();
		
		final String actualModelAndView = getBaseController().performFormChoice(request, vrState, vrName, formType, model);
		
		assertEquals("A redirection to the flow is returned", FormChoiceController.BASE_FLOW_REDIRECTION + "/" + formType + ".htm", actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#performFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where there is just a voting state.
	 */
	@Test
	public final void testPerformFormChoice_votingState() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = "VR";
		final String vrName = null;
		final String formType = "formType";
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "vrState", vrState);
		replayAll();
		
		final String actualModelAndView = getBaseController().performFormChoice(request, vrState, vrName, formType, model);
		
		assertEquals("A redirection to the flow is returned", FormChoiceController.BASE_FLOW_REDIRECTION + "/" + formType + ".htm", actualModelAndView);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.actions.FormChoiceController#performFormChoice(javax.servlet.http.HttpServletRequest, java.lang.String, java.lang.String, java.lang.String, org.springframework.ui.ModelMap)}
	 * for the case where there is a voting region.
	 */
	@Test
	public final void testPerformFormChoice_votingRegion() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = new OverseasUser();
		addOverseasUserToAuthentication(authentication, user);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final String vrState = "VR";
		final String vrName = "Name";
		final String formType = "formType";
		final ModelMap model = createModelMap(user, request, null, true, false);
		addAttributeToModelMap(model, "vrState", vrState);
		addAttributeToModelMap(model, "vrName", vrName);
		replayAll();
		
		final String actualModelAndView = getBaseController().performFormChoice(request, vrState, vrName, formType, model);
		
		assertEquals("A redirection to the flow is returned", FormChoiceController.BASE_FLOW_REDIRECTION + "/" + formType + ".htm", actualModelAndView);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
    protected final FormChoiceController createBaseController() {
		return new FormChoiceController();
    }

	/** {@inheritDoc} */
	@Override
    protected final String getExpectedContentBlock() {
		return FormChoiceController.CONTENT_BLOCK;
    }

	/** {@inheritDoc} */
	@Override
    protected final String getExpectedPageTitle() {
		return FormChoiceController.PAGE_TITLE;
    }

	/** {@inheritDoc} */
	@Override
    protected final String getExpectedSectionCss() {
		return FormChoiceController.SECTION_CSS;
    }

	/** {@inheritDoc} */
	@Override
    protected final String getExpectedSectionName() {
		return FormChoiceController.SECTION_NAME;
    }

	/** {@inheritDoc} */
	@Override
    protected final String getExpectedSuccessContentBlock() {
	    return null;
    }

	/** {@inheritDoc} */
	@Override
    protected final void setUpForBaseController() {
    }

	/** {@inheritDoc} */
	@Override
    protected final void tearDownForBaseController() {
    }
}
