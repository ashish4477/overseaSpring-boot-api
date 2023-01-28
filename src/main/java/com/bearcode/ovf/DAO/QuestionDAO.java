package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.questionnaire.*;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 15, 2007
 * Time: 4:02:39 PM
 *
 * @author Leonid Ginzburg
 */
@Repository
@SuppressWarnings("unchecked")
public class QuestionDAO extends BearcodeDAO {

    public Collection<Question> findQuestionsAfterQuestion( Question question, int number ) {
        return getSession().createCriteria( Question.class )
                .add( Restrictions.eq( "page", question.getPage() ) ).
                        add( Restrictions.ge( "order", number ) ).list();
    }

    public Question findQuestionById( long id ) {
        return getHibernateTemplate().get( Question.class, id );
    }

    public QuestionVariant findQuestionVariantById( long id ) {
        return getHibernateTemplate().get( QuestionVariant.class, id );
    }

    public BasicDependency findQuestionDependencyById( long dependencyId ) {
        return (BasicDependency) getSession().createCriteria( BasicDependency.class ).
                add( Restrictions.eq( "id", dependencyId ) ).
                uniqueResult();
    }

    public Collection<Question> findQuestionForDependency( Question question ) {
/*
        DetachedCriteria criteria = DetachedCriteria.forClass( Question.class )
                .createAlias("page", "page")
                .add( Restrictions.lt("page.number", question.getPage().getNumber()));
*/
        String sql = "select question from Question as question " +
                "join question.page as page " +
                "join  question.variants as variant " +
                "join  variant.fields as field " +
                "where page.number < :pageNumber " +
                "and page.type = :pageType " +
                "group by question " +
                "having count(variant) = 1 and count(field) = 1";


        return getHibernateTemplate().findByNamedParam( sql,
                new String[]{"pageNumber", "pageType"},
                new Object[]{question.getPage().getNumber(), question.getPage().getType()}
        );
    }

    public Collection<Question> findQuestionForDependency() {
/*
        DetachedCriteria criteria = DetachedCriteria.forClass( Question.class )
                .createAlias("page", "page")
                .add( Restrictions.lt("page.number", question.getPage().getNumber()));
*/
        String sql = "select question from Question as question " +
                "join question.page as page " +
                "join  question.variants as variant " +
                "join  variant.fields as field " +
                "group by question " +
                "having count(variant) = 1 and count(field) = 1";


        return getHibernateTemplate().find( sql );
    }

    public boolean checkQuestionUsing( Question question ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( QuestionDependency.class )
                .add( Restrictions.eq( "dependsOn", question ) )
                .setProjection( Projections.rowCount() );
        return ((Long) getHibernateTemplate().findByCriteria( criteria ).iterator().next()) > 0;
    }

    public Collection<QuestionDependency> findDependents( Question question ) {
/*
        String sql = " from Question " +
                "join QuestionDependency"
*/
        DetachedCriteria criteria = DetachedCriteria.forClass( QuestionDependency.class )
                .add( Restrictions.eq( "dependsOn", question ) );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public Collection<BasicDependency> findQuestionDependencies( Related dependent, Question dependsOn ) {
        Criteria criteria = getSession().createCriteria( QuestionDependency.class )
                .add( Restrictions.eq( "dependent", dependent ) )
                .add( Restrictions.eq( "dependsOn", dependsOn ) )
                .createAlias( "condition", "condition" )
                .addOrder( Order.asc( "condition.value" ) );
        return criteria.list();
    }

    public Collection<BasicDependency> findUserFieldDependencies( Related dependent, String fieldName ) {
        return getSession().createCriteria( UserFieldDependency.class )
                .add( Restrictions.eq( "dependent", dependent ) )
                .add( Restrictions.eq( "fieldName", fieldName ) )
                .addOrder( Order.asc( "fieldValue" ) )
                .list();
    }

    public Collection<BasicDependency> findFaceDependencies( Related dependent ) {
        return getSession().createCriteria( FaceDependency.class )
                .add( Restrictions.eq( "dependent", dependent ) )
                .createAlias( "dependsOn", "face" )
                .addOrder( Order.asc( "face.relativePrefix" ) )
                .list();
    }

    public Collection<BasicDependency> findFlowDependencies( Related dependent ) {
        return getSession().createCriteria( FlowDependency.class )
                .add( Restrictions.eq( "dependent", dependent ) )
                .addOrder( Order.asc( "flowType" ) )
                .list();
    }

    /**
     * Finds the dependent variants for the question.
     *
     * @param question the question.
     * @return the dependent variants for the question.
     * @author IanBrown
     * @version May 25, 2012
     * @since May 25, 2012
     */
    public Collection<QuestionVariant> findDependentVariants( Question question ) {
        final String sql = "SELECT variant "
                + "FROM QuestionVariant AS variant "
                + "JOIN variant.keys AS dependency "
                + "WHERE dependency.dependsOn.id=" + question.getId();
        return getHibernateTemplate().find( sql );
    }

    public Collection<Question> findQuestionsOfPageType( PageType pageType ) {
        Criteria criteria = getSession().createCriteria( Question.class )
                .createAlias( "page", "page" )
                .add( Restrictions.eq( "page.type", pageType ) )
                .addOrder( Order.asc( "page.number" ) )
                .addOrder( Order.asc( "order" ) );
        return criteria.list();
    }
}
