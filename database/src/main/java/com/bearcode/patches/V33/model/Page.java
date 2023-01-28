package com.bearcode.patches.V33.model;

/**
 * Date: 30.04.12
 * Time: 23:33
 *
 * @author Leonid Ginzburg
 */
public class Page {
    private long id;
    private String title = "";
    private String popupBubble = "";
    private int number;
    private int stepNumber;
    private String formType = "";

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getPopupBubble() {
        return popupBubble;
    }

    public void setPopupBubble( String popupBubble ) {
        this.popupBubble = popupBubble;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber( int number ) {
        this.number = number;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber( int stepNumber ) {
        this.stepNumber = stepNumber;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType( String formType ) {
        this.formType = formType;
    }

    public Page copyFrom( Page page ) {
        title = page.getTitle();
        popupBubble = page.getPopupBubble();
        stepNumber = page.getStepNumber();
        return this;
    }
}
