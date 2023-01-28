/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.admin;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Collection;

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
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;

/**
 * Extended {@link BaseControllerExam} integration test for {@link EditQuestion}.
 * 
 * @author IanBrown
 * 
 * @since May 25, 2012
 * @version Jun 27, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "WizardAdminIntegration-context.xml" })
public final class EditQuestionIntegration extends BaseControllerExam<EditQuestion> {

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
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#deleteQuestionHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since May 25, 2012
	 * @version Jun 27, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/questionnaire/admin/WizardAdmin.xml" })
	public final void testDeleteQuestionHierarchy() throws Exception {
		final Question question = questionDAO.findQuestionById(2l);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/admin/EditQuestionGroup.htm");
		request.setMethod("POST");
		request.setParameter("deleteHierarchy", "deleteHierarchy");
		request.addParameter("id", Long.toString(question.getId()));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The view is a redirection to the edit questionnaire page admin page",
				EditQuestion.REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + question.getPage().getId(),
				actualModelAndView.getViewName());
		final ModelMap modelMap = actualModelAndView.getModelMap();
		final String key = BindingResult.MODEL_KEY_PREFIX + "question";
		final BindingResult bindingResult = (BindingResult) modelMap.get(key);
		assertEquals("There were no errors", 0, bindingResult.getErrorCount());
		assertNull("The question " + question.getTitle() + " has been deleted", questionDAO.findQuestionById(question.getId()));
		for (final QuestionVariant variant : question.getVariants()) {
			assertNull("The variant " + variant.getTitle() + " has been deleted",
					questionDAO.findQuestionVariantById(variant.getId()));
			for (final BasicDependency checkKey : variant.getKeys()) {
				assertNull("The key " + checkKey.getId() + " has been deleted",
						questionDAO.findQuestionDependencyById(checkKey.getId()));
			}
			for (final QuestionField field : variant.getFields()) {
				assertNull("The field " + field.getId() + " has been deleted", questionFieldDAO.getById(field.getId()));
			}
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#deleteQuestionHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * for the case where the question has dependents.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@SuppressWarnings("unchecked")
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/questionnaire/admin/WizardAdmin.xml" })
	public final void testDeleteQuestionHierarchy_dependents() throws Exception {
		final Question question = questionDAO.findQuestionById(1l);
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setRequestURI("/admin/EditQuestionGroup.htm");
		request.setMethod("POST");
		request.setParameter("deleteHierarchy", "deleteHierarchy");
		request.addParameter("id", Long.toString(question.getId()));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertEquals("The main template is used as the view", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		final ModelMap modelMap = actualModelAndView.getModelMap();
		final String key = BindingResult.MODEL_KEY_PREFIX + "question";
		final BindingResult bindingResult = (BindingResult) modelMap.get(key);
		assertEquals("There was one error", 1, bindingResult.getErrorCount());
		assertEquals("There is a dependent variant", 1, ((Collection<QuestionVariant>) modelMap.get("dependentVariants")).size());
	}

	/** {@inheritDoc} */
	@Override
	protected final EditQuestion createBaseController() {
		final EditQuestion editQuestion = applicationContext.getBean(EditQuestion.class);
		return editQuestion;
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
