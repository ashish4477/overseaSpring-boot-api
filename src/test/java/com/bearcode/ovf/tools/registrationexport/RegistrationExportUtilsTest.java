package com.bearcode.ovf.tools.registrationexport;

import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.registrationexport.ExportLevel;
import com.bearcode.ovf.service.QuestionnaireService;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by leonid
 */
public class RegistrationExportUtilsTest  extends EasyMockSupport {

    private RegistrationExportUtils registrationExportUtils;

    private QuestionnaireService questionnaireService;

    @Before
    public void setUp() throws Exception {
        RegistrationExportUtils utils = new RegistrationExportUtils();

        setRegistrationExportUtils( utils );
        setQuestionnaireService( createMock( "questionnaireService", QuestionnaireService.class ) );
        ReflectionTestUtils.setField( utils, "questionnaireService", getQuestionnaireService() );
    }

    @Test
    public void testCreateCsv() throws IOException, ParseException {
        List<WizardResults> results = createWizardResults();
        QuestionVariant variant = createMock( "question", QuestionVariant.class );
        EasyMock.expect( variant.getDependencyDescription() ).andReturn( "Face" ).anyTimes();
        EasyMock.expect( getQuestionnaireService().findQuestionVariantById( EasyMock.anyLong() ) ).andReturn( variant ).anyTimes();
        replayAll();

        final byte[] csv = getRegistrationExportUtils().createCsv( results, ExportLevel.EXPORT_FACE );

        final List<WizardResults> actualResultsList = ExportUtilities.extractFromCsv( csv );
        ExportUtilities.assertWizardResults( results, actualResultsList );
        verifyAll();
    }

    /**
     * Creates wizard results.
     *
     * @return the wizard results
     */
    private List<WizardResults> createWizardResults() {
        final List<WizardResults> resultsList = new LinkedList<WizardResults>();
        resultsList.add( createWizardResults( 1l, false, false, false ) );
        resultsList.add( createWizardResults( 2l, true, false, false ) );
        resultsList.add( createWizardResults( 3l, false, true, false ) );
        resultsList.add( createWizardResults( 4l, false, false, true ) );
        return resultsList;
    }


    /**
     * Creates a wizard results.
     * <p>
     * This method is used to create wizard result for the conversion to CSV form.
     *
     * @param id
     *            the identifier.
     * @param forwardingAddress
     *            create forwarding address
     * @param previousAddress
     *            create previous address
     * @param previousName
     *            create previous name f
     * @return the wizard results.
     */
    private WizardResults createWizardResults( final long id, final boolean forwardingAddress,
                                               final boolean previousAddress, final boolean previousName ) {
        final WizardResults results = new WizardResults( FlowType.RAVA );
        results.setId( id );
        results.setAlternateEmail( id + "@alternate.email.address" );
        results.setAlternatePhone( id + "ap" );
        results.setAnswers( createAnswers( id ) );
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.add( Calendar.YEAR, (int) (-18 - id) );
        results.setBirthDate( calendar.get( Calendar.DAY_OF_MONTH ) );
        results.setBirthMonth( calendar.get( Calendar.MONTH ) + 1 );
        results.setBirthYear( calendar.get( Calendar.YEAR ) );
        //final GregorianCalendar createCalendar = new GregorianCalendar();
        //createCalendar.set( Calendar.MILLISECOND, 0 );
        //createCalendar.set( Calendar.SECOND, 0 );
        results.setCreationDate( new Date() );
        results.setCurrentAddress( createAddress( id, "current", true ) );
        results.setUsername( id + "@email.address" );
        results.setFaceUrl( "faces/" + id );
        results.setForwardingAddress( createAddress( id, "forwarding", forwardingAddress ) );
        results.setGender( (id % 2) == 0 ? "Male" : "Female" );
        results.setName( createPerson( id, "", true ) );
        results.setPhone( id + "p" );
        results.setPreviousAddress( createAddress( id, "previous", previousAddress ) );
        results.setPreviousName( createPerson( id, "previous", previousName ) );
        results.setVoterHistory( "Voter History " + id );
        results.setVoterType( "Voter Type " + id );
        results.setVotingAddress( createAddress( id, "voting", true ) );
        results.setVotingRegionName( "Voting Region " + id );
        results.setVotingRegionState( "V" + (id % 10) );
        return results;
    }

    /**
     * Creates an address if desired.
     *
     * @param id
     *            the identifier.
     * @param string
     *            the string used to specify which address.
     * @param needAddress
     *            <code>true</code> to actually create an address, <code>false</code> otherwise.
     * @return the address or <code>null</code>.
     */
    private WizardResultAddress createAddress( final long id, final String string, final boolean needAddress ) {
        if (!needAddress) {
            return null;
        }

        final WizardResultAddress pendingVoterAddress = new WizardResultAddress();
        pendingVoterAddress.setCity(string + " City");
        pendingVoterAddress.setCountry(string);
        pendingVoterAddress.setDescription(string + " description");
        pendingVoterAddress.setZip( Integer.toString( string.hashCode() ) );
        pendingVoterAddress.setState( string.substring( 0, 2 ) );
        pendingVoterAddress.setStreet1( id + " " + string + " St." );
        pendingVoterAddress.setStreet2( id + " " + string );
        return pendingVoterAddress;
    }

    /**
     * Creates an  answer for the specified identifier and question identifier.
     *
     * @param id
     *            the identifier.
     * @param questionId
     *            the question identifier.
     * @return the answer.
     */
    private Answer createAnswer( final long id, final String questionId ) {
        final Answer answer = new EnteredAnswer();
        final QuestionVariant questionVariant = new QuestionVariant();
        questionVariant.setTitle( "Question " + questionId );
        questionVariant.setId( id );
        final QuestionField questionField = new QuestionField();
        questionField.setTitle( "Question " + questionId );
        questionField.setInPdfName( "inPdfName" );
        questionField.setId( id );
        questionField.setQuestion( questionVariant );
        answer.setId( id );
        answer.setField( questionField );
        answer.setValue( "Answer " + questionId + " " + id );
        return answer;
    }

    /**
     * Creates the answers for the specified identifier.
     *
     * @param id
     *            the identifier.
     * @return the answers.
     */
    private List<Answer> createAnswers( final long id ) {
        final List<Answer> answerList = new LinkedList<Answer>();
        answerList.add( createAnswer( id, "1" ) );
        if (id % 10 == 2) {
            answerList.add( createAnswer( id, "2a" ) );
            answerList.add( createAnswer( id, "2b" ) );
        }
        if (id % 10 == 3) {
            answerList.add( createAnswer( id, "3" ) );
        }
        answerList.add( createAnswer( id, "4" ) );
        return answerList;
    }

    /**
     * Creates a person name for the identifier and string if desired.
     *
     * @param id
     *            the identifier.
     * @param string
     *            the string used to indicate which name.
     * @param needName
     *            <code>true</code> if the name is needed, <code>false</code> otherwise.
     * @return the name or <code>null</code>.
     */
    private WizardResultPerson createPerson( final long id, final String string, final boolean needName ) {
        if (needName) {
            return null;
        }

        final WizardResultPerson person = new WizardResultPerson();
        person.setFirstName( id + "first" + string );
        person.setLastName( id + "last" + string );
        person.setMiddleName( id + "middle" + string );
        person.setSuffix( "SF" );
        person.setTitle( id + string );
        return person;
    }


    public RegistrationExportUtils getRegistrationExportUtils() {
        return registrationExportUtils;
    }

    public void setRegistrationExportUtils( RegistrationExportUtils registrationExportUtils ) {
        this.registrationExportUtils = registrationExportUtils;
    }

    public QuestionnaireService getQuestionnaireService() {
        return questionnaireService;
    }

    public void setQuestionnaireService( QuestionnaireService questionnaireService ) {
        this.questionnaireService = questionnaireService;
    }
}
