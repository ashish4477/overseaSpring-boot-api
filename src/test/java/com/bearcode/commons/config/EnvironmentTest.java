/**
 * 
 */
package com.bearcode.commons.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.configuration.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * Test for {@link Environment}.
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 19, 2011
 */
public final class EnvironmentTest {

	/**
	 * the environment to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private Environment environment;

	/**
	 * Sets up to test the environment.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Before
	public final void setUpEnvironment() {
		setEnvironment(createEnvironment());
	}

	/**
	 * Tears down the environment after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@After
	public final void tearDownEnvironment() {
		setEnvironment(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#buildConfiguration()} for
	 * the case where there is a file with debug mode set.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testBuildConfiguration_debugMode() throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "debugMode",
					"true");

			getEnvironment().buildConfiguration();

			final Object oConfiguration = ReflectionTestUtils.getField(
					getEnvironment(), "configuration");
			assertTrue("A configuration is built",
					oConfiguration instanceof Configuration);
			final Configuration configuration = Configuration.class
					.cast(oConfiguration);
			assertEquals("The value is set in the configuration", value,
					configuration.getProperty("value"));
			assertTrue("Debugging mode is off",
					(Boolean) ReflectionTestUtils.getField(getEnvironment(),
							"debugMode"));

		} finally {
			configFile.delete();
		}
	}


	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#buildConfiguration()} for
	 * the case where there is an external file.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testBuildConfiguration_externalFile() throws IOException {
		final String value = "Value4";
		final File folder = new File("target/test-classes");
		final File externalConfigFile = File.createTempFile(
				"ExternalEnvironmentTest", ".xml", folder);
		try {
			getEnvironment().setExternalConfigFilename(
					externalConfigFile.getPath());
			buildConfigurationFile(externalConfigFile, "value", value);

			getEnvironment().buildConfiguration();

			final Object oConfiguration = ReflectionTestUtils.getField(
					getEnvironment(), "configuration");
			assertTrue("A configuration is built",
					oConfiguration instanceof Configuration);
			final Configuration configuration = Configuration.class
					.cast(oConfiguration);
			assertEquals("The value is set in the configuration", value,
					configuration.getProperty("value"));

		} finally {
			externalConfigFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#buildConfiguration()} for
	 * the case where there is a file.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testBuildConfiguration_file() throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			getEnvironment().buildConfiguration();

			final Object oConfiguration = ReflectionTestUtils.getField(
					getEnvironment(), "configuration");
			assertTrue("A configuration is built",
					oConfiguration instanceof Configuration);
			final Configuration configuration = Configuration.class
					.cast(oConfiguration);
			assertEquals("The value is set in the configuration", value,
					configuration.getProperty("value"));
			assertFalse("Debugging mode is off",
					(Boolean) ReflectionTestUtils.getField(getEnvironment(),
							"debugMode"));

		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#buildConfiguration()} for
	 * the case where there is a file and an alternative file.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testBuildConfiguration_mergeFiles() throws IOException {
		final String value = "Value3";
		final String anotherValue = "AnotherValue1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		final File altConfigFile = File.createTempFile("AltEnvironmentTest",
				".xml", folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);
			getEnvironment().setAltConfigFilename(altConfigFile.getName());
			buildConfigurationFile(altConfigFile, "anotherValue", anotherValue);

			getEnvironment().buildConfiguration();

			final Object oConfiguration = ReflectionTestUtils.getField(
					getEnvironment(), "configuration");
			assertTrue("A configuration is built",
					oConfiguration instanceof Configuration);
			final Configuration configuration = Configuration.class
					.cast(oConfiguration);
			assertEquals("The value is set in the configuration", value,
					configuration.getProperty("value"));
			assertEquals("Another value is set in the configuration",
					anotherValue, configuration.getProperty("anotherValue"));

		} finally {
			configFile.delete();
			altConfigFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#buildConfiguration()} for
	 * the case where the file does not exist.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testBuildConfiguration_noSuchFile() {
		getEnvironment().setConfigFilename("No such file");

		getEnvironment().buildConfiguration();

		assertNull("No configuration is built",
				ReflectionTestUtils.getField(getEnvironment(), "configuration"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#buildConfiguration()} for
	 * the case where nothing has been set.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testBuildConfiguration_nothingSet() {
		getEnvironment().buildConfiguration();

		assertNull("No configuration is built",
				ReflectionTestUtils.getField(getEnvironment(), "configuration"));
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getLongProperty(java.lang.String, java.lang.Long)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGeLongProperty_noConfiguration() {
		getEnvironment().getLongProperty("property", 0l);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getAltConfigFilename()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetAltConfigFilename() {
		final String actualAltConfigFilename = getEnvironment()
				.getAltConfigFilename();

		assertNull("No alternate configuration filename is set",
				actualAltConfigFilename);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getBooleanProperty(java.lang.String, java.lang.Boolean)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetBooleanProperty() throws IOException {
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "property",
					Boolean.TRUE.toString());

			final Boolean actualBoolean = getEnvironment().getBooleanProperty(
					"property", false);

			assertTrue("The boolean property is set", actualBoolean);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getBooleanProperty(java.lang.String, java.lang.Boolean)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetBooleanProperty_noConfiguration() {
		getEnvironment().getBooleanProperty("property", false);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getBooleanProperty(java.lang.String, java.lang.Boolean)}
	 * for the case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetBooleanProperty_notInConfiguration()
			throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final Boolean actualBoolean = getEnvironment().getBooleanProperty(
					"property", false);

			assertFalse("The boolean property is not set", actualBoolean);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getStringProperty(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCompoundStringProperty() throws IOException {
		final String value = "Value1";
		final String stringValue = "String Value10";
		final String stringValue2 = "String Value 20";
		final List<String> compoundList = Arrays.asList(stringValue,
				stringValue2);
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property",
					compoundList);

			final String actualCompoundString = getEnvironment()
					.getCompoundStringProperty("property.entry", null);

			final String expectedCompoundString = stringValue + ", "
					+ stringValue2;
			assertEquals("The compound string property is set",
					expectedCompoundString, actualCompoundString);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getCompoundStringProperty(java.lang.String, java.lang.String)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetCompoundStringProperty_noConfiguration() {
		getEnvironment().getCompoundStringProperty("property",
				"compound, string");
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getCompoundStringProperty(String, String)}
	 * for the case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCompoundStringProperty_notInConfiguration()
			throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final String actualString = getEnvironment()
					.getCompoundStringProperty("property", null);

			assertNull("The compound string property is not set", actualString);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getStringProperty(java.lang.String, java.lang.String)}
	 * for the case where the property is a simple string.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCompoundStringProperty_simpleString()
			throws IOException {
		final String value = "Value1";
		final String stringValue = "String Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property",
					stringValue.toString());

			final String actualCompoundString = getEnvironment()
					.getCompoundStringProperty("property", null);

			assertEquals("The compound string property is set", stringValue,
					actualCompoundString);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getConfigFilename()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetConfigFilename() {
		final String actualConfigFilename = getEnvironment()
				.getConfigFilename();

		assertNull("No configuration filename is set", actualConfigFilename);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getExternalConfigFilename()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetExternalConfigFilename() {
		final String actualExternalConfigFilename = getEnvironment()
				.getExternalConfigFilename();

		assertNull("No external configuration filename is set",
				actualExternalConfigFilename);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getIntProperty(java.lang.String, java.lang.Integer)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetIntProperty() throws IOException {
		final String value = "Value1";
		final Integer intValue = 1;
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property",
					intValue.toString());

			final Integer actualInt = getEnvironment().getIntProperty(
					"property", 0);

			assertEquals("The integer property is set", intValue, actualInt);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getIntProperty(java.lang.String, java.lang.Integer)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetIntProperty_noConfiguration() {
		getEnvironment().getIntProperty("property", 0);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getIntProperty(java.lang.String, java.lang.Integer)}
	 * for the case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetIntProperty_notInConfiguration()
			throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final Integer actualInt = getEnvironment().getIntProperty(
					"property", null);

			assertNull("The integer property is not set", actualInt);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getList(String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetList() throws IOException {
		final String value = "Value1";
		final List list = Arrays.asList(value);
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property", list);

			final List actualList = getEnvironment().getList("property.entry");

			assertEquals("The list is set", list, actualList);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getList(java.lang.String)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetList_noConfiguration() {
		getEnvironment().getList("property.entry");
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getList(String)} for the
	 * case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetList_notInConfiguration() throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final List actualList = getEnvironment().getList("property.entry");

			assertTrue("The list is empty", actualList.isEmpty());
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getLongProperty(java.lang.String, java.lang.Long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetLongProperty() throws IOException {
		final String value = "Value1";
		final Long longValue = 2l;
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property",
					longValue.toString());

			final Long actualLong = getEnvironment().getLongProperty(
					"property", 0l);

			assertEquals("The long property is set", longValue, actualLong);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getLongProperty(java.lang.String, java.lang.Long)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetLongProperty_noConfiguration() {
		getEnvironment().getLongProperty("property", 0l);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getLongProperty(java.lang.String, java.lang.Long)}
	 * for the case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetLongProperty_notInConfiguration()
			throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final Long actualLong = getEnvironment().getLongProperty(
					"property", null);

			assertNull("The long property is not set", actualLong);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getProperty(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetProperty() throws IOException {
		final String value = "Value1";
		final String objectValue = "Object Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property",
					objectValue.toString());

			final Object actualObject = getEnvironment()
					.getProperty("property");

			assertEquals("The property is set", objectValue, actualObject);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getProperty(java.lang.String)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetProperty_noConfiguration() {
		getEnvironment().getProperty("property");
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getProperty(java.lang.String)}
	 * for the case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetProperty_notInConfiguration() throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final Object actualObject = getEnvironment()
					.getProperty("property");

			assertNull("The property is not set", actualObject);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getStringProperty(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetStringProperty() throws IOException {
		final String value = "Value1";
		final String stringValue = "String Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value, "property",
					stringValue.toString());

			final String actualString = getEnvironment().getStringProperty(
					"property", null);

			assertEquals("The string property is set", stringValue,
					actualString);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getStringProperty(java.lang.String, java.lang.String)}
	 * for the case where there is no configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test(expected = NullPointerException.class)
	public final void testGetStringProperty_noConfiguration() {
		getEnvironment().getStringProperty("property", "string");
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#getStringProperty(java.lang.String, java.lang.String)}
	 * for the case where the property is not in the configuration.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem creating the configuration file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetStringProperty_notInConfiguration()
			throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);

			final String actualString = getEnvironment().getStringProperty(
					"property", null);

			assertNull("The string property is not set", actualString);
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for {@link com.bearcode.commons.config.Environment#reset()}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws IOException
	 *             if there is a problem building the configuration.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testReset() throws IOException {
		final String value = "Value1";
		final File folder = new File("target/test-classes");
		final File configFile = File.createTempFile("EnvironmentTest", ".xml",
				folder);
		try {
			getEnvironment().setConfigFilename(configFile.getName());
			buildConfigurationFile(configFile, "value", value);
			getEnvironment().buildConfiguration();

			getEnvironment().reset();

			assertNull("There is no configuration",
					ReflectionTestUtils.getField(getEnvironment(),
							"configuration"));
		} finally {
			configFile.delete();
		}
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#setAltConfigFilename(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetAltConfigFilename() {
		final String altConfigFilename = "Alt Config Filename";

		getEnvironment().setAltConfigFilename(altConfigFilename);

		assertEquals("The alternative configuration filename is set",
				altConfigFilename, getEnvironment().getAltConfigFilename());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#setConfigFilename(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetConfigFilename() {
		final String configFilename = "Configuration Filename";

		getEnvironment().setConfigFilename(configFilename);

		assertEquals("The configuration filename is set", configFilename,
				getEnvironment().getConfigFilename());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.config.Environment#setExternalConfigFilename(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetExternalConfigFilename() {
		final String configFilename = "External Config Filename";

		getEnvironment().setExternalConfigFilename(configFilename);

		assertEquals("The external configuration filename is set",
				configFilename, getEnvironment().getExternalConfigFilename());
	}

	/**
	 * Builds a configuration file with the specified value.
	 * 
	 * @author IanBrown
	 * @param configFile
	 *            the configuration file.
	 * @param nameValues
	 *            the names and values to put in the file.
	 * @throws IOException
	 *             if there is a problem building the file.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("unchecked")
	private void buildConfigurationFile(final File configFile,
			final Object... nameValues) throws IOException {
		final PrintWriter pw = new PrintWriter(new FileWriter(configFile));
		pw.println("<simple>");
		for (int idx = 0; idx < nameValues.length; idx += 2) {
			if (nameValues[idx + 1] instanceof List) {
				pw.println("<" + nameValues[idx] + ">");
				for (final String entry : (List<String>) nameValues[idx + 1]) {
					pw.println("<entry>" + entry + "</entry>");
				}
				pw.println("</" + nameValues[idx] + ">");
			} else {
				pw.println(String.format("<%1$s>%2$s</%1$s>", nameValues[idx],
						nameValues[idx + 1]));
			}
		}
		pw.println("</simple>");
		pw.close();
	}

	/**
	 * Creates an environment object.
	 * 
	 * @author IanBrown
	 * @return the environment.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private Environment createEnvironment() {
		return new Environment();
	}

	/**
	 * Gets the environment.
	 * 
	 * @author IanBrown
	 * @return the environment.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private Environment getEnvironment() {
		return environment;
	}

	/**
	 * Sets the environment.
	 * 
	 * @author IanBrown
	 * @param environment
	 *            the environment to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setEnvironment(final Environment environment) {
		this.environment = environment;
	}

}
