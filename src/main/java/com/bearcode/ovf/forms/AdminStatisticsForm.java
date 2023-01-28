package com.bearcode.ovf.forms;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 28, 2007
 * Time: 2:41:40 PM
 * @author Leonid Ginzburg
 */
public class AdminStatisticsForm {
    private Date startRangeDate;
    private Date endRangeDate;

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");


    public Date getStartRangeDate() {
        return startRangeDate;
    }

    public Date getEndRangeDate() {
        return endRangeDate;
    }


    public String getStartRange() {
        if ( startRangeDate == null ) return "";
        return dateFormat.format( startRangeDate );
    }

    public void setStartRange(String startRange) {
        try {
            startRangeDate = dateFormat.parse( startRange );
        } catch (ParseException e) {
            startRangeDate = null;
        }
    }

    public String getEndRange() {
        if ( endRangeDate == null ) return "";
        return dateFormat.format( endRangeDate );
    }

    public void setEndRange(String endRange) {
        try {
            endRangeDate = dateFormat.parse( endRange );
        } catch (ParseException e) {
            endRangeDate = null;
        }
    }
}
