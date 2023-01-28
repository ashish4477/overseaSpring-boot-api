/**
 * Copyright 2015 Bear Code, LLC<br/>
 * All Rights Reserved
 */
package com.bearcode.ovf.actions.eod.admin;

import static com.bearcode.ovf.model.common.CommonTestHelper.assertEod;
import static com.bearcode.ovf.model.common.CommonTestHelper.buildEod;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.DAO.StateDAO;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.service.LocalOfficialService;

/**
 * Integration test for {@link ExcelDataUpload}.
 * @author Ian
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Ignore
@ContextConfiguration(locations = { "../../commons/OverseasFormController-context.xml", "ExcelDataUploadIntegration-context.xml" })
public final class ExcelDataUploadIntegration extends BaseControllerExam<ExcelDataUpload> {
	
	/** the name of the file to upload. */
	private final static String FILENAME = "src/test/resources/ExcelPortTest.xls";
	
	/** the local official service to use. */
	@Autowired
	private LocalOfficialService localOfficialService;

	/** the DAO to work on the state. */
	@Autowired
	private StateDAO stateDAO;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataUpload#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Long)}
	 * for a completely EOD.
	 * @throws Exception if there is a problem uploading the file.
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/eod/admin/ExcelDataUpload_stateOnly.xml" })
	public final void testOnSubmit_newEOD() throws Exception {
		final State state = stateDAO.getByName("State Name");
		final MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/admin/EodDataUpload.htm");
		request.setParameter("stateId", state.getId().toString());
		final File excelFile = new File(FILENAME);
		final FileInputStream excelFileStream = new FileInputStream(excelFile);
		final MockMultipartFile requestFile = new MockMultipartFile("leosFile", FILENAME, "application/vnd.ms-excel", excelFileStream);
		request.addFile(requestFile);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model is returned", actualModel);
		assertNull("No error occurred", actualModel.get("error"));
		assertTrue("The file was successfully uploaded", (Boolean) actualModel.get("success"));
		assertEquals("The processed state is the requested one", state, actualModel.get("processedState"));
		assertEquals("The correct number of LEOs were processed", 4, ((Integer) actualModel.get("numberOfLeos")).intValue());
		assertNull("No error occurred", actualModel.get("error"));
		final Collection<LocalOfficial> actualEod = localOfficialService.findForState(state);
		final Collection<LocalOfficial> expectedEod = buildEod(state);
		assertEod(expectedEod, actualEod);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataUpload#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Long)}
	 * for the case where there is one new LEO, everything else is unchanged.
	 * @throws Exception if there is a problem uploading the file.
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/eod/admin/ExcelDataUpload_threeLEO.xml" })
	public final void testOnSubmit_newLEO() throws Exception {
		clearIdentifiers();
		final State state = stateDAO.getByName("State Name");
		final MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/admin/EodDataUpload.htm");
		request.setParameter("stateId", state.getId().toString());
		final File excelFile = new File(FILENAME);
		final FileInputStream excelFileStream = new FileInputStream(excelFile);
		final MockMultipartFile requestFile = new MockMultipartFile("leosFile", FILENAME, "application/vnd.ms-excel", excelFileStream);
		request.addFile(requestFile);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model is returned", actualModel);
		assertNull("No error occurred", actualModel.get("error"));
		assertTrue("The file was successfully uploaded", (Boolean) actualModel.get("success"));
		assertEquals("The processed state is the requested one", state, actualModel.get("processedState"));
		assertEquals("The correct number of LEOs were processed", 4, ((Integer) actualModel.get("numberOfLeos")).intValue());
		assertNull("No error occurred", actualModel.get("error"));
		final Collection<LocalOfficial> actualEod = localOfficialService.findForState(state);
		final Collection<LocalOfficial> expectedEod = buildEod(state);
		assertEod(expectedEod, actualEod);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.ExcelDataUpload#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, Long)}
	 * for the case where there is one fixed LEO, everything else is unchanged.
	 * @throws Exception if there is a problem uploading the file.
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/eod/admin/ExcelDataUpload_fourLEO.xml" })
	public final void testOnSubmit_fixLEO() throws Exception {
		clearIdentifiers();
		final State state = stateDAO.getByName("State Name");
		final MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("POST");
		request.setRequestURI("/admin/EodDataUpload.htm");
		request.setParameter("stateId", state.getId().toString());
		final File excelFile = new File(FILENAME);
		final FileInputStream excelFileStream = new FileInputStream(excelFile);
		final MockMultipartFile requestFile = new MockMultipartFile("leosFile", FILENAME, "application/vnd.ms-excel", excelFileStream);
		request.addFile(requestFile);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		final ModelMap actualModel = actualModelAndView.getModelMap();
		assertNotNull("A model is returned", actualModel);
		assertNull("No error occurred", actualModel.get("error"));
		assertTrue("The file was successfully uploaded", (Boolean) actualModel.get("success"));
		assertEquals("The processed state is the requested one", state, actualModel.get("processedState"));
		assertEquals("The correct number of LEOs were processed", 4, ((Integer) actualModel.get("numberOfLeos")).intValue());
		assertNull("No error occurred", actualModel.get("error"));
		final Collection<LocalOfficial> actualEod = localOfficialService.findForState(state);
		final Collection<LocalOfficial> expectedEod = buildEod(state);
		assertEod(expectedEod, actualEod);
	}
	
	/**
	 * Unfortunately, the database load code appears to fill zeros into the identifiers when they should be
	 * <code>NULL</code>. This code fixes those.
	 */
	private final void clearIdentifiers() {
	    final Session session = stateDAO.getSessionFactory().getCurrentSession();
	    
	    // Fix the county_id and municipality_id fields of the voting_regions table.
	    SQLQuery sqlQuery = session.createSQLQuery( //
	    		"UPDATE `voting_regions`" + //
	    		"   SET `county_id` = CASE" + //
	    		"                       WHEN `county_id` = 0 THEN NULL" + //
	    		"                       ELSE `county_id`" + //
	    		"                     END," + //
	    		"        `municipality_id` = CASE" + //
	    		"                               WHEN `municipality_id` = 0 THEN NULL" + //
	    		"                               ELSE `municipality_id`" + //
	    		"                            END;"
	    		);
	    sqlQuery.executeUpdate();
	    
	    // Fix the county_id field of the municipalities table.
	    sqlQuery = session.createSQLQuery( //
	    		"UPDATE `municipalities`" + //
	    		"   SET `county_id` = CASE" + //
	    		"                       WHEN `county_id` = 0 THEN NULL" + //
	    		"                       ELSE `county_id`" + //
	    		"                      END;"
	    		);
	    sqlQuery.executeUpdate();
    }

	/** {@inheritDoc} */
	@Override
    protected final ExcelDataUpload createBaseController() {
	       final ExcelDataUpload excelDataUpload = applicationContext.getBean(ExcelDataUpload.class);
	        excelDataUpload.setLocalOfficialService(localOfficialService);
	        return excelDataUpload;
    }

	/** {@inheritDoc} */
	@Override
    protected final void setUpForBaseController() {
    }

	/** {@inheritDoc} */
	@Override
    protected final void tearDownForBaseController() {
    }
}
