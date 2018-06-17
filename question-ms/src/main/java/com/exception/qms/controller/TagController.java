package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.TagBusiness;
import com.exception.qms.common.BaseController;
import com.exception.qms.common.BaseResponse;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.web.dto.question.request.QueryTagsByNameRequestDTO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class TagController extends BaseController {

    @Autowired
    private TagBusiness tagBusiness;

    @GetMapping("/tag")
    @OperatorLog(description = "展示标签墙")
    public String showUserWall(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                               @RequestParam(value = "pageSize", defaultValue = "36") Integer pageSize,
                               Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), tagBusiness.queryTagPageList(pageIndex, pageSize));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.TAG.getCode());
        return "tag/tag-wall";
    }


    /**
     * 标签模糊查询
     * @return
     */
    @PostMapping("/tag/queryByName")
    @OperatorLog(description = "根据输入模糊查询标签信息")
    @ApiOperation("根据输入模糊查询标签信息")
    @ResponseBody
    public BaseResponse queryTagsByTagName(@Validated @RequestBody QueryTagsByNameRequestDTO queryTagsByNameRequestDTO) {
        return tagBusiness.queryTagsByTagName(queryTagsByNameRequestDTO);
    }

}
