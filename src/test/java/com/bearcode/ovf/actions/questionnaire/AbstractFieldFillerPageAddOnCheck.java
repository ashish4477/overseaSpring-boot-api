/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.bearcode.ovf.model.questionnaire.*;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;

/**
 * Abstract test for implementations of {@link AbstractFieldFillerPageAddOn}.
 * 
 * @author IanBrown
 * 
 * @param <A>
 *            the type of field filler page add on to test.
 * @since Apr 24, 2012
 * @version Jul 12, 2012
 */
public abstract class AbstractFieldFillerPageAddOnCheck<A extends AbstractFieldFillerPageAddOn> extends EasyMockSupport {

	/**
	 * the add on to test.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private A addOn;

	/**
	 * Sets up to test the field filler page add on.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@Before
	public final void setUpFieldFillerPageAddOn() {
		setUpForAddOn();
		setAddOn(createAddOn());
	}

	/**
	 * Tears down the set up for testing the field filler page add on.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	@After
	public final void tearDownFieldFillerPageAddOn() {
		tearDownForAddOn();
		setAddOn(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#getFirstFieldId(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testGetFirstFieldId() {
		final QuestionnairePage page = createMock("WizardPage", QuestionnairePage.class);

		assertNull("No first field ID is available", getAddOn().getFirstFieldId(page));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there are no fields.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noFields() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final Collection<QuestionField> fields = null;
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for a page without questions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noQuestions() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final List<Question> questions = null;
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.AbstractFieldFillerPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is a question with no variants.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noVariants() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final Collection<QuestionVariant> variants = null;
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}


	/**
	 * Creates a field filler page add on of the type to test.
	 * 
	 * @author IanBrown
	 * @return the field filler page add on.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	protected abstract A createAddOn();

	/**
	 * Gets the add on.
	 * 
	 * @author IanBrown
	 * @return the add on.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	protected final A getAddOn() {
		return addOn;
	}

	/**
	 * Sets up to test the specific type of field filler page add on.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	protected abstract void setUpForAddOn();

	/**
	 * Tears down the set up for testing the specific type of field filler page add on.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	protected abstract void tearDownForAddOn();

	/**
	 * Sets the add on.
	 * 
	 * @author IanBrown
	 * @param addOn
	 *            the add on to set.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private void setAddOn(final A addOn) {
		this.addOn = addOn;
	}
}
