package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.DependentRoot;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: May 5, 2008
 * Time: 9:13:15 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "question_dependencies")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "kind")
public abstract class BasicDependency implements Serializable {
    private static final long serialVersionUID = -6627080967913420067L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "variant_id")
    private Related dependent;

    protected BasicDependency() {
    }

    protected BasicDependency( Related dependent ) {
        this.dependent = dependent;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public Related getDependent() {
        return dependent;
    }

    public void setDependent( Related dependent ) {
        this.dependent = dependent;
    }

    public abstract DependentRoot getDependsOn();

    public abstract String getConditionName();

    public abstract boolean checkDependency( WizardContext wizardContext );

    public abstract boolean checkGroup( Object factor );

    public abstract String getDependsOnName();
}
