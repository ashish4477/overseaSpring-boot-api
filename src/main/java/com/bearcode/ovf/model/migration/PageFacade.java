package com.bearcode.ovf.model.migration;

import com.bearcode.ovf.model.questionnaire.*;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * Date: 16.12.11
 * Time: 13:23
 *
 * @author Leonid Ginzburg
 */
public class PageFacade extends AbleToMigrate {
    private long id;
    private String title;
    private String popupBubble;
    private int number;
    private int stepNumber;
    private PageType pageType;
    private PageClassType pageClassType;
    private Collection<GroupFacade> questionGroups = new LinkedList<GroupFacade>();

    public PageFacade() {
    }

    public PageFacade( QuestionnairePage page ) {
        if ( page instanceof AddOnPage ) {
            this.pageClassType = PageClassType.ADD_ON;
        }
        else if ( page instanceof ExternalPage ) {
            this.pageClassType = PageClassType.EXTERNAL;
        }
        else {
            this.pageClassType = PageClassType.GENERAL;
        }
        id = page.getId();
        title = page.getTitle();
        number = page.getNumber();
        stepNumber = page.getStepNumber();
        pageType = page.getType();
        popupBubble = page.getPopupBubble();

        for ( Question group : page.getQuestions() ) {
            questionGroups.add( new GroupFacade( group ) );
        }
    }

    public QuestionnairePage createPage() {
        QuestionnairePage page;
        switch ( this.pageClassType ) {
            case ADD_ON:
                page = new AddOnPage();
                break;
            case EXTERNAL:
                page = new ExternalPage();
                break;
            default:
                page = new QuestionnairePage();
                break;
        }
        exportTo( page );
        page.setQuestions( new LinkedList<Question>() );
        return page;
    }

    public void exportTo( QuestionnairePage page ) {
        page.setType( pageType );
        page.setTitle( title );
        page.setNumber( number );
        page.setStepNumber( stepNumber );
        page.setPopupBubble( popupBubble );
    }

    public boolean matchClass( QuestionnairePage page ) {
        Class desired = null;
        switch ( this.pageClassType ) {
            case ADD_ON:
                desired = AddOnPage.class;
                break;
            case EXTERNAL:
                desired = ExternalPage.class;
                break;
            default:
                desired = QuestionnairePage.class;
                break;
        }
        return page.getClass().equals( desired );
    }

    public long getId() {
        return id;
    }

    public void setId( long id ) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle( String title ) {
        this.title = title;
    }

    public String getPopupBubble() {
        return popupBubble;
    }

    public void setPopupBubble( String popupBubble ) {
        this.popupBubble = popupBubble;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber( int number ) {
        this.number = number;
    }

    public int getStepNumber() {
        return stepNumber;
    }

    public void setStepNumber( int stepNumber ) {
        this.stepNumber = stepNumber;
    }

    public PageType getPageType() {
        return pageType;
    }

    public void setPageType( PageType pageType ) {
        this.pageType = pageType;
    }

    public PageClassType getPageClassType() {
        return pageClassType;
    }

    public void setPageClassType( PageClassType pageClassType ) {
        this.pageClassType = pageClassType;
    }

    public Collection<GroupFacade> getQuestionGroups() {
        return questionGroups;
    }

    public void setQuestionGroups( Collection<GroupFacade> questionGroups ) {
        this.questionGroups = questionGroups;
    }

    @Override
    public String getBaseClassName() {
        switch ( this.pageClassType ) {
            case ADD_ON:
                return AddOnPage.class.getSimpleName();
            case EXTERNAL:
                return ExternalPage.class.getSimpleName();
            default:
                return QuestionnairePage.class.getSimpleName();
        }
    }

    @Override
    public long assignMigrationId( Map<String, Long> outputMap, Collection<MigrationId> createdIds, long migrationId, int version ) {
        long mId = super.assignMigrationId( outputMap, createdIds, migrationId, version );
        for ( GroupFacade groupFacade : questionGroups ) {
            mId = groupFacade.assignMigrationId( outputMap, createdIds, mId, version );
        }
        return mId;
    }
}
