package com.bearcode.ovf.eodcommands;

import com.bearcode.commons.config.Environment;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by leonid on 11.07.16.
 * Class for keeping context of exporting/importing LocalOfiicials (EOD)
 */
public class ExcelPortContext {
    /**
     * List of Local Officials to be exported / imported to/from Excel file
     */
    private List<LocalOfficial> localOfficials;
    /**
     * Descriptor which knows about each property column and cell
     */
    private ExcelPortDescriptor descriptor;

    private Logger log = LoggerFactory.getLogger( ExcelPortContext.class );

    public ExcelPortContext( Environment config ) {
        descriptor = new ExcelPortDescriptor( config );
        localOfficials = new LinkedList<LocalOfficial>(  );
    }

    public void addLocalOfficials( Collection<LocalOfficial> officials ) {
        localOfficials.addAll( officials );
    }

    public void addLocalOfficial( LocalOfficial official ) {
        localOfficials.add( official );
    }

    public List<LocalOfficial> getLocalOfficials() {
        return localOfficials;
    }

    /**
     * Define columns for export. Count numbers of objects in collections of LocalOfficials
     * (like officers or additionalAddresses)
     * to define actual column number for those collections
     */
    public void makeDispositions() {
        for ( LocalOfficial leo : localOfficials ) {
            descriptor.countCollections( leo );
        }
    }

    /**
     * Export information into row
     * @param row Excel row
     * @param leo LocalOfficial to be exported
     */
    public void writeExcelRow( HSSFRow row, LocalOfficial leo ) {
        try {
            descriptor.writeExcelRow( row, leo );
        } catch (ExcelPortException e) {
            log.error( "Error while writing data", e );
        }
    }

    /**
     * Write header with column titles
     * @param header Header row
     */
    public void writeExcelHeader( HSSFRow header ) {
        descriptor.writeExcelHeader( header );
    }

    /**
     * Write version number and some additional info
     */
    public void writeVersion( HSSFRow row ) {
        HSSFCell cell = row.createCell( (short) descriptor.getVersionLabelColumn() );
        cell.setCellValue( new HSSFRichTextString( "Version" ) );
        cell = row.createCell( (short) descriptor.getVersionColumn() );
        cell.setCellValue( descriptor.getVersion() );

        descriptor.writeBagsInfo( row, (short) (descriptor.getVersionColumn() + 1) );
    }


    public boolean checkVersion( HSSFSheet sheet ) {
        HSSFRow row = sheet.getRow( descriptor.getVersionLine() );
        return descriptor.parseVersionAndSizeInfo( row );
    }

    public boolean readEod( HSSFSheet sheet, State state ) {
        int rowNum = descriptor.getStartFromRow();
        try {
            do {
                HSSFRow row = sheet.getRow( rowNum );
                LocalOfficial leo = descriptor.parseFormRow( row, state );
                if ( leo == null ) {
                    break;
                }
                localOfficials.add( leo );
                rowNum++;
            } while ( true );
        } catch (ExcelPortException e) {
            log.error( "Cant parse file.", e );
        }

        return true;
    }
}
