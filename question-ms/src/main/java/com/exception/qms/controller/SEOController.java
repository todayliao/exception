package com.exception.qms.controller;

import com.exception.qms.business.SEOBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription SEO
 **/
@RestController
@RequestMapping("/seo")
@Slf4j
public class SEOController extends BaseController {

    @Autowired
    private SEOBusiness seoBusiness;

    /**
     * 主动推送所有问题的连接给百度
     *
     * @return
     */
    @GetMapping("/baidu/pushAllQuestion")
    public BaseResponse pushAllQuestion() {
        return seoBusiness.pushAllQuestion();
    }

}
