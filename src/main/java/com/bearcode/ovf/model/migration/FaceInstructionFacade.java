package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * Date: 26.12.11
 * Time: 17:23
 *
 * @author Leonid Ginzburg
 */
public class FaceInstructionFacade {
    private FlowType flowType;
    private String text;

    public FaceInstructionFacade() {
    }

    public FaceInstructionFacade( FaceFlowInstruction instruction ) {
        flowType = instruction.getFlowType();
        text = instruction.getText();
    }

    public void exportTo( FaceFlowInstruction instruction ) {
        instruction.setFlowType( flowType );
        instruction.setText( text );
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType( FlowType flowType ) {
        this.flowType = flowType;
    }

    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }
}
