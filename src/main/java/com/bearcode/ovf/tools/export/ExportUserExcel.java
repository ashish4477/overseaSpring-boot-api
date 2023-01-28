package com.bearcode.ovf.tools.export;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.forms.UserFilterForm;
import com.bearcode.ovf.model.common.OverseasUser;
import org.apache.poi.hssf.usermodel.*;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 25, 2007
 * Time: 4:44:11 PM
 * Class for convert LEO's data into or from Excel file
 */
public class ExportUserExcel extends UserExportAbstract {


    // export into file
    protected void writeSheetIntoExcel( String sheetName, LinkedHashMap<OverseasUser, WizardContext> userData, HSSFWorkbook excelbook ) throws IOException {
        HSSFSheet sheet = excelbook.createSheet();
        excelbook.setSheetName( excelbook.getNumberOfSheets() - 1, sheetName );
        //writeSheet( userData, sheet);
    }

    // export into file
    protected void writeIntoSheet( OverseasUser user, HSSFSheet sheet ) throws IOException {

        HSSFRow row = null;
        int rownum = sheet.getPhysicalNumberOfRows();

        // create header row
        if ( rownum == 0 ) {
            row = sheet.createRow( rownum++ );
            writeHeader( row );

            // set column width
            for ( short i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++ ) {
                HSSFCell cell = row.getCell( i );
                short headerw = (short) (cell.getRichStringCellValue().length() * 300);
                sheet.setColumnWidth( i, headerw );
            }
        }

        // write the data row
        row = sheet.createRow( rownum++ );
        writeRow( row, user );

        for ( short i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++ ) {
            HSSFCell cell = row.getCell( i );
            if ( cell != null ) {
                HSSFRichTextString content = cell.getRichStringCellValue();
                short origw = sheet.getColumnWidth( i );
                short headerw = (short) (content.length() * 300);
                sheet.setColumnWidth( i, headerw > origw ? headerw : origw );
            }
        }

    }

    private void writeRow( HSSFRow row, OverseasUser user ) {

        short col = (short) 0;
        HSSFCell cell;

        Map<String, String> userVals = getUserValues( user );
        for ( int i = 0; i < getRavaColumnTitles().length; i++ ) {
            cell = row.createCell( col++ );
            cell.setCellValue( new HSSFRichTextString( userVals.get( getRavaColumnTitles()[i] ) ) );
        }
    }


    private void writeHeader( HSSFRow row ) {

        short col = (short) 0;
        HSSFCell cell;
        for ( String namesStr : getRavaColumnTitles() ) {
            cell = row.createCell( col++ );
            cell.setCellValue( new HSSFRichTextString( namesStr ) );
        }
    }


    @Override
    public void write( OutputStream out, int start, int limit ) throws Exception {

        HSSFWorkbook excelbook = new HSSFWorkbook();
        // break the data into chunks for each sheet

        int MAX_PER_SHEET = 65535;
        int RESULTS_BUFFER_SIZE = 1000;
        int sheetCnt = 0;
        int resultsPage = 0;
        HSSFSheet currentSheet = null;

        UserFilterForm userFilter = new UserFilterForm();
        userFilter.setPage( start );
        userFilter.setPageSize( RESULTS_BUFFER_SIZE );


        while ( true ) {

            Collection<OverseasUser> results = userService.findUsers( userFilter );
            resultsPage++;
            if ( results.size() < 1 ) {
                break;
            }

            for ( OverseasUser user : results ) {
                if ( currentSheet == null || currentSheet.getPhysicalNumberOfRows() >= MAX_PER_SHEET ) {
                    currentSheet = excelbook.createSheet();
                    sheetCnt = excelbook.getNumberOfSheets();
                    excelbook.setSheetName( sheetCnt - 1, "sheet" + sheetCnt );
                    sheetCnt++;
                }
                writeIntoSheet( user, currentSheet );
            }
        }
        excelbook.write( out );

    }
}
