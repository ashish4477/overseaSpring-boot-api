package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Date: 19.12.11
 * Time: 18:30
 *
 * @author Leonid Ginzburg
 */
public class VariantFacade extends AbleToMigrate implements RelatedFacade {
    private long id;
    private String title;
    private String description;
    private long groupId;

    private Collection<FieldFacade> fields = new LinkedList<FieldFacade>();
    private Collection<DependencyFacade> dependencies = new LinkedList<DependencyFacade>();

    public VariantFacade() {
    }

    public VariantFacade( QuestionVariant variant ) {
        id = variant.getId();
        title = variant.getTitle();
        description = variant.getDescription();
        groupId = variant.getQuestion().getId();

        for ( QuestionField field : variant.getFields() ) {
            fields.add( new FieldFacade( field ) );
        }

        for ( BasicDependency key : variant.getKeys() ) {
            dependencies.add( new DependencyFacade( key ) );
        }

        // fix errors on fly
        if ( title.trim().isEmpty() ) {
            title = "temporary title";
        }
    }

    public QuestionVariant createVariant() {
        QuestionVariant variant = new QuestionVariant();
        exportTo( variant );
        variant.setFields( new LinkedList<QuestionField>() );
        variant.setKeys( new LinkedList<BasicDependency>() );
        return variant;
    }

    public void exportTo( QuestionVariant variant) {
        variant.setTitle( title );
        variant.setDescription( description );
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription( String description ) {
        this.description = description;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId( long groupId ) {
        this.groupId = groupId;
    }

    public Collection<FieldFacade> getFields() {
        return fields;
    }

    public void setFields( Collection<FieldFacade> fields ) {
        this.fields = fields;
    }

    public Collection<DependencyFacade> getDependencies() {
        return dependencies;
    }

    public void setDependencies( Collection<DependencyFacade> dependencies ) {
        this.dependencies = dependencies;
    }

    @Override
    public String getBaseClassName() {
        return QuestionVariant.class.getSimpleName();
    }

    @Override
    public long assignMigrationId( Map<String, Long> outputMap, Collection<MigrationId> createdIds, long migrationId, int version ) {
        long mId = super.assignMigrationId( outputMap, createdIds, migrationId, version );
        for ( FieldFacade fieldFacade : fields ) {
            mId = fieldFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        for ( DependencyFacade dependencyFacade : dependencies ) {
            mId = dependencyFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        return mId;
    }
}
