package com.bearcode.patches.V75;

/**
 * Date: 12.08.14
 * Time: 16:46
 *
 * @author Leonid Ginzburg
 */
public enum Instructions {
    STREET1("ufIndianaVotingStreet1","Indiana Overseas Voter Address 1",
            "#if ( $ufVoterType == \"CITIZEN_OVERSEAS_INDEFINITELY\" && $leo )$!leo.physical.street1 " +
            "#else$!ufVotingStreet1 #end"),
    STREET2("ufIndianaVotingStreet2","Indiana Overseas Voter Address 2",
                "#if ( $ufVoterType == \"CITIZEN_OVERSEAS_INDEFINITELY\" && $leo )$!leo.physical.street2 " +
                "#else$!ufVotingStreet2 #end"),
    CITY("ufIndianaVotingCity","Indiana Overseas Voter City",
                "#if ( $ufVoterType == \"CITIZEN_OVERSEAS_INDEFINITELY\" && $leo )$!leo.physical.city " +
                "#else$!ufVotingCity #end"),
    ZIP_CODE5("ufIndianaVotingZipCode5","Indiana Overseas Voter Zip Code 5",
                "#if ( $ufVoterType == \"CITIZEN_OVERSEAS_INDEFINITELY\" && $leo )$!leo.physical.zip " +
                "#else$!ufVotingZipCode5 #end"),
    ZIP_CODE4("ufIndianaVotingZipCode4","Indiana Overseas Voter Zip Code 4",
                "#if ( $ufVoterType == \"CITIZEN_OVERSEAS_INDEFINITELY\" && $leo )$!leo.physical.zip4 " +
                "#else$!ufVotingZipCode4 #end");

    private String inPdfName;
    private String description;
    private String text;

    Instructions(String inPdfName, String description, String text) {
        this.inPdfName = inPdfName;
        this.description = description;
        this.text = text;
    }

    public String getInPdfName() {
        return inPdfName;
    }

    public void setInPdfName(String inPdfName) {
        this.inPdfName = inPdfName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
