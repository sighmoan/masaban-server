package com.masagal.masaban_server.controllers;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.Card;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.coyote.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/board")
@Tag(name="Main Board interface", description="Primary interface for CRUD'ing masaban boards.")
public class MasabanController {

    Logger logger = LogManager.getLogger();
    Board board;

    public MasabanController(Board board/*, Logger logger*/) {
        this.board = board;
        //this.logger = logger;
    }

    @PostMapping
    @Tag(name="Create board")
    public ResponseEntity<String> storeBoard() throws URISyntaxException {
        this.board = new Board();
        logger.info("created a board");
        URI location = null;
        try {
            location = new URI("/api/v1/board/"+board.getId());
        } catch(URISyntaxException ex) {
            logger.error("Failed to generate URI for location header.");
            throw new URISyntaxException("api/v1/board/"+board.getId(), "Failed to generate location URI.");
        }
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{boardId}")
    @Tag(name="Get currently active board.", description="The ID must match with the currently active board.")
    @Parameter(name="boardId", description="the ID of the currently active board")
    public ResponseEntity<Board> getBoard(@PathVariable int boardId) {
        logger.info("get board no. {}", boardId);
        if(board.getId() != boardId) {
            logger.warn("404: the currently active board is {}, not {}", board.getId(), boardId);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/card")
    public ResponseEntity<Card> storeNewCard() {
        Card resultingCard = board.createCard("");
        URI location = URI.create("/api/v1/board/"+board.getId()+"/card/"+resultingCard.id());
        return ResponseEntity.created(location)
                .body(resultingCard);
    }

    @GetMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<String> getCard() {
        return null;
    }
}
