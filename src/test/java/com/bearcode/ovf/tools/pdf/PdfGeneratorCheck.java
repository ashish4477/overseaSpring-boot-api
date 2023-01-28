/**
 * 
 */
package com.bearcode.ovf.tools.pdf;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Abstract base class for testing implementations of {@link PdfGenerator}.
 * 
 * @author IanBrown
 * 
 * @param <G>
 *            the type of PDF generator to test.
 * @since Mar 21, 2012
 * @version Mar 22, 2012
 */
public abstract class PdfGeneratorCheck<G extends PdfGenerator> extends EasyMockSupport {

	/**
	 * the PDF generator to test.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private G pdfGenerator;

	/**
	 * the wizard wizardContext to use.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private WizardContext wizardContext;

	/**
	 * the face configuration to use.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private FaceConfig faceConfig;

	/**
	 * the HTTP response to use.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 22, 2012
	 */
	private MockHttpServletResponse response;

	/**
	 * the answers "provided" to the wizard.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private Collection<Answer> answers;

	/**
	 * Sets up the PDF generator to test.
	 * 
	 * @author IanBrown
	 * @throws IOException
	 *             if there is a problem creating the PDF generator.
	 * @since Mar 21, 2012
	 * @version Mar 22, 2012
	 */
	@Before
	public final void setUpPdfGenerator() throws IOException {
		setAnswers(new LinkedList<Answer>());
		setWizardContext(createWizardContext());
		setFaceConfig(createMock("FaceConfig", FaceConfig.class));
		setResponse(new MockHttpServletResponse());
		getResponse().setOutputStreamAccessAllowed(true);
		setUpForPdfGenerator();
		setPdfGenerator(createPdfGenerator(getWizardContext(), getFaceConfig(), getResponse()));
	}

	/**
	 * Tears down the PDF generator after testing.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@After
	public final void tearDownPdfGenerator() {
		setPdfGenerator(null);
		tearDownForPdfGenerator();
		setResponse(null);
		setFaceConfig(null);
		setWizardContext(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.pdf.PdfGenerator#dispose()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@Test
	public final void testDispose() {
		setUpForDispose();

		getPdfGenerator().dispose();

		assertDispose();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.tools.pdf.PdfGenerator#run()}.
	 * 
	 * @author IanBrown
	 * 
	 * @throws PdfGeneratorException
	 *             if there is a problem running the PDF generator.
	 * @throws IOException
	 *             if there is an I/O problem setting up for the run.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	@Test
	public final void testRun() throws PdfGeneratorException, IOException {
		setUpForRun();
		replayAll();

		getPdfGenerator().run();

		assertRun();
		verifyAll();
	}

	/**
	 * Custom assertion to ensure that a dispose was handled properly by the specific PDF generator.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void assertDispose();

	/**
	 * Custom assertion ensure that the file name is correct for the specific PDF generator.
	 * 
	 * @author IanBrown
	 * @param actualFileName
	 *            the file name.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void assertFileName(String actualFileName);

	/**
	 * Custom assertion to ensure that the file name parameters are set.
	 * 
	 * @author IanBrown
	 * @param actualFileNameParameters
	 *            the actual file name parameters.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected void assertFileNameParameters(final Map<String, String> actualFileNameParameters) {
		assertTrue("The year is set", actualFileNameParameters.containsKey("%year"));
		assertTrue("The month is set", actualFileNameParameters.containsKey("%month"));
		assertTrue("The day of month is set", actualFileNameParameters.containsKey("%day_of_month"));
		assertTrue("The hour is set", actualFileNameParameters.containsKey("%hour"));
		assertTrue("The minute is set", actualFileNameParameters.containsKey("%minute"));
		assertTrue("The second is set", actualFileNameParameters.containsKey("%second"));
	}

	/**
	 * Custom assertion to ensure that the ID for the generator is correct.
	 * 
	 * @author IanBrown
	 * @param actualId
	 *            the actual ID.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected void assertId(final String actualId) {
		assertEquals("The ID is the class simple name", getPdfGenerator().getClass().getSimpleName(), actualId);
	}

	/**
	 * Custom assertion to ensure that a run was performed correctly.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void assertRun();

	/**
	 * Creates a PDF generator of the type to test.
	 * 
	 * @author IanBrown
	 * @param wizardContext
	 *            the wizard wizardContext.
	 * @param faceConfig
	 *            the face configuration.
	 * @param response
	 *            the HTTP response.
	 * @return the PDF generator.
	 * @throws IOException
	 *             if there is a problem creating the PDF generator.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract G createPdfGenerator(WizardContext wizardContext, FaceConfig faceConfig, HttpServletResponse response)
			throws IOException;

	/**
	 * Gets the answers "provided" by the user.
	 * 
	 * @author IanBrown
	 * @return the answers.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected final Collection<Answer> getAnswers() {
		return answers;
	}

	/**
	 * Gets the faceConfig.
	 * 
	 * @author IanBrown
	 * @return the faceConfig.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected final FaceConfig getFaceConfig() {
		return faceConfig;
	}

	/**
	 * Gets the PDF generator.
	 * 
	 * @author IanBrown
	 * @return the PDF generator.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected final G getPdfGenerator() {
		return pdfGenerator;
	}

	/**
	 * Gets the response.
	 * 
	 * @author IanBrown
	 * @return the response.
	 * @since Mar 21, 2012
	 * @version Mar 22, 2012
	 */
	protected final MockHttpServletResponse getResponse() {
		return response;
	}

	/**
	 * Gets the wizardContext.
	 * 
	 * @author IanBrown
	 * @return the wizardContext.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected final WizardContext getWizardContext() {
		return wizardContext;
	}

	/**
	 * Sets things up to be able to perform a {@link com.bearcode.ovf.tools.pdf.PdfGenerator#dispose()} call for the specific type
	 * of PDF generator.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void setUpForDispose();

	/**
	 * Sets up to test the specific type of PDF generator.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void setUpForPdfGenerator();

	/**
	 * Sets things up to perform a run of the specific PDF generator.
	 * 
	 * @author IanBrown
	 * @throws IOException
	 *             if there is a I/O problem setting up for the run.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void setUpForRun() throws IOException;

	/**
	 * Tears down the set up for testing the specific type of PDF generator after testing.
	 * 
	 * @author IanBrown
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	protected abstract void tearDownForPdfGenerator();

	/**
	 * Custom assertion to ensure that the template parameters are correct.
	 * 
	 * @author IanBrown
	 * @param actualTemplateParameters
	 *            the actual template parameters.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private void assertTemplateParameters(final Map<String, String> actualTemplateParameters) {
		assertTrue("There are no template parameters", actualTemplateParameters.isEmpty());
	}

	/**
	 * Creates the wizard wizardContext for testing.
	 * 
	 * @author IanBrown
	 * @return the wizard wizardContext.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private WizardContext createWizardContext() {
		final WizardResults wizardResults = new WizardResults(FlowType.DOMESTIC_ABSENTEE);
		wizardResults.setFlowType(FlowType.RAVA);
		final WizardResultAddress votingAddress = new WizardResultAddress();
		votingAddress.setState("ST");
		wizardResults.setVotingAddress(votingAddress);
		wizardResults.setAnswers(getAnswers());
		final WizardContext context = new WizardContext(wizardResults);
		return context;
	}

	/**
	 * Sets the answers.
	 * 
	 * @author IanBrown
	 * @param answers
	 *            the answers to set.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private void setAnswers(final Collection<Answer> answers) {
		this.answers = answers;
	}

	/**
	 * Sets the face configuration.
	 * 
	 * @author IanBrown
	 * @param faceConfig
	 *            the face configuration to set.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private void setFaceConfig(final FaceConfig faceConfig) {
		this.faceConfig = faceConfig;
	}

	/**
	 * Sets the PDF generator.
	 * 
	 * @author IanBrown
	 * @param pdfGenerator
	 *            the PDF generator to set.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private final void setPdfGenerator(final G pdfGenerator) {
		this.pdfGenerator = pdfGenerator;
	}

	/**
	 * Sets the response.
	 * 
	 * @author IanBrown
	 * @param response
	 *            the response to set.
	 * @since Mar 21, 2012
	 * @version Mar 22, 2012
	 */
	private void setResponse(final MockHttpServletResponse response) {
		this.response = response;
	}

	/**
	 * Sets the wizardContext.
	 * 
	 * @author IanBrown
	 * @param context
	 *            the wizardContext to set.
	 * @since Mar 21, 2012
	 * @version Mar 21, 2012
	 */
	private final void setWizardContext(final WizardContext context) {
		this.wizardContext = context;
	}

}
