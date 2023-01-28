package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.County;
import com.bearcode.ovf.model.common.Municipality;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.VotingRegion;
import com.bearcode.ovf.model.eod.LocalOfficeType;
import com.bearcode.ovf.model.eod.LocalOfficial;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.classic.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 5:36:32 PM
 * @author Leonid Ginzburg
 */
@Repository
public class VotingRegionDAO extends BearcodeDAO {

    private static final String REGIONS_QUERY = "select region from LocalOfficial leo " +
                    "inner join leo.region as region " +
                    "where region.state = :state and leo.localOfficeType = :type " +
                    "order by region.name asc";
    
    private static final String DELETE_UNUSED_MUNICIPALITIES =
    		"DELETE FROM municipalities" +
    		" WHERE state_id IN (:state_list)" +
    		"   AND NOT EXISTS (SELECT 1" +
    		"                     FROM voting_regions" +
    		"                    WHERE voting_regions.municipality_id = municipalities.id);";

    private static final String DELETE_UNUSED_COUNTIES =
    		"DELETE FROM counties" +
    		" WHERE state_id IN (:state_list)" +
    		"   AND NOT EXISTS (SELECT 1" +
    		"                     FROM voting_regions" +
    		"                    WHERE voting_regions.county_id = counties.id);";

    @SuppressWarnings("unchecked")
    public List<VotingRegion> getRegionsForState( State state ) {

        Query q = getSessionFactory().getCurrentSession().createQuery( REGIONS_QUERY )
                .setParameter( "state", state )
                .setParameter( "type", LocalOfficeType.ALL );
        return q.list();
    }

    @SuppressWarnings("unchecked")
    public List<VotingRegion> getRegionsForState( State state, LocalOfficeType type ) {

        Query q = getSessionFactory().getCurrentSession().createQuery( REGIONS_QUERY )
                .setParameter( "state", state )
                .setParameter( "type", type );
        return q.list();
    }

    public VotingRegion getById( long id ) {
        return (VotingRegion) getHibernateTemplate().get( VotingRegion.class, id );
    }

    public VotingRegion getRegionByName(VotingRegion region) {
        DetachedCriteria criteria = DetachedCriteria.forClass( VotingRegion.class )
                .add( Restrictions.eq( "name", region.getName() ) )
                .add( Restrictions.eq( "state", region.getState() ));
        List result = getHibernateTemplate().findByCriteria( criteria );
        return result.size() != 0 ?  (VotingRegion) result.iterator().next() : null ;  
    }

    public LocalOfficial findLeoByRegionId(long id) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( LocalOfficial.class )
                .add( Restrictions.eq("region.id", id ))
                .setMaxResults(1);
        LocalOfficial leo = (LocalOfficial) criteria.uniqueResult();
        if (leo != null) {
        	final VotingRegion region = leo.getRegion();
            getHibernateTemplate().initialize( region );
            getHibernateTemplate().initialize( region.getState() );
            final County county = region.getCounty();
            if (county != null) {
            	getHibernateTemplate().initialize(county);
            }
            final Municipality municipality = region.getMunicipality();
            if (municipality != null) {
            	getHibernateTemplate().initialize(municipality);
            }
            getHibernateTemplate().initialize( leo.getMailing() );
            getHibernateTemplate().initialize( leo.getPhysical() );
        }
        return leo;
    }

    /**
     * Finds a county belonging to the specified state by the county's name.
     * @param state the state containing the county.
     * @param countyName the name of the county.
     * @return the county or <code>null</code> if no county is found.
     */
	public County findCountyByStateAndName(final State state, final String countyName) {
        final Criteria criteria = getSessionFactory().getCurrentSession().createCriteria(County.class)
                .add( Restrictions.eq( "state.id", state.getId() ) )
                .add( Restrictions.eq( "name" , countyName ) )
                .setMaxResults(1);
        return (County) criteria.uniqueResult();
    }

	/**
	 * Cleans out the counties and municipalities that do not belong to any voting regions within the
	 * specified states.
	 * @param states the states.
	 */
	public void cleanCountiesAndMunicipalities(final Set<State> states) {
		final List<Long> stateIds = new ArrayList<Long>();
		for (final State state : states) {
			stateIds.add(state.getId());
		}
		
		final Session session = getSessionFactory().getCurrentSession();
		final SQLQuery deleteMunicipalities = session.createSQLQuery(DELETE_UNUSED_MUNICIPALITIES);
		deleteMunicipalities.setParameterList("state_list", stateIds);
		deleteMunicipalities.executeUpdate();

		final SQLQuery deleteCounties = session.createSQLQuery(DELETE_UNUSED_COUNTIES);
		deleteCounties.setParameterList("state_list", stateIds);
		deleteCounties.executeUpdate();
    }
}