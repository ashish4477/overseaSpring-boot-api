package com.bearcode.ovf.actions.questionnaire.admin.forms;

import com.bearcode.ovf.model.questionnaire.BasicDependency;
import com.bearcode.ovf.model.questionnaire.DependencyType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.Related;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Date: 08.12.11
 * Time: 20:29
 *
 * @author Leonid Ginzburg
 */
public class DependencyGroupForm {

    private DependencyType type;
    private Related dependent;
    private Collection<BasicDependency> dependencies;
    /**
     * this is for co-working with web interface. derived from 'dependencies'
     */
    private Collection<String> selectedCondition;
    /**
     * related question. for Question dependency type only.
     */
    private Question dependsOn;
    /**
     * User field name. for USER dependency type only
     */
    private String fieldName;

    public DependencyType getType() {
        return type;
    }

    public void setType( DependencyType type ) {
        this.type = type;
    }

    public Related getDependent() {
        return dependent;
    }

    public void setDependent( Related dependent ) {
        this.dependent = dependent;
    }

    public Collection<BasicDependency> getDependencies() {
        return dependencies;
    }

    public void setDependencies( Collection<BasicDependency> dependencies ) {
        this.dependencies = dependencies;
        //populateConditions();
    }

    public void populateConditions() {
        selectedCondition = new LinkedList<String>();
        if ( dependencies == null ) return;
        for ( BasicDependency key : dependencies ) {
            selectedCondition.add( key.getConditionName() );
        }
    }

    public Collection<String> getSelectedCondition() {
        return selectedCondition;
    }

    public void setSelectedCondition( Collection<String> selectedCondition ) {
        this.selectedCondition = selectedCondition;
    }

    public Question getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn( Question dependsOn ) {
        this.dependsOn = dependsOn;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName( String fieldName ) {
        this.fieldName = fieldName;
    }
}
