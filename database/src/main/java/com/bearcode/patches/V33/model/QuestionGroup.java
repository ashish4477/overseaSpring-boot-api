package com.bearcode.patches.V33.model;

/**
 * Date: 30.04.12
 * Time: 23:41
 *
 * @author Leonid Ginzburg
 */
public class QuestionGroup {
    private long id;
    private long pageId;
    private String name = "";
    private String title = "";
    private int order;

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

    public int getOrder() {
        return order;
    }

    public void setOrder( int order ) {
        this.order = order;
    }

    public void copyFrom( QuestionGroup question ) {
        name = question.getName();
        title = question.getTitle();
    }
}
