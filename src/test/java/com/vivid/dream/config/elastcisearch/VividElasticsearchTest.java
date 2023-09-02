package com.vivid.dream.config.elastcisearch;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VividElasticsearchTest {
//
//    @Value("${spring.elasticsearch.host}")
//    private String elasticHost;
//
//    @Value("${spring.elasticsearch.port}")
//    private int elasticPort;
//
//    @Value("${spring.elasticsearch.username}")
//    private String elasticUsername;
//
//    @Value("${spring.elasticsearch.password}")
//    private String elasticPassword;
//
//    RestClient restClient = null;
//
//    @Test
//    @Order(1)
//    void connect_elasticsearch_rest_client(){
//        connectElasticsearch();
//    }
//
//    @Test
//    @Order(2)
//    @DisplayName("elastic 연결 확인")
//    void connect_elastic_rest_client(){
//        Assertions.assertNotNull(restClient);
//    }
//
//    @Test
//    @Order(3)
//    @DisplayName("application-test.yaml Properties 값 확인")
//    void get_properties(){
//        Assertions.assertEquals("127.0.0.1", elasticHost);
//    }
//
//    @Test
//    @Order(4)
//    void get_recent_index() {
//        try {
//            Request request = new Request("GET", "_cat/indices/song_info-*?v&s=index:desc");
//            Response response = restClient.performRequest(request);
//            System.out.println(response);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    private void connectElasticsearch() {
//        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
//        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticUsername, elasticPassword));
//
//        HttpHost httpHost = new HttpHost(elasticHost, elasticPort, "https");
//
//        RestClientBuilder restBuilder = RestClient.builder(httpHost).setHttpClientConfigCallback(
//                httpClientBuilder -> httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider));
//
//        restBuilder.setRequestConfigCallback(
//                requestConfigBuilder -> requestConfigBuilder.setSocketTimeout(10000));
//
//        restClient = restBuilder.build();
//    }
}