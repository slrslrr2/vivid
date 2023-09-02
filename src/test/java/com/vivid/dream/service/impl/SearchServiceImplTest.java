package com.vivid.dream.service.impl;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchServiceImplTest {
//    @Autowired private RestClient restClient;
//    @Autowired private SearchService searchService;
//
//    @Test
//    @DisplayName("Elasticsearch Index 존재 및 status 조회")
//    void exist_elasticsearch_index(){
//        try {
//            String searchKeyword = "song_info";
//            Request request = new Request("GET", "_cat/indices/"+ searchKeyword +"-*?s=index:desc&h=index,health");
//            Response response = restClient.performRequest(request);
//
//            InputStream inputStream = response.getEntity().getContent();
//            List<String> indexes = new BufferedReader(new InputStreamReader(inputStream))
//                    .lines()
//                    .collect(Collectors.toList());
//
//            String[] nowIndex = indexes.get(0).split(" ");
//            String findIndexName = nowIndex[0];
//            assertAll(
//                    () -> assertTrue(indexes.size() > 1, () -> searchKeyword + " 관련 INDEX가 존재하지않습니다"),
//                    () -> assertNotNull(findIndexName, () -> searchKeyword + " 관련 INDEX가 존재하지않습니다"),
//                    () -> assertThat(findIndexName, containsString(searchKeyword)),
//                    () -> assertThat(nowIndex[1], anyOf(is("green"), is("yellow")))
//            );
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    void get_last_index_name() {
//        try {
//            String lastIndexName = searchService.getLastIndexName("song_info");
//            assertNotNull(lastIndexName);
//        } catch (Exception e){
//
//        }
//    }
}