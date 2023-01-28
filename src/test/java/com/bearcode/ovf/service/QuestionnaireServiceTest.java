/**
 * 
 */
package com.bearcode.ovf.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.DAO.AnswerDAO;
import com.bearcode.ovf.DAO.PdfAnswersDAO;
import com.bearcode.ovf.DAO.QuestionDAO;
import com.bearcode.ovf.DAO.QuestionFieldDAO;
import com.bearcode.ovf.DAO.QuestionnairePageDAO;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.DependencyType;
import com.bearcode.ovf.model.questionnaire.FieldDependency;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.GenericStringItem;
import com.bearcode.ovf.model.questionnaire.PageType;
import com.bearcode.ovf.model.questionnaire.PdfFilling;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionDependency;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.Related;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.tools.MigrationDealer;

/**
 * Test for {@link QuestionnaireService}.
 * 
 * @author IanBrown
 * 
 * @since May 15, 2012
 * @version May 30, 2012
 */
public final class QuestionnaireServiceTest extends EasyMockSupport {

	/**
	 * the answer data access object.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private AnswerDAO answerDAO;

	/**
	 * the PDF answers data access object.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private PdfAnswersDAO pdfAnswersDAO;

	/**
	 * the questionnaire service to test.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private QuestionnaireService questionnaireService;

	/**
	 * the questionnaire page data access object.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private QuestionnairePageDAO questionnairePageDAO;

	/**
	 * the question data access object.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private QuestionDAO questionDAO;

	/**
	 * the question field DAO.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private QuestionFieldDAO questionFieldDAO;

	/**
	 * Sets up the questionnaire service to test.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 25, 2012
	 */
	@Before
	public final void setUpQuestionnaireService() {
		setQuestionnairePageDAO(createMock("QuestionnairePageDAO", QuestionnairePageDAO.class));
		setQuestionDAO(createMock("QuestionDAO", QuestionDAO.class));
		setPdfAnswersDAO(createMock("PDFAnswersDAO", PdfAnswersDAO.class));
		setAnswerDAO(createMock("AnswerDAO", AnswerDAO.class));
		setQuestionFieldDAO(createMock("QuestionFieldDAO", QuestionFieldDAO.class));
		setQuestionnaireService(new QuestionnaireService());
		getQuestionnaireService().setPageDAO(getQuestionnairePageDAO());
		getQuestionnaireService().setQuestionDAO(getQuestionDAO());
		getQuestionnaireService().setPdfAnswersDAO(getPdfAnswersDAO());
		getQuestionnaireService().setAnswerDAO(getAnswerDAO());
		getQuestionnaireService().setQuestionFieldDAO(getQuestionFieldDAO());
	}

	/**
	 * Tears down the questionnaire service after testing.
	 * 
	 * @author IanBrown
	 * @since May 15, 2012
	 * @version May 25, 2012
	 */
	@After
	public final void tearDownQuestionnaireService() {
		setQuestionnaireService(null);
		setQuestionFieldDAO(null);
		setAnswerDAO(null);
		setPdfAnswersDAO(null);
		setQuestionDAO(null);
		setQuestionnairePageDAO(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#checkUsingInDependencies(com.bearcode.ovf.model.questionnaire.Question)}
	 * for the case where the question is not used in dependencies.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testCheckUsingInDependencies_notUsed() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		replayAll();

		final boolean actualUsingInDepdendencies = getQuestionnaireService().checkUsingInDependencies(question);

		assertFalse("The question is not used in dependencies", actualUsingInDepdendencies);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#checkUsingInDependencies(com.bearcode.ovf.model.questionnaire.Question)}
	 * for the case where the question is used in dependencies.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testCheckUsingInDependencies_used() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(true).anyTimes();
		replayAll();

		final boolean actualUsingInDepdendencies = getQuestionnaireService().checkUsingInDependencies(question);

		assertTrue("The question is used in dependencies", actualUsingInDepdendencies);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#cloneVariant(QuestionVariant)} for the case where the variant has no fields.
	 * 
	 * @author IanBrown
	 * @since May 29, 2012
	 * @version May 30, 2012
	 */
	@Test
	public final void testCloneVariant_noFields() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
		final String description = "Description";
		EasyMock.expect(variant.getDescription()).andReturn(description).atLeastOnce();
		final String title = "Title";
		EasyMock.expect(variant.getTitle()).andReturn(title).atLeastOnce();
		EasyMock.expect(variant.getFields()).andReturn(null).atLeastOnce();
		replayAll();

		final QuestionVariant actualNewVariant = getQuestionnaireService().cloneVariant(variant);
		
		assertNotNull("A new variant is returned", actualNewVariant);
		assertEquals("The variant belongs to the question", question, actualNewVariant.getQuestion());
		assertEquals("The variant description is set", description, actualNewVariant.getDescription());
		assertEquals("The variant title is set", title, actualNewVariant.getTitle());
		assertNull("The variant does not have fields", actualNewVariant.getFields());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#cloneVariant(QuestionVariant)} for the case where the variant has just a no input field.
	 * 
	 * @author IanBrown
	 * @since May 29, 2012
	 * @version May 30, 2012
	 */
	@Test
	public final void testCloneVariant_noInputField() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
		final String description = "Description";
		EasyMock.expect(variant.getDescription()).andReturn(description).atLeastOnce();
		final String title = "Title";
		EasyMock.expect(variant.getTitle()).andReturn(title).atLeastOnce();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).atLeastOnce();
		final String fieldTitle = "Field Title";
		EasyMock.expect(field.getTitle()).andReturn(fieldTitle).atLeastOnce();
		final String additionalHelp = "Additional Help";
		EasyMock.expect(field.getAdditionalHelp()).andReturn(additionalHelp).atLeastOnce();
		final String firstText = "First Text";
		EasyMock.expect(field.getFirstText()).andReturn(firstText).atLeastOnce();
		final String helpText = "Help Text";
		EasyMock.expect(field.getHelpText()).andReturn(helpText).atLeastOnce();
		final int order = 1;
		EasyMock.expect(field.getOrder()).andReturn(order).atLeastOnce();
		final String secondText = "Second Text";
		EasyMock.expect(field.getSecondText()).andReturn(secondText).atLeastOnce();
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).atLeastOnce();
		final String inPdfName = "In PDF name";
		EasyMock.expect(field.getInPdfName()).andReturn(inPdfName).atLeastOnce();
		final String verificationPattern = "Pattern";
		EasyMock.expect(field.getVerificationPattern()).andReturn(verificationPattern ).atLeastOnce();
		EasyMock.expect(field.isEncoded()).andReturn(true).atLeastOnce();
		EasyMock.expect(field.isRequired()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.isSecurity()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.getGenericOptions()).andReturn(null).atLeastOnce();
		replayAll();

		final QuestionVariant actualNewVariant = getQuestionnaireService().cloneVariant(variant);
		
		assertNotNull("A new variant is returned", actualNewVariant);
		assertEquals("The variant belongs to the question", question, actualNewVariant.getQuestion());
		assertEquals("The variant description is set", description, actualNewVariant.getDescription());
		assertEquals("The variant title is set", title, actualNewVariant.getTitle());
		final Collection<QuestionField> actualNewFields = actualNewVariant.getFields();
		assertEquals("The variant has the correct number of fields", fields.size(), actualNewFields.size());
		final QuestionField actualNewField = actualNewFields.iterator().next();
		assertEquals("The field additional help is set", field.getAdditionalHelp(), actualNewField.getAdditionalHelp());
		assertEquals("The field first text is set", field.getFirstText(), actualNewField.getFirstText());
		assertNull("The field has no generic options", actualNewField.getGenericOptions());
		assertEquals("The field help text is set", field.getHelpText(), actualNewField.getHelpText());
		assertEquals("The field in PDF name is set", field.getInPdfName(), actualNewField.getInPdfName());
		assertEquals("The field order is set", field.getOrder(), actualNewField.getOrder());
		assertEquals("The field second text is set", field.getSecondText(), actualNewField.getSecondText());
		assertEquals("The field title is set", field.getTitle(), actualNewField.getTitle());
		assertSame("The field type is set", field.getType(), actualNewField.getType());
		assertEquals("The field verification pattern is set", field.getVerificationPattern(), actualNewField.getVerificationPattern());
		assertEquals("The field encoded flag is set", field.isEncoded(), actualNewField.isEncoded());
		assertEquals("The field required flag is set", field.isRequired(), actualNewField.isRequired());
		assertEquals("The field security flag is set", field.isSecurity(), actualNewField.isSecurity());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#cloneVariant(QuestionVariant)} for the case where the variant has a simple entry field.
	 * 
	 * @author IanBrown
	 * @since May 29, 2012
	 * @version May 30, 2012
	 */
	@Test
	public final void testCloneVariant_simpleInputField() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
		final String description = "Description";
		EasyMock.expect(variant.getDescription()).andReturn(description).atLeastOnce();
		final String title = "Title";
		EasyMock.expect(variant.getTitle()).andReturn(title).atLeastOnce();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).atLeastOnce();
		final String fieldTitle = "Field Title";
		EasyMock.expect(field.getTitle()).andReturn(fieldTitle).atLeastOnce();
		final String additionalHelp = "Additional Help";
		EasyMock.expect(field.getAdditionalHelp()).andReturn(additionalHelp).atLeastOnce();
		final String firstText = "First Text";
		EasyMock.expect(field.getFirstText()).andReturn(firstText).atLeastOnce();
		final String helpText = "Help Text";
		EasyMock.expect(field.getHelpText()).andReturn(helpText).atLeastOnce();
		final int order = 1;
		EasyMock.expect(field.getOrder()).andReturn(order).atLeastOnce();
		final String secondText = "Second Text";
		EasyMock.expect(field.getSecondText()).andReturn(secondText).atLeastOnce();
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).atLeastOnce();
		final String inPdfName = "In PDF name";
		EasyMock.expect(field.getInPdfName()).andReturn(inPdfName).atLeastOnce();
		final String verificationPattern = "Pattern";
		EasyMock.expect(field.getVerificationPattern()).andReturn(verificationPattern ).atLeastOnce();
		EasyMock.expect(field.isEncoded()).andReturn(true).atLeastOnce();
		EasyMock.expect(field.isRequired()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.isSecurity()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.getGenericOptions()).andReturn(null).atLeastOnce();
		replayAll();

		final QuestionVariant actualNewVariant = getQuestionnaireService().cloneVariant(variant);
		
		assertNotNull("A new variant is returned", actualNewVariant);
		assertEquals("The variant belongs to the question", question, actualNewVariant.getQuestion());
		assertEquals("The variant description is set", description, actualNewVariant.getDescription());
		assertEquals("The variant title is set", title, actualNewVariant.getTitle());
		final Collection<QuestionField> actualNewFields = actualNewVariant.getFields();
		assertEquals("The variant has the correct number of fields", fields.size(), actualNewFields.size());
		final QuestionField actualNewField = actualNewFields.iterator().next();
		assertEquals("The field additional help is set", field.getAdditionalHelp(), actualNewField.getAdditionalHelp());
		assertEquals("The field first text is set", field.getFirstText(), actualNewField.getFirstText());
		assertNull("The field has no generic options", actualNewField.getGenericOptions());
		assertEquals("The field help text is set", field.getHelpText(), actualNewField.getHelpText());
		assertEquals("The field in PDF name is set", field.getInPdfName(), actualNewField.getInPdfName());
		assertEquals("The field order is set", field.getOrder(), actualNewField.getOrder());
		assertEquals("The field second text is set", field.getSecondText(), actualNewField.getSecondText());
		assertEquals("The field title is set", field.getTitle(), actualNewField.getTitle());
		assertSame("The field type is set", field.getType(), actualNewField.getType());
		assertEquals("The field verification pattern is set", field.getVerificationPattern(), actualNewField.getVerificationPattern());
		assertEquals("The field encoded flag is set", field.isEncoded(), actualNewField.isEncoded());
		assertEquals("The field required flag is set", field.isRequired(), actualNewField.isRequired());
		assertEquals("The field security flag is set", field.isSecurity(), actualNewField.isSecurity());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#cloneVariant(QuestionVariant)} for the case where the variant has a checkbox field.
	 * 
	 * @author IanBrown
	 * @since May 29, 2012
	 * @version May 30, 2012
	 */
	@Test
	public final void testCloneVariant_checkboxField() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
		final String description = "Description";
		EasyMock.expect(variant.getDescription()).andReturn(description).atLeastOnce();
		final String title = "Title";
		EasyMock.expect(variant.getTitle()).andReturn(title).atLeastOnce();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).atLeastOnce();
		final String fieldTitle = "Field Title";
		EasyMock.expect(field.getTitle()).andReturn(fieldTitle).atLeastOnce();
		final String additionalHelp = "Additional Help";
		EasyMock.expect(field.getAdditionalHelp()).andReturn(additionalHelp).atLeastOnce();
		final String firstText = "First Text";
		EasyMock.expect(field.getFirstText()).andReturn(firstText).atLeastOnce();
		final String helpText = "Help Text";
		EasyMock.expect(field.getHelpText()).andReturn(helpText).atLeastOnce();
		final int order = 1;
		EasyMock.expect(field.getOrder()).andReturn(order).atLeastOnce();
		final String secondText = "Second Text";
		EasyMock.expect(field.getSecondText()).andReturn(secondText).atLeastOnce();
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).atLeastOnce();
		final String inPdfName = "In PDF name";
		EasyMock.expect(field.getInPdfName()).andReturn(inPdfName).atLeastOnce();
		final String verificationPattern = "Pattern";
		EasyMock.expect(field.getVerificationPattern()).andReturn(verificationPattern ).atLeastOnce();
		EasyMock.expect(field.isEncoded()).andReturn(true).atLeastOnce();
		EasyMock.expect(field.isRequired()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.isSecurity()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.getGenericOptions()).andReturn(null).atLeastOnce();
		replayAll();

		final QuestionVariant actualNewVariant = getQuestionnaireService().cloneVariant(variant);
		
		assertNotNull("A new variant is returned", actualNewVariant);
		assertEquals("The variant belongs to the question", question, actualNewVariant.getQuestion());
		assertEquals("The variant description is set", description, actualNewVariant.getDescription());
		assertEquals("The variant title is set", title, actualNewVariant.getTitle());
		final Collection<QuestionField> actualNewFields = actualNewVariant.getFields();
		assertEquals("The variant has the correct number of fields", fields.size(), actualNewFields.size());
		final QuestionField actualNewField = actualNewFields.iterator().next();
		assertEquals("The field additional help is set", field.getAdditionalHelp(), actualNewField.getAdditionalHelp());
		assertEquals("The field first text is set", field.getFirstText(), actualNewField.getFirstText());
		assertNull("The field has no generic options", actualNewField.getGenericOptions());
		assertEquals("The field help text is set", field.getHelpText(), actualNewField.getHelpText());
		assertEquals("The field in PDF name is set", field.getInPdfName(), actualNewField.getInPdfName());
		assertEquals("The field order is set", field.getOrder(), actualNewField.getOrder());
		assertEquals("The field second text is set", field.getSecondText(), actualNewField.getSecondText());
		assertEquals("The field title is set", field.getTitle(), actualNewField.getTitle());
		assertSame("The field type is set", field.getType(), actualNewField.getType());
		assertEquals("The field verification pattern is set", field.getVerificationPattern(), actualNewField.getVerificationPattern());
		assertEquals("The field encoded flag is set", field.isEncoded(), actualNewField.isEncoded());
		assertEquals("The field required flag is set", field.isRequired(), actualNewField.isRequired());
		assertEquals("The field security flag is set", field.isSecurity(), actualNewField.isSecurity());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#cloneVariant(QuestionVariant)} for the case where the variant has a select field.
	 * 
	 * @author IanBrown
	 * @since May 29, 2012
	 * @version May 30, 2012
	 */
	@Test
	public final void testCloneVariant_selectField() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).atLeastOnce();
		final String description = "Description";
		EasyMock.expect(variant.getDescription()).andReturn(description).atLeastOnce();
		final String title = "Title";
		EasyMock.expect(variant.getTitle()).andReturn(title).atLeastOnce();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).atLeastOnce();
		final String fieldTitle = "Field Title";
		EasyMock.expect(field.getTitle()).andReturn(fieldTitle).atLeastOnce();
		final String additionalHelp = "Additional Help";
		EasyMock.expect(field.getAdditionalHelp()).andReturn(additionalHelp).atLeastOnce();
		final String firstText = "First Text";
		EasyMock.expect(field.getFirstText()).andReturn(firstText).atLeastOnce();
		final String helpText = "Help Text";
		EasyMock.expect(field.getHelpText()).andReturn(helpText).atLeastOnce();
		final int order = 1;
		EasyMock.expect(field.getOrder()).andReturn(order).atLeastOnce();
		final String secondText = "Second Text";
		EasyMock.expect(field.getSecondText()).andReturn(secondText).atLeastOnce();
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(fieldType).atLeastOnce();
		final String inPdfName = "In PDF name";
		EasyMock.expect(field.getInPdfName()).andReturn(inPdfName).atLeastOnce();
		final String verificationPattern = "Pattern";
		EasyMock.expect(field.getVerificationPattern()).andReturn(verificationPattern ).atLeastOnce();
		EasyMock.expect(field.isEncoded()).andReturn(true).atLeastOnce();
		EasyMock.expect(field.isRequired()).andReturn(false).atLeastOnce();
		EasyMock.expect(field.isSecurity()).andReturn(false).atLeastOnce();
		final GenericStringItem genericOption = createMock("GenericOption", GenericStringItem.class);
		final Collection<FieldDictionaryItem> genericOptions = Arrays.asList((FieldDictionaryItem) genericOption);
		EasyMock.expect(field.getGenericOptions()).andReturn(genericOptions).atLeastOnce();
		final String genericValue = "Value";
		EasyMock.expect(genericOption.getValue()).andReturn(genericValue).atLeastOnce();
		replayAll();

		final QuestionVariant actualNewVariant = getQuestionnaireService().cloneVariant(variant);
		
		assertNotNull("A new variant is returned", actualNewVariant);
		assertEquals("The variant description is set", description, actualNewVariant.getDescription());
		assertEquals("The variant title is set", title, actualNewVariant.getTitle());
		final Collection<QuestionField> actualNewFields = actualNewVariant.getFields();
		assertEquals("The variant has the correct number of fields", fields.size(), actualNewFields.size());
		final QuestionField actualNewField = actualNewFields.iterator().next();
		assertEquals("The field additional help is set", field.getAdditionalHelp(), actualNewField.getAdditionalHelp());
		assertEquals("The field first text is set", field.getFirstText(), actualNewField.getFirstText());
		final Collection<FieldDictionaryItem> actualNewGenericOptions = actualNewField.getGenericOptions();
		assertEquals("The field has the correct number of generic options", genericOptions.size(), actualNewGenericOptions.size());
		final FieldDictionaryItem actualNewGenericOption = actualNewGenericOptions.iterator().next();
		assertEquals("The generic option has the correct value", genericOption.getValue(), actualNewGenericOption.getValue());
		assertEquals("The field help text is set", field.getHelpText(), actualNewField.getHelpText());
		assertEquals("The field in PDF name is set", field.getInPdfName(), actualNewField.getInPdfName());
		assertEquals("The field order is set", field.getOrder(), actualNewField.getOrder());
		assertEquals("The field second text is set", field.getSecondText(), actualNewField.getSecondText());
		assertEquals("The field title is set", field.getTitle(), actualNewField.getTitle());
		assertSame("The field type is set", field.getType(), actualNewField.getType());
		assertEquals("The field verification pattern is set", field.getVerificationPattern(), actualNewField.getVerificationPattern());
		assertEquals("The field encoded flag is set", field.isEncoded(), actualNewField.isEncoded());
		assertEquals("The field required flag is set", field.isRequired(), actualNewField.isRequired());
		assertEquals("The field security flag is set", field.isSecurity(), actualNewField.isSecurity());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#countPages(com.bearcode.ovf.model.questionnaire.PageType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testCountPages() {
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final int pages = 25;
		EasyMock.expect(getQuestionnairePageDAO().countPages(type)).andReturn(pages).anyTimes();
		replayAll();

		final int actualPages = getQuestionnaireService().countPages(type);

		assertEquals("The expected number of pages are returned", pages, actualPages);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#deleteDependencies(java.util.Collection)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testDeleteDependencies() {
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		getQuestionDAO().makeAllTransient(dependencies);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().deleteDependencies(dependencies);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteDependency(com.bearcode.ovf.model.questionnaire.BasicDependency)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testDeleteDependency() {
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		getQuestionDAO().makeTransient(dependency);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().deleteDependency(dependency);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deletePage(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testDeletePage() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		setUpPageForDelete(page);
		replayAll();

		getQuestionnaireService().deletePage(page);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deletePageHierarchy(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the simple case where the page has no questions.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeletePageHierarchy_noQuestions() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(page.getQuestions()).andReturn(null).anyTimes();
		setUpPageForDelete(page);
		replayAll();

		getQuestionnaireService().deletePageHierarchy(page);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deletePageHierarchy(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the simple case where the page has a question with no variants.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeletePageHierarchy_noVariants() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(page.getQuestions()).andReturn(questions).anyTimes();
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		EasyMock.expect(question.getVariants()).andReturn(null).anyTimes();
		setUpQuestionForDelete(question);
		setUpPageForDelete(page);
		replayAll();

		getQuestionnaireService().deletePageHierarchy(page);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deletePageHierarchy(com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the simple case where the page has a question with dependents.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 25, 2012
	 */
	@Test(expected = IllegalStateException.class)
	public final void testDeletePageHierarchy_questionWithDependents() {
		final long pageId = 98123l;
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(page.getId()).andReturn(pageId).anyTimes();
		EasyMock.expect(getQuestionnairePageDAO().findById(pageId)).andReturn(page).anyTimes();
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(page.getQuestions()).andReturn(questions).anyTimes();
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(true).anyTimes();
		EasyMock.expect(page.getTitle()).andReturn("Page Title").anyTimes();
		replayAll();

		getQuestionnaireService().deletePageHierarchy(page);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestion(com.bearcode.ovf.model.questionnaire.Question)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testDeleteQuestion() {
		final Question question = createMock("Question", Question.class);
		setUpQuestionForDelete(question);
		replayAll();

		getQuestionnaireService().deleteQuestion(question);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionHierarchy(com.bearcode_ovf.model.questionnaire.Question)}
	 * for the case where the question that has dependents.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test(expected = IllegalStateException.class)
	public final void testDeleteQuestionHierarchy_dependents() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(true).anyTimes();
		EasyMock.expect(question.getTitle()).andReturn("Question Title").anyTimes();
		replayAll();

		getQuestionnaireService().deleteQuestionHierarchy(question);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionHierarchy(com.bearcode_ovf.model.questionnaire.Question)}
	 * for the case where the question has a variant with no fields or keys.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeleteQuestionHierarchy_noFieldsOrKeys() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getFields()).andReturn(null).anyTimes();
		final Collection<BasicDependency> keys = new ArrayList<BasicDependency>();
		setUpVariantForDelete(variant, keys);
		setUpQuestionForDelete(question);
		replayAll();

		getQuestionnaireService().deleteQuestionHierarchy(question);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionHierarchy(com.bearcode_ovf.model.questionnaire.Question)}
	 * for the case where the question has no variants.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeleteQuestionHierarchy_noVariants() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		EasyMock.expect(question.getVariants()).andReturn(null).anyTimes();
		setUpQuestionForDelete(question);
		replayAll();

		getQuestionnaireService().deleteQuestionHierarchy(question);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionVariant(com.bearcode.ovf.model.questionnaire.QuestionVariant)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testDeleteQuestionVariant() {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final BasicDependency key = createMock("Key", BasicDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList(key);
		setUpVariantForDelete(variant, keys);
		replayAll();

		getQuestionnaireService().deleteQuestionVariant(variant);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionVariant(com.bearcode.ovf.model.questionnaire.QuestionVariant)}
	 * for the case where the variant has no keys .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeleteQuestionVariant_noKeys() {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<BasicDependency> keys = new ArrayList<BasicDependency>();
		setUpVariantForDelete(variant, keys);
		replayAll();

		getQuestionnaireService().deleteQuestionVariant(variant);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionVariantHierarchy(QuestionVariant)} for the
	 * case where the variant belongs to a question with variants.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test(expected = IllegalStateException.class)
	public final void testDeleteQuestionVariantHierarchy_dependents() {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(true).anyTimes();
		EasyMock.expect(variant.getTitle()).andReturn("Variant Title").anyTimes();
		replayAll();

		getQuestionnaireService().deleteQuestionVariantHierarchy(variant);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionVariantHierarchy(QuestionVariant)} for the
	 * case where the variant has a field.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testDeleteQuestionVariantHierarchy_fields() {
		final int fieldOrder = 3;
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(field);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		EasyMock.expect(field.getOrder()).andReturn(fieldOrder).anyTimes();
		final QuestionField fieldBefore = createMock("FieldBefore", QuestionField.class);
		final Collection<QuestionField> fieldsBefore = Arrays.asList(fieldBefore);
		EasyMock.expect(getQuestionFieldDAO().findFieldsBeforeNumber(field, fieldOrder + 1)).andReturn(fieldsBefore).anyTimes();
		fieldBefore.setOrder(fieldOrder);
		EasyMock.expectLastCall().anyTimes();
		getQuestionFieldDAO().makeAllPersistent(fieldsBefore);
		EasyMock.expectLastCall().anyTimes();
		final FieldDictionaryItem genericOption = createMock("GenericOption", FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> genericOptions = Arrays.asList(genericOption);
		EasyMock.expect(field.getGenericOptions()).andReturn(genericOptions).anyTimes();
		getQuestionFieldDAO().makeAllTransient(genericOptions);
		EasyMock.expectLastCall().anyTimes();
		getQuestionFieldDAO().makeTransient(field);
		EasyMock.expectLastCall().anyTimes();
		final BasicDependency key = createMock("Key", BasicDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList(key);
		setUpVariantForDelete(variant, keys);
		replayAll();

		getQuestionnaireService().deleteQuestionVariantHierarchy(variant);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionVariantHierarchy(QuestionVariant)} for the
	 * case where the variant has no fields.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeleteQuestionVariantHierarchy_noFields() {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		EasyMock.expect(variant.getFields()).andReturn(null).anyTimes();
		final BasicDependency key = createMock("Key", BasicDependency.class);
		final Collection<BasicDependency> keys = Arrays.asList(key);
		setUpVariantForDelete(variant, keys);
		replayAll();

		getQuestionnaireService().deleteQuestionVariantHierarchy(variant);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#deleteQuestionVariantHierarchy(QuestionVariant)} for the
	 * case where the variant has no fields or keys.
	 * 
	 * @author IanBrown
	 * @since May 24, 2012
	 * @version May 24, 2012
	 */
	@Test
	public final void testDeleteQuestionVariantHierarchy_noFieldsOrKeys() {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
		EasyMock.expect(getQuestionDAO().checkQuestionUsing(question)).andReturn(false).anyTimes();
		EasyMock.expect(variant.getFields()).andReturn(null).anyTimes();
		final Collection<BasicDependency> keys = new ArrayList<BasicDependency>();
		setUpVariantForDelete(variant, keys);
		replayAll();

		getQuestionnaireService().deleteQuestionVariantHierarchy(variant);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findByFieldSelectedValue(com.bearcode.ovf.model.questionnaire.QuestionField, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindByFieldSelectedValueQuestionFieldInt() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final int value = 23;
		final WizardResults wizardResult = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> wizardResults = Arrays.asList(wizardResult);
		EasyMock.expect(getPdfAnswersDAO().findByFieldSelectedValue(field, value, null, null)).andReturn(wizardResults).anyTimes();
		replayAll();

		final Collection<WizardResults> actualWizardResults = getQuestionnaireService().findByFieldSelectedValue(field, value);

		assertSame("The wizard results are returned", wizardResults, actualWizardResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findByFieldSelectedValue(com.bearcode.ovf.model.questionnaire.QuestionField, int, java.util.Date, java.util.Date)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindByFieldSelectedValueQuestionFieldIntDateDate() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final int value = 23;
		final Date after = new Date();
		final Date before = new Date();
		final WizardResults wizardResult = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> wizardResults = Arrays.asList(wizardResult);
		EasyMock.expect(getPdfAnswersDAO().findByFieldSelectedValue(field, value, after, before)).andReturn(wizardResults)
				.anyTimes();
		replayAll();

		final Collection<WizardResults> actualWizardResults = getQuestionnaireService().findByFieldSelectedValue(field, value,
				after, before);

		assertSame("The wizard results are returned", wizardResults, actualWizardResults);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findByFieldValue(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindByFieldValueString() {
		final String value = "Value";
		final WizardResults wizardResult = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> wizardResults = Arrays.asList(wizardResult);
		EasyMock.expect(getPdfAnswersDAO().findByFieldValue(value, null, null)).andReturn(wizardResults).anyTimes();
		replayAll();

		final Collection<WizardResults> actualWizardResults = getQuestionnaireService().findByFieldValue(value);

		assertSame("The wizard results are returned", wizardResults, actualWizardResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findByFieldValue(java.lang.String, java.util.Date, java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindByFieldValueStringDateDate() {
		final String value = "Value";
		final Date after = new Date();
		final Date before = new Date();
		final WizardResults wizardResult = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> wizardResults = Arrays.asList(wizardResult);
		EasyMock.expect(getPdfAnswersDAO().findByFieldValue(value, after, before)).andReturn(wizardResults).anyTimes();
		replayAll();

		final Collection<WizardResults> actualWizardResults = getQuestionnaireService().findByFieldValue(value, after, before);

		assertSame("The wizard results are returned", wizardResults, actualWizardResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findCrossPageConnection(com.bearcode.ovf.model.questionnaire.PageType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindCrossPageConnection() {
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		final Map<Integer, Collection<Integer>> connections = new HashMap<Integer, Collection<Integer>>();
		final int left = 891;
		final Collection<Integer> right = Arrays.asList(123);
		connections.put(left, right);
		EasyMock.expect(getQuestionnairePageDAO().defineDependencies(type)).andReturn(connections).anyTimes();
		replayAll();

		final Map<Integer, Collection<Integer>> actualConnections = getQuestionnaireService().findCrossPageConnection(type);

		assertSame("The connections are returned", connections, actualConnections);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findDependenciesOfType(com.bearcode.ovf.model.questionnaire.DependencyType, com.bearcode.ovf.model.questionnaire.Related, com.bearcode.ovf.model.questionnaire.Question, java.lang.String)}
	 * for a face dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindDependenciesOfType_face() {
		final DependencyType type = DependencyType.FACE;
		final Related dependent = createMock("Dependent", Related.class);
		final Question dependsOn = null;
		final String fieldName = null;
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		EasyMock.expect(getQuestionDAO().findFaceDependencies(dependent)).andReturn(dependencies).anyTimes();
		replayAll();

		final Collection<BasicDependency> actualDependencies = getQuestionnaireService().findDependenciesOfType(type, dependent,
				dependsOn, fieldName);

		assertSame("The dependencies are returned", dependencies, actualDependencies);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findDependenciesOfType(com.bearcode.ovf.model.questionnaire.DependencyType, com.bearcode.ovf.model.questionnaire.Related, com.bearcode.ovf.model.questionnaire.Question, java.lang.String)}
	 * for a flow dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindDependenciesOfType_flow() {
		final DependencyType type = DependencyType.FLOW;
		final Related dependent = createMock("Dependent", Related.class);
		final Question dependsOn = null;
		final String fieldName = null;
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		EasyMock.expect(getQuestionDAO().findFlowDependencies(dependent)).andReturn(dependencies).anyTimes();
		replayAll();

		final Collection<BasicDependency> actualDependencies = getQuestionnaireService().findDependenciesOfType(type, dependent,
				dependsOn, fieldName);

		assertSame("The dependencies are returned", dependencies, actualDependencies);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findDependenciesOfType(com.bearcode.ovf.model.questionnaire.DependencyType, com.bearcode.ovf.model.questionnaire.Related, com.bearcode.ovf.model.questionnaire.Question, java.lang.String)}
	 * for a question dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindDependenciesOfType_question() {
		final DependencyType type = DependencyType.QUESTION;
		final Related dependent = createMock("Dependent", Related.class);
		final Question dependsOn = createMock("DependsOn", Question.class);
		final String fieldName = null;
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		EasyMock.expect(getQuestionDAO().findQuestionDependencies(dependent, dependsOn)).andReturn(dependencies).anyTimes();
		replayAll();

		final Collection<BasicDependency> actualDependencies = getQuestionnaireService().findDependenciesOfType(type, dependent,
				dependsOn, fieldName);

		assertSame("The dependencies are returned", dependencies, actualDependencies);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findDependenciesOfType(com.bearcode.ovf.model.questionnaire.DependencyType, com.bearcode.ovf.model.questionnaire.Related, com.bearcode.ovf.model.questionnaire.Question, java.lang.String)}
	 * for an unknown dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	@Test
	public final void testFindDependenciesOfType_unknown() {
		final DependencyType type = null;
		final Related dependent = createMock("Dependent", Related.class);
		final Question dependsOn = null;
		final String fieldName = null;
		replayAll();

		final Collection<BasicDependency> actualDependencies = getQuestionnaireService().findDependenciesOfType(type, dependent,
				dependsOn, fieldName);

		assertTrue("An empty collection is returned", actualDependencies.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findDependenciesOfType(com.bearcode.ovf.model.questionnaire.DependencyType, com.bearcode.ovf.model.questionnaire.Related, com.bearcode.ovf.model.questionnaire.Question, java.lang.String)}
	 * for a user dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindDependenciesOfType_user() {
		final DependencyType type = DependencyType.USER;
		final Related dependent = createMock("Dependent", Related.class);
		final Question dependsOn = null;
		final String fieldName = "Field Name";
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		EasyMock.expect(getQuestionDAO().findUserFieldDependencies(dependent, fieldName)).andReturn(dependencies).anyTimes();
		replayAll();

		final Collection<BasicDependency> actualDependencies = getQuestionnaireService().findDependenciesOfType(type, dependent,
				dependsOn, fieldName);

		assertSame("The dependencies are returned", dependencies, actualDependencies);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findDependents(com.bearcode.ovf.model.questionnaire.Question)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindDependents() {
		final Question question = createMock("Question", Question.class);
		final QuestionDependency dependent = createMock("Dependent", QuestionDependency.class);
		final Collection<QuestionDependency> dependents = Arrays.asList(dependent);
		EasyMock.expect(getQuestionDAO().findDependents(question)).andReturn(dependents).anyTimes();
		replayAll();

		final Collection<QuestionDependency> actualDependents = getQuestionnaireService().findDependents(question);

		assertSame("The dependents are returned", dependents, actualDependents);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findDependentVariants(Question)}.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testFindDependentVariantsQuestion() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		final Collection<QuestionVariant> dependentVariants = Arrays.asList(dependentVariant);
		EasyMock.expect(getQuestionDAO().findDependentVariants(question)).andReturn(dependentVariants).anyTimes();
		replayAll();

		final Collection<QuestionVariant> actualDependentVariants = getQuestionnaireService().findDependentVariants(question);

		assertEquals("There are dependent variants", dependentVariants, actualDependentVariants);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findDependentVariants(Question)} for the case where
	 * there are no dependent variants.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testFindDependentVariantsQuestion_noDependents() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().findDependentVariants(question)).andReturn(null).anyTimes();
		replayAll();

		final Collection<QuestionVariant> actualDependentVariants = getQuestionnaireService().findDependentVariants(question);

		assertTrue("There are no dependent variants", actualDependentVariants.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findDependentVariants(QuestionnairePage)}.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testFindDependentVariantsQuestionnairePage() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(page.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		final Collection<QuestionVariant> dependentVariants = Arrays.asList(dependentVariant);
		EasyMock.expect(getQuestionDAO().findDependentVariants(question)).andReturn(dependentVariants).anyTimes();
		replayAll();

		final Collection<QuestionVariant> actualDependentVariants = getQuestionnaireService().findDependentVariants(page);

		assertEquals("The dependent variants for the question are returned", dependentVariants, actualDependentVariants);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findDependentVariants(QuestionnairePage)} for the case
	 * where the page has no dependents.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testFindDependentVariantsQuestionnairePage_noDependents() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(page.getQuestions()).andReturn(questions).anyTimes();
		EasyMock.expect(getQuestionDAO().findDependentVariants(question)).andReturn(null).anyTimes();
		replayAll();

		final Collection<QuestionVariant> actualDependentVariants = getQuestionnaireService().findDependentVariants(page);

		assertTrue("There are no dependent variants", actualDependentVariants.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findDependentVariants(QuestionnairePage)} for the case
	 * where the page has no questions.
	 * 
	 * @author IanBrown
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	@Test
	public final void testFindDependentVariantsQuestionnairePage_noQuestions() {
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(page.getQuestions()).andReturn(null).anyTimes();
		replayAll();

		final Collection<QuestionVariant> actualDependentVariants = getQuestionnaireService().findDependentVariants(page);

		assertTrue("There are no dependent variants", actualDependentVariants.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findPageById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindPageById() {
		final long id = 2198l;
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(getQuestionnairePageDAO().findById(id)).andReturn(page).anyTimes();
		replayAll();

		final QuestionnairePage actualPage = getQuestionnaireService().findPageById(id);

		assertSame("The page is returned", page, actualPage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findPageByNumber(int, com.bearcode.ovf.model.questionnaire.PageType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindPageByNumber() {
		final int pageNumber = 98;
		final PageType type = PageType.DOMESTIC_REGISTRATION;
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(getQuestionnairePageDAO().findPageByNumber(pageNumber, type)).andReturn(page).anyTimes();
		replayAll();

		final QuestionnairePage actualPage = getQuestionnaireService().findPageByNumber(pageNumber, type);

		assertSame("The page is returned", page, actualPage);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionById() {
		final long id = 8712387l;
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getQuestionDAO().findQuestionById(id)).andReturn(question).anyTimes();
		replayAll();

		final Question actualQuestion = getQuestionnaireService().findQuestionById(id);

		assertSame("The question is returned", question, actualQuestion);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionDependencyById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionDependencyById() {
		final long dependencyId = 63702l;
		final BasicDependency questionDependency = createMock("QuestionDependency", BasicDependency.class);
		EasyMock.expect(getQuestionDAO().findQuestionDependencyById(dependencyId)).andReturn(questionDependency).anyTimes();
		replayAll();

		final BasicDependency actualQuestionDependency = getQuestionnaireService().findQuestionDependencyById(dependencyId);

		assertSame("The question dependency is returned", questionDependency, actualQuestionDependency);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionForDependency()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionForDependency() {
		final Question question = createMock("Question", Question.class);
		final Collection<Question> questions = Arrays.asList(question);
		EasyMock.expect(getQuestionDAO().findQuestionForDependency()).andReturn(questions).anyTimes();
		replayAll();

		final Collection<Question> actualQuestions = getQuestionnaireService().findQuestionForDependency();

		assertSame("The questions are returned", questions, actualQuestions);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionForDependency(com.bearcode.ovf.model.questionnaire.Question)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionForDependencyQuestion() {
		final Question question = createMock("Question", Question.class);
		final Question forDependency = createMock("ForDependency", Question.class);
		final Collection<Question> questions = Arrays.asList(forDependency);
		EasyMock.expect(getQuestionnaireService().findQuestionForDependency(question)).andReturn(questions).anyTimes();
		replayAll();

		final Collection<Question> actualQuestions = getQuestionnaireService().findQuestionForDependency(question);

		assertSame("The questions are returned", questions, actualQuestions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionnairePages()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionnairePages() {
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		EasyMock.expect(getQuestionnairePageDAO().findPages()).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<QuestionnairePage> actualQuestionnairePages = getQuestionnaireService().findQuestionnairePages();

		assertSame("The questionnaire pages are returned", questionnairePages, actualQuestionnairePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionnairePages(com.bearcode.ovf.model.questionnaire.PageType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionnairePagesPageType() {
		final PageType type = PageType.OVERSEAS;
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		EasyMock.expect(getQuestionnairePageDAO().findPages(type)).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<QuestionnairePage> actualQuestionnairePages = getQuestionnaireService().findQuestionnairePages(type);

		assertSame("The questionnaire pages are returned", questionnairePages, actualQuestionnairePages);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#findQuestionVariantById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindQuestionVariantById() {
		final long id = 675267l;
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(getQuestionDAO().findQuestionVariantById(id)).andReturn(variant).anyTimes();
		replayAll();

		final QuestionVariant actualVariant = getQuestionnaireService().findQuestionVariantById(id);

		assertSame("The variant is returned", variant, actualVariant);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#findUserPdfs(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testFindUserPdfs() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final WizardResults wizardResult = createMock("WizardResult", WizardResults.class);
		final Collection<WizardResults> wizardResults = Arrays.asList(wizardResult);
		EasyMock.expect(getPdfAnswersDAO().getUserPdfs(user)).andReturn(wizardResults).anyTimes();
		replayAll();

		final Collection<WizardResults> actualWizardResults = getQuestionnaireService().findUserPdfs(user);

		assertSame("The wizard results are returned", wizardResults, actualWizardResults);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#makeUserAnswersAnonymity(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testMakeUserAnswersAnonymity() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final WizardResults wizardResult = createMock("WizardResult", WizardResults.class);
		final Collection<WizardResults> wizardResults = Arrays.asList(wizardResult);
		EasyMock.expect(getPdfAnswersDAO().getUserPdfs(user)).andReturn(wizardResults).anyTimes();
		final OverseasUser userToEvict = createMock("UserToEvict", OverseasUser.class);
		EasyMock.expect(wizardResult.getUser()).andReturn(userToEvict).anyTimes();
		wizardResult.setUser(null);
		EasyMock.expectLastCall().anyTimes();
		getPdfAnswersDAO().evict(userToEvict);
		EasyMock.expectLastCall().anyTimes();
		final Answer anonymous = createMock("Anonymous", Answer.class);
		final Collection<Answer> anonymized = Arrays.asList(anonymous);
		EasyMock.expect(wizardResult.anonymize()).andReturn(anonymized).anyTimes();
		getPdfAnswersDAO().makeAllPersistent(wizardResults);
		EasyMock.expectLastCall().anyTimes();
		getAnswerDAO().makeAllPersistent(anonymized);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().makeUserAnswersAnonymity(user);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveDependency(com.bearcode.ovf.model.questionnaire.BasicDependency)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveDependency() {
		final BasicDependency dependency = createMock("Dependency", BasicDependency.class);
		getQuestionDAO().makePersistent(dependency);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveDependency(dependency);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.QuestionnaireService#saveMigration(com.bearcode.ovf.tools.MigrationDealer)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveMigration() {
		final MigrationDealer dealer = createMock("Dealer", MigrationDealer.class);
		final Collection<FieldDictionaryItem> itemsToDelete = createItemsToDelete("ItemsToDelete");
		EasyMock.expect(dealer.getItemsToDelete()).andReturn(itemsToDelete).anyTimes();
		final Collection<QuestionField> fieldsToDelete = createFieldsToDelete("FieldsToDelete");
		EasyMock.expect(dealer.getFieldsToDelete()).andReturn(fieldsToDelete).anyTimes();
		final Collection<BasicDependency> dependenciesToDelete = createDependenciesToDelete("DependenciesToDelete");
		EasyMock.expect(dealer.getDependenciesToDelete()).andReturn(dependenciesToDelete).anyTimes();
		final Collection<QuestionVariant> variantsToDelete = createVariantsToDelete("VariantsToDelete");
		EasyMock.expect(dealer.getVariantsToDelete()).andReturn(variantsToDelete).anyTimes();
		final Collection<Question> groupsToDelete = createGroupsToDelete("GroupsToDelete");
		EasyMock.expect(dealer.getGroupsToDelete()).andReturn(groupsToDelete).anyTimes();
		final Collection<QuestionnairePage> pagesToDelete = createPagesToDelete("PagesToDelete");
		EasyMock.expect(dealer.getPagesToDelete()).andReturn(pagesToDelete).anyTimes();
		final Collection<QuestionnairePage> pagesToPersist = createPagesToPersist("PagesToPersist");
		EasyMock.expect(dealer.getPagesToPersist()).andReturn(pagesToPersist).anyTimes();
		final Collection<PdfFilling> fillingsToDelete = createFillingsToDelete("FillingsToDelete");
		EasyMock.expect(dealer.getFillingsToDelete()).andReturn(fillingsToDelete).anyTimes();
		final Collection<PdfFilling> fillingsToPersist = createFillingsToPersist("FillingsToPersist");
		EasyMock.expect(dealer.getFillingsToPersist()).andReturn(fillingsToPersist).anyTimes();
		final Collection<FieldDependency> fieldDependenciesToDelete = createFieldDependenciesToDelete("FieldDependenciesToDelete");
		EasyMock.expect(dealer.getFieldDependenciesToDelete()).andReturn(fieldDependenciesToDelete).anyTimes();
		final Collection<FieldDependency> fieldDependenciesToPersist = createFieldDependenciesToPersist("FieldDependenciesToPersist");
		EasyMock.expect(dealer.getFieldDependenciesToPersist()).andReturn(fieldDependenciesToPersist).anyTimes();
        final Collection<FieldType> fieldTypes = createFieldTypesToPersist("FieldTypesToPersist");
        EasyMock.expect(dealer.getFieldTypes()).andReturn(fieldTypes).anyTimes();
		replayAll();

		getQuestionnaireService().saveMigration(dealer);

		verifyAll();
	}

    private Collection<FieldType> createFieldTypesToPersist( final String name ) {
        final String fieldTypeName = name + "FieldType";
        final FieldType fieldType = createMock(fieldTypeName, FieldType.class);
        final Collection<FieldType> fieldTypes = Arrays.asList(fieldType);
        getQuestionFieldDAO().makeAllPersistent(fieldTypes);
        EasyMock.expectLastCall().anyTimes();
        return fieldTypes;

    }

    /**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#savePage(com.bearcode.ovf.model.questionnaire.QuestionnairePage)} for
	 * the case where the page number is decreased.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	@Test
	public final void testSavePage_numberDecreased() {
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final long id = 1238l;
		EasyMock.expect(questionnairePage.getId()).andReturn(id).anyTimes();
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		EasyMock.expect(questionnairePage.getType()).andReturn(type).anyTimes();
		final int oldNumber = 5;
		EasyMock.expect(questionnairePage.getOldNumber()).andReturn(oldNumber).anyTimes();
		final int number = 3;
		EasyMock.expect(questionnairePage.getNumber()).andReturn(number).anyTimes();
		final QuestionnairePage pageAfterPage = createMock("PageAfterPage", QuestionnairePage.class);
		final List<QuestionnairePage> pagesAfterPage = Arrays.asList(pageAfterPage, questionnairePage);
		EasyMock.expect(getQuestionnairePageDAO().findPagesAfterPage(number, type)).andReturn(pagesAfterPage).anyTimes();
		pageAfterPage.setNumber(number + 1);
		getQuestionnairePageDAO().makeAllPersistent(EasyMock.eq(Arrays.asList(pageAfterPage, questionnairePage)));
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().savePage(questionnairePage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#savePage(com.bearcode.ovf.model.questionnaire.QuestionnairePage)} for
	 * the case where the page number is increased.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	@Test
	public final void testSavePage_numberIncreased() {
		final QuestionnairePage questionnairePage = createMock("QuestionnairePage", QuestionnairePage.class);
		final long id = 1238l;
		EasyMock.expect(questionnairePage.getId()).andReturn(id).anyTimes();
		final PageType type = PageType.DOMESTIC_ABSENTEE;
		EasyMock.expect(questionnairePage.getType()).andReturn(type).anyTimes();
		final int oldNumber = 3;
		EasyMock.expect(questionnairePage.getOldNumber()).andReturn(oldNumber).anyTimes();
		final int number = 5;
		EasyMock.expect(questionnairePage.getNumber()).andReturn(number).anyTimes();
		questionnairePage.setNumber(number - 1);
		EasyMock.expectLastCall().anyTimes();
		final QuestionnairePage pageAfterPage = createMock("PageAfterPage", QuestionnairePage.class);
		final List<QuestionnairePage> pagesAfterPage = Arrays.asList(questionnairePage, pageAfterPage);
		EasyMock.expect(getQuestionnairePageDAO().findPagesAfterPage(oldNumber, type)).andReturn(pagesAfterPage).anyTimes();
		pageAfterPage.setNumber(oldNumber + 1);
		getQuestionnairePageDAO().makeAllPersistent(EasyMock.eq(Arrays.asList(questionnairePage, pageAfterPage)));
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().savePage(questionnairePage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveQuestion(com.bearcode.ovf.model.questionnaire.Question)} for the
	 * case where the order is decreased.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	@Test
	public final void testSaveQuestion_orderDecreased() {
		final Question question = createMock("Question", Question.class);
		final long id = 61270l;
		EasyMock.expect(question.getId()).andReturn(id).anyTimes();
		final int oldOrder = 9;
		EasyMock.expect(question.getOldOrder()).andReturn(oldOrder).anyTimes();
		final int order = 4;
		EasyMock.expect(question.getOrder()).andReturn(order).anyTimes();
		final Question questionAfterQuestion = createMock("QuestionAfterQuestion", Question.class);
		final Collection<Question> questionsAfterQuestion = Arrays.asList(questionAfterQuestion, question);
		EasyMock.expect(getQuestionDAO().findQuestionsAfterQuestion(question, order)).andReturn(questionsAfterQuestion).anyTimes();
		questionAfterQuestion.setOrder(order + 1);
		getQuestionDAO().makeAllPersistent(EasyMock.eq(Arrays.asList(questionAfterQuestion, question)));
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveQuestion(question);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveQuestion(com.bearcode.ovf.model.questionnaire.Question)} for the
	 * case where the order is increased.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	@Test
	public final void testSaveQuestion_orderIncreased() {
		final Question question = createMock("Question", Question.class);
		final long id = 61270l;
		EasyMock.expect(question.getId()).andReturn(id).anyTimes();
		final int oldOrder = 2;
		EasyMock.expect(question.getOldOrder()).andReturn(oldOrder).anyTimes();
		final int order = 8;
		EasyMock.expect(question.getOrder()).andReturn(order).anyTimes();
		question.setOrder(order - 1);
		EasyMock.expectLastCall().anyTimes();
		final Question questionAfterQuestion = createMock("QuestionAfterQuestion", Question.class);
		final Collection<Question> questionsAfterQuestion = Arrays.asList(question, questionAfterQuestion);
		EasyMock.expect(getQuestionDAO().findQuestionsAfterQuestion(question, oldOrder)).andReturn(questionsAfterQuestion)
				.anyTimes();
		questionAfterQuestion.setOrder(oldOrder + 1);
		getQuestionDAO().makeAllPersistent(EasyMock.eq(Arrays.asList(question, questionAfterQuestion)));
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveQuestion(question);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveQuestionVariant(com.bearcode.ovf.model.questionnaire.QuestionVariant)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	@Test
	public final void testSaveQuestionVariant() {
		final QuestionVariant questionVariant = createMock("QuestionVariant", QuestionVariant.class);
		getQuestionDAO().makePersistent(questionVariant);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveQuestionVariant(questionVariant);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveWizardResults(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * for an empty current address.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveWizardResults_emptyCurrentAddress() {
		final WizardResults results = createMock("Results", WizardResults.class);
		final WizardResultAddress votingAddress = createAddress("Voting", false);
		EasyMock.expect(results.getVotingAddress()).andReturn(votingAddress).anyTimes();
		final WizardResultAddress currentAddress = createAddress("Current", true);
		EasyMock.expect(results.getCurrentAddress()).andReturn(currentAddress);
		results.setCurrentAddress(null);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(results.getCurrentAddress()).andReturn(null).anyTimes();
		final WizardResultAddress forwardingAddress = createAddress("Forwarding", false);
		EasyMock.expect(results.getForwardingAddress()).andReturn(forwardingAddress).anyTimes();
		final WizardResultAddress previousAddress = createAddress("Previous", false);
		EasyMock.expect(results.getPreviousAddress()).andReturn(previousAddress).anyTimes();
		results.setLastChangedDate((Date) EasyMock.anyObject());
		EasyMock.expectLastCall().anyTimes();
		getPdfAnswersDAO().makePersistent(results);
		final Answer answer = createMock("Answer", Answer.class);
		final Collection<Answer> answers = Arrays.asList(answer);
		EasyMock.expect(results.getAnswers()).andReturn(answers).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).anyTimes();
		EasyMock.expect(field.isSecurity()).andReturn(false).anyTimes();
		getAnswerDAO().makePersistent(answer);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveWizardResults(results);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveWizardResults(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * for an empty forwarding address.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveWizardResults_emptyForwardingAddress() {
		final WizardResults results = createMock("Results", WizardResults.class);
		final WizardResultAddress votingAddress = createAddress("Voting", false);
		EasyMock.expect(results.getVotingAddress()).andReturn(votingAddress).anyTimes();
		final WizardResultAddress currentAddress = createAddress("Current", false);
		EasyMock.expect(results.getCurrentAddress()).andReturn(currentAddress).anyTimes();
		final WizardResultAddress forwardingAddress = createAddress("Forwarding", true);
		EasyMock.expect(results.getForwardingAddress()).andReturn(forwardingAddress);
		results.setForwardingAddress(null);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(results.getForwardingAddress()).andReturn(null).anyTimes();
		final WizardResultAddress previousAddress = createAddress("Previous", false);
		EasyMock.expect(results.getPreviousAddress()).andReturn(previousAddress).anyTimes();
		results.setLastChangedDate((Date) EasyMock.anyObject());
		EasyMock.expectLastCall().anyTimes();
		getPdfAnswersDAO().makePersistent(results);
		final Answer answer = createMock("Answer", Answer.class);
		final Collection<Answer> answers = Arrays.asList(answer);
		EasyMock.expect(results.getAnswers()).andReturn(answers).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).anyTimes();
		EasyMock.expect(field.isSecurity()).andReturn(false).anyTimes();
		getAnswerDAO().makePersistent(answer);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveWizardResults(results);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveWizardResults(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * for an empty previous address.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveWizardResults_emptyPreviousAddress() {
		final WizardResults results = createMock("Results", WizardResults.class);
		final WizardResultAddress votingAddress = createAddress("Voting", false);
		EasyMock.expect(results.getVotingAddress()).andReturn(votingAddress).anyTimes();
		final WizardResultAddress currentAddress = createAddress("Current", false);
		EasyMock.expect(results.getCurrentAddress()).andReturn(currentAddress).anyTimes();
		final WizardResultAddress forwardingAddress = createAddress("Forwarding", false);
		EasyMock.expect(results.getForwardingAddress()).andReturn(forwardingAddress).anyTimes();
		final WizardResultAddress previousAddress = createAddress("Previous", true);
		EasyMock.expect(results.getPreviousAddress()).andReturn(previousAddress);
		results.setPreviousAddress(null);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(results.getPreviousAddress()).andReturn(null).anyTimes();
		results.setLastChangedDate((Date) EasyMock.anyObject());
		EasyMock.expectLastCall().anyTimes();
		getPdfAnswersDAO().makePersistent(results);
		final Answer answer = createMock("Answer", Answer.class);
		final Collection<Answer> answers = Arrays.asList(answer);
		EasyMock.expect(results.getAnswers()).andReturn(answers).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).anyTimes();
		EasyMock.expect(field.isSecurity()).andReturn(false).anyTimes();
		getAnswerDAO().makePersistent(answer);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveWizardResults(results);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveWizardResults(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * for an empty voting address.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveWizardResults_emptyVotingAddress() {
		final WizardResults results = createMock("Results", WizardResults.class);
		final WizardResultAddress votingAddress = createAddress("Voting", true);
		EasyMock.expect(results.getVotingAddress()).andReturn(votingAddress);
		results.setVotingAddress(null);
		EasyMock.expectLastCall().anyTimes();
		EasyMock.expect(results.getVotingAddress()).andReturn(null).anyTimes();
		final WizardResultAddress currentAddress = createAddress("Current", false);
		EasyMock.expect(results.getCurrentAddress()).andReturn(currentAddress).anyTimes();
		final WizardResultAddress forwardingAddress = createAddress("Forwarding", false);
		EasyMock.expect(results.getForwardingAddress()).andReturn(forwardingAddress).anyTimes();
		final WizardResultAddress previousAddress = createAddress("Previous", false);
		EasyMock.expect(results.getPreviousAddress()).andReturn(previousAddress).anyTimes();
		results.setLastChangedDate((Date) EasyMock.anyObject());
		EasyMock.expectLastCall().anyTimes();
		getPdfAnswersDAO().makePersistent(results);
		final Answer answer = createMock("Answer", Answer.class);
		final Collection<Answer> answers = Arrays.asList(answer);
		EasyMock.expect(results.getAnswers()).andReturn(answers).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).anyTimes();
		EasyMock.expect(field.isSecurity()).andReturn(false).anyTimes();
		getAnswerDAO().makePersistent(answer);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		getQuestionnaireService().saveWizardResults(results);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.QuestionnaireService#saveWizardResults(com.bearcode.ovf.model.questionnaire.WizardResults)}
	 * for security answer.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	@Test
	public final void testSaveWizardResults_securityAnswer() {
		final WizardResults results = createMock("Results", WizardResults.class);
		final WizardResultAddress votingAddress = createAddress("Voting", false);
		EasyMock.expect(results.getVotingAddress()).andReturn(votingAddress).anyTimes();
		final WizardResultAddress currentAddress = createAddress("Current", false);
		EasyMock.expect(results.getCurrentAddress()).andReturn(currentAddress).anyTimes();
		final WizardResultAddress forwardingAddress = createAddress("Forwarding", false);
		EasyMock.expect(results.getForwardingAddress()).andReturn(forwardingAddress).anyTimes();
		final WizardResultAddress previousAddress = createAddress("Previous", false);
		EasyMock.expect(results.getPreviousAddress()).andReturn(previousAddress).anyTimes();
		results.setLastChangedDate((Date) EasyMock.anyObject());
		EasyMock.expectLastCall().anyTimes();
		getPdfAnswersDAO().makePersistent(results);
		final Answer answer = createMock("Answer", Answer.class);
		final Collection<Answer> answers = Arrays.asList(answer);
		EasyMock.expect(results.getAnswers()).andReturn(answers).anyTimes();
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field).anyTimes();
		EasyMock.expect(field.isSecurity()).andReturn(true).anyTimes();
		replayAll();

		getQuestionnaireService().saveWizardResults(results);

		verifyAll();
	}

	/**
	 * Creates an address with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the address.
	 * @param empty
	 *            <code>true</code> if the address should be empty, <code>false</code> if it should not.
	 * @return the address.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private WizardResultAddress createAddress(final String name, final boolean empty) {
		final WizardResultAddress address = createMock(name, WizardResultAddress.class);
		EasyMock.expect(address.isEmptySpace()).andReturn(empty).anyTimes();
		if (empty) {
			getPdfAnswersDAO().makeTransient(address);
			EasyMock.expectLastCall().anyTimes();
		}
		return address;
	}

	/**
	 * Creates dependencies to delete for a specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the dependencies to delete.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<BasicDependency> createDependenciesToDelete(final String name) {
		final String dependencyName = name + "Dependency";
		final BasicDependency dependency = createMock(dependencyName, BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		getQuestionnairePageDAO().makeAllTransient(dependencies);
		EasyMock.expectLastCall().anyTimes();
		return dependencies;
	}

	/**
	 * Creates dependencies to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the dependencies to persist.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private Collection<BasicDependency> createDependenciesToPersist(final String name) {
		final String dependencyName = name + "Dependency";
		final BasicDependency dependency = createMock(dependencyName, BasicDependency.class);
		final Collection<BasicDependency> dependencies = Arrays.asList(dependency);
		getQuestionnairePageDAO().makeAllPersistent(dependencies);
		EasyMock.expectLastCall().anyTimes();
		return dependencies;
	}

	/**
	 * Creates field dependencies to delete using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the field dependencies to delete.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private Collection<FieldDependency> createFieldDependenciesToDelete(final String name) {
		final String fieldDependencyName = name + "FieldDependency";
		final FieldDependency fieldDependency = createMock(fieldDependencyName, FieldDependency.class);
		final Collection<FieldDependency> fieldDependencies = Arrays.asList(fieldDependency);
		getQuestionnairePageDAO().makeAllTransient(fieldDependencies);
		EasyMock.expectLastCall().anyTimes();
		return fieldDependencies;
	}

	/**
	 * Creates field dependencies to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the field dependencies to persist.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private Collection<FieldDependency> createFieldDependenciesToPersist(final String name) {
		final String fieldDependencyName = name + "FieldDependency";
		final FieldDependency fieldDependency = createMock(fieldDependencyName, FieldDependency.class);
		final Collection<FieldDependency> fieldDependencies = Arrays.asList(fieldDependency);
		getQuestionnairePageDAO().makeAllPersistent(fieldDependencies);
		EasyMock.expectLastCall().anyTimes();
		return fieldDependencies;
	}

	/**
	 * Creates fields to delete using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the fields to delete.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<QuestionField> createFieldsToDelete(final String name) {
		final String fieldName = name + "Field";
		final QuestionField field = createMock(fieldName, QuestionField.class);
		final Collection<FieldDictionaryItem> genericOptions = createItemsToDelete(fieldName + "GenericOptions");
		EasyMock.expect(field.getGenericOptions()).andReturn(genericOptions).anyTimes();
		final Collection<QuestionField> fields = Arrays.asList(field);
		getQuestionnairePageDAO().makeAllTransient(fields);
		EasyMock.expectLastCall().anyTimes();
		return fields;
	}

	/**
	 * Creates fields to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the fields to persist.
	 * @since May 15, 2012
	 * @version May 16, 2012
	 */
	private Collection<QuestionField> createFieldsToPersist(final String name) {
		final String fieldName = name + "Field";
		final QuestionField field = createMock(fieldName, QuestionField.class);
		final Collection<FieldDictionaryItem> fieldGenericOptions = createItemsToPersist(fieldName + "GenericOptions");
		EasyMock.expect(field.getGenericOptions()).andReturn(fieldGenericOptions).anyTimes();
		final Collection<QuestionField> fields = Arrays.asList(field);
		getQuestionnairePageDAO().makeAllPersistent(fields);
		EasyMock.expectLastCall().anyTimes();
		return fields;
	}

	/**
	 * Creates fillings to delete using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the fillings to delete.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private Collection<PdfFilling> createFillingsToDelete(final String name) {
		final String fillingName = name + "Filling";
		final PdfFilling filling = createMock(fillingName, PdfFilling.class);
		final Collection<PdfFilling> fillings = Arrays.asList(filling);
		getQuestionnairePageDAO().makeAllTransient(fillings);
		EasyMock.expectLastCall().anyTimes();
		return fillings;
	}

	/**
	 * Creates fillings to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the fillings to persist.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private Collection<PdfFilling> createFillingsToPersist(final String name) {
		final String fillingName = name + "Filling";
		final PdfFilling filling = createMock(fillingName, PdfFilling.class);
		final Collection<BasicDependency> fillingKeys = createDependenciesToPersist(fillingName + "Keys");
		EasyMock.expect(filling.getKeys()).andReturn(fillingKeys).anyTimes();
		final Collection<PdfFilling> fillings = Arrays.asList(filling);
		getQuestionnairePageDAO().makeAllPersistent(fillings);
		EasyMock.expectLastCall().anyTimes();
		return fillings;
	}

	/**
	 * Creates groups to delete with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the groups to delete.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private List<Question> createGroupsToDelete(final String name) {
		final String groupName = name + "Group";
		final Question group = createMock(groupName, Question.class);
		final Collection<QuestionVariant> groupVariants = createVariantsToDelete(groupName + "Variants");
		EasyMock.expect(group.getVariants()).andReturn(groupVariants).anyTimes();
		final List<Question> groups = Arrays.asList(group);
		getQuestionnairePageDAO().makeAllTransient(groups);
		EasyMock.expectLastCall().anyTimes();
		return groups;
	}

	/**
	 * Creates groups to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the groups to persist.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private List<Question> createGroupsToPersist(final String name) {
		final String groupName = name + "Group";
		final Question group = createMock(groupName, Question.class);
		final Collection<QuestionVariant> groupVariants = createVariantsToPersist(groupName + "Variants");
		EasyMock.expect(group.getVariants()).andReturn(groupVariants).anyTimes();
		final List<Question> groups = Arrays.asList(group);
		getQuestionnairePageDAO().makeAllPersistent(groups);
		EasyMock.expectLastCall().anyTimes();
		return groups;
	}

	/**
	 * Creates items to be deleted for the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the items.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<FieldDictionaryItem> createItemsToDelete(final String name) {
		final String itemName = name + "Item";
		final FieldDictionaryItem item = createMock(itemName, FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> items = Arrays.asList(item);
		getQuestionnairePageDAO().makeAllTransient(items);
		EasyMock.expectLastCall().anyTimes();
		return items;
	}

	/**
	 * Creates items to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the items to persist.
	 * @since May 16, 2012
	 * @version May 16, 2012
	 */
	private Collection<FieldDictionaryItem> createItemsToPersist(final String name) {
		final String itemName = name + "Item";
		final FieldDictionaryItem item = createMock(itemName, FieldDictionaryItem.class);
		final Collection<FieldDictionaryItem> items = Arrays.asList(item);
		getQuestionnairePageDAO().makeAllPersistent(items);
		EasyMock.expectLastCall().anyTimes();
		return items;
	}

	/**
	 * Creates pages to delete with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the pages to delete.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<QuestionnairePage> createPagesToDelete(final String name) {
		final String pageName = name + "Page";
		final QuestionnairePage page = createMock(pageName, QuestionnairePage.class);
		final List<Question> pageQuestions = createGroupsToDelete(pageName + "Questions");
		EasyMock.expect(page.getQuestions()).andReturn(pageQuestions).anyTimes();
		final Collection<QuestionnairePage> pages = Arrays.asList(page);
		getQuestionnairePageDAO().makeAllTransient(pages);
		EasyMock.expectLastCall().anyTimes();
		return pages;
	}

	/**
	 * Creates pages to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the pages to persist.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<QuestionnairePage> createPagesToPersist(final String name) {
		final String pageName = name + "Page";
		final QuestionnairePage page = createMock(pageName, QuestionnairePage.class);
		final List<Question> pageQuestions = createGroupsToPersist(pageName + "Questions");
		EasyMock.expect(page.getQuestions()).andReturn(pageQuestions).anyTimes();
		final Collection<QuestionnairePage> pages = Arrays.asList(page);
		getQuestionnairePageDAO().makeAllPersistent(pages);
		EasyMock.expectLastCall().anyTimes();
		return pages;
	}

	/**
	 * Creates variants to delete using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the variants to delete.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<QuestionVariant> createVariantsToDelete(final String name) {
		final String variantName = name + "Variant";
		final QuestionVariant variant = createMock(variantName, QuestionVariant.class);
		final Collection<QuestionField> variantFields = createFieldsToDelete(variantName + "Fields");
		EasyMock.expect(variant.getFields()).andReturn(variantFields).anyTimes();
		final Collection<BasicDependency> variantKeys = createDependenciesToDelete(variantName + "Keys");
		EasyMock.expect(variant.getKeys()).andReturn(variantKeys).anyTimes();
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		getQuestionnairePageDAO().makeAllTransient(variants);
		EasyMock.expectLastCall().anyTimes();
		return variants;
	}

	/**
	 * Creates variants to persist using the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the variants to persist.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private Collection<QuestionVariant> createVariantsToPersist(final String name) {
		final String variantName = name + "Variant";
		final QuestionVariant variant = createMock(variantName, QuestionVariant.class);
		final Collection<QuestionField> variantFields = createFieldsToPersist(variantName + "Fields");
		EasyMock.expect(variant.getFields()).andReturn(variantFields).anyTimes();
		final Collection<BasicDependency> variantKeys = createDependenciesToPersist(variantName + "Keys");
		EasyMock.expect(variant.getKeys()).andReturn(variantKeys).anyTimes();
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		getQuestionnairePageDAO().makeAllPersistent(variants);
		EasyMock.expectLastCall().anyTimes();
		return variants;
	}

	/**
	 * Gets the answer DAO.
	 * 
	 * @author IanBrown
	 * @return the answer DAO.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private AnswerDAO getAnswerDAO() {
		return answerDAO;
	}

	/**
	 * Gets the PDF answers DAO.
	 * 
	 * @author IanBrown
	 * @return the PDF answers DAO.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private PdfAnswersDAO getPdfAnswersDAO() {
		return pdfAnswersDAO;
	}

	/**
	 * Gets the question DAO.
	 * 
	 * @author IanBrown
	 * @return the question DAO.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private QuestionDAO getQuestionDAO() {
		return questionDAO;
	}

	/**
	 * Gets the question field DAO.
	 * 
	 * @author IanBrown
	 * @return the question field DAO.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private QuestionFieldDAO getQuestionFieldDAO() {
		return questionFieldDAO;
	}

	/**
	 * Gets the questionnaire page DAO.
	 * 
	 * @author IanBrown
	 * @return the questionnaire page DAO.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private QuestionnairePageDAO getQuestionnairePageDAO() {
		return questionnairePageDAO;
	}

	/**
	 * Gets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @return the questionnaire service.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private QuestionnaireService getQuestionnaireService() {
		return questionnaireService;
	}

	/**
	 * Sets the answer DAO.
	 * 
	 * @author IanBrown
	 * @param answerDAO
	 *            the answer DAO to set.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private void setAnswerDAO(final AnswerDAO answerDAO) {
		this.answerDAO = answerDAO;
	}

	/**
	 * Sets the PDF answers DAO.
	 * 
	 * @author IanBrown
	 * @param pdfAnswersDAO
	 *            the PDF answers DAO to set.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private void setPdfAnswersDAO(final PdfAnswersDAO pdfAnswersDAO) {
		this.pdfAnswersDAO = pdfAnswersDAO;
	}

	/**
	 * Sets the question DAO.
	 * 
	 * @author IanBrown
	 * @param questionDAO
	 *            the question DAO to set.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private void setQuestionDAO(final QuestionDAO questionDAO) {
		this.questionDAO = questionDAO;
	}

	/**
	 * Sets the question field DAO.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the question field DAO to set.
	 * @since May 25, 2012
	 * @version May 25, 2012
	 */
	private void setQuestionFieldDAO(final QuestionFieldDAO questionFieldDAO) {
		this.questionFieldDAO = questionFieldDAO;
	}

	/**
	 * Sets the questionnaire page DAO.
	 * 
	 * @author IanBrown
	 * @param questionnairePageDAO
	 *            the questionnaire page DAO to set.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private void setQuestionnairePageDAO(final QuestionnairePageDAO questionnairePageDAO) {
		this.questionnairePageDAO = questionnairePageDAO;
	}

	/**
	 * Sets the questionnaire service.
	 * 
	 * @author IanBrown
	 * @param questionnaireService
	 *            the questionnaire service to set.
	 * @since May 15, 2012
	 * @version May 15, 2012
	 */
	private void setQuestionnaireService(final QuestionnaireService questionnaireService) {
		this.questionnaireService = questionnaireService;
	}

	/**
	 * Sets up the page to be deleted.
	 * 
	 * @author IanBrown
	 * @param page
	 *            the page.
	 * @since May 24, 2012
	 * @version May 25, 2012
	 */
	private final void setUpPageForDelete(final QuestionnairePage page) {
		final long pageId = 981298l;
		final int pageNumber = 2;
		final PageType pageType = PageType.OVERSEAS;
		EasyMock.expect(page.getId()).andReturn(pageId).anyTimes();
		EasyMock.expect(getQuestionnairePageDAO().findById(pageId)).andReturn(page).anyTimes();
		EasyMock.expect(page.getNumber()).andReturn(pageNumber).anyTimes();
		EasyMock.expect(page.getType()).andReturn(pageType).anyTimes();
		final QuestionnairePage afterPage = createMock("AfterPage", QuestionnairePage.class);
		final List<QuestionnairePage> pagesAfterPage = Arrays.asList(afterPage);
		EasyMock.expect(getQuestionnairePageDAO().findPagesAfterPage(pageNumber + 1, pageType)).andReturn(pagesAfterPage)
				.anyTimes();
		afterPage.setNumber(pageNumber);
		EasyMock.expectLastCall().anyTimes();
		getQuestionnairePageDAO().makeAllPersistent(pagesAfterPage);
		EasyMock.expectLastCall().anyTimes();
		getQuestionnairePageDAO().makeTransient(page);
		EasyMock.expectLastCall().anyTimes();
	}

	/**
	 * Sets up to delete the question.
	 * 
	 * @author IanBrown
	 * @param question
	 *            the question.
	 * @since May 24, 2012
	 * @version May 25, 2012
	 */
	private void setUpQuestionForDelete(final Question question) {
		final long questionId = 287358l;
		final int order = 3;
		EasyMock.expect(question.getId()).andReturn(questionId).anyTimes();
		EasyMock.expect(getQuestionDAO().findQuestionById(questionId)).andReturn(question).anyTimes();
		EasyMock.expect(question.getOrder()).andReturn(order).anyTimes();
		final Question afterQuestion = createMock("AfterQuestion", Question.class);
		final Collection<Question> questionsAfterQuestion = Arrays.asList(afterQuestion);
		EasyMock.expect(getQuestionDAO().findQuestionsAfterQuestion(question, order + 1)).andReturn(questionsAfterQuestion)
				.anyTimes();
		afterQuestion.setOrder(order);
		EasyMock.expectLastCall().anyTimes();
		getQuestionDAO().makeAllPersistent(questionsAfterQuestion);
		EasyMock.expectLastCall().anyTimes();
		getQuestionDAO().makeTransient(question);
		EasyMock.expectLastCall().anyTimes();
	}

	/**
	 * Sets up the variant for delete, with the optional keys.
	 * 
	 * @author IanBrown
	 * @param variant
	 *            the variant.
	 * @param keys
	 *            the optional keys for the variant.
	 * @since May 24, 2012
	 * @version May 25, 2012
	 */
	private final void setUpVariantForDelete(final QuestionVariant variant, final Collection<BasicDependency> keys) {
		final long variantId = 789186l;
		EasyMock.expect(variant.getId()).andReturn(variantId).anyTimes();
		EasyMock.expect(getQuestionDAO().findQuestionVariantById(variantId)).andReturn(variant).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(keys).anyTimes();
		if (keys != null) {
			getQuestionDAO().makeAllTransient(keys);
			EasyMock.expectLastCall().anyTimes();
		}
		getQuestionDAO().makeTransient(variant);
		EasyMock.expectLastCall().anyTimes();
	}
}
