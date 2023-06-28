package com.vivid.dream.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SongControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("@ControllerAdvice 적용여부 확인")
    public void controller_advice_pass_test() throws Exception {
        mockMvc.perform(get("/test/list").param("date", "2022-04-03"))
                .andExpect(status().isInternalServerError());
    }
}