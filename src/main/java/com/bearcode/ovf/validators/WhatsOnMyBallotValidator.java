/**
 * 
 */
package com.bearcode.ovf.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.bearcode.ovf.forms.cf.WhatsOnMyBallotForm;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserAddress;
import com.bearcode.ovf.tools.votingprecinct.VotingPrecinctService;
import com.bearcode.ovf.tools.votingprecinct.model.ValidAddress;

/**
 * Implementation of {@link Validator} to ensure that a {@link WhatsOnMyBallotForm} is valid.
 * 
 * @author IanBrown
 * 
 * @since Aug 13, 2012
 * @version Oct 9, 2012
 */
@Component
public class WhatsOnMyBallotValidator implements Validator {

	/**
	 * the message produced if the address is not valid.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	static final String INVALID_ADDRESS_ERROR = "The address is not officially recognized";

	/**
	 * the message produced if there is no address.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	static final String MISSING_ADDRESS_ERROR = "An address must be provided";

	/**
	 * the voting precinct service.
	 * 
	 * @author IanBrown
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	@Autowired
	private VotingPrecinctService votingPrecinctService;

	/**
	 * Gets the voting precinct service.
	 * 
	 * @author IanBrown
	 * @return the voting precinct service.
	 * @since Aug 14, 2012
	 * @version Aug 14, 2012
	 */
	public VotingPrecinctService getVotingPrecinctService() {
		return votingPrecinctService;
	}

	/**
	 * Is the input state or voting region supported?
	 * 
	 * @author IanBrown
	 * @param stateIdentifier
	 *            the state identifier.
	 * @param votingRegionName
	 *            the optional voting region name.
	 * @return <code>true</code> if the state or voting region is supported, <code>false</code> otherwise.
	 * @since Aug 13, 2012
	 * @version Oct 9, 2012
	 */
	public boolean isSupported(final String stateIdentifier, final String votingRegionName) {
		boolean supported = false;

		if (stateIdentifier != null) {
			supported = getVotingPrecinctService().isSupported(stateIdentifier, null);
		}

		return supported;
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
	public void setVotingPrecinctService(final VotingPrecinctService votingPrecinctService) {
		this.votingPrecinctService = votingPrecinctService;
	}

	/** {@inheritDoc} */
	@Override
	public boolean supports(final Class<?> clazz) {
		return WhatsOnMyBallotForm.class.isAssignableFrom(clazz);
	}

	/**
	 * Validates the input form.
	 * 
	 * @author IanBrown
	 * @param errors
	 *            the errors object - may be <code>null</code>.
	 * @param whatsOnMyBallot
	 *            the what's on my ballot? form to validate.
	 * @return the validated address or <code>null</code> if it isn't valid.
	 * @since Aug 13, 2012
	 * @version Aug 31, 2012
	 */
	public ValidAddress validate(final Errors errors, final WhatsOnMyBallotForm whatsOnMyBallot) {
		final UserAddress address = whatsOnMyBallot == null ? null : whatsOnMyBallot.getAddress();
		if (address != null) {
			final State votingState = whatsOnMyBallot.getVotingState();
			final String state = address.getState();
			if (state == null || state.trim().isEmpty()) {
				address.setState(votingState.getAbbr());
			}
			final ValidAddress validAddress = getVotingPrecinctService().validateAddress(address, votingState);
			if (validAddress != null) {
				return validAddress;

			} else if (errors != null) {
				errors.rejectValue("address", "", INVALID_ADDRESS_ERROR);
			}

		} else if (errors != null) {
			errors.rejectValue("address", "", MISSING_ADDRESS_ERROR);
		}

		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void validate(final Object target, final Errors errors) {
		validate(errors, (WhatsOnMyBallotForm) target);
	}
}
