/**
 * 
 */
package com.bearcode.ovf.actions.commons;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.ui.ModelMap;

import static org.junit.Assert.assertEquals;

/**
 * Extended {@link OverseasFormControllerCheck} test for
 * {@link CrossPlatformBridge}.
 * 
 * @author IanBrown
 * 
 * @since Dec 20, 2011
 * @version Dec 21, 2011
 */
public final class CrossPlatformBridgeTest extends
		BaseControllerCheck<CrossPlatformBridge> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.CrossPlatformBridge#drupalBringe(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, String)}
	 * for the case where there is no requested page.
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
	public final void testBuildReferences_noRequestedPage() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication( authentication, null );
        final ModelMap model = createModelMap(null, request, null, true, false);
        addAttributeToModelMap(model,"reqContent","");
		replayAll();

		final String actualReferences = getBaseController()
				.drupalBringe( request, model, "" );

		verifyAll();
	}



    @Test
    public final void testFixDrupalUrl() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final String urlOne = "http://www.test.org";
        final String urlTwo = "www.test.org/http_test";

        assertEquals("Url with 'http' returns untouched", getBaseController().fixUrlScheme( request, urlOne ), urlOne );
        assertEquals("'Https' was added to the url", getBaseController().fixUrlScheme( request, urlTwo ),
                "https://www.test.org/http_test");
    }

    @Override
    protected final CrossPlatformBridge createBaseController() {
        return new CrossPlatformBridge();
    }

    @Override
    protected final String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/DrupalContent.jsp";
    }

    @Override
    protected final String getExpectedPageTitle() {
        return "Home";
    }

    @Override
    protected final String getExpectedSectionCss() {
        return "/css/drupal.css";
    }

    @Override
    protected final String getExpectedSectionName() {
        return "drupal";
    }

    @Override
    protected final String getExpectedSuccessContentBlock() {
        return null;
    }

    @Override
    protected void setUpForBaseController() {
    }

    @Override
    protected void tearDownForBaseController() {
    }
}
