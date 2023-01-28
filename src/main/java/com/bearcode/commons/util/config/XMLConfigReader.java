//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.bearcode.commons.util.config;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfigurationXMLReader;
import org.apache.commons.configuration.XMLConfiguration;

public class XMLConfigReader {
    public XMLConfigReader() {
    }

    public static Configuration getConfiguration(File source) throws ConfigurationException {
        if (source == null) {
            throw new XMLConfigReaderException();
        } else {
            XMLConfiguration configuration = new XMLConfiguration();
            configuration.load(source);
            HierarchicalConfigurationXMLReader xmlReader = new HierarchicalConfigurationXMLReader();
            xmlReader.setConfiguration(configuration);
            return xmlReader.getParsedConfiguration();
        }
    }

    public static Configuration getConfiguration(String source) throws ConfigurationException {
        if (source == null) {
            throw new XMLConfigReaderException();
        } else {
            XMLConfiguration configuration = new XMLConfiguration();
            configuration.load(source);
            HierarchicalConfigurationXMLReader xmlReader = new HierarchicalConfigurationXMLReader();
            xmlReader.setConfiguration(configuration);
            return xmlReader.getParsedConfiguration();
        }
    }

    public static Configuration getConfiguration(URL source) throws ConfigurationException {
        if (source == null) {
            throw new XMLConfigReaderException();
        } else {
            XMLConfiguration configuration = new XMLConfiguration();
            configuration.load(source);
            HierarchicalConfigurationXMLReader xmlReader = new HierarchicalConfigurationXMLReader();
            xmlReader.setConfiguration(configuration);
            return xmlReader.getParsedConfiguration();
        }
    }

    public static Configuration getConfiguration(InputStream source) throws ConfigurationException {
        if (source == null) {
            throw new XMLConfigReaderException();
        } else {
            XMLConfiguration configuration = new XMLConfiguration();
            configuration.load(source);
            HierarchicalConfigurationXMLReader xmlReader = new HierarchicalConfigurationXMLReader();
            xmlReader.setConfiguration(configuration);
            return xmlReader.getParsedConfiguration();
        }
    }

    public static Configuration getConfiguration(InputStream source, String encoding) throws ConfigurationException {
        if (source != null && encoding != null) {
            XMLConfiguration configuration = new XMLConfiguration();
            configuration.load(source);
            HierarchicalConfigurationXMLReader xmlReader = new HierarchicalConfigurationXMLReader();
            xmlReader.setConfiguration(configuration);
            return xmlReader.getParsedConfiguration();
        } else {
            throw new XMLConfigReaderException();
        }
    }

    public static Configuration getConfiguration(Reader source) throws ConfigurationException {
        if (source == null) {
            throw new XMLConfigReaderException();
        } else {
            XMLConfiguration configuration = new XMLConfiguration();
            configuration.load(source);
            HierarchicalConfigurationXMLReader xmlReader = new HierarchicalConfigurationXMLReader();
            xmlReader.setConfiguration(configuration);
            return xmlReader.getParsedConfiguration();
        }
    }
}
