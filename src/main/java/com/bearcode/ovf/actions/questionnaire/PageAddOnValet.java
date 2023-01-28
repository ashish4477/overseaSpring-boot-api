/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import java.util.Collection;

import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.vip.VipContest;

/**
 * Interface for objects that provide resources to {@link AllowedForAddOn}.
 * 
 * TODO: consider pulling the VIP specific parts into extended valet.
 * 
 * @author IanBrown
 * 
 * @since Aug 7, 2012
 * @version Oct 1, 2013
 */
public interface PageAddOnValet {
	
	/**
	 * the string displayed for write-in candidates.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2013
	 * @version Aug 14, 2013
	 */
	public static final String WRITE_IN = "LEAVE BLANK FOR WRITE-IN";

	/**
	 * Acquires a field type for multiple checkboxes.
	 * 
	 * @author IanBrown
	 * @return the multiple checkbox field type.
	 * @since Oct 1, 2012
	 * @version Oct 1, 2012
	 */
	FieldType acquireMultipleCheckboxesFieldType();

	/**
	 * Creates a simple field of the specified type for the specified variant.
	 * 
	 * @author IanBrown
	 * @param variant
	 *            the question variant.
	 * @param id
	 *            the identifier for the field.
	 * @param fieldType
	 *            the type of field.
	 * @param title
	 *            the title of the field.
	 * @param helpText
	 *            the help text for the field.
	 * @return the question field.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	QuestionField createField(QuestionVariant variant, Long id, FieldType fieldType, String title, String helpText);

	/**
	 * Creates a group for the specified page.
	 * 
	 * @author IanBrown
	 * @param page
	 *            the page.
	 * @param id
	 *            the identifier for the group.
	 * @param name
	 *            the name of the group.
	 * @param title
	 *            the title of the group.
	 * @return the question group.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	Question createGroup(QuestionnairePage page, Long id, String name, String title);

	/**
	 * Creates options from the contest.
	 * 
	 * @author IanBrown
	 * @param standard
	 *            is this a standard contest?
	 * @param contest
	 *            the contest.
	 * @param partisanParty the name of the partisan party - used by Minnesota to choose primary candidates.
	 * @param noPartyName the name to use to indicate that the candidate is not in a party.
	 * @return the options.
	 * @since Aug 7, 2012
	 * @version Oct 1, 2013
	 */
	Collection<FieldDictionaryItem> createOptions(boolean standard, VipContest contest, String partisanParty, String noPartyName);

	/**
	 * Creates options for the list of values.
	 * @param optionValues the option values.
	 * @return the options.
	 */
	Collection<FieldDictionaryItem> createOptions(String[] optionValues);

	/**
	 * Creates a variant for the specified group.
	 * 
	 * @author IanBrown
	 * @param group
	 *          the question group.
	 * @param id
	 *          the identifier for the variant.
	 * @param title
	 *          the title of the variant.
	 * @return the question variant.
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	QuestionVariant createVariant(Question group, Long id, String title);
}
