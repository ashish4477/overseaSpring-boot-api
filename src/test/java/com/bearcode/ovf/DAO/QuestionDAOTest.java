/**
 * 
 */
package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.questionnaire.*;
import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link QuestionDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 19, 2012
 * @version Jul 23, 2012
 */
public final class QuestionDAOTest extends BearcodeDAOCheck<QuestionDAO> {

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#checkQuestionUsing(com.bearcode.ovf.model.questionnaire.Question)}
	 * for the case where the question is not used.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testCheckQuestionUsing_notUsed() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(0l));
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkQuestionUsing(question);

		assertFalse("The question is not used", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#checkQuestionUsing(com.bearcode.ovf.model.questionnaire.Question)}
	 * for the case where the question is used.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testCheckQuestionUsing_used() {
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject()))
				.andReturn(Arrays.asList(1l));
		replayAll();

		final boolean actualResult = getBearcodeDAO().checkQuestionUsing(question);

		assertTrue("The question is used", actualResult);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findDependents(com.bearcode.ovf.model.questionnaire.Question)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindDependents() {
		final Question question = createMock("Question", Question.class);
		final QuestionDependency dependent = createMock("Dependent", QuestionDependency.class);
		final List<QuestionDependency> dependents = Arrays.asList(dependent);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(dependents);
		replayAll();

		final Collection<QuestionDependency> actualDependents = getBearcodeDAO().findDependents(question);

		assertSame("The dependents are returned", dependents, actualDependents);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findDependentVariants(com.bearcode.ovf.model.questionnaire.Question)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindDependentVariants() {
		final Question question = createMock("Question", Question.class);
		final QuestionVariant dependentVariant = createMock("DependentVariant", QuestionVariant.class);
		final List<QuestionVariant> dependentVariants = Arrays.asList(dependentVariant);
		final long questionId = 54289l;
		EasyMock.expect(question.getId()).andReturn(questionId);
		EasyMock.expect(getHibernateTemplate().find((String) EasyMock.anyObject())).andReturn(dependentVariants);
		replayAll();

		final Collection<QuestionVariant> actualDependentVariants = getBearcodeDAO().findDependentVariants(question);

		assertSame("The dependent variants are returned", dependentVariants, actualDependentVariants);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findFaceDependencies(com.bearcode.ovf.model.questionnaire.Related)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindFaceDependencies() {
		final Related dependent = createMock("Dependent", Related.class);
		final BasicDependency faceDependency = createMock("FaceDependency", BasicDependency.class);
		final List<BasicDependency> faceDependencies = Arrays.asList(faceDependency);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FaceDependency.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.createAlias("dependsOn", "face")).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(faceDependencies);
		replayAll();

		final Collection<BasicDependency> actualFaceDependencies = getBearcodeDAO().findFaceDependencies(dependent);

		assertSame("The face dependencies are returned", faceDependencies, actualFaceDependencies);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findFlowDependencies(com.bearcode.ovf.model.questionnaire.Related)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindFlowDependencies() {
		final Related dependent = createMock("Dependent", Related.class);
		final BasicDependency flowDependency = createMock("FlowDependency", BasicDependency.class);
		final List<BasicDependency> flowDependencies = Arrays.asList(flowDependency);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(FlowDependency.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(flowDependencies);
		replayAll();

		final Collection<BasicDependency> actualFlowDependencies = getBearcodeDAO().findFlowDependencies(dependent);

		assertSame("The flow dependencies are returned", flowDependencies, actualFlowDependencies);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindQuestionById() {
		final long id = 456278l;
		final Question question = createMock("Question", Question.class);
		EasyMock.expect(getHibernateTemplate().get(Question.class, id)).andReturn(question);
		replayAll();

		final Question actualQuestion = getBearcodeDAO().findQuestionById(id);

		assertSame("The question is returned", question, actualQuestion);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionDependencies(com.bearcode.ovf.model.questionnaire.Related, com.bearcode.ovf.model.questionnaire.Question)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindQuestionDependencies() {
		final Related dependent = createMock("Dependent", Related.class);
		final Question dependsOn = createMock("DependsOn", Question.class);
		final BasicDependency questionDependency = createMock("QuestionDependency", BasicDependency.class);
		final List<BasicDependency> questionDependencies = Arrays.asList(questionDependency);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(QuestionDependency.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.createAlias("condition", "condition")).andReturn(criteria);
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(questionDependencies);
		replayAll();

		final Collection<BasicDependency> actualQuestionDependencies = getBearcodeDAO().findQuestionDependencies(dependent,
				dependsOn);

		assertSame("The question dependencies are returned", questionDependencies, actualQuestionDependencies);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionDependencyById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindQuestionDependencyById() {
		final long dependencyId = 51278l;
		final BasicDependency questionDependency = createMock("QuestionDependency", BasicDependency.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(BasicDependency.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.uniqueResult()).andReturn(questionDependency);
		replayAll();

		final BasicDependency actualQuestionDependency = getBearcodeDAO().findQuestionDependencyById(dependencyId);

		assertSame("The question dependency is returned", questionDependency, actualQuestionDependency);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionForDependency()}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindQuestionForDependency() {
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		EasyMock.expect(getHibernateTemplate().find((String) EasyMock.anyObject())).andReturn(questions);
		replayAll();

		final Collection<Question> actualQuestions = getBearcodeDAO().findQuestionForDependency();

		assertSame("The question are returned", questions, actualQuestions);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionForDependency(com.bearcode.ovf.model.questionnaire.Question)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindQuestionForDependencyQuestion() {
		final Question dependency = createMock("Dependency", Question.class);
		final Question question = createMock("Question", Question.class);
		final List<Question> questions = Arrays.asList(question);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(dependency.getPage()).andReturn(page).anyTimes();
		final int pageNumber = 3;
		EasyMock.expect(page.getNumber()).andReturn(pageNumber).anyTimes();
        final PageType pageType = PageType.OVERSEAS;
        EasyMock.expect(page.getType()).andReturn(pageType).anyTimes();
		EasyMock.expect(
				getHibernateTemplate().findByNamedParam((String) EasyMock.anyObject(), EasyMock.<String[]>anyObject(),
						EasyMock.<Object[]>anyObject())).andReturn(questions);
		replayAll();

		final Collection<Question> actualQuestions = getBearcodeDAO().findQuestionForDependency(dependency);

		assertSame("The questions are returned", questions, actualQuestions);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionsAfterQuestion(com.bearcode.ovf.model.questionnaire.Question, int)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 23, 2012
	 */
	@Test
	public final void testFindQuestionsAfterQuestion() {
		final Question question = createMock("Question", Question.class);
		final int number = 3;
		final Question afterQuestion = createMock("AfterQuestion", Question.class);
		final List<Question> questions = Arrays.asList(afterQuestion);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(Question.class)).andReturn(criteria);
		final QuestionnairePage page = createMock("Page", QuestionnairePage.class);
		EasyMock.expect(question.getPage()).andReturn(page);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.list()).andReturn(questions);
		replayAll();

		final Collection<Question> actualQuestions = getBearcodeDAO().findQuestionsAfterQuestion(question, number);

		assertSame("The questions are returned", questions, actualQuestions);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.QuestionDAO#findQuestionVariantById(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 23, 2012
	 */
	@Test
	public final void testFindQuestionVariantById() {
		final long id = 987276l;
		final QuestionVariant variant = createMock("Variant", QuestionVariant.class);
		EasyMock.expect(getHibernateTemplate().get(QuestionVariant.class, id)).andReturn(variant);
		replayAll();

		final QuestionVariant actualVariant = getBearcodeDAO().findQuestionVariantById(id);

		assertSame("The variant is returned", variant, actualVariant);
		verifyAll();
	}

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.QuestionDAO#findUserFieldDependencies(com.bearcode.ovf.model.questionnaire.Related, java.lang.String)}
	 * .
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 19, 2012
	 * @version Jul 19, 2012
	 */
	@Test
	public final void testFindUserFieldDependencies() {
		final Related dependent = createMock("Dependent", Related.class);
		final String fieldName = "Field Name";
		final BasicDependency userFieldDependency = createMock("UserFieldDependency", BasicDependency.class);
		final List<BasicDependency> userFieldDependencies = Arrays.asList(userFieldDependency);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(UserFieldDependency.class)).andReturn(criteria);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).atLeastOnce();
		EasyMock.expect(criteria.addOrder((Order) EasyMock.anyObject())).andReturn(criteria);
		EasyMock.expect(criteria.list()).andReturn(userFieldDependencies);
		replayAll();

		final Collection<BasicDependency> actualUserFieldDependencies = getBearcodeDAO().findUserFieldDependencies(dependent,
				fieldName);

		assertSame("The user field dependencies are returned", userFieldDependencies, actualUserFieldDependencies);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final QuestionDAO createBearcodeDAO() {
		return new QuestionDAO();
	}
}
