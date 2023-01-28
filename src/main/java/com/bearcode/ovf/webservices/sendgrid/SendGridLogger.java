package com.bearcode.ovf.webservices.sendgrid;

import com.bearcode.ovf.model.system.OvfPropertyNames;
import com.bearcode.ovf.service.OvfPropertyService;
import com.bearcode.ovf.service.SendGridLogService;
import com.bearcode.ovf.webservices.sendgrid.model.SendGridLogMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;

/**
 * @author leonid.
 */
@Component
public class SendGridLogger {

    @Autowired
    SendGridLogService sendGridService;

    @Autowired
    OvfPropertyService propertyService;

    private int levelNumber = 0;

    private Calendar cacheTime = Calendar.getInstance();

    private void createMessage( String logLevel, String message ) {
        SendGridLogMessage logMessage =  new SendGridLogMessage();
        logMessage.setLogDate( new Date() );
        logMessage.setLogLevel( logLevel );
        logMessage.setMessage( message );
        sendGridService.saveLog( logMessage );
    }

    public void info( String message ) {
        if ( checkLogLevel() >= 3 ) {
            createMessage( "INFO", message );
        }
    }

    public void debug( String message ) {
        if ( checkLogLevel() >= 4 ) {
            createMessage( "DEBUG", message );
        }
    }

    public void warning( String message ) {
        if ( checkLogLevel() >= 2 ) {
            createMessage( "WARNING", message );
        }
    }

    public void error( String message ) {
        if ( checkLogLevel() >= 1 ) {
            createMessage( "ERROR", message );
        }
    }

    private int checkLogLevel() {
        long time = Calendar.getInstance().getTimeInMillis() - cacheTime.getTimeInMillis();
        if ( levelNumber == 0 || time > 3600000 ) {  // once in hour
            String level = propertyService.getProperty( OvfPropertyNames.SEND_GRID_LOG_LEVEL );
            int logAvailable = propertyService.getPropertyAsInt( OvfPropertyNames.SEND_GRID_LOG_AVAILABLE );
            if ( logAvailable == 0 ) {
                levelNumber = -1;
            } else {
                if ( level.equalsIgnoreCase( "ERROR" ) ) {
                    levelNumber = 1;
                }
                if ( level.equalsIgnoreCase( "WARNING" ) ) {
                    levelNumber = 2;
                }
                if ( level.equalsIgnoreCase( "INFO" ) ) {
                    levelNumber = 3;
                }
                if ( level.equalsIgnoreCase( "DEBUG" ) ) {
                    levelNumber = 4;
                }
            }
            cacheTime = Calendar.getInstance();
        }
        return levelNumber;
    }
}
