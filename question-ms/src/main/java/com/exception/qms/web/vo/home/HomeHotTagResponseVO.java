package com.exception.qms.web.vo.home;

import lombok.Data;

import java.io.Serializable;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/26
 * @time 上午11:12
 * @discription
 **/
@Data
public class HomeHotTagResponseVO implements Serializable {
    private Long tagId;
    private String tagName;
    private Integer questionCount;
}
