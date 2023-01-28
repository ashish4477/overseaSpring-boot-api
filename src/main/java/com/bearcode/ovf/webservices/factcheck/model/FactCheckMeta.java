package com.bearcode.ovf.webservices.factcheck.model;

import java.io.Serializable;

/**
 * Date: 27.05.14
 * Time: 16:35
 *
 * @author Leonid Ginzburg
 */
public class FactCheckMeta implements Serializable {
    private static final long serialVersionUID = -5352511324214737341L;

    private Integer limit;
    private Integer offset;
    private Integer totalCount;
    private String next;
    private String previous;

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }
}
