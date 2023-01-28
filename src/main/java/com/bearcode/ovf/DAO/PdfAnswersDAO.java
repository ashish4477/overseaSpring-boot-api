package com.bearcode.ovf.DAO;

import com.bearcode.commons.DAO.BearcodeDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.questionnaire.FieldType;
import com.bearcode.ovf.model.questionnaire.FlowType;
import com.bearcode.ovf.model.questionnaire.QuestionField;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jun 20, 2007
 * Time: 5:37:06 PM
 *
 * @author Leonid Ginzburg
 */
@Repository
@SuppressWarnings("unchecked")
public class PdfAnswersDAO extends BearcodeDAO {


    public Collection<WizardResults> getUserPdfs( OverseasUser user ) {

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( WizardResults.class )
                .add( Restrictions.eq( "user", user ) );
        return criteria.list();
    }

    /**
     * Returns a list of WizardResults that have an answer for the "Add me into mail list" with the given value
     * and were created within the given date range (inclusive)
     *
     * @param value
     * @param after
     * @param before
     * @return List<WizardResults>
     */
    public List<WizardResults> findByFieldValue( String value, Date after, Date before ) {
        Query query = getSessionFactory().getCurrentSession().getNamedQuery( "wizardresultsWithFieldValue" );
        if ( after == null ) {
            after = new Date( Long.MIN_VALUE );
        }
        if ( before == null ) {
            before = new Date();
        }
        query.setParameter( "typeName", FieldType.MAILING_LIST_TYPE_NAME + "%" );
        query.setParameter( "value", value );
        query.setParameter( "before", before );
        query.setParameter( "after", after );
        return (List<WizardResults>) query.list();
    }

    /**
     * Returns a list of WizardResults that have an answer for the given field with the given selectedValue
     * and were created within the given date range (inclusive)
     *
     * @param field
     * @param value
     * @param after
     * @param before
     * @return
     */
    public List<WizardResults> findByFieldSelectedValue( QuestionField field, int value, Date after, Date before ) {
        Query query = getSessionFactory().getCurrentSession().getNamedQuery( "wizardresultsWithFieldSelectedValue" );
        query.setParameter( "field", field.getId() );
        query.setParameter( "value", value );
        query.setParameter( "before", before );
        query.setParameter( "after", after );
        return (List<WizardResults>) query.list();
    }

    public List<WizardResults> findForPendingConfiguration( PendingVoterRegistrationConfiguration configuration,
                                                            Date startDate,
                                                            Date endDate,
                                                            boolean useLimit ) {
        Criteria criteriaFace = getSessionFactory().getCurrentSession().createCriteria( FaceConfig.class )
                .add( Restrictions.eq( "relativePrefix", configuration.getFacePrefix() ) );
        List<FaceConfig> configs = criteriaFace.list();
        Criterion faceRestriction = null;
        if ( configs.size() == 1 ) {
            faceRestriction = Restrictions.eq( "faceUrl", configs.get( 0 ).getUrlPath() );
        }
        else if ( configs.size() > 1 ) {
            for ( FaceConfig config : configs ) {
                Criterion oneRestriction = Restrictions.eq( "faceUrl", config.getUrlPath() );
                if ( faceRestriction != null ) {
                    faceRestriction = Restrictions.or( faceRestriction, oneRestriction );
                }
                else {
                    faceRestriction = oneRestriction;
                }
            }
        }
        else {
            return Collections.<WizardResults>emptyList();
        }

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( WizardResults.class )
                .add( faceRestriction )
                .add( Restrictions.eq( "flowType", FlowType.RAVA ) )
                .add( Restrictions.eq( "downloaded", true ) );
        if ( startDate != null ) {
            criteria.add( Restrictions.gt( "creationDate", startDate ) );
        }
        if ( endDate != null ) {
            criteria.add( Restrictions.le( "creationDate", endDate ) );
        }
        if ( useLimit ) {
            criteria.setMaxResults( 50 );
        }
        return criteria.list();
    }

    public Long countForPendingConfiguration( PendingVoterRegistrationConfiguration configuration, Date startDate, Date endDate ) {
        Criteria criteriaFace = getSessionFactory().getCurrentSession().createCriteria( FaceConfig.class )
                .add( Restrictions.eq( "relativePrefix", configuration.getFacePrefix() ) );
        List<FaceConfig> configs = criteriaFace.list();
        Criterion faceRestriction = null;
        if ( configs.size() == 1 ) {
            faceRestriction = Restrictions.eq( "faceUrl", configs.get( 0 ).getUrlPath() );
        }
        else if ( configs.size() > 1 ) {
            for ( FaceConfig config : configs ) {
                Criterion oneRestriction = Restrictions.eq( "faceUrl", config.getUrlPath() );
                if ( faceRestriction != null ) {
                    faceRestriction = Restrictions.or( faceRestriction, oneRestriction );
                }
                else {
                    faceRestriction = oneRestriction;
                }
            }
        }
        else {
            return 0L;
        }

        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( WizardResults.class )
                .add( faceRestriction )
                .add( Restrictions.eq( "flowType", FlowType.RAVA ) )
                .add( Restrictions.eq( "downloaded", true ) );
        if ( startDate != null ) {
            criteria.add( Restrictions.gt( "creationDate", startDate ) );
        }
        if ( endDate != null ) {
            criteria.add( Restrictions.le( "creationDate", endDate ) );
        }
        criteria.setProjection( Projections.rowCount() );
        return (Long) criteria.uniqueResult();
    }

    public List<WizardResults> findWizardResults( List<Long> resultIds ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( WizardResults.class )
                .add( Restrictions.in( "id", resultIds ) );
        return criteria.list();
    }

    public List<WizardResults> findWizardResults( String uuid ) {
        Criteria criteria = getSessionFactory().getCurrentSession().createCriteria( WizardResults.class )
                .add( Restrictions.eq( "uuid", uuid ) );
        return criteria.list();
    }
}
