package com.bearcode.ovf.model.questionnaire;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Sep 24, 2007
 * Time: 3:16:27 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Table(name = "field_dependencies")
public class FieldDependency implements Serializable {
    private static final long serialVersionUID = 5641328744051089207L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "field_id")
    @NotNull
    private QuestionField dependent;

    @ManyToOne
    @JoinColumn(name = "depends_on")
    @NotNull
    private Question dependsOn;


    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public QuestionField getDependent() {
        return dependent;
    }

    public void setDependent( QuestionField dependent ) {
        this.dependent = dependent;
    }

    public Question getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn( Question dependsOn ) {
        this.dependsOn = dependsOn;
    }
}
