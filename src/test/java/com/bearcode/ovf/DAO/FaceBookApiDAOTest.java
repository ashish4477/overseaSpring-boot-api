/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.FaceBookApi;
import com.bearcode.ovf.model.questionnaire.*;
import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Extended {@link com.bearcode.commons.DAO.BearcodeDAOCheck} test for {@link com.bearcode.ovf.DAO.FaceBookApiDAOTest}.
 *
 * @author Daemmon Hughes
 *
 */
public final class FaceBookApiDAOTest extends BearcodeDAOCheck<FaceBookApiDAO> {

	/**
	 * Test method for
	 * {@link QuestionFieldDAO#checkFieldUsing(com.bearcode.ovf.model.rava.QuestionField)}
	 * for the case where there are no results.
	 *
	 * @author Daemmon Hughes
	 *
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testCheckFieldUsing_noResults() {
        String domain = "www.ovf";
        String appKey = "232697990112981";
        String appSecret = "acac029fad3b92c60344f0198c6966e4";


        FaceBookApi result = new FaceBookApi();
        result.setDomain(domain+"ASDF");
        result.setAppKey(appKey);
        result.setAppSecret(appSecret);
        result.setActive(true);

        final List results = new ArrayList();
        results.add(result);
        EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(results);
		replayAll();

		final FaceBookApi actualUsing = getBearcodeDAO().getByDomain(domain);

		//assertFalse("Nothing is using the field", actualUsing);
		verifyAll();
	}


	/** {@inheritDoc} */
	@Override
	protected final FaceBookApiDAO createBearcodeDAO() {
		return new FaceBookApiDAO();
	}

}
