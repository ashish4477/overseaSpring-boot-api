/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.questionnaire.FieldDependency;
import com.bearcode.ovf.model.questionnaire.FieldDictionaryItem;
import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;
import com.bearcode.ovf.model.questionnaire.QuestionnairePage;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link QuestionFieldDAO}.
 * 
 * @author IanBrown
 * 
 * @since Dec 12, 2011
 * @version Jan 3, 2012
 */
public final class QuestionFieldDAOTest extends BearcodeDAOCheck<QuestionFieldDAO> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#checkFieldUsing(com.bearcode.ovf.model.rava.QuestionField)}
	 * for the case where there are no results.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testCheckFieldUsing_noResults() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final QuestionVariant question = createMock("Question", QuestionVariant.class);
		EasyMock.expect(field.getQuestion()).andReturn(question);
		final Question group = createMock("Group", Question.class);
		EasyMock.expect(question.getQuestion()).andReturn(group);
		final List results = new ArrayList();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(results);
		replayAll();

		final boolean actualUsing = getBearcodeDAO().checkFieldUsing(field);

		assertFalse("Nothing is using the field", actualUsing);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#checkFieldUsing(com.bearcode.ovf.model.rava.QuestionField)}
	 * for the case where there are results.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testCheckFieldUsing_results() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final QuestionVariant question = createMock("Question", QuestionVariant.class);
		EasyMock.expect(field.getQuestion()).andReturn(question);
		final Question group = createMock("Group", Question.class);
		EasyMock.expect(question.getQuestion()).andReturn(group);
		final Object result = createMock("Result", Object.class);
		final List results = Arrays.asList(result);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(results);
		replayAll();

		final boolean actualUsing = getBearcodeDAO().checkFieldUsing(field);

		assertTrue("The field is used", actualUsing);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#checkOptionUsing(com.bearcode.ovf.model.rava.FieldDictionaryItem)}
	 * for the case where there are no results.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testCheckOptionUsing_noResults() {
		final FieldDictionaryItem option = createMock("Option", FieldDictionaryItem.class);
		final List results = new ArrayList();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(results);
		replayAll();

		final boolean actualUsing = getBearcodeDAO().checkOptionUsing(option);

		assertFalse("Nothing is using the option", actualUsing);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#checkOptionUsing(com.bearcode.ovf.model.rava.FieldDictionaryItem)}
	 * for the case where there are results.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testCheckOptionUsing_results() {
		final FieldDictionaryItem option = createMock("Option", FieldDictionaryItem.class);
		final Object result = createMock("Result", Object.class);
		final List results = Arrays.asList(result);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(results);
		replayAll();

		final boolean actualUsing = getBearcodeDAO().checkOptionUsing(option);

		assertTrue("The option is used", actualUsing);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#findAnswerFiedsOfPage(java.util.Collection, com.bearcode.ovf.model.rava.QuestionnairePage)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindAnswerFiedsOfPage() {
		final long answerFieldId = 125l;
		final Collection<Long> answerFields = Arrays.asList(answerFieldId);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		final int pageNumber = 3;
		EasyMock.expect(page.getNumber()).andReturn(pageNumber);
		final QuestionField answerFieldOnPage = createMock("AnswerFieldOnPage", QuestionField.class);
		final Collection<QuestionField> answerFieldsOnPage = Arrays.asList(answerFieldOnPage);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(
				(List) answerFieldsOnPage);
		replayAll();

		final Collection<QuestionField> actualAnswerFieldsOnPage = getBearcodeDAO().findAnswerFiedsOfPage(answerFields, page);

		assertSame("The answer fields on the page are returned", answerFieldsOnPage, actualAnswerFieldsOnPage);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#findDictionaryItems(java.util.Collection)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindDictionaryItems() {
		final long selectedId = 832l;
		final Collection selectedIds = Arrays.asList(selectedId);
		final FieldDictionaryItem dictionaryItem = createMock("DictionaryItem", FieldDictionaryItem.class);
		final Session session = addAnotherSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FieldDictionaryItem.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		final Collection<FieldDictionaryItem> dictionaryItems = Arrays.asList(dictionaryItem);
		EasyMock.expect(criteria.list()).andReturn((List) dictionaryItems);
		replayAll();

		final Collection<FieldDictionaryItem> actualDictionaryItems = getBearcodeDAO().findDictionaryItems(selectedIds);

		assertSame("The dictionary items are returned", dictionaryItems, actualDictionaryItems);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#findFieldDependencies()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindFieldDependencies() {
		final FieldDependency fieldDependency = createMock("FieldDependency", FieldDependency.class);
		final Collection<FieldDependency> fieldDependencies = Arrays.asList(fieldDependency);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(
				(List) fieldDependencies);
		replayAll();

		final Collection<FieldDependency> actualFieldDependencies = getBearcodeDAO().findFieldDependencies();

		assertSame("The field dependencies are returned", fieldDependencies, actualFieldDependencies);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#findFieldsBeforeNumber(com.bearcode.ovf.model.rava.QuestionField, int)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindFieldsBeforeNumber() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final int number = 7;
		final QuestionVariant question = createMock("Question", QuestionVariant.class);
		EasyMock.expect(field.getQuestion()).andReturn(question);
		final QuestionField fieldBeforeNumber = createMock("FieldBeforeNumber", QuestionField.class);
		final Collection<QuestionField> fieldsBeforeNumber = Arrays.asList(fieldBeforeNumber);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(
				(List) fieldsBeforeNumber);
		replayAll();

		final Collection<QuestionField> actualFieldsBeforeNumber = getBearcodeDAO().findFieldsBeforeNumber(field, number);

		assertSame("The fields before number is returned", fieldsBeforeNumber, actualFieldsBeforeNumber);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#findReportableFields()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindReportableFields() {
		final QuestionField reportableField = createMock("ReportableField", QuestionField.class);
		final Session session = addAnotherSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(QuestionField.class)).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("type", "type")).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("question", "variant")).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		final Collection<QuestionField> reportableFields = Arrays.asList(reportableField);
		EasyMock.expect(criteria.list()).andReturn((List) reportableFields);
		replayAll();

		final Collection<QuestionField> actualReportableFields = getBearcodeDAO().findReportableFields();

		assertSame("The reportable fields are returned", reportableFields, actualReportableFields);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#getById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Jan 3, 2012
	 */
	@Test
	public final void testGetById() {
		final long id = 8065l;
		final Session session = addSessionToHibernateTemplate();
		final QuestionField questionField = createMock("QuestionField", QuestionField.class);
		EasyMock.expect(session.get(QuestionField.class, id)).andReturn(questionField);
		replayAll();

		final QuestionField actualQuestionField = getBearcodeDAO().getById(id);

		assertSame("The question field is returned", questionField, actualQuestionField);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#getDependencyForField(com.bearcode.ovf.model.rava.QuestionField)}
	 * for the case where there is a dependency.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetDependencyForField_dependency() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final FieldDependency dependency = createMock("Dependency", FieldDependency.class);
		final List dependencies = Arrays.asList(dependency);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(dependencies);
		replayAll();

		final FieldDependency actualDependency = getBearcodeDAO().getDependencyForField(field);

		assertSame("The dependency is returned", dependency, actualDependency);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#getDependencyForField(com.bearcode.ovf.model.rava.QuestionField)}
	 * for the case where there are no dependencies.
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testGetDependencyForField_noDependency() {
		final QuestionField field = createMock("Field", QuestionField.class);
		final List dependencies = new ArrayList();
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(dependencies);
		replayAll();

		final FieldDependency actualDependency = getBearcodeDAO().getDependencyForField(field);

		assertNull("No dependency is returned", actualDependency);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#getDictionaryItemById(long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testGetDictionaryItemById() {
		final long itemId = 7502l;
		final FieldDictionaryItem dictionaryItem = createMock("DictionaryItem", FieldDictionaryItem.class);
		EasyMock.expect(getHibernateTemplate().get(FieldDictionaryItem.class, itemId)).andReturn(dictionaryItem);
		replayAll();

		final FieldDictionaryItem actualDictionaryItem = getBearcodeDAO().getDictionaryItemById(itemId);

		assertSame("The dictionary item is returned", dictionaryItem, actualDictionaryItem);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionFieldDAO#getFieldDependencyById(long)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Dec 12, 2011
	 * @version Dec 12, 2011
	 */
	@Test
	public final void testGetFieldDependencyById() {
		final long dependencyId = 30876l;
		final FieldDependency fieldDependency = createMock("FieldDependency", FieldDependency.class);
		EasyMock.expect(getHibernateTemplate().get(FieldDependency.class, dependencyId)).andReturn(fieldDependency);
		replayAll();

		final FieldDependency actualFieldDependency = getBearcodeDAO().getFieldDependencyById(dependencyId);

		assertSame("The field dependency is returned", fieldDependency, actualFieldDependency);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final QuestionFieldDAO createBearcodeDAO() {
		return new QuestionFieldDAO();
	}

}
