package com.masagal.masaban_server.domain;

import com.masagal.masaban_server.model.*;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;

import javax.management.InstanceNotFoundException;
import java.util.*;

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

    @Transactional
    public UUID createCardOnBoard(UUID boardId) {
        Board board = boardRepo.findById(boardId).orElseThrow();
        Column col = board.getColumns().getFirst();
        return createCardOnColumn(boardId, col.getId());
    }

    @Transactional
    public UUID createCardOnColumn(UUID boardId, UUID columnId) {
        Card newCard = new Card("");
        Column col = columnRepo.findById(columnId).orElseThrow();
        cardRepo.save(newCard);

        col.add(newCard);
        columnRepo.save(col);
        return newCard.getId();
    }

    public List<Card> getCardsByColumn(UUID columnId) {
        return columnRepo.findById(columnId).orElseThrow(NoSuchElementException::new).getCards();
    }

    public Card getCardOnBoard(UUID boardId, UUID cardId) {
        return cardRepo.findById(cardId).orElseThrow();
    }

    public void updateCardOnBoard(UUID boardId, UUID cardId, Card updatedCard) {
        Card card = cardRepo.findById(cardId).orElseThrow(NoSuchElementException::new);
        card.setText(updatedCard.getText());
        cardRepo.save(card);
    }

    public void deleteCardOnBoard(UUID boardId, UUID cardId) {
        getBoard(boardId).deleteCard(cardId);
    }

    public List<Column> getColumns(UUID boardId) {
        return getBoard(boardId).getColumns().stream().sorted(Comparator.comparingInt(Column::getIndex)).toList();
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
        logger.info("deleting column {} on board {}", columnId, boardId);

        Board board =boardRepo.findById(boardId).orElseThrow();
        board.removeColumn(columnRepo.findById(columnId).orElseThrow());
        boardRepo.save(board);

        columnRepo.deleteById(columnId);
        logger.info("delete {} complete", columnId);
    }

    @Transactional
    public void moveColumn(UUID boardId, UUID columnId, Integer index) {
        Column col = columnRepo.findById(columnId).orElseThrow();
        if(index == null) {
            logger.warn("new column index set to null. Setting unchanged");
            index = col.getIndex();
        }
        col.setIndex(index);
        columnRepo.save(col);
        // ?????
        Board board = boardRepo.findById(boardId).orElseThrow();
        boardRepo.save(board);
    }

    public Column getColumnById(UUID columnId) {
        return columnRepo.findById(columnId).orElseThrow();
    }

    public void moveCard(UUID cardId, UUID newColumnId) {
        Card cardToMove = cardRepo.findById(cardId).orElseThrow(NoSuchElementException::new);

        columnRepo.findByCardsContaining(List.of(cardToMove)).removeCard(cardToMove);
        columnRepo.findById(newColumnId).orElseThrow().add(cardToMove);
    }
}
