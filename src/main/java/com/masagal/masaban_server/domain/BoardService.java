package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.Card;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
public class BoardService {
    HashMap<UUID, Board> boards = new HashMap<>();

    public UUID createBoard() {
        Board board = new Board();
        boards.put(board.getId(), board);
        return board.getId();
    }

    public Board addBoard(Board board) {
        return board;
    }

    public Board getBoard(UUID id) {
        return boards.get(id);
    }

    public UUID createCardOnBoard(UUID boardId) {
        Board board = getBoard(boardId);
        Card newCard = board.createCard("");
        return newCard.id();
    }

    public Card getCardOnBoard(UUID boardId, UUID cardId) {
        return getBoard(boardId).getCardById(cardId);
    }

    public void updateCardOnBoard(UUID boardId, UUID cardId, Card updatedCard) {
        getBoard(boardId).deleteCard(cardId);
        getBoard(boardId).add(updatedCard);
    }

    public void deleteCardOnBoard(UUID boardId, UUID cardId) {
        getBoard(boardId).deleteCard(cardId);
    }
}
