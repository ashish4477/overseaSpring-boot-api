package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.eodcommands.ExcelPort;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 22, 2007
 * Time: 7:39:40 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/EodDataUpload.htm")
public class ExcelDataUpload extends BaseController {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private ExcelPort excelPort;

    public ExcelDataUpload() {
        setContentBlock( "/WEB-INF/pages/blocks/admin/EodDataUpload.jsp" );
        setPageTitle( "Import EOD Data from Excel" );
        setSectionName( "eod" );
        setSectionCss( "/css/eod.css" );
    }

    public LocalOfficialService getLocalOfficialService() {
        return localOfficialService;
    }

    public void setLocalOfficialService( LocalOfficialService localOfficialService ) {
        this.localOfficialService = localOfficialService;
    }

    public ExcelPort getExcelPort() {
        return excelPort;
    }

    public void setExcelPort( ExcelPort excelPort ) {
        this.excelPort = excelPort;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String showPage( HttpServletRequest request, ModelMap model ) {
        return buildModelAndView( request, model );
    }

    @RequestMapping(method = RequestMethod.POST)
    protected String onSubmit( HttpServletRequest request, ModelMap model,
                               @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId ) throws Exception {
        State state = getStateService().findState( stateId );
        model.put( "error", true );
        if ( state != null ) {
            // get excel file from multipart request, create new records for LEOs
            try {
                if ( request instanceof MultipartHttpServletRequest ) {
                    MultipartHttpServletRequest multipart = (MultipartHttpServletRequest) request;
                    if ( multipart.getFileMap().containsKey( "leosFile" ) && !multipart.getFile( "leosFile" ).isEmpty() ) {
                        MultipartFile file = multipart.getFile( "leosFile" );
                        if ( file.getOriginalFilename().matches( ".*\\.xls" ) ) {
                            //if ( excelPort.checkFileVersion( file.getInputStream() ) ) {
                            Collection<LocalOfficial> eod = excelPort.readFromExcel( file.getInputStream(), state );
                            if ( eod.size() > 0 ) {
                                localOfficialService.saveAllLocalOfficial( eod );
                                model.put( "success", true );
                                model.put( "processedState", state );
                                model.put( "numberOfLeos", eod.size() );
                                model.remove( "error" );
                                //  }
                            }
                            else {
                                model.remove( "error" );
                                model.put( "wrongVersion", true );
                            }
                        }
                    }
                }
            } catch ( IOException e ) {
                logger.error( "Can't upload information from Excel file", e );
            }
        }
        return buildModelAndView( request, model );
    }

}
