package com.exception.qms.service.impl;

import com.alibaba.fastjson.JSON;
import com.exception.qms.utils.JsonUtil;
import com.exception.qms.domain.entity.Question;
import com.exception.qms.domain.entity.QuestionDesc;
import com.exception.qms.domain.entity.User;
import com.exception.qms.domain.mapper.QuestionDescMapper;
import com.exception.qms.domain.mapper.QuestionMapper;
import com.exception.qms.domain.mapper.UserMapper;
import com.exception.qms.elasticsearch.QuestionIndexKey;
import com.exception.qms.elasticsearch.QuestionIndexTemplate;
import com.exception.qms.service.QuestionSearchService;
import com.exception.qms.service.QuestionTagService;
import com.exception.qms.utils.ConstantsUtil;
import com.exception.qms.web.vo.common.TagResponseVO;
import com.google.common.collect.Lists;
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
public class QuestionSearchServiceImpl implements QuestionSearchService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionDescMapper questionDescMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionTagService questionTagService;
    @Autowired
    private Mapper mapper;
    @Autowired
    private TransportClient esClient;

    @Override
    public boolean index(long questionId) {
        Question question = questionMapper.selectByPrimaryKey(questionId);

        if (question == null) {
            log.warn("the question does not exist, questionId: {}", questionId);
            return false;
        }

        QuestionDesc questionDesc = questionDescMapper.queryQuestionDesc(questionId);

        if (questionDesc == null) {
            log.warn("the questionDesc does not exist, questionId: {}", questionId);
            return false;
        }

        Map<Long, List<TagResponseVO>> map = questionTagService.queryTagInfoByQuestionIds(Arrays.asList(questionId));

        List<User> users = userMapper.queryUsersByUserIds(Arrays.asList(question.getCreateUserId()));

        QuestionIndexTemplate questionIndexTemplate = mapper.map(question, QuestionIndexTemplate.class);
        questionIndexTemplate.setTitle(question.getTitleCn());
        questionIndexTemplate.setDesc(questionDesc.getDescriptionCn());
        questionIndexTemplate.setQuestionId(question.getId());
        questionIndexTemplate.setCreateUserName(CollectionUtils.isEmpty(users) ? null : users.get(0).getName());
        questionIndexTemplate.setTags(map.get(questionId));

        SearchRequestBuilder builder = esClient.prepareSearch(ConstantsUtil.EXCEPTION_INDEX)
                .setTypes(ConstantsUtil.EXCEPTION_INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(QuestionIndexKey.QUESTION_ID, questionId));

        log.info("query es question by builder: {}", builder.toString());

        SearchResponse searchResponse = builder.get();

        long totalHit = searchResponse.getHits().getTotalHits();

        boolean isSuccess;
        // create
        if (totalHit == 0) {
            isSuccess = create(questionIndexTemplate);
        }
        // update
        else if (totalHit == 1) {
            String esId = searchResponse.getHits().getAt(0).getId();
            isSuccess = update(questionIndexTemplate, esId);
        }
        // delete & create
        else {
            isSuccess = deleteAndCreate(questionIndexTemplate, totalHit);
        }
        if (isSuccess) {
            log.info("index success with questionId: {}", questionId);
        }
        return isSuccess;
    }

    private boolean create(QuestionIndexTemplate questionIndexTemplate) {
        log.info("json: {}", JSON.toJSONString(questionIndexTemplate));

        IndexResponse indexResponse = esClient.prepareIndex(ConstantsUtil.EXCEPTION_INDEX, ConstantsUtil.EXCEPTION_INDEX_TYPE)
                .setSource(JSON.toJSONString(questionIndexTemplate), XContentType.JSON)
                .get();

        log.info("create index with question, id: {}", questionIndexTemplate.getQuestionId());

            if (indexResponse.status() == RestStatus.CREATED) {
            return true;
        }
        return false;
    }

    private boolean update(QuestionIndexTemplate questionIndexTemplate, String esId) {
        UpdateResponse updateResponse = esClient.prepareUpdate(ConstantsUtil.EXCEPTION_INDEX, ConstantsUtil.EXCEPTION_INDEX_TYPE, esId)
                .setDoc(JsonUtil.toString(questionIndexTemplate), XContentType.JSON)
                .get();

        log.info("update index with question, id: {}", questionIndexTemplate.getQuestionId());

        if (updateResponse.status() == RestStatus.OK) {
            return true;
        }
        return false;
    }

    private boolean deleteAndCreate(QuestionIndexTemplate questionIndexTemplate, long totalHit) {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE.newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(QuestionIndexKey.QUESTION_ID, questionIndexTemplate))
                .source(ConstantsUtil.EXCEPTION_INDEX);

        log.info("delete by query for question: {}", questionIndexTemplate);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();

        if (deleted != totalHit) {
            log.warn("need delete {}, but {} was deleted!", totalHit, deleted);
            return false;
        }
        return create(questionIndexTemplate);
    }

    @Override
    public void remove(long questionId) {

    }
}
