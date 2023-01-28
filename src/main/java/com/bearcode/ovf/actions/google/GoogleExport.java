package com.bearcode.ovf.actions.google;

import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.velocity.VelocityEngineUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Mar 31, 2008
 * Time: 2:23:21 PM
 *
 * @author Leonid Ginzburg
 */
@Controller
@RequestMapping("/service/GoogleExport.htm")
public class GoogleExport {
    public static final String GOOGLE_TEMPLATE_NAME = "eip/google.vm";
    public static final String GOOGLE_EXPORT_NAME = "eip/google.xml";

    private Logger logger = LoggerFactory.getLogger( GoogleExport.class );

    @Autowired
    private VelocityEngine velocityEngine;

    @Autowired
    private LocalOfficialService localOfficialService;

    @Autowired
    private StateService stateService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<byte[]> doExport( @RequestParam(value = "state_id", required = false) Long stateId,
                                            @RequestParam(value = "enforce", required = false) String enforce ) {
        HashMap<String, Object> model = new HashMap<String, Object>();
        model.put( "locals", (stateId != null && stateId != 0) ? localOfficialService.findForState( stateId ) : localOfficialService.findAll() );
        model.put( "states", stateService.findAllStates() );
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
        model.put( "datetime", df.format( new Date() ) );
        model.put( "escaper", new StringEscapeUtils() );

        if ( enforce != null || localOfficialService.checkLeoGotUpdated() ) {
            try {
                String baseUrl = getClass().getClassLoader().getResource( "/" ).getPath();
                OutputStreamWriter out = new OutputStreamWriter( new FileOutputStream( baseUrl + GOOGLE_EXPORT_NAME ), "utf-8" );
                VelocityEngineUtils.mergeTemplate( velocityEngine, GOOGLE_TEMPLATE_NAME, model, out );
                out.close();
            } catch ( IOException e ) {
                logger.error( "Can't write into file.", e );
            }
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.TEXT_PLAIN );
        return new ResponseEntity<byte[]>( "Done".getBytes(), headers, HttpStatus.CREATED );
    }

}
