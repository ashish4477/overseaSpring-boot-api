package com.bearcode.ovf.eodcommands.model;

import com.bearcode.ovf.editor.ReportDateEditor;
import com.bearcode.ovf.eodcommands.ExcelPortException;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.tools.vip.xml.ObjectFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.propertyeditors.CustomDateEditor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by leonid on 11.07.16.
 * Class to represent one element of export (one cell with value of simple property in most cases)
 */
public class ExportElement {

    /**
     * date format using dashes for year, month, and day.
     */
    final static SimpleDateFormat DASHES_YEAR_MONTH_DAY_TIME = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

    /**
     * Column to write info into / read info from
     */
    protected int column = 0;
    /**
     * Property name
     */
    protected String propertyName = "";
    /**
     * Label in header
     */
    protected String headerLabel = "";

    /**
     * Link to parent element (in case this element represents property of object from collection of
     * LocalOfficial, either Contacts or AdditionalAddresses)
     */
    protected ExportElement bag = null;

    /**
     * Maximum number of objects in collection
     */
    protected int actualObjectsCount = 1;

    public ExportElement( int column, String propertyName, String headerLabel ) {
        this.column = column;
        this.propertyName = propertyName;
        this.headerLabel = headerLabel;
    }

    public ExportElement( int column, String propertyName, String headerLabel, ExportElement bag ) {
        this.column = column;
        this.propertyName = propertyName;
        this.headerLabel = headerLabel;
        this.bag = bag;
    }

    public int getColumn() {
        return column;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getHeaderLabel() {
        return headerLabel;
    }

    public int getActualObjectsCount() {
        return actualObjectsCount;
    }

    /**
     * Used do define maximum number of object in collections
     * so it could increase only
     * @param actualObjectsCount actual number of objects in a collection of a LocalOfficial
     */
    public void setActualObjectsCount( int actualObjectsCount ) {
        if ( actualObjectsCount > this.actualObjectsCount ) {
            this.actualObjectsCount = actualObjectsCount; // set maximum
        }
    }

    public int defineActualColumn( int column ) {
        this.column = column;
        return column + actualObjectsCount;
    }

    public void writeIntoRow( HSSFRow row, Object leo ) throws ExcelPortException {
        writeIntoRow( row, 0, leo );
    }

    public void writeIntoRow( HSSFRow row, int offset, Object leo ) throws ExcelPortException {
        HSSFCell cell = row.createCell( (short) (offset + column ) );
        cell.setCellValue( new HSSFRichTextString( getValueByPath( leo, propertyName ) ) );
    }

    protected Object getObjectByPath( Object operand, String path ) throws ExcelPortException {
        try {
            final String[] pathValues = path.split("\\.");
            Object workingValue = operand;
            for (final String pathValue : pathValues) {
                final BeanWrapper op = new BeanWrapperImpl(workingValue);
                op.registerCustomEditor( Date.class, new CustomDateEditor( DASHES_YEAR_MONTH_DAY_TIME, true ) );
                workingValue = op.getPropertyValue(pathValue);
                if (workingValue == null) {
                    break;
                }
            }
            return workingValue;
        } catch ( BeansException e ) {
            throw new ExcelPortException( "Can't access property :" + path, e );
        }

    }

    protected String getValueByPath( Object operand, String path ) throws ExcelPortException {
        Object value = getObjectByPath( operand, path );
        return value == null ? "" : value.toString();
    }

    protected void setValueByPath( Object operand, String path, String value ) throws ExcelPortException {
        try {
            BeanWrapper op = new BeanWrapperImpl( operand );
            op.registerCustomEditor( Date.class, new CustomDateEditor( DASHES_YEAR_MONTH_DAY_TIME, true ) );
            op.setAutoGrowNestedPaths( true );
            op.setPropertyValue( path, value );
        } catch ( BeansException e ) {
            throw new ExcelPortException( "Can't set property :" + path, e );
        }
    }

    public void writeHeader( HSSFRow header  ) {
        writeHeader( header, 0, "" );
    }

    public void writeHeader( HSSFRow header, int offset, String prefix ) {
        HSSFCell cell = header.createCell( (short) (offset + column) );
        cell.setCellValue( new HSSFRichTextString( String.format( "%s%s", prefix, headerLabel ) ) );
    }

    public short writeActualCountsInfo( HSSFRow row, short cellNum ) {
        return cellNum;
    }

    public void parseFromRow( HSSFRow row, Object leo ) throws ExcelPortException {
        parseFromRow( row, 0, "", leo );
    }

    public void parseFromRow( HSSFRow row, int offset, String prefix, Object leo ) throws ExcelPortException {
        HSSFCell cell = row.getCell( (short) (offset + column) );
        if ( cell != null ) {
            String value;
            switch ( cell.getCellType() ) {
                case HSSFCell.CELL_TYPE_STRING:
                    value = cell.getRichStringCellValue().getString().trim();
                    break;
                case HSSFCell.CELL_TYPE_NUMERIC:
                    value = Long.toString( Math.round( cell.getNumericCellValue() ) );
                    break;
                default:
                    return;
/*
                    value = "";  //unsupported cell type
                    log.warn( String.format( "Trying to parse unsupported cell type \"%d\"", cell.getCellType() ) );
*/
            }
            setValueByPath( leo, String.format( "%s%s", prefix, propertyName ), value );
        }
    }
}
