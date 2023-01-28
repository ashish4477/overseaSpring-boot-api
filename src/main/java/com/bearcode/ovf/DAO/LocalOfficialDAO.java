package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.commons.DAO.PagingInfo;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.*;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 5:37:37 PM
 * @author Leonid Ginzburg
 */
@SuppressWarnings("unchecked")
@Repository
public class LocalOfficialDAO extends BearcodeDAO {
    public LocalOfficial findLeoByRegion( VotingRegion region ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( LocalOfficial.class )
                .add( Restrictions.eq("region", region ))
                .setMaxResults(1);
        return (LocalOfficial) criteria.uniqueResult();
    }

    public LocalOfficial getById(long id) {
        return getHibernateTemplate().get( LocalOfficial.class, id );
    }

    public Collection<LocalOfficial> findLeoByState(State state) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( LocalOfficial.class )
                .createAlias("region", "region")
                .add(Restrictions.eq("region.state", state ));
        return criteria.list();

    }

    public Collection<LocalOfficial> findLeoByState(long stateId, PagingInfo paging ) {
        return findLeoByState( stateId, paging, null );
    }

    public Collection<LocalOfficial> findLeoByState(long stateId, PagingInfo paging, VotingRegion lookFor ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( LocalOfficial.class )
                .createAlias("region", "region")
                .createAlias("region.state", "state")
                .add(Restrictions.eq("state.id", stateId ));
        Long actualRows = calculateRows( criteria, paging );
        if ( lookFor != null  ) {
            criteria = DetachedCriteria.forClass( LocalOfficial.class )
                    .createAlias("region", "region")
                    .createAlias("region.state", "state")
                    .add(Restrictions.eq("state.id", stateId ))
            .add( Restrictions.le( "region.name", lookFor.getName() ) );
            paging.setFirstResult( actualRows.intValue()+1 );  // It's kind of trick. First result is set out of boundary.
            calculateRows( criteria, paging );                 //  It will be adjusted to page of looking region
            paging.setActualRows( actualRows );
        }
        criteria = DetachedCriteria.forClass( LocalOfficial.class )
                .createAlias("region", "region")
                .createAlias("region.state", "state")
                .add(Restrictions.eq("state.id", stateId ))
                .addOrder( Order.asc("region.name") );
        return findBy( criteria, paging );
    }

    public Collection findStateStatistics( PagingInfo pagingInfo ) {
        Long rows = (Long) getHibernateTemplate().
                find("select count(*) from State ").iterator().next();
        adjustPagingInfo( pagingInfo, rows.longValue() );

        String query = "select state.name, count(region), sum( leo.status ), state from LocalOfficial as leo " +
                "right join leo.region as region " +
                "right join region.state as state " +
                "group by state " +
                "order by state.name";
        Query select = getHibernateTemplate().getSessionFactory().getCurrentSession()
                .createQuery( query )
                .setMaxResults( pagingInfo.getMaxResults() )
                .setFirstResult( pagingInfo.getFirstResult() );
        return select.list();
    }

    public Collection findAll() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( LocalOfficial.class );
        return criteria.list();  
    }

    public boolean checkLeoGotChanged() {
        String query = "select count(*) as updated_count from local_officials " +
                "where updated >= DATE_SUB( NOW(), INTERVAL 1 DAY) ";
        Query check = getSessionFactory().getCurrentSession().createSQLQuery( query )
                .addScalar("updated_count", Hibernate.INTEGER);
        Object res = check.uniqueResult();
        if ( res == null || res.equals(0) ) return false;
        return true;
    }
    
    public void initialize( LocalOfficial leo ) {
        getHibernateTemplate().initialize( leo.getMailing() );
        getHibernateTemplate().initialize( leo.getPhysical() );
    }

    public StateSpecificDirectory findSvidForState(State state) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( StateSpecificDirectory.class );
        criteria.add( Restrictions.eq( "state", state ) )
                .setMaxResults( 1 );
        return (StateSpecificDirectory) criteria.uniqueResult();
    }

    public StateSpecificDirectory findSvidForState(long stateId) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( StateSpecificDirectory.class );
        criteria.createAlias("state", "state")
                .add( Restrictions.eq( "state.id", stateId ) )
                .setMaxResults(1);
        return (StateSpecificDirectory) criteria.uniqueResult();
    }

    public StateSpecificDirectory getSvidById(long svidId) {
        return (StateSpecificDirectory) getHibernateTemplate().get( StateSpecificDirectory.class, svidId );
    }

    public Election findElection(long electionId) {
        return (Election) getHibernateTemplate().get( Election.class, electionId );  
    }

    public Collection search(List<String> params, List<String> states) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( LocalOfficial.class )
                .createAlias("region", "region")
                .createAlias("region.state", "state");
        if ( params.size() > 0 ) {
            Junction namesCriterion = Restrictions.disjunction();
            for ( String param : params ) {
                namesCriterion
                        .add( Restrictions.like("region.name", "% "+param+" %"))
                        .add( Restrictions.like("region.name", param+" %"))
                        .add( Restrictions.like("region.name", "% "+param));
            }
            criteria.add( namesCriterion );
        }

        if ( states.size() > 0 ) {
            Junction statesCriterion = Restrictions.disjunction();
            for ( String state : states ) {
                statesCriterion.add( Restrictions.eq("state.abbr",state));
            }
            criteria.add( statesCriterion );
        }

        return (Collection<LocalOfficial>)criteria.list();
    }

    public Collection<LocalOfficial> search( List<String> params, List<String> states, PagingInfo pagingInfo ) {
        calculateRows( buildSearchCriteria( params, states ), pagingInfo );
        return findBy( buildSearchCriteria( params, states ), pagingInfo );
    }

    private DetachedCriteria buildSearchCriteria( List<String> params, List<String> states ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( LocalOfficial.class )
                .createAlias("region", "region")
                .createAlias("region.state", "state");
        if ( params.size() > 0 ) {
            Junction namesCriterion = Restrictions.disjunction();
            for ( String param : params ) {
                namesCriterion
                        .add( Restrictions.like("region.name", "% "+param+" %"))
                        .add( Restrictions.like("region.name", param+" %"))
                        .add( Restrictions.like("region.name", "% "+param));
            }
            criteria.add( namesCriterion );
        }

        if ( states.size() > 0 ) {
            Junction statesCriterion = Restrictions.disjunction();
            for ( String state : states ) {
                statesCriterion.add( Restrictions.eq("state.abbr",state));
            }
            criteria.add( statesCriterion );
        }
        return criteria;
    }

    public void makePersistent( Officer object ) throws DataAccessException {
        getHibernateTemplate().saveOrUpdate( "Officer", object );
    }

    public StateVotingLaws getStateVotingLaws( long id ) {
        return getHibernateTemplate().get( StateVotingLaws.class, id );
    }

    public StateVotingLaws findVotingLawsByState( State state ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( StateVotingLaws.class )
                .add( Restrictions.eq( "state", state ) )
                .setMaxResults(1);
        return (StateVotingLaws) criteria.uniqueResult();
    }

    public StateVotingLaws findVotingLawsByStateAbbreviation( String state ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( StateVotingLaws.class )
                .createAlias( "state", "state" )
                .add( Restrictions.eq( "state.abbr", state ) )
                .setMaxResults(1);
        return (StateVotingLaws) criteria.uniqueResult();
    }

    public long countStateWithNoExcuse() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( StateVotingLaws.class )
                .add( Restrictions.eq( "noExcuseAbsenteeVoting", true ) )
                .setProjection( Projections.count( "id" ) );
        return (Long) criteria.uniqueResult();
    }

    public Collection<StateVotingLaws> findAllStateVotingLaws() {
        return getSessionFactory().getCurrentSession().createCriteria( StateVotingLaws.class ).list();
    }

    public Collection<Election> findAllElections() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( Election.class )
                .createAlias("stateInfo", "stateInfo")
                .addOrder( Order.asc( "stateInfo.id") )
                .addOrder( Order.asc( "order") );
        return criteria.list();
    }

    public Collection<AdditionalAddressType> findAdditionalAddressTypes() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( AdditionalAddressType.class )
                .addOrder( Order.asc( "name" ) );
        return criteria.list();
    }

    public AdditionalAddressType findAdditionalAddressType( long id ) {
        return getHibernateTemplate().get( AdditionalAddressType.class, id );
    }
}
