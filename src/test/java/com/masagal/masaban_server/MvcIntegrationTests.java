package com.masagal.masaban_server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MvcIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

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
    void canCreateNewCard() throws Exception {
        //Arrange
        MvcResult result = mockMvc.perform(post("/api/v1/board")).andReturn();
        String location = result.getResponse().getHeader("location");
        //Act
        //Assert
        mockMvc.perform(post(location + "/card"))
                .andExpect(status().isCreated())
                .andExpect(header().exists("location"));
    }

    @Test
    void canUpdateContentOfCard() throws Exception {
        //Arrange
        MvcResult result = mockMvc.perform(post("/api/v1/board"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String boardLocation = result.getResponse().getHeader("location");
        result = mockMvc.perform(post(boardLocation + "/card"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        String cardLocation = result.getResponse().getHeader("location");
        String cardId = cardLocation.substring(cardLocation.lastIndexOf("/card/")+6);
        //Act
        String updatedCard = "{\"id\": "+cardId+", \"contents\":\"This is the contents of a card.\"}";
        mockMvc.perform(post(cardLocation).content(updatedCard))
        //Assert
                .andExpect(status().isOk());
    }
}
