package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.questionnaire.*;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Aug 14, 2007
 * Time: 2:34:18 PM
 * @author Leonid Ginzburg
 */
@SuppressWarnings("unchecked")
@Repository
public class QuestionFieldDAO extends BearcodeDAO {
    public QuestionField getById( long id ) {
        return (QuestionField) getSession().get( QuestionField.class, id );
    }

    public Collection<QuestionField> findFieldsBeforeNumber(QuestionField field, int number) {
        DetachedCriteria criteria = DetachedCriteria.forClass( QuestionField.class )
                .add(Restrictions.eq("question", field.getQuestion() ))
                .add(Restrictions.ge("order", number ))
                .addOrder( Order.asc("order") );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public FieldDictionaryItem getDictionaryItemById(long itemId) {
        return getHibernateTemplate().get( FieldDictionaryItem.class, itemId );
    }

    public boolean checkFieldUsing(QuestionField field) {
        DetachedCriteria criteria = DetachedCriteria.forClass(QuestionDependency.class)
                .add( Restrictions.eq("dependsOn", field.getQuestion().getQuestion() ));
        return getHibernateTemplate().findByCriteria( criteria ).size() > 0;
    }

    public boolean checkOptionUsing( FieldDictionaryItem option ) {
        DetachedCriteria criteria = DetachedCriteria.forClass(QuestionDependency.class);
        criteria.add( Restrictions.eq("condition", option ));
        return getHibernateTemplate().findByCriteria( criteria ).size() > 0;
    }

    public FieldDependency getFieldDependencyById(long dependencyId) {
        return (FieldDependency) getHibernateTemplate().get( FieldDependency.class, dependencyId);
    }

    public Collection<FieldDependency> findFieldDependencies() {
        DetachedCriteria criteria = DetachedCriteria.forClass( FieldDependency.class );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public FieldDependency getDependencyForField(QuestionField field) {
        DetachedCriteria criteria = DetachedCriteria.forClass( FieldDependency.class )
                .add( Restrictions.eq( "dependent", field ));
        Collection<FieldDependency> deps = getHibernateTemplate().findByCriteria( criteria );
        if ( deps.size() > 0 ) return deps.iterator().next();
        return null;  
    }

    public Collection<QuestionField> findAnswerFiedsOfPage(Collection<Long> answerFields, QuestionnairePage page) {
        DetachedCriteria criteria = DetachedCriteria.forClass( QuestionField.class )
                .add( Restrictions.in("id", answerFields) )
                .createAlias("question", "variant")
                .createAlias("variant.question", "myquestion")
                .createAlias("myquestion.page", "mypage")
                .add( Restrictions.eq("mypage.number", page.getNumber() ));
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public Collection<QuestionField> findReportableFields() {
        final String[] reportableTemplates = new String[] {
                FieldType.TEMPLATE_CHECKBOX,
                FieldType.TEMPLATE_CHECKBOX_FILLED,
                FieldType.TEMPLATE_RADIO,
                FieldType.TEMPLATE_SELECT
        };
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( QuestionField.class )
                .createAlias("type", "type")
                .createAlias("question", "variant")
                .add( Restrictions.in("type.templateName", reportableTemplates ))
                .addOrder( Order.asc("variant.title"));
        return criteria.list();
    }

    public Collection<FieldDictionaryItem> findDictionaryItems(Collection selectedIds) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( FieldDictionaryItem.class )
                .add( Restrictions.in("id", selectedIds ));
        return criteria.list();
    }

    public List<QuestionField> findMailInFields() {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( QuestionField.class )
                .createAlias( "type", "type" )
                .add( Restrictions.like( "type.name", "%mail-in" ) );
        return criteria.list();
    }
}
