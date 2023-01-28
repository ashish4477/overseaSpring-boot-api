package com.bearcode.ovf.model.questionnaire;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * Date: 10.12.11
 * Time: 16:47
 *
 * @author Leonid Ginzburg
 */
@Entity
@org.hibernate.annotations.Entity(dynamicUpdate = true, dynamicInsert = true)
@DiscriminatorValue("EXTERNAL")
public class ExternalPage extends QuestionnairePage {
    private static final long serialVersionUID = 5966655050093480558L;

    @Transient
    @NotBlank
    private String externalLink;

    public ExternalPage() {
    }

    public ExternalPage( String typeName ) {
        super( typeName );
    }

    public String getExternalLink() {
        return externalLink;
    }

    public void setExternalLink( String externalLink ) {
        setPopupBubble( externalLink );
    }

    @Override
    public void setPopupBubble( String popupBubble ) {
        super.setPopupBubble( popupBubble );
        this.externalLink = popupBubble;
    }
}
