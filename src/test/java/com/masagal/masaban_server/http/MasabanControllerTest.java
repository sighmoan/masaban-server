package com.masagal.masaban_server.http;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.domain.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MasabanController.class)
class MasabanControllerTest {

    @MockBean
    Board mockBoard;
    @MockBean
    BoardService boardService;
    @Autowired
    private MockMvc mockMvc;
    UUID useBoardUuid = UUID.randomUUID();
    UUID useCardUuid = UUID.randomUUID();

    @BeforeEach
    void setup() {

        when(mockBoard.getId())
                .thenReturn(useBoardUuid);
        when(boardService.createBoard())
                .thenReturn(useBoardUuid);
        when(boardService.createCardOnBoard(ArgumentMatchers.any(UUID.class)))
                .thenReturn(useCardUuid);
    }

    @Test
    void shouldRespondOk() throws Exception {
        // Arrange
        // Act
        mockMvc.perform(get("/api/v1/board/"+ useBoardUuid))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRespond400ForBadUuids() throws Exception {
        mockMvc.perform(get("/api/v1/board/1337"))
                .andExpect(status().isBadRequest());
        mockMvc.perform(get("/api/v1/board/1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRespond404ForUnknownUuids() throws Exception {
        when(boardService.getBoard(ArgumentMatchers.any(UUID.class)))
                .thenThrow(new NoSuchElementException("hello"));
        mockMvc.perform(get("/api/v1/board/"+UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldCreateBoard() throws Exception {
        mockMvc.perform(post("/api/v1/board"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
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