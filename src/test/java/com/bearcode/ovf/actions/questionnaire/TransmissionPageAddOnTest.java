/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.LocalOfficialService;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Extended {@link AbstractFieldFillerPageAddOnCheck} test for {@link TransmissionPageAddOn}.
 * 
 * @author IanBrown
 * 
 * @since Apr 23, 2012
 * @version Jul 30, 2012
 */
public final class TransmissionPageAddOnTest extends AbstractFieldFillerPageAddOnCheck<TransmissionPageAddOn> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.TransmissionPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Jul 30, 2012
	 */
	@Test
	public final void testPrepareAddOnPage() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField nameField = createMock("MailField", QuestionField.class);
		final QuestionField emailField = createMock("EmailField", QuestionField.class);
		final QuestionField faxField = createMock("FaxField", QuestionField.class);
		final QuestionField mailField = createMock("MailField", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(nameField, emailField, faxField, mailField);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		EasyMock.expect(getLocalOfficialService().findForRegion(votingRegion)).andReturn(localOfficial).anyTimes();
		final FieldType noInputType = createMock("NoInputType", FieldType.class);
		EasyMock.expect(noInputType.getTemplateName()).andReturn(FieldType.TEMPLATE_NOT_INPUT).anyTimes();
		EasyMock.expect(nameField.getType()).andReturn(noInputType).anyTimes();
		EasyMock.expect(nameField.getTitle()).andReturn(TransmissionPageAddOn.TRANSMISSION_NAME_FIELD).anyTimes();
		final Person leo = createMock("LEO", Person.class);
		EasyMock.expect(localOfficial.getLeo()).andReturn(leo).anyTimes();
		final String firstName = "First";
		EasyMock.expect(leo.getFirstName()).andReturn(firstName).anyTimes();
		final String lastName = "Last";
		EasyMock.expect(leo.getLastName()).andReturn(lastName).anyTimes();
		nameField.setHelpText(firstName + " " + lastName);
		EasyMock.expect(emailField.getType()).andReturn(noInputType).anyTimes();
		EasyMock.expect(emailField.getTitle()).andReturn(TransmissionPageAddOn.TRANSMISSION_EMAIL_FIELD).anyTimes();
		final String leoEmail = "LEO Email";
		EasyMock.expect(localOfficial.getLeoEmail()).andReturn(leoEmail).anyTimes();
		emailField.setHelpText(leoEmail);
		EasyMock.expect(faxField.getType()).andReturn(noInputType).anyTimes();
		EasyMock.expect(faxField.getTitle()).andReturn(TransmissionPageAddOn.TRANSMISSION_FAX_FIELD).anyTimes();
		final String leoFax = "LEO Fax";
		EasyMock.expect(localOfficial.getLeoFax()).andReturn(leoFax).anyTimes();
		faxField.setHelpText("+1 " + leoFax);
		EasyMock.expect(mailField.getType()).andReturn(noInputType).anyTimes();
		EasyMock.expect(mailField.getTitle()).andReturn(TransmissionPageAddOn.TRANSMISSION_MAIL_FIELD).anyTimes();
		final Address mailingAddress = createMock("MailingAddress", Address.class);
		EasyMock.expect(localOfficial.getMailing()).andReturn(mailingAddress).anyTimes();
		final String fullStreet = "Full Street";
		EasyMock.expect(mailingAddress.getFullStreet()).andReturn(fullStreet).anyTimes();
		final String city = "City";
		EasyMock.expect(mailingAddress.getCity()).andReturn(city).anyTimes();
		final String state = "ST";
		EasyMock.expect(mailingAddress.getState()).andReturn(state).anyTimes();
		final String zip = "12345";
		EasyMock.expect(mailingAddress.getZip()).andReturn(zip).anyTimes();
		final String zip4 = "6789";
		EasyMock.expect(mailingAddress.getZip4()).andReturn(zip4).anyTimes();
		final StringBuilder sb = new StringBuilder(fullStreet);
		sb.append("<br>\n").append(city).append(", ").append(state).append(" ").append(zip).append("-").append(zip4).append(" USA");
		mailField.setHelpText(EasyMock.eq(sb.toString()));
        EasyMock.expect(form.getFlowType()).andReturn(FlowType.DOMESTIC_ABSENTEE).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.TransmissionPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is no local official.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noLocalOfficial() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField nameField = createMock("MailField", QuestionField.class);
		final QuestionField emailField = createMock("EmailField", QuestionField.class);
		final QuestionField faxField = createMock("FaxField", QuestionField.class);
		final QuestionField mailField = createMock("MailField", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(nameField, emailField, faxField, mailField);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).anyTimes();
		EasyMock.expect(getLocalOfficialService().findForRegion(votingRegion)).andReturn(null).anyTimes();
        EasyMock.expect(form.getFlowType()).andReturn(FlowType.DOMESTIC_ABSENTEE).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.TransmissionPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for the case where there is no voting region.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testPrepareAddOnPage_noVotingRegion() {
		final WizardContext form = createMock("Form", WizardContext.class);
		final AddOnPage currentPage = createMock("CurrentPage", AddOnPage.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(currentPage.getQuestions()).andReturn(questions).anyTimes();
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(null).anyTimes();
		final QuestionField nameField = createMock("MailField", QuestionField.class);
		final QuestionField emailField = createMock("EmailField", QuestionField.class);
		final QuestionField faxField = createMock("FaxField", QuestionField.class);
		final QuestionField mailField = createMock("MailField", QuestionField.class);
		final Collection<QuestionField> fields = Arrays.asList(nameField, emailField, faxField, mailField);
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(form.getWizardResults()).andReturn(wizardResults).anyTimes();
		EasyMock.expect(wizardResults.getVotingRegion()).andReturn(null).anyTimes();
		replayAll();

		getAddOn().prepareAddOnPage(form, currentPage);

		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected TransmissionPageAddOn createAddOn() {
		final TransmissionPageAddOn transmissionPageAddOn = new TransmissionPageAddOn();
		transmissionPageAddOn.setLocalOfficialService(getLocalOfficialService());
		return transmissionPageAddOn;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForAddOn() {
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForAddOn() {
		setLocalOfficialService(null);
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}
}
