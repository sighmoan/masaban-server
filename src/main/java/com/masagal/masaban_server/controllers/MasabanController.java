package com.masagal.masaban_server.controllers;

import com.masagal.masaban_server.model.Board;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class MasabanController {

    Logger logger = LogManager.getLogger();
    Board board;

    public MasabanController(Board board/*, Logger logger*/) {
        this.board = board;
        //this.logger = logger;
    }

    @PostMapping("/board/")
    public ResponseEntity<String> storeBoard(@RequestBody Board board) {
        this.board = board;
        logger.info("returned a board");
        return ResponseEntity.ok("yep");
    }

    @GetMapping("/board/")
    public ResponseEntity<Board> getBoard() {
        logger.info("received a request for a board");
        return new ResponseEntity<>(board, HttpStatus.OK);
    }
}
