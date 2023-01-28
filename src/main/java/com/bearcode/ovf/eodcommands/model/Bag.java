package com.bearcode.ovf.eodcommands.model;

import com.bearcode.ovf.eodcommands.ExcelPortException;
import com.bearcode.ovf.model.eod.LocalOfficial;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by leonid on 12.07.16.
 * Class to represent collection of LocalOfficial ( contacts or AdditionalAddress )
 */
public class Bag extends ExportElement {
    /**
     * List of elements to be exported/imported for each object in collection
     */
    private List<ExportElement> elements;

    public Bag( int column, String propertyName, String headerLabel ) {
        super( column, propertyName, headerLabel );
        elements = new LinkedList<ExportElement>( );
        actualObjectsCount = 0;
    }

    public void addElement( int column, String propertyName, String headerLabel ) {
        ExportElement element = new ExportElement( column, propertyName, headerLabel, this );
        elements.add( element );
    }

    @Override
    public int defineActualColumn( int column ) {
        this.column = column;
        return column + elements.size() * actualObjectsCount;
    }

    @Override
    public void writeIntoRow( HSSFRow row, int offset, Object leo ) throws ExcelPortException {
        BeanWrapper beanWrapper = new BeanWrapperImpl( leo );
        Object bag = beanWrapper.getPropertyValue( propertyName );
        if ( bag instanceof Collection ) {
            int index = 0;
            for ( Object operand : (Collection)bag ) {
                int colNum = offset + column + index * elements.size();
                for ( ExportElement element : elements ) {
                    element.writeIntoRow( row, colNum, operand );
                }
                index++;
            }
        }
    }

    @Override
    public void writeHeader( HSSFRow header, int offset, String prefix ) {
        for ( int i = 0; i < actualObjectsCount; i++ ) {
            int colNum = offset + column + i * elements.size();
            for ( ExportElement element : elements ) {
                String headerLabel = String.format( "%s%s%d.", prefix, this.getHeaderLabel(), i + 1 );

                element.writeHeader( header, colNum, headerLabel );
            }
        }
    }

    @Override
    public short writeActualCountsInfo( HSSFRow row, short cellNum ) {
        HSSFCell cell = row.createCell( cellNum );
        cell.setCellValue( new HSSFRichTextString( propertyName ) );
        cell = row.createCell( (short) (cellNum + 1) );
        cell.setCellValue( (new Integer( actualObjectsCount )).doubleValue() );
        return (short) (cellNum + 2);
    }

    @Override
    public void parseFromRow( HSSFRow row, int offset, String prefix, Object leo ) throws ExcelPortException {
        BeanWrapper beanWrapper = new BeanWrapperImpl( leo );
        Object property = beanWrapper.getPropertyValue( propertyName );
        if ( property instanceof Collection ) {
            // bean
            for ( int index = 0; index < actualObjectsCount; index++ ) {
                int colNum = offset + column + index * elements.size();
                for ( ExportElement element : elements ) {
                    element.parseFromRow( row, colNum, String.format( "%s%s[%d].", prefix, propertyName, index ), leo );
                }
            }
        }
    }
}
