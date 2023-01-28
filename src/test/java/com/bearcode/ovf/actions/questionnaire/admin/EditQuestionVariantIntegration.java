/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.DAO.QuestionDAO;
import com.bearcode.ovf.DAO.QuestionFieldDAO;
import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;

/**
 * Extended {@link BaseControllerExam} integration test for {@link EditQuestionVariant}.
 * 
 * @author IanBrown
 * 
 * @since May 25, 2012
 * @version Jun 27, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "WizardAdminIntegration-context.xml" })
public final class EditQuestionVariantIntegration extends BaseControllerExam<EditQuestionVariant> {

	/**
	 * the question DAO.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Autowired
	private QuestionDAO questionDAO;

	/**
	 * the question field DAO.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Autowired
	private QuestionFieldDAO questionFieldDAO;

/**
	 * Test method for {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#cloneVariant(javax.servlet.http.HttpServletRequest, ModelMap, QuestionVariant)|.
	 * @author IanBrown
	 * @throws Exception if there is a problem handling the request.
	 * @since May 29, 2012
	 * @version May 29, 2012
	 */
	@Ignore
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/questionnaire/admin/WizardAdmin.xml" })
	public final void testCloneVariant() throws Exception {
		final QuestionVariant variant = questionDAO.findQuestionVariantById(2l);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/admin/EditQuestionVariant.htm");
		request.setMethod("GET");
		request.setParameter("cloneVariant", "cloneVariant");
		request.addParameter("id", Long.toString(variant.getId()));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The main template is returned as the view",
				ReflectionTestUtils.getField(getBaseController(), "mainTemplate"), actualModelAndView.getViewName());
		final ModelMap model = actualModelAndView.getModelMap();
		final QuestionVariant newVariant = (QuestionVariant) model.get("variant");
		assertNotNull("There is a new variant", newVariant);
		assertTrue("The new variant has an identifier", 0l != newVariant.getId());
		assertTrue("The new variant has a different identifier than the old", variant.getId() != newVariant.getId());
		assertEquals("The new variant belongs to the same question", variant.getQuestion(), newVariant.getQuestion());
		assertEquals("The new variant has the same number of fields", variant.getFields().size(), newVariant.getFields().size());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#deleteVariantHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since May 25, 2012
	 * @version Jun 27, 2012
	 */
	@Ignore
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/questionnaire/admin/WizardAdmin.xml" })
	public final void testDeleteVariantHierarchy() throws Exception {
		final QuestionVariant variant = questionDAO.findQuestionVariantById(2l);
		final Collection<BasicDependency> keys = variant.getKeys();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/admin/EditQuestionVariant.htm");
		request.setMethod("POST");
		request.setParameter("deleteHierarchy", "deleteHierarchy");
		request.addParameter("id", Long.toString(variant.getId()));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The view is a redirection to the edit question admin page", EditQuestionVariant.REDIRECT_ADMIN_EDIT_QUESTION
				+ "?id=" + variant.getQuestion().getId(), actualModelAndView.getViewName());
		final ModelMap modelMap = actualModelAndView.getModelMap();
		final String key = BindingResult.MODEL_KEY_PREFIX + "variant";
		final BindingResult bindingResult = (BindingResult) modelMap.get(key);
		assertEquals("There were no errors", 0, bindingResult.getErrorCount());
		assertNull("The variant " + variant.getTitle() + " has been deleted", questionDAO.findQuestionVariantById(variant.getId()));
		for (final BasicDependency checkKey : keys) {
			assertNull("The dependency " + checkKey + " has been deleted", questionDAO.findQuestionDependencyById(checkKey.getId()));
		}
		for (final QuestionField field : variant.getFields()) {
			assertNull("The field " + field.getId() + " has been deleted", questionFieldDAO.getById(field.getId()));
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#deleteVariantHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * for the case where the question containing the variant has dependents.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Ignore
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/questionnaire/admin/WizardAdmin.xml" })
	public final void testDeleteVariantHierarchy_dependents() throws Exception {
		final QuestionVariant variant = questionDAO.findQuestionVariantById(1l);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/admin/EditQuestionVariant.htm");
		request.setMethod("POST");
		request.setParameter("deleteHierarchy", "deleteHierarchy");
		request.addParameter("id", Long.toString(variant.getId()));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The main template is used as the view", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap modelMap = actualModelAndView.getModelMap();
		final String key = BindingResult.MODEL_KEY_PREFIX + "variant";
		final BindingResult bindingResult = (BindingResult) modelMap.get(key);
		assertEquals("There was one error", 1, bindingResult.getErrorCount());
		assertEquals("There is a dependent variant", 1, ((Collection<QuestionVariant>) modelMap.get("dependentVariants")).size());
	}

	/** {@inheritDoc} */
	@Override
	protected final EditQuestionVariant createBaseController() {
		final EditQuestionVariant editQuestionVariant = applicationContext.getBean(EditQuestionVariant.class);
		return editQuestionVariant;
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
