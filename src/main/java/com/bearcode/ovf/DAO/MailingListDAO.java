package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.eod.LocalOfficial;
import com.bearcode.ovf.model.mail.*;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.webservices.sendgrid.model.SendGridLogMessage;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.hibernate.type.StringType;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Date: 05.07.12
 * Time: 17:19
 *
 * @author Leonid Ginzburg
 */
@Repository
@SuppressWarnings("unchecked")
public class MailingListDAO extends BearcodeDAO {

    public List<MailingList> findAll() {
        return getHibernateTemplate().loadAll( MailingList.class );
    }

    public MailingList findByFieldType( FieldType fieldType ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingList.class )
                .add( Restrictions.eq( "fieldType", fieldType ) )
                .setMaxResults( 1 );
        return (MailingList) criteria.uniqueResult();
    }

    public MailingList findById( Long id ) {
        return getHibernateTemplate().get( MailingList.class, id );
    }

    public MailingLink findLinkByListAndAddress( final MailingList mailingList,
                                                 final MailingAddress mailingAddress ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingLink.class )
                .add( Restrictions.eq( "mailingList", mailingList ) )
                .add( Restrictions.eq( "mailingAddress", mailingAddress ) )
                .setMaxResults( 1 );
        return (MailingLink) criteria.uniqueResult();
    }

    public List<MailingLink> findNewLinks() {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingLink.class )
                .add( Restrictions.eq( "status", MailingLinkStatus.NEW ) );
        return criteria.list();
    }

    public List<String> findMailingApiKeys() {
        String sql = "SELECT DISTINCT ml.apiKey  FROM MailingList ml";
        Query query = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery( sql );
        return (List<String>) query.list();

    }

    public MailingLink findLinkByCompaignAndEmail( String apiKey, String campaignId, String email ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingLink.class )
                .createAlias( "mailingAddress", "mailingAddress" )
                .createAlias( "mailingList", "mailingList" )
                .add( Restrictions.eq( "mailingList.apiKey", apiKey ) )
                .add( Restrictions.eq( "mailingList.campaignId", campaignId ) )
                .add( Restrictions.eq( "mailingAddress.email", email ) )
                .setMaxResults( 1 );
        return (MailingLink) criteria.uniqueResult();
    }

    public MailingLink findLinkByListAndEmail( Long mailingListId, String email ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingLink.class )
                .createAlias( "mailingAddress", "mailingAddress" )
                .createAlias( "mailingList", "mailingList" )
                .add( Restrictions.eq( "mailingList.id", mailingListId ) )
                .add( Restrictions.eq( "mailingAddress.email", email ) )
                .setMaxResults( 1 );
        return (MailingLink) criteria.uniqueResult();
    }


    public List<LocalOfficial> findLeoForMailing( MailingList list, int maxResults ) {
        String sql = "select  leo from LocalOfficial leo " +
                "where leo.leoEmail not in (select address.email from  MailingLink link " +
                "join link.mailingAddress address " +
                "where link.mailingList = :list ) " +
                "and char_length(trim(leo.leoEmail)) > 0 ";
        Query query = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery( sql )
                .setParameter( "list", list )
                .setMaxResults( maxResults );
        return query.list();
    }

    public List<LocalOfficial> findLovcForMailing( MailingList list, int maxResults ) {
        String sql = "select  leo from LocalOfficial leo " +
                "where leo.lovcEmail not in (select address.email from  MailingLink link " +
                "join link.mailingAddress address " +
                "where link.mailingList = :list ) "+
                "and char_length(trim(leo.lovcEmail)) > 0 ";
        Query query = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery( sql )
                .setParameter( "list", list )
                .setMaxResults( maxResults );
        return query.list();
    }

    public void removeAllLinks( Collection<MailingList> mailingListsToDelete ) {
        if ( !mailingListsToDelete.isEmpty() ) {
            String sql = "delete from MailingLink where mailingList in (:lists)";
            getHibernateTemplate().getSessionFactory().getCurrentSession()
                    .createQuery( sql )
                    .setParameterList( "lists", mailingListsToDelete )
                    .executeUpdate();

            sql = "delete from FaceMailingList where mailingList in (:lists)";
            getHibernateTemplate().getSessionFactory().getCurrentSession()
                    .createQuery( sql )
                    .setParameterList( "lists", mailingListsToDelete )
                    .executeUpdate();
        }

    }


    public List countLinks( MailingLinkStatus... linkStatus ) {
        StringBuilder sql = new StringBuilder( "select link.mailingList.id, count(link.id) as link_count from MailingLink link ");
        for( int i=0; i < linkStatus.length; i++) {
            sql.append( i == 0 ? "where " : " or " )
                    .append( "link.status = :status" )
                    .append( i );
        }
        sql.append( " group by mailingList" );
        Query query = getHibernateTemplate().getSessionFactory().getCurrentSession().createQuery( sql.toString() );
        for( int i=0; i < linkStatus.length; i++ ) {
            query.setParameter( String.format( "status%d", i), linkStatus[i] );
        }
        return query.list();
    }

    public List<MailingLink> findLinks( MailingList mailingList, long offset, long pageSize ) {
        Criteria criteria = /*getSessionFactory().getCurrentSession()*/getSession().createCriteria( MailingLink.class )
                .add( Restrictions.eq( "mailingList", mailingList ) )
                .add( Restrictions.eq( "status", MailingLinkStatus.SUBSCRIBED ) )
                .addOrder( Order.asc( "id" ) )
                .setFirstResult( (int) offset )
                .setMaxResults( (int) pageSize );

        return criteria.list();
    }


    public List<MailingTask> findTasks() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( MailingTask.class )
                .add( Restrictions.lt( "startOn", new Date() ) )
                .add( Restrictions.eq( "status", MailingTask.STATUS_READY ) );
        return criteria.list();
    }

    public List<String> findLastEmailsWithErrors() {
        String sql = "SELECT email.to_addr FROM email join (" +
                "SELECT to_addr, max(created_time) created FROM email group by to_addr ) b " +
                "WHERE email.to_addr = b.to_addr AND email.created_time = b.created AND email.status = 'ERROR'";
        SQLQuery query = getSessionFactory().getCurrentSession().createSQLQuery( sql )
                .addScalar( "to_addr", StringType.INSTANCE );
        return query.list();

    }

    public List<MailingLink> findLinksByEmails( List<String> emails ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( MailingLink.class )
                .createAlias( "mailingAddress", "mailingAddress" )
                .add( Restrictions.in( "mailingAddress.email", emails ) );
        return  criteria.list();
    }

    public List<MailingTask> findAllTasks() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( MailingTask.class )
                .addOrder( Order.desc( "startOn" ) );
        return criteria.list();
    }

    public MailingTask findMailingTaskById( Long taskId ) {
        return getHibernateTemplate().load( MailingTask.class, taskId );
    }

    public MailingSemaphore findSemaphore() {
        List<MailingSemaphore> all = getHibernateTemplate().loadAll( MailingSemaphore.class );
        if ( all.isEmpty() ) {
            return new MailingSemaphore();
        }
        return all.get( 0 );
    }

    public MailingTask findFirstTask() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( MailingTask.class )
                .add( Restrictions.eq( "status", MailingTask.STATUS_READY ) )
                .addOrder( Order.desc( "startOn" ) )
                .setMaxResults( 1 );
        return (MailingTask) criteria.uniqueResult();
    }

    public long countLinksOfList( MailingList list ) {
        Criteria criteria = /*getSessionFactory().getCurrentSession()*/getSession().createCriteria( MailingLink.class )
                .add( Restrictions.eq( "mailingList", list ) )
                .setProjection( Projections.rowCount() );
        return (Long) criteria.uniqueResult();
    }

    public List<FaceMailingList> findFacesForMailingList( MailingList mailingList ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( FaceMailingList.class )
                .add( Restrictions.eq( "mailingList", mailingList ) );
        return criteria.list();
    }

    public FaceMailingList findMailingListOfFace( FaceConfig face ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( FaceMailingList.class )
                .add( Restrictions.eq( "face", face ) )
                .setMaxResults( 1 );
        return (FaceMailingList) criteria.uniqueResult();
    }

    public List<MailingLink> findLinksByStatus( MailingLinkStatus status, int limit ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingLink.class )
                .add( Restrictions.eq( "status", status ) );
        if ( limit > 0 ) {
            criteria.setMaxResults( limit );
        }
        return criteria.list();
    }

    public SendGridMark findLastMark() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( SendGridMark.class )
                .addOrder( Order.desc( "lastRun" ) )
                .setMaxResults( 1 );
        return (SendGridMark) criteria.uniqueResult();
    }

    public List<MailingLinkStats> findMailingLinkStatistic( Date from ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( MailingLink.class )
                .add( Restrictions.gt( "lastUpdated", from ) )
                .setProjection(
                        Projections.projectionList()
                                .add( Projections.groupProperty( "status" ), "status" )
                                .add( Projections.groupProperty( "errorCount" ), "errorCount" )
                                .add( Projections.groupProperty( "errorMessage" ), "errorMessage" )
                                .add( Projections.rowCount(), "rowCount" )
                )
                .setResultTransformer( Transformers.aliasToBean( MailingLinkStats.class ) );
        return criteria.list();
    }

    public List<SendGridLogMessage> findSendGridMessages() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( SendGridLogMessage.class )
                .addOrder( Order.desc( "logDate" ) )
                .setMaxResults( 100 );
        return criteria.list();
    }

    public List<MailingLink> findMailingLinksForChange( Date fromDate, MailingLinkStatus status, int limit ) {
        Criteria criteria = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createCriteria( MailingLink.class )
                .add( Restrictions.ge( "lastUpdated", fromDate ) )
                .add( Restrictions.eq( "status", status ) );
        if ( limit > 0 ) {
            criteria.setMaxResults( limit );
        }
        return criteria.list();
    }
}
