package com.masagal.masaban_server.controllers;

import com.masagal.masaban_server.model.Board;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/{boardId}")
    @Tag(name="Create board")
    public ResponseEntity<String> storeBoard(@RequestBody Board board) {
        this.board = board;
        logger.info("returned a board");
        return ResponseEntity.ok("yep");
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

    @PostMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<String> storeNewCard() {
        return null;
    }

    @GetMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<String> getCard() {
        return null;
    }
}
