package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.SessionContextStorage;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.PdfFormTrack;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.*;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.tools.pdf.generator.PdfGeneratorUtil;
import com.bearcode.ovf.tools.pdf.generator.crypto.CipherService;
import com.bearcode.ovf.utils.FacConfigUtil;
import com.bearcode.ovf.webservices.s3.S3Service;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.mail.EmailException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 11, 2007
 * Time: 11:05:53 PM
 * Simple action that create pdf-form and also send the "thank-you" email
 */
@Controller
public class CreateForm {
    private final Logger logger = LoggerFactory.getLogger( CreateForm.class );

    @Autowired
    private EmailService emailService;

    @Autowired
    private FacesService facesService;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private FormTrackingService formTrackingService;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private PdfFormTrackService formTrackService;

    @Autowired
    private S3Service s3Service;

    @RequestMapping(value = "/CreatePdf.htm", method = RequestMethod.GET)
    public ResponseEntity<byte[]> newCreatePdf( final HttpServletRequest request,
                                                @RequestParam( value = "generationId", required = false ) final Long generationId,
                                                @RequestParam( value = "generationUUID", required = false ) final String uuid) {
        final WizardContext context = SessionContextStorage.create(request).load();
        if (context == null) {
            return new ResponseEntity<byte[]>(HttpStatus.FORBIDDEN);
        }

        final WizardResults results = context.getWizardResults();
        try {
            // Sanity check - make sure current session user is the same as
            // user who initialized the form in case where there are user accounts involved.
            sanityUserCheck(results.getUser());
        } catch (final SecurityUserException e) {
            return new ResponseEntity<byte[]>(HttpStatus.FORBIDDEN);
        }


        final String serverPath = request.getServerName() + request.getContextPath();
        final FaceConfig config = facesService.findConfig(serverPath);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            // send PDF file...
            if (FacConfigUtil.isS3StorageRequired(config)) {
                outputStream = s3Service.getFile(results.getUrl());
            } else {
                File encoded = formTrackService.findTrackedFile(generationId);
                InputStream fileInput = new FileInputStream(encoded);
                cipherService.decrypt(fileInput, outputStream, generationId);
            }

        } catch (final Exception e) {
            results.setDownloaded(false);
            logger.error("Cannot send PDF", e);
            logger.info("Pdf Generator OutputPath: {}", serverPath);
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }

        results.setLastChangedDate(new Date());
        results.setDownloaded(true);
        if (!results.isEmailSent()) {
            try {
                // send `thank you` email
                emailService.sendEmail(results, config);
                results.setEmailSent(true);
                formTrackingService.saveAfterThankYou(context); // If desired by the SHS, remind the voter for a while about the form.

            } catch (final EmailException e) {
                results.setEmailSent(false);
                logger.error("Cannot send email", e);
            }
        }

        context.processSaveResults(questionnaireService);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String attachment = String.format("attachment; filename=\"%s\";", PdfGeneratorUtil.getFileName(context) );
        headers.set( "Content-Disposition", attachment );
        headers.setContentLength( outputStream.size() );
        headers.set("Content-Transfer-Encoding", "binary");
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK );
    }

    @RequestMapping(value = "/CreatePdf.htm", method = RequestMethod.HEAD)
    public ResponseEntity<String> dummyHeadRequest() {
        HttpHeaders headers =  new HttpHeaders();
        Set<HttpMethod> allowed = new HashSet<HttpMethod>( 2 );
        Collections.addAll( allowed, HttpMethod.GET, HttpMethod.POST  );
        headers.setAllow( allowed );
        return new ResponseEntity<String>(headers, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @RequestMapping("/ajax/checkFormTrack.htm")
    public ResponseEntity<String> checkTrackStatus( @RequestParam(value = "trackId", required = false) Long trackId ) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( MediaType.APPLICATION_JSON );
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        if ( trackId != null ) {
            PdfFormTrack track = formTrackService.findFormTrack( trackId );
            if ( track != null ) {
                String result = gson.toJson( track );
                return new ResponseEntity<String>( result, headers, HttpStatus.OK );
            }
        }
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("message", "Form not found");
        map.put("status", PdfFormTrack.TRACK_NOT_FOUND);
        String result = gson.toJson( map );
        return new ResponseEntity<String>( result, headers, HttpStatus.OK );
    }

    private void sanityUserCheck( final OverseasUser formUser ) throws SecurityUserException {
        OverseasUser loggedInUser = null;
        final Object securityObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ( securityObj instanceof OverseasUser ) {
            loggedInUser = (OverseasUser) securityObj;
        }

        if ( formUser != null && (loggedInUser == null || formUser.getId() != loggedInUser.getId()) ) {
            throw new SecurityUserException();
        }

        if ( loggedInUser != null && (formUser == null || formUser.getId() != loggedInUser.getId()) ) {
            throw new SecurityUserException();
        }
    }

}
