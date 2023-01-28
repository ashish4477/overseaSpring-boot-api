/**
 * 
 */
package com.bearcode.commons.DAO;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for {@link PagingInfo}
 * 
 * @author IanBrown
 * 
 * @since Dec 19, 2011
 * @version Dec 19, 2011
 */
public final class PagingInfoTest {

	/**
	 * the paging info to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private PagingInfo pagingInfo;

	/**
	 * Sets up the paging info to test.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Before
	public final void setUpPagingInfo() {
		setPagingInfo(createPagingInfo());
	}

	/**
	 * Tears down the paging info after testing.
	 * 
	 * @author IanBrown
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@After
	public final void tearDownPagingInfo() {
		setPagingInfo(null);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#getActualRows()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetActualRows() {
		final long actualRows = getPagingInfo().getActualRows();

		assertEquals("There are no actual rows", 0l, actualRows);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#getFirstResult()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetFirstResult() {
		final int actualFirstResult = getPagingInfo().getFirstResult();

		assertEquals("There is no first result", 0, actualFirstResult);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#getMaxResults()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetMaxResults() {
		final int actualMaxResults = getPagingInfo().getMaxResults();

		assertEquals("There are no max results", -1, actualMaxResults);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#getOrderFields()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testGetOrderFields() {
		final String[] actualOrderFields = getPagingInfo().getOrderFields();

		assertNull("There are no order fields", actualOrderFields);
	}

	/**
	 * Test method for {@link com.bearcode.commons.DAO.PagingInfo#isAscending()}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testIsAscending() {
		final boolean actualAscending = getPagingInfo().isAscending();

		assertTrue("The ascending flag is set", actualAscending);
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#PagingInfo(int, int, java.lang.String[], boolean)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testPagingInfoIntIntStringArrayBoolean() {
		final int firstResult = 2;
		final int maxResults = 10;
		final String[] orderFields = new String[] { "field1", "field2" };
		final boolean ascending = false;

		final PagingInfo actualPagingInfo = new PagingInfo(firstResult,
				maxResults, orderFields, ascending);

		assertEquals("The first result is set", firstResult,
				actualPagingInfo.getFirstResult());
		assertEquals("The max results is set", maxResults,
				actualPagingInfo.getMaxResults());
		assertArrayEquals("The order fields are set", orderFields,
				actualPagingInfo.getOrderFields());
		assertFalse("The ascending flag is cleared",
				actualPagingInfo.isAscending());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#PagingInfo(java.lang.String[], boolean)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testPagingInfoStringArrayBoolean() {
		final String[] orderFields = new String[] { "field3", "field4" };
		final boolean ascending = false;

		final PagingInfo actualPagingInfo = new PagingInfo(orderFields,
				ascending);

		assertEquals("The first result is not set", 0,
				actualPagingInfo.getFirstResult());
		assertEquals("The max results is not set", -1,
				actualPagingInfo.getMaxResults());
		assertArrayEquals("The order fields are set", orderFields,
				actualPagingInfo.getOrderFields());
		assertFalse("The ascending flag is cleared",
				actualPagingInfo.isAscending());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#setActualRows(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetActualRows() {
		final long actualRows = 10l;

		getPagingInfo().setActualRows(actualRows);

		assertEquals("The actual rows is set", actualRows, getPagingInfo()
				.getActualRows());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#setAscending(boolean)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetAscending() {
		final boolean ascending = !getPagingInfo().isAscending();

		getPagingInfo().setAscending(ascending);

		assertEquals("The ascending flag has changed", ascending,
				getPagingInfo().isAscending());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#setFirstResult(int)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetFirstResult() {
		final int firstResult = 97;

		getPagingInfo().setFirstResult(firstResult);

		assertEquals("The first result is set", firstResult, getPagingInfo()
				.getFirstResult());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#setMaxResults(int)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetMaxResults() {
		final int maxResults = 1282;

		getPagingInfo().setMaxResults(maxResults);

		assertEquals("The max results is set", maxResults, getPagingInfo()
				.getMaxResults());
	}

	/**
	 * Test method for
	 * {@link com.bearcode.commons.DAO.PagingInfo#setOrderFields(java.lang.String[])}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	@Test
	public final void testSetOrderFields() {
		final String[] orderFields = new String[] { "field5", "field6",
				"field7" };

		getPagingInfo().setOrderFields(orderFields);

		assertArrayEquals("The order fields are set", orderFields,
				getPagingInfo().getOrderFields());
	}

	/**
	 * Creates the paging info.
	 * 
	 * @author IanBrown
	 * @return the paging info.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private PagingInfo createPagingInfo() {
		return new PagingInfo();
	}

	/**
	 * Gets the paging info.
	 * 
	 * @author IanBrown
	 * @return the paging info.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private PagingInfo getPagingInfo() {
		return pagingInfo;
	}

	/**
	 * Sets the paging info.
	 * 
	 * @author IanBrown
	 * @param pagingInfo
	 *            the paging info to set.
	 * @since Dec 19, 2011
	 * @version Dec 19, 2011
	 */
	private void setPagingInfo(final PagingInfo pagingInfo) {
		this.pagingInfo = pagingInfo;
	}

}
