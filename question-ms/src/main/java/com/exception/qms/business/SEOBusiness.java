package com.exception.qms.business;

import site.exception.common.BaseResponse;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface SEOBusiness {

    BaseResponse pushAllQuestion();

    String createSiteMapXmlContent();
}
