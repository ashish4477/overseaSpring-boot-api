package com.bearcode.ovf.actions.mva;
import com.bearcode.ovf.DAO.OverseasUserDAO;
import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.VoterHistory;
import org.junit.Test;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

/**
 * @author Daemmon Hughes
 * @date Feb 8, 2012
 */
   @RunWith(SpringJUnit4ClassRunner.class)
   @ContextConfiguration(locations = { "UserAccountIntegration-context.xml" })
   public final class UserAccountIntegration extends BaseControllerExam<UserAccount>{


    @Test
    @OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/commons/OverseasUser.xml" })
    public final void testUserIsInUpdateDisplayModel() throws Exception {
        setUpUserAuth();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/UpdateAccount.htm");
        request.setMethod("GET");
        request.setSession(new MockHttpSession());

        final ModelAndView displayedModelAndView = getHandlerAdapter().handle(request, response, getBaseController() );

        final ModelMap displayedModel = displayedModelAndView.getModelMap();
        assertNotNull("A model map is used as the model", displayedModel);
        assertTrue("Model has a user key",displayedModel.containsKey("user"));
        Object displayededUserObj = displayedModel.get("user");
        assertNotNull("Updated User object is not null", displayededUserObj);
        assertTrue("Updated User object is OversesUser", displayededUserObj instanceof OverseasUser);
        OverseasUser displayedUser = (OverseasUser)displayededUserObj;
        assertNotNull("displayedUser is null", displayedUser);
    }

    @Test
    @OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/commons/OverseasUser.xml" })
    public final void testUserIsUpdated() throws Exception {
        setUpUserAuth();

        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        request.setRequestURI("/UpdateAccount.htm");
        request.setMethod("GET");
        request.setSession(new MockHttpSession());

        final ModelAndView displayedModelAndView = getHandlerAdapter().handle(request, response, getBaseController() );

        OverseasUser displayedUser = (OverseasUser)displayedModelAndView.getModelMap().get("user");

        // change something
        displayedUser.setGender("F");
        displayedUser.setVoterHistory(VoterHistory.DOMESTIC_VOTER);

        // submit the update form
        request.setMethod("POST");
        final ModelAndView updatedModelAndView = getHandlerAdapter().handle(request, response, getBaseController() );
        final ModelMap updatedModel = updatedModelAndView.getModelMap();

        // check the updated model
        assertTrue("Model has a user key",updatedModel.containsKey("user"));
        Object updatedUserObj = updatedModel.get("user");
        assertNotNull("Updated User object is not null", updatedUserObj);
        assertTrue("Updated User object is OversesUser", updatedUserObj instanceof OverseasUser);
        OverseasUser updatedUser = (OverseasUser)updatedUserObj;
        assertNotNull("displayedUser is null", updatedUser);

        // compare with the stored item
        OverseasUser storedUser = getUser();
        assertEquals("Stored user and updated user.gender are the same", updatedUser.getGender(),storedUser.getGender());

    }


    @Override
    protected UserAccount createBaseController(){
        final UserAccount userAccount = applicationContext.getBean(UserAccount.class);
        return userAccount;
    }

    @Override
    protected void setUpForBaseController(){

    }

    @Override
    protected void tearDownForBaseController(){

    }


    private OverseasUser getUser()  {
       final OverseasUserDAO overseasUserDAO = applicationContext.getBean(OverseasUserDAO.class);
       return overseasUserDAO.findById(1);
    }

    private void setUpUserAuth(){
       OverseasUser user = getUser();
       UsernamePasswordAuthenticationToken t = new UsernamePasswordAuthenticationToken(
               user, "test", user.getAuthorities() );
       SecurityContextHolder.getContext().setAuthentication( t );
    }
   }
