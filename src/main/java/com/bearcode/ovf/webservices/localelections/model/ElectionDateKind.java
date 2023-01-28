package com.bearcode.ovf.webservices.localelections.model;

/**
 * Created by leonid on 31.05.16.
 *
 * Election Date Kind - to define Election Date Type and make it readable
 */
public enum ElectionDateKind {
    //multiple fields per election
    DRD("Domestic","Registration"),
    DBRD("Domestic","Ballot Request"),
    DBED("Domestic","Ballot Return"),

    ORD("Overseas","Registration"),
    OBRD("Overseas","Ballot Request"),
    OBED("Overseas","Ballot Return"),

    MRD("Military","Registration"),
    MBRD("Military","Ballot Request"),
    MBED("Military","Ballot Return"),

    // single field per election
    EVF("Early In-Person Voting","From"),
    EVT("Early In-Person Voting","To"),

    AVF("Absentee Voting","From"),
    AVT("Absentee Voting","To");

    private String name;
    private String kind;

    ElectionDateKind( String kind, String name ) {
        this.name = name;
        this.kind = kind;
    }

    public String getName() {
        return name;
    }

    public String getKind() {
        return kind;
    }
}
