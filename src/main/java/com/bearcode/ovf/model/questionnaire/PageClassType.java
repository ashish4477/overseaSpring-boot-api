package com.bearcode.ovf.model.questionnaire;

/**
 * Date: 10.12.11
 * Time: 16:39
 *
 * @author Leonid Ginzburg
 */
public enum PageClassType {
    GENERAL( "General Questionnaire Page" ),
    ADD_ON( "Add-on Page" ),
    EXTERNAL( "External Page" );

    private String description;

    PageClassType( String description ) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
