package com.exception.qms.business;

import com.exception.qms.model.dto.question.response.SearchAboutQuestionResponseDTO;
import com.exception.qms.model.dto.question.response.SearchAboutRecommendedArticleResponseDTO;
import com.exception.qms.model.vo.common.QuestionSearchResponseVO;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

import java.util.List;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/16
 * @time 下午9:19
 * @discription
 **/
public interface SearchBusiness {

    BaseResponse updateAllQuestionIndex();

    PageQueryResponse<QuestionSearchResponseVO> searchQuestion(Integer pageIndex, Integer pageSize, String key, String tab);

    BaseResponse<List<SearchAboutQuestionResponseDTO>> searchAboutQuestion(String title, Long id);

    BaseResponse<List<SearchAboutRecommendedArticleResponseDTO>> searchAboutRecommendedArticle(String title, Long id);
}
