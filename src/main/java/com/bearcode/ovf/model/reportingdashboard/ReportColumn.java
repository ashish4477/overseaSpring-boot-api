/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import java.util.Collection;
import java.util.LinkedList;

import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;

/**
 * The definition of a column of a {@link Report}.
 * 
 * @author IanBrown
 * 
 * @since Jan 4, 2012
 * @version Feb 2, 2012
 */
public class ReportColumn {

	/**
	 * the unique identifier for the report column.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Long id;

	/**
	 * the fields that supply the data for this column.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Collection<ReportField> fields;

	/**
	 * the name of the column.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private String name;

	/**
	 * the report for the column.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Report report;

	/**
	 * the number of column within the report.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	private int columnNumber = -1;

	/**
	 * Adds the specified field to the column.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the field.
	 * @since Jan 12, 2012
	 * @version Feb 2, 2012
	 */
	public void addField(final ReportField field) {
		final ReportColumn fieldColumn = field.getColumn();
		if ((fieldColumn != null) && (fieldColumn != this)) {
			throw new IllegalArgumentException(field + " belongs to a different column " + fieldColumn + " rather than " + this);
		} else if ((fieldColumn == this) && (getFields() != null) && getFields().contains(field)) {
			throw new IllegalStateException(field + " already belongs to the column " + this);
		}

		if (getFields() == null) {
			setFields(new LinkedList<ReportField>());
		}
		field.setColumn(this);
		getFields().add(field);
	}

	/**
	 * Creates a deep copy of the column (copies all of its elements).
	 * 
	 * @author IanBrown
	 * @return the copied column.
	 * @since Jan 9, 2012
	 * @version Jan 11, 2012
	 */
	public ReportColumn deepCopy() {
		final ReportColumn copiedColumn = new ReportColumn();
		copiedColumn.setColumnNumber(getColumnNumber());
		copiedColumn.setFields(deepCopyFields(copiedColumn));
		copiedColumn.setName(getName());
		return copiedColumn;
	}

	/**
	 * Gets the column number.
	 * 
	 * @author IanBrown
	 * @return the column number.
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	public int getColumnNumber() {
		return columnNumber;
	}

	/**
	 * Gets the fields that provide the data for the column.
	 * 
	 * @author IanBrown
	 * @return the fields.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public Collection<ReportField> getFields() {
		return fields;
	}

	/**
	 * Gets the report column identifier.
	 * 
	 * @author IanBrown
	 * @return the identifier.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Gets the name of the column.
	 * 
	 * @author IanBrown
	 * @return the name.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the question group containing all of the question fields used by this column.
	 * 
	 * @author IanBrown
	 * @return the question group or <code>null</code> if no group has been selected.
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	public Question getQuestionGroup() {
		if (getFields() != null) {
			for (final ReportField field : getFields()) {
				final QuestionField question = field.getQuestion();
				if (question != null) {
					return question.getQuestion().getQuestion();
				}
			}
		}

		return null;
	}

	/**
	 * Gets the report for the column.
	 * 
	 * @author IanBrown
	 * @return the report.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public Report getReport() {
		return report;
	}

	/**
	 * Is this column a question column?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if there is a question field, <code>false</code> otherwise.
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	public boolean isQuestionColumn() {
		if (getFields() != null) {
			for (final ReportField field : getFields()) {
				if (field.getQuestion() != null) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Is this column a user field column?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if there is a user name field, <code>false</code> otherwise.
	 * @since Jan 24, 2012
	 * @version Jan 31, 2012
	 */
	public boolean isUserFieldColumn() {
		if (getFields() != null) {
			for (final ReportField field : getFields()) {
				if ((field.getUserFieldName() != null) && !field.getUserFieldName().isEmpty()) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Removes the specified field from the column.
	 * 
	 * @author IanBrown
	 * @param field
	 *            the field.
	 * @since Jan 12, 2012
	 * @version Feb 2, 2012
	 */
	public void removeField(final ReportField field) {
		if (getFields() != null) {
			if (!getFields().contains(field)) {
				throw new IllegalArgumentException(field + " does not belong to the column " + this);
			}

			getFields().remove(field);
			field.setColumn(null);
		}
	}

	/**
	 * Sets the column number.
	 * 
	 * @author IanBrown
	 * @param columnNumber
	 *            the column number.
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	public void setColumnNumber(final int columnNumber) {
		this.columnNumber = columnNumber;
	}

	/**
	 * Sets the fields that provide the data for the column.
	 * 
	 * @author IanBrown
	 * @param fields
	 *            the fields.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	public void setFields(final Collection<ReportField> fields) {
		this.fields = fields;
	}

	/**
	 * Sets the report column identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the name of the column.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Sets the report for the column.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report.
	 * @since Jan 4, 2012
	 * @version Jan 4, 2012
	 */
	public void setReport(final Report report) {
		this.report = report;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" ").append(getName()).append(" Fields: ").append(getFields());

		return sb.toString();
	}

	/**
	 * Performs a deep copy of the fields.
	 * 
	 * @author IanBrown
	 * @param copiedColumn
	 *            the copied column.
	 * @return the copied fields.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private Collection<ReportField> deepCopyFields(final ReportColumn copiedColumn) {
		if (getFields() == null) {
			return null;
		}

		final Collection<ReportField> copiedFields = new LinkedList<ReportField>();
		for (final ReportField field : getFields()) {
			final ReportField copiedField = field.deepCopy();
			copiedField.setColumn(copiedColumn);
			copiedFields.add(copiedField);
		}
		return copiedFields;
	}
}
