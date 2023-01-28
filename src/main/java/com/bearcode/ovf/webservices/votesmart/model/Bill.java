package com.bearcode.ovf.webservices.votesmart.model;

import java.util.List;

/**
 * Date: 09.10.13
 * Time: 18:15
 *
 * @author Leonid Ginzburg
 */
public class Bill {
    String billId;
    String billNumber;
    String title;
    String officeId;
    String office;
    String vote;
    String actionId;
    String stage;
    List<VotesCategory> categories;

    public Bill() {
    }

    public Bill(String billId, String billNumber, String title, String officeId, String office, String vote, String actionId, String stage) {
        this.billId = billId;
        this.billNumber = billNumber;
        this.title = title;
        this.officeId = officeId;
        this.office = office;
        this.vote = vote;
        this.actionId = actionId;
        this.stage = stage;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getVote() {
        if ( vote.equalsIgnoreCase("n")) return "Nay";
        if ( vote.equalsIgnoreCase("y")) return "Yea";
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public List<VotesCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<VotesCategory> categories) {
        this.categories = categories;
    }

}
