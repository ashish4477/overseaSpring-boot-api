package com.bearcode.ovf.model.eod;

/**
 * Date: 13.01.14
 * Time: 17:06
 *
 * @author Leonid Ginzburg
 */
public enum SameDayRegistrationType {
    NO("", "No"),
    YES("✓", "Yes"),
    ONLY_IN_PERSON_EARLY_VOTING("allows same day registration ONLY for early in-person voting");

    private String text;
    private String mailText;

    SameDayRegistrationType(String text) {
        this.text = text;
        this.mailText = text;
    }

    SameDayRegistrationType( String text, String mailText ) {
        this.text = text;
        this.mailText = mailText;
    }

    public String getText() {
        return text;
    }

    public String getMailText() {
        return mailText;
    }
}
