/**
 * 
 */
package com.bearcode.ovf.actions.eod;

import com.bearcode.ovf.actions.commons.BaseController;
import com.bearcode.ovf.actions.commons.BaseControllerCheck;
import com.bearcode.ovf.actions.commons.OverseasFormControllerCheck;
import com.bearcode.ovf.model.common.Address;
import com.bearcode.ovf.model.common.Person;
import com.bearcode.ovf.model.eod.Officer;
import com.bearcode.ovf.service.LocalOfficialService;
import org.easymock.EasyMock;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;

/**
 * Abstract extended {@link OverseasFormControllerCheck} test for corrections
 * controllers.
 * 
 * @author IanBrown
 * 
 * @param <C>
 * @since Dec 23, 2011
 * @version Dec 23, 2011
 */
public abstract class CorrectionsControllerCheck<C extends BaseController> extends BaseControllerCheck<C> {

	/**
	 * the local official service.
	 * 
	 * @author IanBrown
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private LocalOfficialService localOfficialService;

	/**
	 * Custom assertion to check that the address is correct.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the address.
	 * @param expectedAddress
	 *            the expected values for the address.
	 * @param actualAddress
	 *            the actual values for the address.
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	protected final void assertAddress(final String name, final Address expectedAddress, final Address actualAddress) {
		assertEquals("The " + name + " address to is set", expectedAddress.getAddressTo(), actualAddress.getAddressTo());
		assertEquals("The " + name + " city is set", expectedAddress.getCity(), actualAddress.getCity());
		assertEquals("The " + name + " state is set", expectedAddress.getState(), actualAddress.getState());
		assertEquals("The " + name + " street 1 is set", expectedAddress.getStreet1(), actualAddress.getStreet1());
		assertEquals("The " + name + " street 2 is set", expectedAddress.getStreet2(), actualAddress.getStreet2());
		assertEquals("The " + name + " ZIP is set", expectedAddress.getZip(), actualAddress.getZip());
		assertEquals("The " + name + " ZIP4 is set", expectedAddress.getZip4(), actualAddress.getZip4());
	}

	/**
	 * Custom assertion to ensure that the person is correct.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name of the person.
	 * @param expectedPerson
	 *            the expected values for the person.
	 * @param actualPerson
	 *            the actual values for the person.
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	protected final void assertPerson(final String name, final Person expectedPerson, final Person actualPerson) {
		assertEquals(name + "'s first name is set", expectedPerson.getFirstName(), actualPerson.getFirstName());
		assertEquals(name + "'s initial is set", expectedPerson.getInitial(), actualPerson.getInitial());
		assertEquals(name + "'s last name is set", expectedPerson.getLastName(), actualPerson.getLastName());
		assertEquals(name + "'s suffix is set", expectedPerson.getSuffix(), actualPerson.getSuffix());
		assertEquals(name + "'s title is set", expectedPerson.getTitle(), actualPerson.getTitle());
	}

    /**
     * Custom assertion to ensure that the officer is correct.
     *
     * @author IanBrown
     * @param name
     *            the name of the person.
     * @param expectedPerson
     *            the expected values for the person.
     * @param actualPerson
     *            the actual values for the person.
     * @since Dec 7, 2012
     */
    protected final void assertOfficer(final String name, final Officer expectedPerson, final Officer actualPerson) {
        assertEquals(name + "'s first name is set", expectedPerson.getFirstName(), actualPerson.getFirstName());
        assertEquals(name + "'s initial is set", expectedPerson.getInitial(), actualPerson.getInitial());
        assertEquals(name + "'s last name is set", expectedPerson.getLastName(), actualPerson.getLastName());
        assertEquals(name + "'s suffix is set", expectedPerson.getSuffix(), actualPerson.getSuffix());
        assertEquals(name + "'s title is set", expectedPerson.getTitle(), actualPerson.getTitle());
        assertEquals(name + "'s email is set", expectedPerson.getEmail(), actualPerson.getEmail());
        assertEquals(name + "'s phone is set", expectedPerson.getPhone(), actualPerson.getPhone());
        assertEquals(name + "'s fax is set", expectedPerson.getFax(), actualPerson.getFax());
    }
	/**
	 * Creates an address with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the address.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	protected final Address createAddress(final String name) {
		final Address address = createMock(name, Address.class);
		final String addressTo = name;
		EasyMock.expect(address.getAddressTo()).andReturn(addressTo).atLeastOnce();
		final String street1 = name + " Street 1";
		EasyMock.expect(address.getStreet1()).andReturn(street1).atLeastOnce();
		final String street2 = name + " Street 2";
		EasyMock.expect(address.getStreet2()).andReturn(street2).atLeastOnce();
		final String city = name + " City";
		EasyMock.expect(address.getCity()).andReturn(city).atLeastOnce();
		final String state = "State of " + name;
		EasyMock.expect(address.getState()).andReturn(state).atLeastOnce();
		final String zip = name + " ZIP";
		EasyMock.expect(address.getZip()).andReturn(zip).atLeastOnce();
		final String zip4 = "ZIP4 " + name;
		EasyMock.expect(address.getZip4()).andReturn(zip4).atLeastOnce();
		return address;
	}

	/**
	 * Creates a corrections controller of the type to test.
	 * 
	 * @author IanBrown
	 * @return the corrections controller.
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	protected abstract C createCorrectionsController();

    @Override
    protected final C createBaseController() {
		final C correctionsController = createCorrectionsController();
		ReflectionTestUtils.invokeSetterMethod(correctionsController, "localOfficialService", getLocalOfficialService());
		return correctionsController;
	}

	/**
	 * Creates a person with the specified name.
	 * 
	 * @author IanBrown
	 * @param name
	 *            the name.
	 * @return the person.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	protected final Person createPerson(final String name) {
		final Person person = createMock(name.replace(" ", ""), Person.class);
		final String title = name + "Title";
		EasyMock.expect(person.getTitle()).andReturn(title).atLeastOnce();
		final String firstName = name + "First";
		EasyMock.expect(person.getFirstName()).andReturn(firstName).atLeastOnce();
		final String initial = name.substring(0);
		EasyMock.expect(person.getInitial()).andReturn(initial).atLeastOnce();
		final String lastName = name + "Last";
		EasyMock.expect(person.getLastName()).andReturn(lastName).atLeastOnce();
		final String suffix = name + "Suffix";
		EasyMock.expect(person.getSuffix()).andReturn(suffix).atLeastOnce();
		return person;
	}

    /**
     * Creates a officer with the specified name.
     *
     * @author IanBrown
     * @param name
     *            the name.
     * @return the officer.
     * @since Dec 7, 2012
     */
    protected final Officer createOfficer(final String name) {
        final Officer officer = createMock(name.replace(" ", ""), Officer.class);
        final String title = name + "Title";
        EasyMock.expect(officer.getTitle()).andReturn(title).anyTimes();
        final String firstName = name + "First";
        EasyMock.expect(officer.getFirstName()).andReturn( firstName ).anyTimes();
        final String initial = name.substring(0);
        EasyMock.expect(officer.getInitial()).andReturn( initial ).anyTimes();
        final String lastName = name + "Last";
        EasyMock.expect(officer.getLastName()).andReturn( lastName ).anyTimes();
        final String suffix = name + "Suffix";
        EasyMock.expect(officer.getSuffix()).andReturn( suffix ).anyTimes();
        final String email = name + "Email@website";
        EasyMock.expect(officer.getEmail()).andReturn( email ).anyTimes();
        final String phone = name + "Phone";
        EasyMock.expect(officer.getPhone()).andReturn( phone ).anyTimes();
        final String fax = name + "Fax";
        EasyMock.expect(officer.getFax()).andReturn( fax ).anyTimes();
        final String office = name + "OfficeName";
        EasyMock.expect(officer.getOfficeName()).andReturn( office ).anyTimes();
        EasyMock.expect(officer.getOrderNumber()).andReturn( 1 ).anyTimes();
        EasyMock.expect(officer.getId()).andReturn( 0l ).anyTimes();

        return officer;
    }

	/**
	 * Gets the local official service.
	 * 
	 * @author IanBrown
	 * @return the local official service.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	protected final LocalOfficialService getLocalOfficialService() {
		return localOfficialService;
	}

	/**
	 * Sets up to test the specific type of corrections controller.
	 * 
	 * @author IanBrown
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	protected abstract void setUpForCorrectionsController();

	/** {@inheritDoc} */
	@Override
	protected final void setUpForBaseController() {
		setLocalOfficialService(createMock("LocalOfficialService", LocalOfficialService.class));
		setUpForCorrectionsController();
	}

	/**
	 * Tear down for the specific type of corrections controller.
	 * 
	 * @author IanBrown
	 * @since Dec 23, 2011
	 * @version Dec 23, 2011
	 */
	protected abstract void tearDownForCorrectionsController();

	/** {@inheritDoc} */
	@Override
	protected final void tearDownForBaseController() {
		tearDownForCorrectionsController();
		setLocalOfficialService(null);
	}

	/**
	 * Sets the local official service.
	 * 
	 * @author IanBrown
	 * @param localOfficialService
	 *            the local official service to set.
	 * @since Dec 22, 2011
	 * @version Dec 22, 2011
	 */
	private void setLocalOfficialService(final LocalOfficialService localOfficialService) {
		this.localOfficialService = localOfficialService;
	}
}