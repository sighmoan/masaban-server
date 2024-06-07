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
import static org.mockito.Mockito.when;
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

        when(mockBoard.getId()).thenReturn(1);
    }

    @Test
    void shouldRespondOk() throws Exception {
        // Arrange
        // Act
        mockMvc.perform(get("/api/v1/board/1"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespond404forUnknownBoards() throws Exception {
        mockMvc.perform(get("/api/v1/board/1337"))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/api/v1/board/"))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/api/v1/board/abc"))
                .andExpect(status().is4xxClientError());
        mockMvc.perform(get("/api/v1/board/00b4rf"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void canRetrieveStoredCard() throws Exception {
        //Arrange
        mockMvc.perform(post("/api/v1/board/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"cards\":[{\"contents\":\"test\", \"id\": \"1\"}]}"));
        //Act & Asset
        mockMvc.perform(get("/api/v1/board/1"))
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

    @Test
    void shouldStoreCard() {
        // Arrange
        // Act
        // Assert
    }

}