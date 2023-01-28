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

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * Test for {@link Report}.
 * 
 * @author IanBrown
 * 
 * @since Jan 5, 2012
 * @version Feb 3, 2012
 */
public final class ReportTest extends EasyMockSupport {

	/**
	 * the report to test.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Report report;

	/**
	 * Sets up the report before testing.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Before
	public final void setUpReport() {
		setReport(createReport());
	}

	/**
	 * Tears down the report before testing.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@After
	public final void tearDownReport() {
		setReport(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#addColumn(ReportColumn)} for the case where there was
	 * a column.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testAddColumn_additionalColumn() {
		final ReportColumn column1 = createMock("Column1", ReportColumn.class);
		column1.setColumnNumber(0);
		column1.setReport(getReport());
		final ReportColumn column2 = createMock("Column2", ReportColumn.class);
		column2.setColumnNumber(1);
		column2.setReport(getReport());
		replayAll();
		getReport().addColumn(column1);

		final boolean actualAddColumn = getReport().addColumn(column2);

		assertTrue("The column is added", actualAddColumn);
		final List<ReportColumn> expectedColumns = Arrays.asList(column1, column2);
		assertEquals("The column is in the report", expectedColumns, report.getColumns());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#addColumn(ReportColumn)} for the case where there
	 * were no columns.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testAddColumn_firstColumn() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		column.setColumnNumber(0);
		column.setReport(getReport());
		replayAll();

		final boolean actualAddColumn = getReport().addColumn(column);

		assertTrue("The column is added", actualAddColumn);
		final List<ReportColumn> expectedColumns = Arrays.asList(column);
		assertEquals("The column is in the report", expectedColumns, report.getColumns());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#addColumn(ReportColumn)} for the case where the
	 * column is added for a second time.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testAddColumn_secondTime() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		column.setColumnNumber(0);
		column.setReport(getReport());
		replayAll();
		getReport().addColumn(column);

		final boolean actualAddColumn = getReport().addColumn(column);

		assertFalse("The column is not added", actualAddColumn);
		final List<ReportColumn> expectedColumns = Arrays.asList(column);
		assertEquals("The column is in the report once", expectedColumns, report.getColumns());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where there are columns.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_columns() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		final ReportColumn copiedColumn = createMock("CopiedColumn", ReportColumn.class);
		EasyMock.expect(column.deepCopy()).andReturn(copiedColumn);
		copiedColumn.setReport((Report) EasyMock.anyObject());
		getReport().setColumns(columns);
		replayAll();

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the from date is set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_dateFrom() {
		getReport().setDateFrom(new Date());

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the to date is set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_dateTo() {
		getReport().setDateTo(new Date());

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where there is a
	 * description.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_description() {
		getReport().setDescription("Description");

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the faces are set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_faces() {
		final Set<String> faces = new HashSet<String>(Arrays.asList("face1", "face2"));
		getReport().setFaces(faces);

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the flow type is set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_flowType() {
		getReport().setFlowType(FlowType.RAVA);

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where no fields are set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_noFields() {
		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the owner is set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_owner() {
		final OverseasUser owner = createMock("Owner", OverseasUser.class);
		getReport().setOwner(owner);

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the standard flag is
	 * set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_standard() {
		getReport().setStandard(true);

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#deepCopy()} for the case where the title is set.
	 * 
	 * @author IanBrown
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	@Test
	public final void testDeepCopy_title() {
		getReport().setTitle("Title");

		final Report actualDeepCopy = getReport().deepCopy();

		assertCopiedReport(getReport(), actualDeepCopy);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getColumns()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetColumns() {
		final List<ReportColumn> actualColumns = getReport().getColumns();

		assertNull("There are no columns", actualColumns);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getDateFrom()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetDateFrom() {
		final Date actualDateFrom = getReport().getDateFrom();

		assertNull("There is no from date", actualDateFrom);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getDateTo()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetDateTo() {
		final Date actualDateTo = getReport().getDateTo();

		assertNull("There is no to date", actualDateTo);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getDescription()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetDescription() {
		final String actualDescription = getReport().getDescription();

		assertNull("There is no description", actualDescription);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getFaces()}.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testGetFaces() {
		final Set<String> actualFaces = getReport().getFaces();

		assertNull("There are no faces", actualFaces);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getFacesString()} for the case where there are faces.
	 * 
	 * @author IanBrown
	 * @since Jan 7, 2012
	 * @version Jan 7, 2012
	 */
	@Test
	public final void testGetFacesString_faces() {
		final Set<String> faces = new HashSet<String>(Arrays.asList("face1", "face2"));
		getReport().setFaces(faces);

		final String actualFacesString = getReport().getFacesString();

		final String expectedFacesString = "face1,face2";
		assertEquals("There are faces in the string", expectedFacesString, actualFacesString);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getFacesString()} for the case where there are no
	 * faces.
	 * 
	 * @author IanBrown
	 * @since Jan 7, 2012
	 * @version Jan 7, 2012
	 */
	@Test
	public final void testGetFacesString_noFaces() {
		final String actualFacesString = getReport().getFacesString();

		assertEquals("There are no faces in the string", "", actualFacesString);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getFlowType()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testGetFlowType() {
		final FlowType actualFlowType = getReport().getFlowType();

		assertNull("There is no flow type", actualFlowType);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getId()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetId() {
		final Long actualId = getReport().getId();

		assertNull("There is no ID", actualId);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getOwner()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetOwner() {
		final OverseasUser actualOwner = getReport().getOwner();

		assertNull("There is no owner", actualOwner);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getRangeEnd()} for the case where there is a to date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testGetRangeEnd_dateTo() {
		final Date dateTo = new Date();
		getReport().setDateTo(dateTo);

		final String actualRangeEnd = getReport().getRangeEnd();

		final String expectedRangeEnd = formatDate(dateTo);
		assertEquals("The to date is formatted", expectedRangeEnd, actualRangeEnd);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getRangeEnd()} for the case where there is no to
	 * date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testGetRangeEnd_noDateTo() {
		final String actualRangeEnd = getReport().getRangeEnd();

		assertTrue("An empty string is returned", actualRangeEnd.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getRangeStart()} for the case where there is a from
	 * date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testGetRangeStart_dateFrom() {
		final Date dateFrom = new Date();
		getReport().setDateFrom(dateFrom);

		final String actualRangeStart = getReport().getRangeStart();

		final String expectedRangeStart = formatDate(dateFrom);
		assertEquals("The from date is formatted", expectedRangeStart, actualRangeStart);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getRangeStart()} for the case where there is no from
	 * date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testGetRangeStart_noDateFrom() {
		final String actualRangeStart = getReport().getRangeStart();

		assertTrue("An empty string is returned", actualRangeStart.isEmpty());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#getTitle()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testGetTitle() {
		final String actualTitle = getReport().getTitle();

		assertNull("There is no title", actualTitle);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyDateRange()} for the case where there is a
	 * from and a to date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testIsApplyDateRange_fromAndToDate() {
		final Date dateFrom = new Date();
		getReport().setDateFrom(dateFrom);
		final Date dateTo = new Date();
		getReport().setDateTo(dateTo);

		final boolean actualApplyDateRange = getReport().isApplyDateRange();

		assertTrue("There is a date range to apply", actualApplyDateRange);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyDateRange()} for the case where there is a
	 * from date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testIsApplyDateRange_fromDate() {
		final Date dateFrom = new Date();
		getReport().setDateFrom(dateFrom);

		final boolean actualApplyDateRange = getReport().isApplyDateRange();

		assertTrue("There is a date range to apply", actualApplyDateRange);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyDateRange()} for the case where there is no
	 * date range.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testIsApplyDateRange_noDateRange() {
		final boolean actualApplyDateRange = getReport().isApplyDateRange();

		assertFalse("There is no date range to apply", actualApplyDateRange);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyDateRange()} for the case where there is a to
	 * date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testIsApplyDateRange_toDate() {
		final Date dateTo = new Date();
		getReport().setDateTo(dateTo);

		final boolean actualApplyDateRange = getReport().isApplyDateRange();

		assertTrue("There is a date range to apply", actualApplyDateRange);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyFaces()}.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Feb 3, 2012
	 */
	@Test
	public final void testIsApplyFaces() {
		final boolean actualApplyFaces = getReport().isApplyFaces();

		assertFalse("Faces are not applied", actualApplyFaces);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyFlow()} for the case where there is a flow
	 * type.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testIsApplyFlow_flowType() {
		final FlowType flowType = FlowType.FWAB;
		getReport().setFlowType(flowType);

		final boolean actualApplyFlow = getReport().isApplyFlow();

		assertTrue("There is a flow to apply", actualApplyFlow);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isApplyFlow()} for the case where there is no flow
	 * type.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testIsApplyFlow_noFlowType() {
		final boolean actualApplyFlow = getReport().isApplyFlow();

		assertFalse("There is no flow to apply", actualApplyFlow);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#isStandard()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testIsStandard() {
		final boolean actualStandard = getReport().isStandard();

		assertFalse("The report is not a standard report", actualStandard);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#removeColumn(ReportColumn)} for the case where it is
	 * not the only one in the report.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testRemoveColumn_anotherColumn() {
		final ReportColumn column1 = createMock("Column1", ReportColumn.class);
		column1.setColumnNumber(0);
		column1.setReport(getReport());
		final ReportColumn column2 = createMock("Column2", ReportColumn.class);
		column2.setColumnNumber(1);
		column2.setReport(getReport());
		column2.setReport(null);
		replayAll();
		getReport().addColumn(column1);
		getReport().addColumn(column2);

		final boolean actualRemoveColumn = getReport().removeColumn(column2);

		assertTrue("The column is removed", actualRemoveColumn);
		final List<ReportColumn> expectedColumns = Arrays.asList(column1);
		assertEquals("The first column is still in the report", expectedColumns, report.getColumns());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#removeColumn(ReportColumn)} for the case where there
	 * are no columns in the report.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testRemoveColumn_noColumns() {
		final ReportColumn column = createMock("Column", ReportColumn.class);

		final boolean actualRemoveColumn = getReport().removeColumn(column);

		assertFalse("The column is not removed", actualRemoveColumn);
		assertNull("There are no columns in the report", report.getColumns());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#removeColumn(ReportColumn)} for the case where it
	 * isn't in the report.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 11, 2012
	 */
	@Test
	public final void testRemoveColumn_notInReport() {
		final ReportColumn column1 = createMock("Column1", ReportColumn.class);
		column1.setColumnNumber(0);
		column1.setReport(getReport());
		final ReportColumn column2 = createMock("Column2", ReportColumn.class);
		replayAll();
		getReport().addColumn(column1);

		final boolean actualRemoveColumn = getReport().removeColumn(column2);

		assertFalse("The column is not removed", actualRemoveColumn);
		final List<ReportColumn> expectedColumns = Arrays.asList(column1);
		assertEquals("The first column is still in the report", expectedColumns, report.getColumns());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#removeColumn(ReportColumn)} for the case where it is
	 * the only column in the report.
	 * 
	 * @author IanBrown
	 * @since Jan 11, 2012
	 * @version Jan 27, 2012
	 */
	@Test
	public final void testRemoveColumn_onlyColumn() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		column.setColumnNumber(0);
		column.setReport(getReport());
		column.setReport(null);
		replayAll();
		getReport().addColumn(column);

		final boolean actualRemoveColumn = getReport().removeColumn(column);

		assertTrue("The column is removed", actualRemoveColumn);
		assertTrue("There are no columns in the report", getReport().getColumns().isEmpty());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setApplyFaces(boolean)}.
	 * 
	 * @author IanBrown
	 * @since Feb 3, 2012
	 * @version Feb 3, 2012
	 */
	@Test
	public final void testSetApplyFaces() {
		getReport().setApplyFaces(true);

		assertTrue("Faces are applied", getReport().isApplyFaces());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setColumns(java.util.List)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetColumns() {
		final ReportColumn column = createMock("Column", ReportColumn.class);
		final List<ReportColumn> columns = Arrays.asList(column);
		replayAll();

		getReport().setColumns(columns);

		assertSame("The columns are set", columns, getReport().getColumns());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setDateFrom(java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetDateFrom() {
		final Date dateFrom = new Date();

		getReport().setDateFrom(dateFrom);

		assertSame("The from date is set", dateFrom, getReport().getDateFrom());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setDateTo(java.util.Date)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetDateTo() {
		final Date dateTo = new Date();

		getReport().setDateTo(dateTo);

		assertSame("The to date is set", dateTo, getReport().getDateTo());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setDescription(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetDescription() {
		final String description = "Description";

		getReport().setDescription(description);

		assertEquals("The description is set", description, getReport().getDescription());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setFaces(Set)}.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Feb 3, 2012
	 */
	@Test
	public final void testSetFaces() {
		final Set<String> faces = new HashSet<String>(Arrays.asList("face"));

		getReport().setFaces(faces);

		assertSame("The faces are set", faces, getReport().getFaces());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setFlowType(FlowType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetFlowType() {
		final FlowType flowType = FlowType.DOMESTIC_REGISTRATION;

		getReport().setFlowType(flowType);

		assertEquals("The flow type is set", flowType, getReport().getFlowType());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setId(java.lang.Long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetId() {
		final Long id = 781266l;

		getReport().setId(id);

		assertSame("The ID is set", id, getReport().getId());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setOwner(com.bearcode.ovf.model.common.OverseasUser)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetOwner() {
		final OverseasUser owner = createMock("Owner", OverseasUser.class);
		replayAll();

		getReport().setOwner(owner);

		assertSame("The owner is set", owner, getReport().getOwner());
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setRangeEnd(String)} for bad string.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetRangeEnd_badString() {
		getReport().setRangeEnd("Bad String");

		assertEquals("The range end is empty", "", getReport().getRangeEnd());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setRangeEnd(String)} for an empty string.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetRangeEnd_emptyString() {
		getReport().setRangeEnd("");

		assertEquals("The range end is empty", "", getReport().getRangeEnd());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setRangeEnd(String)} for formatted date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetRangeEnd_formattedDate() {
		final String rangeEnd = "01/07/2011";

		getReport().setRangeEnd(rangeEnd);

		assertEquals("The range end is set", rangeEnd, getReport().getRangeEnd());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setRangeStart(String)} for bad string.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetRangeStart_badString() {
		getReport().setRangeStart("Bad String");

		assertEquals("The range start is empty", "", getReport().getRangeStart());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setRangeStart(String)} for an empty string.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetRangeStart_emptyString() {
		getReport().setRangeStart("");

		assertEquals("The range start is empty", "", getReport().getRangeStart());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setRangeStart(String)} for formatted date.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	@Test
	public final void testSetRangeStart_formattedDate() {
		final String rangeStart = "01/06/2011";

		getReport().setRangeStart(rangeStart);

		assertEquals("The range start is set", rangeStart, getReport().getRangeStart());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setStandard(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetStandard() {
		getReport().setStandard(true);

		assertTrue("The report is a standard report", getReport().isStandard());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.reportingdashboard.Report#setTitle(java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	@Test
	public final void testSetTitle() {
		final String title = "Title";

		getReport().setTitle(title);

		assertEquals("The title is set", title, getReport().getTitle());
	}

	/**
	 * Custom assertion to ensure that the columns of a report are copied properly.
	 * 
	 * @author IanBrown
	 * @param expectedColumns
	 *            the expected columns (may be <code>null</code>).
	 * @param actualColumns
	 *            the actual columns (may be <code>null</code>).
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private void assertColumns(final List<ReportColumn> expectedColumns, final List<ReportColumn> actualColumns) {
		if (expectedColumns == null) {
			assertNull("There are no columns", actualColumns);
		} else {
			assertNotNull("There are columns", actualColumns);
			assertEquals("There are the correct number of columns", expectedColumns.size(), actualColumns.size());
		}
	}

	/**
	 * Custom assertion to ensure that the report is copied properly.
	 * 
	 * @author IanBrown
	 * @param expectedReport
	 *            the expected (original) report.
	 * @param actualReport
	 *            the actual (copied) report.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private void assertCopiedReport(final Report expectedReport, final Report actualReport) {
		assertNotNull("A copy of the report is created", actualReport);
		assertColumns(expectedReport.getColumns(), actualReport.getColumns());
		assertEquals("The from date is correct", expectedReport.getDateFrom(), actualReport.getDateFrom());
		assertEquals("The to date is correct", expectedReport.getDateTo(), actualReport.getDateTo());
		assertEquals("The description is correct", expectedReport.getDescription(), actualReport.getDescription());
		assertEquals("The faces are correct", expectedReport.getFaces(), actualReport.getFaces());
		assertSame("The flow type is correct", expectedReport.getFlowType(), actualReport.getFlowType());
		assertSame("The owner is correct", expectedReport.getOwner(), actualReport.getOwner());
		assertEquals("The standard flag is correct", expectedReport.isStandard(), actualReport.isStandard());
		assertEquals("The title is correct", expectedReport.getTitle(), actualReport.getTitle());
	}

	/**
	 * Creates a report.
	 * 
	 * @author IanBrown
	 * @return the report.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Report createReport() {
		return new Report();
	}

	/**
	 * Formats the input date.
	 * 
	 * @author IanBrown
	 * @param date
	 *            the date.
	 * @return the formatted date string.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	private String formatDate(final Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		return dateFormat.format(date);
	}

	/**
	 * Gets the report.
	 * 
	 * @author IanBrown
	 * @return the report.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Report getReport() {
		return report;
	}

	/**
	 * Sets the report.
	 * 
	 * @author IanBrown
	 * @param report
	 *            the report to set.
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private void setReport(final Report report) {
		this.report = report;
	}
}
