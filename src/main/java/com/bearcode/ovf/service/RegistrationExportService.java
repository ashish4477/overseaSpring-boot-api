package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.PdfAnswersDAO;
import com.bearcode.ovf.DAO.RegistrationExportDAO;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.questionnaire.WizardResults;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.model.registrationexport.DataExportHistory;
import com.bearcode.ovf.model.registrationexport.DeliverySchedule;
import com.bearcode.ovf.model.registrationexport.ExportHistoryStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by leonid on 26.09.16.
 */
@Service
public class RegistrationExportService {

    @Autowired
    private RegistrationExportDAO registrationExportDAO;

    @Autowired
    private PdfAnswersDAO pdfAnswersDAO;

    public List<DataExportHistory> findHistoriesForExport( DataExportConfiguration configuration ) {
        return registrationExportDAO.findHistoriesForExport( configuration );
    }

    public List<WizardResults> findResultsForHistories( List<DataExportHistory> histories ) {
        if ( histories == null || histories.size() == 0 ) {
            return Collections.emptyList();
        }
        List<Long> resultIds = new ArrayList<Long>();
        for ( DataExportHistory history : histories ) {
            resultIds.add( history.getWizardResults().getId() );
        }
        return pdfAnswersDAO.findWizardResults( resultIds );
    }

    public List<DataExportConfiguration> findConfigurations(  DeliverySchedule deliverySechdule ) {
        return registrationExportDAO.findConfigurations( deliverySechdule );
    }

    public void makeComplete( List<DataExportHistory> histories ) {
        for ( DataExportHistory history : histories ) {
            history.setExportDate( new Date() );
            history.setStatus( ExportHistoryStatus.EXPORTED );
        }
        registrationExportDAO.makeAllPersistent( histories );
    }

    public void saveExportHistory( DataExportHistory history ) {
        registrationExportDAO.makePersistent( history );
    }

    public List<DataExportConfiguration> findConfigurationsForFace( FaceConfig currentFace ) {
        return registrationExportDAO.findConfigurationsForFace( currentFace );
    }

    public DataExportConfiguration findConfiguration( Long id ) {
        return registrationExportDAO.findConfiguration( id );
    }

    public void saveConfiguration( DataExportConfiguration configuration ) {
        registrationExportDAO.makePersistent( configuration );
    }

    public void makeHistoriesForNewConfig( DataExportConfiguration configuration ) {
        List<WizardResults> resultsList = registrationExportDAO.findNewResultsForConfiguration( configuration );
        for ( WizardResults results : resultsList ) {
            DataExportHistory history = new DataExportHistory();
            history.setStatus( ExportHistoryStatus.NEW );
            history.setExportConfig( configuration );
            history.setWizardResults( results );
            history.setCreationDate( results.getCreationDate() );
            registrationExportDAO.makePersistent( history );

        }
    }

    public List<DataExportConfiguration> findAllConfigurations() {
        return registrationExportDAO.findAllConfigurations();
    }

    public long countHistories( DataExportConfiguration configuration ) {
        return registrationExportDAO.countHistories( configuration, null );
    }

    public long countHistories( DataExportConfiguration configuration, ExportHistoryStatus status ) {
        return registrationExportDAO.countHistories( configuration, status );
    }

    public DataExportHistory findFirstHistory( DataExportConfiguration configuration ) {
        return registrationExportDAO.findOneHistory( configuration, null, RegistrationExportDAO.LOOK_FOR_FIRST );
    }

    public DataExportHistory findFirstHistory( DataExportConfiguration configuration, ExportHistoryStatus status ) {
        return registrationExportDAO.findOneHistory( configuration, status, RegistrationExportDAO.LOOK_FOR_FIRST );
    }

    public DataExportHistory findLastHistory( DataExportConfiguration configuration ) {
        return registrationExportDAO.findOneHistory( configuration, null, RegistrationExportDAO.LOOK_FOR_LAST );
    }

    public DataExportHistory findLastHistory( DataExportConfiguration configuration, ExportHistoryStatus status ) {
        return registrationExportDAO.findOneHistory( configuration, status, RegistrationExportDAO.LOOK_FOR_LAST );
    }

    public void updateHistoriesForExport( DataExportConfiguration configuration, Date exportDate ) {
        List<DataExportHistory> histories = registrationExportDAO.findHistoriesForChangeExport( configuration, exportDate );
        for ( DataExportHistory history : histories ) {
            history.setStatus( ExportHistoryStatus.PREPARED );
        }
        registrationExportDAO.makeAllPersistent( histories );
    }
}
