package com.bearcode.ovf.actions.commons;

import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.QuestionFieldService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.utils.UserInfoFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Dec 17, 2007
 * Time: 6:43:37 PM
 *
 * @author Leonid Ginzburg
 * @deprecated
 */
@Controller
@RequestMapping(value = "/service/exportMailingList.htm")
public class MailingList {

    @Autowired
    QuestionnaireService questionnaireService;

    @Autowired
    QuestionFieldService questionFieldService;

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<byte[]> handleRequestInternal(
            HttpServletRequest request,
            @RequestParam(value = "fromDate", required = false, defaultValue = "") String fromDateStr,
            @RequestParam(value = "toDate", required = false, defaultValue = "") String toDateStr
    ) throws Exception {
        String[] outTitles = {
                UserInfoFields.VOTING_STATE,
                UserInfoFields.VOTING_REGION,
                UserInfoFields.CURRENT_COUNTRY,
                UserInfoFields.VOTING_CITY,
                UserInfoFields.BIRTH_YEAR,
                UserInfoFields.VOTER_TYPE,
                UserInfoFields.CURRENT_ADDRESS,
                UserInfoFields.CURRENT_POSTAL_CODE,
                UserInfoFields.CURRENT_CITY,
                UserInfoFields.PHONE
        };

        SimpleDateFormat format = new SimpleDateFormat( "MM/dd/yyyy-hh:mm:ss" );
        SimpleDateFormat outFormat = new SimpleDateFormat( "MM/dd/yyyy hh:mm:ss" );
        //int page = MapUtils.getInteger( request.getParameterMap(), "page", 0 );
        Date fromDate;
        Date toDate;
        try {
            //try to parse as date
            fromDate = format.parse( fromDateStr );
        } catch ( ParseException e ) {
            // try to parse as long
            try {
                Long fromMls = Long.parseLong( fromDateStr );
                fromDate = new Date( fromMls );
            } catch ( NumberFormatException e1 ) {
                //set default
                fromDate = new Date( 0 );
            }
        }
        try {
            //try to parse as date
            toDate = format.parse( toDateStr );
        } catch ( ParseException e ) {
            // try to parse as long
            try {
                Long fromMls = Long.parseLong( toDateStr );
                toDate = fromMls > 0 ? new Date( fromMls ) : new Date();
            } catch ( NumberFormatException e1 ) {
                //set default
                toDate = new Date();
            }
        }

        Collection<WizardResults> wizardResults = questionnaireService.findByFieldValue( "true", fromDate, toDate );

        // make header
        StringBuffer resString = new StringBuffer( "\"email\",\"first name\",\"last name\",\"Date of last update\",\"Face\"," );
        for ( String title : outTitles ) {
            outputString( resString, title );
        }
        resString.deleteCharAt( resString.length() - 1 );
        resString.append( "\n" );
        for ( WizardResults wizardResult : wizardResults ) {
            outputString( resString, wizardResult.getUsername() );
            outputString( resString, wizardResult.getName().getFirstName() );
            outputString( resString, wizardResult.getName().getLastName() );
            outputString( resString, outFormat.format( wizardResult.getLastChangedDate() ) );
            outputString( resString, wizardResult.getFaceUrl().replaceAll( request.getContextPath(), "" ) );
            outputString( resString, wizardResult.getVotingAddress() != null ? wizardResult.getVotingAddress().getState() : "" );
            outputString( resString, wizardResult.getVotingRegionName() != null ? wizardResult.getVotingRegionName() : "" );
            outputString( resString, wizardResult.getVotingAddress() != null ? wizardResult.getVotingAddress().getCountry() : "" );
            outputString( resString, wizardResult.getVotingAddress() != null ? wizardResult.getVotingAddress().getCity() : "" );
            outputString( resString, String.valueOf( wizardResult.getBirthYear() ) );
            outputString( resString, wizardResult.getVoterType() );
            outputString( resString, wizardResult.getCurrentAddress() != null ? wizardResult.getCurrentAddress().getStreet1() + "," + wizardResult.getCurrentAddress().getStreet2() : "" );
            outputString( resString, wizardResult.getCurrentAddress() != null ? wizardResult.getCurrentAddress().getZip() : "" );
            outputString( resString, wizardResult.getCurrentAddress() != null ? wizardResult.getCurrentAddress().getCity() : "" );
            outputString( resString, wizardResult.getPhone() );

            resString.deleteCharAt( resString.length() - 1 ); // remove comma at end
            resString.append( "\n" );
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType( new MediaType( "text", "csv" ) );
        headers.add( "Content-Disposition", "attachment; filename=mailing_list.csv" );
        return new ResponseEntity<byte[]>( resString.toString().getBytes( "UTF-8" ), headers, HttpStatus.CREATED );
    }

    private void outputString( StringBuffer resString, String value ) {
        resString.append( "\"" )
                .append( value )
                .append( "\"," );
    }
}
