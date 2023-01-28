package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.*;
import com.bearcode.ovf.model.migration.MigrationId;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.model.reportingdashboard.ReportAnswer;
import com.bearcode.ovf.model.reportingdashboard.ReportColumn;
import com.bearcode.ovf.model.reportingdashboard.ReportField;
import com.bearcode.ovf.tools.MigrationDealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Date: 15.10.12
 * Time: 15:38
 *
 * @author Leonid Ginzburg
 */
@Service
public class MigrationService {

    @Autowired
    private MigrationDAO migrationDAO;

    @Autowired
    private ReportingDashboardDAO reportingDashboardDAO;

    @Autowired
    private QuestionnairePageDAO pageDAO;

    @Autowired
    private QuestionFieldDAO questionFieldDAO;


    public void saveAll( Collection<MigrationId> migrationIds ) {
        migrationDAO.makeAllPersistent( migrationIds );
    }

    public Map<String, Long> buildOutputMap() {
        Map<String, Long> theMap = new HashMap<String, Long>();
        for ( MigrationId id : migrationDAO.findAll() ) {
            theMap.put( String.format( "%d_%s", id.getObjectId(), id.getClassName() ), id.getMigrationId() );
        }
        return theMap;
    }

    public  Map<String, Long> buildInputMap( int version ) {
        Map<String, Long> theMap = new HashMap<String, Long>();
        for ( MigrationId id : migrationDAO.findAll() ) {
            theMap.put( String.format( "%d_%s", id.getMigrationId(), id.getClassName() ), id.getObjectId() );
        }
        return theMap;
    }

    public void deleteNewestIds( int version ) {
        migrationDAO.removeNewest( version );
    }

    public int findVersion() {
        return migrationDAO.findVersion() + 1;
    }

    public long findMigrationId() {
        return migrationDAO.findMaxMigrationId() + 1;
    }

    public boolean checkConflicts() {
        return !migrationDAO.findMigrationConflicts().isEmpty() || !migrationDAO.findObjectConflicts().isEmpty();
    }

    public void deleteConflicts() {
        List<MigrationId> conflicts;
        //1)
        conflicts = migrationDAO.findMigrationConflicts();
        if ( !conflicts.isEmpty() ) {
            migrationDAO.clearMigrationConflicts( conflicts );
        }
        //2)
        conflicts = migrationDAO.findObjectConflicts();
        if ( !conflicts.isEmpty() ) {
            migrationDAO.clearObjectConflicts( conflicts );
        }
    }

    /**
     * Save all migration work. It needs to be done under single transaction.
     * @param dealer
     */
    public void saveAllMigrationStuff( MigrationDealer dealer ) {
        //check dependencies in reporting dashboard
/*
        if ( !dealer.getItemsToDelete().isEmpty() ) {
            syncAnswersWithMigration( dealer.getItemsToDelete() );
        }
        Collection<Report> reports = new HashSet<Report>();
        if ( !dealer.getFieldsToDelete().isEmpty() ) {
            reports.addAll( syncReportFieldsWithMigration( dealer.getFieldsToDelete() ) );
        }
        if ( !dealer.getVariantsToDelete().isEmpty() ) {
            checkAndRemoveReports( dealer.getVariantsToDelete(), reports );
        }
        if ( !dealer.getGroupsToDelete().isEmpty() ) {
            checkAndRemoveReportsForGroup( dealer.getGroupsToDelete(), reports );
        }
        if ( !dealer.getPagesToDelete().isEmpty() ) {
            checkAndRemoveReportsForPage( dealer.getPagesToDelete(), reports );
        }
        if ( !reports.isEmpty() ) {
            for ( Report report : reports ) {
                Report actualReport = reportingDashboardDAO.findReportById( report.getId() );
                int indx = 0;
                for ( ReportColumn column : actualReport.getColumns() ) {
                    if ( column != null ) {
                        column.setColumnNumber( indx++ );
                    }
                }
                reportingDashboardDAO.makePersistent( actualReport );
            }
        }
*/

        //questionnaireService.saveMigration( this );
        saveMigrationToPersist( dealer );
        deleteMigrationToTransient( dealer );

    }

    /**
     *  Delete answers which depend on removing dictionary items
     * @param items Removing dictionary items
     */
    private void syncAnswersWithMigration( Collection<FieldDictionaryItem> items ) {
        if ( items != null && !items.isEmpty() ) {
            Collection<ReportAnswer> answers = reportingDashboardDAO.findAnswersWithItems( items );
            if ( answers != null && !answers.isEmpty() ) {
                reportingDashboardDAO.makeAllTransient( answers );
            }
        }
    }

    /**
     * Delete report fields which depend on removing question fields
     * @param fields removing question fields
     */
    private Collection<Report> syncReportFieldsWithMigration( Collection<QuestionField> fields ) {
        if ( fields == null || fields.isEmpty() ) {
            return Collections.emptyList();
        }
        Collection<ReportField> reportFields = reportingDashboardDAO.findFieldsWithQuestions( fields );
        Collection<Report> reports = new HashSet<Report>();
        if ( reportFields != null && !reportFields.isEmpty() ) {
            Collection<ReportColumn> columns = new ArrayList<ReportColumn>();
            for ( ReportField actualField : reportFields ) {
                columns.add( actualField.getColumn() );
                reports.add( actualField.getColumn().getReport() );
            }
            Collection<ReportAnswer> answers = new ArrayList<ReportAnswer>();
            for ( ReportField reportField : reportFields ) {
                answers.addAll( reportField.getAnswers() );
            }
            reportingDashboardDAO.makeAllTransient( answers );
            reportingDashboardDAO.makeAllTransient( reportFields );
            reportingDashboardDAO.makeAllTransient( columns );
        }
        return reports;
    }

    private void checkAndRemoveReportsForPage( Collection<QuestionnairePage> pagesToDelete, Collection<Report> reports  ) {
        for ( QuestionnairePage page : pagesToDelete ) {
            checkAndRemoveReportsForGroup( page.getQuestions(), reports );
        }
    }

    private void checkAndRemoveReportsForGroup( Collection<Question> groupsToDelete, Collection<Report> reports  ) {
        for ( Question group : groupsToDelete ) {
            checkAndRemoveReports( group.getVariants(), reports );
        }
    }

    private void checkAndRemoveReports( Collection<QuestionVariant> variantsToDelete, Collection<Report> reports ) {
        for ( QuestionVariant variant : variantsToDelete ) {
            reports.addAll( syncReportFieldsWithMigration( variant.getFields() ) );
        }
    }

    // divide save migration on two parts : save-update  and remove
    private void saveMigrationToPersist( MigrationDealer dealer ) {
        questionFieldDAO.makeAllPersistent( dealer.getFieldTypes() );

        pageDAO.makeAllPersistent( dealer.getPagesToPersist() );
        for ( QuestionnairePage page : dealer.getPagesToPersist() ) {
            pageDAO.makeAllPersistent( page.getQuestions() );
            for ( Question question : page.getQuestions() ) {
                pageDAO.makeAllPersistent( question.getVariants() );
                for ( QuestionVariant variant : question.getVariants() ) {
                    pageDAO.makeAllPersistent( variant.getFields() );
                    for ( QuestionField field : variant.getFields() ) {
                        pageDAO.makeAllPersistent( field.getGenericOptions() );
                    }
                    pageDAO.makeAllPersistent( variant.getKeys() );
                }
            }
        }
        pageDAO.makeAllPersistent( dealer.getFillingsToPersist() );
        for ( PdfFilling filling : dealer.getFillingsToPersist() ) {
            pageDAO.makeAllPersistent( filling.getKeys() );
        }

        pageDAO.makeAllPersistent( dealer.getFieldDependenciesToPersist() );
    }

    private void deleteMigrationToTransient( MigrationDealer dealer ) {

        Collection<Long> itemsId = new HashSet<Long>();
        for ( FieldDictionaryItem item : dealer.getItemsToDelete() ) {
            itemsId.add( item.getId() );
        }
        if ( !itemsId.isEmpty() ) {
            Collection<FieldDictionaryItem> realItems = questionFieldDAO.findDictionaryItems( itemsId );
            questionFieldDAO.makeAllTransient( realItems );
        }

        clearFields( dealer.getFieldsToDelete() );
        pageDAO.makeAllTransient( dealer.getDependenciesToDelete() );
        clearVariants( dealer.getVariantsToDelete() );
        clearQuestionGroups( dealer.getGroupsToDelete() );
        clearPages( dealer.getPagesToDelete() );

        pageDAO.makeAllTransient( dealer.getFillingsToDelete() );

        pageDAO.makeAllTransient( dealer.getFieldDependenciesToDelete() );
    }

    private void clearFields( Collection<QuestionField> fields ) {
        for ( QuestionField field : fields ) {
            pageDAO.makeAllTransient( field.getGenericOptions() );
        }
        pageDAO.makeAllTransient( fields );
    }

    private void clearVariants( Collection<QuestionVariant> variants ) {
        for ( QuestionVariant variant : variants ) {
            clearFields( variant.getFields() );
            pageDAO.makeAllTransient( variant.getKeys() );
        }
        pageDAO.makeAllTransient( variants );
    }

    private void clearQuestionGroups( Collection<Question> questions ) {
        for ( Question question : questions ) {
            clearVariants( question.getVariants() );
        }
        pageDAO.makeAllTransient( questions );
    }

    private void clearPages( Collection<QuestionnairePage> pages ) {
        for ( QuestionnairePage page : pages ) {
            clearQuestionGroups( page.getQuestions() );
        }
        pageDAO.makeAllTransient( pages );
    }

}
