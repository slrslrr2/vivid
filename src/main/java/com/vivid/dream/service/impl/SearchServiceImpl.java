package com.vivid.dream.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.AggregateVariant;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import com.vivid.dream.model.PopularSearchWordVo;
import com.vivid.dream.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchServiceImpl implements SearchService {
    private final RestClient restClient;
    private final ElasticsearchClient esClient;

    @Override
    public String getLastIndexName(String name) throws IOException {
        String indexName = "";
        Request request = new Request("GET", "_cat/indices/"+name+"-*?s=index:desc&h=index,health");
        Response response = restClient.performRequest(request);

        InputStream inputStream = response.getEntity().getContent();
        List<String> indexes = new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .collect(Collectors.toList());

        if (indexes.size() > 1) {
            String[] nowIndex = indexes.get(0).split(" ");
            String[] oldIndex = indexes.get(1).split(" ");
            indexName = "green".equals(nowIndex[1]) || "yellow".equals(nowIndex[1]) ? nowIndex[0] : oldIndex[0];
        }
        return indexName;
    }

    @Override
    public String getSongInfoSearch(String keyword) {
        return "";
    }

    @Override
    public List<PopularSearchWordVo> getPopularSearchWords() throws Exception {
        RangeQuery dateRange = getRangeQuery("received_at", LocalDateTime.now().minusDays(30).toString(), LocalDateTime.now().toString());

        SearchRequest searchRequest = new SearchRequest.Builder()
            .index("nginx-*")
            .query(q -> q
                .range(dateRange))
            .size(0)
            .aggregations("params_searchWord", a -> a
                .terms(
                    t -> t.field("params.searchWord.keyword")
                    .size(10)
                ))
            .build();

        SearchResponse<HashMap> searchResponse = esClient.search(searchRequest, HashMap.class);
        AggregateVariant agg = searchResponse.aggregations().get("params_searchWord")._get();
        List<StringTermsBucket> buckets = ((StringTermsAggregate) agg).buckets().array();

        List<PopularSearchWordVo> populars = new ArrayList<>();
        for (StringTermsBucket bucket : buckets) {
            populars.add(PopularSearchWordVo.builder()
                .docCount(bucket.docCount())
                .key(bucket.key()).build());
        }
        return populars;
    }

    private RangeQuery getRangeQuery(String fieldRange, String gte, String lte) {
        RangeQuery dateRange = RangeQuery.of(r -> r
                .field(fieldRange)
                .gte(JsonData.of(gte))
                .lte(JsonData.of(lte))
        );
        return dateRange;
    }
}