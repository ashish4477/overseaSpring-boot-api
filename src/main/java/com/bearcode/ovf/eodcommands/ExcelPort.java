package com.bearcode.ovf.eodcommands;

import com.bearcode.commons.config.Environment;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 25, 2007
 * Time: 4:44:11 PM
 * Class for convert LEO's data into or from Excel file
 */
public class ExcelPort {

    private Environment config;
    private Logger log = LoggerFactory.getLogger( ExcelPort.class );


    public Environment getConfig() {
        return config;
    }

    public void setConfig( Environment config ) {
        this.config = config;
    }

    // ------------------------------------------------
    // IMPORT
    // ------------------------------------------------
    public Collection<LocalOfficial> readFromExcel( InputStream file, State state ) throws IOException {

        POIFSFileSystem fs = new POIFSFileSystem( file );
        HSSFWorkbook wb = new HSSFWorkbook( fs );

        HSSFSheet sheet = wb.getSheetAt( 0 );

        ExcelPortContext context = new ExcelPortContext( config );

        if ( context.checkVersion( sheet ) ) {
            context.readEod( sheet, state );
            return context.getLocalOfficials();
        }
        return Collections.emptyList();
    }


/*  This code was not deleted to keep example of a feature which is noimplemented and is not used anymore
    Maybe we want to implement it in the future

    private int parseCompoundElement( HSSFRow row, LocalOfficial leo, String configPath, int cellNum, String propertyPath, int elementOffset ) throws ExcelPortException {
        String path = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_OBJECT_PATH, "" );
        List columns = config.getList( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_NUMB );
        String pattern = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_PATTERN, "" );
        int offset = config.getIntProperty( ATTR_COLUMN_OFFSET, 0 ) + elementOffset;

        int lastReadColumn = 0;
        boolean emptyValue = true;
        for ( int elementNum = 0; elementNum < columns.size(); elementNum++ ) {
            short column = Short.parseShort( ((String) columns.get( elementNum )) );
            HSSFCell cell = row.getCell( (short) (column + offset) );
            String value = "";
            if ( cell != null ) {
                switch ( cell.getCellType() ) {
                    case HSSFCell.CELL_TYPE_STRING:
                        value = cell.getRichStringCellValue().getString().trim();
                        break;
                    case HSSFCell.CELL_TYPE_NUMERIC:
                        value = Long.toString( Math.round( cell.getNumericCellValue() ) );
                        break;
                    default:
                }
            }
            if ( value.length() > 1 ) {
                emptyValue = false;
            }
            pattern = pattern.replaceAll( "#" + (elementNum + 1), value );
            lastReadColumn = Math.max( column, lastReadColumn );
        }
        setValueByPath( leo, propertyPath + path, emptyValue ? "" : pattern );
        return lastReadColumn + elementOffset;
    }
*/

    // ------------------------------------------------
    // EXPORT
    // ------------------------------------------------
    public HSSFWorkbook writeIntoExcel( State state, Collection<LocalOfficial> eod ) throws IOException {
        HSSFWorkbook excelBook = new HSSFWorkbook();
        writeSheetIntoExcel( state, eod, excelBook );
        return excelBook;
    }

    // export into file
    public HSSFWorkbook writeIntoExcel( Collection<State> states, Collection<LocalOfficial> eod ) throws IOException {
        HSSFWorkbook excelBook = new HSSFWorkbook();
        for ( State state : states ) {
            writeSheetIntoExcel( state, eod, excelBook );
        }
        return excelBook;
    }

    // export into file
    protected void writeSheetIntoExcel( State state, Collection<LocalOfficial> eod, HSSFWorkbook excelbook ) throws IOException {
        HSSFSheet sheet = excelbook.createSheet();
        excelbook.setSheetName( excelbook.getNumberOfSheets() - 1, state.getName() );
        writeSheet( state, eod, sheet );
    }

    // export into file
    protected void writeSheet( State state, Collection<LocalOfficial> eod, HSSFSheet sheet ) throws IOException {

        //make some preparations
        ExcelPortContext context = new ExcelPortContext( config );
        // add officals into export list
        for ( LocalOfficial leo : eod ) {
            if ( leo.getRegion().getState().equals( state ) ) {
                context.addLocalOfficial( leo );
            }
        }
        context.makeDispositions();

        HSSFRow row;
        int rownum = 0;

        // create header row
        row = sheet.createRow( rownum++ );
        context.writeVersion( row );
        HSSFRow header = sheet.createRow( rownum++ );
        context.writeExcelHeader( header );

        // write the data rows
        for ( LocalOfficial leo : context.getLocalOfficials() ) {
            row = sheet.createRow( rownum++ );
            context.writeExcelRow( row, leo );
        }
        // set column width
        for ( short i = header.getFirstCellNum(); i <= header.getLastCellNum(); i++ ) {
            HSSFCell cell = header.getCell( i );
            if ( cell != null ) {
                short headerw = (short) (cell.getRichStringCellValue().length() * 310);
                sheet.setColumnWidth( i, headerw );
            }
        }
        for ( int rowNum = header.getRowNum() + 1; rowNum <= sheet.getLastRowNum(); rowNum++ ) {
            row = sheet.getRow( rowNum );
            for ( short i = row.getFirstCellNum(); i <= row.getLastCellNum(); i++ ) {
                HSSFCell cell = row.getCell( i );
                if ( cell != null ) {
                    HSSFRichTextString content = cell.getRichStringCellValue();
                    short origw = sheet.getColumnWidth( i );
                    short headerw = (short) (content.length() * 310);
                    sheet.setColumnWidth( i, headerw > origw ? headerw : origw );
                }
            }
        }
    }


/*
    private int writeCompoundCell( HSSFRow row, LocalOfficial leo, String configPath, int cellNum, String propertyPath, int elementOffset ) throws ExcelPortException {
        String path = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_OBJECT_PATH, "" );
        List columns = config.getList( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_NUMB );
        int offset = config.getIntProperty( ATTR_COLUMN_OFFSET, 0 ) + elementOffset;
        String writePattern = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_WRITE_PATTERN, "" );

        int lastWrittenColumn = 0;
        Matcher matcher = Pattern.compile( writePattern ).matcher( getValueByPath( leo, propertyPath + path ) );
        if ( matcher.matches() ) {

            for ( int elementNum = 0; elementNum < columns.size(); elementNum++ ) {
                short column = Short.parseShort( ((String) columns.get( elementNum )) );
                HSSFCell cell = row.createCell( (short) (column + offset) );
                cell.setCellValue( new HSSFRichTextString( matcher.group( elementNum + 1 ) ) );
                lastWrittenColumn = Math.max( column, lastWrittenColumn );
            }
        }

        return lastWrittenColumn + elementOffset;
    }
*/

/*

    private void writeCompoundHeader( HSSFRow row, int cellNum, String configPath, int elementOffset ) {
        String path = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_OBJECT_PATH, "" );
        List columns = config.getList( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_NUMB );
        List labels = config.getList( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_LABEL );
        int offset = config.getIntProperty( ATTR_COLUMN_OFFSET, 0 ) + elementOffset;

        for ( int elementNum = 0; elementNum < columns.size(); elementNum++ ) {
            short column = (short) (Short.parseShort( ((String) columns.get( elementNum )) ) + offset);
            String label = path + " " + (elementNum + 1);
            if ( labels != null && labels.size() == columns.size() ) {
                label = (String) labels.get( elementNum );
            }
            HSSFCell cell = row.createCell( column );
            cell.setCellValue( new HSSFRichTextString( label ) );
        }

    }
*/


}
