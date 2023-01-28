package com.bearcode.ovf.tools.pdf;

import java.text.SimpleDateFormat;

/**
 * PDF Generator interface
 */
public interface PdfGenerator {

	public static final String USER_FIELD_PREFIX = "uf";
	public static final String USER_FIELD_CHECKBOX_PREFIX = "uc";
	public static final SimpleDateFormat SHORT_FORMAT = new SimpleDateFormat("MM/dd/yyyy");
	public static final SimpleDateFormat LONG_FORMAT = new SimpleDateFormat("MMMM d, yyyy");
	public static final SimpleDateFormat YEAR_FORMAT = new SimpleDateFormat("yyyy");
	public static final String USER_FIELD_LEO_ADDRESS = "ufLocalElectionOfficialAddress";
	public static final String USER_FIELD_LEO_PERSON = "ufLocalElectionOfficial";
    public static final String USER_FIELD_LOVC_PERSON = "ufAbsenteeVoterClerk";
	public static final String USER_FIELD_LEO_EMAIL = "ufLocalElectionOfficialEmail";
	public static final String USER_FIELD_LEO_NAME = "ufLeoName";
	public static final String USER_FIELD_LEO_PHONE = "ufLeoPhone";
	public static final String USER_FIELD_LEO_FAX = "ufLeoFax";

	/**
	 * Dispose generator
	 */
	void dispose();

	/**
	 * Generate PDF file
	 * 
	 * @throws PdfGeneratorException
	 */
	void run() throws PdfGeneratorException;

}