/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.WizardResults;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link PdfAnswersDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 19, 2012
 * @version Jul 19, 2012
 */
public final class PdfAnswersDAOTest extends BearcodeDAOCheck<PdfAnswersDAO> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.PdfAnswersDAO#findByFieldSelectedValue(com.bearcode.ovf.model.questionnaire.QuestionField, int, java.util.Date, java.util.Date)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindByFieldSelectedValue() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final int value = 23;
		final Date after = new Date();
		final Date before = new Date();
		final WizardResults wizardResults = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> results = Arrays.asList(wizardResults);
		final Session session = addSessionToHibernateTemplate();
		final Query namedQuery = createMock("NamedQuery", Query.class);
		EasyMock.expect(session.getNamedQuery("wizardresultsWithFieldSelectedValue")).andReturn(namedQuery);
		final long fieldId = 7626l;
		EasyMock.expect(field.getId()).andReturn(fieldId);
		EasyMock.expect(namedQuery.setParameter("field", fieldId)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter("value", value)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter("before", before)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter("after", after)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.list()).andReturn(results);
		replayAll();

		final List<WizardResults> actualResults = getBearcodeDAO().findByFieldSelectedValue(field, value, after, before);

		assertSame("The results are returned", results, actualResults);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.PdfAnswersDAO#findByFieldValue(java.lang.String, java.util.Date, java.util.Date)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindByFieldValue() {
		final String value = "Value";
		final Date after = new Date();
		final Date before = new Date();
		final WizardResults wizardResults = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> results = Arrays.asList(wizardResults);
		final Session session = addSessionToHibernateTemplate();
		final Query namedQuery = createMock("NamedQuery", Query.class);
		EasyMock.expect(session.getNamedQuery("wizardresultsWithFieldValue")).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter( "typeName", FieldType.MAILING_LIST_TYPE_NAME + "%")).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter("value", value)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter("before", before)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.setParameter("after", after)).andReturn(namedQuery);
		EasyMock.expect(namedQuery.list()).andReturn(results);
		replayAll();

		final List<WizardResults> actualResults = getBearcodeDAO().findByFieldValue(value, after, before);

		assertSame("The results are returned", results, actualResults);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.PdfAnswersDAO#getUserPdfs(com.bearcode.ovf.model.common.OverseasUser)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testGetUserPdfs() {
		final OverseasUser user = createMock("User", OverseasUser.class);
		final WizardResults wizardResults = createMock("WizardResult", WizardResults.class);
		final List<WizardResults> results = Arrays.asList( wizardResults );
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock( "criteria", Criteria.class );
		EasyMock.expect( session.createCriteria( WizardResults.class )).andReturn( criteria );
		EasyMock.expect(criteria.add( EasyMock.<Criterion>anyObject( Criterion.class ) )).andReturn( criteria );
		EasyMock.expect( criteria.list() ).andReturn(results);
		replayAll();

		final Collection<WizardResults> actualResults = getBearcodeDAO().getUserPdfs(user);

		assertSame("The results are returned", results, actualResults);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final PdfAnswersDAO createBearcodeDAO() {
		return new PdfAnswersDAO();
	}
}
