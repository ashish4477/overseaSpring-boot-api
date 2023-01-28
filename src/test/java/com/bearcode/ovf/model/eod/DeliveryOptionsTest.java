/**
 * 
 */
package com.bearcode.ovf.model.eod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link DeliveryOptions}.
 * 
 * @author IanBrown
 * 
 * @since Jan 18, 2012
 * @version Jan 18, 2012
 */
public final class DeliveryOptionsTest {

	/**
	 * the delivery options to test.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private DeliveryOptions deliveryOptions;

	/**
	 * Sets up to test the delivery options.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Before
	public final void setUpDeliveryOptions() {
		setDeliveryOptions(createDeliveryOptions());
	}

	/**
	 * Tears down the delivery options after testing.
	 * 
	 * @author IanBrown
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@After
	public final void tearDownDeliveryOptions() {
		setDeliveryOptions(null);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#checkEmpty(com.bearcode.ovf.model.eod.DeliveryOptions)} for
	 * the case where the base object is initialized.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testCheckEmpty_baseInitialized() {
		getDeliveryOptions().updateFrom(createInitializedDeliveryOptions());
		final DeliveryOptions options = createDeliveryOptions();

		final boolean actualBlank = getDeliveryOptions().checkEmpty(options);

		assertFalse("Check empty returns false", actualBlank);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#checkEmpty(com.bearcode.ovf.model.eod.DeliveryOptions)} for
	 * the case where the two objects are blank.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testCheckEmpty_bothBlank() {
		final DeliveryOptions options = createDeliveryOptions();

		final boolean actualBlank = getDeliveryOptions().checkEmpty(options);

		assertTrue("Check empty returns true", actualBlank);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#checkEmpty(com.bearcode.ovf.model.eod.DeliveryOptions)} for
	 * the case where the two objects are initialized to the same values.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testCheckEmpty_bothInitialized() {
		final DeliveryOptions options = createInitializedDeliveryOptions();
		getDeliveryOptions().updateFrom(options);

		final boolean actualBlank = getDeliveryOptions().checkEmpty(options);

		assertTrue("Check empty returns true", actualBlank);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#checkEmpty(com.bearcode.ovf.model.eod.DeliveryOptions)} for
	 * the case where the the compare object is initialized.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testCheckEmpty_compareInitalized() {
		final DeliveryOptions options = createInitializedDeliveryOptions();

		final boolean actualBlank = getDeliveryOptions().checkEmpty(options);

		assertFalse("Check empty returns false", actualBlank);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#isEmail()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testIsEmail() {
		final boolean actualEmail = getDeliveryOptions().isEmail();

		assertFalse("The email flag is not set", actualEmail);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#isFax()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testIsFax() {
		final boolean actualFax = getDeliveryOptions().isFax();

		assertFalse("The fax flag is not set", actualFax);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#isInPerson()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testIsInPerson() {
		final boolean actualInPerson = getDeliveryOptions().isInPerson();

		assertFalse("The in-person flag is not set", actualInPerson);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#isPost()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testIsPost() {
		final boolean actualPost = getDeliveryOptions().isPost();

		assertFalse("The post flag is not set", actualPost);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#isTel()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testIsTel() {
		final boolean actualTel = getDeliveryOptions().isTel();

		assertFalse("The telephone (online) flag is not set", actualTel);
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#setEmail(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetEmail() {
		getDeliveryOptions().setEmail(true);

		assertTrue("The email flag is set", getDeliveryOptions().isEmail());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#setFax(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetFax() {
		getDeliveryOptions().setFax(true);

		assertTrue("The fax flag is set", getDeliveryOptions().isFax());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#setInPerson(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetInPerson() {
		getDeliveryOptions().setInPerson(true);

		assertTrue("The in-person flag is set", getDeliveryOptions().isInPerson());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#setPost(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetPost() {
		getDeliveryOptions().setPost(true);

		assertTrue("The post flag is set", getDeliveryOptions().isPost());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#setTel(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testSetTel() {
		getDeliveryOptions().setTel(true);

		assertTrue("The telephone (online) flag is set", getDeliveryOptions().isTel());
	}

	/**
	 * Test method for {@link com.bearcode.ovf.model.eod.DeliveryOptions#updateFrom(com.bearcode.ovf.model.eod.DeliveryOptions)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	@Test
	public final void testUpdateFrom() {
		final DeliveryOptions options = createInitializedDeliveryOptions();

		getDeliveryOptions().updateFrom(options);

		assertDeliveryOptions(options, getDeliveryOptions());
	}

	/**
	 * Custom assertion to ensure that the delivery options match.
	 * 
	 * @author IanBrown
	 * @param expectedDeliveryOptions
	 *            the expected delivery options.
	 * @param actualDeliveryOptions
	 *            the actual delivery options.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private void assertDeliveryOptions(final DeliveryOptions expectedDeliveryOptions, final DeliveryOptions actualDeliveryOptions) {
		assertEquals("The email flag is correct", expectedDeliveryOptions.isEmail(), actualDeliveryOptions.isEmail());
		assertEquals("The fax flag is correct", expectedDeliveryOptions.isFax(), actualDeliveryOptions.isFax());
		assertEquals("The in-person flag is correct", expectedDeliveryOptions.isInPerson(), actualDeliveryOptions.isInPerson());
		assertEquals("The post flag is correct", expectedDeliveryOptions.isPost(), actualDeliveryOptions.isPost());
		assertEquals("The tel flag is correct", expectedDeliveryOptions.isTel(), actualDeliveryOptions.isTel());
	}

	/**
	 * Creates a delivery options object.
	 * 
	 * @author IanBrown
	 * @return the delivery options.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private DeliveryOptions createDeliveryOptions() {
		return new DeliveryOptions();
	}

	/**
	 * Creates an initialize delivery options object.
	 * 
	 * @author IanBrown
	 * @return the delivery options.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private DeliveryOptions createInitializedDeliveryOptions() {
		final DeliveryOptions options = createDeliveryOptions();
		options.setEmail(true);
		options.setFax(false);
		options.setInPerson(true);
		options.setPost(false);
		options.setTel(true);
		return options;
	}

	/**
	 * Gets the delivery options.
	 * 
	 * @author IanBrown
	 * @return the delivery options.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private DeliveryOptions getDeliveryOptions() {
		return deliveryOptions;
	}

	/**
	 * Sets the delivery options.
	 * 
	 * @author IanBrown
	 * @param deliveryOptions
	 *            the delivery options to set.
	 * @since Jan 18, 2012
	 * @version Jan 18, 2012
	 */
	private void setDeliveryOptions(final DeliveryOptions deliveryOptions) {
		this.deliveryOptions = deliveryOptions;
	}

}
