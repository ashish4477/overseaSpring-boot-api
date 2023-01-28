/**
 * 
 */
package com.bearcode.ovf.model;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import com.bearcode.ovf.DAO.QuestionFieldDAO;
import com.bearcode.ovf.model.common.AddressType;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VoterHistory;
import com.bearcode.ovf.model.common.VoterType;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.common.WizardResultPerson;
import com.bearcode.ovf.model.mobile.MobileAddress;
import com.bearcode.ovf.model.mobile.MobileAnswer;
import com.bearcode.ovf.model.mobile.MobileDependency;
import com.bearcode.ovf.model.mobile.MobileGroup;
import com.bearcode.ovf.model.mobile.MobileOption;
import com.bearcode.ovf.model.mobile.MobilePage;
import com.bearcode.ovf.model.mobile.MobilePerson;
import com.bearcode.ovf.model.mobile.MobileQuestion;
import com.bearcode.ovf.model.mobile.MobileResults;
import com.bearcode.ovf.model.mobile.MobileVariant;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.EnteredAnswer;
import com.bearcode.ovf.model.questionnaire.EnteredDateAnswer;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.PredefinedAnswer;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.utils.BlockLogger;

/**
 * Utility class for testing the building of answers for questions provided via the mobile.
 * 
 * @author IanBrown
 * 
 * @since May 11, 2012
 * @version Jul 10, 2012
 */
public final class MobileAnswerUtility {

	/**
	 * Internal class used to represent a variant that is being worked on.
	 * 
	 * @author IanBrown
	 * 
	 * @since May 3, 2012
	 * @version May 30, 2012
	 */
	public static final class CurrentVariant {

		/**
		 * flag indicating whether the question for this variant just been completed - the variant will be kept around until the end
		 * of the processing. At that point, variants with additional questions will be moved forward to the next question. Variants
		 * with no further questions are considered done and the current record is removed.
		 * 
		 * @author IanBrown
		 * @since May 4, 2012
		 * @version May 11, 2012
		 */
		private boolean completed = true;

		/**
		 * the variant being worked.
		 * 
		 * @author IanBrown
		 * @since May 3, 2012
		 * @version May 11, 2012
		 */
		private final MobileVariant variant;

		/**
		 * are there additional questions for this variant?
		 * 
		 * @author IanBrown
		 * @since May 30, 2012
		 * @version May 30, 2012
		 */
		private boolean additionalQuestions;

		/**
		 * the current question within the variant that is to be changed over time.
		 * 
		 * @author IanBrown
		 * @since May 3, 2012
		 * @version May 11, 2012
		 */
		private MobileQuestion question;

		/**
		 * the options that can be provided for the question.
		 * 
		 * @author IanBrown
		 * @since May 3, 2012
		 * @version May 11, 2012
		 */
		private Stack<?> questionOptions;

		/**
		 * Constructs a current variant for the specified mobile variant.
		 * 
		 * @author IanBrown
		 * @param variant
		 *            the mobile variant.
		 * @since May 3, 2012
		 * @version May 11, 2012
		 */
		public CurrentVariant(final MobileVariant variant) {
			this.variant = variant;
		}

		/**
		 * Gets the question.
		 * 
		 * @author IanBrown
		 * @return the question.
		 * @since May 3, 2012
		 * @version May 30, 2012
		 */
		public final MobileQuestion getQuestion() {
			return question;
		}

		/**
		 * Gets the question options.
		 * 
		 * @author IanBrown
		 * @return the question options.
		 * @since May 3, 2012
		 * @version May 11, 2012
		 */
		public Stack<?> getQuestionOptions() {
			return questionOptions;
		}

		/**
		 * Gets the variant.
		 * 
		 * @author IanBrown
		 * @return the variant.
		 * @since May 3, 2012
		 * @version May 30, 2012
		 */
		public final MobileVariant getVariant() {
			return variant;
		}

		/**
		 * Gets the additional questions.
		 * 
		 * @author IanBrown
		 * @return the additional questions.
		 * @since May 30, 2012
		 * @version May 30, 2012
		 */
		public boolean isAdditionalQuestions() {
			return additionalQuestions;
		}

		/**
		 * Gets the completed flag.
		 * 
		 * @author IanBrown
		 * @return the completed flag.
		 * @since May 4, 2012
		 * @version May 30, 2012
		 */
		public final boolean isCompleted() {
			return completed;
		}

		/**
		 * Sets the additional questions.
		 * 
		 * @author IanBrown
		 * @param additionalQuestions
		 *            the additional questions to set.
		 * @since May 30, 2012
		 * @version May 30, 2012
		 */
		public void setAdditionalQuestions(final boolean additionalQuestions) {
			this.additionalQuestions = additionalQuestions;
		}

		/**
		 * Sets the completed.
		 * 
		 * @author IanBrown
		 * @param completed
		 *            the completed to set.
		 * @since May 4, 2012
		 * @version May 30, 2012
		 */
		public final void setCompleted(final boolean completed) {
			this.completed = completed;
		}

		/**
		 * Sets the question.
		 * 
		 * @author IanBrown
		 * @param question
		 *            the question to set.
		 * @since May 3, 2012
		 * @version May 30, 2012
		 */
		public final void setQuestion(final MobileQuestion question) {
			this.question = question;
		}

		/**
		 * Sets the question options.
		 * 
		 * @author IanBrown
		 * @param questionOptions
		 *            the question options to set.
		 * @since May 3, 2012
		 * @version May 30, 2012
		 */
		public final void setQuestionOptions(final Stack<?> questionOptions) {
			this.questionOptions = questionOptions;
		}

		/** {@inheritDoc} */
		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder(getClass().getSimpleName());
			sb.append(" Variant: ").append(getVariant().getTitle());
			if (isCompleted() || getQuestion() == null) {
				sb.append(" (No question)");
			} else {
				sb.append(" Question: ").append(getQuestion().getTitle()).append(" Options: ").append(getQuestionOptions());
			}
			return sb.toString();
		}
	}

	/**
	 * Enumeration of the types of results fields supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	public static enum ResultsFieldType {

		/**
		 * an optional address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		ANY_ADDRESS,

		/**
		 * a required address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		ANY_ADDRESS_REQUIRED,

		/**
		 * an optional string field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		STRING,

		/**
		 * a required string field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		STRING_REQUIRED,

		/**
		 * an optional integer field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		INTEGER,

		/**
		 * a required integer field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		INTEGER_REQUIRED,

		/**
		 * an optional military address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		MILITARY_ADDRESS,

		/**
		 * a required military address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		MILITARY_ADDRESS_REQUIRED,

		/**
		 * an optional street address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		STREET_ADDRESS,

		/**
		 * a required street address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		STREET_ADDRESS_REQUIRED,

		/**
		 * an optional US address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		US_ADDRESS,

		/**
		 * a required US address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		US_ADDRESS_REQUIRED,

		/**
		 * an optional overseas address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		OVERSEAS_ADDRESS,

		/**
		 * a required overseas address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		OVERSEAS_ADDRESS_REQUIRED,

		/**
		 * an optional person field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		PERSON,

		/**
		 * a required person field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		PERSON_REQUIRED,

		/**
		 * a voter type field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		VOTER_TYPE,

		/**
		 * a voter history field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		VOTER_HISTORY,

		/**
		 * a gender field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		GENDER,

		/**
		 * a race field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		RACE,

		/**
		 * a party field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		PARTY,

		/**
		 * an optional other address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		OTHER_ADDRESS,

		/**
		 * a required other address field.
		 * 
		 * @author IanBrown
		 * @since May 11, 2012
		 * @version May 11, 2012
		 */
		OTHER_ADDRESS_REQUIRED
	}

	/**
	 * Interface for objects representing options for result fields.
	 * 
	 * @author IanBrown
	 * 
	 * @param <V>
	 *            the type of value returned by the option.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	public static interface WorkResultOption<V> {

		/**
		 * Gets the value for the option.
		 * 
		 * @author IanBrown
		 * @return the value.
		 * @since Apr 17, 2012
		 * @version Apr 17, 2012
		 */
		V getValue();
	}

	/**
	 * Class to represent the results being worked.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	public final static class WorkResults {

		/**
		 * the options for the fields of the result.
		 * 
		 * @author IanBrown
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		private final Map<String, Iterator<WorkResultOption<?>>> fieldOptions = new LinkedHashMap<String, Iterator<WorkResultOption<?>>>();

		/**
		 * Gets the field options.
		 * 
		 * @author IanBrown
		 * @return the field options.
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		public final Map<String, Iterator<WorkResultOption<?>>> getFieldOptions() {
			return fieldOptions;
		}
	}

	/**
	 * Abstract implementation of {@link WorkResultOption}.
	 * 
	 * @author IanBrown
	 * 
	 * @param <V>
	 *            the type of value returned by the option.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static abstract class AbstractWorkResultOption<V> implements WorkResultOption<V> {

		/**
		 * the value returned by the option.
		 * 
		 * @author IanBrown
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		private final V value;

		/**
		 * Constructs a work result option for the specified value.
		 * 
		 * @author IanBrown
		 * @param value
		 *            the value.
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		protected AbstractWorkResultOption(final V value) {
			this.value = value;
		}

		/** {@inheritDoc} */
		@Override
		public final V getValue() {
			return value;
		}
	}

	/**
	 * Extended {@link AbstractWorkResultOption} for an address.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private final static class WorkResultAddress extends AbstractWorkResultOption<WizardResultAddress> {

		/**
		 * Constructs an address work result option for the input value.
		 * 
		 * @author IanBrown
		 * @param value
		 *            the address value.
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		public WorkResultAddress(final WizardResultAddress value) {
			super(value);
		}
	}

	/**
	 * Extended {@link AbstractWorkResultOption} for an integer.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private final static class WorkResultInteger extends AbstractWorkResultOption<Integer> {

		/**
		 * Constructs an integer work result option for the input value.
		 * 
		 * @author IanBrown
		 * @param value
		 *            the integer value.
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		public WorkResultInteger(final Integer value) {
			super(value);
		}
	}

	/**
	 * Extended {@link AbstractWorkResultOption} for a person.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private final static class WorkResultPerson extends AbstractWorkResultOption<WizardResultPerson> {

		/**
		 * Constructs a person work result option for the input value.
		 * 
		 * @author IanBrown
		 * @param value
		 *            the person value.
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		public WorkResultPerson(final WizardResultPerson value) {
			super(value);
		}
	}

	/**
	 * Extended {@link AbstractWorkResultOption} for a string.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private final static class WorkResultString extends AbstractWorkResultOption<String> {

		/**
		 * Constructs a string work result option for the input value.
		 * 
		 * @author IanBrown
		 * @param value
		 *            the string value.
		 * @since Apr 17, 2012
		 * @version May 11, 2012
		 */
		public WorkResultString(final String value) {
			super(value);
		}
	}

	/**
	 * the format for the date.
	 * 
	 * @author IanBrown
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * the results fields.
	 * 
	 * @author IanBrown
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	public final static Map<String, ResultsFieldType> RESULTS_FIELDS = new LinkedHashMap<String, ResultsFieldType>();

	static {
		RESULTS_FIELDS.put("username", ResultsFieldType.STRING_REQUIRED);
		RESULTS_FIELDS.put("name", ResultsFieldType.PERSON_REQUIRED);
		RESULTS_FIELDS.put("previousName", ResultsFieldType.PERSON);
		RESULTS_FIELDS.put("phone", ResultsFieldType.STRING);
		RESULTS_FIELDS.put("alternateEmail", ResultsFieldType.STRING);
		RESULTS_FIELDS.put("alternatePhone", ResultsFieldType.STRING);
		RESULTS_FIELDS.put("currentAddress", ResultsFieldType.ANY_ADDRESS_REQUIRED);
		RESULTS_FIELDS.put("votingAddress", ResultsFieldType.US_ADDRESS_REQUIRED);
		RESULTS_FIELDS.put("forwardingAddress", ResultsFieldType.ANY_ADDRESS);
		RESULTS_FIELDS.put("previousAddress", ResultsFieldType.US_ADDRESS);
		RESULTS_FIELDS.put("voterType", ResultsFieldType.VOTER_TYPE);
		RESULTS_FIELDS.put("voterHistory", ResultsFieldType.VOTER_HISTORY);
		RESULTS_FIELDS.put("ballotPref", ResultsFieldType.STRING_REQUIRED);
		RESULTS_FIELDS.put("race", ResultsFieldType.RACE);
		RESULTS_FIELDS.put("ethnicity", ResultsFieldType.STRING);
		RESULTS_FIELDS.put("gender", ResultsFieldType.GENDER);
		RESULTS_FIELDS.put("party", ResultsFieldType.PARTY);
	}

	/**
	 * Builds the answers for the pages.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobilePages
	 *            the pages.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param blockLogger
	 *            the optional block logger.
	 * @return the answers.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	public static List<Answer> buildAnswersForPages(final QuestionFieldDAO questionFieldDAO, final List<MobilePage> mobilePages,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final BlockLogger blockLogger) {
		final List<Answer> answers = new LinkedList<Answer>();
		for (final MobilePage mobilePage : mobilePages) {
			buildAnswersForPage(questionFieldDAO, mobilePage, expandQuestions, currentVariants, answers, blockLogger);
		}

		if (!currentVariants.isEmpty()) {
			boolean moveToNextVariant;
			do {
				moveToNextVariant = false;
				final CurrentVariant currentVariant = currentVariants.peek();
				final Stack<?> questionOptions = currentVariant.getQuestionOptions();
				if (questionOptions != null) {
					questionOptions.pop();
				}
				if (questionOptions == null || questionOptions.isEmpty()) {
					if (currentVariant.isAdditionalQuestions() || currentVariants.size() == 1) {
						currentVariant.setQuestionOptions(null);
						currentVariant.setCompleted(true);
					} else {
						currentVariants.pop();
						moveToNextVariant = true;
					}
				}
			} while (moveToNextVariant && !currentVariants.isEmpty());
		}
		return answers;
	}

	/**
	 * Converts the input wizard results to mobile results.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @return the mobile results.
	 * @since May 11, 2012
	 * @version May 14, 2012
	 */
	public final static MobileResults convertWizardResultsToMobileResults(final WizardResults wizardResults) {
		final MobileResults mobileResults = new MobileResults();
		populateMobileResultsFromWizardResults(wizardResults, mobileResults);
		return mobileResults;
	}

	/**
	 * Populates the wizard results, varying only the current field.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param currentField
	 *            the current field.
	 * @param workResults
	 *            the work results.
	 * @param wizardResults
	 *            the wizard results.
	 * @param blockLogger
	 *            the optional block logger.
	 * @throws InvocationTargetException
	 *             if there is a problem invoking a set method.
	 * @throws IllegalAccessException
	 *             if there is a problem accessing the results.
	 * @throws IllegalArgumentException
	 *             if there is a problem with a value.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	public final static void populateWizardResults(final State state, final String currentField, final WorkResults workResults,
			final WizardResults wizardResults, final BlockLogger blockLogger) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		final Map<String, Iterator<WorkResultOption<?>>> fieldOptions = workResults.getFieldOptions();
		for (final Map.Entry<String, ResultsFieldType> fieldEntry : RESULTS_FIELDS.entrySet()) {
			final String fieldName = fieldEntry.getKey();
			final ResultsFieldType fieldType = fieldEntry.getValue();
			Iterator<WorkResultOption<?>> optionsItr = fieldOptions.get(fieldName);
			if (optionsItr == null) {
				optionsItr = createFieldOptions(state, fieldName, fieldType);
				if (currentField == fieldName) {
					fieldOptions.put(fieldName, optionsItr);
				}
			}

			populateField(fieldName, optionsItr.next(), wizardResults, blockLogger);

			if (!optionsItr.hasNext()) {
				fieldOptions.remove(fieldName);
			}
		}
	}

	/**
	 * Builds an answer for the input question.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO to retrieve the question fields.
	 * @param mobileQuestion
	 *            the mobile question.
	 * @param currentVariant
	 *            the current variant for the mobile variant (or <code>null</code> if there is none).
	 * @param currentVariants
	 *            the stack of current variants.
	 * @param needNewQuestion
	 *            <code>true</code> if a new question is needed for the variant, <code>false</code>otherwise</code>.
	 * @param answers
	 *            the answers.
	 * @param blockLogger
	 *            the optional block logger.
	 * @return <code>true</code> if a new question is needed for the variant, <code>false</code>otherwise</code>.
	 * @since May 3, 2012
	 * @version May 30, 2012
	 */
	private static boolean buildAnswerForQuestion(final QuestionFieldDAO questionFieldDAO, final MobileQuestion mobileQuestion,
			final CurrentVariant currentVariant, final Stack<CurrentVariant> currentVariants, final boolean needNewQuestion,
			final List<Answer> answers, final BlockLogger blockLogger) {
		final String fieldType = mobileQuestion.getFieldType();
		if (FieldType.TEMPLATE_DISABLED_INPUT.equals(fieldType) || FieldType.TEMPLATE_NOT_INPUT.equals(fieldType)) {
			if (blockLogger != null) {
				blockLogger.indentMessage("Question %s skipped", mobileQuestion.getTitle());
			}
			return needNewQuestion;
		}

		Stack<?> questionOptions = retrieveQuestionOptionsFromCurrentVariant(mobileQuestion, currentVariant);
		final int answerIdx = answers.size();

		final QuestionField questionField = mobileQuestion.getQuestionField();
		final QuestionField field = questionField == null ? questionFieldDAO.getById(mobileQuestion.getQuestionFieldId())
				: questionField;
		if (FieldType.TEMPLATE_CHECKBOX.equals(fieldType) || FieldType.TEMPLATE_CHECKBOX_FILLED.equals(fieldType)) {
			questionOptions = buildAnswerFromCheckbox(mobileQuestion, field, fieldType, questionOptions, answers);
		} else if (FieldType.TEMPLATE_DATE.equals(fieldType)) {
			questionOptions = buildAnswerFromDate(mobileQuestion, field, questionOptions, answers);
		} else if (FieldType.TEMPLATE_RADIO.equals(fieldType) || FieldType.TEMPLATE_SELECT.equals(fieldType)) {
			questionOptions = buildAnswerFromOptions(questionFieldDAO, mobileQuestion, field, questionOptions, answers);
		} else if (FieldType.TEMPLATE_REPLICA.equals(fieldType)) {
			throw new IllegalArgumentException("Cannot build answer for replica " + mobileQuestion.getTitle());
		} else if (FieldType.TEMPLATE_TEXT.equals(fieldType) || FieldType.TEMPLATE_TEXT_CONFIRM.equals(fieldType)
				|| FieldType.TEMPLATE_TEXTAREA.equals(fieldType)) {
			questionOptions = buildAnswerFromText(mobileQuestion, field, fieldType, questionOptions, answers);
		}
		if (blockLogger != null) {
			if (answerIdx == answers.size()) {
				blockLogger.indentMessage("Question %s not answered", mobileQuestion.getTitle());
			} else {
				blockLogger.indentMessage("Question %s=%s", mobileQuestion.getTitle(), answers.get(answerIdx).getValue());
			}
		}

		return updateCurrentVariant(mobileQuestion, currentVariant, needNewQuestion, questionOptions);
	}

	/**
	 * Builds an answer for a checkbox.
	 * 
	 * @author IanBrown
	 * @param mobileQuestion
	 *            the mobile question.
	 * @param field
	 *            the field for the question.
	 * @param fieldType
	 *            the type of checkbox.
	 * @param questionOptions
	 *            the stack of question options still available or <code>null</code> if there are none.
	 * @param answers
	 *            the answers - the answer for the question is added.
	 * @return the stack of question options still available (may be empty).
	 * @since May 3, 2012
	 * @version May 30, 2012
	 */
	@SuppressWarnings("unchecked")
	private static Stack<String> buildAnswerFromCheckbox(final MobileQuestion mobileQuestion, final QuestionField field,
			final String fieldType, final Stack<?> questionOptions, final List<Answer> answers) {
		final Stack<String> myQuestionOptions;
		if (questionOptions == null || questionOptions.isEmpty()) {
			myQuestionOptions = new Stack<String>();
			myQuestionOptions.push("true");
			myQuestionOptions.push("false");
			if (!mobileQuestion.isRequired()) {
				myQuestionOptions.push(null);
			}
		} else {
			myQuestionOptions = (Stack<String>) questionOptions;
		}

		final String enteredValue = myQuestionOptions.peek();
		if (enteredValue != null) {
			final EnteredAnswer answer = new EnteredAnswer();
			answer.setField(field);
			answer.setEnteredValue(enteredValue);
			answers.add(answer);
		}

		return myQuestionOptions;
	}

	/**
	 * Builds a date answer for the question.
	 * 
	 * @author IanBrown
	 * @param mobileQuestion
	 *            the question.
	 * @param field
	 *            the field for the question.
	 * @param questionOptions
	 *            the stack of question options still available or <code>null</code> if there are none.
	 * @param answers
	 *            the answers - an answer for the date question is added.
	 * @return the remaining options for the date answer.
	 * @since May 3, 2012
	 * @version May 30, 2012
	 */
	@SuppressWarnings("unchecked")
	private static Stack<Date> buildAnswerFromDate(final MobileQuestion mobileQuestion, final QuestionField field,
			final Stack<?> questionOptions, final List<Answer> answers) {
		final Stack<Date> myQuestionOptions;
		if (questionOptions == null || questionOptions.isEmpty()) {
			myQuestionOptions = new Stack<Date>();
			myQuestionOptions.add(new Date());
			if (!mobileQuestion.isRequired()) {
				myQuestionOptions.add(null);
			}
		} else {
			myQuestionOptions = (Stack<Date>) questionOptions;
		}

		final Date date = myQuestionOptions.peek();
		if (date != null) {
			final String dateAnswer = DATE_FORMAT.format(date);
			final EnteredDateAnswer answer = new EnteredDateAnswer();
			answer.setField(field);
			answer.setValue(dateAnswer);
			answers.add(answer);
		}

		return myQuestionOptions;
	}

	/**
	 * Builds an answer for the question from the options for the question.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO to retrieve question fields.
	 * @param mobileQuestion
	 *            the question.
	 * @param field
	 *            the field for the question.
	 * @param questionOptions
	 *            the stack of question options still available (may be <code>null</code>).
	 * @param answers
	 *            the answers - the answer for the question is added.
	 * @return the question options still available.
	 * @since May 3, 2012
	 * @version Jul 11, 2012
	 */
	@SuppressWarnings("unchecked")
	private static Stack<?> buildAnswerFromOptions(final QuestionFieldDAO questionFieldDAO, final MobileQuestion mobileQuestion,
			final QuestionField field, final Stack<?> questionOptions, final List<Answer> answers) {
		final Stack<MobileOption> myQuestionOptions;
		if (questionOptions == null || questionOptions.isEmpty()) {
			myQuestionOptions = buildQuestionOptions(mobileQuestion.getOptions());
			if (!mobileQuestion.isRequired() || myQuestionOptions.isEmpty()) {
				myQuestionOptions.push(null);
			}
		} else {
			myQuestionOptions = (Stack<MobileOption>) questionOptions;
		}

		final MobileOption questionOption = myQuestionOptions.peek();
		if (questionOption != null) {
			final PredefinedAnswer answer = new PredefinedAnswer();
			final FieldDictionaryItem optionItem = questionOption.getItem();
			final FieldDictionaryItem selectedValue = optionItem == null ? questionFieldDAO.getDictionaryItemById(questionOption
					.getId()) : optionItem;
			answer.setField(field);
			answer.setSelectedValue(selectedValue);
			answers.add(answer);
		}

		return myQuestionOptions;
	}

	/**
	 * Builds an answer for a text question.
	 * 
	 * @author IanBrown
	 * @param mobileQuestion
	 *            the question.
	 * @param field
	 *            the field for the question.
	 * @param fieldType
	 *            the type of text field.
	 * @param questionOptions
	 *            the stack of question options still available or <code>null</code> if there are none.
	 * @param answers
	 *            the answers - the answer for the question is added.
	 * @return the remaining options for answers.
	 * @since May 3, 2012
	 * @version May 17, 2012
	 */
	@SuppressWarnings("unchecked")
	private static Stack<String> buildAnswerFromText(final MobileQuestion mobileQuestion, final QuestionField field,
			final String fieldType, final Stack<?> questionOptions, final List<Answer> answers) {
		final Stack<String> myQuestionOptions;
		if (questionOptions == null || questionOptions.isEmpty()) {
			myQuestionOptions = new Stack<String>();
			myQuestionOptions.add(fieldType + " #" + mobileQuestion.getQuestionFieldId());
			if (!mobileQuestion.isRequired()) {
				myQuestionOptions.add(null);
			}
		} else {
			myQuestionOptions = (Stack<String>) questionOptions;
		}

		final String text = myQuestionOptions.peek();
		if (text != null) {
			final EnteredAnswer answer = new EnteredAnswer();
			answer.setField(field);
			answer.setEnteredValue(text);
			answers.add(answer);
		}

		return myQuestionOptions;
	}

	/**
	 * Builds answers for the group.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobileGroup
	 *            the group.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers - the answers for the group will be added.
	 * @param blockLogger
	 *            the optional block logger.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static void buildAnswersForGroup(final QuestionFieldDAO questionFieldDAO, final MobileGroup mobileGroup,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers,
			final BlockLogger blockLogger) {
		if (blockLogger != null) {
			blockLogger.startBlock("Start group %s", mobileGroup.getTitle());
		}
		final List<MobileVariant> mobileVariants = mobileGroup.getChildren();
		buildAnswersForVariants(questionFieldDAO, mobileVariants, expandQuestions, currentVariants, answers, blockLogger);
		if (blockLogger != null) {
			blockLogger.endBlock("End group %s", mobileGroup.getTitle());
		}
	}

	/**
	 * Builds the answers for the groups.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobileGroups
	 *            the groups.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers - the answers for the groups will be added.
	 * @param blockLogger
	 *            the optional block logger.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static void buildAnswersForGroups(final QuestionFieldDAO questionFieldDAO, final List<MobileGroup> mobileGroups,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers,
			final BlockLogger blockLogger) {
		for (final MobileGroup mobileGroup : mobileGroups) {
			buildAnswersForGroup(questionFieldDAO, mobileGroup, expandQuestions, currentVariants, answers, blockLogger);
		}
	}

	/**
	 * Builds the answers for the page.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobilePage
	 *            the page.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers - the answers for the page will be added.
	 * @param blockLogger
	 *            the optional block logger.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static void buildAnswersForPage(final QuestionFieldDAO questionFieldDAO, final MobilePage mobilePage,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers,
			final BlockLogger blockLogger) {
		if (blockLogger != null) {
			blockLogger.startBlock("Start page %s", mobilePage.getTitle());
		}
		final List<MobileGroup> mobileGroups = mobilePage.getChildren();
		buildAnswersForGroups(questionFieldDAO, mobileGroups, expandQuestions, currentVariants, answers, blockLogger);
		if (blockLogger != null) {
			blockLogger.endBlock("End page %s", mobilePage.getTitle());
		}
	}

	/**
	 * Builds answers for the questions.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobileQuestions
	 *            the questions.
	 * @param currentVariant
	 *            the current variant for the mobile variant (or <code>null</code> if there is none).
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers - the answers for the questions will be added.
	 * @param blockLogger
	 *            the optional block logger.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static void buildAnswersForQuestions(final QuestionFieldDAO questionFieldDAO,
			final List<MobileQuestion> mobileQuestions, final CurrentVariant currentVariant,
			final Stack<CurrentVariant> currentVariants, final List<Answer> answers, final BlockLogger blockLogger) {
		boolean wasCompleted = currentVariant != null && currentVariant.isCompleted();
		boolean needNewQuestion = false;
		boolean willNeedNewQuestion = false;

		for (final MobileQuestion mobileQuestion : mobileQuestions) {
			if (wasCompleted) {
				if (currentVariant.getQuestion() == null) {
					currentVariant.setQuestion(null);
					currentVariant.setQuestionOptions(null);
					currentVariant.setCompleted(false);
					needNewQuestion = true;
					wasCompleted = false;
					willNeedNewQuestion = false;
				} else if (currentVariant.getQuestion() == mobileQuestion) {
					willNeedNewQuestion = true;
				}
			}

			needNewQuestion = buildAnswerForQuestion(questionFieldDAO, mobileQuestion, currentVariant, currentVariants,
					needNewQuestion, answers, blockLogger);
			if (willNeedNewQuestion) {
				currentVariant.setQuestion(null);
				currentVariant.setQuestionOptions(null);
				currentVariant.setCompleted(false);
				needNewQuestion = true;
				wasCompleted = false;
				willNeedNewQuestion = false;
			}
		}

		if (needNewQuestion) {
			// The current variant has no more questions, we're done with it. This can only happen at the top of the stack.
			currentVariants.pop();
		}
	}

	/**
	 * Builds answers for the variant.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobileVariant
	 *            the variants.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers - the answers for the variant will be added.
	 * @param blockLogger
	 *            the optional block logger.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static void buildAnswersForVariant(final QuestionFieldDAO questionFieldDAO, final MobileVariant mobileVariant,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers,
			final BlockLogger blockLogger) {
		CurrentVariant currentVariant = findCurrentVariant(mobileVariant, currentVariants);
		if (currentVariant == null && !checkVariantDependencies(mobileVariant, expandQuestions, currentVariants, answers)) {
			if (blockLogger != null) {
				blockLogger.indentMessage("Variant %s skipped", mobileVariant.getTitle());
			}
			return;
		}

		if (currentVariant == null && !currentVariants.isEmpty() && currentVariants.peek().getVariant() == mobileVariant) {
			currentVariant = currentVariants.peek();
		}

		if (blockLogger != null) {
			blockLogger.startBlock("Start variant %s", mobileVariant.getTitle());
		}
		final List<MobileQuestion> mobileQuestions = mobileVariant.getChildren();
		buildAnswersForQuestions(questionFieldDAO, mobileQuestions, currentVariant, currentVariants, answers, blockLogger);
		if (blockLogger != null) {
			blockLogger.endBlock("End variant %s", mobileVariant.getTitle());
		}
	}

	/**
	 * Builds answers for the variants.
	 * 
	 * @author IanBrown
	 * @param questionFieldDAO
	 *            the DAO used to retrieve question fields.
	 * @param mobileVariants
	 *            the variants.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers - the answers for the variants will be added.
	 * @param blockLogger
	 *            the optional block logger.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static void buildAnswersForVariants(final QuestionFieldDAO questionFieldDAO, final List<MobileVariant> mobileVariants,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers,
			final BlockLogger blockLogger) {
		for (final MobileVariant mobileVariant : mobileVariants) {
			buildAnswersForVariant(questionFieldDAO, mobileVariant, expandQuestions, currentVariants, answers, blockLogger);
		}
	}

	/**
	 * Builds the question options from the input options.
	 * 
	 * @author IanBrown
	 * @param mobileOptions
	 *            the options.
	 * @return the question options stack.
	 * @since May 3, 2012
	 * @version May 17, 2012
	 */
	private static Stack<MobileOption> buildQuestionOptions(final List<MobileOption> mobileOptions) {
		final Stack<MobileOption> questionOptions = new Stack<MobileOption>();
		if (mobileOptions != null) {
			for (final MobileOption mobileOption : mobileOptions) {
				questionOptions.push(mobileOption);
			}
		}
		return questionOptions;
	}

	/**
	 * Checks the dependencies against the answers.
	 * 
	 * @author IanBrown
	 * @param mobileVariant
	 *            the variant that has the dependency.
	 * @param mobileDependencies
	 *            the dependencies.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers.
	 * @return <code>true</code> if a dependency matches the answers, <code>false</code> otherwise.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static boolean checkDependencies(final MobileVariant mobileVariant, final List<MobileDependency> mobileDependencies,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers) {
		for (final MobileDependency mobileDependency : mobileDependencies) {
			if (checkDependency(mobileVariant, mobileDependency, expandQuestions, currentVariants, answers)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks the dependency against the answers.
	 * 
	 * @author IanBrown
	 * @param mobileVariant
	 *            the variant that has the dependency.
	 * @param mobileDependency
	 *            the dependency.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers.
	 * @return <code>true</code> if an answer matches the dependency, <code>false</code> otherwise.
	 * @since May 3, 2012
	 * @version Jul 10, 2012
	 */
	private static boolean checkDependency(final MobileVariant mobileVariant, final MobileDependency mobileDependency,
			final boolean expandQuestions, final Stack<CurrentVariant> currentVariants, final List<Answer> answers) {
		for (final Answer answer : answers) {
			final QuestionField field = answer.getField();
			if (field != null && field.getId() == mobileDependency.getDependsOn() && answer instanceof PredefinedAnswer) {
				final PredefinedAnswer predefinedAnswer = (PredefinedAnswer) answer;
				if (predefinedAnswer.getSelectedValue().getId() == mobileDependency.getCondition()) {
					if (expandQuestions) {
						for (final CurrentVariant earlierVariant : currentVariants) {
							if (earlierVariant.getQuestion().getQuestionFieldId() == mobileDependency.getDependsOn()) {
								final CurrentVariant currentVariant = new CurrentVariant(mobileVariant);
								currentVariants.push(currentVariant);
								break;
							}
						}
					}
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks the dependencies for the variant against the answers.
	 * 
	 * @author IanBrown
	 * @param mobileVariant
	 *            the variant.
	 * @param expandQuestions
	 *            <code>true</code> to expand all of the questions and their answers, <code>false</code> to simply take the first
	 *            possible option.
	 * @param currentVariants
	 *            the stack of current working variants.
	 * @param answers
	 *            the answers.
	 * @return <code>true</code> if the variant has no dependencies or the dependencies are met, <code>false</code> otherwise.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static boolean checkVariantDependencies(final MobileVariant mobileVariant, final boolean expandQuestions,
			final Stack<CurrentVariant> currentVariants, final List<Answer> answers) {
		final List<MobileDependency> mobileDependencies = mobileVariant.getDependencies();
		if (mobileDependencies == null || mobileDependencies.isEmpty()) {
			if (expandQuestions && currentVariants.isEmpty()) {
				final CurrentVariant currentVariant = new CurrentVariant(mobileVariant);
				currentVariants.add(currentVariant);
			}
			return true;
		}

		return checkDependencies(mobileVariant, mobileDependencies, expandQuestions, currentVariants, answers);
	}

	/**
	 * Creates a described address option for the state and value.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state abbreviation.
	 * @param value
	 *            the value.
	 * @return the street address option.
	 * @since May 4, 2012
	 * @version May 11, 2012
	 */
	private static WorkResultAddress createDescribedAddressOption(final String state, final String value) {
		final WizardResultAddress describedAddress = new WizardResultAddress();
		describedAddress.setType(AddressType.DESCRIBED);
		describedAddress.setStreet1("1 " + value + " Street");
		describedAddress.setStreet2(value + " 2");
		describedAddress.setDescription("Description of " + value);
		describedAddress.setCity("City of " + value);
		describedAddress.setCounty(value + " County");
		describedAddress.setState(state);
		describedAddress.setZip("12345");
		describedAddress.setZip4("1234");
		final WorkResultAddress describedAddressOption = new WorkResultAddress(describedAddress);
		return describedAddressOption;
	}

	/**
	 * Creates the options for the specified field.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state.
	 * @param fieldName
	 *            the name of the type.
	 * @param fieldType
	 *            the type of the field.
	 * @return the iterator over the options for the field.
	 * @since Apr 17, 2012
	 * @version Jun 18, 2012
	 */
	private static Iterator<WorkResultOption<?>> createFieldOptions(final State state, final String fieldName,
			final ResultsFieldType fieldType) {
		final List<WorkResultOption<?>> fieldOptions = new LinkedList<WorkResultOption<?>>();

		switch (fieldType) {

		case ANY_ADDRESS:
			fieldOptions.add(null);
			// Fall through.
		case ANY_ADDRESS_REQUIRED:
			fieldOptions.add(0, createStreetAddressOption(state.getAbbr(), fieldName));
			fieldOptions.add(1, createRuralAddressOption(state.getAbbr(), fieldName));
			fieldOptions.add(2, createDescribedAddressOption(state.getAbbr(), fieldName));
			fieldOptions.add(3, createMilitaryAddressOption(fieldName));
			fieldOptions.add(4, createOverseasAddressOption(fieldName));
			break;

		case GENDER:
			fieldOptions.add(createStringOption("M"));
			fieldOptions.add(createStringOption("F"));
			break;

		case INTEGER:
			fieldOptions.add(null);
			// Fall through.
		case INTEGER_REQUIRED:
			fieldOptions.add(0, createIntegerOption(fieldName.hashCode()));
			break;

		case MILITARY_ADDRESS:
			fieldOptions.add(null);
			// Fall through.
		case MILITARY_ADDRESS_REQUIRED:
			fieldOptions.add(0, createMilitaryAddressOption(fieldName));
			break;

		case OTHER_ADDRESS:
			fieldOptions.add(null);
			// Fall through.
		case OTHER_ADDRESS_REQUIRED:
			fieldOptions.add(0, createMilitaryAddressOption(fieldName));
			fieldOptions.add(0, createOverseasAddressOption(fieldName));
			break;

		case OVERSEAS_ADDRESS:
			fieldOptions.add(null);
			// Fall through.
		case OVERSEAS_ADDRESS_REQUIRED:
			fieldOptions.add(0, createOverseasAddressOption(fieldName));
			break;

		case PARTY:
			fieldOptions.add(createStringOption("Party"));
			fieldOptions.add(null);
			break;

		case PERSON:
			fieldOptions.add(null);
			fieldOptions.add(0, createPerson(null, false, false, false));
			// Fall through.
		case PERSON_REQUIRED:
			fieldOptions.add(0, createPerson(fieldName, true, true, true));
			fieldOptions.add(1, createPerson(fieldName, false, false, false));
			fieldOptions.add(2, createPerson(fieldName, true, false, false));
			fieldOptions.add(3, createPerson(fieldName, true, true, false));
			fieldOptions.add(4, createPerson(fieldName, true, false, true));
			fieldOptions.add(5, createPerson(fieldName, false, true, false));
			fieldOptions.add(6, createPerson(fieldName, false, true, true));
			fieldOptions.add(7, createPerson(fieldName, false, false, true));
			break;

		case RACE:
			fieldOptions.add(createStringOption("Race"));
			fieldOptions.add(null);
			break;

		case STREET_ADDRESS:
			fieldOptions.add(null);
			// Fall through.
		case STREET_ADDRESS_REQUIRED:
			fieldOptions.add(0, createStreetAddressOption(state.getAbbr(), fieldName));
			break;

		case STRING:
			fieldOptions.add(null);
			// Fall through.
		case STRING_REQUIRED:
			fieldOptions.add(0, createStringOption(fieldName));
			break;

		case US_ADDRESS:
			fieldOptions.add(null);
			// Fall through
		case US_ADDRESS_REQUIRED:
			fieldOptions.add(0, createStreetAddressOption(state.getAbbr(), fieldName));
			fieldOptions.add(1, createRuralAddressOption(state.getAbbr(), fieldName));
			fieldOptions.add(2, createDescribedAddressOption(state.getAbbr(), fieldName));
			break;

		case VOTER_HISTORY:
			for (final VoterHistory voterHistory : VoterHistory.values()) {
				fieldOptions.add(createStringOption(voterHistory.name()));
			}
			break;

		case VOTER_TYPE:
			for (final VoterType voterType : VoterType.values()) {
				fieldOptions.add(createStringOption(voterType.name()));
			}
			break;

		default:
			throw new UnsupportedOperationException("No options implemented for " + fieldType);
		}

		return fieldOptions.iterator();
	}

	/**
	 * Creates an integer option with the specified value.
	 * 
	 * @author IanBrown
	 * @param value
	 *            the value.
	 * @return the integer option.
	 * @since Apr 17, 2012
	 * @version Apr 17, 2012
	 */
	private static WorkResultInteger createIntegerOption(final int value) {
		return new WorkResultInteger(value);
	}

	/**
	 * Creates a miltary address option for the specified value.
	 * 
	 * @author IanBrown
	 * @param value
	 *            the value.
	 * @return the overseas address option.
	 * @since May 4, 2012
	 * @version May 4, 2012
	 */
	private static WorkResultAddress createMilitaryAddressOption(final String value) {
		final WizardResultAddress militaryAddress = new WizardResultAddress();
		militaryAddress.setType(AddressType.MILITARY);
		militaryAddress.setStreet1("1 " + value + " Miltary");
		militaryAddress.setStreet2(value + " 2");
		militaryAddress.setCity(value + " City");
		militaryAddress.setState(value);
		militaryAddress.setZip("" + value.hashCode());
		final WorkResultAddress miltaryAddressOption = new WorkResultAddress(militaryAddress);
		return miltaryAddressOption;
	}

	/**
	 * Creates a mobile address from the input wizard result address.
	 * 
	 * @author IanBrown
	 * @param wizardAddress
	 *            the wizard results address.
	 * @return the mobile address.
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	private static MobileAddress createMobileAddressFromWizardAddress(final WizardResultAddress wizardAddress) {
		if (wizardAddress == null) {
			return null;
		}

		final MobileAddress mobileAddress = new MobileAddress(wizardAddress.getType());
		mobileAddress.setAddressTo(wizardAddress.getAddressTo());
		mobileAddress.setCity(wizardAddress.getCity());
		mobileAddress.setCountry(wizardAddress.getCountry());
		mobileAddress.setCounty(wizardAddress.getCounty());
		mobileAddress.setDescription(wizardAddress.getDescription());
		mobileAddress.setState(wizardAddress.getState());
		mobileAddress.setStreet1(wizardAddress.getStreet1());
		mobileAddress.setStreet2(wizardAddress.getStreet2());
		mobileAddress.setZip(wizardAddress.getZip());
		mobileAddress.setZip4(wizardAddress.getZip4());
		return mobileAddress;
	}

	/**
	 * Creates a mobile answer from the input wizard answer.
	 * 
	 * @author IanBrown
	 * @param wizardAnswer
	 *            the wizard answer.
	 * @return the mobile answer.
	 * @since May 14, 2012
	 * @version Jul 11, 2012
	 */
	private static MobileAnswer createMobileAnswerFromWizardAnswer(final Answer wizardAnswer) {
		final MobileAnswer mobileAnswer = new MobileAnswer();
		final QuestionField field = wizardAnswer.getField();
		mobileAnswer.setQuestionField(field);
		mobileAnswer.setQuestionFieldId(field.getId());
		if (wizardAnswer instanceof PredefinedAnswer) {
			mobileAnswer.setOption(createMobileOptionFromPredefinedAnswer((PredefinedAnswer) wizardAnswer));
		} else if (wizardAnswer instanceof EnteredDateAnswer) {
			mobileAnswer.setOption(createOptionAnswerFromEnteredDateAnswer((EnteredDateAnswer) wizardAnswer));
		} else {
			mobileAnswer.setOption(createOptionAnswerFromEnteredAnswer((EnteredAnswer) wizardAnswer));
		}
		return mobileAnswer;
	}

	/**
	 * Creates a list of mobile answers from the input wizard answers.
	 * 
	 * @author IanBrown
	 * @param wizardAnswers
	 *            the wizard answers.
	 * @return the mobile answers.
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	private static List<MobileAnswer> createMobileAnswersFromWizardAnswers(final Collection<Answer> wizardAnswers) {
		if (wizardAnswers == null) {
			return null;
		}

		final List<MobileAnswer> mobileAnswers = new LinkedList<MobileAnswer>();
		for (final Answer wizardAnswer : wizardAnswers) {
			mobileAnswers.add(createMobileAnswerFromWizardAnswer(wizardAnswer));
		}
		return mobileAnswers;
	}

	/**
	 * Creates a mobile option from the input predefined answer.
	 * 
	 * @author IanBrown
	 * @param predefinedAnswer
	 *            the predefined answer.
	 * @return the mobile option.
	 * @since May 14, 2012
	 * @version Jul 12, 2012
	 */
	private static MobileOption createMobileOptionFromPredefinedAnswer(final PredefinedAnswer predefinedAnswer) {
		final MobileOption mobileOption = new MobileOption();
		final FieldDictionaryItem selectedValue = predefinedAnswer.getSelectedValue();
		mobileOption.setItem(selectedValue);
		mobileOption.setId(selectedValue.getId());
		//mobileOption.setValue(predefinedAnswer.getValue());
		return mobileOption;
	}

	/**
	 * Creates a mobile person from the input wizard results person.
	 * 
	 * @author IanBrown
	 * @param wizardPerson
	 *            the wizard results person.
	 * @return the mobile person.
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	private static MobilePerson createMobilePersonFromWizardPerson(final WizardResultPerson wizardPerson) {
		if (wizardPerson == null) {
			return null;
		}

		final MobilePerson mobilePerson = new MobilePerson();
		mobilePerson.setTitle(wizardPerson.getTitle());
		mobilePerson.setFirstName(wizardPerson.getFirstName());
		mobilePerson.setMiddleName(wizardPerson.getMiddleName());
		mobilePerson.setLastName(wizardPerson.getLastName());
		mobilePerson.setSuffix(wizardPerson.getSuffix());
		return mobilePerson;
	}

	/**
	 * Creates a mobile option from the input entered answer.
	 * 
	 * @author IanBrown
	 * @param enteredAnswer
	 *            the entered answer.
	 * @return the mobile option.
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	private static MobileOption createOptionAnswerFromEnteredAnswer(final EnteredAnswer enteredAnswer) {
		final MobileOption mobileOption = new MobileOption();
		mobileOption.setValue(enteredAnswer.getEnteredValue());
		return mobileOption;
	}

	/**
	 * Creates a mobile option from the input entered date answer.
	 * 
	 * @author IanBrown
	 * @param enteredDateAnswer
	 *            the entered date answer.
	 * @return the mobile option.
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	private static MobileOption createOptionAnswerFromEnteredDateAnswer(final EnteredDateAnswer enteredDateAnswer) {
		final MobileOption mobileOption = new MobileOption();
		mobileOption.setValue(enteredDateAnswer.getDate(DATE_FORMAT));
		return mobileOption;
	}

	/**
	 * Creates an overseas address option for the specified value.
	 * 
	 * @author IanBrown
	 * @param value
	 *            the value.
	 * @return the overseas address option.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static WorkResultAddress createOverseasAddressOption(final String value) {
		final WizardResultAddress overseasAddress = new WizardResultAddress();
		overseasAddress.setType(AddressType.OVERSEAS);
		overseasAddress.setStreet1("1 " + value + " Street");
		overseasAddress.setStreet2(value + " 2");
		overseasAddress.setCity(value + " City");
		overseasAddress.setCountry(value);
		overseasAddress.setZip("" + value.hashCode());
		final WorkResultAddress overseasAddressOption = new WorkResultAddress(overseasAddress);
		return overseasAddressOption;
	}

	/**
	 * Creates a person option for the specified value.
	 * 
	 * @author IanBrown
	 * @param value
	 *            the value (if <code>null</code>, all fields are left blank).
	 * @param includeTitle
	 *            <code>true</code> to include the title, <code>false</code> to leave it blank.
	 * @param includeMiddleName
	 *            <code>true</code> to include the middle name, <code>false</code> to leave it blank.
	 * @param includeSuffix
	 *            <code>true</code> to include the suffix, <code>false</code> to leave it blank.
	 * @return the person.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static WorkResultPerson createPerson(final String value, final boolean includeTitle, final boolean includeMiddleName,
			final boolean includeSuffix) {
		final WizardResultPerson person = new WizardResultPerson();
		if (value != null) {
			if (includeTitle) {
				person.setTitle(value.charAt(0) + "T");
			}
			person.setFirstName(value + "First");
			if (includeMiddleName) {
				person.setMiddleName(value + "Middle");
			}
			person.setLastName(value + "Last");
			if (includeSuffix) {
				person.setSuffix(value.charAt(0) + "S");
			}
		}
		final WorkResultPerson personOption = new WorkResultPerson(person);
		return personOption;
	}

	/**
	 * Creates a rural address option for the state and value.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state abbreviation.
	 * @param value
	 *            the value.
	 * @return the street address option.
	 * @since May 4, 2012
	 * @version May 11, 2012
	 */
	private static WorkResultAddress createRuralAddressOption(final String state, final String value) {
		final WizardResultAddress ruralAddress = new WizardResultAddress();
		ruralAddress.setType(AddressType.RURAL_ROUTE);
		ruralAddress.setStreet1("1 " + value + " Street");
		ruralAddress.setStreet2(value + " 2");
		ruralAddress.setDescription("Description of " + value);
		ruralAddress.setCity("City of " + value);
		ruralAddress.setCounty(value + " County");
		ruralAddress.setState(state);
		ruralAddress.setZip("12345");
		ruralAddress.setZip4("1234");
		final WorkResultAddress ruralAddressOption = new WorkResultAddress(ruralAddress);
		return ruralAddressOption;
	}

	/**
	 * Creates a street address option for the state and value.
	 * 
	 * @author IanBrown
	 * @param state
	 *            the state abbreviation.
	 * @param value
	 *            the value.
	 * @return the street address option.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static WorkResultAddress createStreetAddressOption(final String state, final String value) {
		final WizardResultAddress streetAddress = new WizardResultAddress();
		streetAddress.setType(AddressType.STREET);
		streetAddress.setStreet1("1 " + value + " Street");
		streetAddress.setStreet2(value + " 2");
		streetAddress.setCity("City of " + value);
		streetAddress.setCounty(value + " County");
		streetAddress.setState(state);
		streetAddress.setZip("12345");
		streetAddress.setZip4("1234");
		final WorkResultAddress streetAddressOption = new WorkResultAddress(streetAddress);
		return streetAddressOption;
	}

	/**
	 * Creates a string option with the specified value.
	 * 
	 * @author IanBrown
	 * @param value
	 *            the value.
	 * @return the string option.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static WorkResultString createStringOption(final String value) {
		return new WorkResultString(value);
	}

	/**
	 * Finds the current variant corresponding to the input mobile variant.
	 * 
	 * @author IanBrown
	 * @param mobileVariant
	 *            the mobile variant.
	 * @param currentVariants
	 *            the current variants.
	 * @return the current variant for the mobile variant.
	 * @since May 3, 2012
	 * @version May 11, 2012
	 */
	private static CurrentVariant findCurrentVariant(final MobileVariant mobileVariant, final Stack<CurrentVariant> currentVariants) {
		for (final CurrentVariant currentVariant : currentVariants) {
			if (currentVariant.getVariant() == mobileVariant) {
				return currentVariant;
			}
		}

		return null;
	}

	/**
	 * Populates the specified field in the results with the value.
	 * 
	 * @author IanBrown
	 * @param fieldName
	 *            the name of the field.
	 * @param fieldValue
	 *            the value for the field.
	 * @param wizardResults
	 *            the wizard results.
	 * @param blockLogger
	 *            the optional block logger.
	 * @throws InvocationTargetException
	 *             if there is a problem invoking the set method.
	 * @throws IllegalAccessException
	 *             if there is a problem accessing the results.
	 * @throws IllegalArgumentException
	 *             if there is a problem with the value.
	 * @since Apr 17, 2012
	 * @version May 11, 2012
	 */
	private static void populateField(final String fieldName, final WorkResultOption<?> fieldValue,
			final WizardResults wizardResults, final BlockLogger blockLogger) throws IllegalArgumentException,
			IllegalAccessException, InvocationTargetException {
		if (fieldValue != null) {
			final Class<?> wizardResultsClass = wizardResults.getClass();
			final String methodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
			final Method[] methods = wizardResultsClass.getMethods();
			for (final Method method : methods) {
				if (methodName.equals(method.getName())) {
					method.invoke(wizardResults, fieldValue.getValue());
					if (blockLogger != null) {
						blockLogger.indentMessage("Field %s = %s", fieldName, fieldValue.getValue().toString());
					}
					break;
				}
			}
		}
	}

	/**
	 * Populates the mobile results object from the input wizard results.
	 * 
	 * @author IanBrown
	 * @param wizardResults
	 *            the wizard results.
	 * @param mobileResults
	 *            the mobile results.
	 * @since May 14, 2012
	 * @version May 14, 2012
	 */
	private static void populateMobileResultsFromWizardResults(final WizardResults wizardResults, final MobileResults mobileResults) {
		mobileResults.setEmailAddress(wizardResults.getUsername());
		mobileResults.setName(createMobilePersonFromWizardPerson(wizardResults.getName()));
		mobileResults.setPreviousName(createMobilePersonFromWizardPerson(wizardResults.getPreviousName()));
		mobileResults.setBirthMonth(wizardResults.getBirthMonth());
		mobileResults.setBirthDay(wizardResults.getBirthDate());
		mobileResults.setBirthYear(wizardResults.getBirthYear());
		mobileResults.setAlternateEmail(wizardResults.getAlternateEmail());
		mobileResults.setPhone(wizardResults.getPhone());
		mobileResults.setAlternatePhone(wizardResults.getAlternatePhone());
		mobileResults.setCurrentAddress(createMobileAddressFromWizardAddress(wizardResults.getCurrentAddress()));
		mobileResults.setVotingAddress(createMobileAddressFromWizardAddress(wizardResults.getVotingAddress()));
		mobileResults.setForwardingAddress(createMobileAddressFromWizardAddress(wizardResults.getForwardingAddress()));
		mobileResults.setPreviousAddress(createMobileAddressFromWizardAddress(wizardResults.getPreviousAddress()));
		mobileResults.setVotingRegionName(wizardResults.getVotingRegionName());
		mobileResults.setVotingRegionState(wizardResults.getVotingRegionState());
		mobileResults.setVoterType(wizardResults.getVoterType());
		mobileResults.setVoterHistory(wizardResults.getVoterHistory());
		mobileResults.setBallotPreference(wizardResults.getBallotPref());
		mobileResults.setRace(wizardResults.getRace());
		mobileResults.setEthnicity(wizardResults.getEthnicity());
		mobileResults.setGender(wizardResults.getGender());
		mobileResults.setParty(wizardResults.getParty());
		mobileResults.setMobile(wizardResults.isMobile());
		mobileResults.setMobileDeviceType(wizardResults.getMobileDeviceType());
		mobileResults.setDownloaded(wizardResults.isDownloaded());
		mobileResults.setAnswers(createMobileAnswersFromWizardAnswers(wizardResults.getAnswers()));
	}

	/**
	 * Retrieves the question options for the question from the current variant. There must be a current variant and the question
	 * must be the active one.
	 * 
	 * @author IanBrown
	 * @param mobileQuestion
	 *            the question.
	 * @param currentVariant
	 *            the current variant.
	 * @return the stack of question options or <code>null</code> if there are none.
	 * @since May 4, 2012
	 * @version May 30, 2012
	 */
	private static Stack<?> retrieveQuestionOptionsFromCurrentVariant(final MobileQuestion mobileQuestion,
			final CurrentVariant currentVariant) {
		if (currentVariant != null && !currentVariant.isCompleted() && currentVariant.getQuestion() == mobileQuestion) {
			final Stack<?> questionOptions = currentVariant.getQuestionOptions();
			return questionOptions;
		}

		return null;
	}

	/**
	 * Updates the current variant for the question and options.
	 * 
	 * @author IanBrown
	 * @param mobileQuestion
	 *            the question.
	 * @param currentVariant
	 *            the current variant (may be <code>null</code>).
	 * @param needNewQuestion
	 *            <code>true</code> if a new question is needed, <code>false</code> otherwise.
	 * @param questionOptions
	 *            the options for the current variant.
	 * @return <code>true</code> if a new question is needed, <code>false</code> otherwise.
	 * @since May 4, 2012
	 * @version May 30, 2012
	 */
	private static boolean updateCurrentVariant(final MobileQuestion mobileQuestion, final CurrentVariant currentVariant,
			final boolean needNewQuestion, final Stack<?> questionOptions) {
		if (currentVariant != null) {
			if (currentVariant.getQuestion() == mobileQuestion) {
				if (questionOptions == null || questionOptions.isEmpty()) {
					currentVariant.setQuestionOptions(null);
					currentVariant.setCompleted(true);
					return true;
				}

				currentVariant.setQuestionOptions(questionOptions);

			} else if (needNewQuestion && questionOptions != null && questionOptions.size() > 1) {
				currentVariant.setQuestion(mobileQuestion);
				currentVariant.setQuestionOptions(questionOptions);
				final MobileVariant mobileVariant = currentVariant.getVariant();
				final List<MobileQuestion> mobileQuestions = mobileVariant.getChildren();
				currentVariant.setAdditionalQuestions(mobileQuestions.get(mobileQuestions.size() - 1) != mobileQuestion);
				return false;
			}
		}

		return needNewQuestion;
	}

	/**
	 * Private constructor - static class.
	 * 
	 * @author IanBrown
	 * @since May 11, 2012
	 * @version May 11, 2012
	 */
	private MobileAnswerUtility() {
		throw new UnsupportedOperationException("This class should not be instantiated");
	}
}
