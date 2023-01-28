package com.bearcode.ovf.forms;

import com.bearcode.commons.DAO.PagingInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Leo
 * Date: Jul 10, 2007
 * Time: 7:00:46 PM
 * @author Leonid Ginzburg
 */
public class CommonFormObject {
    public static final int DEFAULT_PAGESIZE = 25;

    private int page = 0;
    private int pageSize = DEFAULT_PAGESIZE;
    private PagingInfo pagingInfo = null;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public PagingInfo createPagingInfo() {
        pagingInfo = new PagingInfo();
        pagingInfo.setFirstResult( page*pageSize );
        pagingInfo.setMaxResults( pageSize );
        return pagingInfo;
    }

    public PagingInfo getPagingInfo() {
        return pagingInfo;
    }

    public int getPagesTotal() {
        if ( pagingInfo.getActualRows() == 0 ) return 1;
        return pageSize <= 0 ? 1 : (int) Math.ceil( (double)pagingInfo.getActualRows() / pageSize );
    }

    public Map getPagingParams() {
        Map data = new HashMap();
        if ( pageSize != 0 && pageSize != DEFAULT_PAGESIZE ) data.put("pageSize", pageSize );
        return data;
    }
}
