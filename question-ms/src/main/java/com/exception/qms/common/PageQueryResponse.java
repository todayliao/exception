package com.exception.qms.common;

import lombok.Data;

import java.util.List;

/**
 * 分页查询 response
 * @author jiangbing(江冰)
 * @date 2017/12/21
 * @time 下午8:18
 * @discription
 **/
@Data
public class PageQueryResponse<T> extends BaseResponse<List<T>> {

    private int pageIndex;
    private int totalCount;
    private int pageSize;

    public PageQueryResponse<T> successPage(List<T> data, int pageIndex, int totalCount, int pageSize) {
        this.setSuccess(true);
        this.setData(data);
        this.setPageIndex(pageIndex);
        this.setTotalCount(totalCount);
        this.setPageSize(pageSize);
        return this;
    }

    public int getCurrentPage() {
        return this.pageIndex < 1 ? 1 : this.pageIndex;
    }

    public boolean hasNext() {
        int useCount = (this.getCurrentPage() - 1) * this.getPageSize() + this.getSize();
        return this.totalCount > useCount;
    }

    public int getTotalPage() {
        return this.pageSize == 0 ? 0 : (this.totalCount - 1) / this.pageSize + 1;
    }

    private int getSize() {
        List<T> page = (List) this.getData();
        return page == null ? 0 : page.size();
    }
}
