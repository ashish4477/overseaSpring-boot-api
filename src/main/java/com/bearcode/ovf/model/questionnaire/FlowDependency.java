package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.model.common.DependentRoot;
import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 2, 2008
 * Time: 5:03:15 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("FLOW")
public class FlowDependency extends BasicDependency {
    private static final long serialVersionUID = 6317032749543873475L;

    @Column(name = "field_value")
    @Enumerated(EnumType.STRING)
    private FlowType flowType = null;

    public FlowDependency() {
    }

    public FlowDependency( Related dependent, FlowType flowType ) {
        super( dependent );
        this.flowType = flowType;
    }

    public FlowType getFlowType() {
        return flowType;
    }

    public void setFlowType( FlowType flowType ) {
        this.flowType = flowType;
    }

    public DependentRoot getDependsOn() {
        return flowType;
    }

    public String getConditionName() {
        return flowType.toString();
    }

    public boolean checkDependency( WizardContext wizardContext ) {
        return flowType != null && flowType.equals( wizardContext.getFlowType() );
    }

    public boolean checkGroup( Object factor ) {
        return (factor instanceof FlowType);
    }

    public String getDependsOnName() {
        return "Flow";
    }
}
