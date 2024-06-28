package com.masagal.masaban_server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.masagal.masaban_server.http.ColumnUpdateRequestDto;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.JsonPathResultMatchers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;
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
        return createColumn(boardLocation, 1, "To-do");
    }

    String createColumn(String boardLocation, Integer index, String text) throws Exception {
        ColumnUpdateRequestDto dto = new ColumnUpdateRequestDto(text, index);
        ObjectMapper om = new ObjectMapper();
        return mockMvc.perform(
                    post(boardLocation+"/columns")
                            .contentType("application/json")
                            .content(om.writeValueAsString(dto)))
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
            createColumn(boardLocation, 1, "Idea bucket");
            createColumn(boardLocation, 2, "Doing");
            createColumn(boardLocation, 3, "Done");


            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$.length()", is(3)));
        }

        @Test
        void canInsertColumn() throws Exception {
            String boardLocation = setUpBoard();
            createColumn(boardLocation, 1,"Insistence");
            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.[0].label", containsString("Insistence")));

        }

        @Test
        void canRenameColumn() throws Exception {
            String columnLocation = createColumn(createBoard());

            ColumnUpdateRequestDto dto = new ColumnUpdateRequestDto("Variation", -1);
            ObjectMapper om = new ObjectMapper();

            mockMvc.perform(put(columnLocation)
                            .contentType("application/json")
                            .content(om.writeValueAsString(dto)))
                    .andExpect(status().isOk());
            mockMvc.perform(get(columnLocation))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers
                            .jsonPath("$.label", containsString("Variation")));
        }

        @Test
        void canDeleteColumn() throws Exception {
            // Arrange
            String boardLocation = createBoard();
            String columnLocation = createColumn(boardLocation, 1, "Expectation");
            // Act
            mockMvc.perform(delete(columnLocation));
            // Assert
            mockMvc.perform(get(columnLocation))
                    .andExpect(status().isNotFound());
            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk());

        }

        @Test
        void canMoveColumn() throws Exception {
            // Arrange
            String boardLocation = createBoard();
            createColumn(boardLocation, 0, "To do");
            createColumn(boardLocation, 1, "Doing");
            String columnLocation = createColumn(boardLocation, 2, "Idea box");
            // Act
            ColumnUpdateRequestDto dto = new ColumnUpdateRequestDto("Idea box", -1);
            ObjectMapper om = new ObjectMapper();
            mockMvc.perform(put(columnLocation)
                    .content(om.writeValueAsString(dto))
                    .contentType("application/json"))
                    .andExpect(status().isOk());
            // Assert
            mockMvc.perform(get(boardLocation + "/columns"))
                    .andExpect(status().isOk())
                    .andExpect(MockMvcResultMatchers.jsonPath("$[0].label", containsString("Idea box")));
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
        void should400IfColumnIndexInvalid() throws Exception {
            String boardLocation = setUpBoard();
            mockMvc.perform(post(boardLocation + "/column/403")
                            .content("New column name"))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(put(boardLocation + "/column/403")
                            .content("New column name"))
                    .andExpect(status().isBadRequest());
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
