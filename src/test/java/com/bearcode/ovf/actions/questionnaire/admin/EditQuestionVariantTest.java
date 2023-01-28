/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire.admin;

import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionDependency;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.service.QuestionFieldService;
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

import static org.junit.Assert.assertEquals;

/**
 * Extended {@link BaseControllerCheck} test for {@link EditQuestionVariant}.
 * 
 * @author IanBrown
 * 
 * @since May 25, 2012
 * @version May 31, 2012
 */
public final class EditQuestionVariantTest extends BaseControllerCheck<EditQuestionVariant> {

	/**
	 * the questionnaire service.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the question field service.
	 * 
	 * @author IanBrown
	 * @since May 30, 2012
	 * @version May 30, 2012
	 */
	private QuestionFieldService questionFieldService;

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
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#cloneVariant(javax.servlet.http.HttpServletRequest, ModelMap, QuestionVariant)}
	 * .
	 * 
	 * @author IanBrown
	 * @since May 29, 2012
	 * @version May 30, 2012
	 */
	@Test
	public final void testCloneVariant() {
		final long newVariantId = 32767l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final QuestionVariant newVariant = createMock("NewVariant", QuestionVariant.class);
		EasyMock.expect(getQuestionnaireService().cloneVariant(variant)).andReturn(newVariant);
		final QuestionField newField = createMock("NewField", QuestionField.class);
		getQuestionnaireService().saveQuestionVariant(newVariant);
		getQuestionnaireService().refresh(newVariant);
		EasyMock.expectLastCall().atLeastOnce();
		getQuestionFieldService().saveQuestionField(newField);
		EasyMock.expect(model.put(EasyMock.eq("variant"), EasyMock.anyObject())).andReturn(variant).atLeastOnce();
		final Question question = createMock("Question", Question.class);
		setUpForShowVariant(model, newVariant, newVariantId, newField, question, null);
		replayAll();

		final String actualResponse = getBaseController().cloneVariant(request, model, variant);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#deleteVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * for a variant with fields.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteVariant_fields() {
		final long id = 32767l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		final Question question = createMock("Question", Question.class);
		final QuestionField field = createMock("Field", QuestionField.class);
        errors.reject("rava.admin.variant.fields_not_empty","List of fields isn't empty. Please, delete all fields first.");
		setUpForShowVariant(model, variant, id, field, question, null);
		replayAll();

		final String actualResponse = getBaseController().deleteVariant(request, model, variant, errors);

		assertEquals("The main template is returned", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#deleteVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteVariant() {
		final long questionId = 812787l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(variant.getFields()).andReturn(null).atLeastOnce();
		getQuestionnaireService().deleteQuestionVariant(variant);
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
		EasyMock.expect(question.getId()).andReturn(questionId).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().deleteVariant(request, model, variant, errors);

		assertEquals("The response is a redirect to the admin edit question page", EditQuestionVariant.REDIRECT_ADMIN_EDIT_QUESTION
				+ "?id=" + questionId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#deleteVariantHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteVariantHierarchy() {
		final long questionId = 76319l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		getQuestionnaireService().deleteQuestionVariantHierarchy(variant);
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
		EasyMock.expect(question.getId()).andReturn(questionId).anyTimes();
		replayAll();

		final String actualResponse = getBaseController().deleteVariantHierarchy(request, model, variant, errors);

		assertEquals("The response is a redirect to the admin edit question page", EditQuestionVariant.REDIRECT_ADMIN_EDIT_QUESTION
				+ "?id=" + questionId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#deleteVariantHierarchy(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * for the case where the variant belongs to a question with dependents.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteVariantHierarchy_dependents() {
		final long id = 61237846l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		getQuestionnaireService().deleteQuestionVariantHierarchy(variant);
		EasyMock.expectLastCall().andThrow(new IllegalStateException("Page has dependencies")).anyTimes();
		errors.reject(EditQuestionVariant.REJECT_DEPENDENTS_ERROR_CODE, EditQuestionVariant.REJECT_DEPENDENTS_ERROR_MESSAGE);
		final Question question = createMock("Question", Question.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpForShowVariant(model, variant, id, null, question, dependentVariant);
		replayAll();

		final String actualResponse = getBaseController().deleteVariantHierarchy(request, model, variant, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#saveVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testSaveVariant() {
		final long id = 891278l;
		final long questionId = 6512l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false).atLeastOnce();
		getQuestionnaireService().saveQuestionVariant(variant);
		EasyMock.expect(variant.getId()).andReturn(id).atLeastOnce();
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
		EasyMock.expect(question.getId()).andReturn(questionId).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().saveVariant(request, model, variant, errors);

		assertEquals("The response is a redirect to the admin edit question page", EditQuestionVariant.REDIRECT_ADMIN_EDIT_QUESTION
				+ "?id=" + questionId, actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#saveVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * for the case where there are errors.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testSaveVariant_errors() {
		final long id = 461253817l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(true).atLeastOnce();
		final Question question = createMock("Question", Question.class);
		setUpForShowVariant(model, variant, id, null, question, null);
		replayAll();

		final String actualResponse = getBaseController().saveVariant(request, model, variant, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#saveVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant, org.springframework.validation.BindingResult)}
	 * for a new variant.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testSaveVariant_newVariant() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BindingResult errors = createMock("Errors", BindingResult.class);
		EasyMock.expect(errors.hasErrors()).andReturn(false).atLeastOnce();
		getQuestionnaireService().saveQuestionVariant(variant);
		setUpForShowVariant(model, variant, 0l, null, null, null);
		replayAll();

		final String actualResponse = getBaseController().saveVariant(request, model, variant, errors);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#showVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowVariant() {
		final long id = 61237846l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Question question = createMock("Question", Question.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		setUpForShowVariant(model, variant, id, null, question, dependentVariant);
		replayAll();

		final String actualResponse = getBaseController().showVariant(request, model, variant);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#showVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant)}
	 * for a new variant.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowVariant_newVariant() {
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getId()).andReturn(0l).atLeastOnce();
		replayAll();

		final String actualResponse = getBaseController().showVariant(request, model, variant);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.admin.EditQuestionVariant#showVariant(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.questionnaire.QuestionVariant)}
	 * for a variant with no dependents.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 25, 2012
	 * @version May 31, 2012
	 */
	@Test
	public final void testShowVariant_noDependents() {
		final long id = 61237846l;
		final Authentication authentication = addAuthenticationToSecurityContext();
		final OverseasUser user = createMock("User", OverseasUser.class);
		addOverseasUserToAuthentication(authentication, user);
		EasyMock.expect(user.isInRole(UserRole.USER_ROLE_ADMIN)).andReturn(true).anyTimes();
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final ModelMap model = createModelMap(user, request, null, true, false);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Question question = createMock("Question", Question.class);
		setUpForShowVariant(model, variant, id, null, question, null);
		replayAll();

		final String actualResponse = getBaseController().showVariant(request, model, variant);

		assertEquals("The response is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualResponse);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final EditQuestionVariant createBaseController() {
		final EditQuestionVariant editQuestionVariant = new EditQuestionVariant();
		editQuestionVariant.setQuestionnaireService(getQuestionnaireService());
		editQuestionVariant.setQuestionFieldService(getQuestionFieldService());
		editQuestionVariant.setValidator(getValidator());
		return editQuestionVariant;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedContentBlock() {
		return EditQuestionVariant.DEFAULT_CONTENT_BLOCK;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedPageTitle() {
		return EditQuestionVariant.DEFAULT_PAGE_TITLE;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionCss() {
		return EditQuestionVariant.DEFAULT_SECTION_CSS;
	}

	/** {@inheritDoc} */
	@Override
	protected final String getExpectedSectionName() {
		return EditQuestionVariant.DEFAULT_SECTION_NAME;
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
		setQuestionFieldService(createMock("QuestionFieldService", QuestionFieldService.class));
		setValidator(createMock("Validator", Validator.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		setValidator(null);
		setQuestionFieldService(null);
		setQuestionnaireService(null);
	}

	/**
	 * Gets the question field service.
	 * 
	 * @author IanBrown
	 * @return the question field service.
	 * @since May 30, 2012
	 * @version May 30, 2012
	 */
	private QuestionFieldService getQuestionFieldService() {
		return questionFieldService;
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
	 * Sets the question field service.
	 * 
	 * @author IanBrown
	 * @param questionFieldService
	 *            the question field service to set.
	 * @since May 30, 2012
	 * @version May 30, 2012
	 */
	private void setQuestionFieldService(final QuestionFieldService questionFieldService) {
		this.questionFieldService = questionFieldService;
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
	 * Sets up to show the variant.
	 * 
	 * @author IanBrown
	 * @param model
	 *            the model.
	 * @param variant
	 *            the variant.
	 * @param variantId
	 *            the variant identifier.
	 * @param field the question field (if any).
	 * @param question
	 *            the question.
	 * @param dependentVariant
	 *            the dependent variant (if any).
	 * @since May 31, 2012
	 * @version May 31, 2012
	 */
	private void setUpForShowVariant(final ModelMap model, final QuestionVariant variant, final long variantId,
			QuestionField field, final Question question, final QuestionVariant dependentVariant) {
		EasyMock.expect(variant.getId()).andReturn(variantId).atLeastOnce();
		if (variantId != 0l) {
			if (field == null) {
				EasyMock.expect(variant.getFields()).andReturn(null).anyTimes();
			} else {
				final Collection<QuestionField> fields = Arrays.asList(field);
				EasyMock.expect(variant.getFields()).andReturn(fields).atLeastOnce();
			}
			EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
			if (dependentVariant == null) {
				EasyMock.expect(getQuestionnaireService().checkUsingInDependencies(question)).andReturn(false).atLeastOnce();
				addAttributeToModelMap(model, "keyQuestion", false);
			} else {
				EasyMock.expect(getQuestionnaireService().checkUsingInDependencies(question)).andReturn(true).atLeastOnce();
				addAttributeToModelMap(model, "keyQuestion", true);
                final QuestionDependency dependency = createMock( "dependency", QuestionDependency.class );
                EasyMock.expect( dependency.getDependent() ).andReturn( dependentVariant ).anyTimes();
				final Collection<QuestionDependency> dependents = Arrays.asList(dependency);
                final Collection<QuestionVariant> dependentVariants = Arrays.asList( dependentVariant );
				EasyMock.expect(getQuestionnaireService().findDependents( question )).andReturn(dependents)
						.atLeastOnce();
				addAttributeToModelMap(model, "dependentVariants", dependentVariants);
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
