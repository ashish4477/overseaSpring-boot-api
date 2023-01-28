package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValet;
import com.bearcode.ovf.tools.pdf.generator.ITextPdfGeneratorValetImpl;
import com.bearcode.ovf.tools.pdf.generator.PdfGeneratorUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;


/**
 * Date: 04.10.14
 * Time: 18:05
 *
 * @author Leonid Ginzburg
 */
public enum PdfTemplateForm {
    VOTE_LETTER("vote_letter.pdf"),
    STATE_FORM("form.pdf"),
    ADDENDUM("addendum.pdf");

    PdfTemplateForm(String templateFilename) {
        this.templateName = templateFilename;
        setValet(ITextPdfGeneratorValetImpl.getInstance());
    }

    /**
     * the string to indicate that the PDFs should come from the WAR.
     *
     * @author IanBrown
     * @since Mar 21, 2012
     * @version Mar 21, 2012
     */
    static final String PDF_USE_INTERNAL_PDFS = "internal";

    /**
     * the template for the generic files.
     *
     * @author IanBrown
     * @since Mar 21, 2012
     * @version Mar 21, 2012
     */
    static final String GENERIC_TEMPLATE_NAME = "%s/%s";

    /**
     * the logger for the object.
     *
     * @author IanBrown
     * @since Mar 21, 2012
     * @version Mar 21, 2012
     */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * the base directory for the PDFs.
     *
     * @author IanBrown
     * @since Mar 21, 2012
     * @version Mar 21, 2012
     */
    private static String pdfBaseDir = PDF_USE_INTERNAL_PDFS;

    /**
     * the simple name of the template.
     */
    private String templateName;

    private ITextPdfGeneratorValet valet;

    public static String getPdfBaseDir() {
        return pdfBaseDir;
    }

    /**
     * Opens a PDF template. This method is responsible for locating the PDF based on the flow type and voting state from the
     * context.
     *
     * @author IanBrown
     * @param wizardContext
     *            the wizard context.
     * @param language the language
     * @return the template reader (if the template can be located).
     * @throws java.io.IOException
     *             if there is a problem acquiring the input stream.
     * @since Mar 23, 2012
     * @version Apr 30, 2012
     */
    public InputStream openPdfTemplate( final WizardContext wizardContext, final String language ) throws IOException {
      final boolean useInternalPdfs = PDF_USE_INTERNAL_PDFS.equalsIgnoreCase(getPdfBaseDir());
      final String templatePath = getTemplatePath();
      final FlowType originalFlowType = wizardContext.getFlowType();
      final WizardResults results = wizardContext.getWizardResults();
        final FaceConfig faceConfig = wizardContext.getCurrentFace();
      final WizardResultAddress votingAddress = results == null ? null : results.getVotingAddress();
      final String originalState = votingAddress == null ? null : votingAddress.getState().toLowerCase();
      final String originalElectionName = PdfGeneratorUtil.getElectionName( results );

        logger.info( "Getting " + templateName + " for " + originalFlowType.name().toLowerCase() + " and " + originalState );
      String pdfTemplateName;
      boolean additionalPossibilities;
      String prefix = "";
      final StringBuilder sb = new StringBuilder();
      IOException lastException = null;
      final Stack<String> languagePrefixes = new Stack<String>();
      languagePrefixes.push("");
      if ( language != null && !language.trim().isEmpty()) {
        languagePrefixes.push(language.trim() + "/");
      }

      while (!languagePrefixes.isEmpty()) {
        final String languagePrefix = languagePrefixes.pop();
        FlowType flowType = originalFlowType;
               String flowName = originalFlowType.name();
               String faceName = faceConfig.getName().toLowerCase();
            String electionName = originalElectionName;
        String state = originalState;
        do {
                   additionalPossibilities = flowType != null;
                   StringBuffer shortName = new StringBuffer();
                   if ( flowType != null ) {
                       if ( faceName != null ) {
                           shortName.append(faceName).append("_");
                           if ( state != null ) {
                             if(!"skimm".equals(faceName) && !"vote411".equals(faceName)) {
                               shortName.append(state.toLowerCase()).append("_");
                             }
                               if ( electionName != null ) {
                                   shortName.append( electionName.toLowerCase() ).append( "_" );
                                   electionName = null;
                               }
                               else {
                                   state = null;
                               }
                           }
                           else {
                               state = originalState;
                               electionName = originalElectionName;
                               faceName = null;
                           }
                       }
                       else {
                           if ( state != null ) {
                               shortName.append(state.toLowerCase()).append("_");
                               if ( electionName != null ) {
                                   shortName.append( electionName.toLowerCase() ).append( "_" );
                                   electionName = null;
                               }
                               else {
                                   state = null;
                               }
                           }
                           else {
                               flowType = null;
                           }
                       }
                       shortName.append(flowName.toLowerCase()).append("_");
                   }
                   shortName.append(templateName);
                   pdfTemplateName = String.format(GENERIC_TEMPLATE_NAME, templatePath, languagePrefix + (additionalPossibilities ? (flowName + "/"):"") + shortName.toString());

          try {
            sb.append(prefix).append(pdfTemplateName);
            prefix = ", ";
            final InputStream inputStream = getValet().acquireInputStream(useInternalPdfs, pdfTemplateName);
            if (inputStream != null) {
               logger.debug("Using " + pdfTemplateName);
              if (inputStream.available() == 0) {
                // An empty file is handled as a special case. We simply ignore it.
                return null;
              }
              return inputStream;
            }

            lastException = new IOException("Failed to find " + sb.toString());
          } catch (final IOException e) {
            lastException = new IOException("Failed to open " + sb.toString(), e);
          }
        } while (additionalPossibilities);
      }

      throw lastException;
    }
    /**
   * Gets the path to the template folder.
   *
   * @author IanBrown
   * @return the template folder path.
   * @since Mar 21, 2012
   * @version Mar 21, 2012
   */
  private String getTemplatePath() {
    return PDF_USE_INTERNAL_PDFS.equalsIgnoreCase(getPdfBaseDir()) ? "pdfs" : getPdfBaseDir();
  }

    public ITextPdfGeneratorValet getValet() {
        return valet;
    }

    public void setValet( final ITextPdfGeneratorValet valet ) {
        this.valet = valet;
    }
}
