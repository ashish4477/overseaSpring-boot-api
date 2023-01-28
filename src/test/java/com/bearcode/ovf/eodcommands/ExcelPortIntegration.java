/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.eodcommands;

import static com.bearcode.ovf.model.common.CommonTestHelper.assertEod;
import static com.bearcode.ovf.model.common.CommonTestHelper.buildEod;
import static com.bearcode.ovf.model.common.CommonTestHelper.buildState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.DAO.VotingRegionDAO;
import com.bearcode.ovf.dbunittest.OVFDBUnitDatabaseName;
import com.bearcode.ovf.dbunittest.OVFDBUnitTestExecutionListener;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Integration test for {@link ExcelPort}.
 * @author Ian
 *
 */
@TestExecutionListeners({ OVFDBUnitTestExecutionListener.class })
@OVFDBUnitDatabaseName(databaseName = "test_overseas")
@ContextConfiguration(locations = { "applicationContext_test.xml", "ExcelPortIntegration-context.xml" })
@DirtiesContext
public final class ExcelPortIntegration extends AbstractTransactionalJUnit4SpringContextTests {

	/** the name of the file to use to perform read tests. */
	private static final String TEST_FILE = "src/test/resources/ExcelPortTest.xls";

	/** the Excel port to test. */
	@Autowired
	private ExcelPort excelPort;
	
	/** the service used to work with local officials. */
	@Autowired
	private LocalOfficialService localOfficialService;
	
	/** the DAO used to work on the voting region and associated objects. */
	@Autowired
	private VotingRegionDAO regionDAO;
	
	/** the DAO used to work on the state. */
	@Autowired
	private StateDAO stateDAO;
	
	
	/**
	 * Test method for {@link com.bearcode.ovf.eodcommands.ExcelPort#readFromExcel(java.io.InputStream, com.bearcode.ovf.model.common.State)}.
	 * @throws IOException if there is a problem reading the test file.
	 */
	@Test
	public final void testReadFromExcel() throws IOException {
		final State state = buildState();
		final File file = new File(TEST_FILE);
		final FileInputStream fis = new FileInputStream(file);
		stateDAO.makePersistent(state);
		
		try {
			final Collection<LocalOfficial> actualEod = excelPort.readFromExcel(fis, state);
			
			final Collection<LocalOfficial> expectedEod = buildEod(state);
			assertEod(expectedEod, actualEod);
		} finally {
			fis.close();
		}
	}

	/**
	 * Test method for {@link com.bearcode.ovf.eodcommands.ExcelPort#writeIntoExcel(java.util.Collection, java.util.Collection)}.
	 * @throws IOException if there is a problem writing the workbench.
	 */
	@Test
	public final void testWriteIntoExcelCollectionOfStateCollectionOfLocalOfficial() throws IOException {
		final State state = buildState();
		final Collection<State> states = Arrays.asList(state);
		final Collection<LocalOfficial> eod = buildEod(state);
		stateDAO.makePersistent(state);
		localOfficialService.saveAllLocalOfficial(eod);

		final HSSFWorkbook workbook = excelPort.writeIntoExcel(states, eod);

		assertNotNull("Created a workbook", workbook);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.eodcommands.ExcelPort#writeIntoExcel(com.bearcode.ovf.model.common.State, java.util.Collection)}.
	 * @throws IOException if there is a problem writing the file.
	 */
	@Test
	public final void testWriteIntoExcelStateCollectionOfLocalOfficial() throws IOException {
		final State state = buildState();
		final Collection<LocalOfficial> eod = buildEod(state);
		stateDAO.makePersistent(state);
		localOfficialService.saveAllLocalOfficial(eod);

		final HSSFWorkbook workbook = excelPort.writeIntoExcel(state, eod);

		assertNotNull("Created a workbook", workbook);
	}
}
