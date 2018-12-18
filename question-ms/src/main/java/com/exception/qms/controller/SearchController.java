package com.exception.qms.controller;

import com.exception.qms.aspect.OperatorLog;
import com.exception.qms.business.SearchBusiness;
import com.exception.qms.enums.ResponseModelKeyEnum;
import com.exception.qms.enums.TopNavEnum;
import com.exception.qms.model.dto.question.response.SearchAboutQuestionResponseDTO;
import com.exception.qms.model.dto.question.response.SearchAboutRecommendedArticleResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import site.exception.common.BaseResponse;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
@Controller
public class SearchController {

    @Autowired
    private SearchBusiness searchBusiness;

    @GetMapping("/search")
    @OperatorLog(description = "问题搜索")
    public String searchQuestion(@RequestParam(value = "pageIndex", defaultValue = "1") Integer pageIndex,
                                 @RequestParam(value = "pageSize", defaultValue = "20") Integer pageSize,
                                 @RequestParam(value = "key", required = false, defaultValue = "") String key,
                                 @RequestParam(value = "tab", required = false, defaultValue = "relevance") String tab,
                                 Model model) {
        model.addAttribute(ResponseModelKeyEnum.RESPONSE.getCode(), searchBusiness.searchQuestion(pageIndex, pageSize, key, tab));
        model.addAttribute(ResponseModelKeyEnum.TOP_NAV.getCode(), TopNavEnum.HOME.getCode());
        model.addAttribute(ResponseModelKeyEnum.TAB.getCode(), tab);
        model.addAttribute("searchKey", key);
        return "question/question-search";
    }

    @GetMapping("/search/question/about")
    @OperatorLog(description = "相关问题搜索")
    @ResponseBody
    public BaseResponse<List<SearchAboutQuestionResponseDTO>> searchAboutQuestion(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "id") Long id) {
        return searchBusiness.searchAboutQuestion(title, id);
    }

    @GetMapping("/search/recommended/article/about")
    @OperatorLog(description = "优选文章相关搜索")
    @ResponseBody
    public BaseResponse<List<SearchAboutRecommendedArticleResponseDTO>> searchAboutRecommendedArticle(
            @RequestParam(value = "title") String title,
            @RequestParam(value = "id") Long id) {
        return searchBusiness.searchAboutRecommendedArticle(title, id);
    }


    @GetMapping("/search/question/allIndex/update")
    @OperatorLog(description = "更新所有问题索引")
    @ResponseBody
    public BaseResponse updateAllQuestionIndex() {
        return searchBusiness.updateAllQuestionIndex();
    }

}
