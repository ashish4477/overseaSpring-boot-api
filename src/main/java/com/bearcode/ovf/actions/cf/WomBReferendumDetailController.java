/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.vip.VipReferendumDetail;
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
 * Extended {@link BaseController} to produce details about a referendum on a voter's ballot.
 * 
 * @author IanBrown
 * 
 * @since Aug 16, 2012
 * @version Oct 16, 2012
 */
@Controller
@RequestMapping(value = "/WomBReferendumDetail.htm")
public class WomBReferendumDetailController extends BaseController {

	/**
	 * the default content block.
	 * 
	 * @author IanBrown
	 * @since Aug 24, 2012
	 * @version Aug 24, 2012
	 */
	static final String DEFAULT_CONTENT_BLOCK = "/WEB-INF/pages/blocks/WomBReferendumDetail.jsp";

	/**
	 * the default page title.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
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
	 * Constructs a what's on my ballot? referendum detail controller.
	 * 
	 * @author IanBrown
	 * @since Aug 16, 2012
	 * @version Aug 24, 2012
	 */
	public WomBReferendumDetailController() {
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
	 * Handles a request to get details about a referendum.
	 * 
	 * @author IanBrown
	 * @param request
	 *            the request.
	 * @param referendumVipId
	 *            the VIP identifier for the referendum.
	 * @param model
	 *            the model.
	 * @param whatsOnMyBallot
	 *            the what's on my ballot? form used to request ballot details.
	 * @return the view response.
	 * @since Aug 16, 2012
	 * @version Oct 12, 2012
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String handleGetReferendumDetail(final HttpServletRequest request, @RequestParam(value = "referendumVipId",
			required = true) final long referendumVipId, final ModelMap model,
			@ModelAttribute("whatsOnMyBallot") final WhatsOnMyBallotForm whatsOnMyBallot) {
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
		final VipReferendumDetail referendumDetail = getElectionService().findReferendumDetail(votingRegionState, votingRegionName,
				referendumVipId);
		model.addAttribute("referendumDetail", referendumDetail);
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
