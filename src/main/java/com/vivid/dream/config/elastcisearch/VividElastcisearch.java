package com.vivid.dream.config.elastcisearch;

import org.springframework.context.annotation.Configuration;

@Configuration
public class VividElastcisearch {
//    @Value("${spring.elasticsearch.host}")
//    private String host;
//
//    @Value("${spring.elasticsearch.port}")
//    private int port;
//
//    @Value("${spring.elasticsearch.username}")
//    private String username;
//
//    @Value("${spring.elasticsearch.password}")
//    private String password;
//
//    @Bean
//    public RestClient restClient() {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
//
//        HttpHost httpHost = new HttpHost(host, port, "https");
//
//        RestClientBuilder restBuilder = RestClient.builder(httpHost).setHttpClientConfigCallback(
//                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//
//        restBuilder.setRequestConfigCallback(
//                requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(10000));
//
//        return restBuilder.build();
//    }
//
//    @Bean
//    public ElasticsearchClient elasticsearchClient(){
//        RestClient restClient = restClient();
//        ElasticsearchTransport transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//
//        return new ElasticsearchClient(transport);
//    }
}