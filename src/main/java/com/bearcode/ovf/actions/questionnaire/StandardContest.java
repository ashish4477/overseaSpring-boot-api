/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Enumeration of the standard contests.
 * 
 * @author IanBrown
 * 
 * @since Aug 8, 2012
 * @version Nov 1, 2012
 */
public enum StandardContest {

	/**
	 * the contest for the president.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Nov 1, 2012
	 */
	PRESIDENT(1, new String[] { "U.S. President & Vice President", "President", "President and Vice-President"}, "No contest for president", "ufPresident"),

	/**
	 * a contest for the house of representatives.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Nov 1, 2012
	 */
	REPRESENTATIVE(3, new String[] { "U.S. Representative", "Representative", "U.S. Representative.*" }, "No contest for house of representatives",
			"ufRepresentative"),

	/**
	 * a contest for the U.S. senate.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Nov 1, 2012
	 */
	SENATOR(2, new String[] { "U.S. Senator", "Senator", "U.S. Senator.*" }, "No contest for senator", "ufSenator");

	/**
	 * Returns the values of the standard contests ordered by index.
	 * 
	 * @author IanBrown
	 * @return the ordered values.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	public static final StandardContest[] orderedValues() {
		final List<StandardContest> contests = Arrays.asList(values());
		Collections.sort(contests, new Comparator<StandardContest>() {

			@Override
			public int compare(final StandardContest o1, final StandardContest o2) {
				return new Integer(o1.getIndex()).compareTo(new Integer(o2.getIndex()));
			}

		});

		return contests.toArray(new StandardContest[contests.size()]);
	}

	/**
	 * the alternative name for the group to contain the contest.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private final String alternativeGroupName;

	/**
	 * the index of the entry.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private final int index;

	/**
	 * the office of the contest (one or more options).
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	private final String[] office;
	
	/**
	 * the name to put into the PDF.
	 * @author IanBrown
	 * @since Nov 1, 2012
	 * @version Nov 1, 2012
	 */
	private final String inPdfName;

	/**
	 * Constructs a standard contest.
	 * 
	 * @author IanBrown
	 * @param index
	 *            the index of the contest.
	 * @param office
	 *            the office of the contest (one or more options).
	 * @param alternativeGroupName
	 *            the alternative name for the group.
	 * @param inPdfName the name to give the field in the PDF.
	 * @since Aug 8, 2012
	 * @version Nov 1, 2012
	 */
	private StandardContest(final int index, final String[] office, final String alternativeGroupName, String inPdfName) {
		this.index = index;
		this.office = office;
		this.alternativeGroupName = alternativeGroupName;
		this.inPdfName = inPdfName;
	}

	/**
	 * Gets the alternative group name.
	 * 
	 * @author IanBrown
	 * @return the alternative group name.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	public final String getAlternativeGroupName() {
		return alternativeGroupName;
	}

	/**
	 * Gets the index.
	 * 
	 * @author IanBrown
	 * @return the index.
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	public final int getIndex() {
		return index;
	}

	/**
	 * Gets the office.
	 * 
	 * @author IanBrown
	 * @return the office (one or more options).
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	public String[] getOffice() {
		return office;
	}

	/**
	 * Does the input value match?
	 * 
	 * @author IanBrown
	 * @param office
	 *            the office string.
	 * @return <code>true</code> if there is a match, <code>false</code> otherwise.
	 * @since Sep 21, 2012
	 * @version Sep 21, 2012
	 */
	public final boolean matches(final String office) {
		final String officeUC = office.toUpperCase();
		for (final String officePattern : getOffice()) {
			final String officePatternUC = officePattern.toUpperCase();
			if (Pattern.matches(officePatternUC, officeUC)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the name for the field in the PDF.
	 * @author IanBrown
	 * @return the in PDF name.
	 * @since Nov 1, 2012
	 * @version Nov 1, 2012
	 */
	public String getInPdfName() {
		return inPdfName;
	}
}