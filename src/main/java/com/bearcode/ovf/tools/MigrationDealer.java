package com.bearcode.ovf.tools;

import com.bearcode.ovf.actions.questionnaire.admin.forms.MigrationContext;
import com.bearcode.ovf.model.common.FaceConfig;
import com.bearcode.ovf.model.mail.MailingList;
import com.bearcode.ovf.model.migration.*;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.reportingdashboard.Report;
import com.bearcode.ovf.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.*;

/**
 * Date: 20.12.11
 * Time: 16:47
 *
 * @author Leonid Ginzburg
 */
@Component
public class MigrationDealer {
    private static final String PROGRAM_VERSION = "2.0";

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionFieldService questionFieldService;

    @Autowired
    private RelatedService relatedService;

    @Autowired
    private FacesService facesService;

    @Autowired
    private MailingListService mailingListService;

    @Autowired
    private MigrationService migrationService;

    @Autowired
    private ReportingDashboardService reportingDashboardService;

    private Collection<QuestionnairePage> pagesToDelete;
    private Collection<QuestionnairePage> pagesToPersist;
    private Collection<Question> groupsToDelete;
    private Collection<QuestionVariant> variantsToDelete;
    private Collection<QuestionField> fieldsToDelete;
    private Collection<BasicDependency> dependenciesToDelete;
    private Collection<FieldDictionaryItem> itemsToDelete;

    private Collection<PdfFilling> fillingsToDelete;
    private Collection<PdfFilling> fillingsToPersist;

    private Collection<FieldDependency> fieldDependenciesToDelete;
    private Collection<FieldDependency> fieldDependenciesToPersist;

    private Collection<MailingList> mailingListsToDelete;
    private Collection<MailingList> mailingListsToPersist;


    private Map<Long, FieldType> fieldTypeCache;
    private Map<Long, Question> questionsCache;
    private Map<Long, FieldDictionaryItem> dictionaryCache;
    //private Map<Long, QuestionField> fieldsCache;

    private Collection<String> messages;

    private Logger logger = LoggerFactory.getLogger( MigrationDealer.class );

    public MigrationDealer() {
        pagesToPersist = new LinkedHashSet<QuestionnairePage>();
        groupsToDelete = new LinkedHashSet<Question>();
        variantsToDelete = new LinkedHashSet<QuestionVariant>();
        fieldsToDelete = new LinkedHashSet<QuestionField>();
        dependenciesToDelete = new LinkedHashSet<BasicDependency>();
        itemsToDelete = new LinkedHashSet<FieldDictionaryItem>();

        fillingsToPersist = new LinkedHashSet<PdfFilling>();

        fieldDependenciesToPersist = new LinkedHashSet<FieldDependency>();
        fieldDependenciesToDelete = new LinkedHashSet<FieldDependency>();

        mailingListsToDelete = new LinkedList<MailingList>();
        mailingListsToPersist = new LinkedList<MailingList>();

        fieldTypeCache = new HashMap<Long, FieldType>();
        questionsCache = new HashMap<Long, Question>();
        dictionaryCache = new HashMap<Long, FieldDictionaryItem>();
        //fieldsCache = new HashMap<Long, QuestionField>();
    }

    private void initCaches( MigrationContext context ) {
        if ( context.getPageType() == null ) {
            pagesToDelete = questionnaireService.findQuestionnairePages();
            fillingsToDelete = relatedService.findPdfFillings();
        } else {
            pagesToDelete = questionnaireService.findQuestionnairePages( context.getPageType() );
            fillingsToDelete = new LinkedList<PdfFilling>();
            for ( QuestionnairePage page : pagesToDelete ) {
                for ( Question group : page.getQuestions() ) {
                    Collection<QuestionDependency> dependencies = questionnaireService.findDependents( group );
                    for ( QuestionDependency dependency : dependencies ) {
                        if ( dependency.getDependent() instanceof PdfFilling ) {
                            fillingsToDelete.add( (PdfFilling) dependency.getDependent() );
                        }
                    }
                }
            }
        }
        mailingListsToDelete = mailingListService.findAllMailingLists();

        messages = context.getMessages();
    }

    private void clearCaches() {
        pagesToDelete.clear();
        pagesToPersist.clear();
        groupsToDelete.clear();
        variantsToDelete.clear();
        fieldsToDelete.clear();
        dependenciesToDelete.clear();
        itemsToDelete.clear();

        questionsCache.clear();
        dictionaryCache.clear();

        fillingsToDelete.clear();
        fillingsToPersist.clear();

        fieldDependenciesToDelete.clear();
        fieldDependenciesToPersist.clear();
        //messages = null;
        mailingListsToDelete.clear();
        mailingListsToPersist.clear();

        fieldTypeCache.clear();
    }

    public void doExport( MigrationContext context ) {
        context.setProgramVersion( PROGRAM_VERSION );

        Collection<FieldType> fieldTypes = questionFieldService.findFieldTypes();
        for ( FieldType fieldType : fieldTypes ) {
            context.getFieldTypes().add( new FieldTypeFacade( fieldType ) );
        }

        Collection<QuestionnairePage> pages;
        if ( context.getPageType() == null ) {
            pages = questionnaireService.findQuestionnairePages();
        } else {
            pages = questionnaireService.findQuestionnairePages( context.getPageType() );
        }
        for ( QuestionnairePage page : pages ) {
            context.getPages().add( new PageFacade( page ) );
        }

        if ( context.getPageType() == null ) {  //means "migrate ALL"
            Collection<PdfFilling> fillings = relatedService.findPdfFillings();
            for ( PdfFilling filling : fillings ) {
                context.getFillings().add( new FillingFacade( filling ) );
            }
        } else {
            for ( QuestionnairePage page : pages ) {
                for ( Question group : page.getQuestions() ) {
                    Collection<QuestionDependency> dependencies = questionnaireService.findDependents( group );
                    for ( QuestionDependency dependency : dependencies ) {
                        if ( dependency.getDependent() instanceof PdfFilling ) {
                            context.getFillings().add( new FillingFacade( (PdfFilling) dependency.getDependent() ) );
                        }
                    }
                }
            }
        }
        Collection<MailingList> mailingLists = mailingListService.findAllMailingLists();
        for ( MailingList mailingList : mailingLists ) {
            context.getMailingLists().add( new MailingListFacade( mailingList ) );
        }

        assignMigrationIds( context );
    }

    private void assignMigrationIds( MigrationContext context ) {
        context.setVersion( migrationService.findVersion() );

        Map<String, Long> outputMap = migrationService.buildOutputMap();
        List<MigrationId> createdIds = new LinkedList<MigrationId>();
        long migrationId = migrationService.findMigrationId();

        Map<Long,Long> questionsCache = new HashMap<Long, Long>();   // objectId -> migrationId
        Map<Long,Long> dictionaryCache =  new HashMap<Long, Long>(); // objectId -> migrationId

        for ( FieldTypeFacade fieldTypeFacade : context.getFieldTypes() ) {
            migrationId = fieldTypeFacade.assignMigrationId( outputMap, createdIds, migrationId, context.getVersion() );
        }
        for ( PageFacade pageFacade : context.getPages() ) {
            migrationId = pageFacade.assignMigrationId( outputMap, createdIds, migrationId, context.getVersion() );
            // assign migration ids in references, ooh!
            for ( GroupFacade groupFacade : pageFacade.getQuestionGroups() ) {
                questionsCache.put( groupFacade.getId(), groupFacade.getMigrationId() );
                for ( VariantFacade variantFacade : groupFacade.getVariants() ) {
                    for ( FieldFacade fieldFacade : variantFacade.getFields() ) {
                        FieldTypeFacade fieldTypeFacade = findObjectById( context.getFieldTypes(), fieldFacade.getTypeId(), FieldTypeFacade.class );
                        if ( fieldTypeFacade != null ) {
                            fieldFacade.setTypeMigrationId( fieldTypeFacade.getMigrationId() );
                        }
                        for ( FieldDictionaryItemFacade itemFacade : fieldFacade.getItems() ) {
                            dictionaryCache.put( itemFacade.getId(), itemFacade.getMigrationId() );
                        }
                        for ( FieldDependencyFacade fieldDependencyFacade : fieldFacade.getDependencies() ) {
                            fieldDependencyFacade.setDependsOnMigrationId( questionsCache.get( fieldDependencyFacade.getDependsOnId() ) );
                        }
                    }
                    for ( DependencyFacade dependencyFacade : variantFacade.getDependencies() ) {
                        if ( dependencyFacade.getClassType() == DependencyClassType.QUESTION_DEPENDENCY ) {
                            dependencyFacade.setDependsOnMigrationId( questionsCache.get( dependencyFacade.getDependsOnId() ) );
                            dependencyFacade.setConditionMigrationId( dictionaryCache.get( dependencyFacade.getConditionId() ) );
                        }
                    }
                }
            }
        }
        for ( FillingFacade fillingFacade : context.getFillings() ) {
            migrationId = fillingFacade.assignMigrationId( outputMap, createdIds, migrationId, context.getVersion() );
            // assign migration ids in references
            for ( DependencyFacade dependencyFacade : fillingFacade.getDependencies() ) {
                if ( dependencyFacade.getClassType() == DependencyClassType.QUESTION_DEPENDENCY ) {
                    dependencyFacade.setDependsOnMigrationId( questionsCache.get( dependencyFacade.getDependsOnId() ) );
                    dependencyFacade.setConditionMigrationId( dictionaryCache.get( dependencyFacade.getConditionId() ) );
                }
            }
        }
        for ( MailingListFacade mailingListFacade : context.getMailingLists() ) {
            migrationId = mailingListFacade.assignMigrationId( outputMap, createdIds, migrationId, context.getVersion() );
            // assign migration ids in references
            FieldTypeFacade fieldTypeFacade = findObjectById( context.getFieldTypes(), mailingListFacade.getFieldTypeId(), FieldTypeFacade.class );
            if ( fieldTypeFacade != null ) {
                mailingListFacade.setFieldTypeMigrationId( fieldTypeFacade.getMigrationId() );
            }
        }

        // finally save migration Ids
        migrationService.saveAll( createdIds );

    }

    public synchronized void doImport( MigrationContext context ) {

        initCaches( context );
        if ( !PROGRAM_VERSION.equalsIgnoreCase( context.getProgramVersion() ) ) {
            messages.add( "Incompatible format version. Migration has been stopped." );
            return;
        }

        Map<Object,MigrationId> createdIds = new HashMap<Object,MigrationId>();
        migrationService.deleteNewestIds( context.getVersion() );
        Map<String,Long> inputMap = migrationService.buildInputMap( context.getVersion() );  // existed migrationIds

        importFieldTypes( context.getFieldTypes(), inputMap, createdIds );
        importPages( context.getPages(), inputMap, createdIds  );
        importPdfFillings( context.getFillings(), inputMap, createdIds );  // fillings are always imported
        checkDependencies();   // check for possible orphaned PdfFillings and do not delete such Question
        importMailingLists( context.getMailingLists(), inputMap, createdIds );

        try {
            migrationService.saveAllMigrationStuff( this );

            // save mailing lists
            mailingListService.saveMigrations( mailingListsToDelete, mailingListsToPersist );
            // save migration ids
            // objects have been saved and there are real Ids
            createdIds.remove( this );  // remove a trick
            for ( Object object : createdIds.keySet() ) {
                MigrationId migrationId = createdIds.get( object );
                try {
                    Method idMethod = object.getClass().getMethod( "getId" );
                    Long objId = (Long) idMethod.invoke( object );
                    migrationId.setObjectId( objId );
                    migrationId.setVersion( context.getVersion() );
                } catch ( Exception e ) {
                    logger.error( "Cant get Id for simple object", e );
                }
            }
            migrationService.saveAll( createdIds.values() );
        } finally {
            clearCaches();
        }

    }

    private void importFieldTypes( Collection<FieldTypeFacade> fieldTypes, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        List<FieldType> existedFieldTypes = questionFieldService.findFieldTypes();
        for ( FieldTypeFacade typeFacade : fieldTypes ) {
            FieldType fieldType = findObjectAndCreateMigrationId( existedFieldTypes, typeFacade, FieldType.class, inputMap, createdIds );
            if ( fieldType != null )  {
                typeFacade.exportTo( fieldType );
            }
            else {
                fieldType = typeFacade.createFieldType();
                MigrationId migrationId = createdIds.get( this );
                createdIds.put( fieldType, migrationId );
            }
            fieldTypeCache.put( typeFacade.getMigrationId(), fieldType );
        }
    }

    private void importPdfFillings( Collection<FillingFacade> fillings, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        for ( FillingFacade fillingFacade : fillings ) {
            PdfFilling filling = findObjectAndCreateMigrationId( fillingsToDelete, fillingFacade, PdfFilling.class, inputMap, createdIds );
            if ( filling != null ) {
                fillingsToDelete.remove( filling );
                fillingFacade.exportTo( filling );
            } else {
                filling = fillingFacade.createFilling();
                MigrationId migrationId = createdIds.get( this );
                createdIds.put( filling, migrationId );
            }
            fillingsToPersist.add( filling );
            importDependencies( filling, fillingFacade, inputMap, createdIds );
        }
    }

    private void checkDependencies() {
        Collection<Question> unselected = new LinkedList<Question>();
        for ( Question group : groupsToDelete ) {
            Collection<QuestionDependency> dependencies = questionnaireService.findDependents( group );
            for ( QuestionDependency dependency : dependencies ) {
                if ( dependency.getDependent() instanceof PdfFilling &&
                        !fillingsToDelete.contains( dependency.getDependent() )) {
                    unselected.add( group );
                    break;
                }
            }
        }
        for ( Question group : unselected ) {
            groupsToDelete.remove( group );
            logger.error( String.format( "Migration : Question \"%s\" ( page \"%s\") couldn't be deleted", group.getName(), group.getPage().getTitle() ) );
            messages.add( String.format( "Question \"%s\" ( page \"%s\") couldn't be deleted because of unresolved dependency", group.getName(), group.getPage().getTitle() ) );
        }
    }

    private void importPages( Collection<PageFacade> pageFacades, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        for ( PageFacade pageFacade : pageFacades ) {
            QuestionnairePage page = findObjectAndCreateMigrationId( pagesToDelete, pageFacade, QuestionnairePage.class, inputMap, createdIds );
            if ( page != null && pageFacade.matchClass( page ) ) {
                pagesToDelete.remove( page );
                pageFacade.exportTo( page );
            } else {
                MigrationId migrationId = createdIds.get( this );
                if ( page != null ) { // class does not match
                    migrationId = new MigrationId();
                    migrationId.setMigrationId( pageFacade.getMigrationId() );
                    migrationId.setClassName( pageFacade.getBaseClassName() );
                }
                page = pageFacade.createPage();
                createdIds.put( page, migrationId );
            }
            pagesToPersist.add( page );
            importGroups( page, pageFacade, inputMap, createdIds );
        }
    }

    private void importGroups( QuestionnairePage page, PageFacade pageFacade, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        groupsToDelete.addAll( page.getQuestions() );
        page.getQuestions().clear();

        for ( GroupFacade groupFacade : pageFacade.getQuestionGroups() ) {
            Question group = findObjectAndCreateMigrationId( groupsToDelete, groupFacade, Question.class, inputMap, createdIds );
            if ( group != null ) {
                groupsToDelete.remove( group );
                groupFacade.exportTo( group );
            } else {
                group = groupFacade.createQuestion();
                MigrationId migrationId = createdIds.get( this );
                createdIds.put( group, migrationId );
            }
            group.setPage( page );
            page.getQuestions().add( group );
            questionsCache.put( groupFacade.getMigrationId(), group );
            importVariants( group, groupFacade, inputMap, createdIds );
        }

    }

    private void importVariants( Question group, GroupFacade groupFacade, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        variantsToDelete.addAll( group.getVariants() );
        group.getVariants().clear();

        for ( VariantFacade variantFacade : groupFacade.getVariants() ) {
            QuestionVariant variant = findObjectAndCreateMigrationId( variantsToDelete, variantFacade, QuestionVariant.class, inputMap, createdIds );
            if ( variant != null ) {
                variantsToDelete.remove( variant );
                variantFacade.exportTo( variant );
            } else {
                variant = variantFacade.createVariant();
                MigrationId migrationId = createdIds.get( this );
                createdIds.put( variant, migrationId );
            }
            variant.setQuestion( group );
            group.getVariants().add( variant );

            importFields( variant, variantFacade, inputMap, createdIds );
            importDependencies( variant, variantFacade, inputMap, createdIds );
        }
    }

    private void importDependencies( Related related, RelatedFacade variantFacade, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        dependenciesToDelete.addAll( related.getKeys() );
        related.getKeys().clear();

        for ( DependencyFacade dependencyFacade : variantFacade.getDependencies() ) {
            FaceConfig face = null;
            Question question = null;
            FieldDictionaryItem item = null;
            BasicDependency key = findObjectAndCreateMigrationId( dependenciesToDelete, dependencyFacade, BasicDependency.class, inputMap, createdIds );
            switch ( dependencyFacade.getClassType() ) {
                case FACE_DEPENDENCY:
                    face = facesService.findConfigById( dependencyFacade.getFaceId() );
                    if ( face == null ) {
                        logger.error( String.format( "Migration : Face Config was not found for Face Dependency of \"%s\" variant", variantFacade.getTitle() ) );
                        messages.add( String.format( "Face Config was not found for Face Dependency of \"%s\" variant", variantFacade.getTitle() ) );
                        continue;
                    }
                    break;
                case QUESTION_DEPENDENCY:
                    question = questionsCache.get( dependencyFacade.getDependsOnMigrationId() );
                    item = dictionaryCache.get( dependencyFacade.getConditionMigrationId() );
                    if ( item == null ) {
                        item = questionFieldService.findDictionaryItemById( dependencyFacade.getConditionId() );
                        if ( item == null ) {
                            logger.error( String.format( "Migration : Dictionary Item was not found for Question Dependency of \"%s\" variant", variantFacade.getTitle() ) );
                            messages.add( String.format( "Dictionary Item was not found for Question Dependency of \"%s\" variant", variantFacade.getTitle() ) );
                            continue;
                        }
                    }
                    break;
            }
            if ( key != null && dependencyFacade.matchClass( key ) ) {
                dependenciesToDelete.remove( key );
                dependencyFacade.exportTo( key );
            } else {
                MigrationId migrationId = createdIds.get( this );
                if ( key != null ) { // class does not match
                    migrationId = new MigrationId();
                    migrationId.setMigrationId( dependencyFacade.getMigrationId() );
                    migrationId.setClassName( dependencyFacade.getBaseClassName() );
                }
                key = dependencyFacade.createDependency();
                createdIds.put( key, migrationId );
            }
            switch ( dependencyFacade.getClassType() ) {
                case FACE_DEPENDENCY:
                    ((FaceDependency) key).setDependsOn( face );
                    break;
                case QUESTION_DEPENDENCY:
                    QuestionDependency questionKey = (QuestionDependency) key;
                    questionKey.setDependsOn( question );
                    questionKey.setCondition( item );
                    break;
            }
            key.setDependent( related );
            related.getKeys().add( key );
        }
    }

    private void importFields( QuestionVariant variant, VariantFacade variantFacade, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        fieldsToDelete.addAll( variant.getFields() );
        variant.getFields().clear();

        for ( FieldFacade fieldFacade : variantFacade.getFields() ) {
            QuestionField field = findObjectAndCreateMigrationId( fieldsToDelete, fieldFacade, QuestionField.class, inputMap, createdIds );

            FieldType fieldType = fieldTypeCache.get( fieldFacade.getTypeMigrationId() ); //questionFieldService.findFieldTypeById( fieldFacade.getTypeId() );
            if ( fieldType != null ) {
                if ( field != null ) {
                    fieldsToDelete.remove( field );
                    fieldFacade.exportTo( field );
                } else {
                    field = fieldFacade.createField();
                    MigrationId migrationId = createdIds.get( this );
                    createdIds.put( field, migrationId );
                }
                field.setQuestion( variant );
                variant.getFields().add( field );
                field.setType( fieldType );
                // add to cache
                importDictionaryItems( field, fieldFacade, inputMap, createdIds  );
                importFieldDependencies( field, fieldFacade.getDependencies(), inputMap, createdIds  );
                //fieldsCache.put( fieldFacade.getId(), field );

            } else {
                logger.error( String.format( "Migration: Field type was not found for \"%s\" field of \"%s\" variant", fieldFacade.getTitle(), variantFacade.getTitle() ) );
                messages.add( String.format( "Field type was not found for \"%s\" field of \"%s\" variant", fieldFacade.getTitle(), variantFacade.getTitle() ) );
            }
        }
    }

    private void importDictionaryItems( QuestionField field, FieldFacade fieldFacade, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        for ( FieldDictionaryItem item : field.getGenericOptions() ) {
            item.setForField( null );
        }
        itemsToDelete.addAll( field.getGenericOptions() );
        field.getGenericOptions().clear();

        for ( FieldDictionaryItemFacade itemFacade : fieldFacade.getItems() ) {
            FieldDictionaryItem item = findObjectAndCreateMigrationId( itemsToDelete, itemFacade, FieldDictionaryItem.class, inputMap, createdIds );
            if ( item != null && item instanceof GenericStringItem ) {
                // can't use Collection.remove(o) here because GenericStringItem declares equals() that looks for value, not id
                for ( Iterator<FieldDictionaryItem> itToDelete = itemsToDelete.iterator(); itToDelete.hasNext(); ) {
                    FieldDictionaryItem toDel = itToDelete.next();
                    if ( toDel == item ) {
                        itToDelete.remove();
                        break;
                    }
                }
                itemFacade.exportTo( (GenericStringItem) item );
            } else {
                item = itemFacade.createItem();
                MigrationId migrationId = createdIds.get( this );
                createdIds.put( item, migrationId );
            }
            item.setForField( field );
            field.getGenericOptions().add( item );
            dictionaryCache.put( itemFacade.getMigrationId(), item );
        }
    }

    private void importFieldDependencies( QuestionField field, Collection<FieldDependencyFacade> dependencyFacades, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        fieldDependenciesToDelete.addAll( field.getFieldDependencies() );

        for ( FieldDependencyFacade dependencyFacade : dependencyFacades ) {
            FieldDependency dependency = findObjectAndCreateMigrationId( fieldDependenciesToDelete, dependencyFacade, FieldDependency.class, inputMap, createdIds ) ;
            Question dependsOn = questionsCache.get( dependencyFacade.getDependsOnMigrationId() );
            if ( dependsOn != null ) {
                if ( dependency != null ) {
                    fieldDependenciesToDelete.remove( dependency );
                } else {
                    dependency = new FieldDependency();
                    MigrationId migrationId = createdIds.get( this );
                    createdIds.put( dependency, migrationId );
                }
                dependency.setDependent( field );
                dependency.setDependsOn( dependsOn );
                fieldDependenciesToPersist.add( dependency );
            }
        }
    }

    private void importMailingLists( Collection<MailingListFacade> mailingLists, Map<String, Long> inputMap, Map<Object, MigrationId> createdIds ) {
        for ( MailingListFacade mailingListFacade : mailingLists ) {
            MailingList list = findObjectAndCreateMigrationId( mailingListsToDelete, mailingListFacade, MailingList.class, inputMap, createdIds );
            FieldType fieldType = fieldTypeCache.get( mailingListFacade.getFieldTypeMigrationId() );
            // this is feature now. mailing list could have no field type assigned

            if ( list != null ) {
                mailingListsToDelete.remove( list );
            }
            else {
                MigrationId migrationId = createdIds.get( this );
                list = mailingListFacade.createMailingList();
                createdIds.put( list, migrationId );
            }
            list.setFieldType( fieldType );
            mailingListsToPersist.add( list );
        }
    }

    private <T> T findObjectAndCreateMigrationId( Collection<T> list,
                                                  AbleToMigrate facade,
                                                  Class<T> clazz,
                                                  Map<String,Long> inputMap,
                                                  Map<Object,MigrationId> createdIds ) {

        boolean notFound = false;
        Long objectId = inputMap.get( String.format( "%d_%s", facade.getMigrationId(), facade.getBaseClassName() ) );
        if ( objectId == null ) {
            objectId = facade.getId();
            notFound = true;             // migration id does not exist on the server
        }
        T object = findObjectById( list, objectId, clazz );
        if ( object != null ) {
            if ( notFound ) {
                MigrationId migrationId =  new MigrationId();
                migrationId.setClassName( facade.getBaseClassName() );
                migrationId.setMigrationId( facade.getMigrationId() );
                createdIds.put( object, migrationId );
            }
        } else {
            MigrationId migrationId =  new MigrationId();
            migrationId.setClassName( facade.getBaseClassName() );
            migrationId.setMigrationId( facade.getMigrationId() );
            createdIds.put( this, migrationId );   // this is a trick. key will be reassigned soon
        }
        return object;
    }

    private <T> T findObjectById( Collection<T> list, long id, Class<T> clazz ) {
        try {
            Method idMethod = clazz.getMethod( "getId" );
            for ( T object : list ) {
                Long objId = (Long) idMethod.invoke( object );
                if ( objId == id ) {
                    return object;
                }
            }
        } catch ( Exception e ) {
            logger.error( "Cant get Id for simple object", e );
        }
        return null;
    }

    public Collection<Question> getGroupsToDelete() {
        return groupsToDelete;
    }

    public Collection<QuestionnairePage> getPagesToDelete() {
        return pagesToDelete;
    }

    public Collection<QuestionVariant> getVariantsToDelete() {
        return variantsToDelete;
    }

    public Collection<QuestionField> getFieldsToDelete() {
        return fieldsToDelete;
    }

    public Collection<BasicDependency> getDependenciesToDelete() {
        return dependenciesToDelete;
    }

    public Collection<FieldDictionaryItem> getItemsToDelete() {
        return itemsToDelete;
    }

    public Collection<QuestionnairePage> getPagesToPersist() {
        return pagesToPersist;
    }

    public Collection<PdfFilling> getFillingsToDelete() {
        return fillingsToDelete;
    }

    public Collection<PdfFilling> getFillingsToPersist() {
        return fillingsToPersist;
    }

    public Collection<FieldDependency> getFieldDependenciesToDelete() {
        return fieldDependenciesToDelete;
    }

    public Collection<FieldDependency> getFieldDependenciesToPersist() {
        return fieldDependenciesToPersist;
    }

    public Collection<MailingList> getMailingListsToDelete() {
        return mailingListsToDelete;
    }

    public Collection<MailingList> getMailingListsToPersist() {
        return mailingListsToPersist;
    }

    public Collection<FieldType> getFieldTypes() {
        return fieldTypeCache.values();
    }

}
