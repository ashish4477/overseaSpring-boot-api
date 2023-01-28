/**
 * 
 */
package com.bearcode.ovf.validators;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.validation.Errors;

import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Test for {@link WhatsOnMyBallotValidator}.
 * 
 * @author IanBrown
 * 
 * @since Aug 13, 2012
 * @version Sep 4, 2012
 */
public final class WhatsOnMyBallotValidatorTest extends EasyMockSupport {

	/**
	 * the what's on my ballot? validator to test.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private WhatsOnMyBallotValidator whatsOnMyBallotValidator;

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Sets up the what's on my ballot? validator to test.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 14, 2012
	 */
	@Before
	public final void setUpWhatsOnMyBallotValidator() {
		setVotingPrecinctService(createMock("VotingPrecinctService", VotingPrecinctService.class));
		setWhatsOnMyBallotValidator(createWhatsOnMyBallotValidator());
		getWhatsOnMyBallotValidator().setVotingPrecinctService(getVotingPrecinctService());
	}

	/**
	 * Tears down the what's on my ballot? validator after testing.
	 * 
	 * @author IanBrown
	 * @since Aug 13, 2012
	 * @version Aug 14, 2012
	 */
	@After
	public final void tearDownWhatsOnMyBallotValidator() {
		setWhatsOnMyBallotValidator(null);
		setVotingPrecinctService(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#isSupported(java.lang.String, String)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 15, 2012
	 */
	@Test
	public final void testIsStateSupported() {
		final String votingRegionState = "SI";
		EasyMock.expect(getVotingPrecinctService().isSupported(votingRegionState, null)).andReturn(true);
		replayAll();

		final boolean actualSupported = getWhatsOnMyBallotValidator().isSupported(votingRegionState, null);

		assertTrue("The state is supported", actualSupported);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#isSupported(java.lang.String, String)} for the case
	 * where no state is provided.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 15, 2012
	 */
	@Test
	public final void testIsStateSupported_noState() {
		final String votingRegionState = null;
		replayAll();

		final boolean actualSupported = getWhatsOnMyBallotValidator().isSupported(votingRegionState, null);

		assertFalse("The state is not supported", actualSupported);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#isSupported(java.lang.String, String)} for the case
	 * where the state isn't supported.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 15, 2012
	 */
	@Test
	public final void testIsStateSupported_notSupported() {
		final String votingRegionState = "SI";
		EasyMock.expect(getVotingPrecinctService().isSupported(votingRegionState, null)).andReturn(false);
		replayAll();

		final boolean actualSupported = getWhatsOnMyBallotValidator().isSupported(votingRegionState, null);

		assertFalse("The state is not supported", actualSupported);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#supports(java.lang.Class)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	@Test
	public final void testSupports() {
		replayAll();

		final boolean actualSupported = getWhatsOnMyBallotValidator().supports(WhatsOnMyBallotForm.class);

		assertTrue("The class is supported", actualSupported);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#supports(java.lang.Class)} for an unsupported
	 * class.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	@Test
	public final void testSupports_notSupported() {
		replayAll();

		final boolean actualSupported = getWhatsOnMyBallotValidator().supports(Object.class);

		assertFalse("The class is not supported", actualSupported);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(java.lang.Object, org.springframework.validation.Errors)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Sep 4, 2012
	 */
	@Test
	public final void testValidateObjectErrors() {
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final Errors errors = createMock("Errors", Errors.class);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(address.getState()).andReturn(null);
		final String stateAbbr = "SA";
		EasyMock.expect(votingState.getAbbr()).andReturn(stateAbbr);
		address.setState(stateAbbr);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
		replayAll();

		getWhatsOnMyBallotValidator().validate(whatsOnMyBallot, errors);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(java.lang.Object, org.springframework.validation.Errors)}
	 * for an invalid address.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Sep 4, 2012
	 */
	@Test
	public final void testValidateObjectErrors_invalidAddress() {
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final Errors errors = createMock("Errors", Errors.class);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final String stateAbbr = "SA";
		EasyMock.expect(address.getState()).andReturn(stateAbbr);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(null);
		errors.rejectValue("address", "", WhatsOnMyBallotValidator.INVALID_ADDRESS_ERROR);
		replayAll();

		getWhatsOnMyBallotValidator().validate(whatsOnMyBallot, errors);

		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(org.springframework.validation.Errors, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Sep 4, 2012
	 */
	@Test
	public final void testValidateWhatsOnMyBallotFormErrors() {
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final Errors errors = createMock("Errors", Errors.class);
		final UserAddress address = createMock("Address", UserAddress.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final String stateAbbr = "SA";
		EasyMock.expect(address.getState()).andReturn(stateAbbr);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		final ValidAddress validAddress = createMock("ValidAddress", ValidAddress.class);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(validAddress);
		replayAll();

		final ValidAddress actualValidAddress = getWhatsOnMyBallotValidator().validate(errors, whatsOnMyBallot);

		assertSame("The address is valid", validAddress, actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(org.springframework.validation.Errors, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the address is invalid.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Sep 4, 2012
	 */
	@Test
	public final void testValidateWhatsOnMyBallotFormErrors_invalidAddress() {
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final Errors errors = createMock("Errors", Errors.class);
		final UserAddress address = createMock("Address", UserAddress.class);
		final String stateAbbr = "SA";
		EasyMock.expect(address.getState()).andReturn(stateAbbr);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(null);
		errors.rejectValue("address", "", WhatsOnMyBallotValidator.INVALID_ADDRESS_ERROR);
		replayAll();

		final ValidAddress actualValidAddress = getWhatsOnMyBallotValidator().validate(errors, whatsOnMyBallot);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(org.springframework.validation.Errors, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where the address is invalid and there is no errors object.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Sep 4, 2012
	 */
	@Test
	public final void testValidateWhatsOnMyBallotFormErrors_invalidAddressNoErrors() {
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final Errors errors = null;
		final UserAddress address = createMock("Address", UserAddress.class);
		final String stateAbbr = "SA";
		EasyMock.expect(address.getState()).andReturn(stateAbbr);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(address);
		final State votingState = createMock("VotingState", State.class);
		EasyMock.expect(whatsOnMyBallot.getVotingState()).andReturn(votingState);
		EasyMock.expect(getVotingPrecinctService().validateAddress(address, votingState)).andReturn(null);
		replayAll();

		final ValidAddress actualValidAddress = getWhatsOnMyBallotValidator().validate(errors, whatsOnMyBallot);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(org.springframework.validation.Errors, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is a form, but no address.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 14, 2012
	 */
	@Test
	public final void testValidateWhatsOnMyBallotFormErrors_noAddress() {
		final WhatsOnMyBallotForm whatsOnMyBallot = createMock("WhatsOnMyBallot", WhatsOnMyBallotForm.class);
		final Errors errors = createMock("Errors", Errors.class);
		EasyMock.expect(whatsOnMyBallot.getAddress()).andReturn(null);
		errors.rejectValue("address", "", WhatsOnMyBallotValidator.MISSING_ADDRESS_ERROR);
		replayAll();

		final ValidAddress actualValidAddress = getWhatsOnMyBallotValidator().validate(errors, whatsOnMyBallot);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(org.springframework.validation.Errors, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no form.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 14, 2012
	 */
	@Test
	public final void testValidateWhatsOnMyBallotFormErrors_noForm() {
		final WhatsOnMyBallotForm whatsOnMyBallot = null;
		final Errors errors = createMock("Errors", Errors.class);
		errors.rejectValue("address", "", WhatsOnMyBallotValidator.MISSING_ADDRESS_ERROR);
		replayAll();

		final ValidAddress actualValidAddress = getWhatsOnMyBallotValidator().validate(errors, whatsOnMyBallot);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.validators.WhatsOnMyBallotValidator#validate(org.springframework.validation.Errors, com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm)}
	 * for the case where there is no form and no errors object.
	 * 
	 * @author IanBrown
	 * 
	 * @since Aug 13, 2012
	 * @version Aug 14, 2012
	 */
	@Test
	public final void testValidateWhatsOnMyBallotFormErrors_noInputs() {
		final WhatsOnMyBallotForm whatsOnMyBallot = null;
		final Errors errors = null;
		replayAll();

		final ValidAddress actualValidAddress = getWhatsOnMyBallotValidator().validate(errors, whatsOnMyBallot);

		assertNull("The address is not valid", actualValidAddress);
		verifyAll();
	}

	/**
	 * Creates a what's on my ballot? validator.
	 * 
	 * @author IanBrown
	 * @return the what's on my ballot? validator.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private WhatsOnMyBallotValidator createWhatsOnMyBallotValidator() {
		return new WhatsOnMyBallotValidator();
	}

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Gets the what's on my ballot? validator.
	 * 
	 * @author IanBrown
	 * @return the what's on my ballot? validator.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private WhatsOnMyBallotValidator getWhatsOnMyBallotValidator() {
		return whatsOnMyBallotValidator;
	}

	/**
	 * Sets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @param votingPrecinctService
	 *            the voting precinct service to set.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	private void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/**
	 * Sets the what's on my ballot? validator.
	 * 
	 * @author IanBrown
	 * @param whatsOnMyBallotValidator
	 *            the what's on my ballot? validator to set.
	 * @since Aug 13, 2012
	 * @version Aug 13, 2012
	 */
	private void setWhatsOnMyBallotValidator(final WhatsOnMyBallotValidator whatsOnMyBallotValidator) {
		this.whatsOnMyBallotValidator = whatsOnMyBallotValidator;
	}
}
