package com.masagal.masaban_server.controllers;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.Card;
import com.masagal.masaban_server.services.BoardService;
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
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/board")
@Tag(name="Main Board interface", description="Primary interface for CRUD'ing masaban boards.")
public class MasabanController {

    Logger logger = LogManager.getLogger();
    BoardService service;

    public MasabanController(BoardService service) {
        this.service = service;
    }

    @PostMapping
    @Tag(name="Create board")
    public ResponseEntity<String> storeBoard() throws URISyntaxException {
        Board board = new Board();
        logger.info("created a board");
        service.addBoard(board);
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
    public ResponseEntity<Board> getBoard(@PathVariable UUID boardId) {
        Board board = service.getBoard(boardId);
        logger.info("get board no. {}", boardId);
        if(board.getId() != boardId) {
            logger.warn("404: the currently active board is {}, not {}", board.getId(), boardId);
            return ResponseEntity.notFound().build();
        }
        return new ResponseEntity<>(board, HttpStatus.OK);
    }

    @PostMapping("/{boardId}/card")
    public ResponseEntity<Card> storeNewCard(@PathVariable UUID boardId) {
        Board board = service.getBoard(boardId);
        Card resultingCard = board.createCard("");
        URI location = URI.create("/api/v1/board/"+board.getId()+"/card/"+resultingCard.id());
        return ResponseEntity.created(location)
                .body(resultingCard);
    }

    @GetMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<String> getCard() {
        return null;
    }

    @PostMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable UUID boardId, @RequestBody Card requestedCard) {
        Board board = service.getBoard(boardId);
        Card resultingCard = board.updateCard(requestedCard.id(), requestedCard.text());
        return ResponseEntity.ok(resultingCard);
    }

    @DeleteMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<Card> deleteCard(@PathVariable UUID boardId, @PathVariable String cardId) {
        Board board = service.getBoard(boardId);
        board.deleteCard(Integer.parseInt(cardId));
        return ResponseEntity.ok().build();
    }
}
