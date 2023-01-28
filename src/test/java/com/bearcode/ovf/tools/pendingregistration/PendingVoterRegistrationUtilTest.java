package com.bearcode.ovf.tools.pendingregistration;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.*;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAddress;
import com.bearcode.ovf.model.pendingregistration.PendingVoterAnswer;
import com.bearcode.ovf.model.pendingregistration.PendingVoterName;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.PendingVoterRegistrationService;
import com.bearcode.ovf.service.PendingVoterRegistrationTestUtil;
import com.bearcode.ovf.service.QuestionnaireService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Date: 24.10.14
 * Time: 16:09
 *
 * @author Leonid Ginzburg
 */
public class PendingVoterRegistrationUtilTest extends EasyMockSupport {

    private PendingVoterRegistrationUtil pendingVoterRegistrationUtil;


    private PendingVoterRegistrationService pendingVoterRegistrationService;

    private PendingVoterRegistrationCipher pendingVoterRegistrationCipher;

    private QuestionnaireService questionnaireService;

    @Before
    public void setUp() throws Exception {
        final PendingVoterRegistrationUtil util = new PendingVoterRegistrationUtil();
        setPendingVoterRegistrationUtil( util );
        setPendingVoterRegistrationCipher( createMock( "Cipher", PendingVoterRegistrationCipher.class) );
        setPendingVoterRegistrationService( createMock( "PendingService", PendingVoterRegistrationService.class));
        setQuestionnaireService( createMock( "QuestionnaireService", QuestionnaireService.class));

        ReflectionTestUtils.setField(util, "pendingVoterRegistrationService", getPendingVoterRegistrationService() );
        ReflectionTestUtils.setField(util, "pendingVoterRegistrationCipher", getPendingVoterRegistrationCipher() );
        ReflectionTestUtils.setField(util, "questionnaireService", getQuestionnaireService() );
    }

    @After
    public void tearDown() throws Exception {
        setPendingVoterRegistrationUtil( null );
        setPendingVoterRegistrationCipher( null );
        setPendingVoterRegistrationService( null );
        setQuestionnaireService( null );
    }

    /* ================== Convert Context Tests =========================*/

    /**
     * Test method for
     * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationUtil#convertWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
     * .
     *
     * @author IanBrown
     *
     * @since Nov 5, 2012
     * @version Nov 29, 2012
     */
    @Test
    public final void testConvertWizardContext() {
        final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
        final WizardContext wizardContext = new WizardContext(wizardResults);
        final FlowType flowType = FlowType.RAVA;
        EasyMock.expect(wizardResults.getFlowType()).andReturn( flowType );

        final FaceConfig faceConfig = new FaceConfig();
        faceConfig.setUrlPath( "test.ovf/vote" );
        faceConfig.setRelativePrefix( "relative/prefix" );
        wizardResults.setFaceUrl( faceConfig.getUrlPath() );
        final Date creationDate = new Date();
        EasyMock.expect( wizardResults.getCreationDate() ).andReturn( creationDate );
        //final VotingRegion votingRegion = createMock("VotingRegion", VotingRegion.class);
        //EasyMock.expect(wizardResults.getVotingRegion()).andReturn(votingRegion).atLeastOnce();
        //final State votingState = createMock("VotingState", State.class);
        //EasyMock.expect(votingRegion.getState()).andReturn(votingState).atLeastOnce();
        final String votingStateAbbr = "VS";
        //EasyMock.expect(votingState.getAbbr()).andReturn(votingStateAbbr).atLeastOnce();
        final String votingRegionName = "Voting Region";
        //EasyMock.expect(votingRegion.getName()).andReturn( votingRegionName ).atLeastOnce();
        EasyMock.expect(wizardResults.getVotingRegionName()).andReturn(votingRegionName).atLeastOnce();
        EasyMock.expect(wizardResults.getVotingRegionState()).andReturn(votingStateAbbr).atLeastOnce();


        final PendingVoterRegistrationConfiguration pendingVoterRegistrationConfiguration = createMock(
                "PendingVoterRegistrationConfiguration", PendingVoterRegistrationConfiguration.class );
        EasyMock.expect(pendingVoterRegistrationConfiguration.getExportAnswersLevel()).andReturn(PendingVoterRegistrationConfiguration.EXPORT_STATE).anyTimes();
        EasyMock.expect(getPendingVoterRegistrationService().findPendingVoterRegistrationConfiguration( wizardContext, wizardResults )).andReturn(pendingVoterRegistrationConfiguration);
        final String voterType = VoterType.OVERSEAS_VOTER.getTitle();
        EasyMock.expect(wizardResults.getVoterType()).andReturn(voterType);
        final String voterHistory = VoterHistory.DOMESTIC_VOTER.getValue();
        EasyMock.expect( wizardResults.getVoterHistory() ).andReturn( voterHistory );
        EasyMock.expect(wizardResults.getName()).andReturn(createName("Name")).atLeastOnce();
        EasyMock.expect(wizardResults.getPreviousName()).andReturn( null ).atLeastOnce();
        final String emailAddress = "email@address.com";
        EasyMock.expect(wizardResults.getUsername()).andReturn(emailAddress);
        final String alternateEmailAddress = "alternate@email.address.com";
        EasyMock.expect(wizardResults.getAlternateEmail()).andReturn(alternateEmailAddress);
        final String phoneNumber = "123456789";
        EasyMock.expect(wizardResults.getPhone()).andReturn(phoneNumber);
        final String phoneNumberType = PhoneNumberType.Mobile.name();
        EasyMock.expect( wizardResults.getPhoneType() ).andReturn( phoneNumberType ).anyTimes();
        EasyMock.expect( wizardResults.getAlternatePhoneType() ).andReturn( phoneNumberType ).anyTimes();
        final String alternatePhoneNumber = "234567890";
        EasyMock.expect(wizardResults.getAlternatePhone()).andReturn(alternatePhoneNumber);
        final int birthMonth = 11;
        EasyMock.expect(wizardResults.getBirthMonth()).andReturn(birthMonth);
        final int birthDate = 5;
        EasyMock.expect(wizardResults.getBirthDate()).andReturn(birthDate);
        final int birthYear = 2012;
        EasyMock.expect(wizardResults.getBirthYear()).andReturn(birthYear);
        final String gender = "Male";
        EasyMock.expect(wizardResults.getGender()).andReturn(gender);
        EasyMock.expect(wizardResults.getVotingAddress()).andReturn(createAddress("VotingAddress", AddressType.STREET))
                .atLeastOnce();
        EasyMock.expect(wizardResults.getCurrentAddress()).andReturn(createAddress("CurrentAddress", AddressType.MILITARY))
                .atLeastOnce();
        EasyMock.expect(wizardResults.getForwardingAddress()).andReturn(createAddress("ForwardingAddress", AddressType.OVERSEAS))
                .atLeastOnce();
        EasyMock.expect(wizardResults.getPreviousAddress()).andReturn(null).atLeastOnce();
        final Collection<Answer> answers = createAnswers();
        EasyMock.expect(wizardResults.getAnswers()).andReturn(answers).atLeastOnce();
        replayAll();
        wizardContext.setCurrentFace( faceConfig );

        final PendingVoterRegistration actualPendingVoterRegistration = getPendingVoterRegistrationUtil()
                .convertWizardContext(wizardContext);

        assertNotNull("A pending voter registration is returned", actualPendingVoterRegistration);
        assertEquals("The created date is set", creationDate, actualPendingVoterRegistration.getCreatedDate());
        assertEquals("The voting state is set", votingStateAbbr, actualPendingVoterRegistration.getVotingState());
        assertEquals("The voting region is set", votingRegionName, actualPendingVoterRegistration.getVotingRegion());
        assertEquals("The voter type is set", voterType, actualPendingVoterRegistration.getVoterType());
        assertEquals("The voter history is set", voterHistory, actualPendingVoterRegistration.getVoterHistory());
        assertName("The name", wizardResults.getName(), actualPendingVoterRegistration.getName());
        assertName("The previous name", wizardResults.getPreviousName(), actualPendingVoterRegistration.getPreviousName());
        assertEquals("The email address is set", emailAddress, actualPendingVoterRegistration.getEmailAddress());
        assertEquals("The alternate email address is set", alternateEmailAddress,
                actualPendingVoterRegistration.getAlternateEmailAddress());
        assertEquals("The phone number is set", phoneNumber, actualPendingVoterRegistration.getPhoneNumber());
        assertEquals("The alternate phone number is set", alternatePhoneNumber,
                actualPendingVoterRegistration.getAlternatePhoneNumber());
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.YEAR, birthYear);
        calendar.set(Calendar.MONTH, birthMonth-1);
        calendar.set(Calendar.DAY_OF_MONTH, birthDate);
        final String expectedBirthDay = PendingVoterRegistration.BIRTH_DAY_FORMAT.format(calendar.getTime());
        assertEquals("The birth day is set", expectedBirthDay, actualPendingVoterRegistration.getBirthDay());
        assertEquals("The gender is set", gender, actualPendingVoterRegistration.getGender());
        assertAddress("The voting address", wizardResults.getVotingAddress(), actualPendingVoterRegistration.getVotingAddress());
        assertAddress("The current address", wizardResults.getCurrentAddress(), actualPendingVoterRegistration.getCurrentAddress());
        assertAddress("The forwarding address", wizardResults.getForwardingAddress(),
                actualPendingVoterRegistration.getForwardingAddress());
        assertAddress("The previous address", wizardResults.getPreviousAddress(),
                actualPendingVoterRegistration.getPreviousAddress());
        assertAnswers(wizardResults.getAnswers(), actualPendingVoterRegistration.getAnswers());
        assertNull("Nothing has been encrypted", actualPendingVoterRegistration.getEncrypted());
        verifyAll();
    }

    /**
     * Test method for
     * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationUtil#convertWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
     * for the case where the flow is not FPCA.
     *
     * @author IanBrown
     *
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    @Test(expected = IllegalArgumentException.class)
    public final void testConvertWizardContext_notFPCA() {
        final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
        final FlowType flowType = FlowType.FWAB;
        EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
        replayAll();
        final WizardContext wizardContext = new WizardContext(wizardResults);

        getPendingVoterRegistrationUtil().convertWizardContext(wizardContext);
    }

    /**
     * Test method for
     * {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationUtil#convertWizardContext(com.bearcode.ovf.actions.questionnaire.forms.WizardContext)}
     * for the case where the face does not match the one required for the voting region.
     *
     * @author IanBrown
     *
     * @since Nov 13, 2012
     * @version Nov 28, 2012
     */
    @Test
    public final void testConvertWizardContext_noConfiguration() {
        final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
        final FlowType flowType = FlowType.RAVA;
        EasyMock.expect(wizardResults.getFlowType()).andReturn(flowType);
        EasyMock.expect(getPendingVoterRegistrationService().findPendingVoterRegistrationConfiguration(
                EasyMock.anyObject(WizardContext.class),EasyMock.eq(wizardResults))).andReturn(null);
        replayAll();
        final WizardContext wizardContext = new WizardContext(wizardResults);

        final PendingVoterRegistration actualPendingVoterRegistration = getPendingVoterRegistrationUtil().convertWizardContext(
        wizardContext);

        assertNull("No pending voter registration is returned", actualPendingVoterRegistration);
        verifyAll();
    }

    /**
     * Custom assertion to ensure that the address is copied correctly.
     *
     * @author IanBrown
     * @param string
     *            the string used as the message.
     * @param expectedAddress
     *            the expected address.
     * @param actualAddress
     *            the actual address.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private void assertAddress(final String string, final WizardResultAddress expectedAddress,
                               final PendingVoterAddress actualAddress) {
        if (expectedAddress == null) {
            assertNull(string + " is not set", actualAddress);
            return;
        }

        assertNotNull(string + " is set", actualAddress);
        assertEquals("The first street line is set", expectedAddress.getStreet1(), actualAddress.getStreet1());
        assertEquals("The second street line is set", expectedAddress.getStreet2(), actualAddress.getStreet2());
        assertEquals("The description is set", expectedAddress.getDescription(), actualAddress.getDescription());
        assertEquals("The city is set", expectedAddress.getCity(), actualAddress.getCity());
        assertEquals("The state or region is set", expectedAddress.getState(), actualAddress.getStateOrRegion());
        final String expectedPostalCode = expectedAddress.getZip()
                + (expectedAddress.getZip4() == null || expectedAddress.getZip4().isEmpty() ? "" : "-" + expectedAddress.getZip4());
        assertEquals("The postal code is set", expectedPostalCode, actualAddress.getPostalCode());
        assertEquals("The country is set", expectedAddress.getCountry(), actualAddress.getCountry());
    }

    /**
     * Custom assertion to ensure that the answer was copied correctly.
     *
     * @author IanBrown
     * @param expectedAnswer
     *            the expected answer.
     * @param actualAnswer
     *            the actual answer.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private void assertAnswer(final Answer expectedAnswer, final PendingVoterAnswer actualAnswer) {
        final QuestionField expectedField = expectedAnswer.getField();
        final String expectedTitle = expectedField.getTitle();
        assertEquals("The question is set", expectedTitle, actualAnswer.getQuestion());
        assertEquals("The answer is set", expectedAnswer.getValue(), actualAnswer.getAnswer());
        assertNull("Nothing is encrypted", actualAnswer.getEncrypted());
    }

    /**
     * Custom assertion to ensure that the answers are copied correctly.
     *
     * @author IanBrown
     * @param expectedAnswers
     *            the expected answers.
     * @param actualAnswers
     *            the actual answers.
     * @since Nov 5, 2012
     * @version Nov 29, 2012
     */
    private void assertAnswers(final Collection<Answer> expectedAnswers, final List<PendingVoterAnswer> actualAnswers) {
        final Iterator<Answer> expectedAnswerItr = expectedAnswers.iterator();
        final Iterator<PendingVoterAnswer> actualAnswerItr = actualAnswers.iterator();
        while (expectedAnswerItr.hasNext()) {
            final Answer expectedAnswer = expectedAnswerItr.next();
            final QuestionField expectedField = expectedAnswer.getField();
            if (expectedField.getInPdfName() != null && !expectedField.getInPdfName().trim().isEmpty()) {
                final PendingVoterAnswer actualAnswer = actualAnswerItr.next();
                assertAnswer(expectedAnswer, actualAnswer);
            }
        }
        assertFalse("There are no extra answers", actualAnswerItr.hasNext());
    }

    /**
     * Custom assertion to check that the name is copied correctly.
     *
     * @author IanBrown
     * @param string
     *            the message string.
     * @param expectedName
     *            the expected name.
     * @param actualName
     *            the actual name.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private void assertName(final String string, final WizardResultPerson expectedName, final PendingVoterName actualName) {
        if (expectedName == null) {
            assertNull(string + " is not set", actualName);
            return;
        }

        assertNotNull(string + " is set", actualName);
        assertEquals(string + " title is set", expectedName.getTitle(), actualName.getTitle());
        assertEquals(string + " first name is set", expectedName.getFirstName(), actualName.getFirstName());
        assertEquals(string + "  middle name is set", expectedName.getMiddleName(), actualName.getMiddleName());
        assertEquals(string + "  last name is set", expectedName.getLastName(), actualName.getLastName());
        assertEquals(string + "  suffix is set", expectedName.getSuffix(), actualName.getSuffix());
        assertNull(string + " encrypted is not set", actualName.getEncrypted());
    }

    /**
     * Creates an address of the specified type.
     *
     * @author IanBrown
     * @param name
     *            the name of the address.
     * @param type
     *            the type of address.
     * @return the address.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private WizardResultAddress createAddress(final String name, final AddressType type) {
        final WizardResultAddress address = createMock(name, WizardResultAddress.class);
        EasyMock.expect(address.getType()).andReturn(type).anyTimes();
        EasyMock.expect(address.getStreet1()).andReturn("1 " + name + " Street").atLeastOnce();
        EasyMock.expect(address.getStreet2()).andReturn("2 " + name).atLeastOnce();
        EasyMock.expect(address.getCity()).andReturn(name + " City").atLeastOnce();
        EasyMock.expect(address.getState()).andReturn(name.substring(0, 2).toUpperCase()).atLeastOnce();
        EasyMock.expect(address.getZip()).andReturn(Integer.toString(name.hashCode() % 100000)).atLeastOnce();
        switch (type) {
            case STREET:
                EasyMock.expect(address.getDescription()).andReturn(null).atLeastOnce();
                EasyMock.expect(address.getZip4()).andReturn(Integer.toString(name.hashCode() % 10000)).atLeastOnce();
                EasyMock.expect(address.getCountry()).andReturn(null).atLeastOnce();
                break;

            case RURAL_ROUTE:
            case DESCRIBED:
                EasyMock.expect(address.getDescription()).andReturn("Description of " + name).atLeastOnce();
                EasyMock.expect(address.getZip4()).andReturn(Integer.toString(name.hashCode() % 10000)).atLeastOnce();
                EasyMock.expect(address.getCountry()).andReturn(null).atLeastOnce();
                break;

            case MILITARY:
                EasyMock.expect(address.getDescription()).andReturn(null).atLeastOnce();
                EasyMock.expect(address.getZip4()).andReturn(null).atLeastOnce();
                EasyMock.expect(address.getCountry()).andReturn(name).atLeastOnce();
                break;

            case OVERSEAS:
            default:
                EasyMock.expect(address.getDescription()).andReturn(null).atLeastOnce();
                EasyMock.expect(address.getZip4()).andReturn(null).atLeastOnce();
                EasyMock.expect(address.getCountry()).andReturn(name).atLeastOnce();
                break;
        }
        return address;
    }

    /**
     * Creates an answer for the specified name.
     *
     * @author IanBrown
     * @param name
     *            the name.
     * @param inPdf <code>true</code> if the answer should be in the PDF, <code>false</code> otherwise.
     * @return the answer.
     * @since Nov 5, 2012
     * @version Nov 29, 2012
     */
    private Answer createAnswer(final String name, final boolean inPdf) {
        final Answer answer = createMock("Answer" + name, Answer.class);
        final QuestionField field = createMock("Field" + name, QuestionField.class);
        EasyMock.expect(answer.getField()).andReturn(field).atLeastOnce();
        EasyMock.expect(field.getInPdfName()).andReturn(inPdf ? "InPdf" + name : null).atLeastOnce();
        EasyMock.expect(field.getTitle()).andReturn("Title for " + name).atLeastOnce();
        EasyMock.expect(answer.getValue()).andReturn(name).anyTimes();
        EasyMock.expect(answer.getId()).andReturn(null).anyTimes();
        EasyMock.expect(field.isSecurity()).andReturn(false).anyTimes();
        return answer;
    }

    /**
     * Creates answers.
     *
     * @author IanBrown
     * @return the answers.
     * @since Nov 5, 2012
     * @version Nov 29, 2012
     */
    private Collection<Answer> createAnswers() {
        final Answer answer1 = createAnswer("Answer1", true);
        final Answer answer2 = createAnswer("Answer2", false);
        final Answer answer3 = createAnswer("Answer3", true);
        final Collection<Answer> answers = Arrays.asList(answer1, answer2, answer3);
        return answers;
    }

    /**
     * Creates a name object for the specified string.
     *
     * @author IanBrown
     * @param string
     *            the string defining the name.
     * @return the name.
     * @since Nov 5, 2012
     * @version Nov 5, 2012
     */
    private WizardResultPerson createName(final String string) {
        final WizardResultPerson name = createMock(string.replace(' ', '_'), WizardResultPerson.class);
        EasyMock.expect(name.getTitle()).andReturn("Title").atLeastOnce();
        EasyMock.expect(name.getFirstName()).andReturn("First" + name).atLeastOnce();
        EasyMock.expect(name.getMiddleName()).andReturn("Middle" + name).atLeastOnce();
        EasyMock.expect(name.getLastName()).andReturn("Last" + name).atLeastOnce();
        EasyMock.expect(name.getSuffix()).andReturn("Suffix").atLeastOnce();
        return name;
    }

    /* ==================== Create CSV tests =================================*/
    /**
     * Test method for {@link com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationUtil#createCsv(List)}.
     *
     * @author IanBrown
     * @throws java.io.IOException
     *             if there is a problem reading the CSV.
     * @throws java.text.ParseException
     *             if there is a problem parsing the created date.
     * @since Nov 9, 2012
     * @version Nov 14, 2012
     */
    @Test
    public final void testCreateCsv() throws IOException, ParseException {
        final List<PendingVoterRegistration> pendingVoterRegistrations = createPendingVoterRegistrations();
        replayAll();

        final byte[] csv = getPendingVoterRegistrationUtil().createCsv(pendingVoterRegistrations);

        final List<PendingVoterRegistration> actualPendingVoterRegistrations = PendingVoterRegistrationTestUtil.extractFromCsv(csv);
        PendingVoterRegistrationTestUtil.assertPendingVoterRegistrations(pendingVoterRegistrations, actualPendingVoterRegistrations);
        verifyAll();
    }

    /**
     * Creates a pending voter registration.
     * <p>
     * This method is used to create pending voter registrations for the conversion to CSV form. Since we may need every field in
     * the registration for this, a real registration is created rather than a mock.
     *
     * @author IanBrown
     * @param id
     *            the identifier.
     * @param forwardingAddress
     *            create forwarding address for the pending voter registration.
     * @param previousAddress
     *            create previous address for the pending voter registration.
     * @param previousName
     *            create previous name for the pending voter registration.
     * @return the pending voter registration.
     * @since Nov 9, 2012
     * @version Nov 26, 2012
     */
    private PendingVoterRegistration createPendingVoterRegistration(final long id, final boolean forwardingAddress,
                                                                    final boolean previousAddress, final boolean previousName) {
        final PendingVoterRegistration pendingVoterRegistration = new PendingVoterRegistration();
        pendingVoterRegistration.setId( id );
        pendingVoterRegistration.setAlternateEmailAddress( id + "@alternate.email.address" );
        pendingVoterRegistration.setAlternatePhoneNumber( id + "ap" );
        pendingVoterRegistration.setAnswers( createPendingVoterAnswers( id ) );
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add( Calendar.YEAR, (int) (-18 - id) );
        pendingVoterRegistration.setBirthDay( PendingVoterRegistration.BIRTH_DAY_FORMAT.format( calendar.getTime() ) );
        //final GregorianCalendar createCalendar = new GregorianCalendar();
        //createCalendar.set( Calendar.MILLISECOND, 0 );
        //createCalendar.set( Calendar.SECOND, 0 );
        pendingVoterRegistration.setCreatedDate(new Date());
        pendingVoterRegistration.setCurrentAddress(createPendingVoterAddress(id, "current", true));
        pendingVoterRegistration.setEmailAddress(id + "@email.address");
        pendingVoterRegistration.setFacePrefix("faces/" + id);
        pendingVoterRegistration.setForwardingAddress(createPendingVoterAddress(id, "forwarding", forwardingAddress));
        pendingVoterRegistration.setGender((id % 2) == 0 ? "Male" : "Female");
        pendingVoterRegistration.setName(createPendingVoterName(id, "", true));
        pendingVoterRegistration.setPhoneNumber(id + "p");
        pendingVoterRegistration.setPreviousAddress(createPendingVoterAddress(id, "previous", previousAddress));
        pendingVoterRegistration.setPreviousName(createPendingVoterName(id, "previous", previousName));
        pendingVoterRegistration.setVoterHistory("Voter History " + id);
        pendingVoterRegistration.setVoterType("Voter Type " + id);
        pendingVoterRegistration.setVotingAddress(createPendingVoterAddress(id, "voting", true));
        pendingVoterRegistration.setVotingRegion("Voting Region " + id);
        pendingVoterRegistration.setVotingState("V" + (id % 10));
        return pendingVoterRegistration;
    }

    /**
     * Creates a pending voter address if desired.
     *
     * @author IanBrown
     * @param id
     *            the identifier.
     * @param string
     *            the string used to specify which address.
     * @param needAddress
     *            <code>true</code> to actually create an address, <code>false</code> otherwise.
     * @return the address or <code>null</code>.
     * @since Nov 9, 2012
     * @version Nov 9, 2012
     */
    private PendingVoterAddress createPendingVoterAddress(final long id, final String string, final boolean needAddress) {
        if (!needAddress) {
            return null;
        }

        final PendingVoterAddress pendingVoterAddress = new PendingVoterAddress();
        pendingVoterAddress.setCity(string + " City");
        pendingVoterAddress.setCountry(string);
        pendingVoterAddress.setDescription(string + " description");
        pendingVoterAddress.setPostalCode(Integer.toString(string.hashCode()));
        pendingVoterAddress.setStateOrRegion(string.substring(0, 2));
        pendingVoterAddress.setStreet1(id + " " + string + " St.");
        pendingVoterAddress.setStreet2(id + " " + string);
        return pendingVoterAddress;
    }

    /**
     * Creates an pending voter answer for the specified identifier and question identifier.
     *
     * @author IanBrown
     * @param id
     *            the identifier.
     * @param questionId
     *            the question identifier.
     * @return the pending voter answer.
     * @since Nov 9, 2012
     * @version Nov 9, 2012
     */
    private PendingVoterAnswer createPendingVoterAnswer(final long id, final String questionId) {
        final PendingVoterAnswer pendingVoterAnswer = new PendingVoterAnswer();
        pendingVoterAnswer.setQuestion("Question " + questionId);
        pendingVoterAnswer.setAnswer("Answer " + questionId + " " + id);
        return pendingVoterAnswer;
    }

    /**
     * Creates the pending voter answers for the specified identifier.
     *
     * @author IanBrown
     * @param id
     *            the identifier.
     * @return the pending voter answers.
     * @since Nov 9, 2012
     * @version Nov 9, 2012
     */
    private List<PendingVoterAnswer> createPendingVoterAnswers(final long id) {
        final List<PendingVoterAnswer> pendingVoterAnswers = new LinkedList<PendingVoterAnswer>();
        pendingVoterAnswers.add(createPendingVoterAnswer(id, "1"));
        if (id % 10 == 2) {
            pendingVoterAnswers.add(createPendingVoterAnswer(id, "2a"));
            pendingVoterAnswers.add(createPendingVoterAnswer(id, "2b"));
        }
        if (id % 10 == 3) {
            pendingVoterAnswers.add(createPendingVoterAnswer(id, "3"));
        }
        pendingVoterAnswers.add(createPendingVoterAnswer(id, "4"));
        return pendingVoterAnswers;
    }

    /**
     * Creates a pending voter name for the identifier and string if desired.
     *
     * @author IanBrown
     * @param id
     *            the identifier.
     * @param string
     *            the string used to indicate which name.
     * @param needName
     *            <code>true</code> if the name is needed, <code>false</code> otherwise.
     * @return the name or <code>null</code>.
     * @since Nov 9, 2012
     * @version Nov 9, 2012
     */
    private PendingVoterName createPendingVoterName(final long id, final String string, final boolean needName) {
        if (needName) {
            return null;
        }

        final PendingVoterName pendingVoterName = new PendingVoterName();
        pendingVoterName.setFirstName(id + "first" + string);
        pendingVoterName.setLastName(id + "last" + string);
        pendingVoterName.setMiddleName(id + "middle" + string);
        pendingVoterName.setSuffix("SF");
        pendingVoterName.setTitle(id + string);
        return pendingVoterName;
    }

    /**
     * Creates pending voter registrations.
     *
     * @author IanBrown
     * @return the pending voter registrations.
     * @since Nov 9, 2012
     * @version Nov 9, 2012
     */
    private List<PendingVoterRegistration> createPendingVoterRegistrations() {
        final List<PendingVoterRegistration> pendingVoterRegistrations = new LinkedList<PendingVoterRegistration>();
        pendingVoterRegistrations.add(createPendingVoterRegistration(1l, false, false, false));
        pendingVoterRegistrations.add(createPendingVoterRegistration(2l, true, false, false));
        pendingVoterRegistrations.add(createPendingVoterRegistration(3l, false, true, false));
        pendingVoterRegistrations.add(createPendingVoterRegistration(4l, false, false, true));
        return pendingVoterRegistrations;
    }




    public PendingVoterRegistrationUtil getPendingVoterRegistrationUtil() {
        return pendingVoterRegistrationUtil;
    }

    public void setPendingVoterRegistrationUtil(PendingVoterRegistrationUtil pendingVoterRegistrationUtil) {
        this.pendingVoterRegistrationUtil = pendingVoterRegistrationUtil;
    }

    public PendingVoterRegistrationService getPendingVoterRegistrationService() {
        return pendingVoterRegistrationService;
    }

    public void setPendingVoterRegistrationService(PendingVoterRegistrationService pendingVoterRegistrationService) {
        this.pendingVoterRegistrationService = pendingVoterRegistrationService;
    }

    public PendingVoterRegistrationCipher getPendingVoterRegistrationCipher() {
        return pendingVoterRegistrationCipher;
    }

    public void setPendingVoterRegistrationCipher(PendingVoterRegistrationCipher pendingVoterRegistrationCipher) {
        this.pendingVoterRegistrationCipher = pendingVoterRegistrationCipher;
    }

    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }
}
