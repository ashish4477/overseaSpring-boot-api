package com.bearcode.ovf.model.eod;

/**
 * Created by leonid on 18.06.16.
 */
public enum PredefinedAdditionalAddressTypes {
    VOTER_REGISTRATION_MAILING_ADDRESS("Voter Registration Mailing Address"),
    VOTE_BY_MAIL_BALLOT_REQUEST_ADDRESS("Vote-by-Mail (Absentee) Ballot Request Address"),
    BALLOT_MAILING_ADDRESS("Ballot Return Mailing Address"),
    COURIER_ADDRESS("Courier/Delivery Address");

    private String name;

    PredefinedAdditionalAddressTypes( String name ) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
