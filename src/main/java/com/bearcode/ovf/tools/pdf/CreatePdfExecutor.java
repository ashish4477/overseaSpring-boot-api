package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.PdfFormTrackService;
import com.bearcode.ovf.service.PendingVoterRegistrationService;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.service.email.EmailService;
import com.bearcode.ovf.tools.pdf.generator.crypto.CipherService;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationUtil;
import com.bearcode.ovf.utils.FacConfigUtil;
import com.bearcode.ovf.utils.UserInfoFields;

import com.bearcode.ovf.webservices.s3.S3Service;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.util.Date;
import java.util.Map;

/**
 * Date: 06.10.14
 * Time: 21:01
 *
 * @author Leonid Ginzburg
 */
@Component
public class CreatePdfExecutor {
    private final Logger logger = LoggerFactory.getLogger( CreatePdfExecutor.class );

    //file name for storing in sandbox
    private static final String FILE_NAME_TEMPLATE = "form_%d.pdf";

    private static final long QUESTION_FIELD_NON_EXISTENT_ID = 9999999990l;

    @Autowired
    private AdvancedPdfGeneratorFactory advancedPdfGeneratorFactory;

    @Autowired
    private CipherService cipherService;

    @Autowired
    private PdfFormTrackService pdfFormTrackService;

    @Autowired
    private PendingVoterRegistrationService pendingVoterRegistrationService;

    @Autowired
    private PendingVoterRegistrationUtil pendingVoterRegistrationUtil;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private EmailService emailService;

    @Autowired
    protected QuestionnaireService questionnaireService;


    @Value("${createPdfExecutor.sandboxDir:/tmp/pdf_generation_sandbox}")
    private String sandboxDir;

    private boolean initSandbox = true;

    /**
     * Find or create PdfFormTrack based on WizardContext.
     * It's used for preventing generation forms if user did not changed anything in the context since
     * last from generation.
     *
     * @param context  the the wizard context.
     * @return  PdfFormTrack
     */
    public PdfFormTrack getFormTrack( final WizardContext context ) {
        StringBuilder sb = new StringBuilder();
        final Map<String, String> answers = context.createModel( false );
        addToString( sb, answers );
        final Map<String, String> userFields = UserInfoFields.getUserValues(context.getWizardResults());
        addToString( sb, userFields );
        String hash = DigestUtils.md5DigestAsHex(sb.toString().getBytes());
        PdfFormTrack track = pdfFormTrackService.findFormTrackByHash( hash );
        if ( track == null ) {
            track = new PdfFormTrack();
            track.setHash(hash);
            track.setUser(context.getWizardResults().getUser());
        } else {
            track.setCreated(new Date());
        }
        pdfFormTrackService.saveFormTrack( track );
        return track;

    }

    private void addToString( final StringBuilder sb, final Map<String, String> map ) {
        for ( String key : map.keySet() ) {
            // pendingVoterRegistration adds additional field AFTER defining hash code for this context
            if ( !key.equalsIgnoreCase( PendingVoterRegistration.PDF_ID ) ) {
                sb.append( key ).append( map.get( key ) );
            }
        }
    }

    /**
     * Generate pdf-form for given WizardContext.
     * Store encrypted file in the sandbox directory.
     * Also run "pending voter registration" process
     *
     * @param generatorId id of PdfFormTrack
     * @param context the wizard context.
     */
    @Async
    public void createPdfFormFile( final long generatorId, final WizardContext context ) {
        PdfFormTrack track = pdfFormTrackService.findFormTrack( generatorId );
        if ( track != null && track.getStatus() == PdfFormTrack.CREATED ) {
            track.setStatus( PdfFormTrack.IN_PROCESS );
            pdfFormTrackService.saveFormTrack( track );
            PendingVoterRegistration pendingVoterRegistration = null;
            try {
                pendingVoterRegistration = cachePendingVoterRegistration(context);

                File resultFile = createFormFile( track );
                ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
                OutputStream fileStream = new FileOutputStream( resultFile );
                PdfGenerator generator = advancedPdfGeneratorFactory.createPdfGenerator( context, byteOutput );
                generator.run();
                generator.dispose();
                ByteArrayInputStream byteInput = new ByteArrayInputStream( byteOutput.toByteArray() );
                
                if(FacConfigUtil.isS3StorageRequired(context.getCurrentFace())) {
                	IOUtils.copy( byteInput, fileStream );
                    String key = s3Service.getKey("/" + context.getWizardResults().getUuid() +  "/" + RandomStringUtils.randomAlphanumeric(128));
                    logger.warn(key);
                    s3Service.putObject(key, resultFile);
                    context.getWizardResults().setUrl(key);
                    emailService.sendEmail(context.getWizardResults(),context.getCurrentFace());
                    context.getWizardResults().setEmailSent(true);
                    questionnaireService.saveWizardResults(context.getWizardResults());
                } else {
                	cipherService.encrypt( byteInput, fileStream, generatorId );
                }
                track.setStatus( PdfFormTrack.GENERATED );
                track.setFormFileName( resultFile.getAbsolutePath() );
            } catch (Exception e) {
                logger.error("Can't create pdf file ", e );
                track.setStatus( PdfFormTrack.ERROR );
                if (pendingVoterRegistration != null) {
                    try {
                        getPendingVoterRegistrationService().delete(pendingVoterRegistration);
                    } catch (final Exception e1) {
                        logger.error("Failed to clean up pending voter registration after PDF generation error", e);
                    }
                }
            }
            pdfFormTrackService.saveFormTrack( track );
        }
    }

    /**
     * Check if sandbox directory exists and create it
     */
//    @PostConstruct
    public void createSandbox() throws IllegalArgumentException {
        String sandboxTmp = FilenameUtils.normalizeNoEndSeparator(sandboxDir);
        if ( sandboxTmp == null || sandboxTmp.startsWith( "${" )) {
            throw new IllegalArgumentException( "PDF Form Generation: Invalid directory '" + sandboxTmp + "'" );
        }
        File sandboxDir = new File( sandboxTmp );
        if ( sandboxDir.exists() ) {
            logger.info( "PDF Generation Sandbox Directory '" + sandboxTmp + "' found" );
        } else {
            sandboxDir.mkdirs();
            logger.info( "PDF Generation Sandbox Directory '" + sandboxTmp + "' has been created" );
        }
        this.sandboxDir = sandboxTmp + File.separator;
    }

    private File createFormFile( final PdfFormTrack track ) throws IOException {
        if ( initSandbox ) {
            createSandbox();
            initSandbox = false;
        }
        String fullName = sandboxDir + makeFilename( track );
        File outputFile = new File( fullName );
        if ( !outputFile.exists() ) {
            if ( outputFile.createNewFile() ) {
                return outputFile;
            }
        }
        throw new FileNotFoundException( "Could not create file " + fullName  );
    }

    public String makeFilename( final PdfFormTrack track ) {
        if ( track == null || track.getId() == 0 ) {
            throw new IllegalStateException( "PdfFormTrack should be created" );
        }
        return String.format( FILE_NAME_TEMPLATE, track.getId() );
    }

    /**
   	 * Caches a pending voter registration for the FPCA flow.
   	 * @author IanBrown
   	 * @param context the wizard context.
   	 * @return the pending voter registration.
   	 * @since Nov 14, 2012
   	 * @version Nov 15, 2012
   	 */
   	public PendingVoterRegistration cachePendingVoterRegistration(final WizardContext context) {
   		if (context.getFlowType() != FlowType.RAVA) {
   			return null;
   		}

        final PendingVoterRegistration pendingVoterRegistration = pendingVoterRegistrationUtil.convertWizardContext(context);
        if (pendingVoterRegistration == null) {
            return null;
        }
        try {
            pendingVoterRegistrationUtil.encryptAndSave(pendingVoterRegistration);
            final QuestionField field = new QuestionField();
            field.setId(QUESTION_FIELD_NON_EXISTENT_ID);
            field.setType(getQuestionFieldService().findFieldTypeByTemplate(FieldType.TEMPLATE_TEXT));
            field.setTitle("Pending Voter Registration ID");
            field.setInPdfName(PendingVoterRegistration.PDF_ID);
            field.setSecurity(true);
            final Answer answer = field.createAnswer();
            answer.setValue(Long.toString(pendingVoterRegistration.getId()));
            context.putAnswer(answer);
        } catch (Exception e) {
            logger.warn( "Pending voter registration issue ", e );
            if ( pendingVoterRegistration.getId() != null && pendingVoterRegistration.getId() != 0 ) {
                try {
                    getPendingVoterRegistrationService().delete(pendingVoterRegistration);
                } catch (final Exception e1) {
                    logger.error("Failed to clean up pending voter registration after error", e);
                }
            }
        }

        return pendingVoterRegistration;
   	}

    public AdvancedPdfGeneratorFactory getAdvancedPdfGeneratorFactory() {
        return advancedPdfGeneratorFactory;
    }

    public CipherService getCipherService() {
        return cipherService;
    }

    public PdfFormTrackService getPdfFormTrackService() {
        return pdfFormTrackService;
    }

    public PendingVoterRegistrationService getPendingVoterRegistrationService() {
        return pendingVoterRegistrationService;
    }

    public QuestionFieldService getQuestionFieldService() {
        return questionFieldService;
    }
}
