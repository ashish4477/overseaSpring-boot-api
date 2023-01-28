/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionnaireService;
import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Extended {@link BaseControllerCheck} test for {@link EditQuestion}.
 * 
 * @author IanBrown
 * 
 * @since May 25, 2012
 * @version May 31, 2012
 */
public final class EditQuestionTest extends BaseControllerCheck<EditQuestion> {

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the validator.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private Validator validator;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#deleteQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testDeleteQuestion() {
		final long id = 6223l;
		final long pageId = 78273l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(question.getId()).andReturn(id).atLeastOnce();
		EasyMock.expect(question.getVariants()).andReturn(null).atLeastOnce();
		getQuestionnaireService().deleteQuestion(question);
        EasyMock.expect( getQuestionnaireService().checkUsingInDependencies( question ) ).andReturn( false );
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(question.getPage()).andReturn(page).anyTimes();
		EasyMock.expect(page.getId()).andReturn(pageId).anyTimes();
		replayAll();

		final String actualResponse = getBaseController().deleteQuestion(request, model, question, errors);

		assertEquals("The response is a redirect to the admin edit questionnaire page",
				EditQuestion.REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + pageId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#deleteQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * for the case where there are variants.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testDeleteQuestion_variants() {
		final long id = 6223l;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 4;
		final long pageId = 34435l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		errors.reject("rava.admin.question.variants_not_empty",
				"List of variants isn't empty. Please, delete all variants of the question first.");
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, variant, null, null);
		replayAll();

		final String actualResponse = getBaseController().deleteQuestion(request, model, question, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#deleteQuestionHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteQuestionHierarchy() {
		final long pageId = 78273l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		getQuestionnaireService().deleteQuestionHierarchy(question);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(question.getPage()).andReturn(page).anyTimes();
		EasyMock.expect(page.getId()).andReturn(pageId).anyTimes();
		replayAll();

		final String actualResponse = getBaseController().deleteQuestionHierarchy(request, model, question, errors);

		assertEquals("The response is a redirect to the admin edit questionnaire page",
				EditQuestion.REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + pageId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#deleteQuestionHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * for the case where the question has dependents.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteQuestionHierarchy_dependents() {
		final long id = 298l;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 72;
		final long pageId = 98123l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		getQuestionnaireService().deleteQuestionHierarchy(question);
		EasyMock.expectLastCall().andThrow(new IllegalStateException("Page has dependencies")).anyTimes();
		errors.reject(EditQuestion.REJECT_DEPENDENTS_ERROR_CODE, EditQuestion.REJECT_DEPENDENTS_ERROR_MESSAGE);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BasicDependency key = createMock("Key", BasicDependency.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, variant, key, dependentVariant);
		replayAll();

		final String actualResponse = getBaseController().deleteQuestionHierarchy(request, model, question, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#moveQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult, Long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testMoveQuestion() {
		final long newPageId = 872l;
		final int newOrder = 2;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final QuestionnairePage newPage = createMock("NewPage", QuestionnairePage.class);
		EasyMock.expect(getQuestionnaireService().findPageById(newPageId)).andReturn(newPage).atLeastOnce();
		final Question newQuestion = createMock("NewQuestion", Question.class);
		final List<Question> newQuestions = Arrays.asList(newQuestion);
		EasyMock.expect(newPage.getQuestions()).andReturn(newQuestions).atLeastOnce();
		EasyMock.expect(newQuestion.getOrder()).andReturn(newOrder).atLeastOnce();
		question.setPage(newPage);
		question.setOrder(newOrder + 1);
		question.setOldOrder(newOrder + 1);
		getQuestionnaireService().saveQuestion(question);
        final BindingResult errors = createMock( "errors", BindingResult.class );
        EasyMock.expect( errors.hasErrors() ).andReturn( false ).anyTimes();
		replayAll();
		
		final String actualResponse = getBaseController().moveQuestion(request, model, question, errors, newPageId);
		
		assertEquals("The response is a redirect to the admin edit questionnaire page",
				EditQuestion.REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + newPageId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#moveQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult, Long)}
	 * for the case where the new page has no existing questions.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testMoveQuestion_noQuestions() {
		final long newPageId = 872l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final QuestionnairePage newPage = createMock("NewPage", QuestionnairePage.class);
		EasyMock.expect(getQuestionnaireService().findPageById(newPageId)).andReturn(newPage).atLeastOnce();
		EasyMock.expect(newPage.getQuestions()).andReturn(null).atLeastOnce();
		question.setPage(newPage);
		question.setOrder(2);
		question.setOldOrder(2);
		getQuestionnaireService().saveQuestion(question);
        final BindingResult errors = createMock( "errors", BindingResult.class );
        EasyMock.expect( errors.hasErrors() ).andReturn( false ).anyTimes();
		replayAll();
		
		final String actualResponse = getBaseController().moveQuestion(request, model, question, errors, newPageId);
		
		assertEquals("The response is a redirect to the admin edit questionnaire page",
				EditQuestion.REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + newPageId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#saveQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testSaveQuestion() {
		final long id = 62782l;
		final long pageId = 3972l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false).atLeastOnce();
		EasyMock.expect(question.getId()).andReturn(id).atLeastOnce();
		getQuestionnaireService().saveQuestion(question);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(question.getPage()).andReturn(page).atLeastOnce();
		EasyMock.expect(page.getId()).andReturn(pageId).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().saveQuestion(request, model, question, errors);

		assertEquals("The response is a redirect to the admin edit questionnaire page",
				EditQuestion.REDIRECT_ADMIN_EDIT_QUESTIONNAIRE_PAGE + "?id=" + pageId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#saveQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * for a question with errors.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testSaveQuestion_errors() {
		final long id = 4682l;
		final PageType type = PageType.OVERSEAS;
		final int pageNumber = 6;
		final long pageId = 263l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(true).atLeastOnce();
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, null, null, null);
		replayAll();

		final String actualResponse = getBaseController().saveQuestion(request, model, question, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#saveQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question, org.springframework.validation.BindingResult)}
	 * for a new question.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testSaveQuestion_newQuestion() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false).atLeastOnce();
		setUpForShowQuestion(model, question, 0l, null, null, null, null, null, null);
		getQuestionnaireService().saveQuestion(question);
		replayAll();

		final String actualResponse = getBaseController().saveQuestion(request, model, question, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#showQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question)}
	 * for a question with a dependent.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowQuestion_dependent() {
		final long id = 1349l;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 1;
		final long pageId = 8671l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BasicDependency key = createMock("Key", BasicDependency.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, variant, key, dependentVariant);
		replayAll();

		final String actualResponse = getBaseController().showQuestion(request, model, question);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#showQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question)}
	 * for a simple question without even an ID.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowQuestion_noId() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		setUpForShowQuestion(model, question, 0l, null, null, null, null, null, null);
		replayAll();

		final String actualResponse = getBaseController().showQuestion(request, model, question);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#showQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question)}
	 * for a simple question without variants.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowQuestion_noVariants() {
		final long id = 1349l;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 1;
		final long pageId = 8671l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, null, null, null);
		replayAll();

		final String actualResponse = getBaseController().showQuestion(request, model, question);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#showQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question)}
	 * for a question with a variant.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowQuestion_variant() {
		final long id = 1349l;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 1;
		final long pageId = 8671l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, variant, null, null);
		replayAll();

		final String actualResponse = getBaseController().showQuestion(request, model, question);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestion#showQuestion(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.Question)}
	 * for a question with a variant with a key.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowQuestion_variantWithKey() {
		final long id = 1349l;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 1;
		final long pageId = 8671l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BasicDependency key = createMock("Key", BasicDependency.class);
		setUpForShowQuestion(model, question, id, type, pageNumber, pageId, variant, key, null);
		replayAll();

		final String actualResponse = getBaseController().showQuestion(request, model, question);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditQuestion createBaseController() {
		final EditQuestion editQuestion = new EditQuestion();
		editQuestion.setQuestionnaireService(getQuestionnaireService());
		editQuestion.setValidator(getValidator());
		return editQuestion;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return EditQuestion.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return EditQuestion.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return EditQuestion.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return EditQuestion.DEFAULT_SECTION_NAME;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSuccessContentBlock() {
		return null;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setQuestionnaireService(createMock("QuestionnaireService", QuestionnaireService.class));
		setValidator(createMock("Validator", Validator.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setValidator(null);
		setQuestionnaireService(null);
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Gets the validator.
	 * 
	 * @author IanBrown
	 * @return the validator.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private Validator getValidator() {
		return validator;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Sets up to show a question.
	 * 
	 * @author IanBrown
	 * @param model
	 *            the model.
	 * @param question
	 *            the question.
	 * @param id
	 *            the question identifier.
	 * @param type
	 *            the page type.
	 * @param pageNumber
	 *            the page number.
	 * @param pageId
	 *            the page identifier.
	 * @param variant
	 *            the variant for the question (if any).
	 * @param key
	 *            the variant key (if any).
	 * @param dependentVariant
	 *            the dependent variant (if any).
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void setUpForShowQuestion(final ModelMap model, final Question question, final long id, final PageType type,
			final Integer pageNumber, final Long pageId, final QuestionVariant variant, final BasicDependency key,
			final QuestionVariant dependentVariant) {
		EasyMock.expect(question.getId()).andReturn(id).atLeastOnce();
		if (id != 0l) {
			if (dependentVariant == null) {
				EasyMock.expect(getQuestionnaireService().checkUsingInDependencies(question)).andReturn(false).atLeastOnce();
				addAttributeToModelMap(model, "keyQuestion", false);
			} else {
				EasyMock.expect(getQuestionnaireService().checkUsingInDependencies(question)).andReturn(true).atLeastOnce();
                final QuestionDependency dependency = createMock( "dependency", QuestionDependency.class );
                EasyMock.expect( dependency.getDependent() ).andReturn( dependentVariant ).anyTimes();
				final Collection<QuestionDependency> dependents = Arrays.asList(dependency);
                final Collection<QuestionVariant> dependentVariants = Arrays.asList( dependentVariant );
				EasyMock.expect(getQuestionnaireService().findDependents(question)).andReturn(dependents)
						.atLeastOnce();
				addAttributeToModelMap(model, "keyQuestion", true);
				addAttributeToModelMap(model, "dependentVariants", dependentVariants);
			}
			final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
			EasyMock.expect(question.getPage()).andReturn(page).atLeastOnce();
			EasyMock.expect(page.getType()).andReturn(type).atLeastOnce();
			final List<QuestionnairePage> pages = Arrays.asList(page);
			EasyMock.expect(getQuestionnaireService().findQuestionnairePages(type)).andReturn(pages).anyTimes();
			EasyMock.expect(page.getNumber()).andReturn(pageNumber).atLeastOnce();
			EasyMock.expect(page.getId()).andReturn(pageId).atLeastOnce();
			final List<Question> questions = Arrays.asList(question);
			EasyMock.expect(page.getQuestions()).andReturn(questions).atLeastOnce();
			addAttributeToModelMap(model, "minNumber", pageNumber - 1);
			addAttributeToModelMap(model, "maxNumber", pageNumber + 1);
			addAttributeToModelMap(model, "pages", pages);
			if (variant == null) {
				EasyMock.expect(question.getVariants()).andReturn(null).atLeastOnce();
			} else {
				final Collection<QuestionVariant> variants = Arrays.asList(variant);
				EasyMock.expect(question.getVariants()).andReturn(variants).atLeastOnce();
				if (key == null) {
					EasyMock.expect(variant.getKeys()).andReturn(null).atLeastOnce();
				} else {
					final Collection<BasicDependency> keys = Arrays.asList(key);
					EasyMock.expect(variant.getKeys()).andReturn(keys).atLeastOnce();
				}
			}
		}
	}

	/**
	 * Sets the validator.
	 * 
	 * @author IanBrown
	 * @param validator
	 *            the validator to set.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private void setValidator(final Validator validator) {
		this.validator = validator;
	}
}
