/**
 * 
 */
package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.OverseasFormControllerCheck;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.eod.AdditionalAddress;
import com.bearcode.ovf.model.eod.CorrectionsLeo;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import org.easymock.EasyMock;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 * Extended {@link OverseasFormControllerCheck} test for
 * {@link SendCorrectionsController}.
 * 
 * @author IanBrown
 * 
 * @since Dec 22, 2011
 * @version Dec 22, 2011
 */
public final class SendCorrectionsControllerTest extends CorrectionsControllerCheck<SendCorrectionsController> {


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.SendCorrectionsController#formBackingObject
	 * for the case where the leo ID is set.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem building the form backing object.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testFormBackingObject_leoId() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final long leoId = 1l;
		request.setParameter("leoId", Long.toString(leoId));
		final LocalOfficial localOfficial = createLocalOfficial();
		EasyMock.expect(getLocalOfficialService().findById(leoId)).andReturn(localOfficial);
		replayAll();

		final Object actualFormBackingObject = getBaseController().formBackingObject( leoId, 0l );

		assertTrue("The form backing object is a CorrectionsLeo", actualFormBackingObject instanceof CorrectionsLeo);
		final CorrectionsLeo correctionsLeo = (CorrectionsLeo) actualFormBackingObject;
		assertCorrectionsLeo(localOfficial, correctionsLeo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.SendCorrectionsController#formBackingObject
	 * for the case where the region ID is set.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem building the form backing object.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testFormBackingObject_regionId() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final long regionId = 2l;
		request.setParameter("regionId", Long.toString(regionId));
		final LocalOfficial localOfficial = createLocalOfficial();
		EasyMock.expect(getLocalOfficialService().findForRegion(regionId)).andReturn(localOfficial);
		replayAll();

		final Object actualFormBackingObject = getBaseController().formBackingObject( 0l, regionId );

		assertTrue("The form backing object is a CorrectionsLeo", actualFormBackingObject instanceof CorrectionsLeo);
		final CorrectionsLeo correctionsLeo = (CorrectionsLeo) actualFormBackingObject;
		assertCorrectionsLeo(localOfficial, correctionsLeo);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.SendCorrectionsController#getLocalOfficialService()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testGetLocalOfficialService() {
		assertSame("The local official service is set", getLocalOfficialService(), getBaseController()
				.getLocalOfficialService());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.SendCorrectionsController#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.eod.CorrectionsLeo, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the submit.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@Test
	public final void testOnSubmitHttpServletRequestHttpServletResponseObjectBindException() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final CorrectionsLeo command = createMock("Command", CorrectionsLeo.class);
		final LocalOfficial leo = createMock( "leo", LocalOfficial.class );
		EasyMock.expect( command.getCorrectionFor() ).andReturn( leo ).atLeastOnce();
		EasyMock.expect( leo.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).atLeastOnce();
		final BindingResult errors = createMock("Errors", BindingResult.class);
        final ModelMap actualModel = createModelMap( null, request, null, true, true );
		EasyMock.expect( errors.hasErrors() ).andReturn( false ).anyTimes();
        getLocalOfficialService().makeCorrections( command );
		final Authentication authentication = addAuthenticationToSecurityContext();
		addOverseasUserToAuthentication( authentication, null );
		EasyMock.expect( actualModel.addAttribute( EasyMock.eq( "messageCode" ), EasyMock.eq( "eod.corrections.save_success" ) ) ).andReturn( actualModel );
		replayAll();

		final String actualView = getBaseController().onSubmit(request, actualModel, command, errors);

		assertNotNull("There is no view", actualView);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.SendCorrectionsController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.eod.CorrectionsLeo)}
	 * for the case where there is no object to correct.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem showing the form.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testShowFormHttpServletRequestHttpServletResponseBindExceptionMap_noObject() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final ModelMap actualModel = createModelMap( null, request, null, true, false );
		final CorrectionsLeo command = createMock("Command", CorrectionsLeo.class);
		EasyMock.expect(command.getCorrectionFor()).andReturn(null);
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualView = getBaseController().showForm(request, actualModel, command);

		assertNotNull("A model and view is returned", actualView);
		assertTrue("The view is a redirect", actualView.startsWith( "redirect" ));
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.SendCorrectionsController#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.eod.CorrectionsLeo)}
	 * for the case where there is an object to correct.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem showing the form.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testShowFormHttpServletRequestHttpServletResponseBindExceptionMap_object() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final BindException errors = createMock("Errors", BindException.class);
		final CorrectionsLeo command = createMock("Command", CorrectionsLeo.class);
		EasyMock.expect(errors.getTarget()).andReturn(command).anyTimes();
		final LocalOfficial object = createMock("Object", LocalOfficial.class);
		EasyMock.expect(command.getCorrectionFor()).andReturn(object).anyTimes();
		EasyMock.expect( object.getAdditionalAddresses() ).andReturn( Collections.<AdditionalAddress>emptyList() ).anyTimes();
        final ModelMap actualModel = createModelMap( null, request, null, true, false );
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualModelAndView = getBaseController().showForm(request, actualModel, command );
		assertNotNull("A model and view is returned", actualModelAndView);
		verifyAll();
	}


	/** {@inheritDoc} */
	@Override
	protected final SendCorrectionsController createCorrectionsController() {
		final SendCorrectionsController sendCorrectionsController = new SendCorrectionsController();
		return sendCorrectionsController;
	}


	/** {@inheritDoc} */
	@Override
	protected final void setUpForCorrectionsController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForCorrectionsController() {
	}

	/**
	 * Custom assertion to ensure that the local official is copied over
	 * correctly.
	 * 
	 * @author IanBrown
	 * @param localOfficial
	 *            the local official.
	 * @param correctionsLeo
	 *            the corrections LEO.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void assertCorrectionsLeo(final LocalOfficial localOfficial, final CorrectionsLeo correctionsLeo) {
		assertSame("The corrections are for the local official", localOfficial, correctionsLeo.getCorrectionFor());
		assertPerson("Add Contact", localOfficial.getAddContact(), correctionsLeo.getAddContact());
		assertEquals("The Add Email is set", localOfficial.getAddEmail(), correctionsLeo.getAddEmail());
		assertEquals("The Add phone is set", localOfficial.getAddPhone(), correctionsLeo.getAddPhone());
		assertEquals("The Dsn phone is set", localOfficial.getDsnPhone(), correctionsLeo.getDsnPhone());
		assertEquals("The further instruction is set", localOfficial.getFurtherInstruction(),
				correctionsLeo.getFurtherInstruction());
		assertEquals("The hours are set", localOfficial.getHours(), correctionsLeo.getHours());
		assertPerson("Leo", localOfficial.getLeo(), correctionsLeo.getLeo());
		assertEquals("The Leo email is set", localOfficial.getLeoEmail(), correctionsLeo.getLeoEmail());
		assertEquals("The Leo fax is set", localOfficial.getLeoFax(), correctionsLeo.getLeoFax());
		assertEquals("The Leo phone is set", localOfficial.getLeoPhone(), correctionsLeo.getLeoPhone());
		assertPerson("Lovc", localOfficial.getLovc(), correctionsLeo.getLovc());
		assertEquals("The Lovc Email is set", localOfficial.getLovcEmail(), correctionsLeo.getLovcEmail());
		assertEquals("The Lovc fax is set", localOfficial.getLovcFax(), correctionsLeo.getLovcFax());
		assertEquals("The Lovc phone is set", localOfficial.getLovcPhone(), correctionsLeo.getLovcPhone());
		assertAddress("Mailing", localOfficial.getMailing(), correctionsLeo.getMailing());
		assertAddress("Physical", localOfficial.getPhysical(), correctionsLeo.getPhysical());
		assertEquals("The website is set", localOfficial.getWebsite(), correctionsLeo.getWebsite());
	}

	/**
	 * Creates a local official.
	 * 
	 * @author IanBrown
	 * @return the local official.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private LocalOfficial createLocalOfficial() {
		final LocalOfficial localOfficial = new LocalOfficial();
        localOfficial.setWebsite( "http://website" );
        localOfficial.setDsnPhone( "DSN Phone" );
        localOfficial.setFurtherInstruction( "Further Instruction" );
		final Officer leo = createOfficer( "LEO" );
		localOfficial.getOfficers().add( leo );
        final Officer lovc = createOfficer( "LOVC" );
        localOfficial.getOfficers().add( lovc );
        final Officer addContact = createOfficer( "Add Contact" );
        localOfficial.getOfficers().add( addContact );
		final Address mailing = createAddress("Mailing");
        localOfficial.setMailing( mailing );
		final Address physical = createAddress("Physical");
        localOfficial.setPhysical( physical );
        localOfficial.setGeneralEmail( "Election Office Email Address" );
        localOfficial.setHours( "9-5" );
		return localOfficial;
	}

    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/EodSendCorrections.jsp";
    }

    @Override
    protected String getExpectedPageTitle() {
        return "Send Local Official Corrections";
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
        return "/WEB-INF/pages/blocks/EodSendCorrectionsSuccess.jsp";
    }
}
