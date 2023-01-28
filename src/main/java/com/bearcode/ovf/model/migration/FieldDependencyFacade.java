package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.FieldDependency;

/**
 * Date: 23.12.11
 * Time: 13:28
 *
 * @author Leonid Ginzburg
 */
public class FieldDependencyFacade extends AbleToMigrate {
    private long id;
    private long fieldId;
    private long dependsOnId;
    private long dependsOnMigrationId;

    public FieldDependencyFacade() {
    }

    public FieldDependencyFacade( FieldDependency dependency ) {
        id = dependency.getId();
        fieldId = dependency.getDependent().getId();
        dependsOnId = dependency.getDependsOn().getId();
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getFieldId() {
        return fieldId;
    }

    public void setFieldId( long fieldId ) {
        this.fieldId = fieldId;
    }

    public long getDependsOnId() {
        return dependsOnId;
    }

    public void setDependsOnId( long dependsOnId ) {
        this.dependsOnId = dependsOnId;
    }

    public long getDependsOnMigrationId() {
        return dependsOnMigrationId;
    }

    public void setDependsOnMigrationId( long dependsOnMigrationId ) {
        this.dependsOnMigrationId = dependsOnMigrationId;
    }

    @Override
    public String getBaseClassName() {
        return FieldDependency.class.getSimpleName();
    }
}
