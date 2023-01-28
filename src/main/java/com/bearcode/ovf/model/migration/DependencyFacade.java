package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.*;

/**
 * Date: 19.12.11
 * Time: 20:15
 *
 * @author Leonid Ginzburg
 */
public class DependencyFacade extends AbleToMigrate {
    private long id;
    private long dependsOnId;
    private long conditionId;
    private long dependsOnMigrationId;
    private long conditionMigrationId;
    private long faceId;
    private String fieldName;
    private String fieldValue;
    private DependencyClassType classType;

    public DependencyFacade() {
    }

    public DependencyFacade( BasicDependency dependency ) {
        id = dependency.getId();
        if ( dependency instanceof QuestionDependency ) {
            classType = DependencyClassType.QUESTION_DEPENDENCY;
            dependsOnId = ((Question)dependency.getDependsOn()).getId();
            conditionId = ((QuestionDependency) dependency).getCondition().getId();
        }
        else if ( dependency instanceof FlowDependency ) {
            classType = DependencyClassType.FLOW_DEPENDENCY;
            fieldValue = dependency.getConditionName();
        }
        else if ( dependency instanceof FaceDependency ) {
            classType = DependencyClassType.FACE_DEPENDENCY;
            faceId = ((FaceConfig)dependency.getDependsOn()).getId();
        }
        else if ( dependency instanceof UserFieldDependency ) {
            classType = DependencyClassType.USER_FIELD_DEPENDENCY;
            fieldName = ((UserFieldDependency) dependency).getFieldName();
            fieldValue = ((UserFieldDependency) dependency).getFieldValue();
        }
    }

    public BasicDependency createDependency() {
        BasicDependency dependency = null;
        switch ( classType ) {
            case QUESTION_DEPENDENCY:
                dependency = new QuestionDependency();
                break;
            case FLOW_DEPENDENCY:
                dependency = new FlowDependency();
                break;
            case FACE_DEPENDENCY:
                dependency = new FaceDependency();
                break;
            case USER_FIELD_DEPENDENCY:
                dependency = new UserFieldDependency();
                break;
        }
        exportTo( dependency );
        return dependency;
    }

    public void exportTo( BasicDependency dependency ) {
        switch ( classType ) {
            case QUESTION_DEPENDENCY:
                break;
            case USER_FIELD_DEPENDENCY:
                if ( dependency instanceof UserFieldDependency ) {
                    ((UserFieldDependency)dependency).setFieldName( fieldName );
                    ((UserFieldDependency)dependency).setFieldValue( fieldValue );
                }
                break;
            case FLOW_DEPENDENCY:
                if ( dependency instanceof FlowDependency ) {
                    ((FlowDependency)dependency).setFlowType( FlowType.valueOf( fieldValue ) );
                }
                break;
            case FACE_DEPENDENCY:
                break;
        }
    }

    public boolean matchClass( BasicDependency dependency ) {
        Class desired = null;
        switch ( classType ) {
            case FACE_DEPENDENCY:
                desired = FaceDependency.class;
                break;
            case FLOW_DEPENDENCY:
                desired = FlowDependency.class;
                break;
            case QUESTION_DEPENDENCY:
                desired = QuestionDependency.class;
                break;
            case USER_FIELD_DEPENDENCY:
                desired = UserFieldDependency.class;
                break;
        }
        return dependency.getClass().equals( desired );
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getDependsOnId() {
        return dependsOnId;
    }

    public void setDependsOnId( long dependsOnId ) {
        this.dependsOnId = dependsOnId;
    }

    public long getConditionId() {
        return conditionId;
    }

    public void setConditionId( long conditionId ) {
        this.conditionId = conditionId;
    }

    public long getFaceId() {
        return faceId;
    }

    public void setFaceId( long faceId ) {
        this.faceId = faceId;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName( String fieldName ) {
        this.fieldName = fieldName;
    }

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue( String fieldValue ) {
        this.fieldValue = fieldValue;
    }

    public DependencyClassType getClassType() {
        return classType;
    }

    public void setClassType( DependencyClassType classType ) {
        this.classType = classType;
    }

    public long getDependsOnMigrationId() {
        return dependsOnMigrationId;
    }

    public void setDependsOnMigrationId( long dependsOnMigrationId ) {
        this.dependsOnMigrationId = dependsOnMigrationId;
    }

    public long getConditionMigrationId() {
        return conditionMigrationId;
    }

    public void setConditionMigrationId( long conditionMigrationId ) {
        this.conditionMigrationId = conditionMigrationId;
    }

    @Override
    public String getBaseClassName() {
        switch ( classType ) {
            case FACE_DEPENDENCY:
                return FaceDependency.class.getSimpleName();
            case FLOW_DEPENDENCY:
                return FlowDependency.class.getSimpleName();
            case QUESTION_DEPENDENCY:
                return QuestionDependency.class.getSimpleName();
            case USER_FIELD_DEPENDENCY:
                return UserFieldDependency.class.getSimpleName();
        }
        return "";
    }
}
