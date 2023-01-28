/**
 * 
 */
package com.bearcode.ovf.actions.questionnaire;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Extended {@link AbstractFieldFillerPageAddOn} that allows the user to select how he or she will send the information. The add on
 * automatically fills in local official address information.
 * 
 * @author IanBrown
 * 
 * @since Apr 23, 2012
 * @version Jul 30, 2012
 */
@Component
public class TransmissionPageAddOn extends AbstractFieldFillerPageAddOn {

	/**
	 * the title for the email transmission field.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public static final String TRANSMISSION_EMAIL_FIELD = "Transmission Email Address";

	/**
	 * the title for the fax transmission field.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public static final String TRANSMISSION_FAX_FIELD = "Transmission Fax Address";

	/**
	 * the title for the mail transmission field.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public static final String TRANSMISSION_MAIL_FIELD = "Transmission Mail Address";

	/**
	 * the title for the full name field.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public static final String TRANSMISSION_NAME_FIELD = "Transmission Full Name";

	/**
	 * the service used to get information about the local official.
	 * 
	 * @author IanBrown
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	@Autowired
	private LocalOfficialService localOfficialService;

    @Autowired
    private StateService stateService;

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Apr 23, 2012
	 * @version Apr 23, 2012
	 */
	public void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}

    public StateService getStateService() {
        return stateService;
    }

    public void setStateService( final StateService stateService ) {
        this.stateService = stateService;
    }

    /** {@inheritDoc} */
	@Override
	protected void prepareAddOnVariant(final WizardContext form, final QuestionVariant variant) {
		final Collection<QuestionField> fields = variant.getFields();

		if (fields != null) {
			final WizardResults wizardResults = form.getWizardResults();
			final VotingRegion votingRegion = wizardResults.getVotingRegion();
			if (votingRegion != null) {
				LocalOfficial localOfficial = getLocalOfficialService().findForRegion(votingRegion);
                if ( form.getFlowType() == FlowType.RAVA || form.getFlowType() == FlowType.FWAB ) {
                   LocalOfficial uocava = getStateService().findUocavaOfficeForState(votingRegion.getState());
                   if ( uocava != null ) {
                       localOfficial = uocava;
                   }
               }
				if (localOfficial != null) {
					for (final QuestionField field : fields) {
						prepareAddOnField(localOfficial, field);
					}
				}
			}
		}
	}

	/**
	 * Prepares the add on field by filling in information from the local official if the field calls for it.
	 * 
	 * @author IanBrown
	 * @param localOfficial
	 *            the local official.
	 * @param field
	 *            the field.
	 * @since Apr 23, 2012
	 * @version Jul 30, 2012
	 */
	private void prepareAddOnField(final LocalOfficial localOfficial, final QuestionField field) {
		if (FieldType.TEMPLATE_NOT_INPUT.equals(field.getType().getTemplateName())) {
			final String fieldTitle = field.getTitle();

			if (TRANSMISSION_NAME_FIELD.equals(fieldTitle)) {
				final Person leo = localOfficial.getLeo();
				field.setHelpText(leo.getFirstName() + " " + leo.getLastName());
			} else if (TRANSMISSION_EMAIL_FIELD.equals(fieldTitle)) {
				field.setHelpText(localOfficial.getLeoEmail());
			} else if (TRANSMISSION_FAX_FIELD.equals(fieldTitle)) {
				field.setHelpText("+1 " + localOfficial.getLeoFax());
			} else if (TRANSMISSION_MAIL_FIELD.equals(fieldTitle)) {
				final Address mailingAddress = localOfficial.getMailing();
				final StringBuilder sb = new StringBuilder(mailingAddress.getFullStreet());
				sb.append("<br>\n").append(mailingAddress.getCity()).append(", ").append(mailingAddress.getState()).append(" ")
						.append(mailingAddress.getZip());
				final String zip4 = mailingAddress.getZip4();
				if ((zip4 != null) && !zip4.trim().isEmpty()) {
					sb.append("-").append(zip4);
				}
                sb.append(" USA");
				field.setHelpText(sb.toString());
			}
		}
	}

}
