/**
 * 
 */
package com.bearcode.ovf.model.reportingdashboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FlowType;

/**
 * A report for the reporting dashboard.
 * 
 * @author IanBrown
 * 
 * @since Jan 4, 2012
 * @version Feb 3, 2012
 */
public class Report {

	/**
	 * the columns of the report.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private List<ReportColumn> columns;

	/**
	 * the starting date for the data.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Date dateFrom;

	/**
	 * the ending date for the data.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Date dateTo;

	/**
	 * the description of the report.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private String description;

	/**
	 * the type of flow for the report.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 6, 2012
	 */
	private FlowType flowType;

	/**
	 * the unique ID for the report.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private Long id;

	/**
	 * the owner of the report.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private OverseasUser owner;

	/**
	 * the title of the report.
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private String title;

	/**
	 * is this a standard report?
	 * 
	 * @author IanBrown
	 * @since Jan 5, 2012
	 * @version Jan 5, 2012
	 */
	private boolean standard;

	/**
	 * the faces for this report.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	private Set<String> faces;

	/**
	 * the format string for the dates.
	 * 
	 * @author IanBrown
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

	/**
	 * are faces to be applied?
	 * 
	 * @author IanBrown
	 * @since Feb 3, 2012
	 * @version Feb 3, 2012
	 */
	private boolean applyFaces;

	/**
	 * Adds a column to the report.
	 * 
	 * @author IanBrown
	 * @param column
	 *            the column.
	 * @return <code>true</code> if the column was added, <code>false</code> if it was not.
	 * @since Jan 10, 2012
	 * @version Jan 11, 2012
	 * @return
	 */
	public boolean addColumn(final ReportColumn column) {
		if (getColumns() == null) {
			setColumns(new LinkedList<ReportColumn>());
		}

		if (!getColumns().contains(column)) {
			getColumns().add(column);
			column.setReport(this);
			column.setColumnNumber(getColumns().size() - 1);
			return true;
		}

		return false;
	}

	/**
	 * Creates a deep copy of this report (copies all of the elements of the report).
	 * 
	 * @author IanBrown
	 * @return the copied report.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	public Report deepCopy() {
		final Report copiedReport = new Report();
		copiedReport.setColumns(deepCopyColumns(copiedReport));
		copiedReport.setDateFrom(getDateFrom());
		copiedReport.setDateTo(getDateTo());
		copiedReport.setDescription(getDescription());
		copiedReport.setFaces(copyFaces());
		copiedReport.setFlowType(getFlowType());
		copiedReport.setOwner(getOwner());
		copiedReport.setStandard(isStandard());
		copiedReport.setTitle(getTitle());
		return copiedReport;
	}

	/**
	 * Gets the columns of the report.
	 * 
	 * @author IanBrown
	 * @return the columns.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public List<ReportColumn> getColumns() {
		return columns;
	}

	/**
	 * Gets the starting date of the report.
	 * 
	 * @author IanBrown
	 * @return the starting date.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public Date getDateFrom() {
		return dateFrom;
	}

	/**
	 * Gets the ending date of the report.
	 * 
	 * @author IanBrown
	 * @return the ending date.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public Date getDateTo() {
		return dateTo;
	}

	/**
	 * Gets the description of the report.
	 * 
	 * @author IanBrown
	 * @return the description.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the faces that apply to this report.
	 * 
	 * @author IanBrown
	 * @return the names of the faces.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public Set<String> getFaces() {
		return faces;
	}

	/**
	 * Gets the faces as a string.
	 * 
	 * @author IanBrown
	 * @return the faces as a string.
	 * @since Jan 7, 2012
	 * @version Jan 7, 2012
	 */
	public String getFacesString() {
		final StringBuffer facesSB = new StringBuffer();

		final Set<String> faces = getFaces();
		if (faces != null) {
			String prefix = "";
			for (final String face : faces) {
				facesSB.append(prefix).append(face);
				prefix = ",";
			}
		}

		return facesSB.toString();
	}

	/**
	 * Gets the type of flow for the report.
	 * 
	 * @author IanBrown
	 * @return the flow type.
	 * @since Jan 4, 2012
	 * @version Jan 6, 2012
	 */
	public FlowType getFlowType() {
		return flowType;
	}

	/**
	 * Gets the report identifier.
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
	 * Gets the owner of the report.
	 * 
	 * @author IanBrown
	 * @return the owner.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public OverseasUser getOwner() {
		return owner;
	}

	/**
	 * Gets the end of the date range as a string.
	 * 
	 * @author IanBrown
	 * @return the end of the date range.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public String getRangeEnd() {
		return formatDate(getDateTo());
	}

	/**
	 * Gets the start of the date range as a string.
	 * 
	 * @author IanBrown
	 * @return the start of the date range.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public String getRangeStart() {
		return formatDate(getDateFrom());
	}

	/**
	 * Gets the title of the report.
	 * 
	 * @author IanBrown
	 * @return the title.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Is there a date range to apply? The date range can be open-ended (a <code>null</code> from or to).
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if there is a date range, <code>false</code> if there is not.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public boolean isApplyDateRange() {
		return (getDateFrom() != null) || (getDateTo() != null);
	}

	/**
	 * Are faces to be applied?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if faces are to be applied, <code>false</code> if not.
	 * @since Jan 6, 2012
	 * @version Feb 3, 2012
	 */
	public boolean isApplyFaces() {
		return applyFaces;
	}

	/**
	 * Is there a specific flow to apply?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if there is a flow, <code>false</code> if there is not.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public boolean isApplyFlow() {
		return getFlowType() != null;
	}

	/**
	 * Is this a standard report?
	 * 
	 * @author IanBrown
	 * @return <code>true</code> if this is a standard report, <code>false</code> otherwise.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public boolean isStandard() {
		return standard;
	}

	/**
	 * Removes the column from the report.
	 * 
	 * @author IanBrown
	 * @param column
	 *            the column.
	 * @return <code>true</code> if the column was removed, <code>false</code> if the column was not in the report.
	 * @since Jan 10, 2012
	 * @version Jan 27, 2012
	 */
	public boolean removeColumn(final ReportColumn column) {
		if ((getColumns() != null) && getColumns().contains(column)) {
			column.setReport(null);
			return getColumns().remove(column);
		}

		return false;
	}

	/**
	 * Sets the apply faces flag.
	 * 
	 * @author IanBrown
	 * @param applyFaces
	 *            the apply faces flag.
	 * @since Feb 3, 2012
	 * @version Feb 3, 2012
	 */
	public void setApplyFaces(final boolean applyFaces) {
		this.applyFaces = applyFaces;
	}

	/**
	 * Sets the columns of the report.
	 * 
	 * @author IanBrown
	 * @param columns
	 *            the columns.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setColumns(final List<ReportColumn> columns) {
		this.columns = columns;
	}

	/**
	 * Sets the starting date of the report.
	 * 
	 * @author IanBrown
	 * @param dateFrom
	 *            the starting date.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setDateFrom(final Date dateFrom) {
		this.dateFrom = dateFrom;
	}

	/**
	 * Sets the ending date of the report.
	 * 
	 * @author IanBrown
	 * @param dateTo
	 *            the ending date.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setDateTo(final Date dateTo) {
		this.dateTo = dateTo;
	}

	/**
	 * Sets the description of the report.
	 * 
	 * @author IanBrown
	 * @param description
	 *            the description.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setDescription(final String description) {
		this.description = description;
	}

	/**
	 * Sets the faces that apply to this report.
	 * 
	 * @author IanBrown
	 * @param faces
	 *            the names of the faces.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public void setFaces(final Set<String> faces) {
		this.faces = faces;
	}

	/**
	 * Sets the type of flow for the report.
	 * 
	 * @author IanBrown
	 * @param flowType
	 *            the flow type.
	 * @since Jan 4, 2012
	 * @version Jan 6, 2012
	 */
	public void setFlowType(final FlowType flowType) {
		this.flowType = flowType;
	}

	/**
	 * Sets the report identifier.
	 * 
	 * @author IanBrown
	 * @param id
	 *            the identifier.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setId(final Long id) {
		this.id = id;
	}

	/**
	 * Sets the owner of the report.
	 * 
	 * @author IanBrown
	 * @param owner
	 *            the owner.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setOwner(final OverseasUser owner) {
		this.owner = owner;
	}

	/**
	 * Sets the end of the date range as a string.
	 * 
	 * @author IanBrown
	 * @param rangeEnd
	 *            the end of the date range.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public void setRangeEnd(final String rangeEnd) {
		setDateTo(convertStringToDate(rangeEnd));
	}

	/**
	 * Sets the start of the date range as a string.
	 * 
	 * @author IanBrown
	 * @param rangeStart
	 *            the start of the date range.
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	public void setRangeStart(final String rangeStart) {
		setDateFrom(convertStringToDate(rangeStart));
	}

	/**
	 * Sets the standard report flag.
	 * 
	 * @author IanBrown
	 * @param standard
	 *            <code>true</code> if this is a standard report, <code>false</code> otherwise.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setStandard(final boolean standard) {
		this.standard = standard;
	}

	/**
	 * Sets the title of the report.
	 * 
	 * @author IanBrown
	 * @param title
	 *            the title.
	 * @since Jan 4, 2012
	 * @version Jan 5, 2012
	 */
	public void setTitle(final String title) {
		this.title = title;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(getClass().getSimpleName());

		sb.append(" ").append(getTitle()).append(" ").append(getDescription());
		if (isApplyDateRange()) {
			sb.append(" ").append(getRangeStart()).append("-").append(getRangeEnd());
		}
		if (isApplyFaces()) {
			sb.append(" States: ").append(getFaces());
		}
		sb.append(" Columns: ").append(getColumns());

		return sb.toString();
	}

	/**
	 * Converts the input string to a date.
	 * 
	 * @author IanBrown
	 * @param string
	 *            the string to convert.
	 * @return the date or <code>null</code> if the string is not a date.
	 * @since Jan 6, 2012
	 * @version Feb 3, 2012
	 */
	private Date convertStringToDate(final String string) {
		try {
			return (string == null) ? null : DATE_FORMAT.parse(string);
		} catch (final ParseException e) {
			return null;
		}
	}

	/**
	 * Copies the faces - handles the case where the existing list is <code>null</code>.
	 * 
	 * @author IanBrown
	 * @return the copied faces (or <code>null</code>).
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private Set<String> copyFaces() {
		if (getFaces() == null) {
			return null;
		}

		final Set<String> copiedFaces = new HashSet<String>(getFaces());
		return copiedFaces;
	}

	/**
	 * Performs a deep copy of the columns.
	 * 
	 * @author IanBrown
	 * @param copiedReport
	 *            the copied report.
	 * @return copiedReport the copied columns.
	 * @since Jan 9, 2012
	 * @version Jan 9, 2012
	 */
	private List<ReportColumn> deepCopyColumns(final Report copiedReport) {
		if (getColumns() == null) {
			return null;
		}

		final List<ReportColumn> copiedColumns = new LinkedList<ReportColumn>();
		for (final ReportColumn column : getColumns()) {
			final ReportColumn copiedColumn = column.deepCopy();
			copiedColumns.add(copiedColumn);
			copiedColumn.setReport(copiedReport);
		}
		return copiedColumns;
	}

	/**
	 * Formats the input date, allowing for <code>null</code> dates.
	 * 
	 * @author IanBrown
	 * @param date
	 *            the date to format.
	 * @return the formatted date (an empty string is the date is <code>null</code>).
	 * @since Jan 6, 2012
	 * @version Jan 6, 2012
	 */
	private String formatDate(final Date date) {
		return (date == null) ? "" : DATE_FORMAT.format(date);
	}
}
