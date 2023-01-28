package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DataExportHistory;
import com.bearcode.ovf.model.registrationexport.DeliverySchedule;
import com.bearcode.ovf.model.registrationexport.ExportHistoryStatus;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by leonid on 26.09.16.
 */
@Repository
public class RegistrationExportDAO extends BearcodeDAO {

    public static final int LOOK_FOR_FIRST = 1;
    public static final int LOOK_FOR_LAST = 2;

    public List<DataExportHistory> findHistoriesForExport( DataExportConfiguration configuration ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportHistory.class )
                .add( Restrictions.eq( "status", ExportHistoryStatus.PREPARED ) )
                .add( Restrictions.eq( "exportConfig", configuration ) );
        // to do think about size limit

        return criteria.list();
    }

    public List<DataExportConfiguration> findConfigurations( DeliverySchedule deliverySechdule ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportConfiguration.class )
                .add( Restrictions.eq( "deliverySchedule", deliverySechdule ) )
                .add( Restrictions.eq( "enabled", true ) );
        return criteria.list();
    }

    public List<DataExportConfiguration> findConfigurationsForFace( FaceConfig face ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportConfiguration.class )
                .add( Restrictions.eq( "enabled", true ) );
        criteria.createCriteria( "faceConfigs" )
                .add( Restrictions.idEq( face.getId() ) );
        return criteria.list();
    }

    public DataExportConfiguration findConfiguration( Long id ) {
        return getHibernateTemplate().get( DataExportConfiguration.class, id );
    }

    public List<WizardResults> findNewResultsForConfiguration( DataExportConfiguration configuration ) {
        Criteria historyCriteria = getSessionFactory().getCurrentSession().createCriteria( DataExportHistory.class )
                .add( Restrictions.eq( "exportConfig", configuration ) )
                .addOrder( Order.desc( "creationDate" ) )
                .setMaxResults( 1 );
        DataExportHistory history = (DataExportHistory) historyCriteria.uniqueResult();
        List<String> faceUrls = new ArrayList<String>();
        for ( FaceConfig faceConfig : configuration.getFaceConfigs() ) {
            faceUrls.add( faceConfig.getUrlPath() );
        }
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( WizardResults.class )
                .add( Restrictions.eq( "flowType", FlowType.RAVA ) )
                .add( Restrictions.eq( "downloaded", true ) )
                .add( Restrictions.eq( "reportable", true ) );
        if ( faceUrls.size() > 0 ) {
            criteria.add( Restrictions.in( "faceUrl", faceUrls ) );
        }
        if ( history != null ) {
            criteria.add( Restrictions.gt( "creationDate", history.getCreationDate() ) );
        }

        return criteria.list();
    }

    public List<DataExportConfiguration> findAllConfigurations() {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportConfiguration.class );

        return criteria.list();
    }

    public long countHistories( DataExportConfiguration configuration, ExportHistoryStatus status ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportHistory.class )
                .add( Restrictions.eq( "exportConfig", configuration ) )
                .setProjection( Projections.rowCount() );
        if ( status != null ) {
            criteria.add( Restrictions.eq( "status", status ) );
        }
        return (Long) criteria.uniqueResult();
    }

    public DataExportHistory findOneHistory( DataExportConfiguration configuration, ExportHistoryStatus status, int lookFor ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportHistory.class )
                .add( Restrictions.eq( "exportConfig", configuration ) )
                .setMaxResults( 1 );
        if ( status != null ) {
            criteria.add( Restrictions.eq( "status", status ) );
        }
        if ( lookFor == LOOK_FOR_FIRST ) {
            criteria.addOrder( Order.asc( "creationDate" ) );
        }
        else {
            criteria.addOrder( Order.desc( "creationDate" ) );
        }

        return (DataExportHistory) criteria.uniqueResult();
    }

    public List<DataExportHistory> findHistoriesForChangeExport( DataExportConfiguration configuration, Date exportDate ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( DataExportHistory.class )
                .add( Restrictions.ne( "status", ExportHistoryStatus.PREPARED ) )
                .add( Restrictions.eq( "exportConfig", configuration ) )
                .add( Restrictions.gt( "creationDate", exportDate ) );
        return criteria.list();
    }
}
