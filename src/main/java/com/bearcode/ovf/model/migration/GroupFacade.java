package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.Question;
import com.bearcode.ovf.model.questionnaire.QuestionVariant;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Date: 16.12.11
 * Time: 13:49
 *
 * @author Leonid Ginzburg
 */
public class GroupFacade extends AbleToMigrate {
    private long id;
    private long pageId;
    private int order;
    private String name;
    private String title;
    private String htmlClassFieldset = "";
    private String htmlClassOption = "";
    private Collection<VariantFacade> variants = new LinkedList<VariantFacade>();

    public GroupFacade() {
    }

    public GroupFacade( Question question ) {
        id = question.getId();
        pageId = question.getPage().getId();
        order = question.getOrder();
        name = question.getName();
        title = question.getTitle();
        htmlClassFieldset = question.getHtmlClassFieldset();
        htmlClassOption = question.getHtmlClassOption();

        for ( QuestionVariant variant : question.getVariants() ) {
            variants.add( new VariantFacade( variant ) );
        }
    }

    public Question createQuestion() {
        Question question = new Question();
        exportTo( question );
        question.setVariants( new LinkedList<QuestionVariant>() );
        return question;
    }

    public void exportTo( Question question ) {
        question.setName( name );
        question.setOrder( order );
        question.setTitle( title );
        question.setHtmlClassFieldset( htmlClassFieldset );
        question.setHtmlClassOption( htmlClassOption );
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public long getPageId() {
        return pageId;
    }

    public void setPageId( long pageId ) {
        this.pageId = pageId;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public Collection<VariantFacade> getVariants() {
        return variants;
    }

    public void setVariants( Collection<VariantFacade> variants ) {
        this.variants = variants;
    }

    @Override
    public String getBaseClassName() {
        return Question.class.getSimpleName();
    }

    @Override
    public long assignMigrationId( Map<String, Long> outputMap, Collection<MigrationId> createdIds, long migrationId, int version ) {
        long mId = super.assignMigrationId( outputMap, createdIds, migrationId, version );
        for ( VariantFacade variantFacade : variants ) {
            mId = variantFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        return mId;
    }
}
