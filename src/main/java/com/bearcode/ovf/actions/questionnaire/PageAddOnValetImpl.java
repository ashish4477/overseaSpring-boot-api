/**
 *
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.vip.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Singleton implementation of {@link PageAddOnValet}.
 *
 * TODO: consider pulling the VIP specific parts into extended valet.
 *
 * @author IanBrown
 *
 * @since Aug 7, 2012
 * @version Oct 1, 2013
 */
enum PageAddOnValetImpl implements PageAddOnValet {

    /**
     * the singleton instance of the add on.
     *
     * @author IanBrown
     * @since Aug 7, 2012
     * @version Aug 7, 2012
     */
    INSTANCE;

    /**
     * Gets an instance of the page add on valet.
     *
     * @author IanBrown
     * @return the state contests page add on valet.
     * @since Aug 7, 2012
     * @version May 7, 2013
     */
    public static final PageAddOnValet getInstance() {
        return INSTANCE;
    }

    /** {@inheritDoc} */
    @Override
    public FieldType acquireMultipleCheckboxesFieldType() {
        return new FieldTypeMultipleSelection();
    }

    /** {@inheritDoc} */
    @Override
    public QuestionField createField(final QuestionVariant variant,
                                     final Long id, final FieldType fieldType, final String title,
                                     final String helpText) {
        final QuestionField field = new QuestionField();
        field.setQuestion(variant);
        field.setId(id);
        field.setType(fieldType);
        field.setTitle(title);
        field.setHelpText(helpText);
        final Collection<QuestionField> fields = variant.getFields();
        fields.add(field);
        field.setOrder(fields.size());
        return field;
    }

    /** {@inheritDoc} */
    @Override
    public Question createGroup(final QuestionnairePage page, final Long id,
                                final String name, final String title) {
        final Question group = new Question();
        group.setPage(page);
        group.setId(id);
        group.setName(name);
        group.setTitle(title);
        final List<Question> questions = page.getQuestions();
        questions.add(group);
        group.setOrder(questions.size());
        group.setVariants(new LinkedList<QuestionVariant>());
        return group;
    }

    /** {@inheritDoc} */
    @Override
    public Collection<FieldDictionaryItem> createOptions(
            final boolean standard, final VipContest contest, final String partisanParty, final String noPartyName) {
        final VipBallot ballot = contest.getBallot();
        Collection<FieldDictionaryItem> options;
        final List<VipBallotCandidate> candidates = ballot.getCandidates();
        if (candidates != null && !candidates.isEmpty()) {
            options = optionsForCandidates(standard, contest, candidates, partisanParty, noPartyName);
        } else {
            final VipReferendum referendum = ballot.getReferendum();
            if (referendum != null) {
                options = optionsForReferendum(referendum);
            } else {
                final VipCustomBallot customBallot = ballot.getCustomBallot();
                if (customBallot != null) {
                    options = optionsForCustomBallot(customBallot);
                } else {
                    options = new LinkedList<FieldDictionaryItem>();
                }
            }
        }
        return options;
    }

    /** {@inheritDoc} */
    @Override
    public final Collection<FieldDictionaryItem> createOptions(
            String[] optionValues) {
        Collection<FieldDictionaryItem> options = new LinkedList<FieldDictionaryItem>();
        for (final String optionValue : optionValues) {
            FieldDictionaryItem option = new VirtualDictionaryItem();
            option.setValue(optionValue);
            options.add(option);
        }
        return options;
    }

    /** {@inheritDoc} */
    @Override
    public QuestionVariant createVariant(final Question group, final Long id,
                                         final String title) {
        final QuestionVariant variant = new QuestionVariant();
        variant.setQuestion(group);
        variant.setId(id);
        variant.setTitle(title);
        group.getVariants().add(variant);
        variant.setFields(new LinkedList<QuestionField>());
        return variant;
    }

    /**
     * Creates an option for the input candidate.
     *
     * @author IanBrown
     * @param standard
     *            is this a standard contest?
     * @param contest
     *            the contest.
     * @param candidate
     *            the candidate.
     * @return the option.
     * @since Aug 9, 2012
     * @version May 7, 2013
     */
    private FieldDictionaryItem createOptionForCandidate(
            final boolean standard, final VipContest contest,
            final VipCandidate candidate) {
        final VirtualDictionaryItem option = new VirtualDictionaryItem();
        final String officeName = contest.getOffice();
        // final VipElectoralDistrict electoralDistrict =
        // contest.getElectoralDistrict();
        // final String districtType = electoralDistrict.getType();
        final StringBuilder sb = new StringBuilder();
        boolean addElection = false;
        if (contest.isSpecial()) {
            sb.append(" Special");
            addElection = true;
        }

        if (!"GENERAL".equalsIgnoreCase(contest.getType())) {
            sb.append(" ").append(contest.getType());
            addElection = true;
        }

        if (addElection) {
            sb.append(" election");
        }
        final String districtName = sb.toString();
        final StringBuilder fullName = new StringBuilder();
        final String candidateName;
        final String party;
        if (candidate != null) {
            candidateName = candidate.getName();
            fullName.append(candidateName);
            party = candidate.getParty();
            final boolean incumbent = (officeName.toUpperCase().contains("JUDGE") || officeName.toUpperCase().contains("JUSTICE") || officeName.toUpperCase().contains("COURT")) && candidate.isIncumbent();
            String prefix = " {";
            String suffix = "";
            if (party != null && !party.trim().isEmpty()) {
                fullName.append(prefix).append(party);
                prefix = "";
                suffix = "}";
            }
            if (incumbent) {
                if (" {".equals(prefix)) {
                    prefix = " {N/A,";
                }
                fullName.append(prefix).append(",").append("Incumbent");
                suffix = "}";
            }
            fullName.append(suffix);
        } else {
            candidateName = "";
            party = "";
            fullName.append(WRITE_IN);
        }
        fullName.append("=");
        if (!standard) {
            // Non-standard contests need to provide the office and district
            // names as part of the value for the PDF.
            fullName.append(officeName + districtName).append("|")
                    .append( party ).append( "|" );
        }
        fullName.append(candidateName);
        option.setValue(fullName.toString());
        return option;
    }

    /**
     * Creates an option for the input response.
     *
     * @author IanBrown
     * @param title
     *            the title of the ballot entry.
     * @param ballotResponse
     *            the ballot response.
     * @return the option.
     * @since Aug 9, 2012
     * @version Oct 22, 2012
     */
    private FieldDictionaryItem createOptionForResponse(String title,
                                                        final VipBallotResponse ballotResponse) {
        final VirtualDictionaryItem option = new VirtualDictionaryItem();
        final StringBuilder fullResponse = new StringBuilder();
        final String[] parts = title.split("\\n");
        final String text = ballotResponse.getText();
        fullResponse.append(text).append("=").append(parts[0]).append("|")
                .append(text);
        option.setValue(fullResponse.toString());
        return option;
    }

    /**
     * Creates options for the candidates.
     *
     * @author IanBrown
     * @param standard
     *            is this a standard contest?
     * @param contest
     *            the contest.
     * @param candidates
     *            the candidates.
     * @param partisanParty the partisan party - used by Minnesota to select candidates for primary contests.
     * @param noPartyName the name to use to indicate that the candidate is not in a party.
     * @return the options.
     * @since Aug 9, 2012
     * @version Oct 1, 2013
     */
    private Collection<FieldDictionaryItem> optionsForCandidates(
            final boolean standard, final VipContest contest,
            final List<VipBallotCandidate> candidates, String partisanParty, String noPartyName) {
        final Collection<FieldDictionaryItem> options = new LinkedList<FieldDictionaryItem>();
        for (final VipBallotCandidate candidate : candidates) {
            if ("PRIMARY".equalsIgnoreCase(contest.getType()) && (partisanParty != null)) {
                String partyName = candidate.getCandidate().getParty();
                if (!partisanParty.equalsIgnoreCase(partyName) && ((noPartyName == null) || !noPartyName.equalsIgnoreCase(partyName))) {
                    // Skip candidates that do not belong to the primary partisan party.
                    continue;
                }
            }
            options.add(createOptionForCandidate(standard, contest,
                    candidate.getCandidate()));
        }

        // Add an additional, write-in candidate to the list to let the voter choose to write-in the
        // candidate later.
        if (!standard && !"PRIMARY".equalsIgnoreCase(contest.getType())) {
            options.add(createOptionForCandidate(standard, contest, null));
        }

        return options;
    }

    /**
     * Creates options for the custom ballot.
     *
     * @author IanBrown
     * @param customBallot
     *            the custom ballot.
     * @return the options.
     * @since Aug 9, 2012
     * @version Oct 19, 2012
     */
    private Collection<FieldDictionaryItem> optionsForCustomBallot(
            final VipCustomBallot customBallot) {
        final String title = customBallot.getHeading();
        final Collection<FieldDictionaryItem> options = new LinkedList<FieldDictionaryItem>();
        final List<VipCustomBallotResponse> ballotResponses = customBallot
                .getBallotResponses();
        for (final VipCustomBallotResponse ballotResponse : ballotResponses) {
            options.add(createOptionForResponse(title,
                    ballotResponse.getBallotResponse()));
        }
        return options;
    }

    /**
     * Creates options for the referendum.
     *
     * @author IanBrown
     * @param referendum
     *            the referendum.
     * @return the options.
     * @since Aug 9, 2012
     * @version Oct 19, 2012
     */
    private Collection<FieldDictionaryItem> optionsForReferendum(
            final VipReferendum referendum) {
        final String title = referendum.getTitle();
        final Collection<FieldDictionaryItem> options = new LinkedList<FieldDictionaryItem>();
        final List<VipReferendumBallotResponse> ballotResponses = referendum
                .getBallotResponses();
        for (final VipReferendumBallotResponse ballotResponse : ballotResponses) {
            options.add(createOptionForResponse(title,
                    ballotResponse.getBallotResponse()));
        }
        return options;
    }
}
