package com.bearcode.ovf.eodcommands;

import com.bearcode.commons.config.Environment;
import com.bearcode.ovf.eodcommands.model.Bag;
import com.bearcode.ovf.eodcommands.model.ExportElement;
import com.bearcode.ovf.model.common.County;
import com.bearcode.ovf.model.common.Municipality;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.*;

/**
 * Created by leonid on 12.07.16.
 *  Excel format descriptions. Define properties and columns where the properties should be written to
 */
public class ExcelPortDescriptor {
    public static final String ATTR_START_FROM_LINE = "start_from_line";
    public static final String ATTR_CONFIG_NAME = "local_official";
    public static final String ATTR_ELEMENT = ".cell";
    public static final String ATTR_COLUMN_NUMB = ".column";
    public static final String ATTR_COLUMN_LABEL = ".label";
    public static final String ATTR_OBJECT_PATH = ".path";
    public static final String ATTR_PATTERN = ".import_pattern";
    public static final String ATTR_WRITE_PATTERN = ".export_pattern";
    public static final String ATTR_BAG = ".bag";
    public static final String ATTR_REQUIRED_COLUMN = "required_column";
    public static final String ATTR_COLUMN_OFFSET = "column_offset";
    public static final String ATTR_VERSION_LINE = "version_line";
    public static final String ATTR_VERSION_COLUMN = "version_cell";
    public static final String ATTR_VERSION_LABEL_COLUMN = "version_label_cell";
    public static final String ATTR_FORMAT_VERSION_NUMBER = "format_version";

    private static final double BASIC_VERSION = 3.1;

    private Logger log = LoggerFactory.getLogger( ExcelPortDescriptor.class );

    /**
     * Elements which describes information for each property
     */
    private List<ExportElement> elements;

    private int startFromRow = 0;
    private int required = 0;
    private int offset = 0;
    private int versionLabelColumn = 0;
    private int versionColumn = 0;
    private int versionLine = 0;
    private double version = BASIC_VERSION;

    public ExcelPortDescriptor( Environment config ) {
        elements = new LinkedList<ExportElement>(  );
        buildDescriptor( config );
    }

    //------------------------------------------------------
    //Initialize methods : read information from config file
    //------------------------------------------------------

    /**
     * Read format information from configuration file
     * This information will be changed according to actual LocalOfficials
     * @param config Primary configuration
     */
    protected void buildDescriptor( Environment config ) {
        String versionNumber = config.getStringProperty( ATTR_FORMAT_VERSION_NUMBER, String.valueOf( BASIC_VERSION) );
        parseVersion( versionNumber );

        startFromRow = config.getIntProperty( ATTR_START_FROM_LINE, 3 );
        required = config.getIntProperty( ATTR_REQUIRED_COLUMN, 1 );
        versionLabelColumn = config.getIntProperty( ATTR_VERSION_LABEL_COLUMN, 1 );
        versionColumn = config.getIntProperty( ATTR_VERSION_COLUMN, 2 );
        versionLine = config.getIntProperty( ATTR_VERSION_LINE, 0 );
        offset = config.getIntProperty( ATTR_COLUMN_OFFSET, 0 );

        buildSimpleElements( config, ATTR_CONFIG_NAME, null );
        buildBags( config );
    }

    protected void buildSimpleElements( Environment config, String configPath, Bag bag ) {
        Object listOfElements = config.getProperty( configPath + ATTR_ELEMENT + ATTR_COLUMN_NUMB );
        if ( listOfElements instanceof Collection ) {
            for ( int cellNum = 0; cellNum < ((Collection) listOfElements).size(); cellNum++ ) {
                List columns = config.getList( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_NUMB );
                int c = 0;
                if ( columns != null ) {
                    if ( columns.size() == 1 ) {
                        String propertyPath = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_OBJECT_PATH, "" );
                        int column = config.getIntProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_NUMB, 0 );
                        String header = config.getStringProperty( configPath + ATTR_ELEMENT + "(" + cellNum + ")" + ATTR_COLUMN_LABEL, propertyPath );

                        if ( bag == null ) {
                            ExportElement element = new ExportElement( column, propertyPath, header );
                            elements.add( element );
                        } else {
                            bag.addElement( column, propertyPath, header );
                        }

                    } else if ( columns.size() > 1 ) {
                        //c = parseCompoundElement( row, leo, configPath, cellNum, propertyPath, startColumn );
                    }
                }
            }
        }
    }

    protected void buildBags( Environment config ) {
        Object listOfBags = config.getProperty( ATTR_CONFIG_NAME + ATTR_BAG + ATTR_COLUMN_NUMB );
        if ( listOfBags instanceof Collection ) {
            for ( int cellNum = 0; cellNum < ((Collection) listOfBags).size(); cellNum++ ) {
                String configPath = String.format( "%s%s(%d)", ATTR_CONFIG_NAME, ATTR_BAG, cellNum );

                buildBag( config, configPath );
            }
        } else {
            String configPath = String.format( "%s%s", ATTR_CONFIG_NAME, ATTR_BAG );
            buildBag( config, configPath );
        }

    }

    protected void buildBag( Environment config, String configPath ) {
        int column = config.getIntProperty( configPath + ATTR_COLUMN_NUMB, 0 );
        String path = config.getStringProperty( configPath + ATTR_OBJECT_PATH, "" );
        String header = config.getStringProperty( configPath + ATTR_COLUMN_LABEL, path );

        Bag bag = new Bag( column, path, header );
        elements.add( bag );

        buildSimpleElements( config, configPath, bag );
    }

    public void sortElements() {
        Collections.sort( elements, new Comparator<ExportElement>() {
            @Override
            public int compare( ExportElement o1, ExportElement o2 ) {
                return o1.getColumn() - o2.getColumn();
            }
        } );
    }

    public List<ExportElement> getElements() {
        return elements;
    }

    public int getVersionLabelColumn() {
        return versionLabelColumn;
    }

    public int getVersionColumn() {
        return versionColumn;
    }

    public int getVersionLine() {
        return versionLine;
    }

    public int getStartFromRow() {
        return startFromRow;
    }

    public int getRequired() {
        return required;
    }

    private void parseVersion( String versionNumber ) {
        try {
            Double formatVersion = Double.parseDouble( versionNumber );
            version = formatVersion.doubleValue();
        } catch (NumberFormatException e) {
            log.warn( "ExcelPort: can't parse version number, possible issue in config file." );
        }
    }

    public double getVersion() {
        return version;
    }

    //------------------------------------------------------
    //Preparation methods : count objects in collections of LocalOfficials
    //------------------------------------------------------

    public void countCollections( LocalOfficial leo ) {
        BeanWrapper beanWrapper = new BeanWrapperImpl( leo );
        for ( ExportElement element : elements ) {
            if ( element instanceof Bag ) {
                Object property = beanWrapper.getPropertyValue( element.getPropertyName() );
                if ( property instanceof Collection ) {
                    element.setActualObjectsCount( ((Collection)property).size() );
                }
            }
        }
        // define actual columns
        defineActualColumn();
    }

    /**
     * define actual column of each element. bag's column will be adjusted according to real collection size
     */
    private void defineActualColumn() {
        int lastColumn = offset + elements.size() > 0 ? elements.get( 0 ).getColumn() : 0;
        // elements must not be empty but for any case to prevent exception
        for ( ExportElement element : elements ) {
            lastColumn = element.defineActualColumn( lastColumn );
        }
    }

    //------------------------------------------------------
    //Write methods : write into Excel file
    //------------------------------------------------------

    public void writeExcelRow( HSSFRow row, LocalOfficial leo ) throws ExcelPortException {
        HSSFCell cell = row.createCell( (short) 0 );
        cell.setCellValue( new HSSFRichTextString( String.valueOf( leo.getId() ) ) );

        for ( ExportElement element : elements ) {
            element.writeIntoRow( row, leo );
        }
    }

    public void writeExcelHeader( HSSFRow header ) {
        HSSFCell cell = header.createCell( (short) 0 );
        cell.setCellValue( new HSSFRichTextString( "id" ) );

        for ( ExportElement element : elements ) {
            element.writeHeader( header );
        }
    }

    public void writeBagsInfo( HSSFRow row, short cellNum ) {
        for ( ExportElement element : elements ) {
            cellNum = element.writeActualCountsInfo( row, cellNum );
        }

    }

    //------------------------------------------------------
    //Read methods : read from Excel file
    //------------------------------------------------------

    public boolean parseVersionAndSizeInfo( HSSFRow header ) {
        short cellNum = (short) versionColumn;
        HSSFCell versionCell = header.getCell( cellNum );
        if( versionCell.getCellType() != HSSFCell.CELL_TYPE_NUMERIC ) {
            return false;
        }
        Double versionNumber = versionCell.getNumericCellValue();
        if ( versionNumber != version ) {
            return false;
        }
        for ( ExportElement element : elements ) {
            if ( element instanceof Bag ) {
                HSSFCell nameCell = header.getCell( ++cellNum );
                if ( nameCell.getCellType() == HSSFCell.CELL_TYPE_STRING &&
                        element.getPropertyName().equalsIgnoreCase( nameCell.getRichStringCellValue().toString().trim() )) {
                    HSSFCell sizeCell = header.getCell( ++cellNum );
                    if ( sizeCell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC ) {
                        element.setActualObjectsCount( Double.valueOf( sizeCell.getNumericCellValue() ).intValue() );
                    }
                    else {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
        }
        defineActualColumn();
        return true;
    }

    public LocalOfficial parseFormRow( HSSFRow row, State state ) throws ExcelPortException {
        // check if row is valid
        if ( row == null || row.getCell( (short) (required + offset) ) == null ) {
            return null;
        }

        VotingRegion region = new VotingRegion();
        region.setState( state );

        County county = new County();
        county.setState( state );
        region.setCounty( county );

        Municipality municipality = new Municipality();
        region.setMunicipality( municipality );
        municipality.setState( state );
        municipality.setCounty( county );

        LocalOfficial leo = new LocalOfficial();
        leo.setRegion( region );

        for ( ExportElement element : elements ) {
            element.parseFromRow( row, leo );
        }

        if ( StringUtils.isBlank( county.getName() )) {
            region.setCounty(null);
            municipality.setCounty(null);
        }
        if (StringUtils.isBlank(municipality.getName())) {
            region.setMunicipality(null);
        }
        return leo;
    }

}
