package com.bearcode.ovf.model.eod;

/**
 * Date: 25.12.13
 * Time: 20:15
 *
 * @author Leonid Ginzburg
 */
public enum MailVotingType {
    EMPTY(""),
    CERTAIN_ELECTIONS("certain Elections may be held by mail"),
    ALL_ELECTIONS("all elections are held by mail");

    private String text;

    private MailVotingType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
