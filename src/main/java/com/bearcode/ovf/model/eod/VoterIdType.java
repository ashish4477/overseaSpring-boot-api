package com.bearcode.ovf.model.eod;

/**
 * Date: 25.12.13
 * Time: 20:17
 *
 * @author Leonid Ginzburg
 */
public enum VoterIdType {
    EMPTY(""),
    STRICT_PHOTO_ID("Strict Photo ID"), 	//A voter cannot cast a valid ballot without first presenting a photo ID (9 states)
    NON_STRICT_PHOTO_ID("Non-Strict Photo ID"), 	//A voter must first present a photo ID, but if the voter does not have one, they can vote a provisional ballot but must later present ID (8 states)
    STRICT_NON_PHOTO_ID("Strict Non-Photo ID"),	//A voter must first present some form of ID (it must not have a photo) or vote a provisional ballot that will only be counted if the voter presents ID later
    NON_STRICT_NON_PHOTO_ID("Non-Strict, Non-Photo ID"), 	//A voter must present some form of ID (it must not have a photo)
    NO_ID_REQUIRED("No ID Required");                     // No ID required

    private String text;

    private VoterIdType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
