package com.bearcode.ovf.actions;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.FlowType;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * Extended {@link BaseController} to provide the ability to choose what type of form the voter wishes to use.
 *
 */
@Controller
public class FormChoiceController extends BaseController {

    public static final String PAGE_TITLE = "Voter Registration and Absentee Ballots";
    public static final String CONTENT_BLOCK = "/WEB-INF/pages/blocks/FormTypeSelection.jsp";
    public static final String SECTION_NAME = "rava";
    public static final String SECTION_CSS = "/css/rava.css";
    public static final String BASE_FLOW_REDIRECTION = "redirect:/w";

    public FormChoiceController() {
        setSectionName(SECTION_NAME);
        setSectionCss(SECTION_CSS);
        setContentBlock(CONTENT_BLOCK);
        setPageTitle(PAGE_TITLE);
    }

    @RequestMapping(value = "/register.htm", method = RequestMethod.GET)
    public String requestDirectFlowForm(HttpServletRequest request,
                                        @RequestParam(value = "flow", required = false) final String flow,
                                        ModelMap model) {

        if (StringUtils.isNotEmpty(flow) && FlowType.isValidFlow(flow)) {
            FlowType flowType = FlowType.getFormSlugByFlow(flow);
            return BASE_FLOW_REDIRECTION + "/" + flowType.getPageSlug() + ".htm";
        }

        return requestFormChoice(request, null, null, model);
    }

    /**
     * Requests a form choice page to allow the voter to choose the type of form the voter wishes to use.
     */
    @RequestMapping(value = "/voter-registration-absentee-voting.htm", method = RequestMethod.GET)
    public String requestFormChoice(HttpServletRequest request, @RequestParam(value = "vrState", required = false) String votingRegionState,
                                    @RequestParam(value = "vrName", required = false) String votingRegionName, ModelMap model) {
        if (votingRegionState != null) {
            model.addAttribute("vrState", votingRegionState);
        }
        if (votingRegionName != null) {
            model.addAttribute("vrName", votingRegionName);
        }

        FaceConfig config = getFaceConfig(request);
        if (config.getRelativePrefix().contains("skimm") || config.getRelativePrefix().contains("vote411")) {
            return BASE_FLOW_REDIRECTION + "/domestic_absentee.htm";
        }

        return buildModelAndView(request, model);
    }

    /**
     * Performs a form choice by starting the flow for the selected form.
     *
     * @param votingRegionState the state containing the voting region.
     * @param votingRegionName  the name of the voting region.
     * @param formType          the type of form.
     */
    @RequestMapping(value = "/voter-registration-absentee-voting.htm", method = RequestMethod.POST)
    public String performFormChoice(final HttpServletRequest request,
                                    @RequestParam(value = "vrState", required = false) final String votingRegionState,
                                    @RequestParam(value = "vrName", required = false) final String votingRegionName,
                                    @RequestParam(value = "formType", required = false) final String formType, final ModelMap model) {
        if (formType == null || formType.isEmpty()) {
            return requestFormChoice(request, votingRegionState, votingRegionName, model);
        }
        if (votingRegionState != null) {
            model.addAttribute("vrState", votingRegionState);
        }
        if (votingRegionName != null) {
            model.addAttribute("vrName", votingRegionName);
        }

        return BASE_FLOW_REDIRECTION + "/" + formType + ".htm";
    }

    @RequestMapping(method = RequestMethod.HEAD)
    public ResponseEntity<String> answerToHeadRequest() {
        return sendMethodNotAllowed();
    }
}
