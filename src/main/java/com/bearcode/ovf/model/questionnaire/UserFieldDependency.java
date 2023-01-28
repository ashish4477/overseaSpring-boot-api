package com.bearcode.ovf.model.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.DependentRoot;
import com.bearcode.ovf.utils.UserInfoFields;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 13, 2007
 * Time: 4:08:02 PM
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("USER")
public class UserFieldDependency extends BasicDependency {
    private static final long serialVersionUID = 7512289208130056622L;

    @Column(name = "field_name")
    private String fieldName;

    @Column(name = "field_value")
    private String fieldValue;

    public UserFieldDependency() {
    }

    public UserFieldDependency( Related dependent, String fieldName, String fieldValue ) {
        super( dependent );
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    private class UserFieldDependentRoot implements DependentRoot {
        public String getName() {
            return fieldName;
        }
    }

    public DependentRoot getDependsOn() {
        return new UserFieldDependentRoot();
    }

    public String getConditionName() {
        return fieldValue;
    }

    public String getDependsOnName() {
        return "User field - " + fieldName;
    }

    public boolean checkDependency( WizardContext wizardContext ) {

        Map<String, String> formVals = UserInfoFields.getUserValues( wizardContext.getWizardResults() );
        for ( String field : UserInfoFields.getInstance().getDependencyFields() ) {
            if ( field.equals( fieldName ) ) {
                try {
                    return fieldValue.equals( formVals.get( fieldName ) );
                } catch ( Exception e ) {
                    break;
                }
            }
        }
        return false;
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

    public boolean checkGroup( Object factor ) {
        if ( factor instanceof UserFieldDependentRoot ) {
            UserFieldDependentRoot ufdr = (UserFieldDependentRoot) factor;
            return ufdr.getName() != null && ufdr.getName().equals( fieldName );
        }
        return false;
    }
}
