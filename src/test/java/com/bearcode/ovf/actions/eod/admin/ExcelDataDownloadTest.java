/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.eodcommands.ExcelPort;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

/**
 * Test for {@link ExcelDataDownload}.
 * <p>
 * TODO dawnload is the name of the class, but it probably should be download.
 * 
 * @author IanBrown
 * 
 * @since Jul 26, 2012
 * @version Jul 26, 2012
 */
public final class ExcelDataDownloadTest extends EasyMockSupport {

	/**
	 * the Excel data dawnload to test.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelDataDownload excelDataDawnload;

	/**
	 * the state service to use.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private StateService stateService;

	/**
	 * the local official service to use.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * the Excel port.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelPort excelPort;

	/**
	 * Sets up the Excel data dawnload to test.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Before
	public final void setUpExcelDataDawnload() {
		setStateService(createMock("StateService", StateService.class));
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
		setExcelPort(createMock("ExcelPort", ExcelPort.class));
		setExcelDataDawnload(createExcelDataDawnload());
		getExcelDataDawnload().setStateService(getStateService());
		getExcelDataDawnload().setLocalOfficialService(getLocalOfficialService());
		getExcelDataDawnload().setExcelPort(getExcelPort());
	}

	/**
	 * Tears down the set up for testing the Excel data dawnload.
	 * 
	 * @author IanBrown
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@After
	public final void tearDownExcelDataDawnload() {
		setExcelDataDawnload(null);
		setExcelPort(null);
		setLocalOfficialService(null);
		setStateService(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataDownload#handleRequestInternal(Long, String)}
	 * for all.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_all() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(getStateService().findState(0l)).andReturn(null);
		request.setParameter("all", "all");
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> all = Arrays.asList(localOfficial);
		EasyMock.expect(getLocalOfficialService().findAll()).andReturn(all);
		final State state = createMock("State", State.class);
		final Collection<State> states = Arrays.asList(state);
		EasyMock.expect(getStateService().findAllStates()).andReturn(states);
		final HSSFWorkbook workbook = createMock("Workbook", HSSFWorkbook.class);
		EasyMock.expect(getExcelPort().writeIntoExcel(states, all)).andReturn(workbook);
		workbook.write( EasyMock.isA( OutputStream.class ) );
		replayAll();

		final ResponseEntity<byte[]> actualResponse = getExcelDataDawnload().handleRequestInternal(0l, "all");

		assertNotNull("No result is returned", actualResponse);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataDownload#handleRequestInternal(Long, String)}
	 * no state nor all.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_noStateNorAll() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		EasyMock.expect(getStateService().findState(0l)).andReturn(null);
		replayAll();

		final ResponseEntity<byte[]> actualResponse = getExcelDataDawnload().handleRequestInternal(0l, "");

		assertNull( "Result is returned", actualResponse );
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataDownload#handleRequestInternal(Long, String)}
	 * for the case where there is a state.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	@Test
	public final void testHandleRequestInternalHttpServletRequestHttpServletResponse_state() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final long stateId = 26l;
		request.setParameter("stateId", Long.toString(stateId));
		final State state = createMock("State", State.class);
		EasyMock.expect(getStateService().findState(stateId)).andReturn(state);
		final LocalOfficial localOfficial = createMock("LocalOfficial", LocalOfficial.class);
		final Collection<LocalOfficial> eod = Arrays.asList(localOfficial);
		EasyMock.expect(getLocalOfficialService().findForState(state)).andReturn(eod);
		final String stateName = "State Name";
		EasyMock.expect(state.getName()).andReturn(stateName);
		final HSSFWorkbook workbook = createMock("Workbook", HSSFWorkbook.class);
		EasyMock.expect(getExcelPort().writeIntoExcel(state, eod)).andReturn(workbook);
		workbook.write( EasyMock.isA( OutputStream.class ) );
		replayAll();

		final ResponseEntity<byte[]> actualResponse = getExcelDataDawnload().handleRequestInternal(26l, "");

		assertNotNull("No result is returned", actualResponse);
		verifyAll();
	}

	/**
	 * Creates an Excel data dawnload.
	 * 
	 * @author IanBrown
	 * @return the Excel data dawnload.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelDataDownload createExcelDataDawnload() {
		final ExcelDataDownload excelDataDawnload = new ExcelDataDownload();
		return excelDataDawnload;
	}

	/**
	 * Gets the Excel data dawnload.
	 * 
	 * @author IanBrown
	 * @return the Excel data dawnload.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelDataDownload getExcelDataDawnload() {
		return excelDataDawnload;
	}

	/**
	 * Gets the Excel port.
	 * 
	 * @author IanBrown
	 * @return the Excel port.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private ExcelPort getExcelPort() {
		return excelPort;
	}

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private StateService getStateService() {
		return stateService;
	}

	/**
	 * Sets the Excel data dawnload.
	 * 
	 * @author IanBrown
	 * @param excelDataDawnload
	 *            the Excel data dawnload to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setExcelDataDawnload(final ExcelDataDownload excelDataDawnload) {
		this.excelDataDawnload = excelDataDawnload;
	}

	/**
	 * Sets the Excel port.
	 * 
	 * @author IanBrown
	 * @param excelPort
	 *            the Excel port to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setExcelPort(final ExcelPort excelPort) {
		this.excelPort = excelPort;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

	/**
	 * Sets the state service.
	 * 
	 * @author IanBrown
	 * @param stateService
	 *            the state service to set.
	 * @since Jul 26, 2012
	 * @version Jul 26, 2012
	 */
	private void setStateService(final StateService stateService) {
		this.stateService = stateService;
	}

}
