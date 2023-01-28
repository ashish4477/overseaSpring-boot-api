package com.bearcode.ovf.tools.registrationexport;

import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DataExportHistory;
import com.bearcode.ovf.service.QuestionnaireService;
import com.bearcode.ovf.service.RegistrationExportService;
import com.bearcode.ovf.tools.pendingregistration.DataPreparationStatus;
import com.bearcode.ovf.tools.pendingregistration.OnlineDataTransferCache;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by leonid on 28.09.16.
 */
@Component
public class AsyncDataExportPreparator {
    private static Logger logger = LoggerFactory.getLogger( AsyncDataExportPreparator.class );

    @Autowired
    private OnlineDataTransferCache onlineDataTransferCache;

    @Autowired
    private RegistrationExportService registrationExportService;

    @Autowired
    private RegistrationExportUtils registrationExportUtils;


    @Async
    public void createDataTransferCsvFromWizard( final DataExportConfiguration configuration,
                                                 final List<DataExportHistory> histories,
                                                 final String id) {
        DataPreparationStatus status = getOnlineDataTransferCache().createStatus( id );
        status.setStatus(DataPreparationStatus.IN_PROGRESS);
        getOnlineDataTransferCache().updateStatus( status );
        try {

            List<WizardResults> wizardResultsList = registrationExportService.findResultsForHistories( histories );

            status = getOnlineDataTransferCache().createStatus( id );
            status.setPercent(1);
            status.setStatus( DataPreparationStatus.IN_PROGRESS );
            getOnlineDataTransferCache().updateStatus( status );

            int pvrSize = wizardResultsList.size();
            int count = 0;
            logger.info( "Pending voter registrations: " + wizardResultsList.size() );

            final byte[] csv = registrationExportUtils.createCsv(wizardResultsList, configuration.getExportAnswersLevel() );
            getOnlineDataTransferCache().createCsvData( csv, id );
            status = getOnlineDataTransferCache().createStatus( id );
            status.setPercent(100);
            status.setStatus( DataPreparationStatus.COMPLETED );
            getOnlineDataTransferCache().updateStatus( status );
            logger.info("The CSV is " + csv.length + " bytes in length");
        } catch (Exception e) {
            logger.error( "Could not create the CSV", e);
            status = getOnlineDataTransferCache().createStatus( id );
            status.setStatus( DataPreparationStatus.ERROR );
            status.setMessage( e.getMessage() );
            getOnlineDataTransferCache().updateStatus( status );
        }

    }

    public OnlineDataTransferCache getOnlineDataTransferCache() {
        return onlineDataTransferCache;
    }

    public void setOnlineDataTransferCache( OnlineDataTransferCache onlineDataTransferCache ) {
        this.onlineDataTransferCache = onlineDataTransferCache;
    }
}


