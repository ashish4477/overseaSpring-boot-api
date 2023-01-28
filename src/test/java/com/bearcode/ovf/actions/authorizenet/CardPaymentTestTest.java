/**
 * 
 */
package com.bearcode.ovf.actions.authorizenet;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Map;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.Errors;

import com.bearcode.ovf.actions.commons.OverseasFormControllerCheck;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.StateService;

/**
 * Extended {@link OverseasFormControllerCheck} test for {@link CardPaymentTest}
 * .
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 21, 2011
 */
public final class CardPaymentTestTest extends
		OverseasFormControllerCheck<CardPaymentTest> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.CardPaymentTest#buildReferences(javax.servlet.http.HttpServletRequest, java.lang.Object, org.springframework.validation.Errors)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @throws Exception
	 *             if there is a problem building the references.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testBuildReferences() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final Object object = createReferenceObject();
		final Errors errors = createMock("Errors", Errors.class);

		final Map actualReferences = getOverseasFormController()
				.buildReferences(request, object, errors);

		assertNull("There are no references", actualReferences);
	}


	/** {@inheritDoc} */
	@Override
	protected final void assertFormBackingObject(
			final Object actualFormBackingObject) {
		assertTrue("The form backing object is a string buffer",
				actualFormBackingObject instanceof StringBuffer);
	}

	/** {@inheritDoc} */
	@SuppressWarnings("rawtypes")
	@Override
	protected final void assertMinimalReferenceData(
			final MockHttpServletRequest request, final Object object,
			final Errors errors, final StateService stateService,
			final FacesService facesService, final Map actualReferenceData) {
		// Nothing should be necessary.
	}

	/** {@inheritDoc} */
	@Override
	protected final Object createCommand() {
		return createMock("Command", Object.class);
	}

	/** {@inheritDoc} */
	@Override
	protected final CardPaymentTest createOverseasFormController() {
		return new CardPaymentTest();
	}

	/** {@inheritDoc} */
	@Override
	protected final Object createReferenceObject() {
		return createMock("ReferenceObject", Object.class);
	}

	/** {@inheritDoc} */
	@Override
	protected final boolean getExpectedPostFormSubmission(boolean submitted) {
		return true;
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpForOverseasFormController() {
	}

	/** {@inheritDoc} */
	@Override
	protected final void setUpMinimalReferenceData(
			final MockHttpServletRequest request, final Object object,
			final Errors errors, final StateService stateService,
			final FacesService facesService) {
		// Nothing should be necessary.
	}

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForOverseasFormController() {
	}
}
