package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: May 5, 2008
 * Time: 9:15:29 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("FACE")
public class FaceDependency extends BasicDependency {
    private static final long serialVersionUID = 6549264935509663415L;

    @ManyToOne
    @JoinColumn(name = "face_id")
    private FaceConfig dependsOn;

    public FaceDependency() {
    }

    public FaceDependency( Related dependent, FaceConfig dependsOn ) {
        super( dependent );
        this.dependsOn = dependsOn;
    }

    public FaceConfig getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn( FaceConfig dependsOn ) {
        this.dependsOn = dependsOn;
    }

    public String getConditionName() {
        return this.dependsOn.getName();
    }

    public boolean checkDependency( WizardContext wizardContext ) {
        return wizardContext.getCurrentFace() != null &&
                dependsOn != null &&
                dependsOn.getName().equalsIgnoreCase( wizardContext.getCurrentFace().getName() );
    }

    public String getDependsOnName() {
        return "Face";
    }

    public boolean checkGroup( Object factor ) {
        return (factor instanceof FaceConfig);
    }
}
