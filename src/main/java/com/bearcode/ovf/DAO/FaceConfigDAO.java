package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.FaceFlowInstruction;
import com.bearcode.ovf.model.common.FaceFlowLogo;
import com.bearcode.ovf.model.mail.FaceMailingList;
import com.bearcode.ovf.model.questionnaire.FlowType;
import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Nov 6, 2007
 * Time: 8:15:35 PM
 *
 * @author Leonid Ginzburg
 */
@SuppressWarnings("unchecked")
@Repository
public class FaceConfigDAO extends BearcodeDAO {

    public FaceConfig findConfigByPath( final String path ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( FaceConfig.class )
                .add( Restrictions.or(
                        Restrictions.like( "urlPath", path ),
                        Restrictions.eq( "defaultPath", true )
                ) )
                .addOrder( Order.asc( "defaultPath" ) );
        return (FaceConfig) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
    }

    public FaceConfig findConfigByPrefix( final String prefix ) {
        final DetachedCriteria criteria = DetachedCriteria.forClass( FaceConfig.class )
                .add( Restrictions.or(
                        Restrictions.like( "relativePrefix", prefix ),
                        Restrictions.eq( "defaultPath", true ) ) )
                .addOrder( Order.asc( "defaultPath" ) );
        return (FaceConfig) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
    }

    public FaceConfig getDefaultConfig() {
        DetachedCriteria criteria = DetachedCriteria.forClass( FaceConfig.class )
                .add( Restrictions.eq( "defaultPath", true ) );
        return (FaceConfig) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
    }

    public FaceConfig getDefaultConfig( final FaceConfig config ) {
        DetachedCriteria criteria = DetachedCriteria.forClass( FaceConfig.class )
                .add( Restrictions.eq( "defaultPath", true ) )
                .add( Restrictions.not( Restrictions.idEq( config.getId() ) ) );
        return (FaceConfig) getHibernateTemplate().findByCriteria( criteria ).iterator().next();
    }

    public Collection<FaceConfig> findConfigs() {
        DetachedCriteria criteria = DetachedCriteria.forClass( FaceConfig.class ).addOrder( Order.asc( "relativePrefix" ) );
        return getHibernateTemplate().findByCriteria( criteria );
    }

    public FaceConfig findById( final long id ) {
        return getHibernateTemplate().get( FaceConfig.class, id );
    }

    public FaceFlowInstruction findInstructionById( final long id ) {
        return getHibernateTemplate().get( FaceFlowInstruction.class, id );
    }

    public FaceFlowInstruction findInstructionOfFlow( final FaceConfig faceConfig, final FlowType type ) {
        Criteria criteria = getSession().createCriteria( FaceFlowInstruction.class )
                .add( Restrictions.eq( "faceConfig", faceConfig ) )
                .add( Restrictions.eq( "flowType", type ) )
                .setMaxResults( 1 );
        return (FaceFlowInstruction) criteria.uniqueResult();
    }


    public Collection<FaceFlowInstruction> findInstructions( final FaceConfig faceConfig ) {
        Criteria criteria = getSession().createCriteria( FaceFlowInstruction.class )
                .add( Restrictions.eq( "faceConfig", faceConfig ) )
                .addOrder( Order.asc( "flowType" ) );
        return criteria.list();
    }

    public FaceFlowLogo findLogo( final FaceConfig faceConfig ) {
        Criteria criteria = getSession().createCriteria( FaceFlowLogo.class )
                .add( Restrictions.eq( "faceConfig", faceConfig ) )
                .setMaxResults( 1 );
        return (FaceFlowLogo) criteria.uniqueResult();
    }

    public Collection<FaceFlowInstruction> findAllInstructions() {
        return getSession().createCriteria( FaceFlowInstruction.class ).list();
    }

    public FaceMailingList findFaceMailingList( FaceConfig faceConfig ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( FaceMailingList.class )
                .add( Restrictions.eq( "face", faceConfig ) )
                .setMaxResults( 1 );
        return (FaceMailingList) criteria.uniqueResult();
    }
}
