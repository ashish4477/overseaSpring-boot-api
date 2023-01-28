/**
 * 
 */
package com.bearcode.ovf.actions.cf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.service.VipService;
import com.bearcode.ovf.tools.vip.xml.VipObject;

/**
 * Abstract extended {@link BaseControllerExam} to perform integration testing of what's on my ballot? controller.
 * 
 * @author IanBrown
 * 
 * @param <C>
 *            the type of what's on my ballot? controller to test.
 * @since Aug 14, 2012
 * @version Oct 11, 2012
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "WhatsOnMyBallotIntegration-context.xml" })
@DirtiesContext
public abstract class WhatsOnMyBallotControllerExam<C extends BaseController> extends BaseControllerExam<C> {

	/**
	 * the VIP service.
	 * 
	 * @author IanBrown
	 * @since Aug 8, 2012
	 * @version Aug 8, 2012
	 */
	@Autowired
	private VipService vipService;

	/**
	 * the state service.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@Autowired
	private StateService stateService;

	/** {@inheritDoc} */
	@Override
	protected final C createBaseController() {
		return createWhatsOnMyBallotController();
	}

	/**
	 * Creates a what's on my ballot? controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the what's on my ballot? controller.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	protected abstract C createWhatsOnMyBallotController();

	/**
	 * Gets the state service.
	 * 
	 * @author IanBrown
	 * @return the state service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	protected final StateService getStateService() {
		return stateService;
	}

	/**
	 * Gets the VIP service.
	 * 
	 * @author IanBrown
	 * @return the VIP service.
	 * @since Aug 13, 2012
	 * @version Aug 16, 2012
	 */
	protected final VipService getVipService() {
		return vipService;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		try {
			loadVipData();
		} catch (final FileNotFoundException e) {
			throw new IllegalStateException("Failed to set up VIP data", e);
		} catch (final JAXBException e) {
			throw new IllegalStateException("Failed to set up VIP data", e);
		} catch (final IOException e) {
			throw new IllegalStateException("Failed to set up VIP data", e);
		}

		setUpForWhatsOnMyBallotController();
	}

	/**
	 * Sets up to test the specific type of what's on my ballot? controller.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	protected abstract void setUpForWhatsOnMyBallotController();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		tearDownForWhatsOnMyBallotController();
	}

	/**
	 * Tears down the set up for testing the specific type of what's on my ballot? controller.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	protected abstract void tearDownForWhatsOnMyBallotController();

	/**
	 * Loads the VIP data.
	 * 
	 * @author IanBrown
	 * @throws FileNotFoundException
	 *             if the XML file cannot be found.
	 * @throws JAXBException
	 *             if the XML file cannot be read.
	 * @throws IOException
	 *             if there is a problem with the XML file.
	 * @since Aug 13, 2012
	 * @version Oct 11, 2012
	 */
	private void loadVipData() throws FileNotFoundException, JAXBException, IOException {
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