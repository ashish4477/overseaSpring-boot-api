/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.DependentRoot;
import com.bearcode.ovf.model.questionnaire.*;

import java.util.Collection;
import java.util.List;

/**
 * Abstract implementation of {@link AllowedForAddOn} for add ons that simply fill in the values for existing fields.
 * 
 * @author IanBrown
 * 
 * @since Apr 24, 2012
 * @version Sep 13, 2012
 */
public abstract class AbstractFieldFillerPageAddOn implements AllowedForAddOn {

	/** {@inheritDoc} */
	@Override
	public Long getFirstFieldId(final QuestionnairePage currentPage) {
		// By default, none of these pages will dynamically create new fields.
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void prepareAddOnPage(final WizardContext form, final QuestionnairePage currentPage) {
		final List<Question> questions = currentPage.getQuestions();

		if (questions != null) {
			for (final Question question : questions) {
				prepareAddOnQuestion(form, question);
			}
		}
	}

	/**
	 * Prepares the variant by adding on the information if the variant contains the fields for that information.
	 * 
	 * @author IanBrown
	 * @param form
	 *            the form.
	 * @param variant
	 *            the variant.
	 * @since Apr 23, 2012
	 * @version Apr 24, 2012
	 */
	protected abstract void prepareAddOnVariant(WizardContext form, QuestionVariant variant);

	/**
	 * Checks the variant's dependencies against the form.
	 * 
	 * @author IanBrown
	 * @param form
	 *            the form.
	 * @param variant
	 *            the variant.
	 * @return <code>true</code> if the variant's dependencies are met, <code>false</code> otherwise.
	 * @since Apr 23, 2012
	 * @version Sep 13, 2012
	 */
	@SuppressWarnings("unused")
	private boolean checkVariantDependencies(final WizardContext form, final QuestionVariant variant) {
		boolean success = true;
		final Collection<BasicDependency> keys = variant.getKeys();

		if ((keys != null) && !keys.isEmpty()) {
			DependentRoot grouping = null;
			boolean forceCheckEntry = true;
			for (final BasicDependency key : keys) {
				if (!key.checkGroup(grouping)) {
					grouping = key.getDependsOn();
					if (!success) {
						return false;
					}
					forceCheckEntry = true;
					success = true;
				}

				if (!forceCheckEntry && success) {
					continue;
				}
				forceCheckEntry = false;

				if ((key instanceof FaceDependency) || (key instanceof FlowDependency) || (key instanceof UserFieldDependency)) {
					success = key.checkDependency(form);

				} else {
					throw new UnsupportedOperationException("Not yet implemented " + key);
				}
			}
		}

		return success;
	}

	/**
	 * Prepares the question by adding on the information if the question contains a valid variant with the need for such
	 * information.
	 * 
	 * @author IanBrown
	 * @param form
	 *            the wizard context form.
	 * @param question
	 *            the question.
	 * @since Apr 23, 2012
	 * @version Jul 12, 2012
	 */
	private void prepareAddOnQuestion(final WizardContext form, final Question question) {
		final Collection<QuestionVariant> variants = question.getVariants();

		if (variants != null) {
			for (final QuestionVariant variant : variants) {
				// TODO Not sure that we want to check the variants here - they are checked later.
				//if (checkVariantDependencies(form, variant)) {
					prepareAddOnVariant(form, variant);
				//}
			}
		}
	}
}
