/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import java.util.Collection;
import java.util.Map;

import org.easymock.EasyMock;

import com.bearcode.ovf.actions.commons.AbstractComponentCheck;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.SvrPropertiesService;

/**
 * Extended {@link AbstractComponentCheck} for testing implementations of
 * {@link AllowedForAddOn}.
 * 
 * @author Ian
 * @param <A>
 *          the type of allowed for add on to test.
 * 
 */
public abstract class AllowedForAddOnCheck<A extends AllowedForAddOn> extends
    AbstractComponentCheck<A> {

	/**
	 * the SVR properties service.
	 * 
	 * @author IanBrown
	 * @since Oct 22, 2012
	 * @version Oct 22, 2012
	 */
	private SvrPropertiesService svrPropertiesService;

	/**
	 * the valet.
	 * 
	 * @author IanBrown
	 * @since Aug 7, 2012
	 * @version Aug 7, 2012
	 */
	private PageAddOnValet valet;

	/**
	 * Creates an allowed for add on of the type to test.
	 * 
	 * @return
	 */
	protected abstract A createAllowedForAddOn();

	/** {@inheritDoc} */
	@Override
	protected final A createComponent() {
		return createAllowedForAddOn();
	}

	/**
	 * Creates a field for the specified variant.
	 * 
	 * @author IanBrown
	 * @param variant
	 *          the question variant.
	 * @param fieldName
	 *          the name to give the field mock.
	 * @param softId
	 *          the soft identifier - may be changed on output.
	 * @param fieldType
	 *          the type of field.
	 * @param title
	 *          the title.
	 * @param answer
	 *          the answer to the question - may be <code>null</code>.
	 * @param answers
	 *          the answers by field ID.
	 * @param needAnswer
	 *          is an answer needed for this field?
	 * @param wizardResults
	 *          the wizard results.
	 * @return the question field.
	 * @since Aug 7, 2012
	 * @version May 13, 2013
	 */
	@SuppressWarnings("unchecked")
	protected final QuestionField createField(final QuestionVariant variant,
	    final String fieldName, final long[] softId, final FieldType fieldType,
	    final String title, final Answer answer, final Map<Long, Answer> answers,
	    final boolean needAnswer, final WizardResults wizardResults) {
		final QuestionField field = createMock(makeIdentifier(fieldName),
		    QuestionField.class);
		EasyMock.expect(field.getTitle()).andReturn(title).anyTimes();
		if (answer == null) {
			EasyMock.expect(
			    getValet().createField(EasyMock.same(variant),
			        EasyMock.eq(softId[0]), EasyMock.same(fieldType),
			        EasyMock.isA(String.class), EasyMock.isA(String.class)))
			    .andReturn(field);
			if (needAnswer) {
				final Answer fieldAnswer = createMock(makeIdentifier(fieldName)
				    + "Answer", Answer.class);
				EasyMock.expect(field.createAnswer()).andReturn(fieldAnswer);
				EasyMock.expect(fieldAnswer.getField()).andReturn(field).anyTimes();
				wizardResults.putAnswer(fieldAnswer);
			}
		} else {
			answers.put(softId[0], answer);
			EasyMock.expect(answer.getField()).andReturn(field).anyTimes();
			field.setQuestion(variant);
			final Collection<QuestionField> fields = createMock("Fields",
			    Collection.class);
			EasyMock.expect(variant.getFields()).andReturn(fields);
			EasyMock.expect(fields.add(field)).andReturn(true);
		}
		++softId[0];
		return field;
	}

	/**
	 * Creates a group on the current page.
	 * 
	 * @author IanBrown
	 * @param currentPage
	 *          the current page.
	 * @param groupName
	 *          the name for the group.
	 * @param softId
	 *          the soft identifier - may be updated.
	 * @param name
	 *          the name of the group.
	 * @param title
	 *          the title of the group.
	 * @return the group.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	protected final Question createGroup(final QuestionnairePage currentPage,
	    final String groupName, final long[] softId, final String name,
	    final String title) {
		final Question group = createMock(makeIdentifier(groupName), Question.class);
		EasyMock.expect(
		    getValet()
		        .createGroup(
		            EasyMock.same(currentPage),
		            EasyMock.eq(softId[0]),
		            EasyMock.or(EasyMock.eq(name), EasyMock
		                .eq(StateContestsPageAddOn.STANDARD_OFFICE_GROUP_NAME)),
		            EasyMock.eq(title))).andReturn(group);
		++softId[0];
		return group;
	}

	/**
	 * Creates a question variant.
	 * 
	 * @author IanBrown
	 * @param group
	 *          the group.
	 * @param variantName
	 *          the name for the variant.
	 * @param softId
	 *          the soft identifier - may be changed on output.
	 * @return the variant.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	protected final QuestionVariant createVariant(final Question group,
	    final String variantName, final long[] softId) {
		final QuestionVariant variant = createMock(makeIdentifier(variantName),
		    QuestionVariant.class);
		EasyMock.expect(
		    getValet().createVariant(EasyMock.same(group), EasyMock.eq(softId[0]),
		        (String) EasyMock.anyObject())).andReturn(variant);
		++softId[0];
		return variant;
	}

	/**
	 * Gets the SVR properties service.
	 * 
	 * @author IanBrown
	 * @return the SVR properties service.
	 * @since Oct 22, 2012
	 * @version May 7, 2013
	 */
	protected final SvrPropertiesService getSvrPropertiesService() {
		return svrPropertiesService;
	}

	/**
	 * Gets the valet.
	 * 
	 * @author IanBrown
	 * @return the valet.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	protected final PageAddOnValet getValet() {
		return valet;
	}

	/**
	 * Makes a valid Java identifier for the specified value.
	 * 
	 * @author IanBrown
	 * @param value
	 *          the value.
	 * @return the Java identifier.
	 * @since Nov 1, 2012
	 * @version May 13, 2013
	 */
	protected final String makeIdentifier(final String value) {
		return value.replace(' ', '_').replace('&', '_').replace('.', '_').replace('-', '_');
	}

	/**
	 * Sets up to return a multi-line property.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param propertyPrefix
	 *          the prefix for the property.
	 * @param lines
	 *          the values for the lines (may be <code>null</code>).
	 * @param defaultLines
	 *          the default values for the lines.
	 * @return the resulting line.
	 * @since Oct 23, 2012
	 * @version May 7, 2013
	 */
	protected final String multilineProperty(final String stateAbbreviation,
	    final String votingRegionName, final String propertyPrefix,
	    final String[] lines, final String[] defaultLines) {
		final StringBuilder sb = new StringBuilder();
		int idx = 0;
		if (lines == null) {
			for (final String line : defaultLines) {
				sb.append(line);
			}
		} else {
			for (final String line : lines) {
				EasyMock.expect(
				    getSvrPropertiesService().findProperty(stateAbbreviation,
				        votingRegionName, propertyPrefix + "[" + idx + "]")).andReturn(
				    line);
				sb.append(line);
				++idx;
			}
		}
		EasyMock.expect(
		    getSvrPropertiesService().findProperty(stateAbbreviation,
		        votingRegionName, propertyPrefix + "[" + idx + "]"))
		    .andReturn(null);
		return sb.toString();
	}

	/**
	 * Sets the SVR properties service.
	 * 
	 * @author IanBrown
	 * @param svrPropertiesService
	 *          the SVR properties service to set.
	 * @since Oct 22, 2012
	 * @version May 7, 2013
	 */
	protected final void setSvrPropertiesService(
	    final SvrPropertiesService svrPropertiesService) {
		this.svrPropertiesService = svrPropertiesService;
	}

	/**
	 * Sets up to test the specific type of allowed for add on.
	 */
	protected abstract void setUpForAllowedForAddOn();

	/** {@inheritDoc} */
	@Override
	protected final void setUpForComponent() {
		setUpForAllowedForAddOn();
	}

	/**
	 * Sets the valet.
	 * 
	 * @author IanBrown
	 * @param valet
	 *          the valet to set.
	 * @since Aug 7, 2012
	 * @version May 7, 2013
	 */
	protected final void setValet(final PageAddOnValet valet) {
		this.valet = valet;
	}

	/**
	 * Sets up to return a single line property.
	 * 
	 * @author IanBrown
	 * @param stateAbbreviation
	 *          the abbreviation for the state.
	 * @param votingRegionName
	 *          the name of the voting region.
	 * @param propertyName
	 *          the name of the property.
	 * @param line
	 *          the value for the line (may be <code>null</code>).
	 * @param defaultLine
	 *          the default value for the line.
	 * @return the resulting line.
	 * @since Oct 23, 2012
	 * @version May 7, 2013
	 */
	protected final String singlelineProperty(final String stateAbbreviation,
	    final String votingRegionName, final String propertyName,
	    final String line, final String defaultLine) {
		EasyMock.expect(
		    getSvrPropertiesService().findProperty(stateAbbreviation,
		        votingRegionName, propertyName)).andReturn(line);
		return line == null ? defaultLine : line;
	}

	/**
	 * Tears down the set up for testing the specific type of allowed for add on.
	 */
	protected abstract void tearDownForAllowedForAddOn();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForComponent() {
		tearDownForAllowedForAddOn();
	}
}
