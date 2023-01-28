package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.*;

import javax.persistence.Transient;

/**
 * Date: 15.10.12
 * Time: 15:01
 *
 * @author Leonid Ginzburg
 */
public class FieldTypeFacade extends AbleToMigrate {
    private long id;
    private String name;
    private String templateName;
    private String adminTemplate;
    private boolean genericOptionsAllowed;
    private boolean verificationPatternApplicable;

    private FieldTypeClassType classType;

    public FieldTypeFacade() {
    }

    public FieldTypeFacade( FieldType fieldType ) {
        id = fieldType.getId();
        name = fieldType.getName();
        templateName = fieldType.getTemplateName();
        adminTemplate = fieldType.getAdminTemplate();
        genericOptionsAllowed = fieldType.isGenericOptionsAllowed();
        verificationPatternApplicable = fieldType.isVerificationPatternApplicable();
        if ( fieldType instanceof FieldTypeSingleValue ) {
            classType = FieldTypeClassType.FIELD_SINGLE_VALUE;
        }
        else if ( fieldType instanceof FieldTypeGenericDictionary ) {
            classType = FieldTypeClassType.FIELD_GENERIC_DICTIONARY;
        }
        else if ( fieldType instanceof FieldTypeFixedDictionary ) {
            classType = FieldTypeClassType.FIELD_FIXED_DICTIONARY;
        }
        else if ( fieldType instanceof FieldTypeDate ) {
            classType = FieldTypeClassType.FIELD_DATE;
        }
        else if ( fieldType instanceof FieldTypeReplicator ) {
            classType = FieldTypeClassType.FIELD_REPLICATOR;
        }
        else  {
            // error   TODO
            //throw
        }
    }

    public FieldType createFieldType() {
        FieldType fieldType = null;
        switch ( classType ) {
            case FIELD_SINGLE_VALUE:
                fieldType = new FieldTypeSingleValue();
                break;
            case FIELD_DATE:
                fieldType = new FieldTypeDate();
                break;
            case FIELD_FIXED_DICTIONARY:
                fieldType = new FieldTypeFixedDictionary();
                break;
            case FIELD_GENERIC_DICTIONARY:
                fieldType = new FieldTypeGenericDictionary();
                break;
            case FIELD_REPLICATOR:
                fieldType = new FieldTypeReplicator();
                break;
        }
        exportTo( fieldType );
        return fieldType;
    }

    public void exportTo( FieldType fieldType ) {
        fieldType.setName( name );
        fieldType.setTemplateName( templateName );
        fieldType.setAdminTemplate( adminTemplate );
        fieldType.setGenericOptionsAllowed( genericOptionsAllowed );
        fieldType.setVerificationPatternApplicable( verificationPatternApplicable );
    }

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

    public boolean isGenericOptionsAllowed() {
        return genericOptionsAllowed;
    }

    public void setGenericOptionsAllowed( boolean genericOptionsAllowed ) {
        this.genericOptionsAllowed = genericOptionsAllowed;
    }

    public boolean isVerificationPatternApplicable() {
        return verificationPatternApplicable;
    }

    public void setVerificationPatternApplicable( boolean verificationPatternApplicable ) {
        this.verificationPatternApplicable = verificationPatternApplicable;
    }

    public FieldTypeClassType getClassType() {
        return classType;
    }

    public void setClassType( FieldTypeClassType classType ) {
        this.classType = classType;
    }

    @Transient
    public String getBaseClassName() {
        switch ( classType ) {
            case FIELD_SINGLE_VALUE:
                return FieldTypeSingleValue.class.getSimpleName();
            case FIELD_DATE:
                return FieldTypeDate.class.getSimpleName();
            case FIELD_FIXED_DICTIONARY:
                return FieldTypeFixedDictionary.class.getSimpleName();
            case FIELD_GENERIC_DICTIONARY:
                return FieldTypeGenericDictionary.class.getSimpleName();
            case FIELD_REPLICATOR:
                return FieldTypeReplicator.class.getSimpleName();
        }
        return "";
    }
}
