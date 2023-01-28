package com.bearcode.ovf.tools.pendingregistration;

import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.State;
import com.bearcode.ovf.model.common.UserRole;
import com.bearcode.ovf.model.pendingregistration.PendingVoterRegistration;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.service.PendingVoterRegistrationService;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.service.StateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Date: 20.10.14
 * Time: 18:16
 *
 * @author Leonid Ginzburg
 */
@Component
public class OnlineDataTransferExecutor {
    private static Logger logger = LoggerFactory.getLogger( OnlineDataTransferExecutor.class );

    @Autowired
    private PendingVoterRegistrationService pendingVoterRegistrationService;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private PendingVoterRegistrationUtil pendingVoterRegistrationUtil;

    @Autowired
    private StateService stateService;

    @Autowired
    private PendingVoterRegistrationCipher pendingVoterRegistrationCipher;

    @Autowired
    private OnlineDataTransferCache onlineDataTransferCache;


    @Async
    public void createDataTransferCsv( final PendingVoterRegistrationConfiguration configuration,
                                       final String id,
                                       final OverseasUser user,
                                       final boolean all) {
        DataPreparationStatus status = getOnlineDataTransferCache().createStatus( id );
        status.setStatus(DataPreparationStatus.IN_PROGRESS);
        try {
            final List<PendingVoterRegistration> originalRegistrations = new LinkedList<PendingVoterRegistration>();
            originalRegistrations.addAll( getPendingVoterRegistrationService()
                    .findForConfiguration( configuration, all ) );
            status = getOnlineDataTransferCache().createStatus( id );
            status.setPercent(1);
            status.setStatus( DataPreparationStatus.IN_PROGRESS);
            getOnlineDataTransferCache().updateStatus(status);

            int pvrSize = originalRegistrations.size();
            int count = 0;
            final List<PendingVoterRegistration> processedRegistrations = new LinkedList<PendingVoterRegistration>();
            for ( final PendingVoterRegistration registration : originalRegistrations ) {
                if (getPendingVoterRegistrationCipher().decryptAll(registration)) {
                  processedRegistrations.add( registration );
                }
                count++;
                int percent = Math.round( 99.0f * count / pvrSize );
                status = getOnlineDataTransferCache().createStatus( id );
                status.setPercent( percent );
                status.setStatus( DataPreparationStatus.IN_PROGRESS);
                getOnlineDataTransferCache().updateStatus(status);
            }

            logger.info( "Pending voter registrations: " + processedRegistrations.size() );

            final byte[] csv = pendingVoterRegistrationUtil.createCsv(processedRegistrations);
            getOnlineDataTransferCache().createCsvData( csv, id );
            status = getOnlineDataTransferCache().createStatus( id );
            status.setPercent(100);
            status.setStatus(DataPreparationStatus.COMPLETED);
            getOnlineDataTransferCache().updateStatus(status);
            logger.info("The CSV is " + csv.length + " bytes in length");
        } catch (Exception e) {
            logger.error( "Could not create the CSV", e);
            status = getOnlineDataTransferCache().createStatus( id );
            status.setStatus( DataPreparationStatus.ERROR );
            status.setMessage( e.getMessage() );
            getOnlineDataTransferCache().updateStatus(status);
        }

    }

    @Async
    public void createDataTransferCsvFromWizard( final PendingVoterRegistrationConfiguration configuration,
                                                 final String id,
                                                 Date startDate,
                                                 Date endDate) {
        DataPreparationStatus status = getOnlineDataTransferCache().createStatus( id );
        status.setStatus(DataPreparationStatus.IN_PROGRESS);
        try {
            List<WizardResults> wizardResultsList = questionnaireService.findForPendingConfiguration( configuration,startDate, endDate, false );

            status = getOnlineDataTransferCache().createStatus( id );
            status.setPercent(1);
            status.setStatus( DataPreparationStatus.IN_PROGRESS);

            int pvrSize = wizardResultsList.size();
            int count = 0;
            logger.info( "Pending voter registrations: " + wizardResultsList.size() );

            final byte[] csv = pendingVoterRegistrationUtil.createCsv(wizardResultsList, configuration.getExportAnswersLevel() );
            getOnlineDataTransferCache().createCsvData( csv, id );
            status = getOnlineDataTransferCache().createStatus( id );
            status.setPercent(100);
            status.setStatus(DataPreparationStatus.COMPLETED);
            logger.info("The CSV is " + csv.length + " bytes in length");
        } catch (Exception e) {
            logger.error( "Could not create the CSV", e);
            status = getOnlineDataTransferCache().createStatus( id );
            status.setStatus( DataPreparationStatus.ERROR );
            status.setMessage( e.getMessage() );
        }

    }

    public PendingVoterRegistrationService getPendingVoterRegistrationService() {
        return pendingVoterRegistrationService;
    }

    public StateService getStateService() {
        return stateService;
    }

    public PendingVoterRegistrationCipher getPendingVoterRegistrationCipher() {
        return pendingVoterRegistrationCipher;
    }

    public OnlineDataTransferCache getOnlineDataTransferCache() {
        return onlineDataTransferCache;
    }
}
