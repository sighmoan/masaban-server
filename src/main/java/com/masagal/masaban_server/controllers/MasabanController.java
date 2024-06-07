package com.masagal.masaban_server.controllers;

import com.masagal.masaban_server.model.Board;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1")
public class MasabanController {

    Board board;

    public MasabanController(Board board) {
        this.board = board;
    }

    @PostMapping("/board/")
    public ResponseEntity<String> storeBoard(@RequestBody Board board) {
        this.board = board;
        return ResponseEntity.ok("yep");
    }

    @GetMapping("/board/")
    public ResponseEntity<Board> getBoard() {
        return new ResponseEntity<>(board, HttpStatus.OK);
    }
}
