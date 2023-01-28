/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.admin;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.PageClassType;
import com.bearcode.ovf.model.questionnaire.PageType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.service.QuestionnaireService;

/**
 * Extended {@link BaseControllerCheck} test for {@link EditQuestionnairePage}.
 * 
 * @author IanBrown
 * 
 * @since May 24, 2012
 * @version May 31, 2012
 */
public final class EditQuestionnairePageTest extends BaseControllerCheck<EditQuestionnairePage> {

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#deletePage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testDeletePage() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(page.getQuestions()).andReturn(null).atLeastOnce();
		getQuestionnaireService().deletePage(page);
		replayAll();

		final String actualResponse = getBaseController().deletePage(request, model, page, errors);

		assertEquals("The response is a redirection to admin questionnaire pages",
				EditQuestionnairePage.REDIRECT_ADMIN_QUESTIONNAIRE_PAGES, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#deletePage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * for a page with questions.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testDeletePage_questions() {
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 1;
		final long pageId = 1l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(page.getQuestions()).andReturn(questions).atLeastOnce();
        errors.reject("rava.admin.page.questions_not_empty","List of questions isn't empty. Please, delete all questions of the page first.");
		setUpShowForm(model, page, type, pageId, pageNumber, null, null);
		replayAll();

		final String actualResponse = getBaseController().deletePage(request, model, page, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#deletePageHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeletePageHierarchy() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		getQuestionnaireService().deletePageHierarchy(page);
		replayAll();

		final String actualResponse = getBaseController().deletePageHierarchy(request, model, page, errors);

		assertEquals("The response is a redirection to admin questionnaire pages",
				EditQuestionnairePage.REDIRECT_ADMIN_QUESTIONNAIRE_PAGES, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#deletePageHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * for the case where the page has questions with dependents.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeletePageHierarchy_dependents() {
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 1;
		final long pageId = 1l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		getQuestionnaireService().deletePageHierarchy(page);
		EasyMock.expectLastCall().andThrow(new IllegalStateException("Page has dependencies")).anyTimes();
		errors.reject(EditQuestionnairePage.REJECT_DEPENDENTS_ERROR_CODE, EditQuestionnairePage.REJECT_DEPENDENTS_ERROR_MESSAGE);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpShowForm(model, page, type, pageId, pageNumber, dependentVariant, null);
		replayAll();

		final String actualResponse = getBaseController().deletePageHierarchy(request, model, page, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#savePage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testSavePage() {
		final long pageId = 872l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false).atLeastOnce();
		EasyMock.expect(page.getId()).andReturn(pageId).atLeastOnce();
		getQuestionnaireService().savePage(page);
		replayAll();

		final String actualResponse = getBaseController().savePage(request, model, page, errors);

		assertEquals("The response is a redirection to admin questionnaire pages",
				EditQuestionnairePage.REDIRECT_ADMIN_QUESTIONNAIRE_PAGES, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#savePage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * for the case where there are errors.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testSavePage_errors() {
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pageNumber = 4;
		final long pageId = 8172l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(true).atLeastOnce();
		setUpShowForm(model, page, type, pageId, pageNumber, null, null);
		replayAll();

		final String actualResponse = getBaseController().savePage(request, model, page, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#savePage(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.validation.BindingResult)}
	 * for a new page.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testSavePage_newPage() {
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		final int pageNumber = 2;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false).atLeastOnce();
		setUpShowForm(model, page, type, 0l, pageNumber, null, null);
		getQuestionnaireService().savePage(page);
		replayAll();

		final String actualResponse = getBaseController().savePage(request, model, page, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#showForm(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.ui.ModelMap)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowForm() {
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		final int pageNumber = 2;
		final long pageId = 128l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		setUpShowForm(model, page, type, pageId, pageNumber, null, null);
		replayAll();

		final String actualResponse = getBaseController().showForm(request, page, model);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#showForm(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.ui.ModelMap)}
	 * for a cross page connection.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowForm_crossPageConnection() {
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		final int pageNumber = 2;
		final long pageId = 128l;
		final int otherPageNumber = 3;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpShowForm(model, page, type, pageId, pageNumber, dependentVariant, otherPageNumber);
		replayAll();

		final String actualResponse = getBaseController().showForm(request, page, model);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionnairePage#showForm(javax.servlet.http.HttpServletRequest, com.bearcode.ovf.model.questionnaire.QuestionnairePage, org.springframework.ui.ModelMap)}
	 * for a page with a dependent.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowForm_dependent() {
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		final int pageNumber = 2;
		final long pageId = 128l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpShowForm(model, page, type, pageId, pageNumber, dependentVariant, null);
		replayAll();

		final String actualResponse = getBaseController().showForm(request, page, model);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditQuestionnairePage createBaseController() {
		final EditQuestionnairePage editQuestionnairePage = new EditQuestionnairePage();
		editQuestionnairePage.setQuestionnaireService(getQuestionnaireService());
		return editQuestionnairePage;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return EditQuestionnairePage.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return EditQuestionnairePage.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return EditQuestionnairePage.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return EditQuestionnairePage.DEFAULT_SECTION_NAME;
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
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setQuestionnaireService(null);
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Sets up to show the form.
	 * 
	 * @author IanBrown
	 * @param model
	 *            the model.
	 * @param page
	 *            the page.
	 * @param type
	 *            the type of page.
	 * @param pageId
	 *            the page identifier.
	 * @param pageNumber
	 *            the page number.
	 * @param dependentVariant
	 *            the dependent variant (if any).
	 * @param otherPageNumber
	 *            the other page number (or null) if none.
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void setUpShowForm(final ModelMap model, final QuestionnairePage page, final PageType type, final Long pageId,
			final Integer pageNumber, final QuestionVariant dependentVariant, final Integer otherPageNumber) {
		EasyMock.expect(page.getId()).andReturn(pageId).atLeastOnce();
		if (pageId == 0l) {
			addAttributeToModelMap(model, EasyMock.eq("pageClasses"), EasyMock.aryEq(PageClassType.values()));
		}
		EasyMock.expect(page.getType()).andReturn(type).atLeastOnce();
		final List<QuestionnairePage> pages = new ArrayList<QuestionnairePage>(Arrays.asList(page));
		EasyMock.expect(getQuestionnaireService().findQuestionnairePages(type)).andReturn(pages).atLeastOnce();
		EasyMock.expect(page.getNumber()).andReturn(pageNumber).atLeastOnce();
		final Map<Integer, Collection<Integer>> crossPageConnections = new HashMap<Integer, Collection<Integer>>();
		if (otherPageNumber != null) {
			final Collection<Integer> otherPages = Arrays.asList(otherPageNumber);
			crossPageConnections.put(pageNumber, otherPages);
			final QuestionnairePage otherPage = createMock("OtherPage", QuestionnairePage.class);
			pages.add(otherPage);
			EasyMock.expect(otherPage.getNumber()).andReturn(otherPageNumber).atLeastOnce();
			final QuestionnairePage extraPage = createMock("ExtraPage", QuestionnairePage.class);
			pages.add(extraPage);
			EasyMock.expect(extraPage.getNumber()).andReturn(Math.max(pageNumber, otherPageNumber) + 1).atLeastOnce();
		}
		EasyMock.expect(getQuestionnaireService().findCrossPageConnection(type)).andReturn(crossPageConnections).atLeastOnce();
		addAttributeToModelMap(model, "minNumber", pageNumber - 1);
		addAttributeToModelMap(model, "maxNumber", pageNumber + 1);
		addAttributeToModelMap(model, "pages", pages);
		EasyMock.expect(model.get("pageClass")).andReturn(PageClassType.GENERAL.name()).atLeastOnce();
		addAttributeToModelMap(model, EasyMock.eq("additionalBehavior"), EasyMock.anyObject());
		if (dependentVariant == null) {
			EasyMock.expect(getQuestionnaireService().findDependentVariants(page)).andReturn(null).atLeastOnce();
			addAttributeToModelMap(model, "dependentVariants", null);
		} else {
			final Collection<QuestionVariant> dependentVariants = Arrays.asList(dependentVariant);
			EasyMock.expect(getQuestionnaireService().findDependentVariants(page)).andReturn(dependentVariants).atLeastOnce();
			addAttributeToModelMap(model, "dependentVariants", dependentVariants);
		}
	}
}
