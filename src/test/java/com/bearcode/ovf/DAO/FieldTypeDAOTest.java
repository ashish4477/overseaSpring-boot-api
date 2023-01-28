/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.questionnaire.FieldType;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link FieldTypeDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 18, 2012
 * @version Jul 18, 2012
 */
public final class FieldTypeDAOTest extends BearcodeDAOCheck<FieldTypeDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FieldTypeDAO#checkFieldType(com.bearcode.ovf.model.questionnaire.FieldType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testCheckFieldType() {
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FieldType.class)).andReturn(criteria);
		final long id = 75627l;
		EasyMock.expect(fieldType.getId()).andReturn(id);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		final String name = "Name";
		EasyMock.expect(fieldType.getName()).andReturn(name);
		EasyMock.expect(criteria.setProjection((Projection) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(0l);
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkFieldType(fieldType);

		assertTrue("The field type is unique", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FieldTypeDAO#checkFieldType(com.bearcode.ovf.model.questionnaire.FieldType)} for
	 * the case where another one is found.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testCheckFieldType_anotherOne() {
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FieldType.class)).andReturn(criteria);
		final long id = 75627l;
		EasyMock.expect(fieldType.getId()).andReturn(id);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		final String name = "Name";
		EasyMock.expect(fieldType.getName()).andReturn(name);
		EasyMock.expect(criteria.setProjection((Projection) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(1l);
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkFieldType(fieldType);

		assertFalse("The field type is not unique", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FieldTypeDAO#findById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindById() {
		final long id = 5612l;
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		EasyMock.expect(getHibernateTemplate().get(FieldType.class, id)).andReturn(fieldType);
		replayAll();

		final FieldType actualFieldType = getBearcodeDAO().findById(id);

		assertSame("The field type is returned", fieldType, actualFieldType);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FieldTypeDAO#findFieldTypes()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindFieldTypes() {
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		final List<FieldType> fieldTypes = Arrays.asList(fieldType);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FieldType.class)).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(fieldTypes);
		replayAll();

		final List<FieldType> actualFieldTypes = getBearcodeDAO().findFieldTypes();

		assertSame("The field types are returned", fieldTypes, actualFieldTypes);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.FieldTypeDAO#saveFieldType(com.bearcode.ovf.model.questionnaire.FieldType)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 18, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	@Deprecated
	public final void testSaveFieldType() {
		final FieldType fieldType = createMock("FieldType", FieldType.class);
		final Session session = addSessionToHibernateTemplate();
		session.saveOrUpdate(fieldType);
		replayAll();

		getBearcodeDAO().saveFieldType(fieldType);

		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final FieldTypeDAO createBearcodeDAO() {
		return new FieldTypeDAO();
	}

}
