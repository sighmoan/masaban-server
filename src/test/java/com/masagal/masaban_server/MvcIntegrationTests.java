package com.masagal.masaban_server;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MvcIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    private String setUpBoard() throws Exception {
        MvcResult result = mockMvc.perform(post("/api/v1/board"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        return result.getResponse().getHeader("location");
    }

    String createBoard() throws Exception {
        return mockMvc.perform(post("/api/v1/board"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn()
                .getResponse()
                .getHeader("location");
    }

    String createColumn(String boardLocation) throws Exception {
        return mockMvc.perform(post(boardLocation+"/column"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn()
                .getResponse()
                .getHeader("location");
    }

    @Test
    void canCreateNewBoard() throws Exception {
        //Arrange
        //Act
        //Assert
        mockMvc.perform(post("/api/v1/board"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"))
                .andReturn();

    }

    @Test
    void canCreateNewColumn() throws Exception {
        String boardLocation = createBoard();

        createColumn(boardLocation);
    }

    @Nested
    class ColumnHandling {

        @Test
        void canGetColumns() throws Exception {
            //Arrange
            String boardLocation = setUpBoard();
            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("length()", is(3)));
        }

        @Test
        void canInsertColumn() throws Exception {
            String boardLocation = setUpBoard();
            mockMvc.perform(post(boardLocation + "/column/1")
                            .content("New column name"))
                    .andExpect(status().isCreated());
            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.[1]", containsString("New column name")));

        }

        @Test
        void canRenameColumn() throws Exception {
            String boardLocation = setUpBoard();
            mockMvc.perform(put(boardLocation + "/column/1")
                            .content("New column name"))
                    .andExpect(status().isOk());
            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.[1]", containsString("New column name")));
        }
    }

    @Nested
    class CardHandling {
        @Test
        void canCreateNewCard() throws Exception {
            //Arrange
            MvcResult result = mockMvc.perform(post("/api/v1/board")).andReturn();
            String location = result.getResponse().getHeader("location");
            String columnLocation = createColumn(location);
            //Act
            //Assert
            mockMvc.perform(post(columnLocation + "/card"))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("location"));
        }

        @Test
        void canUpdateContentOfCard() throws Exception {
            //Arrange
            String columnLocation = createColumn(createBoard());
            MvcResult result = mockMvc.perform(post(columnLocation + "/card"))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            String cardLocation = result.getResponse().getHeader("location");
            String cardId = cardLocation.substring(cardLocation.lastIndexOf("/card/") + 6);
            //Act
            String updatedCard = "{\"id\": \"" + cardId + "\", \"contents\":\"This is the contents of a card.\"}";
            mockMvc.perform(post(cardLocation).contentType(MediaType.APPLICATION_JSON)
                            .content(updatedCard))
                    //Assert
                    .andExpect(status().isOk());
        }

        @Test
        void canMoveCardToAnotherColumn() throws Exception {
            fail();
        }

        @Test
        void canDeleteCard() throws Exception {
            //Arrange
            String boardLocation = setUpBoard();
            MvcResult result = mockMvc.perform(post(boardLocation + "/card"))
                    .andExpect(status().is2xxSuccessful())
                    .andReturn();
            String cardLocation = result.getResponse().getHeader("location");
            //Act
            mockMvc.perform(delete(cardLocation))
                    //Assert
                    .andExpect(status().is2xxSuccessful());

        }
    }

    @Nested
    class ExceptionHandling {
        @Test
        void shouldErrorCardsNotFound() throws Exception {
            fail();
        }

        @Test
        void shouldErrorUpdatedCardContentsEmpty() throws Exception {
            fail();
        }

        @Test
        void shouldErrorUpdatedCardContentsUnchanged() throws Exception {
            fail();
        }

        @Test
        void should404IfColumnIndexInvalid() throws Exception {
            String boardLocation = setUpBoard();
            mockMvc.perform(post(boardLocation + "/column/403")
                            .content("New column name"))
                    .andExpect(status().isNotFound());
            mockMvc.perform(put(boardLocation + "/column/403")
                            .content("New column name"))
                    .andExpect(status().isNotFound());
        }

        @Test
        void should400IfColumnContentMissing() throws Exception {
            String boardLocation = setUpBoard();
            mockMvc.perform(post(boardLocation + "/column/1")
                            .content(""))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(put(boardLocation + "/column/1")
                            .content(""))
                    .andExpect(status().isBadRequest());

        }
    }
}
