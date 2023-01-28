package com.bearcode.ovf.actions.questionnaire;

/**
 * Date: 09.12.11
 * Time: 23:25
 *
 * @author Leonid Ginzburg
 *
 * Interface is intended for defining controllers which could be used for external redirect
 * from the SurveyWizard.
 * Such controller should implement this interface and also define "RequestMapping" annotation.
 * Url from RequestMapping annotation could be written in page description.
 * When SurveyWizard comes to that page it will call redirect to the url.
 *
 * The controller should use WizardContext object stored in the session to define its command object.
 * It is free to use any view or any number of steps before it should return control to the SurveyWizard.
 *
 * The interface is used only for automatically define controllers (and mapped urls) on
 * CreateQuestionnairePage step in the administration UI.
 */
public interface AllowedForRedirect {
}
