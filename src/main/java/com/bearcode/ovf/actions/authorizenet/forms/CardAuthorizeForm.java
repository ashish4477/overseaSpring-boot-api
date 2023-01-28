package com.bearcode.ovf.actions.authorizenet.forms;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: May 20, 2008
 * Time: 2:37:11 PM
 * @author Leonid Ginzburg
 */
public class CardAuthorizeForm {
    private double amount;
    private String cardNum;
    private String cardExpiredMonth;
    private String cardExpiredYear;


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardExpiredMonth() {
        return cardExpiredMonth;
    }

    public void setCardExpiredMonth(String cardExpiredMonth) {
        this.cardExpiredMonth = cardExpiredMonth;
    }

    public String getCardExpiredYear() {
        return cardExpiredYear;
    }

    public void setCardExpiredYear(String cardExpiredYear) {
        this.cardExpiredYear = cardExpiredYear;
    }

    public String getCardExpired() {
        return cardExpiredMonth + cardExpiredYear;
    }
}
