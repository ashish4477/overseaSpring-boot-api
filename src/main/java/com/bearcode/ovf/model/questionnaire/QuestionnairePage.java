package com.bearcode.ovf.model.questionnaire;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 13, 2007
 * Time: 9:21:03 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "questionary_pages")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("")
public class QuestionnairePage implements Serializable {
    private static final long serialVersionUID = -5184123328057325424L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "number")
    @Min(1)
    private int number = 1;
    @Transient
    private int oldNumber;

    @Column(name = "title")
    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String title = "";

    @Column(name = "popupBubble")
    private String popupBubble = "";

    @Column(name = "stepNumber")
    @Min(1)
    private int stepNumber = 1;

    @Column(name = "form_type")
    @Enumerated(value = EnumType.STRING)
    private PageType type = PageType.OVERSEAS;

    @OneToMany(mappedBy = "page", fetch = FetchType.EAGER)
    @OrderBy(value = "order")
    private List<Question> questions;

    public QuestionnairePage() {
    }

    public QuestionnairePage( String typeName ) {
        try {
            type = PageType.valueOf( typeName );
        } catch ( Exception e ) {
            // use default value
        }
    }

    public String getPopupBubble() {
        return popupBubble;
    }

    public void setPopupBubble( String popupBubble ) {
        this.popupBubble = popupBubble;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber( int stepNumber ) {
        this.stepNumber = stepNumber;
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber( int number ) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        if ( questions == null ) {
            questions = new ArrayList<Question>();
        }
        return questions;
    }

    public void setQuestions( List<Question> questions ) {
        this.questions = questions;
    }

    public int getOldNumber() {
        return oldNumber;
    }

    public void setOldNumber( int oldNumber ) {
        this.oldNumber = oldNumber;
    }

    public boolean isEmpty() {
        return questions == null || questions.isEmpty();
    }

    public PageType getType() {
        return type;
    }

    public void setType( PageType type ) {
        this.type = type;
    }
}
