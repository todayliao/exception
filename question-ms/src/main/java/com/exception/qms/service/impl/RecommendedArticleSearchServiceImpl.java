package com.exception.qms.service.impl;

import com.alibaba.fastjson.JSON;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.QuestionDesc;
import com.exception.qms.domain.entity.RecommendedArticle;
import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.mapper.QuestionDescMapper;
import com.exception.qms.domain.mapper.QuestionMapper;
import com.exception.qms.domain.mapper.RecommendedArticleMapper;
import com.exception.qms.domain.mapper.UserMapper;
import com.exception.qms.elasticsearch.QuestionIndexKey;
import com.exception.qms.elasticsearch.QuestionIndexTemplate;
import com.exception.qms.elasticsearch.RecommendedArticleIndexKey;
import com.exception.qms.elasticsearch.RecommendedArticleIndexTemplate;
import com.exception.qms.service.QuestionSearchService;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.service.RecommendedArticleSearchService;
import com.exception.qms.service.RecommendedArticleService;
import com.exception.qms.utils.ConstantsUtil;
import com.exception.qms.utils.JsonUtil;
import com.exception.qms.web.vo.common.TagResponseVO;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author jiangbing(江冰)
 * @date 2017/12/22
 * @time 下午2:04
 * @discription
 **/
@Service
@Slf4j
public class RecommendedArticleSearchServiceImpl implements RecommendedArticleSearchService {

    @Autowired
    private RecommendedArticleMapper recommendedArticleMapper;
    @Autowired
    private Mapper mapper;
    @Autowired
    private TransportClient esClient;

    @Override
    public boolean index(long recommendedArticleId) {
        RecommendedArticle recommendedArticle = recommendedArticleMapper.selectByPrimaryKey(recommendedArticleId);

        if (recommendedArticle == null) {
            log.warn("the recommended article does not exist, recommendedArticleId: {}", recommendedArticleId);
            return false;
        }

        RecommendedArticleIndexTemplate recommendedArticleIndexTemplate = mapper.map(recommendedArticle, RecommendedArticleIndexTemplate.class);

        SearchRequestBuilder builder = esClient.prepareSearch(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX)
                .setTypes(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(RecommendedArticleIndexKey.ID, recommendedArticleId));

        log.info("query es recommended article by builder: {}", builder.toString());

        SearchResponse searchResponse = builder.get();

        long totalHit = searchResponse.getHits().getTotalHits();

        boolean isSuccess;
        // create
        if (totalHit == 0) {
            isSuccess = create(recommendedArticleIndexTemplate);
        }
        // update
        else if (totalHit == 1) {
            String esId = searchResponse.getHits().getAt(0).getId();
            isSuccess = update(recommendedArticleIndexTemplate, esId);
        }
        // delete & create
        else {
            isSuccess = deleteAndCreate(recommendedArticleIndexTemplate, totalHit);
        }
        if (isSuccess) {
            log.info("index success with recommendedArticleId: {}", recommendedArticleId);
        }
        return isSuccess;
    }

    @Override
    public void remove(long questionId) {

    }

    private boolean create(RecommendedArticleIndexTemplate recommendedArticleIndexTemplate) {
        log.info("json: {}", JSON.toJSONString(recommendedArticleIndexTemplate));

        IndexResponse indexResponse = esClient.prepareIndex(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX, ConstantsUtil.RECOMMENDED_ARTICLE_INDEX_TYPE)
                .setSource(JSON.toJSONString(recommendedArticleIndexTemplate), XContentType.JSON)
                .get();

        log.info("create index with recommendedArticle, id: {}", recommendedArticleIndexTemplate.getId());

        if (indexResponse.status() == RestStatus.CREATED) {
            return true;
        }
        return false;
    }

    private boolean update(RecommendedArticleIndexTemplate recommendedArticleIndexTemplate, String esId) {
        UpdateResponse updateResponse = esClient.prepareUpdate(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX, ConstantsUtil.RECOMMENDED_ARTICLE_INDEX_TYPE, esId)
                .setDoc(JsonUtil.toString(recommendedArticleIndexTemplate), XContentType.JSON)
                .get();

        log.info("update index with recommendedArticle, id: {}", recommendedArticleIndexTemplate.getId());

        if (updateResponse.status() == RestStatus.OK) {
            return true;
        }
        return false;
    }

    private boolean deleteAndCreate(RecommendedArticleIndexTemplate recommendedArticleIndexTemplate, long totalHit) {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(RecommendedArticleIndexKey.ID, recommendedArticleIndexTemplate))
                .source(ConstantsUtil.RECOMMENDED_ARTICLE_INDEX);

        log.info("delete by query for recommendedArticle: {}", recommendedArticleIndexTemplate);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();

        if (deleted != totalHit) {
            log.warn("need delete {}, but {} was deleted!", totalHit, deleted);
            return false;
        }
        return create(recommendedArticleIndexTemplate);
    }
}
