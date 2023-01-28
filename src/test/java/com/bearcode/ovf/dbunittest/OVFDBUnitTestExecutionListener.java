package com.bearcode.ovf.dbunittest;

import com.googlecode.flyway.core.Flyway;
import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.database.statement.IBatchStatement;
import org.dbunit.database.statement.IStatementFactory;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlMetadataHandler;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

import javax.sql.DataSource;
import java.io.*;
import java.sql.*;

/**
 * Implementation of {@link TestExecutionListener} for setting up DBUnit tests.
 * 
 * @author IanBrown
 * 
 * @since Dec 15, 2011
 * @version May 10, 2013
 */
public final class OVFDBUnitTestExecutionListener implements
        TestExecutionListener {

	/**
	 * the logger for the class.
	 * 
	 * @author IanBrown
	 * @since May 9, 2013
	 * @version May 9, 2013
	 */
	// private final static Logger LOGGER =
	// LoggerFactory.getLogger(OVFDBUnitTestExecutionListener.class);

	/**
	 * the format string used to create the database.
	 * 
	 * @author IanBrown
	 * @since Dec 13, 2011
	 * @version Dec 13, 2011
	 */
	private static final String CREATE_DATABASE_SQL_FORMAT = "CREATE DATABASE %s DEFAULT CHARACTER SET utf8;";

	/**
	 * the format string used to drop the database.
	 * 
	 * @author IanBrown
	 * @since Dec 13, 2011
	 * @version Dec 13, 2011
	 */
	private static final String DROP_DATABASE_SQL_FORMAT = "DROP DATABASE IF EXISTS %s;";

	/** {@inheritDoc} */
	@Override
	public final void afterTestClass(final TestContext testContext)
	        throws Exception {
	}

	/** {@inheritDoc} */
	@Override
	public final void afterTestMethod(final TestContext testContext)
	        throws Exception {
	}

	/** {@inheritDoc} */
	@Override
	public final void beforeTestClass(final TestContext testContext)
	        throws Exception {
		final OVFDBUnitDatabaseName ovfDatabaseName = testContext
		        .getTestClass().getAnnotation(OVFDBUnitDatabaseName.class);
		final String databaseName = ovfDatabaseName.databaseName();
		setUpDatabase(testContext, databaseName);
	}

	/** {@inheritDoc} */
	@Override
	public final void beforeTestMethod(final TestContext testContext)
	        throws Exception {
		final String databaseName = acquireDatabaseName(testContext);
		final IDatabaseConnection connection = acquireDatabaseConnection(
		        testContext, databaseName);

		if (testContext.getTestMethod().getAnnotation(OVFDBUnitUseData.class) != null) {
			loadSQL(connection, "database/data/initial_data.sql");
            loadSQL(connection, "database/data/eod.sql");
            loadSQL(connection, "database/data/officers.sql");
			loadSQL(connection, "database/data/rava_flow_dump.sql");
			loadSQL(connection, "database/data/standard_reports.sql");
		}

		final OVFDBUnitDataSet dbunitDataSet = testContext.getTestMethod()
		        .getAnnotation(OVFDBUnitDataSet.class);
		if (dbunitDataSet != null) {
			loadDataSets(testContext, connection, dbunitDataSet);
		}
	}

	/** {@inheritDoc} */
	@Override
	public final void prepareTestInstance(final TestContext testContext)
	        throws Exception {
	}

	/**
	 * Acquires a DBUnit database connection from the test context.
	 * 
	 * @author IanBrown
	 * @param testContext
	 *            the test context.
	 * @param databaseName
	 *            the name of the database (or schema).
	 * @return the database connection.
	 * @throws DatabaseUnitException
	 *             if there is a problem acquiring the database connection.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	private IDatabaseConnection acquireDatabaseConnection(
	        final TestContext testContext, final String databaseName)
	        throws DatabaseUnitException {
		final DataSource dataSource = (DataSource) testContext
		        .getApplicationContext().getBean("dataSource");
		final Connection sqlConnection = DataSourceUtils
		        .getConnection(dataSource);
		final IDatabaseConnection connection = new DatabaseConnection(
		        sqlConnection, databaseName);
		configureConnectionForMySQL(connection);
		return connection;
	}

	/**
	 * Acquires the database name from the test context.
	 * 
	 * @author IanBrown
	 * @param testContext
	 *            the test context.
	 * @return the database name.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	private String acquireDatabaseName(final TestContext testContext) {
		final OVFDBUnitDatabaseName ovfDatabaseName = testContext
		        .getTestClass().getAnnotation(OVFDBUnitDatabaseName.class);
		final String databaseName = ovfDatabaseName.databaseName();
		return databaseName;
	}

	/**
	 * Configures the input connection for use with MySQL.
	 * 
	 * @author IanBrown
	 * @param connection
	 *            the connection.
	 * @since Dec 15, 2011
	 * @version Dec 15, 2011
	 */
	private void configureConnectionForMySQL(
	        final IDatabaseConnection connection) {
		final DatabaseConfig config = connection.getConfig();
		config.setProperty(DatabaseConfig.PROPERTY_METADATA_HANDLER,
		        new MySqlMetadataHandler());
		config.setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "`?`");
	}

	/**
	 * Creates the database.
	 * 
	 * @author IanBrown
	 * @param connection
	 *            the connection to the database.
	 * @param schemaName
	 *            the name of the schema.
	 * @throws Exception
	 *             if there is a problem creating the database.
	 * @since Dec 13, 2011
	 * @version Dec 13, 2011
	 */
	private void createDatabase(final IDatabaseConnection connection,
	        final String schemaName) throws Exception {
		executeSQL(connection,
		        String.format(CREATE_DATABASE_SQL_FORMAT, schemaName));
	}

	/**
	 * Creates the schema in the database tester.
	 * 
	 * @author IanBrown
	 * @param connection
	 *            the database connection to use.
	 * @param databaseName
	 *            the name of the databaseName.
	 * @throws Exception
	 *             if there is a problem creating the schema.
	 * @since Dec 13, 2011
	 * @version Dec 14, 2011
	 */
	private void createSchema(final TestContext testContext,
	        final IDatabaseConnection connection, final String databaseName)
	        throws Exception {
		executeSQL(connection, "USE " + databaseName);

		final DataSource dataSource = (DataSource) testContext
		        .getApplicationContext().getBean("dataSource");
		final Flyway flyway = new Flyway();
		flyway.setDataSource(dataSource);
		flyway.setBaseDir("patches");
		flyway.setBasePackage("com.bearcode.patches");
		flyway.migrate();

		// Needed to allow DBUnit to update the table.
		executeSQL(connection,
		        "ALTER TABLE users_admin_faces ADD PRIMARY KEY (user_id, face_id);");
	}

	/**
	 * Drops the database using the database tester.
	 * 
	 * @author IanBrown
	 * @param connection
	 *            the connection to the database.
	 * @param schemaName
	 *            the name of the schema.
	 * @throws Exception
	 *             if there is a problem dropping the database.
	 * @since Dec 13, 2011
	 * @version Dec 13, 2011
	 */
	private void dropDatabase(final IDatabaseConnection connection,
	        final String schemaName) throws Exception {
		executeSQL(connection,
		        String.format(DROP_DATABASE_SQL_FORMAT, schemaName));
	}

	/**
	 * Dumps the database to a file for loading in the future.
	 * 
	 * @author Ian Brown
	 * @param testContext
	 * @param connection
	 *            the connection.
	 * @param databaseName
	 *            the name of the database.
	 * @throws SQLException
	 *             if there is a problem communicating with the database.
	 * @throws IOException
	 *             if there is a problem with I/O.
	 * @throws InterruptedException
	 *             if the dump is interrupted.
	 * @since May 9, 2013
	 * @version May 9, 2013
	 */
	private void dumpDatabase(final TestContext testContext,
	        final IDatabaseConnection connection, final String databaseName)
	        throws SQLException, IOException, InterruptedException {
		final File dumpFile = new File("target", databaseName + ".sql");
		dumpFile.delete();

		final Connection sqlConnection = connection.getConnection();
		final DatabaseMetaData metaData = sqlConnection.getMetaData();
		final String url = metaData.getURL();
		final Driver driver = DriverManager.getDriver(url);

		final DriverPropertyInfo[] properties = driver.getPropertyInfo(url,
		        null);
		String host = null;
		String port = null;
		for (final DriverPropertyInfo property : properties) {
			if ("HOST".equals(property.name)) {
				host = property.value;
			} else if ("PORT".equals(property.name)) {
				port = property.value;
			}
		}

		final String command = "mysqldump --host=" + host + " --port=" + port
		        + " --user=" + usernameWithoutHost(metaData.getUserName())
		        + " --password=" + usernameWithoutHost(metaData.getUserName())
		        + " --compact" + " --extended-insert" + " --quote-names"
		        + " -r " + dumpFile.getAbsolutePath() + " " + databaseName;
		final Runtime runtime = Runtime.getRuntime();
		final Process process = runtime.exec(command);
		final int exitStatus = process.waitFor();
		if (exitStatus != 0) {
			dumpFile.delete();
		}
	}

	/**
	 * Executes a SQL statement on the database.
	 * 
	 * @author IanBrown
	 * @param connection
	 *            the database connection.
	 * @param sql
	 *            the SQL to execute.
	 * @throws Exception
	 *             if there is a problem executing the SQL.
	 * @since Dec 13, 2011
	 * @version Dec 13, 2011
	 */
	private void executeSQL(final IDatabaseConnection connection,
	        final String sql) throws Exception {
		final DatabaseConfig databaseConfig = connection.getConfig();
		final IStatementFactory statementFactory = (IStatementFactory) databaseConfig
		        .getProperty(DatabaseConfig.PROPERTY_STATEMENT_FACTORY);
		final IBatchStatement statement = statementFactory
		        .createBatchStatement(connection);
		statement.addBatch(sql);
		statement.executeBatch();
	}

	/**
	 * Loads the database from a file if it exists and is newer than any of the
	 * patches.
	 * 
	 * @author Ian Brown
	 * @param testContext
	 * @param connection
	 *            the connection.
	 * @param databaseName
	 *            the name of the database.
	 * @throws SQLException
	 *             if there is a problem communicating with the database.
	 * @throws IOException
	 *             if there is a problem with I/O.
	 * @throws InterruptedException
	 *             if the load is interrupted.
	 * @since May 9, 2013
	 * @version May 10, 2013
	 */
	private boolean loadDatabase(final TestContext testContext,
	        final IDatabaseConnection connection, final String databaseName)
	        throws SQLException, IOException, InterruptedException {
		final File loadFile = new File("target", databaseName + ".sql");
		if (loadFile.canRead() && loadFileIsNewerThanPatches(loadFile)) {
			final Connection sqlConnection = connection.getConnection();
			final DatabaseMetaData metaData = sqlConnection.getMetaData();
			final String url = metaData.getURL();
			final Driver driver = DriverManager.getDriver(url);

			final DriverPropertyInfo[] properties = driver.getPropertyInfo(url,
			        null);
			String host = null;
			String port = null;
			for (final DriverPropertyInfo property : properties) {
				if ("HOST".equals(property.name)) {
					host = property.value;
				} else if ("PORT".equals(property.name)) {
					port = property.value;
				}
			}

			final String command = "mysql --host=" + host + " --port=" + port
			        + " --user=" + usernameWithoutHost(metaData.getUserName())
			        + " --password="
			        + usernameWithoutHost(metaData.getUserName())
			        + " --silent " + databaseName
			        + " --execute \"SET FOREIGN_KEY_CHECKS=0; source "
			        + loadFile.getAbsolutePath().replace("\\", "/")
			        + "; SET FOREIGN_KEY_CHECKS=1;\" ";
			final Runtime runtime = Runtime.getRuntime();
			final Process process = runtime.exec(command);
			final int exitStatus = process.waitFor();
			if (exitStatus == 0) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Loads the DBUnit data sets.
	 * 
	 * @author IanBrown
	 * @param testContext
	 *            the test context.
	 * @param connection
	 *            the database connection.
	 * @param dbunitDataSet
	 *            the DBUnit data set specification.
	 * @throws DataSetException
	 *             if there is a problem with a data set.
	 * @throws IOException
	 *             if there is a problem reading a data set file.
	 * @throws DatabaseUnitException
	 *             if there is a problem with DBUnit.
	 * @throws SQLException
	 *             if there is a problem communicating with the database.
	 * @since Dec 15, 2011
	 * @version Dec 16, 2011
	 */
	private void loadDataSets(final TestContext testContext,
	        final IDatabaseConnection connection,
	        final OVFDBUnitDataSet dbunitDataSet) throws DataSetException,
	        IOException, DatabaseUnitException, SQLException {
		final String[] dataSetList = dbunitDataSet.dataSetList();
		DatabaseOperation operation = DatabaseOperation.CLEAN_INSERT;
		for (final String dataSetPath : dataSetList) {
			final IDataSet dataSet = loadFlatXMLDataSet(testContext,
			        dataSetPath);
			operation.execute(connection, dataSet);
			operation = DatabaseOperation.REFRESH;
		}
	}

	/**
	 * Determines if the input load file is newer than the database patch files.
	 * 
	 * @author Ian Brown
	 * @param loadFile
	 *            the load file.
	 * @return <code>true</code> if it is newer, <code>false</code> if it is
	 *         older.
	 * @since May 10, 2013
	 * @version May 10, 2013
	 */
	private boolean loadFileIsNewerThanPatches(File loadFile) {
		final long loadFileDate = loadFile.lastModified();
		final File patchFolder = new File("database/src/main/resources/patches");
		for (final File patchFile : patchFolder.listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".sql");
			}

		})) {
			final long patchFileDate = patchFile.lastModified();
			if (patchFileDate > loadFileDate) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Loads the data set defined by the path as a flat XML file.
	 * 
	 * @author IanBrown
	 * @param testContext
	 *            the test context.
	 * @param dataSetPath
	 *            the path to the data set.
	 * @return the loaded data set.
	 * @throws IOException
	 *             if there is a problem reading the data set path.
	 * @throws DataSetException
	 *             if there is a problem with the data set.
	 * @since Dec 13, 2011
	 * @version May 17, 2012
	 */
	private IDataSet loadFlatXMLDataSet(final TestContext testContext,
	        final String dataSetPath) throws DataSetException, IOException {
		final Resource resource = testContext.getApplicationContext()
		        .getResource(dataSetPath);
		return new FlatXmlDataSetBuilder().setColumnSensing(true).build(
		        resource.getInputStream());
	}

	/**
	 * Loads the SQL from the input path.
	 * 
	 * @author IanBrown
	 * @param connection
	 *            the connection to the database.
	 * @param path
	 *            the path.
	 * @throws Exception
	 *             if there is a problem loading the SQL.
	 * @since Dec 13, 2011
	 * @version Apr 13, 2012
	 */
	private void loadSQL(final IDatabaseConnection connection, final String path)
	        throws Exception {
		final File file = new File(path);
		final LineNumberReader lnr = new LineNumberReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		String line;

		while ((line = lnr.readLine()) != null) {
			line = line.trim();
			if (line.isEmpty() || line.startsWith("--")
			        || line.startsWith("use ")) {
				continue;
			}
			sb.append(line).append("\n");
			if (line.endsWith(";")) {
				try {
					executeSQL(connection, sb.toString());
				} catch (final Exception e) {
					// ignore it
				}
				sb = new StringBuilder();
			}
		}

		lnr.close();
	}

	/**
	 * Sets up the database, including the table schema.
	 * 
	 * @author IanBrown
	 * @param testContext
	 *            the test context.
	 * @param databaseName
	 *            the name of the database (schema).
	 * @return the connection to the database.
	 * @throws Exception
	 *             if there is a problem setting up the database.
	 * @since Dec 14, 2011
	 * @version Dec 14, 2011
	 */
	private IDatabaseConnection setUpDatabase(final TestContext testContext,
	        final String databaseName) throws Exception {
		final IDatabaseConnection connection = acquireDatabaseConnection(
		        testContext, "");
		testContext.setAttribute("connection", connection);
		dropDatabase(connection, databaseName);
		createDatabase(connection, databaseName);
		if (!loadDatabase(testContext, connection, databaseName)) {
			createSchema(testContext, connection, databaseName);
			dumpDatabase(testContext, connection, databaseName);
		}
		return connection;
	}

	/**
	 * Shows the contents of the specified table (for debugging purposes).
	 * 
	 * @author IanBrown
	 * @param testContext
	 *            the test context.
	 * @param tableName
	 *            the name of the table.
	 * @throws SQLException
	 *             if there is a problem reading the table.
	 * @since Jan 31, 2012
	 * @version Jan 31, 2012
	 */
	@SuppressWarnings("unused")
	private void showTableContents(final TestContext testContext,
	        final String tableName) throws SQLException {
		final DataSource dataSource = (DataSource) testContext
		        .getApplicationContext().getBean("dataSource");
		final Connection sqlConnection = DataSourceUtils
		        .getConnection(dataSource);
		final Statement statement = sqlConnection.createStatement();
		final ResultSet resultSet = statement.executeQuery("SELECT * FROM "
		        + tableName);
		showTableResults(tableName, resultSet);
		statement.close();
	}

	/**
	 * Shows the results for the table.
	 * 
	 * @author IanBrown
	 * @param tableName
	 *            the name of the table.
	 * @param resultSet
	 *            the result set.
	 * @throws SQLException
	 *             if there is a problem with the SQL.
	 * @since Jan 31, 2012
	 * @version Jan 31, 2012
	 */
	private void showTableResults(final String tableName,
	        final ResultSet resultSet) throws SQLException {
		final ResultSetMetaData metaData = resultSet.getMetaData();
		System.err.println(tableName);
		final int numColumns = metaData.getColumnCount();
		String prefix = "";
		for (int columnIdx = 0; columnIdx < numColumns; ++columnIdx) {
			System.err.print(prefix + metaData.getColumnName(columnIdx + 1));
			prefix = "\t";
		}
		System.err.println("");
		prefix = "";
		for (int columnIdx = 0; columnIdx < numColumns; ++columnIdx) {
			System.err.print(prefix
			        + metaData.getColumnClassName(columnIdx + 1));
			prefix = "\t";
		}
		System.err.println("");
		prefix = "";
		for (int columnIdx = 0; columnIdx < numColumns; ++columnIdx) {
			System.err.print(prefix + metaData.getPrecision(columnIdx + 1));
			prefix = "\t";
		}
		System.err.println("");
		while (resultSet.next()) {
			prefix = "";
			for (int columnIdx = 0; columnIdx < numColumns; ++columnIdx) {
				System.err.print(prefix + resultSet.getObject(columnIdx + 1));
				prefix = "\t";
			}
			System.err.println("");
		}
		System.err.println("");
		resultSet.close();
	}

	/**
	 * Returns the username without the host for the username.
	 * 
	 * @author Ian Brown
	 * @param username
	 *            the username, may contain the host.
	 * @return the username without the host.
	 * @since May 9, 2013
	 * @version May 9, 2013
	 */
	private final String usernameWithoutHost(final String username) {
		final int atSign = username.indexOf("@");
		if (atSign == -1) {
			return username;
		} else {
			return username.substring(0, atSign);
		}
	}
}