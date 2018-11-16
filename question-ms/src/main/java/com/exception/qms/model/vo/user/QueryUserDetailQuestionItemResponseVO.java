package com.exception.qms.model.vo.user;

import com.exception.qms.model.vo.common.TagResponseVO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/5
 * @time 下午6:24
 * @discription 用户详情展示 - 问题方案
 **/
@Data
public class QueryUserDetailQuestionItemResponseVO implements Serializable {
    private Long id;
    private String titleCn;
    /**
     * 时间差
     */
    private String beforeTimeStr;
    private List<TagResponseVO> tags;

}
