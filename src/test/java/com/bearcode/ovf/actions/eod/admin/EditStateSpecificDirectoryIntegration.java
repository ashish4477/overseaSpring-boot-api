/**
 * 
 */
package com.bearcode.ovf.actions.eod.admin;

import com.bearcode.ovf.actions.commons.BaseControllerExam;
import com.bearcode.ovf.actions.commons.OverseasFormControllerExam;
import com.bearcode.ovf.dbunittest.OVFDBUnitDataSet;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.eod.DeliveryOptions;
import com.bearcode.ovf.model.eod.StateSpecificDirectory;
import com.bearcode.ovf.service.FacesService;
import com.bearcode.ovf.service.LocalOfficialService;
import com.bearcode.ovf.service.StateService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Extended {@link OverseasFormControllerExam} integration test for {@link EditStateSpecificDirectory}.
 * 
 * @author IanBrown
 * 
 * @since Jan 13, 2012
 * @version Jan 18, 2012
 */
@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "../../commons/OverseasFormController-context.xml",
		"EditStateSpecificDirectoryIntegration-context.xml" })
public final class EditStateSpecificDirectoryIntegration extends BaseControllerExam<EditStateSpecificDirectory> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Autowired
	private LocalOfficialService localOfficialService;


	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.EditStateSpecificDirectory#showForm(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap)}
	 * for a get.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/eod/admin/EditStateSpecificDirectory.xml" })
	public final void testHandleRequest_get() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final long stateId = 1l;
		request.setMethod("GET");
		request.setParameter("stateId", Long.toString(stateId));

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		assertNotNull("There is a model map", actualModelAndView.getModelMap());
		final ModelMap actualModelMap = actualModelAndView.getModelMap();
		assertNotNull("A SVID is returned in the model as the svid", actualModelMap.get("svid"));
		final StateSpecificDirectory svid = (StateSpecificDirectory) actualModelMap.get("svid");
		assertNotNull("The SVID belongs to a state", svid.getState());
		assertEquals("The SVID belongs to the correct state", 1l, svid.getState().getId().longValue());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.eod.admin.EditStateSpecificDirectory#onSubmit(javax.servlet.http.HttpServletRequest, org.springframework.ui.ModelMap, com.bearcode.ovf.model.eod.StateSpecificDirectory, org.springframework.validation.BindingResult)}
	 * for a post.
	 * 
	 * @author IanBrown
	 * @throws Exception
	 *             if there is a problem handling the request.
	 * @since Jan 13, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	@OVFDBUnitDataSet(dataSetList = { "com/bearcode/ovf/actions/eod/admin/EditStateSpecificDirectory.xml" })
	public final void testHandleRequest_post() throws Exception {
		final MockHttpServletRequest request = new MockHttpServletRequest();
		final MockHttpServletResponse response = new MockHttpServletResponse();
		final long stateId = 1l;
		final String website = "http://www.bearcode.com";
		final Address mailing = createAddress("mailing", "mailing 1", "mailing 2", "mailing City", "MS", "12345", "6789");
		final Address physical = createAddress("physical", "physical 1", "physical 2", "physical City", "PS", "23456", "7890");
		final String statePhone = "345-678-9012";
		final String stateFax = "456-789-0123";
		final String contactNotes = "Contact Notes";
		final String confirmationOrStatus = "Confirmation or Status";
		final DeliveryOptions citizenRegistration = createDeliveryOptions(false, true, false, true, false);
		final DeliveryOptions citizenBallotRequest = createDeliveryOptions(true, false, true, false, false);
		final DeliveryOptions citizenBlankBallot = createDeliveryOptions(false, false, false, false, false);
		final DeliveryOptions citizenBallotReturn = createDeliveryOptions(true, true, true, true, false);
		final String citizenNotes = "Citizen Notes";
		final DeliveryOptions militaryRegistration = createDeliveryOptions(false, false, true, true, false);
		final DeliveryOptions militaryBallotRequest = createDeliveryOptions(true, true, false, false, false);
		final DeliveryOptions militaryBlankBallot = createDeliveryOptions(false, false, false, true, false);
		final DeliveryOptions militaryBallotReturn = createDeliveryOptions(true, true, true, false, false);
		final String militaryNotes = "Military Notes";
		final DeliveryOptions domesticRegistration = createDeliveryOptions(false, false, false, true, true);
		final DeliveryOptions domesticBallotRequest = createDeliveryOptions(false, false, true, false, true);
		final DeliveryOptions domesticBallotReturn = createDeliveryOptions(true, false, false, false, false);
		final String domesticNotes = "Domestic Notes";
		final String voterRegistrationRequirements = "Voter Registration Requirements";
		final String earlyVotingRequirements = "Early Voting Requirements";
		final String absenteeBallotRequestRequirements = "Absentee Ballot Request Requirements";
		final String voterRegistrationIdentificationRequirements = "Voter Registration Identification Requirements";
		final String earlyVotingIdentificationRequirements = "Early Voting Identification Requirements";
		final String absenteeBallotRequestIdentificationRequirements = "Absentee Ballot Request Identification Requirements";
		final String votingInPersonIdentificationRequirements = "Voting In-Person Identification Requirements";
		final String adminNotes = "Admin Notes";
		request.setMethod("POST");
		request.addParameter("stateId", Long.toString(stateId));
		request.addParameter("submission", "true");
		request.addParameter("website", website);
		addAddressToRequest("mailing", mailing, request);
		addAddressToRequest("physical", physical, request);
		request.addParameter("statePhone", statePhone);
		request.addParameter("stateFax", stateFax);
		request.addParameter("contactNotes", contactNotes);
		request.addParameter("confirmationOrStatus", confirmationOrStatus);
		addDeliveryOptionsToRequest("citizenRegistration", citizenRegistration, request);
		addDeliveryOptionsToRequest("citizenBallotRequest", citizenBallotRequest, request);
		addDeliveryOptionsToRequest("citizenBlankBallot", citizenBlankBallot, request);
		addDeliveryOptionsToRequest("citizenBallotReturn", citizenBallotReturn, request);
		request.addParameter("citizenNotes", citizenNotes);
		addDeliveryOptionsToRequest("militaryRegistration", militaryRegistration, request);
		addDeliveryOptionsToRequest("militaryBallotRequest", militaryBallotRequest, request);
		addDeliveryOptionsToRequest("militaryBlankBallot", militaryBlankBallot, request);
		addDeliveryOptionsToRequest("militaryBallotReturn", militaryBallotReturn, request);
		request.addParameter("militaryNotes", militaryNotes);
		addDeliveryOptionsToRequest("domesticRegistration", domesticRegistration, request);
		addDeliveryOptionsToRequest("domesticBallotRequest", domesticBallotRequest, request);
		addDeliveryOptionsToRequest("domesticBallotReturn", domesticBallotReturn, request);
		request.addParameter("domesticNotes", domesticNotes);
		request.addParameter("voterRegistrationRequirements", voterRegistrationRequirements);
		request.addParameter("earlyVotingRequirements", earlyVotingRequirements);
		request.addParameter("absenteeBallotRequestRequirements", absenteeBallotRequestRequirements);
		request.addParameter("voterRegistrationIdentificationRequirements", voterRegistrationIdentificationRequirements);
		request.addParameter("earlyVotingIdentificationRequirements", earlyVotingIdentificationRequirements);
		request.addParameter("absenteeBallotRequestIdentificationRequirements", absenteeBallotRequestIdentificationRequirements);
		request.addParameter("votingInPersonIdentificationRequirements", votingInPersonIdentificationRequirements);
		request.addParameter("adminNotes", adminNotes);

		final ModelAndView actualModelAndView = getHandlerAdapter().handle(request, response, getBaseController());

		assertNotNull("A model and view is returned", actualModelAndView);
		assertEquals("The view is the main template", ReflectionTestUtils.getField(getBaseController(), "mainTemplate"),
				actualModelAndView.getViewName());
		assertNotNull("There is a model map", actualModelAndView.getModelMap());
		final ModelMap actualModelMap = actualModelAndView.getModelMap();
		assertNotNull("A SVID is returned in the model as the svid", actualModelMap.get("svid"));
		final StateSpecificDirectory svid = (StateSpecificDirectory) actualModelMap.get("svid");
		assertNotNull("The SVID belongs to a state", svid.getState());
		assertEquals("The SVID belongs to the correct state", 1l, svid.getState().getId().longValue());
		assertEquals("The website is set", website, svid.getWebsite());
		assertAddress("mailing", mailing, svid.getMailing());
		assertAddress("physical", physical, svid.getPhysical());
		assertEquals("The state phone is set", statePhone, svid.getStatePhone());
		assertEquals("The state fax is set", stateFax, svid.getStateFax());
		assertEquals("The contact notes are set", contactNotes, svid.getContactNotes());
		assertEquals("The confirmation or status value is set", confirmationOrStatus, svid.getConfirmationOrStatus());
		assertDeliveryOptions("citizenRegistration", citizenRegistration, svid.getCitizenRegistration());
		assertDeliveryOptions("citizenBallotRequest", citizenBallotRequest, svid.getCitizenBallotRequest());
		assertDeliveryOptions("citizenBlankBallot", citizenBlankBallot, svid.getCitizenBlankBallot());
		assertDeliveryOptions("citizenBallotReturn", citizenBallotReturn, svid.getCitizenBallotReturn());
		assertEquals("The citizen notes are set", citizenNotes, svid.getCitizenNotes());
		assertDeliveryOptions("militaryRegistration", militaryRegistration, svid.getMilitaryRegistration());
		assertDeliveryOptions("militaryBallotRequest", militaryBallotRequest, svid.getMilitaryBallotRequest());
		assertDeliveryOptions("militaryBlankBallot", militaryBlankBallot, svid.getMilitaryBlankBallot());
		assertDeliveryOptions("militaryBallotReturn", militaryBallotReturn, svid.getMilitaryBallotReturn());
		assertEquals("The military notes are set", militaryNotes, svid.getMilitaryNotes());
		assertDeliveryOptions("domesticRegistration", domesticRegistration, svid.getDomesticRegistration());
		assertDeliveryOptions("domesticBallotRequest", domesticBallotRequest, svid.getDomesticBallotRequest());
		assertDeliveryOptions("domesticBallotReturn", domesticBallotReturn, svid.getDomesticBallotReturn());
		assertEquals("The domestic notes are set", domesticNotes, svid.getDomesticNotes());
		assertEquals("The voter registration requirements are set", voterRegistrationRequirements,
				svid.getVoterRegistrationRequirements());
		assertEquals("The early voting requirements are set", earlyVotingRequirements, svid.getEarlyVotingRequirements());
		assertEquals("The absentee ballot request requirements are set", absenteeBallotRequestRequirements,
				svid.getAbsenteeBallotRequestRequirements());
		assertEquals("The voter registration identification requirements are set", voterRegistrationIdentificationRequirements,
				svid.getVoterRegistrationIdentificationRequirements());
		assertEquals("The early voting identification requirements are set", earlyVotingIdentificationRequirements,
				svid.getEarlyVotingIdentificationRequirements());
		assertEquals("The absentee ballot request identification requirements are set",
				absenteeBallotRequestIdentificationRequirements, svid.getAbsenteeBallotRequestIdentificationRequirements());
		assertEquals("The voting in person identification requirements are set", votingInPersonIdentificationRequirements,
				svid.getVotingInPersonIdentificationRequirements());
		assertEquals("The admin notes are set", adminNotes, svid.getAdminNotes());
	}

	/**
	 * Adds the address to the request.
	 * 
	 * @author IanBrown
	 * @param addressName
	 *            the name of the address.
	 * @param address
	 *            the address.
	 * @param request
	 *            the request.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private void addAddressToRequest(final String addressName, final Address address, final MockHttpServletRequest request) {
		request.addParameter(addressName + ".addressTo", address.getAddressTo());
		request.addParameter(addressName + ".street1", address.getStreet1());
		request.addParameter(addressName + ".street2", address.getStreet2());
		request.addParameter(addressName + ".city", address.getCity());
		request.addParameter(addressName + ".state", address.getState());
		request.addParameter(addressName + ".zip", address.getZip());
		request.addParameter(addressName + ".zip4", address.getZip4());
	}

	/**
	 * Adds the delivery options to the request.
	 * 
	 * @author IanBrown
	 * @param deliveryOptionsName
	 *            the name of the delivery options.
	 * @param deliveryOptions
	 *            the delivery options.
	 * @param request
	 *            the requesst.
	 * @since Jan 13, 2012
	 * @version Jan 18, 2012
	 */
	private void addDeliveryOptionsToRequest(final String deliveryOptionsName, final DeliveryOptions deliveryOptions,
			final MockHttpServletRequest request) {
		request.addParameter(deliveryOptionsName + ".post", Boolean.toString(deliveryOptions.isPost()));
		request.addParameter(deliveryOptionsName + ".fax", Boolean.toString(deliveryOptions.isFax()));
		request.addParameter(deliveryOptionsName + ".inPerson", Boolean.toString(deliveryOptions.isInPerson()));
		request.addParameter(deliveryOptionsName + ".email", Boolean.toString(deliveryOptions.isEmail()));
		request.addParameter(deliveryOptionsName + ".tel", Boolean.toString(deliveryOptions.isTel()));
	}

	/**
	 * Custom assertion to ensure that the address is set correctly.
	 * 
	 * @author IanBrown
	 * @param addressName
	 *            the name of the address.
	 * @param expectedAddress
	 *            the expected address.
	 * @param actualAddress
	 *            the actual address.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private void assertAddress(final String addressName, final Address expectedAddress, final Address actualAddress) {
		assertEquals("The " + addressName + " address to value is correct", expectedAddress.getAddressTo(),
				actualAddress.getAddressTo());
		assertEquals("The " + addressName + " street1 value is set", expectedAddress.getStreet1(), actualAddress.getStreet1());
		assertEquals("The " + addressName + " street2 value is set", expectedAddress.getStreet2(), actualAddress.getStreet2());
		assertEquals("The " + addressName + " city value is set", expectedAddress.getCity(), actualAddress.getCity());
		assertEquals("The " + addressName + " state value is set", expectedAddress.getState(), actualAddress.getState());
		assertEquals("The " + addressName + " ZIP value is set", expectedAddress.getZip(), actualAddress.getZip());
		assertEquals("The " + addressName + " ZIP+4 value is set", expectedAddress.getZip4(), actualAddress.getZip4());
	}

	/**
	 * Custom assertion to ensure that the delivery options are correct.
	 * 
	 * @author IanBrown
	 * @param deliveryOptionsName
	 *            the name of the delivery options.
	 * @param expectedDeliveryOptions
	 *            the expected delivery options.
	 * @param actualDeliveryOptions
	 *            the actual delivery options.
	 * @since Jan 13, 2012
	 * @version Jan 18, 2012
	 */
	private void assertDeliveryOptions(final String deliveryOptionsName, final DeliveryOptions expectedDeliveryOptions,
			final DeliveryOptions actualDeliveryOptions) {
		assertEquals("The " + deliveryOptionsName + " post flag is set", expectedDeliveryOptions.isPost(),
				actualDeliveryOptions.isPost());
		assertEquals("The " + deliveryOptionsName + " fax flag is set", expectedDeliveryOptions.isFax(),
				actualDeliveryOptions.isFax());
		assertEquals("The " + deliveryOptionsName + " in-person flag is set", expectedDeliveryOptions.isInPerson(),
				actualDeliveryOptions.isInPerson());
		assertEquals("The " + deliveryOptionsName + " email flag is set", expectedDeliveryOptions.isEmail(),
				actualDeliveryOptions.isEmail());
		assertEquals("The " + deliveryOptionsName + " tel flag is set", expectedDeliveryOptions.isTel(),
				actualDeliveryOptions.isTel());
	}

	/**
	 * Creates an address.
	 * 
	 * @author IanBrown
	 * @param addressTo
	 *            the addressee.
	 * @param street1
	 *            the first street address.
	 * @param street2
	 *            the second street address.
	 * @param city
	 *            the city.
	 * @param state
	 *            the state.
	 * @param zip
	 *            the ZIP code.
	 * @param zip4
	 *            the ZIP+4 code.
	 * @return the address.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private Address createAddress(final String addressTo, final String street1, final String street2, final String city,
			final String state, final String zip, final String zip4) {
		final Address address = new Address();
		address.setAddressTo(addressTo);
		address.setStreet1(street1);
		address.setStreet2(street2);
		address.setCity(city);
		address.setState(state);
		address.setZip(zip);
		address.setZip4(zip4);
		return address;
	}

	/**
	 * Creates a delivery options.
	 * 
	 * @author IanBrown
	 * @param post
	 *            the post flag.
	 * @param fax
	 *            the fax flag.
	 * @param email
	 *            the email flag.
	 * @param tel
	 *            the telephone flag.
	 * @param inPerson
	 *            the in-person flag.
	 * @return the delivery options.
	 * @since Jan 13, 2012
	 * @version Jan 18, 2012
	 */
	private DeliveryOptions createDeliveryOptions(final boolean post, final boolean fax, final boolean email, final boolean tel,
			final boolean inPerson) {
		final DeliveryOptions deliveryOptions = new DeliveryOptions();
		deliveryOptions.setPost(post);
		deliveryOptions.setFax(fax);
		deliveryOptions.setEmail(email);
		deliveryOptions.setTel(tel);
		deliveryOptions.setInPerson(inPerson);
		return deliveryOptions;
	}

    @Override
    protected EditStateSpecificDirectory createBaseController() {
        final EditStateSpecificDirectory stateSpecificDirectory = applicationContext.getBean(EditStateSpecificDirectory.class);
        stateSpecificDirectory.setLocalOfficialService(getLocalOfficialService());
        return stateSpecificDirectory;
    }


    @Override
    protected void setUpForBaseController() {
    }

    @Override
    protected void tearDownForBaseController() {
    }

    /**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Jan 13, 2012
	 * @version Jan 13, 2012
	 */
	private LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

}
