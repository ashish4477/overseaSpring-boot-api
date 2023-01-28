/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.eodcommands.ExcelPort;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
/**
 *
 * @author IanBrown
 * 
 * @since Jul 26, 2012
 * @version Jul 26, 2012
 */
public final class ExcelDataUploadTest extends BaseControllerCheck<ExcelDataUpload> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * the Excel port.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelPort excelPort;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataUpload#showPage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testBuildReferences() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final OverseasUser user = createMock( "User", OverseasUser.class );
        final ModelMap model = createModelMap(user, request, null, true, false);
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, user);
		replayAll();

		final String actualReferences = getBaseController().showPage( request, model );

		assertEquals( "The main template is returned", ReflectionTestUtils.getField( getBaseController(), "mainTemplate" ),
                actualReferences );

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataUpload#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testOnSubmitHttpServletRequestHttpServletResponseObjectBindException() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final OverseasUser user = createMock( "User", OverseasUser.class );
        final ModelMap model = createModelMap(user, request, null, true, false);
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, user);
		final long stateId = 07262012l;
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findState(stateId)).andReturn(state);
		replayAll();

		final String actualPage = getBaseController().onSubmit(request, model, stateId );

        assertEquals( "The main template is returned", ReflectionTestUtils.getField( getBaseController(), "mainTemplate" ),
                actualPage );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataUpload#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, java.lang.Long)}
	 * for a multiple part request.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem submitting the form.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testOnSubmitHttpServletRequestHttpServletResponseObjectBindException_multipart() throws Exception {
		final MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
        final OverseasUser user = createMock( "User", OverseasUser.class );
        final ModelMap model = createModelMap(user, request, null, true, false);
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication(authentication, user);
		final long stateId = 07262012l;
		request.setParameter("stateId", Long.toString(stateId));
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findState(stateId)).andReturn(state);
		final MultipartFile leosFile = createMock("LeosFile", MultipartFile.class);
		EasyMock.expect(leosFile.getName()).andReturn("leosFile");
		EasyMock.expect(leosFile.isEmpty()).andReturn(false);
		EasyMock.expect(leosFile.getOriginalFilename()).andReturn("leosFile.xls");
		final InputStream inputStream = createMock("InputStream", InputStream.class);
		EasyMock.expect(leosFile.getInputStream()).andReturn(inputStream).anyTimes();
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> eod = Arrays.asList(localOfficial);
		EasyMock.expect(getExcelPort().readFromExcel(inputStream, state)).andReturn(eod);
        //EasyMock.expect( getExcelPort().checkFileVersion( inputStream ) ).andReturn( true ).anyTimes();
		getLocalOfficialService().saveAllLocalOfficial(eod);
		replayAll();
		request.addFile(leosFile);

        final String actualPage = getBaseController().onSubmit(request, model, stateId );

        assertEquals( "The main template is returned", ReflectionTestUtils.getField( getBaseController(), "mainTemplate" ),
                actualPage );
		verifyAll();
	}


	/**
	 * Gets the Excel port.
	 * 
	 * @author IanBrown
	 * @return the Excel port.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelPort getExcelPort() {
		return excelPort;
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the Excel port.
	 * 
	 * @author IanBrown
	 * @param excelPort
	 *            the Excel port to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setExcelPort(final ExcelPort excelPort) {
		this.excelPort = excelPort;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

    @Override
    protected ExcelDataUpload createBaseController() {
        final ExcelDataUpload excelDataUpload = new ExcelDataUpload();
        excelDataUpload.setLocalOfficialService(getLocalOfficialService());
        excelDataUpload.setExcelPort(getExcelPort());
        return excelDataUpload;
    }

    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/admin/EodDataUpload.jsp";
    }

    @Override
    protected String getExpectedPageTitle() {
        return "Import EOD Data from Excel";
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
        return null;
    }

    @Override
    protected void setUpForBaseController() {
        setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
        setExcelPort(createMock("ExcelPort", ExcelPort.class));
    }

    @Override
    protected void tearDownForBaseController() {
        setExcelPort(null);
        setLocalOfficialService(null);
    }
}
