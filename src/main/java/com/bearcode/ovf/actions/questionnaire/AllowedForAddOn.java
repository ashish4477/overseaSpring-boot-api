package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;

/**
 * Date: 09.12.11
 * Time: 23:28
 *
 * @author Leonid Ginzburg
 *
 * Interface is intended to define bean could be used for Add-On.
 * Bean should fill current page with questions and fields according to its logic.
 * It could use getFirstFieldId to define id of fields. This function could use firstFieldId
 * and currentPage number for example to make sure field ids from this page do not intersect
 * with any other field ids.
 */
public interface AllowedForAddOn {
    public static final long firstFieldId = 1000000;

    public void prepareAddOnPage( WizardContext form, QuestionnairePage currentPage );
    public Long getFirstFieldId( QuestionnairePage currentPage );
}
