package com.masagal.masaban_server.controllers;

import com.jayway.jsonpath.JsonPath;
import com.masagal.masaban_server.model.Board;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MasabanController.class)
class MasabanControllerTest {

    @MockBean
    Board mockBoard;
    @Autowired
    private MockMvc mockMvc;
    RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        restTemplate = new RestTemplate();
    }

    @Test
    void shouldRespondOk() throws Exception {
        // Arrange
        // Act
        mockMvc.perform(get("/api/v1/board/"))
                .andExpect(status().isOk());
    }

    @Test
    void canRetrieveStoredCard() throws Exception {
        //Arrange
        mockMvc.perform(post("/api/v1/board/")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cards\":[{\"contents\":\"test\", \"id\": \"1\"}]}"));
        //Act & Asset
        mockMvc.perform(get("/api/v1/board/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cards", hasSize(1)))
                .andExpect(jsonPath("$.cards[0].contents", is("test")))
                .andExpect(jsonPath("$.cards[0].contents", not("not test")));
    }

    @Test
    void shouldReturnBoardOnRequest() throws Exception {
    }

    @Test
    void shouldStoreBoardOnRequest() {

    }

}