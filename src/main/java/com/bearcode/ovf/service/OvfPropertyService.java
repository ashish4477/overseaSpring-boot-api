package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.OvfPropertyDAO;
import com.bearcode.ovf.model.system.OvfProperty;
import com.bearcode.ovf.model.system.OvfPropertyNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @author leonid.
 */
@Service
public class OvfPropertyService {

    @Autowired
    private OvfPropertyDAO ovfPropertyDAO;

    private final HashMap<OvfPropertyNames,String> properties = new HashMap<OvfPropertyNames, String>();

    public OvfPropertyService() {
    }

    public void buildAllPropertiesMap() {
        List<OvfProperty> properties = ovfPropertyDAO.findAll();
        for ( OvfProperty property : properties ) {
            this.properties.put( property.getPropertyName(), property.getPropertyValue() );
        }
    }

    public List<OvfProperty> findAllProperties() {
        return ovfPropertyDAO.findAll();
    }

    public String getProperty( OvfPropertyNames propertyNames ) {
        if ( properties.isEmpty() ) {
            buildAllPropertiesMap();
        }
        String propertyValue = properties.get( propertyNames );
        return propertyValue != null ? propertyValue : propertyNames.getDefaultValue();
    }

    public Integer getPropertyAsInt( OvfPropertyNames propertyName ) {
        final String prop = this.getProperty(propertyName);
        try {
            return Integer.parseInt(prop);
        } catch (NumberFormatException e) {
            try {  //protection against foolish user
                return Integer.parseInt( propertyName.getDefaultValue() );
            } catch (NumberFormatException e2) {
                return null; // this is fatal
            }
        }
    }

    public Long getPropertyAsLong( OvfPropertyNames propertyName ) {
        final String prop = this.getProperty( propertyName );
        try {
            return Long.parseLong(prop);
        } catch (NumberFormatException e) {
            try {  //protection against foolish user
                return Long.parseLong( propertyName.getDefaultValue() );
            } catch (NumberFormatException e2) {
                return null; // this is fatal
            }
        }
    }


    public void saveProperty( OvfProperty property ) {
        ovfPropertyDAO.makePersistent( property );
        properties.put( property.getPropertyName(), property.getPropertyValue() );
    }
}
