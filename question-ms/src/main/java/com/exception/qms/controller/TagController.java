package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.TagBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@RestController
@RequestMapping("/tag")
public class TagController extends BaseController {

    @Autowired
    private TagBusiness tagBusiness;

    /**
     * 标签模糊查询
     * @return
     */
    @PostMapping("/queryByName")
    @OperatorLog(description = "根据输入模糊查询标签信息")
    @ApiOperation("根据输入模糊查询标签信息")
    public BaseResponse queryTagsByTagName(@Validated @RequestBody QueryTagsByNameRequestDTO queryTagsByNameRequestDTO) {
        return tagBusiness.queryTagsByTagName(queryTagsByNameRequestDTO);
    }

}
