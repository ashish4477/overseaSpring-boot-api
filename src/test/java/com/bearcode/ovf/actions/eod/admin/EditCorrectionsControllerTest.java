/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.commons.OverseasFormControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.eod.*;
import com.bearcode.ovf.service.LocalOfficialService;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Extended {@link OverseasFormControllerCheck} test for {@link EditCorrectionsController}.
 * 
 * @author IanBrown
 * 
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public final class EditCorrectionsControllerTest extends BaseControllerCheck<EditCorrectionsController> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.EditCorrectionsController#formBackingObject(Long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the form backing object.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
    @SuppressWarnings( "unchecked" )
	@Test
	public final void testFormBackingObjectHttpServletRequest() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final long correctionsId = 25l;
		request.addParameter( "correctionsId", Long.toString( correctionsId ) );
		final CorrectionsLeo corrections = createMock("Corrections", CorrectionsLeo.class);
        final LocalOfficial localOfficial = createMock( "LocalOfficial", LocalOfficial.class );
        EasyMock.expect( corrections.getCorrectionFor() ).andReturn( localOfficial ).anyTimes();
		EasyMock.expect( getLocalOfficialService().findCorrectionsById( correctionsId ) ).andReturn( corrections );
        final List<Officer> officers = createMock( "Officers", List.class );
        EasyMock.expect( localOfficial.getOfficers() ).andReturn( officers ).anyTimes();
        localOfficial.sortAdditionalAddresses();
        EasyMock.expect( localOfficial.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() );
        EasyMock.expect( corrections.getAdditionalAddresses() ).andReturn( Collections.<CorrectionAdditionalAddress>emptyList() );
        EasyMock.expect( officers.size() ).andReturn( 3 ).anyTimes();
        final List<Officer> officersTwo = createMock( "Officers", List.class );
        EasyMock.expect( corrections.getOfficers() ).andReturn( officersTwo ).anyTimes();
        EasyMock.expect( officersTwo.size() ).andReturn( 3 ).anyTimes();

        replayAll();

		final Object actualFormBackingObject = getBaseController().formBackingObject(correctionsId);

		Assert.assertSame( "The corrections are returned", corrections, actualFormBackingObject );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.EditCorrectionsController#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.eod.CorrectionsLeo, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	@SuppressWarnings("deprecation")
	@Test
	public final void testOnSubmitHttpServletRequestHttpServletResponseObjectBindException() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final CorrectionsLeo correctionsLeo = createMock("Command", CorrectionsLeo.class);
		correctionsLeo.setEditor((OverseasUser) EasyMock.anyObject());
		correctionsLeo.setUpdated((Date) EasyMock.anyObject());
        final ModelMap actualModel = createModelMap( null, request, null, true, false );
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication( authentication, null );
		replayAll();

		final String actualModelAndView = getBaseController().onSubmit(request, actualModel, correctionsLeo, errors);

		Assert.assertNotNull( "A model and view is returned", actualModelAndView );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.EditCorrectionsController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.eod.CorrectionsLeo)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem showing the form.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unchecked" })
	@Test
	public final void testShowFormHttpServletRequestHttpServletResponseBindExceptionMap() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		//final BindException errors = createMock("Errors", BindException.class);
		//EasyMock.expect(errors.getTarget()).andReturn(object).atLeastOnce();
		final CorrectionsLeo correctionsLeo = createMock("Command", CorrectionsLeo.class);
		final LocalOfficial correctionFor = createMock("CorrectionFor", LocalOfficial.class);
		EasyMock.expect(correctionsLeo.getCorrectionFor()).andReturn(correctionFor);
		//final Map<String, Object> errorsModel = createMock("ErrorsModel", Map.class);
		//EasyMock.expect(errors.getModel()).andReturn(errorsModel).anyTimes();
		//errorsModel.putAll((Map<String, Object>) EasyMock.anyObject());
		//EasyMock.expectLastCall().atLeastOnce();
		//final int size = 0;
		//EasyMock.expect(errorsModel.size()).andReturn(size);
        final ModelMap actualModel = createModelMap( null, request, null, true, false );
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication( authentication, null );
		replayAll();

		final String actualModelAndView = getBaseController().showForm(request, actualModel, correctionsLeo );

		Assert.assertNotNull( "A model and view is returned", actualModelAndView );
		verifyAll();
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

    @Override
    protected EditCorrectionsController createBaseController() {
        final EditCorrectionsController editCorrectionsController = new EditCorrectionsController();
        editCorrectionsController.setLocalOfficialService(getLocalOfficialService());
        return editCorrectionsController;
    }

    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/admin/EodCorrections.jsp";
    }

    @Override
    protected String getExpectedPageTitle() {
        return "Edit Local Official Corrections" ;
    }

    @Override
    protected String getExpectedSectionCss() {
        return "/css/eod.css";
    }

    @Override
    protected String getExpectedSectionName() {
        return "eod";
    }

    @Override
    protected String getExpectedSuccessContentBlock() {
        return "/WEB-INF/pages/blocks/admin/EodEditSuccessPage.jsp";
    }

    @Override
    protected void setUpForBaseController() {
        setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
    }

    @Override
    protected void tearDownForBaseController() {
        setLocalOfficialService(null);
    }

}
