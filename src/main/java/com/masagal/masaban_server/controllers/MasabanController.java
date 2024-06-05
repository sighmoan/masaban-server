package com.masagal.masaban_server.controllers;

import com.masagal.masaban_server.model.Board;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
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
    public Board getBoard() {
        return board;
    }
}
