package com.masagal.masaban_server.http;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.domain.BoardService;
import com.masagal.masaban_server.model.Card;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.NoSuchElementException;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MasabanController.class)
class MasabanControllerTest {

    Card mockCard = mock(Card.class);
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

        when(mockCard.id())
                .thenReturn(UUID.randomUUID());
        when(mockCard.text())
                .thenReturn("card contents");
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
    void shouldCreateCard() throws Exception{
        String boardLocation = mockMvc.perform(post("/api/v1/board"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn()
                .getResponse()
                .getHeader("location");

        mockMvc.perform(post(boardLocation + "/card"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void shouldGetCard() throws Exception {

        when(boardService.getCardOnBoard(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(UUID.class))).thenReturn(mockCard);

        String boardLocation = mockMvc.perform(post("/api/v1/board"))
                .andReturn()
                .getResponse()
                .getHeader("location");

        String cardLocation = mockMvc.perform(post(boardLocation+"/card"))
                .andReturn()
                .getResponse()
                .getHeader("location");

        mockMvc.perform(get(cardLocation))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(not(emptyString())));
    }

    @Test
    void shouldDeleteCard() throws Exception {
        // Arrange
        // Act
        // Assert
        when(boardService.getCardOnBoard(ArgumentMatchers.any(UUID.class), ArgumentMatchers.any(UUID.class))).thenReturn(mockCard);

        String boardLocation = mockMvc.perform(post("/api/v1/board"))
                .andReturn()
                .getResponse()
                .getHeader("location");

        String cardLocation = mockMvc.perform(post(boardLocation+"/card"))
                .andReturn()
                .getResponse()
                .getHeader("location");

        mockMvc.perform(delete(cardLocation))
                .andExpect(status().isOk());
    }

}