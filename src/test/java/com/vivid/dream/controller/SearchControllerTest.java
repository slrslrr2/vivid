package com.vivid.dream.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivid.dream.vo.ResultList;
import com.vivid.dream.vo.response.ResponsePopularSearchWord;
import com.vivid.dream.vo.response.ResponseSongInfoSearch;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SearchControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Value("${spring.logstash.host}")
    private String logstashHost;

    @Value("${spring.logstash.end-point-port}")
    private int logstashEndPointPort;

    @Test
    @DisplayName("가수 + 노래제목 + 가사 통합검색 기능 Controller 테스트")
    void get_song_info() {
        try {
            // given
            String searchWord = "아이브 문을 열어";
            String url = "/search/song/info/" + searchWord;

            // when
            ResultActions resultActions = mockMvc.perform(get(url)
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .characterEncoding("UTF-8"));

            // then
            MvcResult mvcResult = resultActions.andExpect(status().isOk())
                    .andReturn();
            String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            ResultList<List<ResponseSongInfoSearch>> result = new ObjectMapper().readValue(responseBody, new TypeReference<>() {});
            List<ResponseSongInfoSearch> list = result.getData();
            for (ResponseSongInfoSearch search : list) {
                log.info(search.toString());
            }

            log.info(result.getTotalCount().toString());

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("[/search/song/info/searchWord]를 검색하면서 등록된 인기검색어 조회")
    void get_popular_search_words() {
        try {
            // given
            int beforeDays = 50;
            int limit = 5;
            ResultActions resultActions = getPopularSearchWordSendRequest(beforeDays, limit);

            // then
            MvcResult mvcResult = resultActions.andExpect(status().isOk())
                    .andReturn();
            String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
            ResultList<List<ResponsePopularSearchWord>> result = new ObjectMapper().readValue(responseBody, new TypeReference<>() {});

            assertTrue(result.getTotalCount() >= limit, () -> "limit는 인기검색어 개수를 가지고오므로 전체카운트는 limit 보다 작거나같아야한다");
            List<ResponsePopularSearchWord> list = result.getData();
            for (int i=0; i<list.size()-1; i++) {
                Long frontDocCount = list.get(i).getDocCount();
                Long backDocCount = list.get(i+1).getDocCount();
                assertTrue(frontDocCount >= backDocCount, () -> "먼저 나온 인기검색어는 뒤에나오는 인기검색어보다 검색 개수가 더 높거나 같아야한다.");
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("[/search/song/info/searchWord] 관련 인기검색어 데이터 잘 쌓이는지 확인")
    void confirm_popular_log() {
        ResponsePopularSearchWord beforeRank1 = new ResponsePopularSearchWord();
        try {
            Socket socket = new Socket();
            InetSocketAddress socketAddress = new InetSocketAddress(logstashHost, logstashEndPointPort);
            socket.connect(socketAddress, 1000); // 1초 동안 접속을 시도
            assertTrue(socket.isConnected(), () -> "STEP 1. logstash 실행 상태 확인");

            beforeRank1 = getPopularSearchWordByTop1(50, 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        String searchKeyword = beforeRank1.getKey() + "_New";
        String url = "http://vivid.co/api/search/song/info/" + searchKeyword;
        HttpURLConnection connection = null;
        try {
            URL requestUrl = new URL(url);
            connection = (HttpURLConnection) requestUrl.openConnection();
            connection.setRequestMethod("GET");

            for (int i=0; i<beforeRank1.getDocCount()+1; i++) {
                connection.getResponseCode(); // 기존 1위 인기검색어 보다 더 많이 Search 하기
            }

            ResponsePopularSearchWord afterRank1 = getPopularSearchWordByTop1(1, 1);
            ResponsePopularSearchWord finalBeforeRank = beforeRank1;
            assertAll(
                () -> assertFalse(searchKeyword.equals(afterRank1.getKey()), () -> "인기검색어와 신규 인기검색어의 값은 다르다"),
                () -> assertTrue(finalBeforeRank.getDocCount() < afterRank1.getDocCount(), () -> "신규인기검색어의 Search Count가 더 많아야한다")
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ResponsePopularSearchWord getPopularSearchWordByTop1(int beforeDays, int limit) throws Exception {
        ResultActions resultActions = getPopularSearchWordSendRequest(beforeDays, limit);
        MvcResult mvcResult = resultActions.andExpect(status().isOk()).andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        ResultList<List<ResponsePopularSearchWord>> result = new ObjectMapper().readValue(responseBody, new TypeReference<>() {});

        return result.getData().get(0);
    }

    private ResultActions getPopularSearchWordSendRequest(int beforeDays, int limit) throws Exception {
        String url = "/search/popular/search/word/list";

        // when
        ResultActions resultActions = mockMvc.perform(get(url)
                .param("beforeDays", String.valueOf(beforeDays))
                .param("limit", String.valueOf(limit))
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("UTF-8"));
        return resultActions;
    }

}