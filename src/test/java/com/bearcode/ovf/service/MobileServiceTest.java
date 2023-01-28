/**
 * 
 */
package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.*;
import com.bearcode.ovf.actions.questionnaire.AllowedForAddOn;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.model.mobile.*;
import com.bearcode.ovf.model.questionnaire.*;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.easymock.IAnswer;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Test for {@link MobileService}.
 * 
 * @author IanBrown
 * 
 * @since Apr 10, 2012
 * @version Aug 15, 2012
 */
public final class MobileServiceTest extends EasyMockSupport {

	/**
	 * the name of the mock add on bean.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private static final String MOCK_ADD_ON = "mockAddOn";

	/**
	 * the application context.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private ApplicationContext applicationContext;

	/**
	 * the question field DAO.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private QuestionFieldDAO questionFieldDAO;

	/**
	 * the mobile service to test.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private MobileService mobileService;

	/**
	 * the DAO for the local officials.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private LocalOfficialDAO localOfficialDAO;

	/**
	 * the DAO used to get the questionnaire pages.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private QuestionnairePageDAO pageDAO;

	/**
	 * the DAO used to get the state.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private StateDAO stateDAO;

	/**
	 * the DOA used to get the voting region.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private VotingRegionDAO votingRegionDAO;

	/**
	 * the format used to convert dates.
	 * 
	 * @author IanBrown
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * the mock add on to use.
	 * 
	 * @author IanBrown
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private AllowedForAddOn mockAddOn;

	/**
	 * Test method for {@link com.bearcode.ovf.service.MobileService#findLocalOfficialForRegion(String, String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void findLocalOfficialForRegion() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region Name";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(state);
		final VotingRegion expectedVotingRegion = createMock("ExpectedVotingRegion", VotingRegion.class);
		EasyMock.expect(getVotingRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andDelegateTo(
				new VotingRegionDAO() {
					@Override
					public final VotingRegion getRegionByName(final VotingRegion votingRegion) {
						assertSame("The state is correct", state, votingRegion.getState());
						assertEquals("The voting region name is correct", votingRegionName, votingRegion.getName());
						return expectedVotingRegion;
					}
				});
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		EasyMock.expect(getLocalOfficialDAO().findLeoByRegion(expectedVotingRegion)).andReturn(localOfficial);
		replayAll();

		final LocalOfficial actualLocalOfficial = getMobileService()
				.findLocalOfficialForRegion(stateAbbreviation, votingRegionName);

		assertSame("The local official is returned", localOfficial, actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MobileService#findLocalOfficialForRegion(String, String)} for the case where
	 * there is no local official.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void findLocalOfficialForRegion_noLocalOfficial() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region Name";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(state);
		final VotingRegion expectedVotingRegion = createMock("ExpectedVotingRegion", VotingRegion.class);
		EasyMock.expect(getVotingRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andDelegateTo(
				new VotingRegionDAO() {
					@Override
					public final VotingRegion getRegionByName(final VotingRegion votingRegion) {
						assertSame("The state is correct", state, votingRegion.getState());
						assertEquals("The voting region name is correct", votingRegionName, votingRegion.getName());
						return expectedVotingRegion;
					}
				});
		EasyMock.expect(getLocalOfficialDAO().findLeoByRegion(expectedVotingRegion)).andReturn(null);
		replayAll();

		final LocalOfficial actualLocalOfficial = getMobileService()
				.findLocalOfficialForRegion(stateAbbreviation, votingRegionName);

		assertNull("No local official is returned", actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MobileService#findLocalOfficialForRegion(String, String)} for the case where
	 * the state doesn't exist.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void findLocalOfficialForRegion_noState() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region Name";
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(null);
		replayAll();

		final LocalOfficial actualLocalOfficial = getMobileService()
				.findLocalOfficialForRegion(stateAbbreviation, votingRegionName);

		assertNull("No local official is returned", actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MobileService#findLocalOfficialForRegion(String, String)} for the case where
	 * the voting region doesn't exist.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void findLocalOfficialForRegion_noVotingRegion() {
		final String stateAbbreviation = "SA";
		final String votingRegionName = "Voting Region Name";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(state);
		EasyMock.expect(getVotingRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andDelegateTo(
				new VotingRegionDAO() {
					@Override
					public final VotingRegion getRegionByName(final VotingRegion votingRegion) {
						assertSame("The state is correct", state, votingRegion.getState());
						assertEquals("The voting region name is correct", votingRegionName, votingRegion.getName());
						return null;
					}
				});
		replayAll();

		final LocalOfficial actualLocalOfficial = getMobileService()
				.findLocalOfficialForRegion(stateAbbreviation, votingRegionName);

		assertNull("No local official is returned", actualLocalOfficial);
		verifyAll();
	}

	/**
	 * Sets up the mobile service to test.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Jul 16, 2012
	 */
	@Before
	public final void setUpMobileService() {
		setMockAddOn(createMock("MockAddOn", AllowedForAddOn.class));
		setApplicationContext(createMock("ApplicationContext", ApplicationContext.class));
		EasyMock.expect(getApplicationContext().getBean(MOCK_ADD_ON, AllowedForAddOn.class)).andReturn(getMockAddOn()).anyTimes();
		setLocalOfficialDAO(createMock("LocalOfficialDAO", LocalOfficialDAO.class));
		setQuestionFieldDAO(createMock("QuestionFieldDAO", QuestionFieldDAO.class));
		setPageDAO(createMock("PageDAO", QuestionnairePageDAO.class));
		setStateDAO(createMock("StateDAO", StateDAO.class));
		setVotingRegionDAO(createMock("VotingRegionDAO", VotingRegionDAO.class));
		setMobileService(new MobileService());
		getMobileService().setApplicationContext(getApplicationContext());
		getMobileService().setLocalOfficialDAO(getLocalOfficialDAO());
		getMobileService().setQuestionFieldDAO(getQuestionFieldDAO());
		getMobileService().setPageDAO(getPageDAO());
		getMobileService().setStateDAO(getStateDAO());
		getMobileService().setVotingRegionDAO(getVotingRegionDAO());
	}

	/**
	 * Tears down the mobile service after testing.
	 * 
	 * @author IanBrown
	 * @since Apr 10, 2012
	 * @version Jul 16, 2012
	 */
	@After
	public final void tearDownMobileService() {
		setMobileService(null);
		setVotingRegionDAO(null);
		setStateDAO(null);
		setPageDAO(null);
		setQuestionFieldDAO(null);
		setLocalOfficialDAO(null);
		setApplicationContext(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a question field with additional help.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_additionalHelp() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final String additionalHelp = "Additional help text";
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null,
				null, additionalHelp, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for an add on page with dynamic questions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_addOnDynamicQuestions() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final List<Question> questions = new LinkedList<Question>();
		final AddOnPage questionnairePage = createQuestionnairePage(AddOnPage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList((QuestionnairePage) questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		EasyMock.expect(questionnairePage.getBeanName()).andReturn(MOCK_ADD_ON).anyTimes();
		final long questionFieldId = 1298l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		getMockAddOn().prepareAddOnPage(wizardContext, questionnairePage);
		EasyMock.expectLastCall().andDelegateTo(new AllowedForAddOn() {

			@Override
			public final Long getFirstFieldId(final QuestionnairePage currentPage) {
				throw new UnsupportedOperationException("Not implemented for this class");
			}

			@Override
			public final void prepareAddOnPage(final WizardContext form, final QuestionnairePage currentPage) {
				questions.add(question);
			}

		}).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for an add on page that modifies a question.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_addOnModifiedQuestions() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 232l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final List<String> helpAnswers = new LinkedList<String>();
		final IAnswer<? extends String> helpTextAnswer = new IAnswer<String>() {

			@Override
			public final String answer() throws Throwable {
				return helpAnswers.get(0);
			}

		};
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, helpTextAnswer,
				null, null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final AddOnPage questionnairePage = createQuestionnairePage(AddOnPage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList((QuestionnairePage) questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		EasyMock.expect(questionnairePage.getBeanName()).andReturn(MOCK_ADD_ON).anyTimes();
		getMockAddOn().prepareAddOnPage(wizardContext, questionnairePage);
		final String helpAnswer = "Injected Help Text";
		EasyMock.expectLastCall().andDelegateTo(new AllowedForAddOn() {

			@Override
			public final Long getFirstFieldId(final QuestionnairePage currentPage) {
				throw new UnsupportedOperationException("Not implemented for this class");
			}

			@Override
			public final void prepareAddOnPage(final WizardContext form, final QuestionnairePage currentPage) {
				helpAnswers.add(helpAnswer);
			}

		}).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for an add on page without any questions.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 24, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_addOnNoQuestions() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final AddOnPage questionnairePage = createQuestionnairePage(AddOnPage.class, flowType, null);
		final List<QuestionnairePage> questionnairePages = Arrays.asList((QuestionnairePage) questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		EasyMock.expect(questionnairePage.getBeanName()).andReturn(MOCK_ADD_ON).anyTimes();
		getMockAddOn().prepareAddOnPage(wizardContext, questionnairePage);
		EasyMock.expectLastCall().anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertTrue("There are no mobile pages", actualMobilePages.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for an encoded security question field.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_encoded() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, true, true);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a face dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_faceDependency() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final FaceConfig currentFace = createMock("CurrentFace", FaceConfig.class);
		EasyMock.expect(wizardContext.getCurrentFace()).andReturn(currentFace).anyTimes();
		final FaceDependency faceDependency = createFaceDependency(wizardContext);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) faceDependency);
		final QuestionVariant variant = createVariant("Variant", keys, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a flow dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_flowDependency() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final FlowDependency flowDependency = createFlowDependency(wizardContext);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) flowDependency);
		final QuestionVariant variant = createVariant("Variant", keys, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a question field with help text.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_helpText() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final String helpText = "Help text";
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, helpText,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a simple case without any dependencies.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_noDependencies() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a question dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Sep 27, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_questionDependency() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long parentQuestionFieldId = 2l;
		final String parentTitle = "Parent Title";
		final FieldType parentFieldType = createFieldType(FieldType.TEMPLATE_RADIO);
		final String parentAnswer1 = "parent answer 1";
		final FieldDictionaryItem parentOption1 = createOption(parentAnswer1);
		final String parentAnswer2 = "parent answer 2";
		final FieldDictionaryItem parentOption2 = createOption(parentAnswer2);
		final Collection<FieldDictionaryItem> parentOptions = Arrays.asList(parentOption1, parentOption2);
		final String dataRole = "Data Role";
		final QuestionField parentField = createQuestionField(parentQuestionFieldId, parentTitle, parentFieldType, dataRole,
				parentOptions, null, null, null, null, null, false, false, false);
		final Collection<QuestionField> parentFields = Arrays.asList(parentField);
		final QuestionVariant parentVariant = createVariant("Variant", null, parentFields);
		final Collection<QuestionVariant> parentVariants = Arrays.asList(parentVariant);
		final Question parentQuestion = createQuestion("ParentQuestion", parentField, parentVariants);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionDependency questionDependency = createQuestionDependency(wizardContext, parentQuestion, parentOption1);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) questionDependency);
		final QuestionVariant variant = createVariant("ParentVariant", keys, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(parentQuestion, question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a required question field.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_required() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, true, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a security question field.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_security() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, true, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a user field dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_userFieldDependency() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, null, null, null,
				null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(wizardContext.getWizardResults()).andReturn(wizardResults).anyTimes();
		final String state = "ST";
		EasyMock.expect(wizardResults.getVotingRegionState()).andReturn(state).anyTimes();
		final UserFieldDependency userFieldDependency = createUserFieldDependency(wizardContext);
		final Collection<BasicDependency> keys = Arrays.asList((BasicDependency) userFieldDependency);
		final QuestionVariant variant = createVariant("Variant", keys, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#acquireMobileQuestions(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
	 * for a question field with a verification pattern.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Jul 10, 2012
	 */
	@Test
	public final void testAcquireMobileQuestions_verificationPattern() {
		final FlowType flowType = FlowType.RAVA;
		final WizardContext wizardContext = createMock("WizardContext", WizardContext.class);
		final long questionFieldId = 1l;
		final String title = "Title";
		final FieldType fieldType = createFieldType(FieldType.TEMPLATE_TEXT);
		final String verificationPattern = "{pattern}";
		final QuestionField field = createQuestionField(questionFieldId, title, fieldType, null, null, null, verificationPattern,
				null, null, null, false, false, false);
		final Collection<QuestionField> fields = Arrays.asList(field);
		final QuestionVariant variant = createVariant("Variant", (Collection<BasicDependency>) null, fields);
		final Collection<QuestionVariant> variants = Arrays.asList(variant);
		final Question question = createQuestion("Question", (QuestionField) null, variants);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage questionnairePage = createQuestionnairePage(QuestionnairePage.class, flowType, questions);
		final List<QuestionnairePage> questionnairePages = Arrays.asList(questionnairePage);
		wizardContext.setCurrentPage(questionnairePage);
		EasyMock.expect(wizardContext.getFlowType()).andReturn(flowType).anyTimes();
		EasyMock.expect(getPageDAO().findPages(flowType.getPageType())).andReturn(questionnairePages).anyTimes();
		replayAll();

		final List<MobilePage> actualMobilePages = getMobileService().acquireMobileQuestions(wizardContext);

		assertMobilePages(questionnairePages, actualMobilePages);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a checkbox answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_checkboxAnswer() {
		final MobileAnswer checkboxAnswer = createCheckboxAnswer(false);
		final List<MobileAnswer> answers = Arrays.asList(checkboxAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a confirmed text answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_confirmedTextAnswer() {
		final MobileAnswer confirmedTextAnswer = createConfirmedTextAnswer();
		final List<MobileAnswer> answers = Arrays.asList(confirmedTextAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the current address is set.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_currentAddress() {
		final MobileAddress currentAddress = createAddress("Current Address");
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, null, null, null,
				currentAddress, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History", "Ballot Preference",
				"Ethnicity", "Race", "M", "Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a date answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_dateAnswer() {
		final MobileAnswer dateAnswer = createDateAnswer();
		final List<MobileAnswer> answers = Arrays.asList(dateAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a filled checkbox answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_filledCheckboxAnswer() {
		final MobileAnswer filledCheckboxAnswer = createCheckboxAnswer(true);
		final List<MobileAnswer> answers = Arrays.asList(filledCheckboxAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the forwarding address is set.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_forwardingAddress() {
		final MobileAddress forwardingAddress = createAddress("Forwarding Address");
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, null, null, null, null, null,
				forwardingAddress, null, "Voting Region", "ST", "Voter Type", "Voting History", "Ballot Preference", "Ethnicity",
				"Race", "M", "Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the person's name is provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_name() {
		final MobilePerson name = createPerson("Name");
		final MobileResults results = createResults("email@somewhere.com", name, null, 04, 12, 2012, null, null, null, null, null,
				null, null, "Voting Region", "ST", "Voter Type", "Voting History", "Ballot Preference", "Ethnicity", "Race", "M",
				"Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the previous address is set.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_previousAddress() {
		final MobileAddress previousAddress = createAddress("Previous Address");
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, null, null, null, null, null,
				null, previousAddress, "Voting Region", "ST", "Voter Type", "Voting History", "Ballot Preference", "Ethnicity",
				"Race", "M", "Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the person's previous name is provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_previousName() {
		final MobilePerson previousName = createPerson("Previous");
		final MobileResults results = createResults("email@somewhere.com", null, previousName, 04, 12, 2012, null, null, null,
				null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History", "Ballot Preference", "Ethnicity",
				"Race", "M", "Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a radio answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_radioAnswer() {
		final MobileAnswer radioAnswer = createRadioAnswer();
		final List<MobileAnswer> answers = Arrays.asList(radioAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a select answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_selectAnswer() {
		final MobileAnswer selectAnswer = createSelectAnswer();
		final List<MobileAnswer> answers = Arrays.asList(selectAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the simple fields are filled in.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_simple() {
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a text answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_textAnswer() {
		final MobileAnswer textAnswer = createTextAnswer();
		final List<MobileAnswer> answers = Arrays.asList(textAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where there is a text area answer provided.
	 * 
	 * @author IanBrown
	 * @since Apr 13, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_textAreaAnswer() {
		final MobileAnswer textAreaAnswer = createTextAreaAnswer();
		final List<MobileAnswer> answers = Arrays.asList(textAreaAnswer);
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, "email@somewhere-else.com",
				"18001234567", "18667654321", null, null, null, null, "Voting Region", "ST", "Voter Type", "Voting History",
				"Ballot Preference", "Ethnicity", "Race", "M", "Party", "Mobile Test", answers, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#addResultsToWizardContext(com.bearcode.ovf.model.mobile.MobileResults, WizardContext)}
	 * for the case where the voting address is set.
	 * 
	 * @author IanBrown
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	@Test
	public final void testAddResultsToWizardContext_votingAddress() {
		final MobileAddress votingAddress = createAddress("Voting Address");
		final MobileResults results = createResults("email@somewhere.com", null, null, 04, 12, 2012, null, null, null, null,
				votingAddress, null, null, "Voting Region", "ST", "Voter Type", "Voting History", "Ballot Preference", "Ethnicity",
				"Race", "M", "Party", "Mobile Test", null, false);
		final WizardContext wizardContext = new WizardContext();
		replayAll();

		getMobileService().addResultsToWizardContext(results, wizardContext);

		assertResults(results, wizardContext);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#createWizardContext(com.bearcode.ovf.model.common.FaceConfig, com.bearcode.ovf.model.questionnaire.FlowType, java.lang.String, String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 10, 2012
	 * @version Apr 24, 2012
	 */
	@Test
	public final void testCreateWizardContext() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String urlPath = "face/vote";
		EasyMock.expect(faceConfig.getUrlPath()).andReturn(urlPath).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		final String state = "VT";
		final String votingRegion = "Voting Region";
		final State stateObject = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(state)).andReturn(stateObject).anyTimes();
		final VotingRegion region = createMock("VotingRegion", VotingRegion.class);
		EasyMock.expect(getVotingRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andReturn(region).anyTimes();
		replayAll();

		final WizardContext actualWizardContext = getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion);

		assertNotNull("A wizard context is returned", actualWizardContext);
		assertSame("The current face is set", faceConfig, actualWizardContext.getCurrentFace());
		assertSame("The flow type is set", flowType, actualWizardContext.getFlowType());
		final WizardResults actualWizardResults = actualWizardContext.getWizardResults();
		assertNotNull("There are wizard results", actualWizardResults);
		assertEquals("The face URL is set", urlPath, actualWizardResults.getFaceUrl());
		assertEquals("The voting region name is set", votingRegion, actualWizardResults.getVotingRegionName());
		assertEquals("The voting region state is set", state, actualWizardResults.getVotingRegionState());
		assertEquals("The voting region is set", region, actualWizardResults.getVotingRegion());
		final WizardResultAddress actualVotingAddress = actualWizardResults.getVotingAddress();
		assertNotNull("There is a voting address", actualVotingAddress);
		assertEquals("The voting address state is set", state, actualVotingAddress.getState());
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#createWizardContext(com.bearcode.ovf.model.common.FaceConfig, com.bearcode.ovf.model.questionnaire.FlowType, java.lang.String, String)}
	 * for the case where the state is not matched.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testCreateWizardContext_noState() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String urlPath = "face/vote";
		EasyMock.expect(faceConfig.getUrlPath()).andReturn(urlPath).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		final String state = "Unknown";
		final String votingRegion = "Voting Region";
		EasyMock.expect(getStateDAO().getByAbbreviation(state)).andReturn(null).anyTimes();
		replayAll();

		getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.service.MobileService#createWizardContext(com.bearcode.ovf.model.common.FaceConfig, com.bearcode.ovf.model.questionnaire.FlowType, java.lang.String, String)}
	 * for the case where the voting region is not matched.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jun 22, 2012
	 * @version Jun 22, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testCreateWizardContext_noVotingRegion() {
		final FaceConfig faceConfig = createMock("FaceConfig", FaceConfig.class);
		final String urlPath = "face/vote";
		EasyMock.expect(faceConfig.getUrlPath()).andReturn(urlPath).anyTimes();
		final FlowType flowType = FlowType.FWAB;
		final String state = "VT";
		final String votingRegion = "Voting Region";
		final State stateObject = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(state)).andReturn(stateObject).anyTimes();
		EasyMock.expect(getVotingRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andReturn(null).anyTimes();
		replayAll();

		getMobileService().createWizardContext(faceConfig, flowType, state, votingRegion);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MobileService#findSvidForState(String)}.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindSvidForState() {
		final String stateAbbreviation = "SA";
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(state);
		final StateSpecificDirectory svid = createMock("SVID", StateSpecificDirectory.class);
		EasyMock.expect(getLocalOfficialDAO().findSvidForState(state)).andReturn(svid);
		replayAll();

		final StateSpecificDirectory actualSvid = getMobileService().findSvidForState(stateAbbreviation);

		assertSame("The SVID is returned", svid, actualSvid);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.service.MobileService#findSvidForState(String)} for the case where there is no state
	 * for the abbreviation.
	 * 
	 * @author IanBrown
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	@Test
	public final void testFindSvidForState_noStateForAbbreviation() {
		final String stateAbbreviation = "SA";
		EasyMock.expect(getStateDAO().getByAbbreviation(stateAbbreviation)).andReturn(null);
		replayAll();

		final StateSpecificDirectory actualSvid = getMobileService().findSvidForState(stateAbbreviation);

		assertNull("No SVID is returned", actualSvid);
		verifyAll();
	}

	/**
	 * Adds a dependency to the question field.
	 * 
	 * @author IanBrown
	 * @param questionField
	 *            the question field.
	 * @param dependsOn
	 *            the question that this one depends on (may be <code>null</code>).
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private void addDependenciesToQuestionField(final QuestionField questionField, final Question dependsOn) {
		if (dependsOn == null) {
			EasyMock.expect(questionField.getFieldDependencies()).andReturn(null).anyTimes();

		} else {
			final FieldDependency fieldDependency = createMock("FieldDependency", FieldDependency.class);
			EasyMock.expect(fieldDependency.getDependsOn()).andReturn(dependsOn).anyTimes();
			final Collection<FieldDependency> fieldDependencies = Arrays.asList(fieldDependency);
			EasyMock.expect(questionField.getFieldDependencies()).andReturn(fieldDependencies).anyTimes();
		}
	}

	/**
	 * Custom assertion to ensure that the address is converted properly.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the address.
	 * @param sourceAddress
	 *            the source address.
	 * @param actualAddress
	 *            the actual address.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void assertAddress(final String name, final MobileAddress sourceAddress, final WizardResultAddress actualAddress) {
		assertNotNull("There is a " + name, actualAddress);
		assertEquals("The first street address for " + name + " is correct", sourceAddress.getStreet1(), actualAddress.getStreet1());
		assertEquals("The second street address for " + name + " is correct", sourceAddress.getStreet2(),
				actualAddress.getStreet2());
		assertEquals("The city for " + name + " is correct", sourceAddress.getCity(), actualAddress.getCity());
		assertEquals("The county for " + name + " is correct", sourceAddress.getCounty(), actualAddress.getCounty());
		assertEquals("The state for " + name + " is correct", sourceAddress.getState(), actualAddress.getState());
		assertEquals("The country for " + name + " is correct", sourceAddress.getCountry(), actualAddress.getCountry());
		assertEquals("The ZIP for " + name + " is correct", sourceAddress.getZip(), actualAddress.getZip());
		assertEquals("The ZIP+4 for " + name + " is correct", sourceAddress.getZip4(), actualAddress.getZip4());
		assertEquals("The type for " + name + " is correct", sourceAddress.getType(), actualAddress.getType());
		assertEquals("The description for " + name + " is correct", sourceAddress.getDescription(), actualAddress.getDescription());
	}

	/**
	 * Custom assertion to ensure that the answers were converted properly.
	 * 
	 * @author IanBrown
	 * @param answers
	 *            the answers provided by the mobile user.
	 * @param actualAnswers
	 *            the actual answers.
	 * @since Apr 12, 2012
	 * @version Apr 13, 2012
	 */
	private void assertAnswers(final List<MobileAnswer> answers, final Map<Long, Answer> actualAnswers) {
		if (answers == null) {
			assertTrue("There are no answers", actualAnswers.isEmpty());

		} else {
			for (final MobileAnswer answer : answers) {
				final Answer actualAnswer = actualAnswers.get(answer.getQuestionFieldId());
				assertNotNull("There is an actual answer", actualAnswer);
				final QuestionField field = getQuestionFieldDAO().getById(answer.getQuestionFieldId());
				final FieldType type = field.getType();
				final String templateName = type.getTemplateName();
				if (FieldType.TEMPLATE_TEXT.equals(templateName) || FieldType.TEMPLATE_TEXTAREA.equals(templateName)
						|| FieldType.TEMPLATE_CHECKBOX.equals(templateName)
						|| FieldType.TEMPLATE_CHECKBOX_FILLED.equals(templateName)
						|| FieldType.TEMPLATE_TEXT_CONFIRM.equals(templateName)) {
					assertEnteredAnswer(answer, (EnteredAnswer) actualAnswer);

				} else if (FieldType.TEMPLATE_DATE.equals(templateName)) {
					assertEnteredDateAnswer(answer, (EnteredDateAnswer) actualAnswer);

				} else if (FieldType.TEMPLATE_SELECT.equals(templateName) || FieldType.TEMPLATE_RADIO.equals(templateName)) {
					assertPredefinedAnswer(answer, (PredefinedAnswer) actualAnswer);

				} else {
					throw new UnsupportedOperationException("No handling for " + templateName);
				}
			}
		}
	}

	/**
	 * Custom assertion to ensure that the entered answer matches the mobile answer.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the mobile answer.
	 * @param actualAnswer
	 *            the actual entered answer.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void assertEnteredAnswer(final MobileAnswer answer, final EnteredAnswer actualAnswer) {
		assertEquals("The value is correct", answer.getOption().getValue(), actualAnswer.getEnteredValue());
	}

	/**
	 * Custom assertion to ensure that the entered date answer matches the mobile answer.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the mobile answer.
	 * @param actualAnswer
	 *            the actual entered date answer.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void assertEnteredDateAnswer(final MobileAnswer answer, final EnteredDateAnswer actualAnswer) {
		assertEquals("The value is correct", answer.getOption().getValue(), actualAnswer.getDate(DATE_FORMAT));
	}

	/**
	 * Custom assertion to ensure tha the dependencies were properly converted to mobile dependencies.
	 * 
	 * @author IanBrown
	 * @param dependencies
	 *            the dependencies.
	 * @param mobileDependencies
	 *            the mobile dependencies.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileDependencies(final Collection<BasicDependency> dependencies,
			final List<MobileDependency> mobileDependencies) {
		if (dependencies == null || dependencies.isEmpty()) {
			assertNull("There are no mobile dependencies", mobileDependencies);
			return;
		}

		final Iterator<MobileDependency> mobileDependencyItr = mobileDependencies == null ? null : mobileDependencies.iterator();
		for (final BasicDependency dependency : dependencies) {
			if (dependency instanceof QuestionDependency) {
				assertNotNull("There should be mobile dependencies", mobileDependencyItr);
				assertTrue("There is a mobile dependency", mobileDependencyItr.hasNext());
				final MobileDependency mobileDependency = mobileDependencyItr.next();
				assertMobileDependency((QuestionDependency) dependency, mobileDependency);
			} else if (dependency instanceof QuestionCheckboxDependency) {
				org.junit.Assert.fail("Question checkbox dependencies are not yet implemented!");
			}
		}
	}

	/**
	 * Custom assertion to ensure that the dependency was properly converted to a mobile dependency.
	 * 
	 * @author IanBrown
	 * @param dependency
	 *            the dependency.
	 * @param mobileDependency
	 *            the mobile dependency.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileDependency(final QuestionDependency dependency, final MobileDependency mobileDependency) {
		assertEquals("The dependency is on the correct question", (Long) dependency.getDependsOn().getKeyField().getId(),
				mobileDependency.getDependsOn());
		assertEquals("The condition is correct", (Long) dependency.getCondition().getId(), mobileDependency.getCondition());
	}

	/**
	 * Custo assertion to ensure that the question was converted to the mobile group properly.
	 * 
	 * @author IanBrown
	 * @param question
	 *            the question.
	 * @param mobileGroup
	 *            the mobile group.
	 * @since Apr 12, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileGroup(final Question question, final MobileGroup mobileGroup) {
		assertEquals("The title is correct", question.getTitle(), mobileGroup.getTitle());
		assertMobileVariants(question.getVariants(), mobileGroup.getChildren());
	}

	/**
	 * Custom assertion to ensure that the questions were properly converted to the mobile groups.
	 * 
	 * @author IanBrown
	 * @param questions
	 *            the questions.
	 * @param mobileGroups
	 *            the mobile groups.
	 * @since Apr 12, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileGroups(final List<Question> questions, final List<MobileGroup> mobileGroups) {
		assertNotNull("There are mobile groups", mobileGroups);
		assertEquals("There are the correct number of mobile groups", questions.size(), mobileGroups.size());
		final Iterator<Question> questionItr = questions.iterator();
		final Iterator<MobileGroup> mobileGroupItr = mobileGroups.iterator();
		while (questionItr.hasNext()) {
			final Question question = questionItr.next();
			final MobileGroup mobileGroup = mobileGroupItr.next();
			assertMobileGroup(question, mobileGroup);
		}
	}

	/**
	 * Custom assertion to ensure that the option was properly converted to a mobile option.
	 * 
	 * @author IanBrown
	 * @param option
	 *            the option.
	 * @param mobileOption
	 *            the mobile option.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void assertMobileOption(final FieldDictionaryItem option, final MobileOption mobileOption) {
		assertEquals("The option identifier is correct", option.getId(), mobileOption.getId().longValue());
		assertEquals("The option value is correct", option.getViewValue(), mobileOption.getValue());
	}

	/**
	 * Custom assertion to ensure that the options were correctly converted to mobile options.
	 * 
	 * @author IanBrown
	 * @param options
	 *            the options.
	 * @param mobileOptions
	 *            the mobile options.
	 * @since Apr 11, 2012
	 * @version Apr 12, 2012
	 */
	private void assertMobileOptions(final Collection<FieldDictionaryItem> options, final List<MobileOption> mobileOptions) {
		if (options == null || options.isEmpty()) {
			assertNull("There are no answers", mobileOptions);

		} else {
			assertNotNull("There are answers", mobileOptions);

			final Iterator<FieldDictionaryItem> optionItr = options.iterator();
			final Iterator<MobileOption> mobileOptionItr = mobileOptions.iterator();
			while (optionItr.hasNext() && mobileOptionItr.hasNext()) {
				final FieldDictionaryItem option = optionItr.next();
				final MobileOption mobileOption = mobileOptionItr.next();
				assertMobileOption(option, mobileOption);
			}

			assertEquals("All of the options were converted", optionItr.hasNext(), mobileOptionItr.hasNext());
		}
	}

	/**
	 * Custom assertion to ensure that the questionnaire page is properly converted to the mobile page.
	 * 
	 * @author IanBrown
	 * @param questionnairePage
	 *            the questionnaire page.
	 * @param mobilePage
	 *            the corresponding mobile page.
	 * @since Apr 30, 2012
	 * @version May 3, 2012
	 */
	private void assertMobilePage(final QuestionnairePage questionnairePage, final MobilePage mobilePage) {
		assertEquals("The title of the page is correct", questionnairePage.getTitle(), mobilePage.getTitle());
		assertMobileGroups(questionnairePage.getQuestions(), mobilePage.getChildren());
	}

	/**
	 * Custom assertion to ensure that the mobile pages are properly created from the questionnaire pages.
	 * 
	 * @author IanBrown
	 * @param questionnairePages
	 *            the questionnaire pages.
	 * @param mobilePages
	 *            the mobile pages.
	 * @since Apr 30, 2012
	 * @version May 3, 2012
	 */
	private void assertMobilePages(final List<QuestionnairePage> questionnairePages, final List<MobilePage> mobilePages) {
		assertNotNull("There is a list of mobile pages", mobilePages);
		assertEquals("There are the correct number of mobile pages", questionnairePages.size(), mobilePages.size());
		final Iterator<QuestionnairePage> questionnairePageItr = questionnairePages.iterator();
		final Iterator<MobilePage> mobilePageItr = mobilePages.iterator();

		while (questionnairePageItr.hasNext()) {
			final QuestionnairePage questionnairePage = questionnairePageItr.next();
			final MobilePage mobilePage = mobilePageItr.next();
			assertMobilePage(questionnairePage, mobilePage);
		}
	}

	/**
	 * Custom assertion to ensure that the field was converted to a mobile question properly.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the field.
	 * @param mobileQuestion
	 *            the mobile question.
	 * @since Apr 12, 2012
	 * @version Sep 27, 2012
	 */
	private void assertMobileQuestion(final QuestionField field, final MobileQuestion mobileQuestion) {
		assertEquals("The identifier is correct", field.getId(), mobileQuestion.getQuestionFieldId());
		assertEquals("The title is correct", field.getTitle(), mobileQuestion.getTitle());
		assertEquals("The type is correct", field.getType().getTemplateName(), mobileQuestion.getFieldType());
		assertEquals("The data role is correct", field.getDataRole(), mobileQuestion.getDataRole());
		assertEquals("The verification pattern is correct", field.getVerificationPattern(), mobileQuestion.getVerificationPattern());
		assertMobileOptions(field.getOptions(), mobileQuestion.getOptions());
		assertEquals("The help text is correct", field.getHelpText(), mobileQuestion.getHelpText());
		assertEquals("The additional help is correct", field.getAdditionalHelp(), mobileQuestion.getAdditionalHelp());
		assertEquals("The required flag is correct", field.isRequired(), mobileQuestion.isRequired());
		assertEquals("The security flag is correct", field.isSecurity(), mobileQuestion.isSecurity());
		assertEquals("The encoded flag is correct", field.isEncoded(), mobileQuestion.isEncoded());
	}

	/**
	 * Custom assertion to ensure that the fields were correctly converted to mobile questions.
	 * 
	 * @author IanBrown
	 * @param fields
	 *            the fields.
	 * @param mobileQuestions
	 *            the mobile questions.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileQuestions(final Collection<QuestionField> fields, final List<MobileQuestion> mobileQuestions) {
		assertNotNull("There are mobile questions", mobileQuestions);
		assertEquals("There are the correct number of mobile questions", fields.size(), mobileQuestions.size());
		final Iterator<QuestionField> fieldItr = fields.iterator();
		final Iterator<MobileQuestion> mobileQuestionItr = mobileQuestions.iterator();

		while (fieldItr.hasNext()) {
			final QuestionField field = fieldItr.next();
			final MobileQuestion mobileQuestion = mobileQuestionItr.next();
			assertMobileQuestion(field, mobileQuestion);
		}
	}

	/**
	 * Custom assertion to ensure that the variant was properly converted to a mobile variant.
	 * 
	 * @author IanBrown
	 * @param variant
	 *            the variant.
	 * @param mobileVariant
	 *            the mobile variant.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileVariant(final QuestionVariant variant, final MobileVariant mobileVariant) {
		assertMobileDependencies(variant.getKeys(), mobileVariant.getDependencies());
		assertMobileQuestions(variant.getFields(), mobileVariant.getChildren());
	}

	/**
	 * Custom assertion to ensure that the variants were properly converted to mobile variants.
	 * 
	 * @author IanBrown
	 * @param variants
	 *            the variants.
	 * @param mobileVariants
	 *            the mobile variants.
	 * @since May 3, 2012
	 * @version May 3, 2012
	 */
	private void assertMobileVariants(final Collection<QuestionVariant> variants, final List<MobileVariant> mobileVariants) {
		assertNotNull("There are mobile variants", mobileVariants);
		assertEquals("There are the correct number of mobile variants", variants.size(), mobileVariants.size());
		final Iterator<QuestionVariant> variantItr = variants.iterator();
		final Iterator<MobileVariant> mobileVariantItr = mobileVariants.iterator();
		while (variantItr.hasNext()) {
			final QuestionVariant variant = variantItr.next();
			final MobileVariant mobileVariant = mobileVariantItr.next();
			assertMobileVariant(variant, mobileVariant);
		}
	}

	/**
	 * Custom assertion to ensure that the person record is copied correctly.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the person object.
	 * @param sourcePerson
	 *            the source person object.
	 * @param actualPerson
	 *            the actual person object.
	 * @since Apr 11, 2012
	 * @version Apr 11, 2012
	 */
	private void assertPerson(final String name, final MobilePerson sourcePerson, final WizardResultPerson actualPerson) {
		assertNotNull("There is a " + name, actualPerson);
		assertEquals("The title is correct", sourcePerson.getTitle(), actualPerson.getTitle());
		assertEquals("The first name is correct", sourcePerson.getFirstName(), actualPerson.getFirstName());
		assertEquals("The initial is correct", sourcePerson.getInitial(), actualPerson.getInitial());
		assertEquals("The last name is correct", sourcePerson.getLastName(), actualPerson.getLastName());
		assertEquals("The suffix is correct", sourcePerson.getSuffix(), actualPerson.getSuffix());
	}

	/**
	 * Custom assertion to ensure that the predefined answer matches the mobile answer.
	 * 
	 * @author IanBrown
	 * @param answer
	 *            the mobile answer.
	 * @param actualAnswer
	 *            the predefined answer.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void assertPredefinedAnswer(final MobileAnswer answer, final PredefinedAnswer actualAnswer) {
		assertEquals("The value is correct", answer.getOption().getValue(), actualAnswer.getSelectedValue().getViewValue());
	}

	/**
	 * Custom assertion to ensure that the results were properly converted.
	 * 
	 * @author IanBrown
	 * @param results
	 *            the results from the user.
	 * @param wizardContext
	 *            the wizard context that was built.
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	private void assertResults(final MobileResults results, final WizardContext wizardContext) {
		final WizardResults wizardResults = wizardContext.getWizardResults();
		assertEquals("The username is correct", results.getEmailAddress(), wizardResults.getUsername());
		assertPerson("name", expectedPerson(results.getName()), wizardResults.getName());
		assertPerson("previousName", expectedPerson(results.getPreviousName()), wizardResults.getPreviousName());
		assertEquals("The birth month is correct", results.getBirthMonth(), wizardResults.getBirthMonth());
		assertEquals("The birth date is correct", results.getBirthDay(), wizardResults.getBirthDate());
		assertEquals("The birth year is correct", results.getBirthYear(), wizardResults.getBirthYear());
		assertEquals("The alternate email is correct", expectedString(results.getAlternateEmail()),
				wizardResults.getAlternateEmail());
		assertEquals("The phone is correct", expectedString(results.getPhone()), wizardResults.getPhone());
		assertEquals("The alternate phone is correct", expectedString(results.getAlternatePhone()),
				wizardResults.getAlternatePhone());
		assertAddress("currentAddress", expectedAddress(AddressType.OVERSEAS, results.getCurrentAddress()),
				wizardResults.getCurrentAddress());
		assertAddress("votingAddress", expectedAddress(AddressType.STREET, results.getVotingAddress()),
				wizardResults.getVotingAddress());
		assertAddress("forwardingAddress", expectedAddress(AddressType.OVERSEAS, results.getForwardingAddress()),
				wizardResults.getForwardingAddress());
		assertAddress("previousAddress", expectedAddress(AddressType.STREET, results.getPreviousAddress()),
				wizardResults.getPreviousAddress());
		final String votingRegionName = results.getVotingRegionName();
		assertEquals("The voting region name is correct", votingRegionName, wizardResults.getVotingRegionName());
		final String votingRegionState = results.getVotingRegionState();
		assertEquals("The voting region state is correct", votingRegionState, wizardResults.getVotingRegionState());
		final VotingRegion votingRegion = wizardResults.getVotingRegion();
		assertNotNull("There is a voting region", votingRegion);
		assertEquals("The voting region's name is correct", votingRegionName, votingRegion.getName());
		final State state = votingRegion.getState();
		assertNotNull("The voting region has a state", state);
		assertEquals("The voting region's state name is correct", votingRegionState, state.getName());
		assertEquals("The voter type is correct", expectedString(results.getVoterType()), wizardResults.getVoterType());
		assertEquals("The voter history is correct", expectedString(results.getVoterHistory()), wizardResults.getVoterHistory());
		assertEquals("The ballot preference is correct", expectedString(results.getBallotPreference()),
				wizardResults.getBallotPref());
		assertEquals("The ethnicity is correct", expectedString(results.getEthnicity()), wizardResults.getEthnicity());
		assertEquals("The race is correct", expectedString(results.getRace()), wizardResults.getRace());
		assertEquals("The gender is correct", expectedString(results.getGender()), wizardResults.getGender());
		assertEquals("The party is correct", expectedString(results.getParty()), wizardResults.getParty());
		assertTrue("The mobile application flag is set", wizardResults.isMobile());
		assertEquals("The mobile device type is correct", expectedString(results.getMobileDeviceType()),
				wizardResults.getMobileDeviceType());
		assertAnswers(results.getAnswers(), wizardResults.getAnswersAsMap());
		assertEquals("The downloaded flag is correct", results.isDownloaded(), wizardResults.isDownloaded());
	}

	/**
	 * Creates an address for the specific name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the address.
	 * @return the address.
	 * @since Apr 12, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAddress createAddress(final String name) {
		final MobileAddress address = createMock(name.replace(' ', '_'), MobileAddress.class);
		EasyMock.expect(address.getTypeName()).andReturn(AddressType.STREET.name()).anyTimes();
		EasyMock.expect(address.getStreet1()).andReturn(name + " Street").atLeastOnce();
		EasyMock.expect(address.getStreet2()).andReturn("Unit " + name).atLeastOnce();
		EasyMock.expect(address.getCity()).andReturn(name + " City").atLeastOnce();
		EasyMock.expect(address.getCounty()).andReturn(name + " County").atLeastOnce();
		EasyMock.expect(address.getState()).andReturn("ST").atLeastOnce();
		EasyMock.expect(address.getCountry()).andReturn("Country of " + name).atLeastOnce();
		EasyMock.expect(address.getZip()).andReturn("12357").atLeastOnce();
		EasyMock.expect(address.getZip4()).andReturn("12357-4689").atLeastOnce();
		EasyMock.expect(address.getType()).andReturn(AddressType.values()[Math.abs(name.hashCode()) % AddressType.values().length])
				.atLeastOnce();
		EasyMock.expect(address.getDescription()).andReturn("Description of " + name).atLeastOnce();
		return address;
	}

	/**
	 * Creates an answer for the specified question with the provided values.
	 * 
	 * @author IanBrown
	 * @param questionFieldId
	 *            the identifier for the question field.
	 * @param fieldType
	 *            the type of question field.
	 * @param optionId
	 *            the option identifier for the answer.
	 * @param text
	 *            the text for the answer.
	 * @return the answer.
	 * @since Apr 13, 2012
	 * @version Jul 11, 2012
	 */
	private MobileAnswer createAnswer(final long questionFieldId, final String fieldType, final Long optionId, final String text) {
		final MobileAnswer answer = createMock(fieldType + "Answer", MobileAnswer.class);
		final MobileOption option = createMock(fieldType + "Option", MobileOption.class);
		EasyMock.expect(answer.getOption()).andReturn(option).atLeastOnce();
		EasyMock.expect(option.getItem()).andReturn(null).anyTimes();
		EasyMock.expect(option.getId()).andReturn(optionId).anyTimes();
		EasyMock.expect(option.getValue()).andReturn(text).atLeastOnce();
		EasyMock.expect(answer.getQuestionField()).andReturn(null).atLeastOnce();
		EasyMock.expect(answer.getQuestionFieldId()).andReturn(questionFieldId).atLeastOnce();
		final QuestionField field = createMock(fieldType + "QuestionField", QuestionField.class);
		EasyMock.expect(getQuestionFieldDAO().getById(questionFieldId)).andReturn(field).atLeastOnce();
		final FieldType type = createMock(fieldType + "Type", FieldType.class);
		EasyMock.expect(field.getType()).andReturn(type).atLeastOnce();
		EasyMock.expect(type.getTemplateName()).andReturn(fieldType).atLeastOnce();
		final Collection<FieldDictionaryItem> fieldOptions;
		if (optionId == null) {
			fieldOptions = null;
		} else {
			final FieldDictionaryItem fieldOption = createMock("FieldOption", FieldDictionaryItem.class);
			fieldOptions = Arrays.asList(fieldOption);
			EasyMock.expect(fieldOption.getId()).andReturn(optionId).anyTimes();
			EasyMock.expect(fieldOption.getViewValue()).andReturn(text).anyTimes();
		}
		EasyMock.expect(field.getOptions()).andReturn(fieldOptions).anyTimes();
		return answer;
	}

	/**
	 * Creates a checkbox answer.
	 * 
	 * @author IanBrown
	 * @param filled
	 *            <code>true</code> if the checkbox is filled by default, <code>false</code> if not.
	 * @return the checkbox answer.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAnswer createCheckboxAnswer(final boolean filled) {
		long id;
		String fieldType;
		String text;
		if (filled) {
			id = 7l;
			fieldType = FieldType.TEMPLATE_CHECKBOX_FILLED;
			text = "false";
		} else {
			id = 8l;
			fieldType = FieldType.TEMPLATE_CHECKBOX;
			text = "true";
		}
		return createAnswer(id, fieldType, null, text);
	}

	/**
	 * Creates a confirmed text answer.
	 * 
	 * @author IanBrown
	 * @return the confirmed text answer.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAnswer createConfirmedTextAnswer() {
		return createAnswer(10l, FieldType.TEMPLATE_TEXT_CONFIRM, null, "Text Confirm");
	}

	/**
	 * Creates a simple date answer.
	 * 
	 * @author IanBrown
	 * @return the text answer.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private MobileAnswer createDateAnswer() {
		final Date date = new Date();
		final String text = DATE_FORMAT.format(date);
		return createAnswer(2l, FieldType.TEMPLATE_DATE, null, text);
	}

	/**
	 * Creates a face dependency.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @return the face dependency.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private FaceDependency createFaceDependency(final WizardContext wizardContext) {
		final FaceDependency faceDependency = createMock("FaceDependency", FaceDependency.class);
		EasyMock.expect(faceDependency.checkGroup(EasyMock.anyObject())).andReturn(true).anyTimes();
		EasyMock.expect(faceDependency.checkDependency(wizardContext)).andReturn(true).anyTimes();
		return faceDependency;
	}

	/**
	 * Creates a field type.
	 * 
	 * @author IanBrown
	 * @param templateName
	 *            the name of the template.
	 * @return the field type.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private FieldType createFieldType(final String templateName) {
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(fieldType.getTemplateName()).andReturn(templateName).anyTimes();
		return fieldType;
	}

	/**
	 * Creates a flow dependency.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @return the flow dependency.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private FlowDependency createFlowDependency(final WizardContext wizardContext) {
		final FlowDependency flowDependency = createMock("FlowDependency", FlowDependency.class);
		EasyMock.expect(flowDependency.checkGroup(EasyMock.anyObject())).andReturn(true).anyTimes();
		EasyMock.expect(flowDependency.checkDependency(wizardContext)).andReturn(true).anyTimes();
		return flowDependency;
	}

	/**
	 * Creates an option with the specified view value.
	 * 
	 * @author IanBrown
	 * @param viewValue
	 *            the value shown in the view.
	 * @return the view value.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private FieldDictionaryItem createOption(final String viewValue) {
		final FieldDictionaryItem option = createMock(viewValue.replace(' ', '_'), FieldDictionaryItem.class);
		EasyMock.expect(option.getId()).andReturn((long) viewValue.hashCode()).anyTimes();
		EasyMock.expect(option.getViewValue()).andReturn(viewValue).anyTimes();
		return option;
	}

	/**
	 * Creates a person object based on the input name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the person object.
	 * @return the person.
	 * @since Apr 11, 2012
	 * @version Apr 12, 2012
	 */
	private MobilePerson createPerson(final String name) {
		final MobilePerson person = createMock(name.replace(' ', '_'), MobilePerson.class);
		EasyMock.expect(person.getTitle()).andReturn("Title" + name).atLeastOnce();
		EasyMock.expect(person.getFirstName()).andReturn("First" + name).atLeastOnce();
		EasyMock.expect(person.getInitial()).andReturn(Character.toString(name.charAt(0))).atLeastOnce();
		EasyMock.expect(person.getLastName()).andReturn("Last" + name).atLeastOnce();
		EasyMock.expect(person.getSuffix()).andReturn("Suffix" + name).atLeastOnce();
		return person;
	}

	/**
	 * Creates a question with the specified key field and variants.
	 * 
	 * @author IanBrown
	 * @param questionTitle
	 *            the question title.
	 * @param keyField
	 *            the key field (may be <code>null</code>).
	 * @param variants
	 *            the variants.
	 * @return the question.
	 * @since Apr 10, 2012
	 * @version Apr 11, 2012
	 */
	private Question createQuestion(final String questionTitle, final QuestionField keyField,
			final Collection<QuestionVariant> variants) {
		final Question question = createMock(questionTitle.replace(' ', '_'), Question.class);
		EasyMock.expect(question.getTitle()).andReturn(questionTitle).anyTimes();
		EasyMock.expect(question.getKeyField()).andReturn(keyField).anyTimes();
		EasyMock.expect(question.getVariants()).andReturn(variants).anyTimes();
		if (variants != null) {
			for (final QuestionVariant variant : variants) {
				EasyMock.expect(variant.getQuestion()).andReturn(question).anyTimes();
			}
		}
		return question;
	}

	/**
	 * Creates a question checkbox dependency.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param dependsOn
	 *            the question that is depended on.
	 * @param condition
	 *            the condition.
	 * @return the question dependency.
	 * @since Aug 15, 2012
	 * @version Aug 15, 2012
	 */
	private QuestionCheckboxDependency createQuestionCheckboxDependency(final WizardContext wizardContext,
			final Question dependsOn, final boolean condition) {
		final QuestionCheckboxDependency questionCheckboxDependency = createMock("QuestionCheckboxDependency",
				QuestionCheckboxDependency.class);
		EasyMock.expect(questionCheckboxDependency.checkGroup(EasyMock.anyObject())).andReturn(true).anyTimes();
		EasyMock.expect(questionCheckboxDependency.getDependsOn()).andReturn(dependsOn).anyTimes();
		EasyMock.expect(questionCheckboxDependency.getFieldValue()).andReturn(Boolean.toString(condition)).anyTimes();
		return questionCheckboxDependency;
	}

	/**
	 * Creates a question dependency.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @param dependsOn
	 *            the question that is depended on.
	 * @param condition
	 *            the condition.
	 * @return the question dependency.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private QuestionDependency createQuestionDependency(final WizardContext wizardContext, final Question dependsOn,
			final FieldDictionaryItem condition) {
		final QuestionDependency questionDependency = createMock("QuestionDependency", QuestionDependency.class);
		EasyMock.expect(questionDependency.checkGroup(EasyMock.anyObject())).andReturn(true).anyTimes();
		EasyMock.expect(questionDependency.getDependsOn()).andReturn(dependsOn).anyTimes();
		EasyMock.expect(questionDependency.getCondition()).andReturn(condition).anyTimes();
		return questionDependency;
	}

	/**
	 * Creates a mock question field.
	 * 
	 * @author IanBrown
	 * @param questionFieldId
	 *            the question field identifier.
	 * @param title
	 *            the title.
	 * @param fieldType
	 *            the type of field.
	 * @param dataRole TODO
	 * @param options
	 *            the options for the field.
	 * @param dependsOn
	 *            the question that this one depends on (may be <code>null</code>).
	 * @param verificationPattern
	 *            the verification pattern.
	 * @param helpTextAnswer
	 *            the dynamic answer for the help text.
	 * @param helpText
	 *            the help text.
	 * @param additionalHelp
	 *            the additional help text.
	 * @param required
	 *            is this a required field?
	 * @param security
	 *            is this a security field?
	 * @param encoded
	 *            should the answer be encoded?
	 * @return the question field.
	 * @since Apr 10, 2012
	 * @version Sep 27, 2012
	 */
	private QuestionField createQuestionField(final long questionFieldId, final String title, final FieldType fieldType,
			String dataRole, final Collection<FieldDictionaryItem> options, final Question dependsOn,
			final String verificationPattern, final IAnswer<? extends String> helpTextAnswer, final String helpText,
			final String additionalHelp, final boolean required, final boolean security, final boolean encoded) {
		final QuestionField questionField = createMock(title.replace(' ', '_'), QuestionField.class);
		EasyMock.expect(questionField.getId()).andReturn(questionFieldId).anyTimes();
		EasyMock.expect(questionField.getTitle()).andReturn(title).anyTimes();
		EasyMock.expect(questionField.getType()).andReturn(fieldType).anyTimes();
		EasyMock.expect(questionField.getDataRole()).andReturn(dataRole).anyTimes();
		EasyMock.expect(questionField.getOptions()).andReturn(options).anyTimes();
		addDependenciesToQuestionField(questionField, dependsOn);
		EasyMock.expect(questionField.getVerificationPattern()).andReturn(verificationPattern).anyTimes();
		if (helpTextAnswer == null) {
			EasyMock.expect(questionField.getHelpText()).andReturn(helpText).anyTimes();
		} else {
			EasyMock.expect(questionField.getHelpText()).andAnswer(helpTextAnswer).anyTimes();
		}
		EasyMock.expect(questionField.getAdditionalHelp()).andReturn(additionalHelp).anyTimes();
		EasyMock.expect(questionField.isRequired()).andReturn(required).anyTimes();
		EasyMock.expect(questionField.isSecurity()).andReturn(security).anyTimes();
		EasyMock.expect(questionField.isEncoded()).andReturn(encoded).anyTimes();
		return questionField;
	}

	/**
	 * Creates a questionnaire page for the flow with the specified questions.
	 * 
	 * @author IanBrown
	 * @param <P>
	 *            the type of questionnaire page.
	 * @param pageClass
	 *            the class of questionnaire page.
	 * @param flowType
	 *            the type of flow.
	 * @param questions
	 *            the questions.
	 * @return the questionnaire page.
	 * @since Apr 10, 2012
	 * @version Apr 30, 2012
	 */
	private <P extends QuestionnairePage> P createQuestionnairePage(final Class<P> pageClass, final FlowType flowType,
			final List<Question> questions) {
		final P questionnairePage = createMock("QuestionnairePage", pageClass);
		EasyMock.expect(questionnairePage.getTitle()).andReturn(pageClass.getSimpleName()).anyTimes();
		EasyMock.expect(questionnairePage.getType()).andReturn(flowType.getPageType()).anyTimes();
		EasyMock.expect(questionnairePage.getQuestions()).andReturn(questions).anyTimes();
		if (questions != null) {
			for (final Question question : questions) {
				EasyMock.expect(question.getPage()).andReturn(questionnairePage).anyTimes();
			}
		}
		return questionnairePage;
	}

	/**
	 * Creates a radio answer.
	 * 
	 * @author IanBrown
	 * @return the radio answer.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAnswer createRadioAnswer() {
		return createAnswer(8l, FieldType.TEMPLATE_RADIO, 9l, "Radio");
	}

	/**
	 * Creates mobile results with the specified values.
	 * 
	 * @author IanBrown
	 * @param emailAddress
	 *            the email address of the user.
	 * @param name
	 *            the name of the person.
	 * @param previousName
	 *            the previous name of the person.
	 * @param birthMonth
	 *            the birth month of the person.
	 * @param birthDay
	 *            the birth day of the person.
	 * @param birthYear
	 *            the birth year of the person.
	 * @param alternateEmail
	 *            the alternate email of the person.
	 * @param phone
	 *            the phone of the person.
	 * @param alternatePhone
	 *            the alternate phone of the person.
	 * @param currentAddress
	 *            the current address.
	 * @param votingAddress
	 *            the voting address.
	 * @param forwardingAddress
	 *            the forwarding address.
	 * @param previousAddress
	 *            the previous address.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param votingRegionState
	 *            the state for the voting region.
	 * @param voterType
	 *            the type of voter.
	 * @param voterHistory
	 *            the voting history of the person.
	 * @param ballotPreference
	 *            the ballot preference of the person.
	 * @param ethnicity
	 *            the ethnicity of the person.
	 * @param race
	 *            the race of the person.
	 * @param gender
	 *            the gender of the person.
	 * @param party
	 *            the party of the person.
	 * @param mobileDeviceType
	 *            the type of mobile device.
	 * @param answers
	 *            the answers provided by the user.
	 * @param downloaded
	 *            the downloaded flag.
	 * @return the results.
	 * @since Apr 11, 2012
	 * @version Apr 20, 2012
	 */
	private MobileResults createResults(final String emailAddress, final MobilePerson name, final MobilePerson previousName,
			final int birthMonth, final int birthDay, final int birthYear, final String alternateEmail, final String phone,
			final String alternatePhone, final MobileAddress currentAddress, final MobileAddress votingAddress,
			final MobileAddress forwardingAddress, final MobileAddress previousAddress, final String votingRegionName,
			final String votingRegionState, final String voterType, final String voterHistory, final String ballotPreference,
			final String ethnicity, final String race, final String gender, final String party, final String mobileDeviceType,
			final List<MobileAnswer> answers, final boolean downloaded) {
		final MobileResults results = createMock("Results", MobileResults.class);
		EasyMock.expect(results.getEmailAddress()).andReturn(emailAddress).atLeastOnce();
		EasyMock.expect(results.getName()).andReturn(name).atLeastOnce();
		EasyMock.expect(results.getPreviousName()).andReturn(previousName).atLeastOnce();
		EasyMock.expect(results.getBirthMonth()).andReturn(birthMonth).atLeastOnce();
		EasyMock.expect(results.getBirthDay()).andReturn(birthDay).atLeastOnce();
		EasyMock.expect(results.getBirthYear()).andReturn(birthYear).atLeastOnce();
		EasyMock.expect(results.getAlternateEmail()).andReturn(alternateEmail).atLeastOnce();
		EasyMock.expect(results.getPhone()).andReturn(phone).atLeastOnce();
		EasyMock.expect(results.getAlternatePhone()).andReturn(alternatePhone).atLeastOnce();
		EasyMock.expect(results.getCurrentAddress()).andReturn(currentAddress).atLeastOnce();
		EasyMock.expect(results.getVotingAddress()).andReturn(votingAddress).atLeastOnce();
		EasyMock.expect(results.getForwardingAddress()).andReturn(forwardingAddress).atLeastOnce();
		EasyMock.expect(results.getPreviousAddress()).andReturn(previousAddress).atLeastOnce();
		EasyMock.expect(results.getVotingRegionName()).andReturn(votingRegionName).atLeastOnce();
		EasyMock.expect(results.getVotingRegionState()).andReturn(votingRegionState).atLeastOnce();
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateDAO().getByAbbreviation(votingRegionState)).andReturn(state).anyTimes();
		final VotingRegion region = createMock("Region", VotingRegion.class);
		EasyMock.expect(region.getState()).andReturn(state).anyTimes();
		EasyMock.expect(state.getName()).andReturn(votingRegionState).atLeastOnce();
		EasyMock.expect(region.getName()).andReturn(votingRegionName).atLeastOnce();
		EasyMock.expect(getVotingRegionDAO().getRegionByName((VotingRegion) EasyMock.anyObject())).andReturn(region).anyTimes();
		EasyMock.expect(results.getVoterType()).andReturn(voterType).atLeastOnce();
		EasyMock.expect(results.getVoterHistory()).andReturn(voterHistory).atLeastOnce();
		EasyMock.expect(results.getBallotPreference()).andReturn(ballotPreference).atLeastOnce();
		EasyMock.expect(results.getEthnicity()).andReturn(ethnicity).atLeastOnce();
		EasyMock.expect(results.getRace()).andReturn(race).atLeastOnce();
		EasyMock.expect(results.getGender()).andReturn(gender).atLeastOnce();
		EasyMock.expect(results.getParty()).andReturn(party).atLeastOnce();
		EasyMock.expect(results.getMobileDeviceType()).andReturn(mobileDeviceType).atLeastOnce();
		EasyMock.expect(results.getAnswers()).andReturn(answers).atLeastOnce();
		EasyMock.expect(results.isDownloaded()).andReturn(downloaded).atLeastOnce();
		return results;
	}

	/**
	 * Creates a select answer.
	 * 
	 * @author IanBrown
	 * @return the select answer.
	 * @since Apr 12, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAnswer createSelectAnswer() {
		return createAnswer(3l, FieldType.TEMPLATE_SELECT, 4l, "Select");
	}

	/**
	 * Creates a simple text answer.
	 * 
	 * @author IanBrown
	 * @return the text answer.
	 * @since Apr 12, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAnswer createTextAnswer() {
		return createAnswer(1l, FieldType.TEMPLATE_TEXT, null, "Text");
	}

	/**
	 * Creates a text area answer.
	 * 
	 * @author IanBrown
	 * @return the text area answer.
	 * @since Apr 13, 2012
	 * @version Apr 13, 2012
	 */
	private MobileAnswer createTextAreaAnswer() {
		return createAnswer(5l, FieldType.TEMPLATE_TEXTAREA, null, "Text Area");
	}

	/**
	 * Creates a user field dependency.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard context.
	 * @return the user field dependency.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private UserFieldDependency createUserFieldDependency(final WizardContext wizardContext) {
		final UserFieldDependency userFieldDependency = createMock("UserFieldDependency", UserFieldDependency.class);
		EasyMock.expect(userFieldDependency.checkGroup(EasyMock.anyObject())).andReturn(true).anyTimes();
		EasyMock.expect(userFieldDependency.checkDependency(wizardContext)).andReturn(true).anyTimes();
		return userFieldDependency;
	}

	/**
	 * Creates a variant with the specified keys and fields.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title of the variant.
	 * @param keys
	 *            the keys.
	 * @param fields
	 *            the fields.
	 * @return the variant.
	 * @since Apr 10, 2012
	 * @version May 3, 2012
	 */
	private QuestionVariant createVariant(final String title, final Collection<BasicDependency> keys,
			final Collection<QuestionField> fields) {
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(variant.getTitle()).andReturn(title).anyTimes();
		EasyMock.expect(variant.getKeys()).andReturn(keys).anyTimes();
		EasyMock.expect(variant.getFields()).andReturn(fields).anyTimes();
		if (fields != null) {
			for (final QuestionField field : fields) {
				EasyMock.expect(field.getQuestion()).andReturn(variant).anyTimes();
			}
		}
		return variant;
	}

	/**
	 * Returns the expected address value.
	 * 
	 * @author IanBrown
	 * @param type
	 *            the type of expected address.
	 * @param address
	 *            the address.
	 * @return the expected address - equal to address if not <code>null</code> or an empty address of the expected type otherwise.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private MobileAddress expectedAddress(final AddressType type, final MobileAddress address) {
		return address == null ? new MobileAddress(type) : address;
	}

	/**
	 * Returns the expected person value.
	 * 
	 * @author IanBrown
	 * @param person
	 *            the person.
	 * @return the expected person - equal to person if not <code>null</code> or an empty person otherwise.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private MobilePerson expectedPerson(final MobilePerson person) {
		return person == null ? new MobilePerson() : person;
	}

	/**
	 * Returns the expected string value.
	 * 
	 * @author IanBrown
	 * @param string
	 *            the string.
	 * @return the expected string - equal to string if not <code>null</code> or "" otherwise.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private String expectedString(final String string) {
		return string == null ? "" : string;
	}

	/**
	 * Gets the application context.
	 * 
	 * @author IanBrown
	 * @return the application context.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * Gets the local official DAO.
	 * 
	 * @author IanBrown
	 * @return the local official DAO.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private LocalOfficialDAO getLocalOfficialDAO() {
		return localOfficialDAO;
	}

	/**
	 * Gets the mobile service.
	 * 
	 * @author IanBrown
	 * @return the mobile service.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private MobileService getMobileService() {
		return mobileService;
	}

	/**
	 * Gets the mock add on.
	 * 
	 * @author IanBrown
	 * @return the mock add on.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private AllowedForAddOn getMockAddOn() {
		return mockAddOn;
	}

	/**
	 * Gets the page DAO.
	 * 
	 * @author IanBrown
	 * @return the page DAO.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private QuestionnairePageDAO getPageDAO() {
		return pageDAO;
	}

	/**
	 * Gets the question field DAO.
	 * 
	 * @author IanBrown
	 * @return the question field DAO.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private QuestionFieldDAO getQuestionFieldDAO() {
		return questionFieldDAO;
	}

	/**
	 * Gets the state DAO.
	 * 
	 * @author IanBrown
	 * @return the state DAO.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private StateDAO getStateDAO() {
		return stateDAO;
	}

	/**
	 * Gets the voting region DAO.
	 * 
	 * @author IanBrown
	 * @return the voting region DAO.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private VotingRegionDAO getVotingRegionDAO() {
		return votingRegionDAO;
	}

	/**
	 * Sets the application context.
	 * 
	 * @author IanBrown
	 * @param applicationContext
	 *            the application context to set.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private void setApplicationContext(final ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	/**
	 * Sets the local official DAO.
	 * 
	 * @author IanBrown
	 * @param localOfficialDAO
	 *            the local official DAO to set.
	 * @since Jul 16, 2012
	 * @version Jul 16, 2012
	 */
	private void setLocalOfficialDAO(final LocalOfficialDAO localOfficialDAO) {
		this.localOfficialDAO = localOfficialDAO;
	}

	/**
	 * Sets the mobile service.
	 * 
	 * @author IanBrown
	 * @param mobileService
	 *            the mobile service to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private void setMobileService(final MobileService mobileService) {
		this.mobileService = mobileService;
	}

	/**
	 * Sets the mock add on.
	 * 
	 * @author IanBrown
	 * @param mockAddOn
	 *            the mock add on to set.
	 * @since Apr 24, 2012
	 * @version Apr 24, 2012
	 */
	private void setMockAddOn(final AllowedForAddOn mockAddOn) {
		this.mockAddOn = mockAddOn;
	}

	/**
	 * Sets the page DAO.
	 * 
	 * @author IanBrown
	 * @param pageDAO
	 *            the page DAO to set.
	 * @since Apr 10, 2012
	 * @version Apr 10, 2012
	 */
	private void setPageDAO(final QuestionnairePageDAO pageDAO) {
		this.pageDAO = pageDAO;
	}

	/**
	 * Sets the question field DAO.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the question field DAO to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void setQuestionFieldDAO(final QuestionFieldDAO questionFieldDAO) {
		this.questionFieldDAO = questionFieldDAO;
	}

	/**
	 * Sets the state DAO.
	 * 
	 * @author IanBrown
	 * @param stateDAO
	 *            the state DAO to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void setStateDAO(final StateDAO stateDAO) {
		this.stateDAO = stateDAO;
	}

	/**
	 * Sets the voting region DAO.
	 * 
	 * @author IanBrown
	 * @param votingRegionDAO
	 *            the voting region DAO to set.
	 * @since Apr 12, 2012
	 * @version Apr 12, 2012
	 */
	private void setVotingRegionDAO(final VotingRegionDAO votingRegionDAO) {
		this.votingRegionDAO = votingRegionDAO;
	}
}
