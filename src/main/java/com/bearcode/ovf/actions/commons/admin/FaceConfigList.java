package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 12, 2007
 * Time: 8:28:39 PM
 * @author Leonid Ginzburg
 */
@Controller
public class FaceConfigList extends BaseController {

    public FaceConfigList() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/FacesList.jsp" );
        setPageTitle( "Faces Config List" );
        setSectionName( "admin" );
        setSectionCss( "/css/admin.css" );
    }

    @ModelAttribute("faceConfigs")
    public Collection<FaceConfig> getFaces() {
        return getFacesService().findAllConfigs();
    }

    @ModelAttribute("faceInstructions")
    public Map<Long, Collection<FaceFlowInstruction>> getInstructions() {
        Map<Long, Collection<FaceFlowInstruction>> map = new HashMap<Long, Collection<FaceFlowInstruction>>();
        Collection<FaceFlowInstruction> allInstr = getFacesService().findInstructions();
        for ( FaceFlowInstruction instruction : allInstr ) {
            Long faceConfigId = instruction.getFaceConfig().getId();
			Collection<FaceFlowInstruction> forFace = map.get( faceConfigId );
            if ( forFace == null ) {
                forFace = new LinkedList<FaceFlowInstruction>();
                map.put( faceConfigId, forFace );
            }
            forFace.add( instruction );
        }
        return map;
    }

    @RequestMapping("/admin/FacesConfigsList.htm")
    public String buildReferences(HttpServletRequest request, ModelMap model) {
        return buildModelAndView( request, model );
    }
}
