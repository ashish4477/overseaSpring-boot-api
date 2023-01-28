package com.bearcode.ovf.model.questionnaire;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: Jul 24, 2007
 * Time: 8:28:25 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@Table(name = "question_field_types")
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class FieldType implements Serializable {
    private static final long serialVersionUID = -1615251759789052568L;

    public static final String TEMPLATE_SELECT = "select";
    public static final String TEMPLATE_TEXT = "text";
    public static final String TEMPLATE_TEXTAREA = "textarea";
    public static final String TEMPLATE_CHECKBOX = "checkbox";
    public static final String TEMPLATE_RADIO = "radio";
    public static final String TEMPLATE_NOT_INPUT = "no_input";
    public static final String TEMPLATE_DATE = "date";
    public static final String TEMPLATE_CHECKBOX_FILLED = "checkbox_filled";
    public static final String TEMPLATE_TEXT_CONFIRM = "text_confirm";
    public static final String TEMPLATE_REPLICA = "replica";
    public static final String TEMPLATE_DISABLED_INPUT = "replica_plus_input";

    /**
     * db key for the country FieldType
     */
    public static final long COUNTRY_FIELDTYPE_ID = 8;

    public static final String MAILING_LIST_TYPE_NAME = "mail-in";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name")
    private String name;

    @Column(name = "view_template")
    private String templateName;

    @Column(name = "admin_template")
    private String adminTemplate;

    @Column(name = "use_generic_options")
    private boolean genericOptionsAllowed;

    @Column(name = "use_pattern")
    private boolean verificationPatternApplicable;

    @OneToMany(mappedBy = "fieldType", fetch = FetchType.EAGER)
    private List<FieldDictionaryItem> fixedOptions;


    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName( String templateName ) {
        this.templateName = templateName;
    }

    public String getAdminTemplate() {
        return adminTemplate;
    }

    public void setAdminTemplate( String adminTemplate ) {
        this.adminTemplate = adminTemplate;
    }

    public List<FieldDictionaryItem> getFixedOptions() {
        return fixedOptions;
    }

    public void setFixedOptions( List<FieldDictionaryItem> fixedOptions ) {
        this.fixedOptions = fixedOptions;
    }

    public boolean isVerificationPatternApplicable() {
        return verificationPatternApplicable;
    }

    public void setVerificationPatternApplicable( boolean verificationPatternApplicable ) {
        this.verificationPatternApplicable = verificationPatternApplicable;
    }

    public boolean isGenericOptionsAllowed() {
        return genericOptionsAllowed;
    }

    public void setGenericOptionsAllowed( boolean genericOptionsAllowed ) {
        this.genericOptionsAllowed = genericOptionsAllowed;
    }

    /**
     *
     * @return List of available templates
     */
    public static List<String> getTemplateNames() {
        String[] names = new String[] {
                TEMPLATE_SELECT,
                TEMPLATE_RADIO,
                TEMPLATE_TEXT,
                TEMPLATE_TEXT_CONFIRM,
                TEMPLATE_TEXTAREA,
                TEMPLATE_CHECKBOX,
                TEMPLATE_CHECKBOX_FILLED,
                TEMPLATE_NOT_INPUT,
                TEMPLATE_DATE,
                TEMPLATE_REPLICA,
                TEMPLATE_DISABLED_INPUT
        };
        return Arrays.asList( names );
    }

    abstract public FieldDictionaryItem createGenericItem();

    public abstract Answer createAnswerOfType();

    /**
     * The function allows to set(customize) field according the answer entered for another field.
     * Used with @see (com.bearcode.ovf.model.questionnaire.FieldDependency).
     * Need for creating Voting Regoins select for previously selected State.
     * @param value Answer for previous question
     */
    public void applyDependency( Answer value ) {

    }

    /**
     * The function allows to set(customize) field according the answer entered for another field.
     *
     * @param dependent Current question field
     * @param value     Answer
     */
    public void applyDependency( QuestionField dependent, Answer value ) {
        applyDependency( value );
    }

    public boolean isDependenciesAllowed() {
        return false;
    }

    /**
     * For add some parameters to PDF form.
     *
     * @param model Model map for creating a PDF form
     * @param field Current field
     * @param value Current answer
     */
    public void addFiveCents( Map<String, String> model, QuestionField field, Answer value ) {
        // do nothing. could be overridden.
    }

    public boolean isMailingListSignUp() {
        return name.toLowerCase().contains( MAILING_LIST_TYPE_NAME );
    }

}
