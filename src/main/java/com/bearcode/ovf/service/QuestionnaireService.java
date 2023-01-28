package com.bearcode.ovf.service;

import com.bearcode.ovf.DAO.*;
import com.bearcode.ovf.model.common.OverseasUser;
import com.bearcode.ovf.model.common.WizardResultAddress;
import com.bearcode.ovf.model.questionnaire.*;
import com.bearcode.ovf.model.registrationexport.DataExportConfiguration;
import com.bearcode.ovf.tools.MigrationDealer;
import com.bearcode.ovf.tools.pendingregistration.PendingVoterRegistrationConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Alexey Polyakov
 *         Date: Aug 14, 2007
 *         Time: 1:38:30 PM
 * @author Leonid Ginzburg
 * @author Ian Brown
 */
@Service
public class QuestionnaireService {

    @Autowired
    private QuestionnairePageDAO pageDAO;

    @Autowired
    private QuestionDAO questionDAO;

    @Autowired
    private AnswerDAO answerDAO;

    @Autowired
    private PdfAnswersDAO pdfAnswersDAO;
   
    @Autowired
    private QuestionFieldDAO questionFieldDAO;

    /* Page */
    public List<QuestionnairePage> findQuestionnairePages() {
        return pageDAO.findPages();
    }

    public List<QuestionnairePage> findQuestionnairePages( PageType type ) {
        return pageDAO.findPages( type );
    }

    public QuestionnairePage findPageById( long id ) {
        return pageDAO.findById( id );
    }
    
    public void savePage( QuestionnairePage questionnairePage ) {
        boolean newPage = questionnairePage.getId() == 0;
        boolean down = false;
        PageType type = questionnairePage.getType();

        if ( newPage ) {
            questionnairePage.setOldNumber( questionnairePage.getNumber() );
        } else {
            down = questionnairePage.getOldNumber() < questionnairePage.getNumber();
            if ( down ) {
                questionnairePage.setNumber( questionnairePage.getNumber() - 1 );
            }
        }

        int startNumber = Math.min( questionnairePage.getOldNumber(), questionnairePage.getNumber() );
        List<QuestionnairePage> lastPages = pageDAO.findPagesAfterPage( startNumber, type );

        for ( QuestionnairePage shifted : lastPages ) {
            if ( shifted == questionnairePage ) { // Hibernate returns the same object!
                if ( down ) startNumber++;
                continue;
            }
            shifted.setNumber( down ? startNumber++ : ++startNumber );
        }

        if ( newPage ) {
            lastPages.add( questionnairePage );
        }
        pageDAO.makeAllPersistent( lastPages );
    }

    public void deletePage( QuestionnairePage page ) {
        int startNumber = page.getNumber();
        Collection<QuestionnairePage> lastPages = pageDAO.findPagesAfterPage( startNumber + 1, page.getType() );
        for ( QuestionnairePage shifted : lastPages ) {
            shifted.setNumber( startNumber );
            startNumber++;
        }
        pageDAO.makeAllPersistent( lastPages );
        pageDAO.makeTransient( page );
    }

    /**
     * Deletes the page and its entire hierarchy.
     * 
     * @author IanBrown
     * @param page
     *            the page.
     * @since May 24, 2012
     * @version May 25, 2012
     */
    public void deletePageHierarchy(final QuestionnairePage page) {
        QuestionnairePage pageToDelete = findPageById(page.getId());
        final List<Question> questions = (pageToDelete.getQuestions() == null) ? null : new ArrayList<Question>(pageToDelete.getQuestions());
        if ((questions != null) && !questions.isEmpty()) {
            for (final Question question : questions) {
                if (getQuestionDAO().checkQuestionUsing(question)) {
                    throw new IllegalStateException("Cannot delete page " + page.getTitle() + ": it contains a question with dependents");
                }
            }
            
            for (final Question question : questions) {
                removeQuestionHierarchy(question);
            }
            
            pageToDelete = findPageById(page.getId());
        }

        deletePage(pageToDelete);
    }

    public int countPages( PageType type ) {
        return pageDAO.countPages( type );
    }

    public QuestionnairePage findPageByNumber( int page, PageType type ) {
        return pageDAO.findPageByNumber( page, type );
    }

    /**
     * Finds the dependent variants of the page.
     * @author IanBrown
     * @param page the page.
     * @return the dependent variants.
     * @since May 25, 2012
     * @version May 25, 2012
     */
    public Collection<QuestionVariant> findDependentVariants(final QuestionnairePage page) {
        final List<Question> questions = page.getQuestions();
        final List<QuestionVariant> dependentVariants = new ArrayList<QuestionVariant>();
        if ((questions != null) && !questions.isEmpty()) {
            for (final Question question : questions) {
                final Collection<QuestionVariant> questionDependentVariants = findDependentVariants(question);
                dependentVariants.addAll(questionDependentVariants);
            }
        }

        return dependentVariants;
    }

    /* Question Variant */

    public QuestionVariant findQuestionVariantById( long id ) {
        return questionDAO.findQuestionVariantById( id );
    }

    public void saveQuestionVariant( QuestionVariant questionVariant ) {
        questionDAO.makePersistent( questionVariant );
    }

    public void deleteQuestionVariant( QuestionVariant variant ) {
        if ( variant.getKeys().size() > 0 ) {
            questionDAO.makeAllTransient( variant.getKeys() );
        }
        questionDAO.makeTransient( variant );
    }

    /**
     * Deletes the variant and all of its fields if the variant's question has no dependents.
     * @author IanBrown
     * @param variant the variant.
     * @since May 24, 2012
     * @version May 24, 2012
     */
    public void deleteQuestionVariantHierarchy(final QuestionVariant variant) {
        if (getQuestionDAO().checkQuestionUsing(variant.getQuestion())) {
            throw new IllegalStateException("Cannot delete variant " + variant.getTitle() + ": it belongs to a question that has dependents");
        }

        removeQuestionVariantHierarchy( variant );
    }

    /**
     * Removes the variant and all of its fields.
     * @author IanBrown
     * @param variant the variant.
     * @since May 24, 2012
     * @version May 25, 2012
     */
    private void removeQuestionVariantHierarchy(final QuestionVariant variant) {
        QuestionVariant variantToDelete = findQuestionVariantById( variant.getId() );
        final Collection<QuestionField> fields = (variant.getFields() == null) ? null : new ArrayList<QuestionField>(variantToDelete.getFields());
        if ((fields != null) && !fields.isEmpty()) {
            for (final QuestionField field : fields) {
                removeQuestionField(field);
            }
            variantToDelete = findQuestionVariantById(variant.getId());
        }
        
        deleteQuestionVariant( variantToDelete );
    }

    /**
     * Removes the question field. This method is similar to the delete in {@link QuestionFieldService} but avoid issues with things being tied to multiple Hibernate sessions.
     * @author IanBrown
     * @param field the field to remove.
     * @since May 25, 2012
     * @version May 25, 2012
     */
    private void removeQuestionField(final QuestionField field) {
        int number = field.getOrder();
        Collection<QuestionField> fieldsBefore = questionFieldDAO.findFieldsBeforeNumber( field, number+1 );
        for ( QuestionField shifted : fieldsBefore ) {
            shifted.setOrder( number++ );
        }
        getQuestionFieldDAO().makeAllPersistent( fieldsBefore );
        getQuestionFieldDAO().makeAllTransient( field.getGenericOptions() );
        getQuestionFieldDAO().makeTransient( field );
    }
    
    /* Question */
    public Question findQuestionById( long id ) {
        return questionDAO.findQuestionById( id );
    }

    public void saveQuestion( Question question ) {
        boolean newQuestion = question.getId() == 0;
        boolean down = false;

        if ( newQuestion ) {
            question.setOldOrder( question.getOrder() );
        } else {
            down = question.getOldOrder() < question.getOrder();
            if ( down ) {
                question.setOrder( question.getOrder() - 1 );
            }
        }

        int startNumber = Math.min( question.getOldOrder(), question.getOrder() );
        Collection<Question> lastQuestions = questionDAO.findQuestionsAfterQuestion( question, startNumber );
        for ( Question shifted : lastQuestions ) {
            if ( shifted == question ) {
                if ( down ) startNumber++;
                continue;
            }

            shifted.setOrder( down ? startNumber++ : ++startNumber );
        }

        if ( newQuestion ) {
            lastQuestions.add( question );
        }
        questionDAO.makeAllPersistent( lastQuestions );
    }

    public void deleteQuestion( Question question ) {
        int startNumber = question.getOrder();
        Collection<Question> lastQuestions = questionDAO.findQuestionsAfterQuestion( question, startNumber + 1 );
        for ( Question shifted : lastQuestions ) {
            shifted.setOrder( startNumber++ );
        }
        questionDAO.makeAllPersistent( lastQuestions );
        questionDAO.makeTransient( question );
    }

    /**
     * Deletes the question and all of its variants if the question has no dependents.
     * 
     * @author IanBrown
     * @param question
     *            the question.
     * @since May 24, 2012
     * @version May 24, 2012
     */
    public void deleteQuestionHierarchy(final Question question) {
        if (getQuestionDAO().checkQuestionUsing(question)) {
            throw new IllegalStateException("Cannot delete question " + question.getTitle() + ": it has dependents");
        }

        removeQuestionHierarchy( question );
    }

    /**
     * Removes the question and all of its variants.
     * @author IanBrown
     * @param question the question.
     * @since May 24, 2012
     * @version May 25, 2012
     */
    private void removeQuestionHierarchy(final Question question) {
        Question questionToDelete = findQuestionById(question.getId());
        final Collection<QuestionVariant> variants = (question.getVariants() == null) ? null : new ArrayList<QuestionVariant>(questionToDelete.getVariants());
        if ((variants != null) && !variants.isEmpty()) {
            for (final QuestionVariant variant : variants) {
                removeQuestionVariantHierarchy(variant);
            }
            
            questionToDelete = findQuestionById(question.getId());
        }

        deleteQuestion(questionToDelete);
    }

    public Collection<Question> findQuestionsOfPageType( PageType pageType ) {
        return questionDAO.findQuestionsOfPageType( pageType );
    }

    /**
     * Finds the dependent variants for the question.
     * @author IanBrown
     * @param question the question.
     * @return the dependent variants.
     * @since May 25, 2012
     * @version May 25, 2012
     */
    public Collection<QuestionVariant> findDependentVariants(final Question question) {
        final Collection<QuestionVariant> dependentVariants = new ArrayList<QuestionVariant>();
        final Collection<QuestionVariant> questionDependentVariants = getQuestionDAO().findDependentVariants(question);
        if (questionDependentVariants != null) {
            dependentVariants.addAll(questionDependentVariants);
        }
        return dependentVariants;
    }

    public BasicDependency findQuestionDependencyById( long dependencyId ) {
        return questionDAO.findQuestionDependencyById( dependencyId );
    }

    public Collection<Question> findQuestionForDependency( Question question ) {
        return questionDAO.findQuestionForDependency( question );
    }

    public void saveDependency( BasicDependency dependency ) {
        questionDAO.makePersistent( dependency );
    }

    public void deleteDependency( BasicDependency dependency ) {
        questionDAO.makeTransient( dependency );
    }

    public Collection<Question> findQuestionForDependency() {
        return questionDAO.findQuestionForDependency();
    }

    public Collection<QuestionDependency> findDependents( Question question ) {
        return questionDAO.findDependents( question );
    }

    public boolean checkUsingInDependencies( Question question ) {
        return questionDAO.checkQuestionUsing( question );
    }

    public Collection<BasicDependency> findDependenciesOfType( DependencyType type, Related dependent, Question dependsOn, String fieldName ) {
        Collection<BasicDependency> dependencies = null;
        if ( type != null ) {
            switch ( type ) {
                case QUESTION:
                    if ( dependsOn != null ) {
                        dependencies = questionDAO.findQuestionDependencies( dependent, dependsOn );
                    }
                    break;
                case USER:
                    if ( fieldName != null ) {
                        dependencies = questionDAO.findUserFieldDependencies( dependent, fieldName );
                    }
                    break;
                case FACE:
                    dependencies = questionDAO.findFaceDependencies( dependent );
                    break;
                case FLOW:
                    dependencies = questionDAO.findFlowDependencies( dependent );
                    break;
            }
        }
        if ( dependencies == null ) {
            dependencies = Collections.emptyList();
        }
        return dependencies;
    }

    public void deleteDependencies( Collection<BasicDependency> dependencies ) {
        questionDAO.makeAllTransient( dependencies );
    }

    /* SurveyWizardData and Answers */


    public void saveWizardResults( WizardResults results ) {
        WizardResultAddress address = results.getVotingAddress();
        if ( address != null && address.isEmptySpace() ) {
            results.setVotingAddress( null );
            pdfAnswersDAO.makeTransient( address );
        }
        address = results.getCurrentAddress();
        if ( address != null && address.isEmptySpace() ) {
            results.setCurrentAddress( null );
            pdfAnswersDAO.makeTransient( address );
        }
        address = results.getForwardingAddress();
        if ( address != null && address.isEmptySpace() ) {
            results.setForwardingAddress( null );
            pdfAnswersDAO.makeTransient( address );
        }
        address = results.getPreviousAddress();
        if ( address != null && address.isEmptySpace() ) {
            results.setPreviousAddress( null );
            pdfAnswersDAO.makeTransient( address );
        }

        results.setLastChangedDate( new Date() );
        pdfAnswersDAO.makePersistent( results );
        for ( Answer answer : results.getAnswers() ) {
            if ( !answer.getField().isSecurity() ) {
                answerDAO.makePersistent( answer );
            }
        }
    }

    /**
     * Updates all stored answers where a user could have entered personally identifiable information
     *
     * @param user
     */
    public void makeUserAnswersAnonymity( final OverseasUser user ) {
        Collection<WizardResults> pdfs = pdfAnswersDAO.getUserPdfs( user );
        Collection<Answer> changedAnswers = new ArrayList<Answer>();
        Collection<WizardResults> changedPdfAnswers = new ArrayList<WizardResults>();
        for ( WizardResults pdf : pdfs ) {
            OverseasUser userToEvict = pdf.getUser();
            pdf.setUser( null );
            pdfAnswersDAO.evict( userToEvict );
            changedPdfAnswers.add( pdf );
            changedAnswers.addAll( pdf.anonymize() );
        }
        pdfAnswersDAO.makeAllPersistent( changedPdfAnswers );
        answerDAO.makeAllPersistent( changedAnswers );
    }


    public Collection<WizardResults> findUserPdfs( OverseasUser user ) {
        return pdfAnswersDAO.getUserPdfs( user );
    }

    public Collection<WizardResults> findByFieldValue( String value, Date after, Date before) {
        return pdfAnswersDAO.findByFieldValue( value, after, before );
    }

    public Collection<WizardResults> findByFieldValue( String value) {
        return pdfAnswersDAO.findByFieldValue( value, null, null );
    }

    public Collection<WizardResults> findByFieldSelectedValue(QuestionField field, int value, Date after, Date before) {
        return pdfAnswersDAO.findByFieldSelectedValue( field, value, after, before );
    }

    public Collection<WizardResults> findByFieldSelectedValue(QuestionField field, int value) {
        return pdfAnswersDAO.findByFieldSelectedValue( field, value, null, null );
    }

    public Map<Integer, Collection<Integer>> findCrossPageConnection( PageType type ) {
        return pageDAO.defineDependencies( type );
    }

    //migration
    public void saveMigration( MigrationDealer dealer ) {
        questionFieldDAO.makeAllPersistent( dealer.getFieldTypes() );

        pageDAO.makeAllTransient( dealer.getItemsToDelete() );
        clearFields( dealer.getFieldsToDelete() );
        pageDAO.makeAllTransient( dealer.getDependenciesToDelete() );
        clearVariants( dealer.getVariantsToDelete() );
        clearQuestionGroups( dealer.getGroupsToDelete() );
        clearPages( dealer.getPagesToDelete() );

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
        pageDAO.makeAllTransient( dealer.getFillingsToDelete() );
        pageDAO.makeAllPersistent( dealer.getFillingsToPersist() );
        for ( PdfFilling filling : dealer.getFillingsToPersist() ) {
            pageDAO.makeAllPersistent( filling.getKeys() );
        }

        pageDAO.makeAllTransient( dealer.getFieldDependenciesToDelete() );
        pageDAO.makeAllPersistent( dealer.getFieldDependenciesToPersist() );

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


    /** DAOs */
    
    /**
     * Sets the question DAO.
     * @author IanBrown
     * @param questionDAO the question DAO>
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public void setQuestionDAO(QuestionDAO questionDAO) {
        this.questionDAO = questionDAO;
    }

    /**
     * Gets the question DAO.
     * @author IanBrown
     * @return the question DAO.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public QuestionDAO getQuestionDAO() {
        return questionDAO;
    }

    /**
     * Gets the page DAO.
     * @author IanBrown
     * @return the page DAO.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public QuestionnairePageDAO getPageDAO() {
        return pageDAO;
    }

    /**
     * Sets the page DAO.
     * @author IanBrown
     * @param pageDAO the page DAO to set.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public void setPageDAO(QuestionnairePageDAO pageDAO) {
        this.pageDAO = pageDAO;
    }

    /**
     * Gets the PDF answers DAO.
     * @author IanBrown
     * @return the PDF answers DAO.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public PdfAnswersDAO getPdfAnswersDAO() {
        return pdfAnswersDAO;
    }

    /**
     * Sets the PDF answers DAO.
     * @author IanBrown
     * @param pdfAnswersDAO the PDF answers DAO to set.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public void setPdfAnswersDAO(PdfAnswersDAO pdfAnswersDAO) {
        this.pdfAnswersDAO = pdfAnswersDAO;
    }

    /**
     * Gets the answer DAO.
     * @author IanBrown
     * @return the answer DAO.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public AnswerDAO getAnswerDAO() {
        return answerDAO;
    }

    /**
     * Sets the answer DAO.
     * @author IanBrown
     * @param answerDAO the answer DAO to set.
     * @since May 15, 2012
     * @version May 15, 2012
     */
    public void setAnswerDAO(AnswerDAO answerDAO) {
        this.answerDAO = answerDAO;
    }

    /**
     * Gets the questionFieldDAO.
     * @author IanBrown
     * @return the questionFieldDAO.
     * @since May 25, 2012
     * @version May 25, 2012
     */
    public QuestionFieldDAO getQuestionFieldDAO() {
        return questionFieldDAO;
    }

    /**
     * Sets the questionFieldDAO.
     * @author IanBrown
     * @param questionFieldDAO the questionFieldDAO to set.
     * @since May 25, 2012
     * @version May 25, 2012
     */
    public void setQuestionFieldDAO(QuestionFieldDAO questionFieldDAO) {
        this.questionFieldDAO = questionFieldDAO;
    }

    /**
     * Clones the variant.
     * @author IanBrown
     * @param variant the variant.
     * @return the cloned variant.
     * @since May 29, 2012
     * @version May 30, 2012
     */
    public QuestionVariant cloneVariant(QuestionVariant variant) {
        final Question question = variant.getQuestion();
        final QuestionVariant newVariant = new QuestionVariant();
        
        newVariant.setDescription(variant.getDescription());
        newVariant.setTitle(variant.getTitle());
        newVariant.setQuestion(question);

        final Collection<QuestionField> fields = variant.getFields();
        final Collection<QuestionField> newFields = new LinkedList<QuestionField>();
        if (fields != null) {
            for (final QuestionField field : fields) {
                final QuestionField newField = cloneField(field);
                newField.setQuestion(newVariant);
                newFields.add(newField);
            }
            newVariant.setFields(newFields);
        }

        return newVariant;
    }

    /**
     * Clones the specified field.
     * @author IanBrown
     * @param field the field to clone.
     * @return the new field.
     * @since May 29, 2012
     * @version May 30, 2012
     */
    private QuestionField cloneField(QuestionField field) {
        final QuestionField newField = new QuestionField();
        newField.setTitle(field.getTitle());
        newField.setAdditionalHelp(field.getAdditionalHelp());
        newField.setEncoded(field.isEncoded());
        newField.setFirstText(field.getFirstText());
        newField.setHelpText(field.getHelpText());
        newField.setInPdfName(field.getInPdfName());
        newField.setOrder(field.getOrder());
        newField.setRequired(field.isRequired());
        newField.setSecondText(field.getSecondText());
        newField.setSecurity( field.isSecurity() );
        final FieldType type = field.getType();
        newField.setType( type );
        newField.setVerificationPattern( field.getVerificationPattern() );
        final Collection<FieldDictionaryItem> genericOptions = field.getGenericOptions();
        if (genericOptions != null) {
            final Collection<FieldDictionaryItem> newGenericOptions = new LinkedList<FieldDictionaryItem>();
            for (final FieldDictionaryItem genericOption : genericOptions) {
                final FieldDictionaryItem newGenericOption = cloneGenericOption( genericOption );
                newGenericOption.setForField( newField );
                newGenericOptions.add(newGenericOption);
            }
            newField.setGenericOptions( newGenericOptions );
        }
        return newField;
    }

    /**
     * Clones the generic option.
     * @author IanBrown
     * @param genericOption the generic option.
     * @return the new generic option.
     * @since May 29, 2012
     * @version May 29, 2012
     */
    private FieldDictionaryItem cloneGenericOption(FieldDictionaryItem genericOption) {
        final FieldDictionaryItem newGenericOption;
        if (genericOption instanceof GenericStringItem) {
            final GenericStringItem newGenericString = new GenericStringItem();
            newGenericString.setValue(((GenericStringItem) genericOption).getValue());
            newGenericOption = newGenericString;
        } else {
            // TODO
            throw new UnsupportedOperationException("Cannot clone " + genericOption);
        }
        return newGenericOption;
    }

    /**
     * Refreshes the object.
     * @author IanBrown
     * @param object the object to refresh.
     * @since May 30, 2012
     * @version May 30, 2012
     */
    public void refresh(Object object) {
        getQuestionDAO().refresh(object);
    }

    public List<QuestionField> findMailInFields() {
        return questionFieldDAO.findMailInFields();
    }

    public List<WizardResults> findForPendingConfiguration( PendingVoterRegistrationConfiguration configuration,
                                                            Date startDate,
                                                            Date endDate,
                                                            boolean useLimit ) {
        return pdfAnswersDAO.findForPendingConfiguration( configuration, startDate, endDate, useLimit );
    }

    public List<WizardResults> findWizardResultByUUID(String uuid) {
        return pdfAnswersDAO.findWizardResults(uuid);
    }

    public Long countForPendingConfiguration( PendingVoterRegistrationConfiguration configuration,
                                             Date startDate,
                                             Date endDate ) {
        return pdfAnswersDAO.countForPendingConfiguration( configuration, startDate, endDate );
    }
}
