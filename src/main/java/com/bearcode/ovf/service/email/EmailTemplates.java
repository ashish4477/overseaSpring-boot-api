package com.bearcode.ovf.service.email;

import com.bearcode.ovf.model.questionnaire.FlowType;

public class EmailTemplates {
	public static final String XML_EYV_THANK_YOU = "/WEB-INF/mails/eyv_thank_you.xml";
	public static final String XML_RAVA_THANK_YOU = "/WEB-INF/mails/rava_thank_you.xml";
	public static final String XML_FWAB_THANK_YOU = "/WEB-INF/mails/fwab_thank_you.xml";
	public static final String XML_DOMESTIC_REG_THANK_YOU = "/WEB-INF/mails/domestic_registration_thank_you.xml";
	public static final String XML_DOMESTIC_ABS_THANK_YOU = "/WEB-INF/mails/domestic_absentee_thank_you.xml";
	public static final String XML_CHANGE_PASSWORD = "/WEB-INF/mails/send_password.xml";
	public static final String XML_CHANGE_LOGIN = "/WEB-INF/mails/change_login.xml";
	public static final String XML_CHANGE_LOGIN_FAILURE = "/WEB-INF/mails/change_login_failure.xml";
	public static final String XML_OFFICER_SNAPSHOT = "/WEB-INF/mails/to_officer.xml";
    public static final String XML_CORRECTIONS_THANK_YOU = "/WEB-INF/mails/corrections_thank_you.xml";
	public static final String XML_NEW_ACCOUNT_THANK_YOU = "/WEB-INF/mails/new_voter_thank_you.xml";


	// template paths with full path hard coded
	public static final String XML_SCYTL_FAILURE_ALERT = "/WEB-INF/faces/basic/mails/scytl_failure_alert.xml";
    public static final String XML_EXCEPTION = "/WEB-INF/faces/basic/mails/exception_alert.xml";
    public static final String XML_AUTOMATED_DELIVERY_FAILURE = "/WEB-INF/faces/basic/mails/automated_delivery_failure.xml";

    public static String getEmailTemplate(final FlowType flowType) {
		switch (flowType) {
		case FWAB:
			return EmailTemplates.XML_FWAB_THANK_YOU;
		case DOMESTIC_REGISTRATION:
			return EmailTemplates.XML_DOMESTIC_REG_THANK_YOU;
		case DOMESTIC_ABSENTEE:
			return EmailTemplates.XML_DOMESTIC_ABS_THANK_YOU;
		case RAVA:
		default:
			return EmailTemplates.XML_RAVA_THANK_YOU;
		}
	}
}
