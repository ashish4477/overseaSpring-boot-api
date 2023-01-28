package com.bearcode.ovf.actions.questionnaire.admin.forms;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.VoterHistory;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.PageType;
import com.bearcode.ovf.model.questionnaire.WizardResults;

/**
 * Date: 24.11.11
 * Time: 14:54
 *
 * @author Leonid Ginzburg
 */
public class QuestionnaireViewForm {
    private FlowType flowType = FlowType.RAVA;
    private String state = "AL";
    private VoterType voterType = VoterType.DOMESTIC_VOTER;
    private VoterHistory voterHistory = VoterHistory.FIRST_TIME_VOTER;
    private long faceConfigId = 0;

    public QuestionnaireViewForm( FaceConfig defaultConfig ) {
        faceConfigId = defaultConfig.getId();
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType( FlowType flowType ) {
        this.flowType = flowType;
    }

    public PageType getFormType() {
        return flowType.getPageType();
    }

    public String getState() {
        return state;
    }

    public void setState( String state ) {
        this.state = state;
    }

    public VoterType getVoterType() {
        return voterType;
    }

    public void setVoterType( VoterType voterType ) {
        this.voterType = voterType;
    }

    public VoterHistory getVoterHistory() {
        return voterHistory;
    }

    public void setVoterHistory( VoterHistory voterHistory ) {
        this.voterHistory = voterHistory;
    }

    public long getFaceConfigId() {
        return faceConfigId;
    }

    public void setFaceConfigId( long faceConfigId ) {
        this.faceConfigId = faceConfigId;
    }

    public WizardContext getWizardContext() {
        final WizardContext wizardContext = new WizardContext();
        final WizardResults wizardResults = new WizardResults( flowType );
        wizardResults.setVoterType( voterType.getName() );
        wizardResults.setVoterHistory( voterHistory.getName() );
        wizardResults.getVotingAddress().setState( state );
        wizardContext.setWizardResults( wizardResults );
        return wizardContext;
    }
}
