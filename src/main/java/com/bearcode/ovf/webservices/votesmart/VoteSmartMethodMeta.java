package com.bearcode.ovf.webservices.votesmart;

/**
 * Date: 27.09.14
 * Time: 0:26
 *
 * @author Leonid Ginzburg
 */
public enum VoteSmartMethodMeta {
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
     ELECTIONS( "%selection_%s", "Election.getElectionByYearState", new String[]{ "year" });

     VoteSmartMethodMeta( String fileNameTemplate, String voteSmartAPIMethod, String[] arguments ) {
         this.fileNameTemplate = fileNameTemplate;
         this.voteSmartAPIMethod = voteSmartAPIMethod;
         this.arguments = arguments;
     }

/*
     String getCacheFileName( Object... args ) {
         String fn = String.format( fileNameTemplate, args ) + "." + VS_API_OUTPUT_FORMAT;
         return FilenameUtils.normalize(fn);
     }
*/

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
