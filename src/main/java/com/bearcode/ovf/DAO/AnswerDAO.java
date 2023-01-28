package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.questionnaire.Answer;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Oct 2, 2007
 * Time: 6:02:44 PM
 */
@Repository
public class AnswerDAO extends BearcodeDAO {

    public Collection<Answer> findUserProfile( long userId ) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Answer.class)
                .add(Restrictions.eq("pdf.user.id", userId));
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public Collection findFirstMailingList(Date fromDate, Date toDate, List<Long> mailingFieldId, int startFrom) {
        Query query = getSession().getNamedQuery("forMailingList");
        query.setTimestamp("fromDate", fromDate )
        		.setTimestamp("toDate", toDate)
                .setParameterList( "fieldId", mailingFieldId )
                .setInteger( "start", startFrom );
        return query.list();
    }

    public Collection findSecondMailingList(Collection emails, Collection date) {
        Query query = getSession().getNamedQuery("secondMailingList");
        query.setParameterList( "emailList", emails )
                .setParameterList( "createdList", date );
        return query.list();  
    }

    public Collection findUserAnswers(Long[] userIds) {
		String idStr = "0";
		for(int i=0; i<userIds.length; i++){
			idStr += ","+userIds[i];
		}
        Query query = getSession().getNamedQuery("userAnswers");
		query.setParameterList("userIds", userIds);
         return query.list();
    }

    public Answer findSameAnswer(Answer answer) {
        Criteria criteria = getSession().createCriteria( Answer.class )
                .add( Restrictions.eq( "pdf", answer.getWizardResults() ))
                .add( Restrictions.eq( "field", answer.getField() ))
                .add( Restrictions.not( Restrictions.idEq( answer.getId() ) ));
        List result = criteria.list();
        if ( !result.isEmpty() ) {
            return (Answer) result.iterator().next();
        }
        return null;
    }
}
