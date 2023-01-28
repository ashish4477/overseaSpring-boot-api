package com.bearcode.ovf.actions.commons.admin;

import com.bearcode.ovf.service.OverseasUserService;
import com.bearcode.ovf.tools.export.ExportUserCsv;
import com.bearcode.ovf.tools.export.ExportUserExcel;
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

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Dec 17, 2007
 * Time: 6:43:37 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/admin/exportUsers.htm")
public class UserExport {

    @Autowired
    private OverseasUserService overseasUserService;

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST})
    public ResponseEntity<byte[]> doExportUsers( @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
                                                 @RequestParam(value = "limit", required = false, defaultValue = "65000") Integer limit,
                                                 @RequestParam(value = "format", required = false, defaultValue = "xls") String format ) throws Exception {

        if ( format.equalsIgnoreCase( "xls" ) ) {
            return buildXls( page, limit );
        } else {
            return buildCsv( page, limit );
        }
    }

    private ResponseEntity<byte[]> buildXls( int start, int limit ) throws Exception {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExportUserExcel exporter = new ExportUserExcel();
        exporter.setOverseasUserService( overseasUserService );
        exporter.write( out, start, limit );

        HttpHeaders headers = new HttpHeaders();
        //headers.add("Content-Type", "application/force-download");
        headers.setContentType( new MediaType( "application", "vnd.ms-excel" ) );
        headers.add( "Content-Disposition", "attachment; filename=allUsers.xls" );
        headers.add( "Content-Transfer-Encoding", "binary" );
        return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.CREATED );
    }

    private ResponseEntity<byte[]> buildCsv( int start, int limit ) throws Exception {


        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ExportUserCsv exporter = new ExportUserCsv();
        exporter.setOverseasUserService( overseasUserService );
        exporter.write( out, start, limit );

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType( new MediaType( "text", "csv" ) );
        headers.add( "Content-Disposition", "attachment; filename=AllUsers.csv" );
        return new ResponseEntity<byte[]>( out.toByteArray(), headers, HttpStatus.CREATED );
    }
}
