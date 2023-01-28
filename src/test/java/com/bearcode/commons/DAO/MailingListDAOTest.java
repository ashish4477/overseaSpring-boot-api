/**
 * 
 */
package com.bearcode.commons.DAO;

import com.bearcode.ovf.DAO.MailingListDAO;
import com.bearcode.ovf.model.mail.MailingList;
import org.easymock.EasyMock;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertSame;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link MailingListDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 19, 2012
 * @version Jul 19, 2012
 */
public final class MailingListDAOTest extends BearcodeDAOCheck<MailingListDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.MailingListDAO#findAll()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindAll() {
		final MailingList mailingList = createMock("MailingList", MailingList.class);
		final List<MailingList> mailingLists = Arrays.asList(mailingList);
		EasyMock.expect(getHibernateTemplate().loadAll(MailingList.class)).andReturn(mailingLists);
		replayAll();

		final List<MailingList> actualMailingLists = getBearcodeDAO().findAll();

		assertSame("The mailing lists are returned", mailingLists, actualMailingLists);
		verifyAll();
	}


	/** {@inheritDoc} */
	@Override
	protected final MailingListDAO createBearcodeDAO() {
		return new MailingListDAO();
	}
}
