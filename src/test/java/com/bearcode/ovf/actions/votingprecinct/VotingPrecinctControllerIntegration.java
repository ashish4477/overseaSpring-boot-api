/**
 * 
 */
package com.bearcode.ovf.actions.votingprecinct;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bearcode.ovf.actions.commons.AbstractControllerExam;
import com.bearcode.ovf.dbunittest.OVFDBUnitUseData;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Extended {@link AbstractControllerExam} test for {@link VotingPrecinctController}.
 * 
 * @author IanBrown
 * 
 * @since Jul 28, 2012
 * @version Oct 11, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "VotingPrecinctControllerIntegration-context.xml" })
@DirtiesContext
public final class VotingPrecinctControllerIntegration extends AbstractControllerExam<VotingPrecinctController> {

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.votingprecinct.VotingPrecinctController#citiesByVotingRegion(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 28, 2012
	 * @version Oct 9, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCitiesByVotingRegion() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/votingPrecinct/cities/state/OH/region/Adams County");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		final ObjectMapper objectMapper = new ObjectMapper();
		final String content = response.getContentAsString();
		final List<String> actualCities = objectMapper.readValue(content, new TypeReference<List<String>>() {
		});
		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There are cities", actualCities.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.votingprecinct.VotingPrecinctController#citiesByZip(java.lang.String, java.lang.String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 28, 2012
	 * @version Jul 31, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testCitiesByZip() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/votingPrecinct/cities/state/VA/zip/22003");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		final ObjectMapper objectMapper = new ObjectMapper();
		final String content = response.getContentAsString();
		final List<String> actualCities = objectMapper.readValue(content, new TypeReference<List<String>>() {
		});
		assertNotNull("A list of cities is returned", actualCities);
		assertFalse("There are cities", actualCities.isEmpty());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.votingprecinct.VotingPrecinctController#streetNamesByCity(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jul 28, 2012
	 * @version Oct 24, 2012
	 */
	@Test
	@OVFDBUnitUseData
	public final void testStreetNamesByCity() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		request.setMethod("GET");
		request.setRequestURI("/votingPrecinct/streets/state/VA/city/Annandale");
		request.addHeader("Accept", "application/json");

		getHandlerAdapter().handle(request, response, getController());

		final ObjectMapper objectMapper = new ObjectMapper();
		final String content = response.getContentAsString();
		final List<String> actualStreetNames = objectMapper.readValue(content, new TypeReference<List<String>>() {
		});
		assertNotNull("A list of street names is returned", actualStreetNames);
		assertFalse("There are street names", actualStreetNames.isEmpty());
	}

	/** {@inheritDoc} */
	@Override
	protected final VotingPrecinctController createController() {
		final VotingPrecinctController votingPrecinctController = applicationContext.getBean(VotingPrecinctController.class);
		return votingPrecinctController;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForController() throws Exception {
		setUpVipService();
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForController() {
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Jul 28, 2012
	 * @version Jul 28, 2012
	 */
	private VipService getVipService() {
		return vipService;
	}

	/**
	 * Sets up the VIP service to have data.
	 * 
	 * @author IanBrown
	 * @throws JAXBException
	 *             if there is a problem parsing the XML.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Jul 28, 2012
	 * @version Oct 11, 2012
	 */
	private void setUpVipService() throws JAXBException, IOException {
		final String source = "src/test/resources/com/bearcode/ovf/tools/vip/vip.xml";
		final File sourceFile = new File(source);
		final FileInputStream fis = new FileInputStream(sourceFile);
		final JAXBContext context = JAXBContext.newInstance(VipObject.class.getPackage().getName());
		final Unmarshaller unmarshaller = context.createUnmarshaller();
		final VipObject vipObject = (VipObject) unmarshaller.unmarshal(fis);
		fis.close();
		getVipService().convert(vipObject, new Date());
	}
}
