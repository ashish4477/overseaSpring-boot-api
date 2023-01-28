package com.bearcode.commons.DAO;

/**
 * User: Dmitry Sarkisov
 * Date: 21.12.2006
 * Time: 17:34:51
 */
public class PagingInfo {

    /**
     * First result to return
     */
    private int firstResult = 0;

    /**
     * Maximal number of results
     */
    private int maxResults = -1;

    private long actualRows;

    private String[] orderFields;

    private boolean ascending = true;

    public PagingInfo() {
    }

    public PagingInfo(String[] orderFields, boolean ascending) {
        this.orderFields = orderFields;
        this.ascending = ascending;
    }

    public PagingInfo(int firstResult, int maxResults, String[] orderFields, boolean ascending) {
        this.firstResult = firstResult;
        this.maxResults = maxResults;
        this.orderFields = orderFields;
        this.ascending = ascending;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getMaxResults() {
        return maxResults;
    }

    public void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    public String[] getOrderFields() {
        return orderFields;
    }

    public void setOrderFields(String[] orderFields) {
        this.orderFields = orderFields;
    }

    public boolean isAscending() {
        return ascending;
    }

    public void setAscending(boolean ascending) {
        this.ascending = ascending;
    }

    public long getActualRows() {
        return actualRows;
    }

    public void setActualRows(long actualRows) {
        this.actualRows = actualRows;
    }
}
