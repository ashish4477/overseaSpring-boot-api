package com.bearcode.commons.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bearcode.commons.util.config.XMLConfigReader;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan
 * Date: 21.11.2005
 * Time: 22:02:58
 * Changed: Leo
 */
public class Environment {

    protected static Logger log = LoggerFactory.getLogger(Environment.class);

    private static final String DEBUG_MODE = "debugMode";

    private Configuration configuration = null;
    private static boolean debugMode = false;

    private String configFilename = null;
    private String altConfigFilename = null;
    private String extConfigFilename = null;

    public Environment() {
    }
    
    public Environment(final String configFilename) {
		this.configFilename = configFilename;
	}

	protected void buildConfiguration(){

        if (configuration == null || debugMode) {
            InputStream inp = null;
            try {
                synchronized (Environment.class) {
                    if(configFilename != null){
                    	inp = Environment.class.getClassLoader().getResourceAsStream(configFilename);
                    }
                    if(extConfigFilename != null){
                    	try {
                    		InputStream old = inp;
    						inp = new FileInputStream(new File(extConfigFilename));
    						tryClose(old);
    					} catch (FileNotFoundException e) {
    						log.info("FileNotFoundException reading config file "+extConfigFilename+":"+e);
    					} 
                	} 
                    configuration = XMLConfigReader.getConfiguration(inp);

                    // alternative configuration
                    if ( altConfigFilename != null && altConfigFilename.length() > 0 ) {
                		InputStream old = inp;
                        inp = Environment.class.getClassLoader().getResourceAsStream(altConfigFilename);
                        if (inp != null) {
    						tryClose(old);
    						
                            Configuration alternativeConfiguration = XMLConfigReader.getConfiguration(inp);

                            if (alternativeConfiguration != null) {
                                if (configuration == null) {
                                    configuration = alternativeConfiguration;
                                } else {
                                    // merge configurations
                                    for (Iterator iterator = alternativeConfiguration.getKeys(); iterator.hasNext();) {
                                        String key =  (String) iterator.next();
                                        configuration.setProperty(key, alternativeConfiguration.getProperty(key));
                                    }
                                }
                            }
                        }
                    }

                    if (configuration != null) {
                        debugMode = configuration.getBoolean(DEBUG_MODE, false);
                    }
                }
            } catch (ConfigurationException e) {
                log.error("", e);
            } finally {
				tryClose(inp);
            }
        }
    }
	
	public void tryClose(final InputStream is) {
		if (is == null) return;
		try {
			is.close();
		} catch (Exception e) { /* do nothing */ }
	}

    public String getStringProperty(String name, String defaultValue) {
        buildConfiguration();
        return configuration.getString(name, defaultValue);
    }

    public Long getLongProperty(String name, Long defaultValue) {
        buildConfiguration();
        return configuration.getLong(name, defaultValue);
    }

    public Integer getIntProperty(String name, Integer defaultValue) {
        buildConfiguration();
        return configuration.getInteger(name, defaultValue);
    }

    public Boolean getBooleanProperty(String name, Boolean defaultValue) {
        buildConfiguration();
        return configuration.getBoolean(name, defaultValue);
    }

    public Object getProperty( String name ) {
        buildConfiguration();
        return configuration.getProperty( name );
    }

    public List getList( String name ) {
        buildConfiguration();
        return configuration.getList( name );
    }


    public String getConfigFilename() {
        return configFilename;
    }

    public void setConfigFilename(String configFilename) {
        this.configFilename = configFilename;
    }

    public String getExternalConfigFilename() {
        return extConfigFilename;
    }

    public void setExternalConfigFilename(String configFilename) {
        this.extConfigFilename = configFilename;
    }

    public String getAltConfigFilename() {
        return altConfigFilename;
    }

    public void setAltConfigFilename(String altConfigFilename) {
        this.altConfigFilename = altConfigFilename;
    }

    public String getCompoundStringProperty( String name, String defaultValue ) {
        buildConfiguration();
        Object property = getProperty( name );
        if ( property != null ) {
            if ( property instanceof String ) {
                if ( ((String)property).length() > 0 ) {
                    return (String)property;
                }
            }
            else if ( property instanceof List ) {
                StringBuffer buffer = new StringBuffer();
                for ( Object bit : (List)property ) {
                    if ( buffer.length() > 0 ) buffer.append(", ");
                    buffer.append( bit.toString() );
                }
                return buffer.toString();
            }
        }
        return defaultValue;
    }

    public void reset() {
        configuration = null;
    }
}
