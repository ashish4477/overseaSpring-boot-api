package com.bearcode.ovf.model.questionnaire;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Date: 10.12.11
 * Time: 16:45
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("ADD_ON")
public class AddOnPage extends QuestionnairePage {
    private static final long serialVersionUID = 5780654069291873349L;

    @Transient
    @NotBlank
    private String beanName;

    public AddOnPage() {
    }

    public AddOnPage( String typeName ) {
        super( typeName );
    }

    public String getBeanName() {
        return beanName;
    }

    public void setBeanName( String beanName ) {
        setPopupBubble( beanName );
    }

    @Override
    public void setPopupBubble( String popupBubble ) {
        super.setPopupBubble( popupBubble );
        this.beanName = popupBubble;
    }
}
