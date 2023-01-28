/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.forms.cf.CandidateFinderForm;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.mail.MailingAddress;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.service.MailingListService;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.validators.CandidateFinderValidator;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Extended {@link BaseControllerCheck} test for
 * {@link CandidateFinderController}.
 * 
 * @author IanBrown
 * @author Leonid Ginzburg
 *
 * @since Dec 20, 2011
 * @version Dec 21, 2011
 */
public final class CandidateFinderControllerTest extends
        BaseControllerCheck<CandidateFinderController> {

	/**
	 * the question field service.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private QuestionFieldService questionFieldService;

	/**
	 * the mailing list service.
	 * 
	 * @author IanBrown
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private MailingListService mailingListService;

    /**
     * the Candidate finder validator
     */
    private CandidateFinderValidator candidateFinderValidator;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderController#showFinderForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
        final ModelMap model = createModelMap(null, request, null, true, true);
        final FieldType countryFieldType = createMock("CountryFieldType",
                FieldType.class);
        EasyMock.expect(getQuestionFieldService().getCountryFieldType())
                .andReturn( countryFieldType ).atLeastOnce();
        final FieldDictionaryItem country = createMock("Country",
                FieldDictionaryItem.class);
        final List<FieldDictionaryItem> countries = Arrays.asList( country );
        EasyMock.expect(countryFieldType.getFixedOptions())
                .andReturn( countries ).atLeastOnce();
        addAttributeToModelMap( model, "countries", countries );
		replayAll();

		final String actualReferences = getBaseController()
				.showFinderForm( request, model );

        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualReferences);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderController#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.CandidateFinderForm, org.springframework.validation.BindingResult)}
	 * for the case where we are adding to the list.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testOnSubmitHttpServletRequestHttpServletResponseObjectBindException_addToList()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final ModelMap model = createModelMap(null, request, null, true, true);
		final CandidateFinderForm command = createMock("Command",
				CandidateFinderForm.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
        EasyMock.expect( errors.hasErrors() ).andReturn( false );
		EasyMock.expect(command.isAddToList()).andReturn(true);
		final String email = "email@somewhere.com";
		EasyMock.expect(command.getEmail()).andReturn(email);
		final MailingAddress mailingAddress = new MailingAddress(); /*createMock("MailingAddress",
				MailingAddress.class);*/
		EasyMock.expect(getMailingListService().findByEmail(email)).andReturn(
                mailingAddress );
		final String serverName = "ServerName";
		request.setServerName(serverName);
		final String requestURI = "/requestURI";
		request.setRequestURI(requestURI);
		//mailingAddress.setUrl(serverName + requestURI);
		final Address address = createMock("Address", Address.class);
		EasyMock.expect(command.getAddress()).andReturn(address).atLeastOnce();
        final String stateAbbreviation = "SA";
        EasyMock.expect(address.getState()).andReturn(stateAbbreviation).atLeastOnce();
        final String votingCity = "townville";
        EasyMock.expect(address.getCity()).andReturn(votingCity);
        final String votingZip = "12345";
        EasyMock.expect(address.getZip()).andReturn(votingZip);
		final State state = createMock("State", State.class);
		EasyMock.expect(
				getStateService().findByAbbreviation(
						stateAbbreviation)).andReturn(state);
        //mailingAddress.setState(state);
        //mailingAddress.setVotingCity(votingCity);
        //mailingAddress.setVotingPostalCode(votingZip);
		final long country = 2l;
		EasyMock.expect(command.getCountry()).andReturn(country);
		//mailingAddress.setCurrentCountry(country);
        //mailingAddress.setVotingStateName( stateAbbreviation );
		getMailingListService().saveMailingAddress( mailingAddress );
		replayAll();

		final String actualModelAndView = getBaseController()
				.onSubmit(request, model, command, errors);

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The command is added to the request session", command,
				request.getSession().getAttribute("cf_address"));
        assertEquals( "The URL of the redirect is set", "redirect:/CandidateFinderList.htm", actualModelAndView  );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.cf.CandidateFinderController#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.cf.CandidateFinderForm, org.springframework.validation.BindingResult)}
	 * for the case where we are not adding to the list.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	@Test
	public final void testOnSubmitHttpServletRequestHttpServletResponseObjectBindException_noAddToList()
			throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final ModelMap model = createModelMap(null, request, null, true, true);
		final CandidateFinderForm command = createMock("Command",
				CandidateFinderForm.class);
        EasyMock.expect(command.isAddToList()).andReturn(false);
        final BindingResult errors = createMock("Errors", BindingResult.class);
        EasyMock.expect( errors.hasErrors() ).andReturn( false );
		replayAll();

		final String actualModelAndView = getBaseController()
				.onSubmit(request, model, command, errors);

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The command is added to the request session", command,
				request.getSession().getAttribute("cf_address"));
        assertEquals( "The URL of the redirect is set", "redirect:/CandidateFinderList.htm", actualModelAndView  );
		verifyAll();
	}


	/**
	 * Gets the mailing list service.
	 * 
	 * @author IanBrown
	 * @return the mailing list service.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private MailingListService getMailingListService() {
		return mailingListService;
	}

	/**
	 * Gets the question field service.
	 * 
	 * @author IanBrown
	 * @return the question field service.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
	}

    /**
	 * Sets the mailing list service.
	 * 
	 * @author IanBrown
	 * @param mailingListService
	 *            the mailing list service to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setMailingListService(
			final MailingListService mailingListService) {
		this.mailingListService = mailingListService;
	}

	/**
	 * Sets the question field service.
	 * 
	 * @author IanBrown
	 * @param questionFieldService
	 *            the question field service to set.
	 * @since Dec 20, 2011
	 * @version Dec 20, 2011
	 */
	private void setQuestionFieldService(
			final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
	}

    public CandidateFinderValidator getCandidateFinderValidator() {
        return candidateFinderValidator;
    }

    public void setCandidateFinderValidator( CandidateFinderValidator candidateFinderValidator ) {
        this.candidateFinderValidator = candidateFinderValidator;
    }

    /** {@inheritDoc} */
    @Override
    protected CandidateFinderController createBaseController() {
        final CandidateFinderController candidateFinderController = new CandidateFinderController();
        ReflectionTestUtils.setField( candidateFinderController, "mailingListService", mailingListService );
        ReflectionTestUtils.setField( candidateFinderController, "questionFieldService", questionFieldService );
        ReflectionTestUtils.setField( candidateFinderController, "validator", candidateFinderValidator );
        return candidateFinderController;
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/CandidateFinder.jsp";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedPageTitle() {
        return "Candidate Finder";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedSectionCss() {
        return "/css/candidate-finder.css";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedSectionName() {
        return "rava";
    }

    /** {@inheritDoc} */
    @Override
    protected String getExpectedSuccessContentBlock() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    protected void setUpForBaseController() {
        setMailingListService(createMock("MailingListService",
                MailingListService.class));
        setQuestionFieldService(createMock("QuestionFieldService",
                QuestionFieldService.class));
        setCandidateFinderValidator( createMock( "CandidateFinderValidator", CandidateFinderValidator.class ) );
    }

    /** {@inheritDoc} */
    @Override
    protected void tearDownForBaseController() {
        setCandidateFinderValidator( null );
        setMailingListService( null );
        setQuestionFieldService( null );
    }
}
