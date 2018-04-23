package com.exception.qms.es;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.engine.Engine;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author jiangbing(江冰)
 * @date 2018/4/17
 * @time 下午4:55
 * @discription
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class EsTests {

    @Autowired
    private TransportClient esClient;

    @Test
    public void testEsQuery() {
        GetResponse getResponse = esClient.prepareGet("people", "man", "1").get();
        if (getResponse.isExists()) {
            log.info(getResponse.getSource().toString());
        }
    }

    @Test
    public void testEsInsert() throws IOException {
        XContentBuilder content = XContentFactory.jsonBuilder().startObject()
                .field("name", "wanglin")
                .field("country", "china")
                .field("age", 20)
                .field("date", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .endObject();

        IndexResponse response = esClient.prepareIndex("people", "man").setSource(content).get();
        log.info("insert into success, id:{}", response.getId());
    }

    @Test
    public void testEsDelete() {
        DeleteResponse deleteResponse = esClient.prepareDelete("people", "man", "1")
                .get();
        log.info(deleteResponse.getResult().toString());
    }

    @Test
    public void testEsUpdate() throws Exception {
        UpdateRequest updateRequest = new UpdateRequest("people", "man", "AWLRyBj6G2bd1KPM4BEs");

        XContentBuilder contentBuilder = XContentFactory.jsonBuilder().startObject();

        contentBuilder.field("name", "new name");

        contentBuilder.endObject();

        updateRequest.doc(contentBuilder);

        UpdateResponse  updateResponse = this.esClient.update(updateRequest).get();

        log.info(updateResponse.getResult().toString());
    }

    @Test
    public void testEsMultiQuery() throws Exception {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        boolQueryBuilder.must(QueryBuilders.matchQuery("name", "wanglin"));

        SearchRequestBuilder searchRequestBuilder = esClient.prepareSearch("people")
                .setTypes("man")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(boolQueryBuilder)
                .setFrom(0)
                .setSize(20);

        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("age").from("10").to("30");

        boolQueryBuilder.filter(rangeQueryBuilder);

        log.info("searchRequestBuilder: {}", searchRequestBuilder);

        SearchResponse searchResponse = searchRequestBuilder.get();

        List<Map<String, Object>> list = Lists.newArrayList();

        searchResponse.getHits().forEach(searchHitFields -> {
            list.add(searchHitFields.getSource());
        });

        log.info("list: {}", list);

    }
}
