package com.bearcode.ovf.tools.pdf.generator.propagators;

import com.bearcode.ovf.actions.questionnaire.forms.WizardContext;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.AbstractLocalOfficial;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.StateService;
import com.bearcode.ovf.tools.pdf.PdfGenerator;
import com.bearcode.ovf.tools.pdf.generator.TerminalModel;
import com.bearcode.ovf.webservices.eod.EodApiService;
import com.bearcode.ovf.webservices.eod.model.EodAddress;
import com.bearcode.ovf.webservices.eod.model.EodAddressFunction;
import com.bearcode.ovf.webservices.eod.model.EodRegion;
import com.bearcode.ovf.webservices.eod.model.LocalOffice;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Date: 03.10.14
 * Time: 19:07
 *
 * @author Leonid Ginzburg
 */
@Component
public class RegionFieldsPropagator extends FieldPropagator {

    @Autowired
    private StateService stateService;

    @Autowired
    private EodApiService eodApiService;

    @Override
    public void propagate(TerminalModel model, WizardContext wizardContext) {
        final LocalOffice leo = getOfficial( wizardContext );

        if ( leo != null ) {
            //get appropriate address
            EodAddress appropriateAddress = findAppropriateAddress( leo, wizardContext.getFlowType() );
            if ( appropriateAddress != null ) {
                model.getFormFields().put( PdfGenerator.USER_FIELD_LEO_ADDRESS, getLeoEnvelopeAddress(leo, appropriateAddress));
                //get contact from address
                Officer addressContact = findAddressContact( leo, appropriateAddress );
                if ( addressContact != null ) {
                    model.getFormFields().put( PdfGenerator.USER_FIELD_LEO_EMAIL, appropriateAddress.getMainEmail() /*addressContact.getEmail()*/ );
                    model.getFormFields().put(PdfGenerator.USER_FIELD_LEO_PERSON, getOfficerInfo( addressContact ) );

                    model.getFormFields().put(PdfGenerator.USER_FIELD_LEO_NAME, addressContact.getFirstName() + " " + addressContact.getLastName() );
                    model.getFormFields().put(PdfGenerator.USER_FIELD_LEO_PHONE, StringUtils.isEmpty( appropriateAddress.getMainPhoneNumber() ) ? "" : appropriateAddress.getMainPhoneNumber() );
                    model.getFormFields().put(PdfGenerator.USER_FIELD_LEO_FAX, StringUtils.isEmpty( appropriateAddress.getMainFaxNumber() ) ? "" : appropriateAddress.getMainFaxNumber() );
                    //????
                    model.getFormFields().put(PdfGenerator.USER_FIELD_LOVC_PERSON, getOfficerInfo(leo.findSecondary()));
                }

            }

/*
            model.getFormFields().put( PdfGenerator.USER_FIELD_LEO_ADDRESS, getLeoEnvelopeAddress(leo));
            String email = leo.getGeneralEmail();
            if ( email == null || email.isEmpty() ) {
                email = leo.getLeoEmail();
            }
            model.getFormFields().put( PdfGenerator.USER_FIELD_LEO_EMAIL, email != null ? email : "" );
            model.getFormFields().put(PdfGenerator.USER_FIELD_LEO_PERSON, getOfficerInfo(leo.findPrimary()) );
            model.getFormFields().put(PdfGenerator.USER_FIELD_LOVC_PERSON, getOfficerInfo(leo.findSecondary()));
*/
            model.setLocalOfficial( leo );
        }
    }

    private EodAddress findAppropriateAddress( LocalOffice leo, FlowType flowType ) {
        EodAddressFunction function;
        switch ( flowType ) {
            case RAVA:
                function = EodAddressFunction.OVS_REQ;
                break;
            case FWAB:
                function = EodAddressFunction.OVS_RET;
                break;
            case DOMESTIC_REGISTRATION:
                function = EodAddressFunction.DOM_VR;
                break;
            case DOMESTIC_ABSENTEE:
                function = EodAddressFunction.DOM_REQ;
                break;
            default:
                return null; //can't be
        }
        Set<EodAddress> firstFiltered = new HashSet<EodAddress>();
        firstFiltered.addAll( leo.getAddresses() );
        //firstFiltered.add( (EodAddress) leo.getPhysical() );
        //firstFiltered.add( (EodAddress) leo.getMailing() );
        // filter by type
        for ( Iterator<EodAddress> it = firstFiltered.iterator(); it.hasNext(); ) {
            EodAddress address = it.next();
            if ( !address.hasFunction( function ) ) {
                it.remove();
            }
        }
        if ( firstFiltered.size() > 1 ) {
            // get regular mail address
            for ( EodAddress foundAddress : firstFiltered ) {
                if ( foundAddress.isRegularMail() ) {
                    return foundAddress;
                }
            }
        }
        else if ( firstFiltered.size() == 1 ) {
            return firstFiltered.iterator().next();
        }
        // nothing found ... :(
        return null;
    }

    private Officer findAddressContact( LocalOffice leo, EodAddress appropriateAddress ) {
        Pattern pattern = Pattern.compile( "\\d+$" );
        Matcher matcher = pattern.matcher( appropriateAddress.getPrimaryContactUri() );
        if ( matcher.find() ) {
            String strContactId = matcher.group();
            Long contactId = Long.parseLong( strContactId );
            for ( Officer contact : leo.getOfficers() ) {
                if ( contact.getId() == contactId ) {
                    return contact;
                }
            }
        }
        return null;
    }

    protected LocalOffice getOfficial( WizardContext wizardContext ) {
        final String votingRegionStateName = wizardContext.getWizardResults().getVotingRegionState();
        WizardResults wizardResults = wizardContext.getWizardResults();
        if ( StringUtils.isEmpty( wizardResults.getEodRegionId() ) ) {
            if (logger.isDebugEnabled()) {
                logger.debug("Voting region is null");
            }
            return null;
        }

        if ( wizardContext.getFlowType() == FlowType.RAVA || wizardContext.getFlowType() == FlowType.FWAB ) {
            EodRegion uocavaRegion = eodApiService.findUocavaRegion( votingRegionStateName );
            LocalOffice uocava = uocavaRegion != null ? eodApiService.getLocalOffice( uocavaRegion.getId().toString(), true ) : null;
            if ( uocava != null ) {
                return uocava;
            }
        }
        LocalOffice leo = eodApiService.getLocalOffice( wizardResults.getEodRegionId(), true );
        if (leo == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("LocalOfficial is null");
            }
            return null;
        }
        return leo;
    }

    /**
   	 * Gets the envelope address from the local election official.
   	 *
   	 * @author IanBrown
   	 * @param leo
   	 *            the local election official.
   	 * @return the envelope address.
   	 * @since Mar 21, 2012
   	 * @version May 10, 2012
   	 */
   	protected String getLeoEnvelopeAddress(final LocalOffice leo) {
   		final Address mailingTo = leo.getMailing();
   		final StringBuilder mailing = new StringBuilder();
   		mailing.append(mailingTo.getAddressTo()).append(", ").append(leo.getRegion().getName()).append("\n")
   				.append(mailingTo.getStreet1());

   		if (!mailingTo.getStreet2().isEmpty()) {
   			mailing.append(", ").append(mailingTo.getStreet2());
   		}

   		mailing.append("\n").append(mailingTo.getCity()).append(", ").append(mailingTo.getState());

   		if (!mailingTo.getZip().isEmpty()) {
   			mailing.append(" ").append(mailingTo.getZip());
   			if (mailingTo.getZip4().length() > 0) {
   				mailing.append("-").append( mailingTo.getZip4() );
   			}
   		}
   		return mailing.toString();
   	}

    protected String getLeoEnvelopeAddress(final LocalOffice leo, final Address mailingTo) {
        final StringBuilder mailing = new StringBuilder();
        mailing.append(mailingTo.getAddressTo()).append(", ").append(leo.getRegion().getName()).append("\n")
                .append(mailingTo.getStreet1());

        if (!mailingTo.getStreet2().isEmpty()) {
            mailing.append("\n").append(mailingTo.getStreet2());
        }

        mailing.append("\n").append(mailingTo.getCity()).append(", ").append(mailingTo.getState());

        if (!mailingTo.getZip().isEmpty()) {
            mailing.append(" ").append(mailingTo.getZip());
            if (mailingTo.getZip4().length() > 0) {
                mailing.append("-").append(mailingTo.getZip4());
            }
        }
        mailing.append( "\nUSA" );
        return mailing.toString();
    }


    /**
   	 * Gets the person from the local election official.
   	 *
   	 * @author IanBrown
   	 * @param officer
   	 *            the local election officer.
   	 * @return the string with all person's info.
   	 * @since Mar 21, 2012
   	 * @version Mar 21, 2012
     */
    private String getOfficerInfo( final Officer officer ) {
        final StringBuilder person = new StringBuilder();
        if ( officer != null ) {
            person.append(officer.getFirstName()).append(" ").append(officer.getLastName()).append("\n");

            if ( officer.getPhone() != null && !officer.getPhone().isEmpty() ) {
                person.append("Phone: ").append(officer.getPhone()).append("\n");
            }

            if ( officer.getFax() != null && !officer.getFax().isEmpty() ) {
                person.append("Fax: ").append(officer.getFax()).append("\n");
            }

            if ( officer.getEmail() != null && !officer.getEmail().isEmpty() ) {
                person.append("Email: ").append(officer.getEmail()).append("\n");
            }
        }
        return person.toString();
    }


}
