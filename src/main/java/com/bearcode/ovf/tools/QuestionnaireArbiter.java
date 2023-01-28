package com.bearcode.ovf.tools;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.DependentRoot;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 28, 2007
 * Time: 3:07:08 PM
 * @author Leonid Ginzburg
 */
@Component
public class QuestionnaireArbiter {

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private QuestionnaireService questionnaireService;

    /**
     * Adjust page to the answers according to the dependencies.
     * Remove questions that should not be asked.
     * @param page Page with all questions and variants as it stored in the DB.
     * @param wizardContext Answers
     */
	public void adjustPage( QuestionnairePage page, WizardContext wizardContext ) {
		// refresh page - re-attach page to session to prevent LazyInitializeException
		page = questionnaireService.findPageById( page.getId() );

		for ( Iterator<Question> itQuestion = page.getQuestions().iterator(); itQuestion.hasNext(); ) {
			final Question question = itQuestion.next();
			adjustQuestion( question, wizardContext );
			
			if ( question.getVariants().isEmpty() ) {
				itQuestion.remove();
			}
		}
	}

    /**
     * Remove all variants that doesn't correspond to the current answers
     * @param question
     * @param wizardContext
     */
    private void adjustQuestion( Question question, WizardContext wizardContext ) {
        QuestionVariant defaultVariant = null;
        for (Iterator<QuestionVariant> itVariant = question.getVariants().iterator(); itVariant.hasNext(); ) {
            QuestionVariant variant = itVariant.next();
            if (!variant.isActive()) {
            	itVariant.remove();
            	continue;
			}
            if ( variant.isDefault() ) {
                if ( defaultVariant == null ) {
                    defaultVariant = variant;
                } else {
                    itVariant.remove(); // remove all other default variants.
                }
                continue;
            }
            if ( !checkDependency( variant, wizardContext ) ) {
                itVariant.remove();
            }
        }
        if ( question.getVariants().size() > 1 && defaultVariant != null ) {
            question.getVariants().remove( defaultVariant );
        }
    }

	/**
	 * Check if the object satisfies to the answers and the dependencies
	 * 
	 * @param dependent Cheking object
	 * @param wizardContext Answers
	 *
	 * @return True if object satisfies
	 */
	private boolean checkDependency( Related dependent, WizardContext wizardContext ) {
		DependentRoot grouping = null;
		boolean result = true;
		boolean groupStart = false;
		boolean resultForGroup = true;
		
		for ( BasicDependency key : dependent.getKeys() ) {
			// checking current dependency
			if ( !key.checkGroup( grouping ) ) {
				grouping = key.getDependsOn();
				groupStart = true;
			}

			if ( groupStart ) {
				groupStart = false;
				// result always is TRUE at this point. If it get FALSE we
				// return from this function immediately
				result = resultForGroup;
				if ( !result )
					break;

				resultForGroup = key.checkDependency( wizardContext );
			} else {
				resultForGroup = resultForGroup || key.checkDependency( wizardContext );
			}

		}
		return result && resultForGroup;
	}

    /**
     * Check instructions (PDF fillings) according to the dependencies.
     * Remove all instruction that should not be shown.
     * @param instructions  All instruction stored in the DB
     * @param wizardContext Answers
     */
	public void ajustInstructions( Collection<PdfFilling> instructions, WizardContext wizardContext ) {
		for ( Iterator<PdfFilling> it = instructions.iterator(); it.hasNext(); ) {
			Related dependent = it.next();
			if ( dependent.isDefault() || checkDependency( dependent, wizardContext ) )
				continue;
			it.remove();
		}
	}

    /**
     * Apply dependencies for any field that could depend on previous answer.
     * @param page Current page
     * @param dependencies Field dependencies
     * @param answers  Answers
     */
    public void applyFieldDependencies( QuestionnairePage page, Collection<FieldDependency> dependencies, Map<Long, Answer> answers ) {
		for ( Question question : page.getQuestions() ) {
			for ( QuestionVariant variant : question.getVariants() ) {
				// only one variant should exist
				for ( QuestionField field : variant.getFields() ) {
					for ( FieldDependency dependency : dependencies ) {
						if ( dependency.getDependent().getId() != field.getId() )
							continue;

						Answer answer = answers.get( dependency.getDependsOn().getKeyField().getId() );
						if ( answer == null )
							continue;

						field.getType().applyDependency( field, answer );
					}
				}
			}
		}
    }

    /**
     * This function removes from the form answers that is inconsistent with the current page.
     * The situation is possible when user goes back and change his answers.
     * According to the dependencies he needs to answer to different fields
     * and we need to remove "old" answers.
     * @param clearPage
     * @param filledForm
     */
    public void checkAnswer(QuestionnairePage clearPage, Map<Long, Answer> filledForm ) {
        Collection<Long> answerFields = filledForm.keySet();
        if ( answerFields.size() == 0 ) return;
        Collection<QuestionField> fieldsOfCurrentPage = questionFieldService.findAnswerFiedsOfPage( answerFields, clearPage);
        if ( fieldsOfCurrentPage.size() == 0 ) return;  // make it faster. answers haven't filled yet
        for( Question question : clearPage.getQuestions() ) {
            for( QuestionVariant variant : question.getVariants() ) {
                for ( QuestionField field : variant.getFields() ) {
                    if ( fieldsOfCurrentPage.contains(field) ) {
                        fieldsOfCurrentPage.remove(field);
                    }
                }
            }
        }
        
        if ( fieldsOfCurrentPage.isEmpty()) 
        	return;  //  all answers belong to fileds of the page
        
        for ( QuestionField incorrectField : fieldsOfCurrentPage ) {
            filledForm.remove( incorrectField.getId() );  // remove answers don't correspond to the current dependencies
        }
    }
}
