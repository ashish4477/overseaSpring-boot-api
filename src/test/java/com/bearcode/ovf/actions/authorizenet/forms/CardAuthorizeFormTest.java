/**
 * 
 */
package com.bearcode.ovf.actions.authorizenet.forms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link CardAuthorizeForm}.
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 19, 2011
 */
public final class CardAuthorizeFormTest {

	/**
	 * the card authorize form.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private CardAuthorizeForm cardAuthorizeForm;

	/**
	 * Sets up to test the card authorize form.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Before
	public final void setUpCardAuthorizeForm() {
		setCardAuthorizeForm(createCardAuthorizeForm());
	}

	/**
	 * Tears down the card authorize form after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@After
	public final void tearDownCardAuthorizeForm() {
		setCardAuthorizeForm(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#getAmount()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetAmount() {
		final double actualAmount = getCardAuthorizeForm().getAmount();

		assertEquals("The amount is not set", 0.0, actualAmount, 0.01);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#getCardExpired()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCardExpired() {
		final String actualCardExpired = getCardAuthorizeForm()
				.getCardExpired();

		assertEquals("The card is not expired", "nullnull", actualCardExpired);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#getCardExpiredMonth()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCardExpiredMonth() {
		final String actualCardExpiredMonth = getCardAuthorizeForm()
				.getCardExpiredMonth();

		assertNull("There is no card expired month", actualCardExpiredMonth);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#getCardExpiredYear()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCardExpiredYear() {
		final String actualCardExpiredYear = getCardAuthorizeForm()
				.getCardExpiredYear();

		assertNull("There is no card expired year", actualCardExpiredYear);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#getCardNum()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetCardNum() {
		final String actualCardNum = getCardAuthorizeForm().getCardNum();

		assertNull("There is no card num", actualCardNum);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#setAmount(double)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetAmount() {
		final double amount = 92.3;

		getCardAuthorizeForm().setAmount(amount);

		assertEquals("The amount is set", amount, getCardAuthorizeForm()
				.getAmount(), 0.1);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#setCardExpiredMonth(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetCardExpiredMonth() {
		final String cardExpiredMonth = "Month";

		getCardAuthorizeForm().setCardExpiredMonth(cardExpiredMonth);

		assertEquals("The card expired month is set", cardExpiredMonth,
				getCardAuthorizeForm().getCardExpiredMonth());
		assertEquals("The card expired value is set",
				cardExpiredMonth + "null", getCardAuthorizeForm()
						.getCardExpired());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#setCardExpiredYear(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetCardExpiredYear() {
		final String cardExpiredYear = "Year";

		getCardAuthorizeForm().setCardExpiredYear(cardExpiredYear);

		assertEquals("The card expired year is set", cardExpiredYear,
				getCardAuthorizeForm().getCardExpiredYear());
		assertEquals("The card expired value is set", "null" + cardExpiredYear,
				getCardAuthorizeForm().getCardExpired());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.actions.authorizenet.forms.CardAuthorizeForm#setCardNum(java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetCardNum() {
		final String cardNum = "Card Number";

		getCardAuthorizeForm().setCardNum(cardNum);

		assertEquals("The card num is set", cardNum, getCardAuthorizeForm()
				.getCardNum());
	}

	/**
	 * Creates a card authorize form.
	 * 
	 * @author IanBrown
	 * @return the card authorize form.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private CardAuthorizeForm createCardAuthorizeForm() {
		return new CardAuthorizeForm();
	}

	/**
	 * Gets the card authorize form.
	 * 
	 * @author IanBrown
	 * @return the card authorize form.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private CardAuthorizeForm getCardAuthorizeForm() {
		return cardAuthorizeForm;
	}

	/**
	 * Sets the card authorize form.
	 * 
	 * @author IanBrown
	 * @param cardAuthorizeForm
	 *            the card authorize form to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setCardAuthorizeForm(final CardAuthorizeForm cardAuthorizeForm) {
		this.cardAuthorizeForm = cardAuthorizeForm;
	}
}
