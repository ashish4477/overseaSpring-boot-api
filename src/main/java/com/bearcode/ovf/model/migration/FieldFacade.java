package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.FieldDependency;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.QuestionField;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Date: 19.12.11
 * Time: 18:37
 *
 * @author Leonid Ginzburg
 */
public class FieldFacade extends AbleToMigrate {
    private long id;
    private int order;
    private String title ;
    private long typeId;
    private long variantId;
    private String helpText;
    private String additionalHelp;
    private boolean encoded;
    private boolean security;
    private boolean required;
    private String verificationPattern;
    private String inPdfName;
    private String dataRole;

    private Collection<FieldDictionaryItemFacade> items = new LinkedList<FieldDictionaryItemFacade>();
    private Collection<FieldDependencyFacade> dependencies = new LinkedList<FieldDependencyFacade>();

    private long typeMigrationId;

    public FieldFacade() {
    }

    public FieldFacade( QuestionField field ) {
        id = field.getId();
        order = field.getOrder();
        title = field.getTitle();
        helpText = field.getHelpText();
        additionalHelp = field.getAdditionalHelp();
        encoded = field.isEncoded();
        security = field.isSecurity();
        required = field.isRequired();
        verificationPattern = field.getVerificationPattern();
        inPdfName = field.getInPdfName();
        typeId = field.getType().getId();
        variantId = field.getQuestion().getId();
        dataRole = field.getDataRole();

        for ( FieldDictionaryItem item : field.getGenericOptions() ) {
            items.add( new FieldDictionaryItemFacade( item ) );
        }

        for ( FieldDependency dependency : field.getFieldDependencies() ) {
            dependencies.add( new FieldDependencyFacade( dependency ) );
        }

        // fix errors on fly
        if ( title.trim().isEmpty() ) {
            title = "temporary title";
        }
    }

    public QuestionField createField() {
        QuestionField field = new QuestionField();
        exportTo( field );
        field.setGenericOptions( new LinkedList<FieldDictionaryItem>() );
        field.setFieldDependencies( new LinkedList<FieldDependency>() );
        return field;
    }

    public void exportTo( QuestionField field ) {
        field.setOrder( order );
        field.setTitle( title );
        field.setHelpText( helpText );
        field.setAdditionalHelp( additionalHelp );
        field.setEncoded( encoded );
        field.setSecurity( security );
        field.setRequired( required );
        field.setVerificationPattern( verificationPattern );
        field.setInPdfName( inPdfName );
        field.setDataRole( dataRole );
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId( long typeId ) {
        this.typeId = typeId;
    }

    public long getVariantId() {
        return variantId;
    }

    public void setVariantId( long variantId ) {
        this.variantId = variantId;
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

    public boolean isRequired() {
        return required;
    }

    public void setRequired( boolean required ) {
        this.required = required;
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

    public Collection<FieldDictionaryItemFacade> getItems() {
        return items;
    }

    public void setItems( Collection<FieldDictionaryItemFacade> items ) {
        this.items = items;
    }

    public String getDataRole() {
        return dataRole;
    }

    public void setDataRole( String dataRole ) {
        this.dataRole = dataRole;
    }

    public Collection<FieldDependencyFacade> getDependencies() {
        return dependencies;
    }

    public void setDependencies( Collection<FieldDependencyFacade> dependencies ) {
        this.dependencies = dependencies;
    }

    public long getTypeMigrationId() {
        return typeMigrationId;
    }

    public void setTypeMigrationId( long typeMigrationId ) {
        this.typeMigrationId = typeMigrationId;
    }

    @Override
    public String getBaseClassName() {
        return QuestionField.class.getSimpleName();
    }

    @Override
    public long assignMigrationId( Map<String, Long> outputMap, Collection<MigrationId> createdIds, long migrationId, int version ) {
        long mId = super.assignMigrationId( outputMap, createdIds, migrationId, version );
        for ( FieldDictionaryItemFacade itemFacade : items ) {
            mId = itemFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        for ( FieldDependencyFacade fieldDependencyFacade : dependencies ) {
            mId = fieldDependencyFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        return mId;
    }
}
