/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Extended {@link BaseControllerExam} integration test for {@link VotingStateAddress}.
 * 
 * @author IanBrown
 * 
 * @since Jul 30, 2012
 * @version Oct 24, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "StateVotingAddressIntegration-context.xml" })
@DirtiesContext
public final class StateVotingAddressIntegration extends BaseControllerExam<StateVotingAddress> {

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handleGetStateVotingAddress(javax.servlet.http.HttpServletRequest, String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem requesting the state voting address.
	 * @since Jul 30, 2012
	 * @version Oct 24, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testGetStateVotingAddress() throws Exception {
		setUpVipService();
		final String state = "VA";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/StateVotingAddress.htm");
		final FlowType flowType = FlowType.FWAB;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final OverseasUser user = new OverseasUser();
		setUpAuthentication(user);
		wizardResults.setUser(user);
		wizardResults.getVotingAddress().setState(state);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view should be the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handleGetStateVotingAddress(javax.servlet.http.HttpServletRequest, String, String, org.springframework.ui.ModelMap, com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for the case where the state doesn't have specific requirements.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem requesting the state voting address.
	 * @since Jul 30, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testGetStateVotingAddress_notRequired() throws Exception {
		setUpVipService();
		final String state = "OK";
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/StateVotingAddress.htm");
		request.setParameter("votingAddress.state", state);
		final FlowType flowType = FlowType.RAVA;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final OverseasUser user = new OverseasUser();
		setUpAuthentication(user);
		wizardResults.setUser(user);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		assertEquals("The view should redirect", "redirect:" + SurveyWizard.getContinueUrl(request),
				actualModelAndView.getViewName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handlePostStateVotingAddress(HttpServletRequest, ModelMap, OverseasUser, BindingResult, WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 30, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPostStateVotingAddress() throws Exception {
		setUpVipService();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/StateVotingAddress.htm");
		final FlowType flowType = FlowType.RAVA;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		final OverseasUser user = new OverseasUser();
		setUpAuthentication(user);
		wizardResults.setUser(user);
		final UserAddress votingAddress = new UserAddress();
		votingAddress.setStreet1("2 E Guinevere Dr SE");
		votingAddress.setCity("Annandale");
		votingAddress.setState("VA");
		votingAddress.setZip("22003");
		user.setVotingAddress(votingAddress);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		assertEquals("The view should redirect", "redirect:" + SurveyWizard.getContinueUrl(request),
				actualModelAndView.getViewName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateVotingAddress#handlePostStateVotingAddress(HttpServletRequest, ModelMap, OverseasUser, BindingResult, WizardContext)}
	 * for an invalid address.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 30, 2012
	 * @version Aug 1, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPostStateVotingAddress_invalid() throws Exception {
		setUpVipService();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/StateVotingAddress.htm");
		final FlowType flowType = FlowType.FWAB;
		final WizardResults wizardResults = new WizardResults(flowType);
		final WizardContext wizardContext = new WizardContext(wizardResults);
		request.getSession().setAttribute(String.format("%s.currentFlow", WizardContext.class.getName()), flowType);
		request.getSession().setAttribute(String.format("%s.%s", WizardContext.class.getName(), flowType.name()), wizardContext);
		request.getSession().setAttribute(SurveyWizard.class.getName() + ".startPage", "2");
		setUpAuthentication(null);
		wizardResults.setUser(null);
		final OverseasUser user = new OverseasUser();
		wizardContext.setPretenceOfUser(user);
		final UserAddress votingAddress = new UserAddress();
		votingAddress.setStreet1("2 E Guinever Dr SE");
		votingAddress.setCity("Annandale");
		votingAddress.setState("VA");
		votingAddress.setZip("22003");
		user.setVotingAddress(votingAddress);
		wizardResults.setVotingAddress(WizardResultAddress.create(votingAddress));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view should be the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
	}

	/** {@inheritDoc} */
	@Override
	protected final StateVotingAddress createBaseController() {
		final StateVotingAddress stateVotingAddress = applicationContext.getBean(StateVotingAddress.class);
		return stateVotingAddress;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 30, 2012
	 * @version Jul 30, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Sets up the VIP service to have data.
	 * 
	 * @author IanBrown
	 * @throws JAXBException
	 *             if there is a problem parsing the XML.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Jul 28, 2012
	 * @version Oct 11, 2012
	 */
	private void setUpVipService() throws JAXBException, IOException {
		final String source = "src/test/resources/com/bearcode/ovf/tools/vip/vip.xml";
		final File sourceFile = new File(source);
		final FileInputStream fis = new FileInputStream(sourceFile);
		final JAXBContext context = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final VipObject vipObject = (VipObject) unmarshaller.unmarshal(fis);
		fis.close();
		getVipService().convert(vipObject, new Date());
	}
}
