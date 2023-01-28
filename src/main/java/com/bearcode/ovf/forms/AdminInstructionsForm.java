package com.bearcode.ovf.forms;

import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Required;

/**
 * Date: 21.09.11
 * Time: 2:13
 *
 * @author Leonid Ginzburg
 */
public class AdminInstructionsForm {

    private static final String STARTING_TEXT = "<page master=\"rava_third_page\">\r\n        <date-printed>$!date_long</date-printed>\r\n        <first-name>$!ufFirstName</first-name>\r\n        <value>\r\n            <font size=\"11px\" family=\"TrebuchetMS\" line-height=\"5mm\">\r\n";
    private static final String ENDING_TEXT ="</font>\r\n        </value>\r\n    </page>";
    private static final String DEFAULT_P = "<p margin-top=\"12px\">";
    private static final String DEFAULT_LI = "<li margin-top=\"10px\">";
    private static final String DEFAULT_UL = "<ul margin-left=\"10px\">";
    private static final String DEFAULT_OL = "<ol margin-left=\"10px\">";

    private FaceFlowInstruction instruction;
    private String text;
    private String flowTypeName;

    public AdminInstructionsForm( FaceFlowInstruction instruction ) {
        this.instruction = instruction;
    	flowTypeName = instruction.getFlowTypeName();
    }

    public FaceFlowInstruction getInstruction() {
        return instruction;
    }

    public void setInstruction( FaceFlowInstruction instruction ) {
        this.instruction = instruction;
    }

    @Required
    @NotEmpty
    public String getText() {
        return text;
    }

    public void setText( String text ) {
        this.text = text;
    }

    @Required
    @NotEmpty
    public String getFlowTypeName() {
        return flowTypeName;
    }

    public void setFlowTypeName( String flowTypeName ) {
        this.flowTypeName = flowTypeName;
    }

    public FaceConfig getFaceConfig() {
        return instruction.getFaceConfig();
    }

    public Long getId() {
        return instruction.getId();
    }

    public void textToShow() {
        text = instruction.getText();
        if ( text.trim().length() == 0 ) return;
		
        String[] splitOne = text.split( "<font[^>]*>", 2 );
        if ( splitOne.length == 2 ) {
            text = splitOne[1];
        }
        String[] splitTwo = text.split( "</font>" );
        if ( splitTwo.length == 2 ) {
            text = splitTwo[0];
        }
        else if ( splitTwo.length > 2 ) {
            StringBuffer buffer = new StringBuffer( splitTwo[0] );
            for ( int i = 1; i< splitTwo.length-1; i++) {
                buffer.append( "</font>" )
                .append( splitTwo[i] );
            }
            text = buffer.toString();
        }

        text = text.replaceAll( DEFAULT_P, "<p>" )
                .replaceAll( DEFAULT_OL, "<ol>" )
                .replaceAll( DEFAULT_UL, "<ul>" )
                .replaceAll( DEFAULT_LI, "<li>" );
    }

    public void textToSave() {
        StringBuffer buffer = new StringBuffer();
        buffer.append( STARTING_TEXT )
        .append( text.replaceAll( "<p>", DEFAULT_P )
                .replaceAll( "<ol>", DEFAULT_OL )
                .replaceAll( "<ul>", DEFAULT_UL )
                .replaceAll( "<li>", DEFAULT_LI ))
        .append( ENDING_TEXT );

        instruction.setText( buffer.toString() );
        instruction.setFlowTypeName( flowTypeName );
    }
}
