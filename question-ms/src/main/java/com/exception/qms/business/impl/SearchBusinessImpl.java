package com.exception.qms.business.impl;

import com.alibaba.fastjson.JSON;
import com.exception.qms.business.SearchBusiness;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.RecommendedArticle;
import com.exception.qms.elasticsearch.QuestionIndexKey;
import com.exception.qms.elasticsearch.RecommendedArticleIndexKey;
import com.exception.qms.enums.QmsResponseCodeEnum;
import com.exception.qms.enums.QuestionSearchTabEnum;
import com.exception.qms.exception.QMSException;
import com.exception.qms.service.QuestionSearchService;
import com.exception.qms.service.QuestionService;
import com.exception.qms.service.RecommendedArticleSearchService;
import com.exception.qms.service.RecommendedArticleService;
import com.exception.qms.utils.ConstantsUtil;
import com.exception.qms.utils.PageUtil;
import com.exception.qms.utils.SearchUtil;
import com.exception.qms.utils.StringUtil;
import com.exception.qms.web.dto.question.response.SearchAboutQuestionResponseDTO;
import com.exception.qms.web.dto.question.response.SearchAboutRecommendedArticleResponseDTO;
import com.exception.qms.web.vo.common.QuestionSearchResponseVO;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.core.util.Integers;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import site.exception.common.BaseResponse;
import site.exception.common.PageQueryResponse;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/20
 * @time 下午3:44
 * @discription
 **/
@Service
@Slf4j
public class SearchBusinessImpl implements SearchBusiness {

    @Autowired
    private QuestionSearchService questionSearchService;
    @Autowired
    private RecommendedArticleSearchService recommendedArticleSearchService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private RecommendedArticleService recommendedArticleService;
    @Autowired
    private ExecutorService executorService;
    @Autowired
    private TransportClient esClient;

    /** 分片查询，最大查询数 **/
    private final static int MAX_QUERY = 100;

    @Override
    public BaseResponse updateAllQuestionIndex() {
        final long totalCount = questionService.queryQuestionTotalCount();

        if (totalCount > 0) {
            final long limit = (totalCount % MAX_QUERY == 0) ? (totalCount / MAX_QUERY) : (totalCount / MAX_QUERY + 1);

            Stream.iterate(0, n -> n + 1).limit(limit).forEach(n -> {
                List<Question> questions = questionService.queryQuestionPageList(n + 1, MAX_QUERY, null);
                questions.parallelStream().forEach(question ->
                        executorService.execute(() -> questionSearchService.index(question.getId())));
            });
        }
        return new BaseResponse().success();
    }

    @Override
    public BaseResponse updateAllRecommendedArticleIndex() {
        int totalCount = recommendedArticleService.queryRecommendedArticleTotalCount();
        if (totalCount > 0) {
            int limit = (totalCount % MAX_QUERY == 0) ? (totalCount / MAX_QUERY) : (totalCount / MAX_QUERY + 1);
            Stream.iterate(0, n -> n + 1).limit(limit).forEach(n -> {
                List<RecommendedArticle> recommendedArticles = recommendedArticleService.queryRecommendedArticleList(n + 1, MAX_QUERY);
                recommendedArticles.parallelStream().forEach(recommendedArticle ->
                        executorService.execute(() -> recommendedArticleSearchService.index(recommendedArticle.getId())));
            });
        }
        return new BaseResponse().success();
    }

    @Override
    public PageQueryResponse<QuestionSearchResponseVO> searchQuestion(Integer pageIndex, Integer pageSize, String key, String tab) {
        if (StringUtils.isBlank(key)) {
            log.warn("the search key is empty");
            return new PageQueryResponse<QuestionSearchResponseVO>()
                    .successPage(null,
                            pageIndex,
                            0,
                            pageSize);
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(
                QueryBuilders.multiMatchQuery(key,
                QuestionIndexKey.TITLE,
                QuestionIndexKey.DESC
        ));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch(ConstantsUtil.EXCEPTION_INDEX)
                .setTypes(ConstantsUtil.EXCEPTION_INDEX_TYPE)
                .setQuery(boolQueryBuilder);

        // sort
        QuestionSearchTabEnum questionSearchTabEnum = QuestionSearchTabEnum.codeOf(tab);
        switch (questionSearchTabEnum) {
            case VOTE:
                requestBuilder.addSort(QuestionIndexKey.VOTE_UP, SortOrder.DESC);
                break;
            case VIEW:
                requestBuilder.addSort(QuestionIndexKey.VIEW_NUM, SortOrder.DESC);
                break;
            case NEW:
                requestBuilder.addSort(QuestionIndexKey.CREATE_TIME, SortOrder.DESC);
                break;
            default:
                // 默认按相关度排序
                break;
        }

        requestBuilder.setFrom(PageUtil.calculateLimitSelectSqlStart(pageIndex, pageSize))
                      .setSize(pageSize);

        // set highlight
        requestBuilder.highlighter(SearchUtil.highlightBuilder);

        log.info("the search request builder: {}", requestBuilder.toString());
        SearchResponse response = requestBuilder.get();

        if (response.status() != RestStatus.OK) {
           log.warn("search status is not ok for: {}", requestBuilder.toString());
           return new PageQueryResponse<QuestionSearchResponseVO>()
                    .successPage(null,
                            pageIndex,
                            0,
                            pageSize);
        }

        Long totalHits = response.getHits().getTotalHits();

        List<QuestionSearchResponseVO> questionSearchResponseVOS = Lists.newArrayList();
        response.getHits().forEach(searchHitFields -> {
            QuestionSearchResponseVO searchResponseVO = new QuestionSearchResponseVO();
            searchResponseVO.setQuestionId(Longs.tryParse(String.valueOf(searchHitFields.getSource().get(QuestionIndexKey.QUESTION_ID))));
            // set highlight
            boolean isTitleCnHighlightExist =
                    !CollectionUtils.isEmpty(searchHitFields.getHighlightFields()) &&
                            searchHitFields.getHighlightFields().get(QuestionIndexKey.TITLE) != null;
            if (isTitleCnHighlightExist) {
                // 转义
                String html = Arrays.asList(searchHitFields.getHighlightFields()
                                .get(QuestionIndexKey.TITLE)
                                .getFragments()).stream().map(Text::toString).collect(Collectors.joining());

                searchResponseVO.setTitleCn(SearchUtil.escapeHtmlButHighlight(html));
            } else {
                searchResponseVO.setTitleCn(searchHitFields.getSource().get(QuestionIndexKey.TITLE).toString());
            }

            boolean isDescriptionCnHighlightExist = !CollectionUtils.isEmpty(searchHitFields.getHighlightFields()) &&
                    searchHitFields.getHighlightFields().get(QuestionIndexKey.DESC) != null;
            if (isDescriptionCnHighlightExist) {
                // 转义
                String html = Arrays.asList(searchHitFields.getHighlightFields().get(QuestionIndexKey.DESC)
                        .getFragments()).stream().map(Text::toString).collect(Collectors.joining());

                searchResponseVO.setDescriptionCn(
                        StringUtil.subString(SearchUtil.escapeHtmlButHighlight(html), 500));
            } else {
                searchResponseVO.setDescriptionCn(StringUtil.subString(searchHitFields.getSource().get(QuestionIndexKey.DESC).toString(), 500));
            }

            String tagsJson = JSON.toJSONString(searchHitFields.getSource().get(QuestionIndexKey.TAGS));
            List<TagResponseVO> responseVOS = JSON.parseArray(tagsJson, TagResponseVO.class);
            searchResponseVO.setTags(responseVOS);

            searchResponseVO.setCreateUserId(Longs.tryParse(String.valueOf(searchHitFields.getSource().get(QuestionIndexKey.CREATE_USER_ID))));
            searchResponseVO.setCreateUserName(searchHitFields.getSource().get(QuestionIndexKey.CREATE_USER_NAME).toString());
            searchResponseVO.setCreateTime(searchHitFields.getSource().get(QuestionIndexKey.CREATE_TIME).toString());
            questionSearchResponseVOS.add(searchResponseVO);
        });

        return new PageQueryResponse<QuestionSearchResponseVO>()
                .successPage(questionSearchResponseVOS,
                pageIndex,
                totalHits.intValue(),
                pageSize);
    }

    @Override
    public BaseResponse<List<SearchAboutQuestionResponseDTO>> searchAboutQuestion(String title, Long id) {
        if (StringUtils.isBlank(title)) {
            log.warn("the search title is empty");
            return new BaseResponse<List<SearchAboutQuestionResponseDTO>>()
                    .fail(new QMSException(QmsResponseCodeEnum.SEARCH_KEY_EMPTY));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(
                QueryBuilders.multiMatchQuery(title,
                        QuestionIndexKey.TITLE,
                        QuestionIndexKey.DESC
                ));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch(ConstantsUtil.EXCEPTION_INDEX)
                .setTypes(ConstantsUtil.EXCEPTION_INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(11);

        log.info("the search request builder: {}", requestBuilder.toString());
        SearchResponse response = requestBuilder.get();

        if (response.status() != RestStatus.OK) {
            log.warn("search status is not ok for: {}", requestBuilder.toString());
            return new BaseResponse<List<SearchAboutQuestionResponseDTO>>()
                    .fail(new QMSException(QmsResponseCodeEnum.ES_STATUS_NOT_OK));
        }

        List<SearchAboutQuestionResponseDTO> searchAboutQuestionResponseDTOS = Lists.newArrayList();
        response.getHits().forEach(searchHitFields -> {
            Long hitQuestionId = Longs.tryParse(String.valueOf(searchHitFields.getSource().get(QuestionIndexKey.QUESTION_ID)));
            if (!Objects.equals(id, hitQuestionId)) {
                SearchAboutQuestionResponseDTO searchAboutQuestionResponseDTO = new SearchAboutQuestionResponseDTO();
                searchAboutQuestionResponseDTO.setId(hitQuestionId);
                searchAboutQuestionResponseDTO.setTitle(searchHitFields.getSource().get(QuestionIndexKey.TITLE).toString());
                searchAboutQuestionResponseDTO.setVoteUp(Integers.parseInt(String.valueOf(searchHitFields.getSource().get(QuestionIndexKey.VOTE_UP))));
                searchAboutQuestionResponseDTOS.add(searchAboutQuestionResponseDTO);
            }
        });

        return new BaseResponse<List<SearchAboutQuestionResponseDTO>>().success(searchAboutQuestionResponseDTOS);
    }

    @Override
    public BaseResponse<List<SearchAboutRecommendedArticleResponseDTO>> searchAboutRecommendedArticle(String title, Long id) {
        if (StringUtils.isBlank(title)) {
            log.warn("the search title is empty");
            return new BaseResponse<List<SearchAboutRecommendedArticleResponseDTO>>()
                    .fail(new QMSException(QmsResponseCodeEnum.SEARCH_KEY_EMPTY));
        }

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(
                QueryBuilders.multiMatchQuery(title,
                        RecommendedArticleIndexKey.TITLE,
                        RecommendedArticleIndexKey.SUMMARY
                ));

        SearchRequestBuilder requestBuilder = esClient.prepareSearch(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX)
                .setTypes(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX_TYPE)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(16);

        log.info("the search request builder: {}", requestBuilder.toString());
        SearchResponse response = requestBuilder.get();

        if (response.status() != RestStatus.OK) {
            log.warn("search status is not ok for: {}", requestBuilder.toString());
            return new BaseResponse<List<SearchAboutRecommendedArticleResponseDTO>>()
                    .fail(new QMSException(QmsResponseCodeEnum.ES_STATUS_NOT_OK));
        }

        List<SearchAboutRecommendedArticleResponseDTO> searchAboutRecommendedArticleResponseDTOS = Lists.newArrayList();
        response.getHits().forEach(searchHitFields -> {
            Long hitRecommendedArticleId = Longs.tryParse(String.valueOf(searchHitFields.getSource().get(RecommendedArticleIndexKey.ID)));
            if (!Objects.equals(id, hitRecommendedArticleId)) {
                SearchAboutRecommendedArticleResponseDTO searchAboutRecommendedArticleResponseDTO = new SearchAboutRecommendedArticleResponseDTO();
                searchAboutRecommendedArticleResponseDTO.setId(hitRecommendedArticleId);
                searchAboutRecommendedArticleResponseDTO.setTitle(searchHitFields.getSource().get(RecommendedArticleIndexKey.TITLE).toString());
                searchAboutRecommendedArticleResponseDTOS.add(searchAboutRecommendedArticleResponseDTO);
            }
        });

        return new BaseResponse<List<SearchAboutRecommendedArticleResponseDTO>>().success(searchAboutRecommendedArticleResponseDTOS);
    }
}
