/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.questionnaire.PartisanPartyAddOn;
import com.bearcode.ovf.actions.questionnaire.StandardContest;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.vip.*;
import com.bearcode.ovf.tools.candidate.ElectionService;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Extended {@link BaseController} to produce a list of what's on the voter's ballot.
 * 
 * @author IanBrown
 * 
 * @since Aug 14, 2012
 * @version Oct 1, 2013
 */
@Controller
@RequestMapping("/WhatsOnMyBallotList.htm")
public class WhatsOnMyBallotListController extends BaseController {

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 23, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/WhatsOnMyBallotList.jsp";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "What's on My Ballot?";

	/**
	 * the path to the CSS file to format the content.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Oct 15, 2012
	 */
	static final String DEFAULT_SECTION_CSS = "/css/womb.css";

	/**
	 * the default section name.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Oct 16, 2012
	 */
	static final String DEFAULT_SECTION_NAME = "womb";

	/**
	 * redirection to the candidate finder.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	static final String REDIRECT_CANDIDATE_FINDER = "redirect:CandidateFinder.htm";

	/**
	 * redirects to the main what's on my ballot? view.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	static final String REDIRECT_WHATS_ON_MY_BALLOT = "redirect:WhatsOnMyBallot.htm";

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@Autowired
	private ElectionService electionService;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Constructs a what's on my ballot? list controller.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public WhatsOnMyBallotListController() {
		setContentBlock(DEFAULT_CONTENT_BLOCK);
		setPageTitle(DEFAULT_PAGE_TITLE);
		setSectionCss(DEFAULT_SECTION_CSS);
		setSectionName(DEFAULT_SECTION_NAME);
	}

	/**
	 * Gets the election service.
	 * 
	 * @author IanBrown
	 * @return the election service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Handles a request to show the list of what's on the voter's ballot.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param model
	 *            the model.
	 * @param whatsOnMyBallot
	 *            the form containing the what's on my ballot? request.
	 * @return the view response.
	 * @throws Exception
	 *             if there is a problem finding the contests.
	 * @since Aug 14, 2012
	 * @version Oct 12, 2012
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handleGetList(final HttpServletRequest request, final ModelMap model,
			@ModelAttribute("whatsOnMyBallot") final WhatsOnMyBallotForm whatsOnMyBallot) throws Exception {
		final State votingState = whatsOnMyBallot == null ? null : whatsOnMyBallot.getVotingState();
		final String stateAbbreviation = votingState == null ? null : votingState.getAbbr();
		final VotingRegion votingRegion = whatsOnMyBallot == null ? null : whatsOnMyBallot.getRegion();
		final String votingRegionName = votingRegion == null ? null : votingRegion.getName();
		if (votingState == null || !getVotingPrecinctService().isReady(stateAbbreviation, votingRegionName)
				|| !getElectionService().isReady(stateAbbreviation, votingRegionName)) {
			return REDIRECT_CANDIDATE_FINDER;
		}

		if (whatsOnMyBallot != null) {
			final VoterType voterType = whatsOnMyBallot.getVoterType();
			final UserAddress address = whatsOnMyBallot.getAddress();

			if (votingState != null && voterType != null && address != null) {
				final ValidAddress validAddress = getVotingPrecinctService().validateAddress(address, votingState);

				if (validAddress != null) {
					final String partisanParty = whatsOnMyBallot.getPartisanParty();
					addElectoralDistrictsForAddress(validAddress, model);
					addContestsForAddress(voterType, votingState, stateAbbreviation, votingRegionName, validAddress, partisanParty, model);
					return buildModelAndView(request, model);
				}
			}
		}

		return REDIRECT_WHATS_ON_MY_BALLOT;
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Retrieves the what's on my ballot? form from the request session.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the what's on my ballot? form.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@ModelAttribute("whatsOnMyBallot")
	public WhatsOnMyBallotForm whatsOnMyBallot(final HttpServletRequest request) {
		return (WhatsOnMyBallotForm) request.getSession().getAttribute("whatsOnMyBallot");
	}

	/**
	 * Adds contests for congress to the model.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation the abbreviation for the state.
	 * @param votingRegionName the name of the voting region.
	 * @param contests
	 *            the contests.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @param model
	 *            the model.
	 * @throws Exception if there is a problem finding the details.
	 * @since Aug 15, 2012
	 * @version Oct 30, 2012
	 */
	private void addCongressionalContests(final String stateAbbreviation, final String votingRegionName, final List<VipContest> contests, final Map<Long, VipCandidateBio> candidateBios, final ModelMap model) throws Exception {
		addContestsForStandardContest(stateAbbreviation, votingRegionName, contests, "representative",
				StandardContest.REPRESENTATIVE, candidateBios, model);
	}

	/**
	 * Adds the contests for the specified valid address to the model.
	 * 
	 * @author IanBrown
	 * @param voterType
	 *            the type of voter.
	 * @param votingState
	 *            the voting state.
	 * @param stateAbbreviation the abbreviation for the state.
	 * @param votingRegionName the name of the voting region.
	 * @param validAddress
	 *            the valid address.
	 * @param partisanParty the partisan party.
	 * @param model
	 *            the model.
	 * @throws Exception
	 *             if there is a problem finding the contests.
	 * @since Aug 14, 2012
	 * @version May 10, 2013
	 */
	private void addContestsForAddress(final VoterType voterType, final State votingState, final String stateAbbreviation,
			final String votingRegionName, final ValidAddress validAddress, final String partisanParty, final ModelMap model) throws Exception {
		final List<VipContest> contests = removePartisanContests(partisanParty, getElectionService().findContests(validAddress));
		if ((contests == null) || contests.isEmpty()) {
			return;
		}

		final Map<Long, VipCandidateBio> candidateBios = new HashMap<Long, VipCandidateBio>();
		final Map<Long, VipReferendumDetail> referendumDetails = new HashMap<Long, VipReferendumDetail>();
		addPresidentialContests(stateAbbreviation, votingRegionName, contests, candidateBios, model);
		addSenatorialContests(stateAbbreviation, votingRegionName, contests, candidateBios, model);
		addCongressionalContests(stateAbbreviation, votingRegionName, contests, candidateBios, model);
		addRegionalContests(voterType, stateAbbreviation, votingRegionName, contests, candidateBios, referendumDetails, model);
		model.addAttribute("candidateBios", candidateBios);
		model.addAttribute("referendumDetails", referendumDetails);
	}

	/**
	 * Adds the contests for the specified standard contest to the model.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param contests
	 *            the contests.
	 * @param name
	 *            the name of the attribute.
	 * @param standardContest
	 *            the standard contest.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @param model
	 *            the model.
	 * @throws Exception
	 *             if there is a problem finding the details.
	 * @since Aug 15, 2012
	 * @version Oct 30, 2012
	 */
	private void addContestsForStandardContest(final String stateAbbreviation, final String votingRegionName,
			final List<VipContest> contests, final String name, final StandardContest standardContest,
			final Map<Long, VipCandidateBio> candidateBios, final ModelMap model) throws Exception {
		final List<VipContest> officeContests = new LinkedList<VipContest>();
		VipContest officeContest = findContest(contests, false, standardContest);
		if (officeContest != null) {
			officeContests.add(officeContest);
			addDetailsForContest(stateAbbreviation, votingRegionName, officeContest, candidateBios, null);
		}
		officeContest = findContest(contests, true, standardContest);
		if (officeContest != null) {
			officeContests.add(officeContest);
			addDetailsForContest(stateAbbreviation, votingRegionName, officeContest, candidateBios, null);
		}
		if (!officeContests.isEmpty()) {
			model.addAttribute(name, officeContests);
		}
	}

	/**
	 * Adds the details flag for the specified candidate.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation of the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param candidate
	 *            the candidate.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @throws Exception
	 *             if there is a problem finding the candidate BIO.
	 * @since Oct 24, 2012
	 * @version Oct 30, 2012
	 */
	private void addDetailsForCandidate(final String stateAbbreviation, final String votingRegionName,
			final VipCandidate candidate, final Map<Long, VipCandidateBio> candidateBios) throws Exception {
		final long candidateVipId = candidate.getVipId();
		final VipCandidateBio candidateBio = getElectionService().findCandidateBio(stateAbbreviation, votingRegionName,
				candidateVipId);
		candidateBios.put(candidateVipId, candidateBio);
	}

	/**
	 * Adds detail flags for the specified candidates.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the state abbreviation.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param candidates
	 *            the candidates.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @throws Exception
	 *             if there is a problem finding details.
	 * @since Oct 24, 2012
	 * @version Oct 30, 2012
	 */
	private void addDetailsForCandidates(final String stateAbbreviation, final String votingRegionName,
			final List<VipBallotCandidate> candidates, final Map<Long, VipCandidateBio> candidateBios) throws Exception {
		for (final VipBallotCandidate ballotCandidate : candidates) {
			final VipCandidate candidate = ballotCandidate.getCandidate();
			addDetailsForCandidate(stateAbbreviation, votingRegionName, candidate, candidateBios);
		}
	}

	/**
	 * Adds the details flags for the specified contest.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param contest
	 *            the contest.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @param referendumDetails the details for the referendums by VIP identifier.
	 * @throws Exception
	 *             if there is a problem finding the details.
	 * @since Oct 24, 2012
	 * @version Oct 30, 2012
	 */
	private void addDetailsForContest(final String stateAbbreviation, final String votingRegionName, final VipContest contest,
			final Map<Long, VipCandidateBio> candidateBios, final Map<Long, VipReferendumDetail> referendumDetails) throws Exception {
		final VipBallot ballot = contest.getBallot();
		if (ballot.getReferendum() != null) {
			addDetailsForReferendum(stateAbbreviation, votingRegionName, ballot.getReferendum(), referendumDetails);
		} else if (ballot.getCandidates() != null && !ballot.getCandidates().isEmpty()) {
			addDetailsForCandidates(stateAbbreviation, votingRegionName, ballot.getCandidates(), candidateBios);
		}
	}

	/**
	 * Adds the details flag for the input referendum.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region (may be <code>null</code>).
	 * @param referendum
	 *            the referendum.
	 * @param referendumDetails the details for the referendums by VIP identifier.
	 * @since Oct 24, 2012
	 * @version Oct 30, 2012
	 */
	private void addDetailsForReferendum(final String stateAbbreviation, final String votingRegionName,
			final VipReferendum referendum, final Map<Long, VipReferendumDetail> referendumDetails) {
		final long referendumVipId = referendum.getVipId();
		final VipReferendumDetail referendumDetail = getElectionService().findReferendumDetail(stateAbbreviation,
				votingRegionName, referendumVipId);
		referendumDetails.put(referendumVipId, referendumDetail);
	}

	/**
	 * Adds the electoral districts from the object with electoral districts.
	 * 
	 * @author IanBrown
	 * @param hasElectoralDistricts
	 *            the object with electoral districts - may be <code>null</code>.
	 * @param electoralDistricts
	 *            the electoral districts.
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	private void addElectoralDistricts(final AbstractVipHasElectoralDistricts hasElectoralDistricts,
			final Set<VipElectoralDistrict> electoralDistricts) {
		if (hasElectoralDistricts != null) {
			electoralDistricts.addAll(hasElectoralDistricts.getElectoralDistricts());
		}
	}

	/**
	 * Adds the electoral districts available for the validated address.
	 * 
	 * @author IanBrown
	 * @param validAddress
	 *            the valid address.
	 * @param model
	 *            the model.
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	private void addElectoralDistrictsForAddress(final ValidAddress validAddress, final ModelMap model) {
		final Set<VipElectoralDistrict> electoralDistricts = new HashSet<VipElectoralDistrict>();
		final VipStreetSegment streetSegment = validAddress.getStreetSegment();
		addElectoralDistricts(streetSegment.getPrecinct(), electoralDistricts);
		addElectoralDistricts(streetSegment.getPrecinctSplit(), electoralDistricts);
		model.addAttribute("electoralDistricts", electoralDistricts);
	}

	/**
	 * Adds the presidential contest(s) to the model.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param contests
	 *            the full list of contests.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @param model
	 *            the model.
	 * @throws Exception
	 *             if there is a problem finding the details.
	 * @since Aug 15, 2012
	 * @version Oct 30, 2012
	 */
	private void addPresidentialContests(final String stateAbbreviation, final String votingRegionName,
			final List<VipContest> contests, final Map<Long, VipCandidateBio> candidateBios, final ModelMap model) throws Exception {
		addContestsForStandardContest(stateAbbreviation, votingRegionName, contests, "president", StandardContest.PRESIDENT,
				candidateBios, model);
	}

	/**
	 * Adds the regional contests for citizens that are not overseas indefinitely.
	 * 
	 * @author IanBrown
	 * @param voterType
	 *            the type of voter.
	 * @param stateAbbreviation the abbreviation for the state.
	 * @param votingRegionName the name of the voting region.
	 * @param contests
	 *            the contests.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @param referendumDetails the details of the referendums by VIP identifier.
	 * @param model
	 *            the model.
	 * @throws Exception if there is a problem finding the details.
	 * @since Oct 24, 2012
	 * @version Oct 30, 2012
	 */
	private void addRegionalContests(final VoterType voterType, final String stateAbbreviation, final String votingRegionName,
			final List<VipContest> contests, final Map<Long, VipCandidateBio> candidateBios, final Map<Long, VipReferendumDetail> referendumDetails, final ModelMap model) throws Exception {
		if (voterType != VoterType.OVERSEAS_VOTER) {
			final List<String> contestOrder = getElectionService().contestOrder(stateAbbreviation, votingRegionName);
			final List<VipContest> orderedContests = ElectionService.orderContests(contests, contestOrder);
			for (final VipContest contest : orderedContests) {
				addDetailsForContest(stateAbbreviation, votingRegionName, contest, candidateBios, referendumDetails);
			}
			model.addAttribute("contests", orderedContests);
		}
	}

	/**
	 * Adds contests for the senate to the model.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *            the abbreviation for the state.
	 * @param votingRegionName
	 *            the name of the voting region.
	 * @param contests
	 *            the contests.
	 * @param candidateBios the bios for the candidates by VIP identifier.
	 * @param model
	 *            the model.
	 * @throws Exception
	 *             if there is a problem finding the details.
	 * @since Aug 15, 2012
	 * @version Oct 30, 2012
	 */
	private void addSenatorialContests(final String stateAbbreviation, final String votingRegionName,
			final List<VipContest> contests, final Map<Long, VipCandidateBio> candidateBios, final ModelMap model) throws Exception {
		addContestsForStandardContest(stateAbbreviation, votingRegionName, contests, "senator", StandardContest.SENATOR,
				candidateBios, model);
	}

	/**
	 * Finds the contest for the specified office.
	 * 
	 * @author IanBrown
	 * @param contests
	 *            the contests.
	 * @param special
	 *            <code>true</code> to find the special office.
	 * @param standardContest
	 *            the standard contest.
	 * @return the contest.
	 * @since Aug 15, 2012
	 * @version Sep 21, 2012
	 */
	private VipContest findContest(final List<VipContest> contests, final boolean special, final StandardContest standardContest) {
		for (final VipContest contest : contests) {
			final VipBallot ballot = contest.getBallot();
			final List<VipBallotCandidate> candidates = ballot.getCandidates();
			if (candidates == null || candidates.isEmpty()) {
				continue;
			}

			if (standardContest.matches(contest.getOffice())) {
				contests.remove(contest);
				return contest;
			}
		}

		return null;
	}

	/**
	 * Removes all partisan contests not belonging to the specified party.
	 * @author IanBrown
	 * @param partisanParty the partisan party.
	 * @param contests the contests.
	 * @return the non-partisan contests plus the partisan contests for the specified party.
	 * @since May 10, 2013
	 * @version Oct 1, 2013
	 */
	private List<VipContest> removePartisanContests(final String partisanParty,
            final List<VipContest> contests) {
		final List<VipContest> nonPartisanContests = new LinkedList<VipContest>();
		for (final VipContest contest : contests) {
			String contestPartisanParty = contest.isPartisan() ? contest.getPartisanParty() : null;
			if ((contestPartisanParty != null) && !contestPartisanParty.isEmpty() && !contestPartisanParty.equalsIgnoreCase(partisanParty)) {
				continue;
			} else if ("PRIMARY".equalsIgnoreCase(contest.getType())) {
				int partisanCandidates = 0;
				for (final VipBallotCandidate candidate : contest.getBallot().getCandidates()) {
					final String candidateParty = candidate.getCandidate().getParty();
					if ((candidateParty == null) || candidateParty.isEmpty() || PartisanPartyAddOn.DEFAULT_NON_PARTISAN_PARTY.equals(candidateParty)) {
						++partisanCandidates;
					} else if (candidateParty.equalsIgnoreCase(partisanParty)) {
						++partisanCandidates;
					}
				}
				if (partisanCandidates == 0) {
					continue;
				}
			}
			nonPartisanContests.add(contest);
		}
		return nonPartisanContests;
    }

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
		return sendMethodNotAllowed();
    }
}
