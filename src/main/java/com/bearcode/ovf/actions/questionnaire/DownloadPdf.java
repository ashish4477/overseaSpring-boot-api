package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.FormTrackingService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.tools.pdf.generator.crypto.CipherService;
import com.bearcode.ovf.utils.FacConfigUtil;
import com.bearcode.ovf.webservices.s3.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

@Controller
public class DownloadPdf {
    private final Logger logger = LoggerFactory.getLogger(DownloadPdf.class);

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private FormTrackingService formTrackingService;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private S3Service s3Service;

    @RequestMapping(value = "/downloadPdf.htm", method = RequestMethod.GET)
    public String newCreatePdf(@RequestParam(value = "generationUUID", required = true) final String uuid) {
        String key = null;
        try {
            WizardResults results = questionnaireService.findWizardResultByUUID(uuid).get(0);
            key = results.getUrl();
            results.setDownloaded(true);
            questionnaireService.saveWizardResults(results);
        } catch (final Exception e) {
            logger.error("Cannot send PDF", e);
            return "redirect:/errors.htm?errorCode=405";
        }
        return "redirect:/downloadForm.htm?key=" + key;
    }
}
