package com.bearcode.ovf.model.questionnaire;

import com.google.common.base.MoreObjects;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 21, 2007
 * Time: 6:53:32 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "question_fields")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
public class QuestionField implements Serializable {
    private static final long serialVersionUID = 3809751215353641792L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "order")
    @Min(1)
    private int order = 1;

    @Transient
    private int oldOrder;

    @Column(name = "title")
    @NotBlank(message = "{rava.admin.fieldtype.empty_name}")
    private String title = "";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id")
    @NotNull(message = "{rava.admin.field.missing_type}")
    private FieldType type;

    @OneToMany(mappedBy = "forField", fetch = FetchType.EAGER, targetEntity = GenericStringItem.class)
    @OrderBy("string_value")
    private Collection<FieldDictionaryItem> genericOptions;

    @ManyToOne
    @JoinColumn(name = "question_variant_id")
    @NotNull
    private QuestionVariant question;

    @Column(name = "help_text")
    private String helpText = "";

    @Column(name = "additional_help")
    private String additionalHelp = "";

    @Column(name = "encoded")
    private boolean encoded = false;

    @Column(name = "security")
    private boolean security = false;

    @Column(name = "required")
    private boolean required = false;

    @Column(name = "verification_pattern")
    private String verificationPattern = "";

    @Column(name = "in_pdf_name")
    private String inPdfName = "";

    @Column(name = "data_role")
    private String dataRole = "";

    @OneToMany(mappedBy = "dependent", targetEntity = FieldDependency.class)
    private Collection<FieldDependency> fieldDependencies;

    /**
     * Additional text field for any case.
     * If we'd need to add some dynamical values or text. Not mapped to the DB.
     */
    @Transient
    private String firstText = "";

    /**
     * One more additional text field.
     */
    @Transient
    private String secondText = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public FieldType getType() {
        return type;
    }

    public void setType( FieldType type ) {
        this.type = type;
    }

    public Collection<FieldDictionaryItem> getGenericOptions() {
        return genericOptions;
    }

    public void setGenericOptions( Collection<FieldDictionaryItem> genericOptions ) {
        this.genericOptions = genericOptions;
    }

    public QuestionVariant getQuestion() {
        return question;
    }

    public void setQuestion( QuestionVariant question ) {
        this.question = question;
    }

    public String getHelpText() {
        return helpText;
    }

    public void setHelpText( String helpText ) {
        this.helpText = helpText;
    }

    public String getAdditionalHelp() {
        return additionalHelp;
    }

    public void setAdditionalHelp( String additionalHelp ) {
        this.additionalHelp = additionalHelp;
    }

    public boolean isEncoded() {
        return encoded;
    }

    public void setEncoded( boolean encoded ) {
        this.encoded = encoded;
    }

    public boolean isSecurity() {
        return security;
    }

    public void setSecurity( boolean security ) {
        this.security = security;
    }


    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired( boolean required ) {
        this.required = required;
    }

    public int getOldOrder() {
        return oldOrder;
    }

    public void setOldOrder( int oldOrder ) {
        this.oldOrder = oldOrder;
    }


    public String getVerificationPattern() {
        return verificationPattern;
    }

    public void setVerificationPattern( String verificationPattern ) {
        this.verificationPattern = verificationPattern;
    }

    public String getInPdfName() {
        return inPdfName;
    }

    public void setInPdfName( String inPdfName ) {
        this.inPdfName = inPdfName;
    }

    public String getFirstText() {
        return firstText;
    }

    public void setFirstText( String firstText ) {
        this.firstText = firstText;
    }

    public String getSecondText() {
        return secondText;
    }

    public void setSecondText( String secondText ) {
        this.secondText = secondText;
    }

    public String getDataRole() {
        return dataRole;
    }

    public void setDataRole( String dataRole ) {
        this.dataRole = dataRole;
    }

    public Collection<FieldDependency> getFieldDependencies() {
        return fieldDependencies;
    }

    public void setFieldDependencies( Collection<FieldDependency> fieldDependencies ) {
        this.fieldDependencies = fieldDependencies;
    }

    @Transient
    public Collection<FieldDictionaryItem> getOptions() {
        if ( type == null ) {
            return new LinkedList<FieldDictionaryItem>();
        }
        if ( type.isGenericOptionsAllowed() ) {
            return genericOptions;
        } else {
            return type.getFixedOptions();
        }
    }

    public Answer createAnswer() {
        Answer answer = type.createAnswerOfType();
        if ( answer != null ) answer.setField( this );
        return answer;
    }

    public boolean equals( Object o ) {
        if ( o == null ) return false;
        if ( !(o instanceof QuestionField) ) return false;
        return ((QuestionField) o).getId() == getId();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper( this )
                .add( "id", getId() )
                .add( "type", getType() )
                .add( "title", getTitle() )
                .add( "inPdfName", getInPdfName() )
                .toString();
    }
}
