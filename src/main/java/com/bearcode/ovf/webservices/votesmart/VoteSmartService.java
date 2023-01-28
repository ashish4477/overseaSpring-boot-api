package com.bearcode.ovf.webservices.votesmart;

import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.VirtualDictionaryItem;
import com.bearcode.ovf.webservices.votesmart.model.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VoteSmartService {

    //
    // VoteSmart Service constants
    // Documentation http://api.votesmart.org/docs/
    //
    private static final String VS_API_URL = "http://api.votesmart.org/";
    private static final String VS_API_OUTPUT_FORMAT = "xml"; // supported format by voteSmart JSON/XML
    private static final String PRECIDENT_OFFICE_ID = "1";
    private static final String VICE_PRECIDENT_OFFICE_ID = "2";

    private static Logger voteSmartLog = LoggerFactory.getLogger("com.bearcode.VoteSmart");
    
    private static SimpleDateFormat KEEP_UNTIL_FORMAT = new SimpleDateFormat("dd-MMM-yyyy");

    private enum VoteSmartMethodMeta {
        PRESIDENT_LIST( "%spresident_%s", "Election.getStageCandidates", new String[]{"year"} ),
        PRESIDENT_PRIMARY_LIST( "%spresident_primary_%s", "Election.getStageCandidates", new String[]{"year"} ),
        SENATE_LIST( "%ssenate_%s", "Candidates.getByOfficeState",  new String[]{"stateId"} ),
        SENATE_PRIMARY_LIST( "%ssenate_%s", "Candidates.getByOfficeState", new String[]{"stateId"} ),
        SENATE_SPECIAL_LIST( "%ssenate_%s", "Candidates.getByOfficeState", new String[]{"stateId"} ),
        REPRESENTATIVE_LIST( "%scongress_%s", "Candidates.getByOfficeState", new String[]{"stateId"} ),
        REPRESENTATIVE_PRIMARY_LIST( "%scongress_%s", "Candidates.getByOfficeState", new String[]{"stateId"} ),
        REPRESENTATIVE_SPECIAL_LIST( "%scongress_%s", "Candidates.getByOfficeState", new String[]{"stateId"} ),
        CANDIDATES_GET_BY_ZIP( "%scandidates_by_zip_%s_%s_%s", "Candidates.getByZip", new String[]{"zip5","zip4","electionYear"} ),
        CANDIDATE_BIO( "%scandidate_bio_%s", "CandidateBio.getBio", new String[]{"candidateId"} ),
        CANDIDATE_ADDL_BIO( "%scandidate_bio_%s_addl", "CandidateBio.getAddlBio", new String[]{"candidateId"} ),
        OFFICIALS_GET_BY_ZIP( "%sofficials_by_zip_%s_%s_%s", "Officials.getByZip", new String[]{"zip5","zip4","electionYear"} ),
        VOTES_GET_CATEGORIES( "%svotes_categories", "Votes.getCategories", new String[]{} ),
        VOTES_GET_BY_OFFICIALS( "%svotes_by_officials_%s_%s_%s", "Votes.getByOfficial", new String[]{"candidateId", "categoryId", "year" }),
        ELECTIONS( "%selection_%s", "Election.getElectionByYearState", new String[]{ "year" }),
        CANDIDATE_DETAILED_BIO( "%scandidate_detailed_bio_%s", "CandidateBio.getDetailedBio", new String[]{"candidateId"} );

        VoteSmartMethodMeta( String fileNameTemplate, String voteSmartAPIMethod, String[] arguments ) {
            this.fileNameTemplate = fileNameTemplate;
            this.voteSmartAPIMethod = voteSmartAPIMethod;
            this.arguments = arguments;
        }

        String getCacheFileName( Object... args ) {
            String fn = String.format( fileNameTemplate, args ) + "." + VS_API_OUTPUT_FORMAT;
            return FilenameUtils.normalize( fn );
        }

        String getMethod() {
            return voteSmartAPIMethod;
        }

        private String[] getArguments() {
            return arguments;
        }

        final String fileNameTemplate;
        final String voteSmartAPIMethod;
        /**
         * Arguments for file name template
         */
        final String[] arguments;
    }


    //
    // Bean properties
    //
    private String votesmartKey;
    private String votesmartCacheDir;
    private boolean useVotesmartPresidental = true;
    private boolean useVotesmartSenate = true;
    private boolean useVotesmartRepresentatives = true;
    private boolean useVotesmartCandidateBio = true;
    private boolean useVotesmartCandidateAdditionalBio = true;
    private boolean useVotesmartCandidatesByDistrict = true;
    private long votesmartCacheTime = 60L * 60L * 1000L; // 1 hour

    private boolean testMode = false;
    private String testCandidatesFileName = "";

    public void setVotesmartKey( String votesmartKey ) {
        this.votesmartKey = votesmartKey;
    }

    public void setVotesmartCacheDir( String voteSmartCacheDir ) {
        voteSmartCacheDir = FilenameUtils.normalizeNoEndSeparator( voteSmartCacheDir );
        if ( voteSmartCacheDir == null )
            throw new IllegalArgumentException( "Invalid VoteSmart Service Cache directory: '" + voteSmartCacheDir + "'" );

        File cacheDir = new File( voteSmartCacheDir );
        if ( cacheDir.exists() ) {
            voteSmartLog.info( "VoteSmart Service Cache Directory '" + voteSmartCacheDir + "' found" );
        } else {
            cacheDir.mkdir();
            voteSmartLog.info( "VoteSmart Service Cache Directory '" + voteSmartCacheDir + "' has been created" );
        }
        this.votesmartCacheDir = voteSmartCacheDir + File.separator;
    }

    private String getVotesmartCacheDir() {
        return votesmartCacheDir;
    }

    public void setUseVotesmartPresidental( boolean useVotesmartPresidental ) {
        this.useVotesmartPresidental = useVotesmartPresidental;
    }

    public void setUseVotesmartSenate( boolean useVotesmartSenate ) {
        this.useVotesmartSenate = useVotesmartSenate;
    }

    public void setUseVotesmartRepresentatives( boolean useVotesmartRepresentatives ) {
        this.useVotesmartRepresentatives = useVotesmartRepresentatives;
    }

    public void setUseVotesmartCandidateBio( boolean useVotesmartCandidateBio ) {
        this.useVotesmartCandidateBio = useVotesmartCandidateBio;
    }

    public void setUseVotesmartCandidateAdditionalBio( boolean useVotesmartCandidateAdditionalBio ) {
        this.useVotesmartCandidateAdditionalBio = useVotesmartCandidateAdditionalBio;
    }

    public void setUseVotesmartCandidatesByDistrict( boolean useVotesmartCandidatesByDistrict ) {
        this.useVotesmartCandidatesByDistrict = useVotesmartCandidatesByDistrict;
    }

    public void setVotesmartCacheTime( long votesmartCacheTime ) {
        this.votesmartCacheTime = votesmartCacheTime;
    }

    //
    // VoteSmart Service methods
    //

    // TODO: Refactoring required,
    // QuestionField field not needed here,
    // it must be initialized in upper level of code
    public void createPresidentList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.PRESIDENT_LIST );
    }

    public void createSenateList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.SENATE_LIST );
    }

    /**
     * Populates the given field with a list of Senate special election candidates
     *
     * @param field
     * @param state
     * @param district
     */
    public void createSenateSpecialList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.SENATE_SPECIAL_LIST );
    }

    public void createRepresentativeList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.REPRESENTATIVE_LIST );
    }

    public void createRepresentativeSpecialList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.REPRESENTATIVE_SPECIAL_LIST );
    }

    public void createPrimaryPresidentList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.PRESIDENT_PRIMARY_LIST );
    }

    public void createRepresentativePrimaryList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.REPRESENTATIVE_PRIMARY_LIST );
    }

    public void createSenatePrimaryList( QuestionField field, String state, String district ) {
        this.createCandidateList( field, state, district, VoteSmartMethodMeta.SENATE_PRIMARY_LIST );
    }

    public List<CandidateZip> getPresidents() throws ConfigurationException {
        List<CandidateZip> presidentDetails = new ArrayList<CandidateZip>();
        String electionId = findPresidentialElectionId();
        Map<String, String> presideentMap = new HashMap<String, String>();
        presideentMap.put("electionId", electionId);
        presideentMap.put("stageId", "G");
        String presidentFile = saveAndCacheFile(VoteSmartService.VoteSmartMethodMeta.PRESIDENT_LIST, presideentMap);
        XMLConfiguration presidentXmlReader = new XMLConfiguration();
        presidentXmlReader.setDelimiterParsingDisabled(true);
        presidentXmlReader.load(presidentFile);
        List<HierarchicalConfiguration> presidentCandidate = presidentXmlReader.configurationsAt("candidate");

        if (presidentCandidate != null && !presidentCandidate.isEmpty()) {
            for (HierarchicalConfiguration candidate : presidentCandidate) {
                if ("Running".equals(candidate.getString("status")) && "1".equals(candidate.getString("officeId"))) {
                    CandidateZip president = new CandidateZip();
                    president.setCandidateId(candidate.getString("candidateId"));
                    president.setFirstName(candidate.getString("firstName"));
                    president.setMiddleName(candidate.getString("middleName"));
                    president.setLastName(candidate.getString("lastName"));
                    president.setRunningMateName(candidate.getString("runningMateName"));
                    president.setOfficeParties(candidate.getString("party"));
                    president.setRunningMateId(candidate.getString("runningMateId"));
                    presidentDetails.add(president);
                }
            }

        }
        return presidentDetails;
    }

    private void createCandidateList( QuestionField field, String state, String district, VoteSmartMethodMeta apiMethod ) {
        if ( state == null || state.length() == 0 ) throw new IllegalArgumentException();

        field.setGenericOptions( new LinkedList<FieldDictionaryItem>() );

        // MN requires candidate info be all upper-case text
        boolean doUppercase = false;
        // if(state.equalsIgnoreCase("MN")) doUppercase= true;

        final String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        Map<String,String> model = new HashMap<String, String>();
        // set additional request arguments
        if ( state.length() != 0 )
            model.put( "stateId", state );

        switch ( apiMethod ) {
            case PRESIDENT_LIST :
                String electionId = findPresidentialElectionId();
                model.put( "electionId", electionId );          //"officeId", "1"
                model.put( "stageId", "G" );
                break;
            case PRESIDENT_PRIMARY_LIST:
                electionId = findPresidentialElectionId();
                model.put( "electionId", electionId );          //"officeId", "1"
                model.put( "stageId", "P" );
            case SENATE_LIST:
            case SENATE_SPECIAL_LIST:
                model.put( "officeId", "6"/*officeId*/ );
                model.put( "electionYear", year );
            case REPRESENTATIVE_LIST:
            case REPRESENTATIVE_SPECIAL_LIST:
                model.put( "officeId", "5"/*officeId*/ );
                model.put( "electionYear", year );
        }

        String candidatesFileName = saveAndCacheFile(apiMethod, model);
        if ( candidatesFileName == null ) {
            return;
        }

        Document candidatesDoc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File candidateFile = new File( candidatesFileName );
            candidatesDoc = builder.parse( candidateFile );
        } catch ( Exception e ) {
            voteSmartLog.error( "Error parsing candidate file " + candidatesFileName, e );
            return;
        }

        String[] inquiredStatus = new String[]{"Running"};
        String inquiredStatusName = "electionStatus";
        String partyName = "electionParties";
        if ( apiMethod == VoteSmartMethodMeta.PRESIDENT_LIST || apiMethod == VoteSmartService.VoteSmartMethodMeta.PRESIDENT_PRIMARY_LIST ) {
            //inquiredStatus = "Announced";  //Status for president
            inquiredStatus = new String[]{"Running","Potential"};
            inquiredStatusName = "status";
            partyName = "party";
            //TODO:  this line removes presidential list form the form. 
            // return;
        }

        try {
            List<Element> candidates = DomUtils.getChildElementsByTagName( candidatesDoc.getDocumentElement(), "candidate" );

            for ( Element candidate : candidates ) {
                for ( String inqStatus : inquiredStatus ) {

                    String status = DomUtils.getChildElementValueByTagName( candidate, inquiredStatusName );

                    if ( inqStatus.equalsIgnoreCase( status ) ) { // check status
                        //NodeList candidateProps = candidate.getChildNodes();

                        String firstName = DomUtils.getChildElementValueByTagName( candidate, "firstName" );
                        String lastName = DomUtils.getChildElementValueByTagName( candidate, "lastName" );
                        String middleName = DomUtils.getChildElementValueByTagName( candidate, "middleName" );
                        String electionDistrictName = DomUtils.getChildElementValueByTagName( candidate, "electionDistrictName" );
                        String runningMateName = DomUtils.getChildElementValueByTagName( candidate, "runningMateName" );
                        String party = DomUtils.getChildElementValueByTagName( candidate, partyName );
                        String isSpecialElection = DomUtils.getChildElementValueByTagName( candidate, "electionSpecial" );
                        String isPrimaryElection = DomUtils.getChildElementValueByTagName( candidate, "electionStage" );
                        String electionOfficeId = DomUtils.getChildElementValueByTagName( candidate, "electionOfficeId" );

                        // check for primary voting
                        boolean containsPrimary = isPrimaryElection != null && isPrimaryElection.toLowerCase().contains( "primary" );
                        //president primary
                        if ( apiMethod == VoteSmartMethodMeta.PRESIDENT_LIST || apiMethod == VoteSmartMethodMeta.PRESIDENT_PRIMARY_LIST ) {
                            if ( !(apiMethod == VoteSmartMethodMeta.PRESIDENT_LIST ^ containsPrimary) ) {
                                continue;
                            }
                            // if method is PRESIDENT and stage contains 'primary' - ignore the line
                            // if method is PRIMARY and stage does not contain 'primary' - also ignore
                        }
                        //other primary
                        if ( apiMethod == VoteSmartMethodMeta.SENATE_LIST
                                || apiMethod == VoteSmartMethodMeta.SENATE_SPECIAL_LIST
                                || apiMethod == VoteSmartMethodMeta.SENATE_PRIMARY_LIST ) {
                            if ( !(apiMethod != VoteSmartMethodMeta.SENATE_PRIMARY_LIST ^ containsPrimary) ) {
                                continue;
                            }
                            // if method is SENATE or SPECIAL and stage contains 'primary' - ignore the line
                            // if method is PRIMARY and stage does not contain 'primary' - also ignore
                        }
                        if ( apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_LIST
                                || apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_SPECIAL_LIST
                                || apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_PRIMARY_LIST ) {
                            if ( !(apiMethod != VoteSmartMethodMeta.REPRESENTATIVE_PRIMARY_LIST ^ containsPrimary) ) {
                                continue;
                            }
                            // if method is REPRESENTATIVE or SPECIAL and stage contains 'primary' - ignore the line
                            // if method is PRIMARY and stage does not contain 'primary' - also ignore
                        }


                        // if looking for candidates of a specific district, skip ones not in that district
                        if ( district.length() != 0 && electionDistrictName != null ) {  // check district name
                            if ( !electionDistrictName.equals( district ) & !electionDistrictName.equalsIgnoreCase( "At-Large" ) ) {
                                continue;
                            }
                        }

                        // ignore special election candidates unless we are specifically looking for them
                        if ( (isSpecialElection != null && isSpecialElection.equalsIgnoreCase( "t" )) ^ (apiMethod == VoteSmartMethodMeta.SENATE_SPECIAL_LIST || apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_SPECIAL_LIST) ) {
                            continue;
                        }

                        final String fullName = firstName + " " + middleName + " " + lastName;
                        String viewName = fullName;
                        if ( runningMateName != null && runningMateName.length() > 0 ) {
                            viewName += " / " + runningMateName;
                        }
                        viewName += " {" + party + "}";
                        if ( doUppercase ) viewName = fullName.toUpperCase();
                        String optionName = viewName.replace("{", "(").replace("}",")");

                        FieldDictionaryItem item = new VirtualDictionaryItem();
                        item.setForField( field );
                        if (apiMethod == VoteSmartMethodMeta.SENATE_PRIMARY_LIST) {
                            item.setValue(viewName + "=U.S. Senator (Primary Election)|" + fullName);
                        } else if (apiMethod == VoteSmartMethodMeta.SENATE_SPECIAL_LIST) {
                            item.setValue(viewName + "=U.S. Senator (Special Election)|" + fullName);
                        } else if (apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_PRIMARY_LIST) {
                            item.setValue(viewName + "=U.S. Representative (Primary Election)|" + fullName);
                        } else if (apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_SPECIAL_LIST) {
                            item.setValue(viewName + "=U.S. Representative (Special Election)|" + fullName);
                        } else {
                            item.setValue( viewName + "=" + optionName );
                        }
                        field.getOptions().add( item );
                        break;
                    }
                }
            }
        } catch ( Exception e ) {
            voteSmartLog.info( "Exception reading file " + candidatesFileName + ":", e );
        }
    }

    @SuppressWarnings("unchecked")
    public CandidateBio getCandidateBio( String candidateId ) {
        if ( candidateId == null || candidateId.length() == 0 ) throw new IllegalArgumentException();

        String fileName = "<unsaved>";
        try {
            Map<String,String> model = new HashMap<String, String>();
            model.put( "candidateId", candidateId );
            fileName = saveAndCacheFile( VoteSmartMethodMeta.CANDIDATE_DETAILED_BIO, model );
            if ( fileName == null ) {
                return null;
            }

            XMLConfiguration xmlReader = new XMLConfiguration();
            xmlReader.setDelimiterParsingDisabled( true );
            xmlReader.load( fileName );

            String error = xmlReader.getString( "errorMessage", "" );
            if ( error != null && error.length() > 0 ) {
                voteSmartLog.error( "GetCandidateBio: Error occured for candidateId=" + candidateId + ", error=" + error );
                return null;
            }

            CandidateBio cb = new CandidateBio();
            cb.setId( xmlReader.getString( "candidate.candidateId", "" ) );
            if ( cb.getId() == null ) return null;

            cb.setOpenSecretID( xmlReader.getString( "candidate.crpId", "" ) );
            cb.setPhoto( xmlReader.getString( "candidate.photo", "" ) );
            cb.setFirstName( xmlReader.getString( "candidate.firstName", "" ) );
            cb.setNickName( xmlReader.getString( "candidate.nickName", "" ) );
            cb.setMiddleName( xmlReader.getString( "candidate.middleName", "" ) );
            cb.setLastName( xmlReader.getString( "candidate.lastName", "" ) );
            cb.setSuffix( xmlReader.getString( "candidate.suffix", "" ) );
            cb.setBirthDate( xmlReader.getString( "candidate.birthDate", "" ) );
            cb.setBirthPlace( xmlReader.getString( "candidate.birthPlace", "" ) );
            cb.setPronunciation( xmlReader.getString( "candidate.pronunciation", "" ) );
            cb.setGender( xmlReader.getString( "candidate.gender", "" ) );
            cb.setFamily( xmlReader.getString( "candidate.family", "" ) );
            cb.setHomeCity( xmlReader.getString( "candidate.homeCity", "" ) );
            cb.setHomeState( xmlReader.getString( "candidate.homeState", "" ) );
            cb.setReligion( xmlReader.getString( "candidate.religion", "" ) );
            List<HierarchicalConfiguration> educations = xmlReader.configurationsAt("candidate.education.institution");
			if (educations != null && !educations.isEmpty()) {
				cb.setEducation("");
				for (HierarchicalConfiguration education : educations) {
					cb.setEducation(cb.getEducation() + "\n"
							+ education.getString("fullText", ""));
				}
			}
			List<HierarchicalConfiguration> professions = xmlReader.configurationsAt("candidate.profession.experience");
			if (professions != null && !professions.isEmpty()) {
				cb.setProfession("");
				for (HierarchicalConfiguration profession : professions) {
					cb.setProfession(cb.getProfession() + "\n" + profession.getString("fullText", ""));
				}
			}
			List<HierarchicalConfiguration> politicals = xmlReader.configurationsAt("candidate.political.experience");
			if (politicals != null && !politicals.isEmpty()) {
				cb.setPolitical("");
				for (HierarchicalConfiguration political : politicals) {
					cb.setPolitical(cb.getPolitical() + "\n" + political.getString("fullText", ""));
				}
			}
			
			List<HierarchicalConfiguration> congMemberships = xmlReader.configurationsAt("candidate.congMembership.experience");
			if (congMemberships != null && !congMemberships.isEmpty()) {
				cb.setCongMembership("");
				for (HierarchicalConfiguration congMembership : congMemberships) {
					cb.setCongMembership(cb.getCongMembership() + "\n" + congMembership.getString("fullText", ""));
				}
			}

			List<HierarchicalConfiguration> orgMemberships = xmlReader.configurationsAt("candidate.orgMembership.experience");
			if (orgMemberships != null && !orgMemberships.isEmpty()) {
				cb.setOrgMembership("");
				for (HierarchicalConfiguration orgMembership : orgMemberships) {
					cb.setOrgMembership(cb.getOrgMembership() + "\n" + orgMembership.getString("fullText", ""));
				}
			}

            cb.setSpecialMsg( xmlReader.getString( "candidate.specialMsg", "" ) );

            CandidateOffice co = new CandidateOffice();
            cb.setOffice( co );

            co.setName( xmlReader.getString( "office.name", "" ) );
            co.setParties( xmlReader.getString( "office.parties", "" ) );
            co.setTitle( xmlReader.getString( "office.title", "" ) );
            co.setShortTitle( xmlReader.getString( "office.shortTitle", "" ) );
            co.setType( xmlReader.getString( "office.type", "" ) );
            co.setStatus( xmlReader.getString( "office.status", "" ) );
            co.setFirstElect( xmlReader.getString( "office.firstElect", "" ) );
            co.setLastElect( xmlReader.getString( "office.lastElect", "" ) );
            co.setNextElect( xmlReader.getString( "office.nextElect", "" ) );
            co.setTermStart( xmlReader.getString( "office.termStart", "" ) );
            co.setTermEnd( xmlReader.getString( "office.termEnd", "" ) );
            co.setDistrict( xmlReader.getString( "office.district", "" ) );
            co.setStateId( xmlReader.getString( "office.stateId", "" ) );

            // process office.committee
            Object objComettee = xmlReader.getProperty( "office.committee.committeeId" );
            if ( objComettee instanceof List ) {

                List<String> committeeIds = (List<String>) objComettee;
                List<String> committeeNames = (List<String>) xmlReader.getProperty( "office.committee.committeeName" );
                if ( committeeIds != null && committeeNames != null && committeeIds.size() == committeeNames.size() ) {
                    List<Committee> committees = new ArrayList<Committee>();
                    for ( int i = 0; i < committeeIds.size(); i++ ) {
                        committees.add( new Committee( committeeIds.get( i ), committeeNames.get( i ) ) );
                    }
                    co.setCommittees( committees );
                }
            }
            else if ( objComettee instanceof String ) {
                List<Committee> committees = new ArrayList<Committee>();
                committees.add( new Committee( (String) objComettee, (String) xmlReader.getProperty( "office.committee.committeeName" )  ) );
                co.setCommittees( committees );
            }
            // process office.election
            List<Election> elections = new ArrayList<Election>();
            Object objOffice = xmlReader.getProperty( "election.office" );
            if ( objOffice instanceof List ) {
                List<String> office = (List<String>) objOffice;
                List<String> officeId = (List<String>) xmlReader.getProperty( "election.officeId" );
                List<String> officeType = (List<String>) xmlReader.getProperty( "election.officeType" );
                List<String> parties = (List<String>) xmlReader.getProperty( "election.parties" );
                List<String> district = (List<String>) xmlReader.getProperty( "election.district" );
                List<String> status = (List<String>) xmlReader.getProperty( "election.status" );
                List<String> ballotName = (List<String>) xmlReader.getProperty( "election.ballotName" );
                if ( office != null && officeId != null && officeType != null && parties != null && district != null && status != null && ballotName != null ) {
                    for ( int i = 0; i < office.size(); i++ ) {
                        elections.add( new Election(
                                officeId.get( i ), office.get( i ), officeType.get( i ), parties.get( i ),
                                district.get( i ), status.get( i ), ballotName.get( i ) ) );
                    }
                }
            } else if ( objOffice instanceof String ) {
                elections.add( new Election(
                        xmlReader.getString( "election.officeId" ),
                        (String) objOffice,
                        xmlReader.getString( "election.officeType" ),
                        xmlReader.getString( "election.parties" ),
                        xmlReader.getString( "election.district" ),
                        xmlReader.getString( "election.status" ),
                        xmlReader.getString( "election.ballotName" ) ) );
            }
            co.setElections( elections );
            return cb;

        } catch ( Exception e ) {
            voteSmartLog.error( "GetCandidateBio: Exception, file=" + fileName + ", exception:\n" + e );
            return null;
        }
    }

    public CandidateAdditionalBio getCandidateAdditionalBio( String candidateId ) {
        if ( candidateId == null || candidateId.length() == 0 ) throw new IllegalArgumentException();

        String fileName = "<unsaved>";
        try {
            Map<String,String> model = new HashMap<String, String>();
            model.put( "candidateId", candidateId );
            fileName = saveAndCacheFile(VoteSmartMethodMeta.CANDIDATE_ADDL_BIO, model);
            if ( fileName == null ) {
                return null;
            }

            XMLConfiguration xmlReader = new XMLConfiguration();
            xmlReader.setDelimiterParsingDisabled( true );
            xmlReader.load( fileName );

            String error = xmlReader.getString( "errorMessage", "" );
            if ( error != null && error.length() > 0 ) {
                voteSmartLog.error( "GetCandidateAdditionalBio: Error occured for candidateId=" + candidateId + ", error=" + error );
                return null;
            }

            CandidateAdditionalBio cab = new CandidateAdditionalBio();

            cab.setShortTitle( xmlReader.getString( "candidate.shortTitle", "" ) );
            cab.setFirstName( xmlReader.getString( "candidate.firstName", "" ) );
            cab.setNickName( xmlReader.getString( "candidate.nickName", "" ) );
            cab.setMiddleName( xmlReader.getString( "candidate.middleName", "" ) );
            cab.setLastName( xmlReader.getString( "candidate.lastName", "" ) );
            cab.setSuffix( xmlReader.getString( "candidate.suffix", "" ) );

            List<String> names = (List<String>) xmlReader.getProperty( "additional.item.name" );
            List<String> data = (List<String>) xmlReader.getProperty( "additional.item.data" );
            if ( names != null && data != null && names.size() == data.size() ) {
                List<AdditionalItem> additionalItems = new ArrayList<AdditionalItem>();
                for ( int i = 0; i < names.size(); i++ ) {
                    additionalItems.add( new AdditionalItem( names.get( i ), data.get( i ) ) );
                }
                cab.setAdditionalItems( additionalItems );
            }

            return cab;

        } catch ( Exception e ) {
            voteSmartLog.error( "GetCandidateAdditionalBio: Exception occured, file=" + fileName + ", exception:\n" + e );
            return null;
        }
    }

    public List<CandidateZip> getCandidatesByZip( String zip5, String zip4 ) throws IllegalArgumentException {
        return this.getCandidatesByZip( zip5, zip4, null );
    }

    public List<CandidateZip> getCandidatesByZip( String zip5, String zip4, String electionYear ) throws IllegalArgumentException {
        if ( zip5 == null || zip5.trim().length() == 0 ) throw new IllegalArgumentException();
        Map<String,String> model = new HashMap<String, String>();
        model.put( "zip5", zip5 );
        model.put( "zip4", zip4 != null ? zip4 : "");
        model.put( "electionYear", electionYear != null ? electionYear : String.valueOf( Calendar.getInstance().get( Calendar.YEAR ) ) );

        String fileName = "<unsaved>";
        try {
            if ( testMode && testCandidatesFileName.length() > 0 ) {
                File testFile = new File( testCandidatesFileName );
                if ( testFile.exists() ) {
                    fileName = testCandidatesFileName;
                }
            }
            if ( fileName.equals("<unsaved>") ) {
                fileName = saveAndCacheFile(VoteSmartMethodMeta.CANDIDATES_GET_BY_ZIP, model);
            }
            if ( fileName == null ) {
                return Collections.emptyList();
            }

            XMLConfiguration xmlReader = new XMLConfiguration();
            xmlReader.setDelimiterParsingDisabled( true );
            xmlReader.load( fileName );

            String error = xmlReader.getString( "errorMessage", "" );
            if ( error != null && error.length() > 0 ) {
                voteSmartLog.error( "GetCandidatesByZip: Error occurred for zip5=" + zip5 +
                        ", year=" + electionYear +
                        ", error=" + error );
                return Collections.emptyList();
            }


            List<CandidateZip> candidates = new ArrayList<CandidateZip>();

            Object vsCandidates = xmlReader.getProperty( "candidate.candidateId" );
            if ( vsCandidates instanceof Collection ) {
                for ( int i = 0; i < ((Collection) vsCandidates).size(); i++ ) {
                    String prefix = String.format( "candidate(%d).", i );
                    CandidateZip cz = new CandidateZip();
                    cz.setCandidateId( xmlReader.getString( prefix + "candidateId", "" ) );
                    cz.setFirstName( xmlReader.getString( prefix + "firstName", "" ) );
                    cz.setNickName( xmlReader.getString( prefix + "nickName", "" ) );
                    cz.setMiddleName( xmlReader.getString( prefix + "middleName", "" ) );
                    cz.setLastName( xmlReader.getString( prefix + "lastName", "" ) );
                    cz.setSuffix( xmlReader.getString( prefix + "suffix", "" ) );
                    cz.setTitle( xmlReader.getString( prefix + "title", "" ) );
                    cz.setElectionParties( xmlReader.getString( prefix + "electionParties", "" ) );
                    cz.setElectionDistrictId( xmlReader.getString( prefix + "electionDistrictId", "" ) );
                    cz.setElectionDistrictName( xmlReader.getString( prefix + "electionDistrictName", "" ) );
                    cz.setElectionOffice( xmlReader.getString( prefix + "electionOffice", "" ) );
                    cz.setElectionOfficeId( xmlReader.getString( prefix + "electionOfficeId", "" ) );
                    cz.setElectionStateId( xmlReader.getString( prefix + "electionStateId", "" ) );
                    cz.setElectionStatus( xmlReader.getString( prefix + "electionStatus", "" ) );
                    cz.setElectionStage( xmlReader.getString( prefix + "electionStage", "" ) );
                    cz.setOfficeParties( xmlReader.getString( prefix + "officeParties", "" ) );
                    cz.setBallotName( xmlReader.getString( prefix + "ballotName", "" ) );
                    cz.setElectionSpecial( xmlReader.getString( prefix + "electionSpecial", "" ) );
                    cz.setRunningMateName( xmlReader.getString( prefix + "runningMateName", "" ) );
                    candidates.add( cz );
                }
            }

            return candidates;
        } catch ( Exception e ) {
            voteSmartLog.error( "getCandidatesByZip: Exception occurred, file=" + fileName + ", exception:\n" + e );
            return Collections.emptyList();
        }
    }


    public List<CandidateZip> getOfficialsByZip( String zip5, String zip4 ) {
        if ( zip5 == null || zip5.trim().length() == 0 ) throw new IllegalArgumentException();
        Map<String,String> model = new HashMap<String, String>();
        model.put( "zip5", zip5 );
        model.put( "zip4", zip4 != null ? zip4 : "");
        model.put( "electionYear", String.valueOf( Calendar.getInstance().get( Calendar.YEAR ) ) );

        String fileName = "<unsaved>";
        try {

            fileName = saveAndCacheFile(VoteSmartMethodMeta.OFFICIALS_GET_BY_ZIP, model);
            if ( fileName == null ) {
                return Collections.emptyList();
            }

            XMLConfiguration xmlReader = new XMLConfiguration();
            xmlReader.setDelimiterParsingDisabled( true );
            xmlReader.load( fileName );

            String error = xmlReader.getString( "errorMessage", "" );
            if ( error != null && error.length() > 0 ) {
                voteSmartLog.error( "GetOfficialsByZip: Error occurred for zip5=" + zip5 +
                        ", error=" + error );
                return Collections.emptyList();
            }


            List<CandidateZip> candidates = new ArrayList<CandidateZip>();

            Object vsCandidates = xmlReader.getProperty( "candidate.candidateId" );
            if ( vsCandidates instanceof Collection ) {
                for ( int i = 0; i < ((Collection) vsCandidates).size(); i++ ) {
                    String prefix = String.format( "candidate(%d).", i );
                    CandidateZip cz = new CandidateZip();
                    cz.setCandidateId( xmlReader.getString( prefix + "candidateId", "" ) );
                    cz.setFirstName( xmlReader.getString( prefix + "firstName", "" ) );
                    cz.setNickName( xmlReader.getString( prefix + "nickName", "" ) );
                    cz.setMiddleName( xmlReader.getString( prefix + "middleName", "" ) );
                    cz.setLastName( xmlReader.getString( prefix + "lastName", "" ) );
                    cz.setSuffix( xmlReader.getString( prefix + "suffix", "" ) );
                    cz.setTitle( xmlReader.getString( prefix + "title", "" ) );
                    cz.setElectionParties( xmlReader.getString( prefix + "electionParties", "" ) );
                    cz.setElectionDistrictId( xmlReader.getString( prefix + "officeDistrictId", "" ) );
                    cz.setElectionDistrictName( xmlReader.getString( prefix + "officeDistrictName", "" ) );
                    cz.setElectionOffice( xmlReader.getString( prefix + "officeName", "" ) );
                    cz.setElectionOfficeId( xmlReader.getString( prefix + "officeId", "" ) );
                    cz.setElectionStateId( xmlReader.getString( prefix + "officeStateId", "" ) );
                    cz.setElectionStatus( xmlReader.getString( prefix + "electionStatus", "" ) );
                    cz.setElectionStage( xmlReader.getString( prefix + "electionStage", "" ) );
                    cz.setOfficeParties( xmlReader.getString( prefix + "officeParties", "" ) );
                    cz.setBallotName( xmlReader.getString( prefix + "ballotName", "" ) );
                    cz.setElectionSpecial( xmlReader.getString( prefix + "electionSpecial", "" ) );
                    cz.setRunningMateName( xmlReader.getString( prefix + "runningMateName", "" ) );
                    candidates.add( cz );
                }
            }

            return candidates;

        } catch ( Exception e ) {
            voteSmartLog.error( "getCandidatesByZip: Exception occurred, file=" + fileName + ", exception:\n" + e );
            return Collections.emptyList();
        }
    }

    private boolean useVoteSmartAPI( VoteSmartMethodMeta method ) {
        switch ( method ) {
            case PRESIDENT_LIST:
                return useVotesmartPresidental;
            case PRESIDENT_PRIMARY_LIST:
                return useVotesmartPresidental;
            case SENATE_LIST:
                return useVotesmartSenate;
            case SENATE_PRIMARY_LIST:
            case SENATE_SPECIAL_LIST:
                return useVotesmartSenate;
            case REPRESENTATIVE_LIST:
                return useVotesmartRepresentatives;
            case REPRESENTATIVE_PRIMARY_LIST:
            case REPRESENTATIVE_SPECIAL_LIST:
                return useVotesmartRepresentatives;
            case CANDIDATE_BIO:
                return useVotesmartCandidateBio;
            case CANDIDATE_ADDL_BIO:
                return useVotesmartCandidateAdditionalBio;
            case CANDIDATES_GET_BY_ZIP:
            case OFFICIALS_GET_BY_ZIP:
                return useVotesmartCandidatesByDistrict;
            case VOTES_GET_CATEGORIES:
            case ELECTIONS:
                return false;
            case VOTES_GET_BY_OFFICIALS:
                return true;
            case CANDIDATE_DETAILED_BIO:
            	return true;

        }
        throw new IllegalArgumentException( "Invalid VoteSmart method '" + method + "'" );
    }

    private boolean useCacheFile( File cacheFile, boolean forceCache ) {
    	if (!cacheFile.exists()) {
    		return false;
    	}
        final XMLConfiguration xmlReader = new XMLConfiguration();
        xmlReader.setDelimiterParsingDisabled( true );
        try {
			xmlReader.load(cacheFile);
			final String keepUntilString = xmlReader.getString("keepUntil");
			if (keepUntilString != null) {
				final Date keepUntil = KEEP_UNTIL_FORMAT.parse(keepUntilString);
				final Date now = new Date();
				return keepUntil.after(now);
			}
		} catch (final ConfigurationException e) {
			return false;
		} catch (final ParseException e) {
			return false;
		}
        return forceCache || (cacheFile.lastModified() > (System.currentTimeMillis() - votesmartCacheTime));
    }

    private void createCacheFile( final File cacheFile, String response ) throws IOException {
        FileOutputStream fos = null;
        try {
            // if it's a valid response, then write it into the	cache file
            if ( response != null && response.trim().length() > 0 ) {
                if ( (VS_API_OUTPUT_FORMAT.equalsIgnoreCase( "xml" ) && response.startsWith( "<?xml" )) ||
                        (VS_API_OUTPUT_FORMAT.equalsIgnoreCase( "json" ) && response.startsWith( "{" )) ) {
                    fos = new FileOutputStream( cacheFile );
                    fos.write( response.getBytes( "UTF-8" ) );
                }
            }
        } finally {
            if ( fos != null ) {
                fos.close();
                fos = null;
            }
        }
    }

    // create necessary arguments for vs api
    private LinkedList<NameValuePair> createDefaultArgs() {
        LinkedList<NameValuePair> pairs = new LinkedList<NameValuePair>();
        pairs.add( new NameValuePair( "key", votesmartKey ) );
        pairs.add( new NameValuePair( "o", VS_API_OUTPUT_FORMAT ) );
        return pairs;
    }

    // execute VS API request
    private String execute( final String method, List<NameValuePair> args ) throws IOException {
        HttpClient httpClient = new HttpClient();
        GetMethod m = new GetMethod( VS_API_URL + method );
        m.setQueryString( args.toArray( new NameValuePair[args.size()] ) );
        httpClient.executeMethod( m );
        return IOUtils.toString( m.getResponseBodyAsStream(), "UTF-8" );
    }

	/**
	 * Retrieves the XML string containing the candidates for the specified ZIP code.
	 * @author IanBrown
	 * @param zip the ZIP code.
	 * @param zip4 the optional ZIP+4.
	 * @return the candidates string.
	 * @throws IOException if there is a problem reading the data.
	 * @since Sep 11, 2012
	 * @version Sep 12, 2012
	 */
	public String retrieveCandidatesByZip(String zip, String zip4) throws IOException {
        Map<String,String> model = new HashMap<String, String>();
        model.put( "zip5", zip );
        model.put( "zip4", zip4 != null ? zip4 : "");
        model.put( "electionYear", String.valueOf( Calendar.getInstance().get( Calendar.YEAR ) ) );
		final String fileName = saveAndCacheFile(VoteSmartMethodMeta.CANDIDATES_GET_BY_ZIP, model);
		return (fileName == null) ? null : readFileAsString(fileName);
	}

	/**
	 * Reads the file specified by the path as a string.
	 * @author IanBrown
	 * @param filePath the file path.
	 * @return the string.
	 * @throws java.io.IOException if there s a problem reading the file.
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	private static String readFileAsString(String filePath) throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
		reader.close();
		return fileData.toString();
	}
	
	/**
	 * @author IanBrown
	 * @param candidateId
	 * @return
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	public String retrieveCandidateBio(long candidateId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

	/**
	 * @author IanBrown
	 * @param candidateId
	 * @return
	 * @since Sep 12, 2012
	 * @version Sep 12, 2012
	 */
	public String retrieveCandidateAdditionalBio(long candidateId) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException("Not implemented yet");
	}

    private String findPresidentialElectionId() {
        Map<String,String> model = new HashMap<String, String>();
        model.put( "year", String.valueOf( Calendar.getInstance().get( Calendar.YEAR ) ) );
        String electionsFileName = saveAndCacheFile( VoteSmartMethodMeta.ELECTIONS, model );
        if ( electionsFileName == null ) {
            return "";
        }

        Document electionsDoc = null;
        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            File electionsFile = new File( electionsFileName );
            electionsDoc = builder.parse( electionsFile );
        } catch ( Exception e ) {
            voteSmartLog.error( "Error parsing election file " + electionsFileName, e );
            return "";
        }

        String inquiredOfficeType = "P";
        String inquiredOfficeTypeName = "officeTypeId";

        String electionId = "";
        try {
            List<Element> elections = DomUtils.getChildElementsByTagName( electionsDoc.getDocumentElement(), "election" );

            for ( Element election : elections ) {

                String status = DomUtils.getChildElementValueByTagName( election, inquiredOfficeTypeName );

                if ( inquiredOfficeType.equalsIgnoreCase( status ) ) { // check status
                    electionId = DomUtils.getChildElementValueByTagName( election, "electionId" );
                }
            }

        } catch ( Exception e ) {
            voteSmartLog.info( "Exception reading file " + electionsFileName + ":", e );
        }
        return electionId;
    }

    public void createCandidateZipList( QuestionField field, String zip5, String zip4, String officeId, boolean primary, boolean special ) {
        field.setGenericOptions( new LinkedList<FieldDictionaryItem>() );

        List<CandidateZip> candidates = getCandidatesByZip( zip5, zip4 );
        for ( CandidateZip candidate : candidates ) {
            if ( !candidate.getElectionStatus().equals( "Running" ) ) continue;
            if ( primary && !candidate.getElectionStage().toLowerCase().contains( "primary" ) ) {
                continue;
            }
            if ( special && !candidate.getElectionSpecial().equalsIgnoreCase( "t" ) ) {
                continue;
            }
            if ( !primary && !special && !candidate.getElectionStage().toLowerCase().contains( "general" ) ) {
                continue;
            }
            if ( !candidate.getElectionOfficeId().equals( officeId ) ) {
                continue;
            }

            final String fullName = candidate.getFirstName() + " "
                    + candidate.getMiddleName() + " "
                    + candidate.getLastName();
            String viewName = fullName;
            if ( candidate.getRunningMateName() != null && candidate.getRunningMateName().length() > 0 ) {
                viewName += " / " + candidate.getRunningMateName();
            }
            viewName += " {" + candidate.getElectionParties() + "}";
            String optionName = viewName.replace("{", "(").replace("}",")");

            FieldDictionaryItem item = new VirtualDictionaryItem();
            item.setForField( field );
            if ( officeId.equals( "6" ) && primary ) {  //apiMethod == VoteSmartMethodMeta.SENATE_PRIMARY_LIST
                item.setValue(viewName + "=U.S. Senator (Primary Election)|" + fullName);
            } else if ( officeId.equals( "6" ) && special ) {  //apiMethod == VoteSmartMethodMeta.SENATE_SPECIAL_LIST
                item.setValue(viewName + "=U.S. Senator (Special Election)|" + fullName);
            } else if ( officeId.equals( "5" ) && primary ) { //apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_PRIMARY_LIST
                item.setValue(viewName + "=U.S. Representative (Primary Election)|" + fullName);
            } else if ( officeId.equals( "5" ) && special ) { //apiMethod == VoteSmartMethodMeta.REPRESENTATIVE_SPECIAL_LIST
                item.setValue(viewName + "=U.S. Representative (Special Election)|" + fullName);
            }  else if ( officeId.equals( "1" ) && primary ) { //President primary
                item.setValue(viewName + "=U.S. President (Primary Election)|" + fullName);
            }
            else {
                item.setValue( viewName + "=" + optionName );
            }
            field.getOptions().add( item );
        }
    }

     public void createSenateZipList( QuestionField field, String zip, String zip4 ) {
         this.createCandidateZipList( field, zip, zip4, "6", false, false );
     }

     public void createSenateSpecialZipList( QuestionField field, String zip, String zip4 ) {
         this.createCandidateZipList( field, zip, zip4, "6", false, true );
     }

     public void createRepresentativeZipList( QuestionField field, String zip, String zip4 ) {
         this.createCandidateZipList( field, zip, zip4, "5", false, false );
     }

     public void createRepresentativeSpecialZipList( QuestionField field, String zip, String zip4 ) {
         this.createCandidateZipList( field, zip, zip4, "5", false, true );
     }


     public void createRepresentativePrimaryZipList( QuestionField field, String zip, String zip4 ) {
         this.createCandidateZipList( field, zip, zip4, "5", true, false );
     }

     public void createSenatePrimaryZipList( QuestionField field, String zip, String zip4 ) {
         this.createCandidateZipList( field, zip, zip4, "6", true, false );
     }

    public void createPresidentZipList( QuestionField field, String zip, String zip4 ) {
        this.createCandidateZipList( field, zip, zip4, "1", false, false );
    }

    public void createPresidentPrimaryZipList( QuestionField field, String zip, String zip4 ) {
        this.createCandidateZipList( field, zip, zip4, "1", true, false );
    }

    private String saveAndCacheFile( VoteSmartMethodMeta method, Map<String,String> model ) {

        Object[] fileNameArgs = new Object[ method.getArguments().length +1 ];
        fileNameArgs[0] = getVotesmartCacheDir();
        for( int i = 0; i < method.getArguments().length; i++ ) {
            String value = model.get( method.getArguments()[i] );
            fileNameArgs[i+1] = (value != null && value.length() >0 ) ? value : "";
        }
        // initialize a cache file name
        final String cacheFileName = method.getCacheFileName( fileNameArgs );
        if ( cacheFileName == null ) return null;

        final File cacheFile = new File( cacheFileName );
        if ( useCacheFile( cacheFile, !useVoteSmartAPI( method ) ) )
            return cacheFileName;

        // create default request arguments
        final List<NameValuePair> args = createDefaultArgs();
        for ( String key : model.keySet() ) {
            args.add( new NameValuePair( key, model.get( key ) ) );
        }

        try {
            createCacheFile( cacheFile, execute( method.getMethod(), args ) );
        } catch ( IOException e ) {
            voteSmartLog.error( "Cannot create cache file '" + cacheFileName + "' to VoteSmart Service Cache Directory :" + e );
            if ( !cacheFile.exists() ) {
                return null;
            }
        }
        return cacheFileName;
    }

    @SuppressWarnings("unchecked")
    public List<VotesCategory> getVotesCategories( ) {
        String fileName = "<unsaved>";
        try {
            Map<String,String> model = new HashMap<String, String>();
            model.put( "year", String.valueOf( Calendar.getInstance().get( Calendar.YEAR ) ) );
            fileName = saveAndCacheFile( VoteSmartMethodMeta.VOTES_GET_CATEGORIES, model );
            if ( fileName == null ) {
                return null;
            }

            XMLConfiguration xmlReader = new XMLConfiguration();
            xmlReader.setDelimiterParsingDisabled( true );
            xmlReader.load( fileName );

            String error = xmlReader.getString( "errorMessage", "" );
            if ( error != null && error.length() > 0 ) {
                voteSmartLog.error( "GetVotesCategories: Error occured " + error );
                return null;
            }

            // process
            List<String> categoryIds = (List<String>) xmlReader.getProperty( "category.categoryId" );
            List<String> categoryNames = (List<String>) xmlReader.getProperty( "category.name" );
            List<VotesCategory> categories = new ArrayList<VotesCategory>();
            if ( categoryIds != null && categoryNames != null && categoryIds.size() == categoryNames.size() ) {
                for ( int i = 0; i < categoryIds.size(); i++ ) {
                    categories.add( new VotesCategory( categoryIds.get( i ), categoryNames.get( i ) ) );
                }
            }

            return categories;
        } catch ( Exception e ) {
            voteSmartLog.error( "GetVotesCategories: Exception, file=" + fileName + ", exception:\n" + e );
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<Bill> getVotesHistory( String candidateId, String categoryId, String year ) {
        String fileName = "<unsaved>";
        try {
            Map<String,String> model = new HashMap<String, String>();
            model.put( "candidateId", candidateId );
            model.put( "categoryId", categoryId );
            model.put( "year", year );
            fileName = saveAndCacheFile( VoteSmartMethodMeta.VOTES_GET_BY_OFFICIALS, model );
            if ( fileName == null ) {
                return null;
            }

            XMLConfiguration xmlReader = new XMLConfiguration();
            xmlReader.setDelimiterParsingDisabled( true );
            xmlReader.load( fileName );

            String error = xmlReader.getString( "errorMessage", "" );
            if ( error != null && error.length() > 0 ) {
                voteSmartLog.error( "GetVotesHistory: Error occured " + error );
                return null;
            }

            final ConfigurationNode rootNode = xmlReader.getRootNode();
            if (rootNode.getChildrenCount() == 0) {
                return Collections.emptyList();
            }
            List<Bill> bills = new ArrayList<Bill>();
            final List<?> billChildren = rootNode.getChildren("bill");
            for (final Object billChild : billChildren) {
                final ConfigurationNode billNode = (ConfigurationNode) billChild;
                final String billId = retrieveNodeValue( billNode, "billId");
                final String billNumber = retrieveNodeValue( billNode, "billNumber");
                final String title = retrieveNodeValue( billNode, "title");
                final String officeId = retrieveNodeValue( billNode, "officeId");
                final String office = retrieveNodeValue( billNode, "office");
                final String vote = retrieveNodeValue( billNode, "vote");
                final String actionId = retrieveNodeValue( billNode, "actionId");
                final String stage = retrieveNodeValue( billNode, "stage");

                Bill bill = new Bill( billId, billNumber, title, officeId, office, vote, actionId, stage) ;
                List<VotesCategory> categories = new LinkedList<VotesCategory>();
                final List<?> categoriesChildren = billNode.getChildren("categories");
                for (final Object categoriesChild : categoriesChildren) {
                    final List<?> categoryChildren = ((ConfigurationNode)categoriesChild).getChildren("category");
                    for (final Object categoryChild : categoryChildren) {
                        final ConfigurationNode categoryNode = (ConfigurationNode) categoryChild;
                        final String billCategoryId = retrieveNodeValue( categoryNode, "categoryId");
                        final String billCategoryName = retrieveNodeValue( categoryNode, "name");
                        categories.add( new VotesCategory(billCategoryId, billCategoryName) );
                    }
                }
                bill.setCategories( categories );
                bills.add( bill );
            }
            return bills;
        } catch ( Exception e ) {
            voteSmartLog.error( "GetVotesHistory: Exception, file=" + fileName + ", exception:\n" + e );
            return null;
        }
    }

    /**
   	 * Retrieves the single node value with the specified name within the candidate node.
   	 *
   	 * @author IanBrown
   	 * @param aNode
   	 *            the candidate node.
   	 * @param nodeName
   	 *            the name of the node.
   	 * @return the value for the node..
   	 * @since Sep 11, 2012
   	 * @version Sep 11, 2012
   	 */
   	private String retrieveNodeValue(final ConfigurationNode aNode, final String nodeName) {
   		final List<?> nodes = aNode.getChildren(nodeName);
   		String nodeValue = "";
   		if (nodes.size() == 1) {
   			final ConfigurationNode nodeObject = (ConfigurationNode) nodes.get(0);
   			nodeValue = (String) nodeObject.getValue();
   		}
   		return nodeValue;
   	}

    public void setTestMode(boolean testMode) {
        this.testMode = testMode;
    }

    public void setTestCandidatesFileName(String testCandidatesFileName) {
        this.testCandidatesFileName = testCandidatesFileName;
    }
}
