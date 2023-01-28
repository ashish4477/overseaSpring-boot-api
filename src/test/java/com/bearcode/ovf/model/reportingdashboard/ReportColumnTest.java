/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;

/**
 * Test for {@link ReportColumn}.
 * 
 * @author IanBrown
 * 
 * @since Jan 5, 2012
 * @version Jan 31, 2012
 */
public final class ReportColumnTest extends EasyMockSupport {

	/**
	 * the report column to test.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportColumn reportColumn;

	/**
	 * Sets up the report column to test.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Before
	public final void setUpReportColumn() {
		setReportColumn(createReportColumn());
	}

	/**
	 * Tears down the report column after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@After
	public final void tearDownReportColumn() {
		setReportColumn(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#addField(ReportField)}.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 20, 2012
	 */
	@Test
	public final void testAddField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		replayAll();

		getReportColumn().addField(field);

		assertTrue("The field is in the column", getReportColumn().getFields().contains(field));
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#addField(ReportField)} for the case where the
	 * field is already in the column.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 20, 2012
	 */
	@Test(expected = IllegalStateException.class)
	public final void testAddField_belongsToColumn() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		EasyMock.expect(field.getColumn()).andReturn(getReportColumn());
		replayAll();
		getReportColumn().addField(field);

		getReportColumn().addField(field);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#addField(ReportField)} for the case where the
	 * field already belongs to a different column.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testAddField_fieldInOtherColumn() {
		final ReportField field = createMock("Field", ReportField.class);
		final ReportColumn fieldColumn = createMock("FieldColumn", ReportColumn.class);
		EasyMock.expect(field.getColumn()).andReturn(fieldColumn);
		replayAll();

		getReportColumn().addField(field);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#deepCopy()} for the case where there the column
	 * number is set.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testDeepCopy_columnNumber() {
		getReportColumn().setColumnNumber(62);

		final ReportColumn actualDeepCopy = getReportColumn().deepCopy();

		assertReportColumn(getReportColumn(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#deepCopy()} for the case where there are
	 * fields.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_fields() {
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		getReportColumn().setFields(fields);
		final ReportField copiedField = createMock("CopiedField", ReportField.class);
		EasyMock.expect(field.deepCopy()).andReturn(copiedField);
		copiedField.setColumn((ReportColumn) EasyMock.anyObject());
		replayAll();

		final ReportColumn actualDeepCopy = getReportColumn().deepCopy();

		assertReportColumn(getReportColumn(), actualDeepCopy);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#deepCopy()} for the case where there the name
	 * is set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_name() {
		getReportColumn().setName("Name");

		final ReportColumn actualDeepCopy = getReportColumn().deepCopy();

		assertReportColumn(getReportColumn(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#deepCopy()} for the case where there are no
	 * fields set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_noFields() {
		final ReportColumn actualDeepCopy = getReportColumn().deepCopy();

		assertReportColumn(getReportColumn(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getColumnNumber()}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testGetColumnNumber() {
		final int actualColumnNumber = getReportColumn().getColumnNumber();

		assertEquals("The column number is the default", -1, actualColumnNumber);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getFields()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetFields() {
		final Collection<ReportField> actualFields = getReportColumn().getFields();

		assertNull("No fields are set", actualFields);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetId() {
		final Long actualId = getReportColumn().getId();

		assertNull("There is no ID", actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getName()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetName() {
		final String actualName = getReportColumn().getName();

		assertNull("There is no name", actualName);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getQuestionGroup()} for the case where there
	 * are no fields.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testGetQuestionGroup_noFields() {
		final Question actualQuestionGroup = getReportColumn().getQuestionGroup();

		assertNull("There is no question group", actualQuestionGroup);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getQuestionGroup()} for the case where there is
	 * a question field.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testGetQuestionGroup_questionField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		final QuestionField question = createMock("Question", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(question);
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(question.getQuestion()).andReturn(variant);
		final Question questionGroup = createMock("QuestionGroup", Question.class);
		EasyMock.expect(variant.getQuestion()).andReturn(questionGroup);
		replayAll();
		getReportColumn().addField(field);

		final Question actualQuestionGroup = getReportColumn().getQuestionGroup();

		assertSame("The question group is returned", questionGroup, actualQuestionGroup);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#getReport()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetReport() {
		final Report actualReport = getReportColumn().getReport();

		assertNull("There is no report", actualReport);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#isQuestionColumn()}.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testIsQuestionColumn_noFields() {
		final boolean actualQuestionColumn = getReportColumn().isQuestionColumn();

		assertFalse("The column is not a question column", actualQuestionColumn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#isUserFieldColumn()} for the case where there
	 * is a field without a question.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testIsQuestionColumn_nonQuestionField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		EasyMock.expect(field.getQuestion()).andReturn(null);
		replayAll();
		getReportColumn().addField(field);

		final boolean actualQuestionColumn = getReportColumn().isQuestionColumn();

		assertFalse("The column is not a question column", actualQuestionColumn);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#isQuestionColumn()} for the case where there is
	 * a field with a question.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testIsQuestionColumn_questionField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		final QuestionField question = createMock("Question", QuestionField.class);
		EasyMock.expect(field.getQuestion()).andReturn(question);
		replayAll();
		getReportColumn().addField(field);

		final boolean actualQuestionColumn = getReportColumn().isQuestionColumn();

		assertTrue("The column is a question column", actualQuestionColumn);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#isUserFieldColumn()}.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testIsUserFieldColumn_noFields() {
		final boolean actualUserFieldColumn = getReportColumn().isUserFieldColumn();

		assertFalse("The column is not a user field column", actualUserFieldColumn);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#isUserFieldColumn()} for the case where there
	 * is a field without a user field name.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 24, 2012
	 */
	@Test
	public final void testIsUserFieldColumn_nonUserField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		EasyMock.expect(field.getUserFieldName()).andReturn(null);
		replayAll();
		getReportColumn().addField(field);

		final boolean actualUserFieldColumn = getReportColumn().isUserFieldColumn();

		assertFalse("The column is not a user field column", actualUserFieldColumn);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#isUserFieldColumn()} for the case where there
	 * is a field with a user field name.
	 * 
	 * @author IanBrown
	 * @since Jan 24, 2012
	 * @version Jan 31, 2012
	 */
	@Test
	public final void testIsUserFieldColumn_userField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		final String userFieldName = "User Field Name";
		EasyMock.expect(field.getUserFieldName()).andReturn(userFieldName).atLeastOnce();
		replayAll();
		getReportColumn().addField(field);

		final boolean actualUserFieldColumn = getReportColumn().isUserFieldColumn();

		assertTrue("The column is a user field column", actualUserFieldColumn);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#removeField(ReportField)}.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testRemoveField() {
		final ReportField field = createMock("Field", ReportField.class);
		EasyMock.expect(field.getColumn()).andReturn(null);
		field.setColumn(getReportColumn());
		field.setColumn(null);
		replayAll();
		getReportColumn().addField(field);

		getReportColumn().removeField(field);

		final Collection<ReportField> actualFields = getReportColumn().getFields();
		assertTrue("There are no fields in the column", actualFields.isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#removeField(ReportField)} for the case where
	 * there are no fields.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test
	public final void testRemoveField_noFields() {
		final ReportField field = createMock("Field", ReportField.class);
		replayAll();

		getReportColumn().removeField(field);

		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#removeField(ReportField)} for the case where
	 * the field is not in the column.
	 * 
	 * @author IanBrown
	 * @since Jan 19, 2012
	 * @version Jan 19, 2012
	 */
	@Test(expected = IllegalArgumentException.class)
	public final void testRemoveField_notInColumn() {
		final ReportField field = createMock("Field", ReportField.class);
		final ReportField fieldInColumn = createMock("FieldInColumn", ReportField.class);
		EasyMock.expect(fieldInColumn.getColumn()).andReturn(null);
		fieldInColumn.setColumn(getReportColumn());
		EasyMock.expect(field.getColumn()).andReturn(null);
		replayAll();
		getReportColumn().addField(fieldInColumn);

		getReportColumn().removeField(field);

		final Collection<ReportField> actualFields = getReportColumn().getFields();
		final Collection<ReportField> expectedFields = Arrays.asList(fieldInColumn);
		assertEquals("The field that was in the column remains in the column", expectedFields, actualFields);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#setColumnNumber(int)}.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testSetColumnNumber() {
		final int columnNumber = 871;

		getReportColumn().setColumnNumber(columnNumber);

		assertEquals("The column number is set", columnNumber, getReportColumn().getColumnNumber());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#setFields(java.util.Collection)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetFields() {
		final ReportField field = createMock("Field", ReportField.class);
		final Collection<ReportField> fields = Arrays.asList(field);
		replayAll();

		getReportColumn().setFields(fields);

		assertSame("The fields are set", fields, getReportColumn().getFields());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#setId(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetId() {
		final Long id = 9886721l;

		getReportColumn().setId(id);

		assertSame("The ID is set", id, getReportColumn().getId());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#setName(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetName() {
		final String name = "Name";

		getReportColumn().setName(name);

		assertEquals("The name is set", name, getReportColumn().getName());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.model.reportingdashboard.ReportColumn#setReport(com.bearcode.ovf.model.reportingdashboard.Report)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetReport() {
		final Report report = createMock("Report", Report.class);
		replayAll();

		getReportColumn().setReport(report);

		assertSame("The report is set", report, getReportColumn().getReport());
		verifyAll();
	}

	/**
	 * Custom assertion to ensure that the fields are copied correctly.
	 * 
	 * @author IanBrown
	 * @param expectedFields
	 *            the expected fields (may be <code>null</code>).
	 * @param actualFields
	 *            the actual fields (may be <code>null</code>).
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private void assertFields(final Collection<ReportField> expectedFields, final Collection<ReportField> actualFields) {
		if (expectedFields == null) {
			assertNull("There are no copied fields", actualFields);
		} else {
			assertNotNull("There are copied fields", actualFields);
			assertEquals("The number of fields is correct", expectedFields.size(), actualFields.size());
		}
	}

	/**
	 * Custom assertion to ensure that the report column is copied correctly.
	 * 
	 * @author IanBrown
	 * @param expectedReportColumn
	 *            the expected (original) report column.
	 * @param actualReport
	 *            the actual (copied) report.
	 * @param actualReportColumn
	 *            the actual (copied) report column.
	 * @since Jan 9, 2012
	 * @version Jan 11, 2012
	 */
	private void assertReportColumn(final ReportColumn expectedReportColumn, final ReportColumn actualReportColumn) {
		assertNotNull("The report column is copied", actualReportColumn);
		assertEquals("The column number is correct", expectedReportColumn.getColumnNumber(), actualReportColumn.getColumnNumber());
		assertFields(expectedReportColumn.getFields(), actualReportColumn.getFields());
		assertEquals("The name is correct", expectedReportColumn.getName(), actualReportColumn.getName());
	}

	/**
	 * Creates a report column.
	 * 
	 * @author IanBrown
	 * @return the report column.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportColumn createReportColumn() {
		return new ReportColumn();
	}

	/**
	 * Gets the report column.
	 * 
	 * @author IanBrown
	 * @return the report column.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private ReportColumn getReportColumn() {
		return reportColumn;
	}

	/**
	 * Sets the report column.
	 * 
	 * @author IanBrown
	 * @param reportColumn
	 *            the report column to set.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private void setReportColumn(final ReportColumn reportColumn) {
		this.reportColumn = reportColumn;
	}

}
