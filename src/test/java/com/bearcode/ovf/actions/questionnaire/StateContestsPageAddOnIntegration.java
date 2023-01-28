/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bearcode.ovf.actions.commons.AbstractComponentExam;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Extended {@link AbstractComponentExam} integration test for {@link StateContestsPageAddOn}.
 * 
 * @author IanBrown
 * 
 * @since Aug 6, 2012
 * @version Oct 11, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "StateContestsPageAddOnIntegration-context.xml" })
@DirtiesContext
public final class StateContestsPageAddOnIntegration extends AbstractComponentExam<StateContestsPageAddOn> {

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@Autowired
	private StateService stateService;

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * the state contests page add on.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	@Autowired
	private StateContestsPageAddOn stateContestsPageAddOn;

	/**
	 * the candidate page add on.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private CandidatePageAddon candidatePageAddon;

	/**
	 * Sets up the state contests page add on for testing.
	 * 
	 * @author IanBrown
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	@Before
	public final void setUpStateContestsPageAddOn() {
		setCandidatePageAddon(EasyMock.createMock("CandidatePageAddon", CandidatePageAddon.class));
		getStateContestsPageAddOn().setCandidatePageAddon(getCandidatePageAddon());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for someone indefinitely overseas.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the VIP data file.
	 * @throws JAXBException
	 *             if there is a problem loading the VIP data.
	 * @throws FileNotFoundException
	 *             if the VIP data file does not exist.
	 * @since Aug 6, 2012
	 * @version Aug 8, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPrepareAddOnPage_indefinite() throws FileNotFoundException, JAXBException, IOException {
		loadVipData();
		final WizardResults wizardResults = new WizardResults(FlowType.FWAB);
		final WizardContext form = new WizardContext(wizardResults);
		final WizardResultAddress votingAddress = new WizardResultAddress();
		votingAddress.setStreet1("2 E Guinevere Dr SE");
		votingAddress.setCity("Annandale");
		votingAddress.setState("VA");
		votingAddress.setZip("22003");
		wizardResults.setVotingAddress(votingAddress);
		final State state = getStateService().findByAbbreviation(votingAddress.getState());
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setState(state);
		votingRegion.setName("Adams County");
		wizardResults.setVotingRegion(votingRegion);
		wizardResults.setVoterType(VoterType.OVERSEAS_VOTER.name());
		final QuestionnairePage currentPage = new QuestionnairePage();
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		final List<Question> questions = currentPage.getQuestions();
		assertNotNull("There is a list of questions on the current page", questions);
		assertFalse("There is at least one question on the current page", questions.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for a state that isn't supported.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the VIP data file.
	 * @throws JAXBException
	 *             if there is a problem loading the VIP data.
	 * @throws FileNotFoundException
	 *             if the VIP data file does not exist.
	 * @since Aug 6, 2012
	 * @version Aug 10, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPrepareAddOnPage_notSupported() throws FileNotFoundException, JAXBException, IOException {
		loadVipData();
		final WizardResults wizardResults = new WizardResults(FlowType.FWAB);
		final WizardContext form = new WizardContext(wizardResults);
		final WizardResultAddress votingAddress = new WizardResultAddress();
		votingAddress.setType(AddressType.STREET);
		votingAddress.setState("VT");
		wizardResults.setVotingAddress(votingAddress);
		final State state = getStateService().findByAbbreviation(votingAddress.getState());
		final VotingRegion votingRegion = getStateService().findRegionsForState(state).iterator().next();
		wizardResults.setVotingRegion(votingRegion);
		final QuestionnairePage currentPage = new QuestionnairePage();
		form.setCurrentPage(currentPage);
		final Question question = EasyMock.createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		getCandidatePageAddon().prepareAddOnPage(form, currentPage);
		EasyMock.expectLastCall().andDelegateTo(new CandidatePageAddon() {
			@Override
			public void prepareAddOnPage(final WizardContext form, final QuestionnairePage currentPage) {
				currentPage.setQuestions(questions);
			}
		});
		EasyMock.replay(question, getCandidatePageAddon());

		getComponent().prepareAddOnPage(form, currentPage);

		final List<Question> actualQuestions = currentPage.getQuestions();
		assertSame("The questions are added to the current page", questions, actualQuestions);
		EasyMock.verify(getCandidatePageAddon(), question);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for someone temporarily overseas.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the VIP data file.
	 * @throws JAXBException
	 *             if there is a problem loading the VIP data.
	 * @throws FileNotFoundException
	 *             if the VIP data file does not exist.
	 * @since Aug 6, 2012
	 * @version Aug 14, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPrepareAddOnPage_temporary() throws FileNotFoundException, JAXBException, IOException {
		loadVipData();
		final WizardResults wizardResults = new WizardResults(FlowType.FWAB);
		final WizardContext form = new WizardContext(wizardResults);
		final WizardResultAddress votingAddress = new WizardResultAddress();
		votingAddress.setStreet1("2 E Guinevere Dr SE");
		votingAddress.setCity("Annandale");
		votingAddress.setState("VA");
		votingAddress.setZip("22003");
		wizardResults.setVotingAddress(votingAddress);
		final State state = getStateService().findByAbbreviation(votingAddress.getState());
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setState(state);
		votingRegion.setName("Adams County");
		wizardResults.setVotingRegion(votingRegion);
		final String voterType = VoterType.OVERSEAS_VOTER.name();
		wizardResults.setVoterType(voterType);
		final QuestionnairePage currentPage = new QuestionnairePage();
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		final List<Question> questions = currentPage.getQuestions();
		assertNotNull("There is a list of questions on the current page", questions);
		assertFalse("There is at least one question on the current page", questions.isEmpty());
		// showQuestions(voterType, questions);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.questionnaire.StateContestsPageAddOn#prepareAddOnPage(com.bearcode.ovf.actions.questionnaire.forms.WizardContext, com.bearcode.ovf.model.questionnaire.QuestionnairePage)}
	 * for a uniformed voter overseas.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem with the VIP data file.
	 * @throws JAXBException
	 *             if there is a problem loading the VIP data.
	 * @throws FileNotFoundException
	 *             if the VIP data file does not exist.
	 * @since Aug 10, 2012
	 * @version Oct 8, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testPrepareAddOnPage_uniformed() throws FileNotFoundException, JAXBException, IOException {
		loadVipData();
		final WizardResults wizardResults = new WizardResults(FlowType.FWAB);
		final WizardContext form = new WizardContext(wizardResults);
		final WizardResultAddress votingAddress = new WizardResultAddress();
		votingAddress.setStreet1("2-A Main Dr");
		votingAddress.setCity("Annandale");
		votingAddress.setState("VA");
		votingAddress.setZip("22003");
		wizardResults.setVotingAddress(votingAddress);
		final State state = getStateService().findByAbbreviation(votingAddress.getState());
		final VotingRegion votingRegion = new VotingRegion();
		votingRegion.setState(state);
		votingRegion.setName("Adams County");
		wizardResults.setVotingRegion(votingRegion);
		final String voterType = VoterType.DOMESTIC_VOTER.name();
		wizardResults.setVoterType(voterType);
		final QuestionnairePage currentPage = new QuestionnairePage();
		form.setCurrentPage(currentPage);

		getComponent().prepareAddOnPage(form, currentPage);

		final List<Question> questions = currentPage.getQuestions();
		assertNotNull("There is a list of questions on the current page", questions);
		assertFalse("There is at least one question on the current page", questions.isEmpty());
		// showQuestions(voterType, questions);
	}

	/** {@inheritDoc} */
	@Override
	protected final StateContestsPageAddOn createComponent() {
		final StateContestsPageAddOn stateContestsPageAddOn = applicationContext.getBean(StateContestsPageAddOn.class);
		return stateContestsPageAddOn;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForComponent() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForComponent() {
	}

	/**
	 * Gets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @return the candidate page add on.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private CandidatePageAddon getCandidatePageAddon() {
		return candidatePageAddon;
	}

	/**
	 * Gets the state contests page add on.
	 * 
	 * @author IanBrown
	 * @return the state contests page add on.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private StateContestsPageAddOn getStateContestsPageAddOn() {
		return stateContestsPageAddOn;
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Loads the VIP data.
	 * 
	 * @author IanBrown
	 * @throws FileNotFoundException
	 *             if the XML file cannot be found.
	 * @throws JAXBException
	 *             if the XML file cannot be read.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Aug 8, 2012
	 * @version Oct 11, 2012
	 */
	private void loadVipData() throws FileNotFoundException, JAXBException, IOException {
		final String source = "src/test/resources/com/bearcode/ovf/tools/vip/vip.xml";
		final File sourceFile = new File(source);
		final FileInputStream fis = new FileInputStream(sourceFile);
		final JAXBContext context = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final VipObject vipObject = (VipObject) unmarshaller.unmarshal(fis);
		fis.close();
		getVipService().convert(vipObject, new Date());
	}

	/**
	 * Sets the candidate page add on.
	 * 
	 * @author IanBrown
	 * @param candidatePageAddon
	 *            the candidate page add on to set.
	 * @since Oct 10, 2012
	 * @version Oct 10, 2012
	 */
	private void setCandidatePageAddon(final CandidatePageAddon candidatePageAddon) {
		this.candidatePageAddon = candidatePageAddon;
	}

	/**
	 * Displays the generated questions for the named test.
	 * 
	 * @author IanBrown
	 * @param voterType
	 *            the type of voter.
	 * @param questions
	 *            the questions.
	 * @since Aug 10, 2012
	 * @version Aug 14, 2012
	 */
	@SuppressWarnings("unused")
	private void showQuestions(final String voterType, final List<Question> questions) {
		System.err.println("Questions for " + voterType);
		for (final Question question : questions) {
			System.err.println("Group: " + question.getName() + " " + question.getTitle());
			for (final QuestionVariant variant : question.getVariants()) {
				System.err.println("  Variant: " + variant.getTitle());
				for (final QuestionField field : variant.getFields()) {
					System.err.println("    Field: " + field.getType().getTemplateName() + " " + field.getTitle() + " ("
							+ field.getHelpText() + ")");
					if (field.getGenericOptions() != null) {
						for (final FieldDictionaryItem option : field.getGenericOptions()) {
							System.err.println("      Option: " + option.getValue());
						}
					}
				}
			}
		}
	}
}
