package com.masagal.masaban_server.services;

import com.masagal.masaban_server.model.Board;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class BoardService {
    HashMap<UUID, Board> boards = new HashMap<>();

    public Board addBoard(Board board) {
        boards.put(board.getId(), board);
        return board;
    }

    public Board getBoard(UUID id) {
        return boards.get(id);
    }
}
