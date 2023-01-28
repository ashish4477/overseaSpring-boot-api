package com.bearcode.ovf.model.questionnaire;

import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 21, 2007
 * Time: 6:59:49 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "question_variants")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class QuestionVariant extends Related {
    private static final long serialVersionUID = -4802949583134595219L;

    @Column(name = "title")
    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String title = "";

    @Column(name = "description")
    private String description = "";

    @ManyToOne
    @JoinColumn(name = "question_id")
    @NotNull
    private Question question;

    @OneToMany(mappedBy = "question")
    private Collection<QuestionField> fields;

    @Column(name = "active", nullable = false, columnDefinition="tinyint(1) NOT NULL")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean active = true;

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public Collection<QuestionField> getFields() {
        return fields;
    }

    public void setFields( Collection<QuestionField> fields ) {
        this.fields = fields;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion( Question question ) {
        this.question = question;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
