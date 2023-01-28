package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.service.FormTrackingService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.pdf.generator.crypto.CipherService;
import com.bearcode.ovf.webservices.s3.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@Controller
public class DownloadForm {
    private final Logger logger = LoggerFactory.getLogger(DownloadForm.class);

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private FormTrackingService formTrackingService;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private S3Service s3Service;

    @RequestMapping(value = "/downloadForm.htm", method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadPDF(@RequestParam(value = "key", required = true) final String key) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream = s3Service.getFile(key);
        } catch (final Exception e) {
            logger.error("Cannot send PDF", e);
            return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentType(MediaType.parseMediaType("application/pdf"));
        String attachment = String.format("attachment; filename=\"%s\";", "Absentee-Ballot-Request-2020.pdf");
        headers.set("Content-Disposition", attachment);
        headers.setContentLength(outputStream.size());
        headers.set("Content-Transfer-Encoding", "binary");
        return new ResponseEntity<byte[]>(outputStream.toByteArray(), headers, HttpStatus.OK);
    }

}
