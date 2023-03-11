package com.vivid.dream.config.elastcisearch;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.RestHighLevelClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class VividElastcisearch {
    @Value("${spring.elasticsearch.uris}")
    private String uri;

    @Value("${spring.elasticsearch.port}")
    private int port;

    @Value("${spring.elasticsearch.username}")
    private String username;

    @Value("${spring.elasticsearch.password}")
    private String password;

    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient httpClient = RestClient.builder(
                new HttpHost(uri, port)
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                httpClient,
                new JacksonJsonpMapper()
        );

        return new ElasticsearchClient(transport);
    }
}