package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.*;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BoardService {
    Logger logger = LogManager.getLogger();
    @Autowired
    BoardRepository repo;
    @Autowired
    ColumnRepository columnRepo;
    @Autowired
    CardRepository cardRepo;
    @Autowired
    private BoardRepository boardRepository;

    public UUID createBoard() {
        Board board = new Board(UUID.randomUUID());
        repo.save(board);
        return board.getId();
    }

    public Board addBoard(Board board) {
        repo.save(board);
        return board;
    }

    public Board getBoard(UUID id) {
        logger.debug("fetching board with id {}", id);
        Board foundBoard = repo.findById(id).orElseThrow(() -> {
            logger.warn("board with id {} could not be found", id);
            return new NoSuchElementException("could not find board with id "+id);
        });
        logger.warn("board with id {} was found: {}", id, foundBoard.toString());
        return foundBoard;
    }

    public UUID createCardOnBoard(UUID boardId) {
        Board board = getBoard(boardId);
        Card newCard = board.createCard("");
        return newCard.getId();
    }

    @Transactional
    public UUID createCardOnColumn(UUID boardId, UUID columnId) {
        Card newCard = new Card("", UUID.randomUUID());
        Column col = columnRepo.findById(columnId).orElseThrow();
        col.add(newCard);
        columnRepo.save(col);
        cardRepo.save(newCard);
        return newCard.getId();
    }

    public Card getCardOnBoard(UUID boardId, UUID cardId) {
        return cardRepo.findById(cardId).orElseThrow();
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

    public void renameColumn(UUID boardId, int columnIndex, String newText) {
        getBoard(boardId).renameColumn(newText, columnIndex);
    }

    public void insertColumn(UUID boardId, int columnIndex, String text) {
        getBoard(boardId).addColumn(text, columnIndex);
    }

    public void deleteColumn(UUID boardId, int columnIndex) {

    }
}
