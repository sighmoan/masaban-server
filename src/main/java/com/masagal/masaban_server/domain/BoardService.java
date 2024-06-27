package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.*;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BoardService {
    Logger logger = LogManager.getLogger();
    BoardRepository boardRepo;
    ColumnRepository columnRepo;
    CardRepository cardRepo;

    @Autowired
    public BoardService(BoardRepository repo, ColumnRepository columnRepo, CardRepository cardRepo) {
        this.boardRepo = repo;
        this.columnRepo = columnRepo;
        this.cardRepo = cardRepo;
    }

    public UUID createBoard() {
        Board board = new Board();
        Board newBoard = boardRepo.save(board);
        logger.info("created and saved board with id {}", newBoard.getId());
        return newBoard.getId();
    }

    public Board addBoard(Board board) {
        boardRepo.save(board);
        return board;
    }

    public Board getBoard(UUID id) {
        logger.debug("fetching board with id {}", id);
        Board foundBoard = boardRepo.findById(id).orElseThrow(() -> {
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

    public List<Column> getColumns(UUID boardId) {
        return getBoard(boardId).getColumns();
    }

    public void renameColumn(UUID boardId, UUID columnId, String newText) {
        Column col = columnRepo.findById(columnId).orElseThrow();
        col.setLabel(newText);
        columnRepo.save(col);
    }

    public UUID insertColumn(UUID boardId, int columnIndex, String text) {
        logger.info("creating column on board id {} with index {}", boardId, columnIndex);
        Column col = new Column(new ArrayList<>(), text);
        col.setIndex(columnIndex);

        Board board = getBoard(boardId);
        board.addColumn(col);
        columnRepo.save(col);
        boardRepo.save(board);

        return col.getId();
    }

    public void deleteColumn(UUID boardId, UUID columnId) {
        boardRepo.findById(boardId).orElseThrow()
                        .removeColumn(columnRepo.findById(columnId).orElseThrow());
        columnRepo.deleteById(columnId);
    }

    public void moveColumn(UUID boardId, UUID columnId, Integer index) {
    }

    public Column getColumnById(UUID columnId) {
        return columnRepo.findById(columnId).orElseThrow();
    }
}
