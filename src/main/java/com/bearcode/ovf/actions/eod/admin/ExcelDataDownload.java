package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.eodcommands.ExcelPort;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Apr 14, 2008
 * Time: 7:04:38 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
public class ExcelDataDownload {

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private StateService stateService;

    @Autowired
    private ExcelPort excelPort;


    public void setStateService( StateService stateService ) {
        this.stateService = stateService;
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

    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/admin/EodDataDownload.htm", method = RequestMethod.GET)
    public ResponseEntity<byte[]> handleRequestInternal(
                                       @RequestParam(value = "stateId", required = false, defaultValue = "0") Long stateId,
                                       @RequestParam(value = "all", required = false, defaultValue = "") String all ) throws Exception {
        State state = stateService.findState( stateId );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( new MediaType( "application", "force-download" ) );
        headers.setContentType( new MediaType( "application", "vnd.ms-excel" ) );
        headers.set( "Content-Transfer-Encoding", "binary" );

        if ( state != null ) {
            Collection<LocalOfficial> eod = localOfficialService.findForState( state );

            headers.set( "Content-Disposition", "attachment; filename=" + state.getName().replace( " ", "_" ) + ".xls" );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HSSFWorkbook excelbook = excelPort.writeIntoExcel( state, eod );
            excelbook.write( out );

            return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.CREATED );
        } else if ( !all.equals( "" ) ) {
            Collection<LocalOfficial> eod = localOfficialService.findAll();
            Collection<State> states = stateService.findAllStates();

            headers.set( "Content-Disposition", "attachment; filename=allStates.xls" );

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            HSSFWorkbook excelbook = excelPort.writeIntoExcel( states, eod );
            excelbook.write( out );

            return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.CREATED );
        }
        return null;
    }
}
