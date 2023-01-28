package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.webservices.DistrictLookupService;
import com.bearcode.ovf.webservices.SmartyStreetService;
import com.bearcode.ovf.webservices.votesmart.VoteSmartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 23, 2008
 * Time: 3:05:35 PM
 *
 * @author Leonid Ginzburg
 */
@Component
public class CandidatePageAddon implements AllowedForAddOn {

    protected static Logger log = LoggerFactory.getLogger( CandidatePageAddon.class.getName() );

    private static final long TYPE_INPUT_ID = 4;
    private static final long TYPE_NO_INPUT_ID = 6;
    private static final long TYPE_RADIO_ID = 3;
    private static final long TYPE_SELECT_ID = 2;

    private String presidentialCandidatesByOfficeState = "";
    private String senateCandidatesByOfficeState = "";
    private String representativesCandidatesByOfficeState = "";


    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private DistrictLookupService districtLookupService;
    @Autowired
    private SmartyStreetService smartyStreetService;
    @Autowired
    private VoteSmartService voteSmartService;


    protected void fillPageWithQuestions( WizardContext formObject, QuestionnairePage page ) {

        UserAddress votingAddress = formObject.getWizardResults().getVotingAddress();
        String state = votingAddress.getState();
        String zip = votingAddress.getZip();
        String address = votingAddress.getStreet1(); // FIXIT: Is it req to use only 1st part of address?
        String city = votingAddress.getCity();

        boolean useOldPresidential = compareStateWithList( state, presidentialCandidatesByOfficeState );
        boolean useOldSenate = compareStateWithList( state, senateCandidatesByOfficeState );
        boolean useOldRepresentatives = compareStateWithList( state, representativesCandidatesByOfficeState );

        String[] districtInfo = smartyStreetService.findDistrict( address, city, state, zip );
        String district = districtInfo[0];
        if ( districtInfo[1].length() > 0 ) { zip = districtInfo[1]; }
        String zip4 = districtInfo[2];
        int additionalCandidate = 1;

        long softId = getFirstFieldId( page );
        page.setQuestions( new LinkedList<Question>() );

        Question question = new Question();
        question.setId( softId++ );
        question.setOrder( 1 );
        question.setPage( page );
        question.setName( "candidates" );
        question.setTitle( "Choosing Your Candidates" );
        question.setVariants( new LinkedList<QuestionVariant>() );
        page.getQuestions().add( question );

        QuestionVariant variant = new QuestionVariant();
        variant.setId( softId++ );
        variant.setQuestion( question );
        variant.setTitle( "Dynamically generated candidate lists" );
        variant.setFields( new LinkedList<QuestionField>() );
        question.getVariants().add( variant );

        QuestionField field = new QuestionField();
        field.setId( softId++ );
        field.setOrder( 1 );
        field.setQuestion( variant );

        String text = "You may use this write-in ballot to vote in federal races by choosing or writing in a candidate or party.<br/><br/>If you are not presented with a list of candidates, please type in the name of the candidate or, if this is a general election, the political party.<br/><br/>If this is a primary election, you must provide the name of the candidate for whom you are voting.<br/><br/>In a non-presidential election year, you will not see a list of candidates for President/Vice President.<br/><br/> If your state allows you to vote for state and local offices, you can write your choices in after you download your FWAB.<br/><br/>";
        if ( zip.length() != 0 && zip4.length() != 0 ) {
            text += "Your congressional candidates are determined based on your 9-digit zip code <strong>(" + zip + "-" + zip4 + ")</strong>. ";
        }
        field.setHelpText( text );
        field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
        variant.getFields().add( field );

        // ----------
        question = new Question();
        question.setId( softId++ );
        question.setOrder( 1 );
        question.setPage( page );
        question.setName( "candidates" );
        question.setTitle( "Note: You are not voting interactively online." );
        question.setVariants( new LinkedList<QuestionVariant>() );
        page.getQuestions().add( question );

        variant = new QuestionVariant();
        variant.setId( softId++ );
        variant.setQuestion( question );
        variant.setTitle( "Note: You are not voting interactively online." );
        variant.setFields( new LinkedList<QuestionField>() );
        question.getVariants().add( variant );

        field = new QuestionField();
        field.setId( softId++ );
        field.setOrder( 1 );
        field.setQuestion( variant );
        field.setHelpText(
                "You must print and mail in your ballot at the end of this procedure. " +
                        "The candidate choices you make will be deleted from this system after you download your write-in ballot and close the window." );
        field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
        variant.getFields().add( field );

        //----  PRESIDENTS
        long fieldId = softId++;
        Answer answer = formObject.getAnswerByFieldId( fieldId );  // looking for existing answer to save previous selection (within the session)
        if ( answer == null ) {
            field = new QuestionField();
            field.setId( fieldId );
            field.setType( questionFieldService.findFieldTypeById( TYPE_RADIO_ID ) );  // select input
        } else {
            field = answer.getField();
        }

        if ( useOldPresidential ) {
            voteSmartService.createPresidentList( field, state, "" );  // no State, no district => list for president
        } else {
            voteSmartService.createPresidentZipList( field, zip, zip4 );
        }
        question = new Question();
        question.setId( softId++ );
        question.setOrder( 1 );
        question.setPage( page );
        question.setTitle( "President/Vice President" );
        question.setVariants( new LinkedList<QuestionVariant>() );
        page.getQuestions().add( question );

        variant = new QuestionVariant();
        variant.setId( softId++ );
        variant.setQuestion( question );
        variant.setTitle( "President/Vice President" );
        variant.setFields( new LinkedList<QuestionField>() );
        question.getVariants().add( variant );

        if ( field.getGenericOptions().size() > 0 ) {
            question.setName( "fwab_choose_candidate" );

            field.setTitle( "President/Vice President candidates" );
            field.setRequired( false );
            field.setSecurity( true );
            field.setInPdfName( "ufPresident" );

            if ( answer == null ) {
                answer = field.createAnswer();
                formObject.putAnswer( answer );
            }
        } else {
            // search for primary list
            if ( useOldPresidential ) {
                voteSmartService.createPrimaryPresidentList( field, state, "" );  // no State, no district => list for president
            } else {
                voteSmartService.createPresidentPrimaryZipList( field, zip, zip4 );
            }

            if ( field.getGenericOptions().size() > 0 ) {

                question.setName( "fwab_choose_candidate" );

                field.setTitle( "President (Primary Election)" );
                question.setTitle( "President (Primary Election)" );
                field.setRequired( false );
                field.setSecurity( true );
                field.setInPdfName( "ufAdditionalCandidate" + additionalCandidate );
                ++additionalCandidate;
                //field.setInPdfName( "ufPrimaryPresident" );

                if ( answer == null ) {
                    answer = field.createAnswer();
                    formObject.putAnswer( answer );
                }
            } else {
                question.setName( "no president race" );

                field = new QuestionField();
                field.setId( softId++ );

                field.setHelpText( String.format( "If you are not presented with a list of candidates please write-in the name of the candidate by hand. For a list of candidates please visit <a href='http://www.congress.org/election/guide' target='_blank' style='text-decoration:underline;'>Congress.org</a> or <a href='http://votesmart.org' target='_blank' style='text-decoration:underline;'>VoteSmart.org</a> or use the <a href='https://www.overseasvotefoundation.org/vote/CandidateFinder.htm' target='_blank' style='text-decoration:underline;'>OVF Candidate Finder tool</a>. " ) );

                field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
            }
        }
        field.setOrder( 1 );
        field.setQuestion( variant );
        variant.getFields().add( field );

        //----  SENATORS
        if ( state.length() != 0 ) {
            // SENATORS
            fieldId = softId++;
            answer = formObject.getAnswerByFieldId( fieldId );  // looking for existing answer to save privious selection (within the session)
            if ( answer == null ) {
                field = new QuestionField();
                field.setId( fieldId );
                field.setType( questionFieldService.findFieldTypeById( TYPE_RADIO_ID ) );  // select input
            } else {
                field = answer.getField();
            }

            if ( useOldSenate ) {
                voteSmartService.createSenateList( field, state, "" );  // State, no district => list for senate
            } else {
                voteSmartService.createSenateZipList( field, zip, zip4 );
            }
            question = new Question();
            question.setId( softId++ );
            question.setOrder( 1 );
            question.setPage( page );
            question.setTitle( "Senate" );
            question.setVariants( new LinkedList<QuestionVariant>() );
            page.getQuestions().add( question );

            variant = new QuestionVariant();
            variant.setId( softId++ );
            variant.setQuestion( question );
            variant.setTitle( "Senate" );
            variant.setFields( new LinkedList<QuestionField>() );
            question.getVariants().add( variant );

            if ( field.getGenericOptions().size() > 0 ) {
                question.setName( "fwab_choose_candidate" );

                field.setTitle( "Senate candidates" );
                field.setRequired( false );
                field.setSecurity( true );
                field.setInPdfName( "ufSenator" );

                if ( answer == null ) {
                    answer = field.createAnswer();
                    formObject.putAnswer( answer );
                }
            } else {

                if ( useOldSenate ) {
                    voteSmartService.createSenatePrimaryList( field, state, "" );
                } else {
                    voteSmartService.createSenatePrimaryZipList( field, zip, zip4 );
                }

                if ( field.getGenericOptions().size() > 0 ) {
                    question.setName( "fwab_choose_candidate" );

                    question.setTitle( "Senate (Primary Election)" );
                    field.setTitle( "Senate primary candidates" );
                    field.setRequired( false );
                    field.setSecurity( true );
                    field.setInPdfName( "ufAdditionalCandidate" + additionalCandidate );
                    ++additionalCandidate;
                    //field.setInPdfName( "senator_primary" );

                    if ( answer == null ) {
                        answer = field.createAnswer();
                        formObject.putAnswer( answer );
                    }
                } else {
                    question.setName( "no senate race" );

                    field = new QuestionField();
                    field.setId( softId++ );
                    field.setHelpText( "Per our current zip-to-district matching, we could not identify a U.S. Senate race for your congressional district based on the U.S. address information you entered. In case of an error in the search, we encourage you to verify the candidate list. Check your Secretary of State website, or the nonpartisan <a target=\"_blank\" href=\"http://www.vote-smart.org\">www.vote-smart.org</a> or <a target=\"_blank\" href=\"http://www.congress.org\">www.congress.org</a> websites to confirm if there is a race in your district. This section of your ballot will print blank: you may write-in a candidate's name or a political party." );
                    field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
                }
            }
            field.setOrder( 1 );
            field.setQuestion( variant );
            variant.getFields().add( field );


            // Special Senate Elections


            fieldId = softId++;
            answer = formObject.getAnswerByFieldId( fieldId );  // looking for existing answer to save privious selection (within the session)
            if ( answer == null ) {
                field = new QuestionField();
                field.setId( fieldId );
                field.setType( questionFieldService.findFieldTypeById( TYPE_RADIO_ID ) );  // select input
            } else {
                field = answer.getField();
            }

            if ( useOldSenate ) {
                voteSmartService.createSenateSpecialList( field, state, "" );  // State, no district => list for senate
            } else {
                voteSmartService.createSenateSpecialZipList( field, zip, zip4 );
            }
            if ( field.getGenericOptions().size() > 0 ) {
                question = new Question();
                question.setId( softId++ );
                question.setOrder( 1 );
                question.setPage( page );
                question.setTitle( "Senate Special Election" );
                question.setVariants( new LinkedList<QuestionVariant>() );
                page.getQuestions().add( question );

                variant = new QuestionVariant();
                variant.setId( softId++ );
                variant.setQuestion( question );
                variant.setTitle( "Senate Special Election" );
                variant.setFields( new LinkedList<QuestionField>() );
                question.getVariants().add( variant );

                question.setName( "fwab_choose_candidate" );

                field.setTitle( "Senate candidates" );
                field.setRequired( false );
                field.setSecurity( true );
                field.setInPdfName( "ufAdditionalCandidate" + additionalCandidate );
                ++additionalCandidate;
                //field.setInPdfName( "senator_special" );

                if ( answer == null ) {
                    answer = field.createAnswer();
                    formObject.putAnswer( answer );
                }
                field.setOrder( 1 );
                field.setQuestion( variant );
                variant.getFields().add( field );
            }

        }

        //----  REPRESENTATIVE
        question = new Question();
        question.setId( softId++ );
        question.setOrder( 1 );
        question.setPage( page );
        question.setTitle( "Representative" );
        question.setVariants( new LinkedList<QuestionVariant>() );
        page.getQuestions().add( question );

        variant = new QuestionVariant();
        variant.setId( softId++ );
        variant.setQuestion( question );
        variant.setTitle( "Representatives" );
        variant.setFields( new LinkedList<QuestionField>() );
        question.getVariants().add( variant );

        if ( district.length() != 0 ) {
            fieldId = softId++;
            answer = formObject.getAnswerByFieldId( fieldId ); // looking for existing answer to save previous selection (within the session)
            if ( answer == null ) {
                field = new QuestionField();
                field.setId( fieldId );
                field.setType( questionFieldService.findFieldTypeById( TYPE_RADIO_ID ) );  // select input
            } else {
                field = answer.getField();
            }

            if ( useOldRepresentatives ) {
                voteSmartService.createRepresentativeList( field, state, district );  // State, district => list for representatives
            } else {
                voteSmartService.createRepresentativeZipList( field, zip, zip4 );
            }

            if ( field.getGenericOptions().size() > 0 ) {
                question.setName( "fwab_choose_candidate" );

                field.setTitle( "Representative candidates" );
                field.setRequired( false );
                field.setSecurity( true );
                field.setInPdfName( "ufRepresentative" );

                if ( answer == null ) {
                    answer = field.createAnswer();
                    formObject.putAnswer( answer );
                }
            } else {

                if ( useOldRepresentatives ) {
                    voteSmartService.createRepresentativePrimaryList( field, state, district );
                } else {
                    voteSmartService.createRepresentativePrimaryZipList( field, zip, zip4 );
                }

                if ( field.getGenericOptions().size() > 0 ) {
                    question.setName( "fwab_choose_candidate" );
                    question.setTitle( "Representative (Primary Election)" );

                    field.setTitle( "Representative primary candidates" );
                    field.setRequired( false );
                    field.setSecurity( true );
                    field.setInPdfName( "ufAdditionalCandidate" + additionalCandidate );
                    ++additionalCandidate;
                    //field.setInPdfName( "representative_primary" );

                    if ( answer == null ) {
                        answer = field.createAnswer();
                        formObject.putAnswer( answer );
                    }
                } else {
                    question.setName( "no representative race" );

                    field = new QuestionField();
                    field.setId( softId++ );
                    field.setHelpText( "Per our current zip-to-district matching, we could not identify your congressional district based upon the U.S. address information you entered. In case of an error in the search, we encourage you to verify the candidate list. Check your Secretary of State website, or the nonpartisan <a target=\"_blank\" href=\"http://www.vote-smart.org\">www.vote-smart.org</a> or <a target=\"_blank\" href=\"http://www.congress.org\">www.congress.org</a> websites to confirm if there is a race in your district. This section of your ballot will print blank: you may write-in a candidate's name or a political party." );
                    field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
                }
            }
            field.setOrder( 1 );
            field.setQuestion( variant );
            variant.getFields().add( field );


            // Representative Special Elections


            fieldId = softId++;
            answer = formObject.getAnswerByFieldId( fieldId );  // looking for existing answer to save privious selection (within the session)
            if ( answer == null ) {
                field = new QuestionField();
                field.setId( fieldId );
                field.setType( questionFieldService.findFieldTypeById( TYPE_RADIO_ID ) );  // select input
            } else {
                field = answer.getField();
            }

            if ( useOldRepresentatives ) {
                voteSmartService.createRepresentativeSpecialList( field, state, district );  // State, no district => list for senate
            } else {
                voteSmartService.createRepresentativeSpecialZipList( field, zip, zip4 );
            }
            if ( field.getGenericOptions().size() > 0 ) {
                question = new Question();
                question.setId( softId++ );
                question.setOrder( 1 );
                question.setPage( page );
                question.setTitle( "Congressional Special Election" );
                question.setVariants( new LinkedList<QuestionVariant>() );
                page.getQuestions().add( question );

                variant = new QuestionVariant();
                variant.setId( softId++ );
                variant.setQuestion( question );
                variant.setTitle( "Congressional Special Election" );
                variant.setFields( new LinkedList<QuestionField>() );
                question.getVariants().add( variant );

                question.setName( "fwab_choose_candidate" );

                field.setTitle( "Conressional candidates" );
                field.setRequired( false );
                field.setSecurity( true );
                field.setInPdfName( "ufAdditionalCandidate" + additionalCandidate );
                ++additionalCandidate;
                //field.setInPdfName( "representative_special" );

                if ( answer == null ) {
                    answer = field.createAnswer();
                    formObject.putAnswer( answer );
                }
                field.setOrder( 1 );
                field.setQuestion( variant );
                variant.getFields().add( field );
            } /*else {
                question.setName( "no representative race" );

                field = new QuestionField();
                field.setId( softId++ );
                field.setHelpText( "Per our current zip-to-district matching, we could not identify a U.S. House of Representatives race for your congressional district based upon the U.S. address information you entered. This section of your ballot will print blank. Check your Secretary of State's website, the non-partisan <a target=\"_blank\" href=\"http://www.vote-smart.org\">www.vote-smart.org</a>, or <a target=\"_blank\" href=\"http://www.congress.org\">www.congress.org</a> to confirm if there is a race in your district in the primary. You may always write-in a candidate's name or a political party. This race will appear on the general election ballot." );
                field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
            }*/

        } else {
            question.setName( "no district found" );

            field = new QuestionField();
            field.setId( softId++ );
            field.setHelpText( "Per our current zip-to-district matching, we could not identify your congressional district based upon the U.S. address information you entered. In case of an error in the search, we encourage you to verify the candidate list. Check your Secretary of State website, or the nonpartisan <a target=\"_blank\" href=\"http://www.votesmart.org\">www.votesmart.org</a> or <a target=\"_blank\" href=\"http://www.congress.org\">www.congress.org</a> websites to confirm if there is a race in your district. This section of your ballot will print blank: you may write-in a candidate's name or a political party." );
            field.setType( questionFieldService.findFieldTypeById( TYPE_NO_INPUT_ID ) );  // string input
            field.setOrder( 1 );
            field.setQuestion( variant );
            variant.getFields().add( field );
        }

    }

    @Override
    public void prepareAddOnPage( WizardContext form, QuestionnairePage currentPage ) {
        // fill page if this is FWAB flow. Leave it intact otherwise, it will be skipped
        if ( form.getFlowType() == FlowType.FWAB ) {
            fillPageWithQuestions( form, currentPage );
        }
    }

    @Override
    public Long getFirstFieldId( QuestionnairePage currentPage ) {
        return firstFieldId + currentPage.getStepNumber() * 1000;
    }

    private boolean compareStateWithList( String state, String statesList ) {
        String[] list = statesList.split( "," );
        if ( list != null && list.length > 0 ) {
            for ( String toCompare : list ) {
                if ( toCompare.trim().length() > 0 &&
                        toCompare.trim().equalsIgnoreCase( state ) ) return true;
            }
        }
        return false;
    }

    public void setPresidentialCandidatesByOfficeState( String presidentialCandidatesByOfficeState ) {
        this.presidentialCandidatesByOfficeState = presidentialCandidatesByOfficeState;
    }

    public void setSenateCandidatesByOfficeState( String senateCandidatesByOfficeState ) {
        this.senateCandidatesByOfficeState = senateCandidatesByOfficeState;
    }

    public void setRepresentativesCandidatesByOfficeState( String representativesCandidatesByOfficeState ) {
        this.representativesCandidatesByOfficeState = representativesCandidatesByOfficeState;
    }
}
