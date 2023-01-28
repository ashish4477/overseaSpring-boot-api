/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.commons.OverseasFormControllerCheck;
import com.bearcode.ovf.forms.AdminCorrectionsListForm;
import com.bearcode.ovf.service.LocalOfficialService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Extended {@link OverseasFormControllerCheck} test for {@link CorrectionsListController}.
 * 
 * @author IanBrown
 * 
 * @since Jul 25, 2012
 * @version Jul 25, 2012
 */
public final class CorrectionsListControllerTest extends BaseControllerCheck<CorrectionsListController> {

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
	 * {@link com.bearcode.ovf.actions.eod.admin.CorrectionsListController#buildReferences(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.AdminCorrectionsListForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        final ModelMap model = createModelMap( null, request, null, true, false );
		final AdminCorrectionsListForm object = getBaseController().formBackingObject();
		final Object correction = createMock("Correction", Object.class);
		final Collection corrections = Arrays.asList(correction);
		EasyMock.expect(getLocalOfficialService().findCorrections(object)).andReturn(corrections);
        addAttributeToModelMap(model, "correctionsList", corrections);
        addOverseasUserToAuthentication(authentication, null);
		replayAll();

		final String actualView = getBaseController().buildReferences(request, model, object );

		//assertNotNull("A references map is returned", actualView);
        assertEquals( "A references map is returned", actualView, "templates/MainTemplate" );
		//assertSame("The corrections are in the map", corrections, );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.CorrectionsListController#formBackingObject()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem creating the form backing object.
	 * @since Jul 25, 2012
	 * @version Jul 25, 2012
	 */
	@Test
	public final void testFormBackingObjectHttpServletRequest() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		replayAll();

		final Object actualFormBackingObject = getBaseController().formBackingObject();

		assertTrue("The form backing object is an admin corrections list form",
				actualFormBackingObject instanceof AdminCorrectionsListForm);
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
    protected CorrectionsListController createBaseController() {
        final CorrectionsListController correctionsListController = new CorrectionsListController();
        correctionsListController.setLocalOfficialService(getLocalOfficialService());
        return correctionsListController;
    }

    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/admin/EodCorrectionsList.jsp";
    }

    @Override
    protected String getExpectedPageTitle() {
        return "List of corrections";
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
    }

    @Override
    protected void tearDownForBaseController() {
        setLocalOfficialService(null);
    }
}
