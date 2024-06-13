package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.Card;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BoardService {
    Logger logger = LogManager.getLogger();
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
        logger.debug("fetching board with id {}", id);
        Board foundBoard = boards.get(id);
        if(foundBoard == null) {
            logger.warn("board with id {} could not be found", id);
            throw new NoSuchElementException("could not find board with id "+id);
        }
        logger.warn("board with id {} was found: {}", id, foundBoard.toString());
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

    public String[] getColumns(UUID boardId) {
        return getBoard(boardId).getColumnLabels();
    }
}
