/**
 * 
 */
package com.bearcode.ovf.DAO;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.easymock.EasyMock;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.junit.Test;

import com.bearcode.commons.DAO.BearcodeDAOCheck;
import com.bearcode.ovf.model.questionnaire.Answer;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.WizardResults;

/**
 * Extended {@link BearcodeDAOCheck} test for {@link AnswerDAO}.
 * 
 * @author IanBrown
 * 
 * @since Jul 17, 2012
 * @version Jul 18, 2012
 */
public final class AnswerDAOTest extends BearcodeDAOCheck<AnswerDAO> {

	/**
	 * Test method for
	 * {@link com.bearcode.ovf.DAO.AnswerDAO#findFirstMailingList(java.util.Date, java.util.Date, java.util.List, int)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindFirstMailingList() {
		final Date fromDate = new Date();
		final Date toDate = new Date();
		final long mailingFieldIdEntry = 7626l;
		final List<Long> mailingFieldId = Arrays.asList(mailingFieldIdEntry);
		final int startFrom = 26;
		final List firstMailingList = Arrays.asList("Not sure what this should return");
		final Session session = addSessionToHibernateTemplate();
		final Query forMailingListQuery = createMock("ForMailingList", Query.class);
		EasyMock.expect(session.getNamedQuery("forMailingList")).andReturn(forMailingListQuery);
		EasyMock.expect(forMailingListQuery.setTimestamp("fromDate", fromDate)).andReturn(forMailingListQuery);
		EasyMock.expect(forMailingListQuery.setTimestamp("toDate", toDate)).andReturn(forMailingListQuery);
		EasyMock.expect(forMailingListQuery.setParameterList("fieldId", mailingFieldId)).andReturn(forMailingListQuery);
		EasyMock.expect(forMailingListQuery.setInteger("start", startFrom)).andReturn(forMailingListQuery);
		EasyMock.expect(forMailingListQuery.list()).andReturn(firstMailingList);
		replayAll();

		final Collection actualFirstMailingList = getBearcodeDAO()
				.findFirstMailingList(fromDate, toDate, mailingFieldId, startFrom);

		assertSame("The first mailing list is returned", firstMailingList, actualFirstMailingList);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.AnswerDAO#findSameAnswer(com.bearcode.ovf.model.questionnaire.Answer)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindSameAnswer() {
		final Answer answer = createMock("Answer", Answer.class);
		final Answer expectedAnswer = createMock("ExpectedAnswer", Answer.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(Answer.class)).andReturn(criteria);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(answer.getWizardResults()).andReturn(wizardResults);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field);
		final long id = 56235l;
		EasyMock.expect(answer.getId()).andReturn(id);
		final List<Answer> answers = Arrays.asList(expectedAnswer);
		EasyMock.expect(criteria.list()).andReturn(answers);
		replayAll();

		final Answer actualAnswer = getBearcodeDAO().findSameAnswer(answer);

		assertSame("The expected answer is returned", expectedAnswer, actualAnswer);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.AnswerDAO#findSameAnswer(com.bearcode.ovf.model.questionnaire.Answer)} for the
	 * case where the answer is not in the database.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 18, 2012
	 */
	@Test
	public final void testFindSameAnswer_notInDatabase() {
		final Answer answer = createMock("Answer", Answer.class);
		final Session session = addSessionToHibernateTemplate();
		final Criteria criteria = createMock("Criteria", Criteria.class);
		EasyMock.expect(session.createCriteria(Answer.class)).andReturn(criteria);
		final WizardResults wizardResults = createMock("WizardResults", WizardResults.class);
		EasyMock.expect(answer.getWizardResults()).andReturn(wizardResults);
		EasyMock.expect(criteria.add((Criterion) EasyMock.anyObject())).andReturn(criteria).times(3);
		final QuestionField field = createMock("Field", QuestionField.class);
		EasyMock.expect(answer.getField()).andReturn(field);
		final long id = 56235l;
		EasyMock.expect(answer.getId()).andReturn(id);
		final List<Answer> answers = new LinkedList<Answer>();
		EasyMock.expect(criteria.list()).andReturn(answers);
		replayAll();

		final Answer actualAnswer = getBearcodeDAO().findSameAnswer(answer);

		assertNull("No answer is returned", actualAnswer);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.AnswerDAO#findSecondMailingList(java.util.Collection, java.util.Collection)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 18, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindSecondMailingList() {
		final Collection emails = Arrays.asList("Not sure what this should be");
		final Collection date = Arrays.asList(new Date());
		final List secondMailingList = Arrays.asList("Not sure what this should be");
		final Session session = addSessionToHibernateTemplate();
		final Query forSecondMailingList = createMock("ForSecondMailingList", Query.class);
		EasyMock.expect(session.getNamedQuery("secondMailingList")).andReturn(forSecondMailingList);
		EasyMock.expect(forSecondMailingList.setParameterList("emailList", emails)).andReturn(forSecondMailingList);
		EasyMock.expect(forSecondMailingList.setParameterList("createdList", date)).andReturn(forSecondMailingList);
		EasyMock.expect(forSecondMailingList.list()).andReturn(secondMailingList);
		replayAll();

		final Collection actualSecondMailingList = getBearcodeDAO().findSecondMailingList(emails, date);

		assertSame("The second mailing list is returned", secondMailingList, actualSecondMailingList);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.AnswerDAO#findUserAnswers(java.lang.Long[])}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@SuppressWarnings("rawtypes")
	@Test
	public final void testFindUserAnswers() {
		final long userId = 1782l;
		final Long[] userIds = new Long[] { userId };
		final Answer answer = createMock("Answer", Answer.class);
		final List<Answer> answers = Arrays.asList(answer);
		final Session session = addSessionToHibernateTemplate();
		final Query forUserAnswers = createMock("ForUserAnswers", Query.class);
		EasyMock.expect(session.getNamedQuery("userAnswers")).andReturn(forUserAnswers);
		// The real code actually passes a string in to the setParameterList method below. I'm not entirely certain how that is
		// supposed to work or why it even compiles.
		EasyMock.expect(forUserAnswers.setParameterList(EasyMock.eq("userIds"), (Object[]) EasyMock.anyObject())).andReturn(
				forUserAnswers);
		EasyMock.expect(forUserAnswers.list()).andReturn(answers);
		replayAll();

		final Collection actualAnswers = getBearcodeDAO().findUserAnswers(userIds);

		assertSame("The answers are returned", answers, actualAnswers);
		verifyAll();
	}

	/**
	 * Test method for {@link com.bearcode.ovf.DAO.AnswerDAO#findUserProfile(long)}.
	 * 
	 * @author IanBrown
	 * 
	 * @since Jul 17, 2012
	 * @version Jul 17, 2012
	 */
	@Test
	public final void testFindUserProfile() {
		final long userId = 4235l;
		final Answer answer = createMock("Answer", Answer.class);
		final List<Answer> userProfile = Arrays.asList(answer);
		EasyMock.expect(getHibernateTemplate().findByCriteria((DetachedCriteria) EasyMock.anyObject())).andReturn(userProfile);
		replayAll();

		final Collection<Answer> actualUserProfile = getBearcodeDAO().findUserProfile(userId);

		assertSame("The user profile is returned", userProfile, actualUserProfile);
		verifyAll();
	}

	/** {@inheritDoc} */
	@Override
	protected final AnswerDAO createBearcodeDAO() {
		return new AnswerDAO();
	}
}
