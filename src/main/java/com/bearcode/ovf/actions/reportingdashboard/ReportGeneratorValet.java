/**
 * 
 */
package com.bearcode.ovf.actions.reportingdashboard;

import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * Interface for objects that acquire resources for {@link ReportGenerator} objects.
 * 
 * @author IanBrown
 * 
 * @since Feb 7, 2012
 * @version Feb 7, 2012
 */
public interface ReportGeneratorValet {

	/**
	 * Acquires a rich text string for the input text.
	 * 
	 * @author IanBrown
	 * @param text
	 *            the text.
	 * @return the rich text string.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	HSSFRichTextString acquireRichTextString(String text);

	/**
	 * Acquires an Excel workbook.
	 * 
	 * @author IanBrown
	 * @return the workbook.
	 * @since Feb 7, 2012
	 * @version Feb 7, 2012
	 */
	HSSFWorkbook acquireWorkbook();
}
