package com.exception.qms.common;

import lombok.Data;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:56
 * @discription
 **/
@Data
public class PageQueryRequest<T> extends BaseRequest<T> {

    private int pageIndex = 1;
    private int pageSize = 20;
    private int totalCount;
    private int start;

    public int getPageIndex() {
        return this.pageIndex <= 0 ? 1 : this.pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex <= 0 ? 1 : pageIndex;
    }

    public int getPageSize() {
        return this.pageSize > 0 && this.pageSize <= 500 ? this.pageSize : 20;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize <= 0 ? 20 : pageSize;
    }

    public int getStartPos() {
        return (this.getPageIndex() - 1) * this.getPageSize();
    }

    public int getStart() {
        return this.start;
    }

    public void setStart(int start) {
        this.start = start;
    }

}
