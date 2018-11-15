package com.exception.qms.common;

import lombok.Data;
import site.exception.common.PageQueryResponse;

import java.util.List;

/**
 * 分页查询 response
 * @author jiangbing(江冰)
 * @date 2017/12/21
 * @time 下午8:18
 * @discription
 **/
@Data
public class HomePageQueryResponse<T> extends PageQueryResponse<T> {
    private String qLimitTime;
    private String aLimitTime;

    public HomePageQueryResponse<T> successPage(List<T> data, int pageIndex, int totalCount, int pageSize, String qLimitTime, String aLimitTime) {
        this.setSuccess(true);
        this.setData(data);
        this.setPageIndex(pageIndex);
        this.setTotalCount(totalCount);
        this.setPageSize(pageSize);
        this.setQLimitTime(qLimitTime);
        this.setALimitTime(aLimitTime);
        return this;
    }
}
