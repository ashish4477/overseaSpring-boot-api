/**
 * 
 */
package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.commons.OverseasFormControllerCheck;
import com.bearcode.ovf.forms.AdminStatisticsForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.service.OverseasUserService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Extended {@link OverseasFormControllerCheck} test for {@link BaseStatistics}.
 * 
 * @author IanBrown
 * 
 * @since Dec 21, 2011
 * @version Dec 22, 2011
 */
public final class BaseStatisticsTest extends
        BaseControllerCheck<BaseStatistics> {

	/**
	 * the user service.
	 * 
	 * @author IanBrown
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private OverseasUserService userService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.commons.admin.BaseStatistics#showStatistics(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.forms.AdminStatisticsForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 21, 2011
	 * @version Dec 22, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final AdminStatisticsForm object = createMock("Object",
				AdminStatisticsForm.class);
		final int users = 987;
		EasyMock.expect(getUserService().countUsers()).andReturn(users);
		final int realUsers = 789;
		EasyMock.expect(getUserService().countRealUsers()).andReturn(realUsers);
		final Map<State, Long> usersByState = new HashMap<State, Long>();
		EasyMock.expect(getUserService().countUsersByState( object )).andReturn(
				usersByState);
		final Collection pdfGenerations = new ArrayList();
		EasyMock.expect(getUserService().countPdfGenerations( object ))
				.andReturn( pdfGenerations );
        final Authentication authentication = addAuthenticationToSecurityContext();
        addOverseasUserToAuthentication( authentication, null );
        final ModelMap model = createModelMap(null, request, null, true, false);
        addAttributeToModelMap(model,"usersCount",users);
        addAttributeToModelMap(model,"realUsers", realUsers);
        addAttributeToModelMap(model,"usersByState", usersByState);
        addAttributeToModelMap(model,"pdfStats", pdfGenerations);
        replayAll();

		final String actualModelAndView = getBaseController()
				.showStatistics( request, model, object );

        assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
                actualModelAndView);
		verifyAll();
	}

	/**
	 * Gets the user service.
	 * 
	 * @author IanBrown
	 * @return the user service.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private OverseasUserService getUserService() {
		return userService;
	}

	/**
	 * Sets the user service.
	 * 
	 * @author IanBrown
	 * @param userService
	 *            the user service to set.
	 * @since Dec 21, 2011
	 * @version Dec 21, 2011
	 */
	private void setUserService(final OverseasUserService userService) {
		this.userService = userService;
	}

    @Override
    protected final BaseStatistics createBaseController() {
        final BaseStatistics baseStatistics = new BaseStatistics();
        ReflectionTestUtils.setField( baseStatistics,"userService",getUserService() );
        return baseStatistics;
    }

    @Override
    protected String getExpectedContentBlock() {
        return "/WEB-INF/pages/blocks/admin/Statistics.jsp";
    }

    @Override
    protected String getExpectedPageTitle() {
        return "Statistics";
    }

    @Override
    protected String getExpectedSectionCss() {
        return "/css/admin.css";
    }

    @Override
    protected String getExpectedSectionName() {
        return "admin";
    }

    @Override
    protected String getExpectedSuccessContentBlock() {
        return null;
    }

    @Override
    protected void setUpForBaseController() {
        setUserService(createMock("UserService", OverseasUserService.class));
    }

    @Override
    protected void tearDownForBaseController() {
        setUserService( null );
    }
}
