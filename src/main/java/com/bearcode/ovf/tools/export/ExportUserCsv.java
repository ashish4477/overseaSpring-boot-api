package com.bearcode.ovf.tools.export;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.OutputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 25, 2007
 * Time: 4:44:11 PM
 * Class for convert LEO's data into or from Excel file
 */
public class ExportUserCsv extends UserExportAbstract {

    private static Logger log = LoggerFactory.getLogger( ExportUserCsv.class.getName() );
    /**
     * The upper limit on how many rows can be returned in the csv
     */
    public static int ROW_LIMIT = 65000;

    /**
     * The number of user instance retrieved per db query - we need to make sure the size of the collection does
     * not create an OutOfMemory exception in the JVM.
     * <p/>
     * IMPORTANT: this value needs to be set such that ROW_LIMIT % RESULTS_BUFFER_SIZE == 0 because the write() method
     * is not smart enough to handle any offsets > 0 :(
     */
    public static int RESULTS_BUFFER_SIZE = 500;

    @Override
    public void write( OutputStream out, int start, int limit ) throws Exception {

        /*
           NOTE: limit parameter is not used. See notes on ROW_LIMIT and RESULTS_BUFFER_SIZE above
           */
        // build header row
        String[] outTitles = getRavaColumnTitles();
        StringBuffer resString = new StringBuffer();
        int cols = outTitles.length;
        for ( int i = 0; i < cols; i++ ) {
            outputString( resString, outTitles[i] );
        }
        resString.deleteCharAt( resString.length() - 1 );
        resString.append( "\n" );
        out.write( resString.toString().getBytes( "UTF-8" ) );

        // this
        boolean done = false;
        // translate the requested page start point to the db paging start point
        int resultsPage = start * (ROW_LIMIT / RESULTS_BUFFER_SIZE);
        int resultSize = 0;

        // initialize all loop variable here
        Collection<OverseasUser> users = null;
        Iterator<OverseasUser> resultsKeysItr;
        OverseasUser user;
        StringBuffer outStr = null;
        WizardContext theForm = null;
        Map<String, Object> model = null;
        //String[] ravaColumnNames = getRavaColumnNames();
        String namesStr;
        String[] names;
        String valuesConcated;
        String name;
        Object valueObj;
        String value;

        UserFilterForm userFilter = new UserFilterForm();
        userFilter.setPage( start );
        userFilter.setPageSize( RESULTS_BUFFER_SIZE );

        while ( !done ) {

            users = userService.findUsers( userFilter );
            outStr = new StringBuffer( "" );
            resultsPage++;
            if ( users.size() < 1 ) {
                done = true;
                break;
            }
            // build data rows
            resultsKeysItr = users.iterator();
            while ( resultsKeysItr.hasNext() ) {
                Map<String, String> userVals = getUserValues( resultsKeysItr.next() );
                if ( resultSize >= ROW_LIMIT ) {
                    done = true;
                    break;
                }
                for ( int i = 0; i < cols; i++ ) {
                    outputString( resString, userVals.get( outTitles[i] ) );
                }
                outStr.deleteCharAt( outStr.length() - 1 );
                outStr.append( "\n" );
                resultSize++;
            }
            // push the current set of records to the client
            out.write( outStr.toString().getBytes( "UTF-8" ) );

            SessionFactory sessionFactory = userService.getUserDAO().getSessionFactory();
            // clear Hibernate first level cache
            sessionFactory.getCurrentSession().clear();
            // clear Hibernate second level cache of the objects we are using
            sessionFactory.getCache().evictEntityRegion( QuestionField.class );
            sessionFactory.getCache().evictEntityRegion( FieldDictionaryItem.class );
            sessionFactory.getCache().evictEntityRegion( Answer.class );
            sessionFactory.getCache().evictEntityRegion( OverseasUser.class );
        }
    }

    private void outputString( StringBuffer resString, String value ) {
        /*
          TODO: handle values that contain a " character
           */
        resString.append( "\"" )
                .append( value )
                .append( "\"," );
    }

}
