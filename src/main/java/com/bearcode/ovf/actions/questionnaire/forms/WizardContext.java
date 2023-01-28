package com.bearcode.ovf.actions.questionnaire.forms;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionnaireService;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * WizardContext is a session based object for storing all wizard state data
 */
public class WizardContext implements Cloneable {

    private QuestionnairePage currentPage;

    private FaceConfig currentFace;

    private boolean loginOffered = false;

    /**
     * Number of pages in current flow. This is session dependent storage for the number to avoid multiple SQL questions in
     * SurveyWizard
     */
    private int pageCount;

    /**
     * User information and answers
     */
    private WizardResults wizardResults;

    /**
     * user to be stored in session along with this form in case of anonymous form filling
     */
    private OverseasUser pretenceOfUser;

    private String wizardUrlTemplate = "";

    private boolean flowFinished = false;

    public boolean isFlowFinished() {
        return flowFinished;
    }

    public void setFlowFinished( boolean flowFinished ) {
        this.flowFinished = flowFinished;
    }

    public WizardContext() {
        this.wizardResults = new WizardResults( FlowType.RAVA );
    }

    public WizardContext( final WizardResults wizardResults ) {
        this.wizardResults = wizardResults;
    }

    public Collection<Answer> anonymize() {
        return wizardResults.anonymize();
    }

    final public Map<String, String> createModel() {
        return createModel( true );
    }

    final public Map<String, String> createModel( boolean doXmlEscape ) {
        final Map<String, String> model = new HashMap<String, String>();
        for ( final Answer answer : getAnswers() ) {
            final QuestionField questionField = answer.getField();
            final FieldType fieldType = questionField.getType();
            answer.output( model, doXmlEscape );
            fieldType.addFiveCents( model, questionField, answer );
        }
        return model;
    }

    public final Answer getAnswerByFieldId( final long fieldId ) {
        return wizardResults.getAnswersAsMap().get( fieldId );
    }

    public final Collection<Answer> getAnswers() {
        return wizardResults.getAnswers();
    }

    public Map<Long, Answer> getAnswersAsMap() {
        return wizardResults.getAnswersAsMap();
    }

    public FaceConfig getCurrentFace() {
        return currentFace;
    }

    public QuestionnairePage getCurrentPage() {
        return currentPage;
    }

    public FlowType getFlowType() {
        return wizardResults.getFlowType();
    }

    public boolean isVoterAlertsOptIn() {
        return wizardResults.isOptIn();
    }

    public int getPageCount() {
        return pageCount;
    }

    public OverseasUser getPretenceOfUser() {
        return pretenceOfUser;
    }

    public WizardResults getWizardResults() {
        return wizardResults;
    }

    public boolean isFormCompleted() {
        return (getCurrentPage() != null && getPageCount() == getCurrentPage().getNumber());
    }

    public boolean isLoginOffered() {
        return loginOffered;
    }

    /**
     * Save Wizard Results from the Context into DB. We don't want to save empty addresses into DB and want to keep empty address
     * objects in the Results at the same time. To do so we make clone, remove empty addresses, save results and then restore
     * addresses. Cloning and restoring should be done outside of transaction because of cascade option of address.
     *
     * @param service Service to call save method
     */
    public void processSaveResults( final QuestionnaireService service ) {
        // no transaction here
        final WizardResults results = getWizardResults();
        if ( currentPage == null ) {
            results.setReportable( false );
        } else {
            results.setReportable( results.checkReportable() );
        }
        final WizardResults temp = results.createTemporary();
        /*if ( results.getUser() == null || results.getUser() == getPretenceOfUser() ) {
            results.anonymize();
        }*/
        // transaction is there
        service.saveWizardResults( results );

        results.copyFromTemporary( temp );
    }

    public void putAnswer( final Answer answer ) {
        if ( answer == null || answer.getField() == null ) {
            return;
        }
        this.wizardResults.putAnswer( answer );
    }

    public void setCurrentFace( final FaceConfig currentFace ) {
        this.currentFace = currentFace;
        if ( currentFace != null ) {
            wizardResults.setFaceUrl( currentFace.getUrlPath() );
        }
    }

    public void setCurrentPage( final QuestionnairePage currentPage ) {
        this.currentPage = currentPage;
        wizardResults.setCurrentPageTitle( currentPage != null ? currentPage.getTitle() : "" );
    }

    public void setLoginOffered( final boolean loginOffered ) {
        this.loginOffered = loginOffered;
    }

    public void setPageCount( final int pageCount ) {
        this.pageCount = pageCount;
    }

    public void setPretenceOfUser( final OverseasUser pretenceOfUser ) {
        this.pretenceOfUser = pretenceOfUser;
    }

    public void setWizardResults( final WizardResults wizardResults ) {
        if ( wizardResults != null ) {
            this.wizardResults = wizardResults;
        }
    }

    public String getWizardUrlTemplate() {
        return wizardUrlTemplate;
    }

    public void setWizardUrlTemplate(String wizardUrlTemplate) {
        this.wizardUrlTemplate = wizardUrlTemplate;
    }

    public String createFlowPageUrl( final String flow, final int page ) {
        if ( wizardUrlTemplate.matches("[^%]*%\\w.*%\\w[^%]*") ) {
            return String.format( wizardUrlTemplate, flow.toLowerCase(), page );
        } else if ( wizardUrlTemplate.matches("[^%]*/%\\w[^%]*") ){
            return String.format( wizardUrlTemplate, page );
        }
        return "";
    }
}
