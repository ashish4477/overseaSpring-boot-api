/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.formtracking.TrackedForm;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link FormTrackingDAO}.
 * 
 * @author IanBrown
 * 
 * @since Apr 26, 2012
 * @version Apr 26, 2012
 */
public class FormTrackingDAOTest extends BearcodeDAOCheck<FormTrackingDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FormTrackingDAO#findAllTrackedForms()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Apr 26, 2012
	 * @version Apr 26, 2012
	 */
	@Test
	public final void testFindAllTrackedForms() {
		final TrackedForm trackedForm = createMock("TrackedForm", TrackedForm.class);
		final List<TrackedForm> trackedForms = Arrays.asList(trackedForm);
		EasyMock.expect(getHibernateTemplate().loadAll(TrackedForm.class)).andReturn(trackedForms).anyTimes();
		replayAll();

		final List<TrackedForm> actualTrackedForms = getBearcodeDAO().findAllTrackedForms();

		assertSame("The tracked forms are returned", trackedForms, actualTrackedForms);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final FormTrackingDAO createBearcodeDAO() {
		return new FormTrackingDAO();
	}
}
