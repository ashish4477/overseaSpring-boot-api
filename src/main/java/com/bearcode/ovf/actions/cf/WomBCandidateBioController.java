/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.vip.VipCandidateBio;
import com.bearcode.ovf.tools.candidate.ElectionService;
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
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Extended {@link BaseController} to handle display of candidate biographics for "What's on my ballot?"
 * 
 * @author IanBrown
 * 
 * @since Aug 24, 2012
 * @version Oct 16, 2012
 */
@Controller
@RequestMapping(value = "/WomBCandidateBio.htm")
public class WomBCandidateBioController extends BaseController {

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/WomBCandidateBio.jsp";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	static final String DEFAULT_PAGE_TITLE = "What's on My Ballot?";

	/**
	 * the path to the CSS file to format the content.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Oct 15, 2012
	 */
	static final String DEFAULT_SECTION_CSS = "/css/womb.css";

	/**
	 * the default section name.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Oct 16, 2012
	 */
	static final String DEFAULT_SECTION_NAME = "womb";

	/**
	 * the election service.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Aug 24, 2012
	 */
	@Autowired
	private ElectionService electionService;

	/**
	 * Constructs a candidate bio.
	 * 
	 * @author IanBrown
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	public WomBCandidateBioController() {
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
	 * @since Aug 16, 2012
	 * @version Aug 24, 2012
	 */
	public ElectionService getElectionService() {
		return electionService;
	}

	/**
	 * Handles a request to get a candidate's biography.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param candidateVipId
	 *            the VIP identifier for the candidate.
	 * @param model
	 *            the model.
	 * @param whatsOnMyBallot
	 *            the what's on my ballot? form used to request ballot details.
	 * @return the view response.
	 * @throws Exception
	 *             if there is a problem finding the candidate bio.
	 * @since Aug 16, 2012
	 * @version Oct 12, 2012
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handleGetCandidateBio(final HttpServletRequest request,
			@RequestParam(value = "candidateVipId", required = true) final long candidateVipId, final ModelMap model,
			@ModelAttribute("whatsOnMyBallot") final WhatsOnMyBallotForm whatsOnMyBallot) throws Exception {
        if ( whatsOnMyBallot == null ) {
            return "redirect:/WhatsOnMyBallot.htm";
        }
		final State votingState = whatsOnMyBallot.getVotingState();
		final String votingRegionState = votingState.getAbbr();
		final VotingRegion votingRegion = whatsOnMyBallot.getRegion();
		final UserAddress votingAddress = whatsOnMyBallot.getAddress();
		String votingRegionName = null;
		if (votingRegion != null) {
			votingRegionName = votingRegion.getName();
		} else if (votingAddress != null) {
			votingRegionName = votingAddress.getCounty();
		}
		final VipCandidateBio candidateBio = getElectionService().findCandidateBio(votingRegionState, votingRegionName,
				candidateVipId);
		model.addAttribute("candidateBio", candidateBio);
		return buildModelAndView(request, model);
	}

	/**
	 * Sets the election service.
	 * 
	 * @author IanBrown
	 * @param electionService
	 *            the election service to set.
	 * @since Aug 16, 2012
	 * @version Aug 24, 2012
	 */
	public void setElectionService(final ElectionService electionService) {
		this.electionService = electionService;
	}

	/**
	 * Retrieves the what's on my ballot? form from the request session.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @return the what's on my ballot? form.
	 * @since Aug 14, 2012
	 * @version Aug 24, 2012
	 */
	@ModelAttribute("whatsOnMyBallot")
	public WhatsOnMyBallotForm whatsOnMyBallot(final HttpServletRequest request) {
		return (WhatsOnMyBallotForm) request.getSession().getAttribute("whatsOnMyBallot");
	}

    @RequestMapping(method = RequestMethod.HEAD )
    public ResponseEntity<String> answerToHeadRequest() {
		return sendMethodNotAllowed();
    }
}
