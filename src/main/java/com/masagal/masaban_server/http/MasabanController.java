package com.masagal.masaban_server.http;

import com.masagal.masaban_server.domain.BoardService;
import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.Card;
import com.masagal.masaban_server.model.Column;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(exposedHeaders = "Location")
@RequestMapping(MasabanController.API_ROOT)
@Tag(name = "Main Board interface", description = "Primary interface for CRUD'ing masaban boards.")
public class MasabanController {

    public static final String API_ROOT = "/api/v1/board";

    Logger logger = LogManager.getLogger();
    BoardService service;

    public MasabanController(BoardService service) {
        this.service = service;
    }

    private URI createRelativeApiUri(String... args) {
        String completeString = API_ROOT + "/" + String.join("/", args);
        try {
            return new URI(completeString);
        } catch (URISyntaxException e) {
            logger.error("URI generation failed.");
            throw new HttpServerErrorException(HttpStatus.I_AM_A_TEAPOT, "URI generation failed");
        }
    }

    @PostMapping
    @Tag(name = "Create board")
    public ResponseEntity<String> storeBoard() throws URISyntaxException {
        UUID id = service.createBoard();
        logger.info("created a board with id " + id.toString());
        URI location = createRelativeApiUri(id.toString());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{boardId}")
    @Tag(name = "Get board by ID")
    public ResponseEntity<Board> getBoard(@PathVariable @Valid UUID boardId) {
        logger.info("get board no. {}", boardId);

        return ResponseEntity.ok(service.getBoard(boardId));
    }

    @PostMapping("/{boardId}/card")
    public ResponseEntity<Card> storeNewCard(@PathVariable @Valid UUID boardId) {
        UUID cardId = service.createCardOnBoard(boardId);
        URI location = createRelativeApiUri(boardId.toString(), "card", cardId.toString());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<Card> getCard(@PathVariable @Valid UUID boardId, @PathVariable @Valid UUID cardId) {
        return ResponseEntity.ok(service.getCardOnBoard(boardId, cardId));
    }

    @PostMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<Card> updateCard(@PathVariable @Valid UUID boardId, @PathVariable @Valid UUID cardId, @RequestBody CardUpdateRequestDto reqDto) {
        service.updateCardOnBoard(boardId, cardId, reqDto.toCard());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{boardId}/columns/{columnId}/cards/{cardId}")
    public ResponseEntity<Void> updatedCard(@PathVariable @Valid UUID boardId, @PathVariable @Valid UUID columnId, @Valid UUID cardId, @RequestBody CardUpdateRequestDto reqDto) {
        service.updateCardOnBoard(boardId, cardId, reqDto.toCard());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<Card> deleteCard(@PathVariable @Valid UUID boardId, @PathVariable @Valid UUID cardId) {
        service.deleteCardOnBoard(boardId, cardId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}/columns")
    @Tag(name = "Get all columns")
    public ResponseEntity<List<Column>> getColumns(@PathVariable @Valid UUID boardId) {
        return ResponseEntity.ok(service.getColumns(boardId));
    }

    @GetMapping("/{boardId}/columns/{columnId}")
    @Tag(name="Get a specific column")
    public ResponseEntity<Column> getColumn(@PathVariable @Valid UUID boardId,
                                            @PathVariable @Valid UUID columnId) {
        return ResponseEntity.ok(service.getColumnById(columnId));
    }

    @PostMapping("/{boardId}/columns")
    @Tag(name = "Insert a new column")
    public ResponseEntity<Void> writeColumn(@PathVariable @Valid UUID boardId,
                                            @RequestBody ColumnUpdateRequestDto dto) {
        UUID columnId = service.insertColumn(boardId, dto.index(), dto.label());
        URI location = createRelativeApiUri(boardId.toString(), "columns", columnId.toString());
        return ResponseEntity.created(location).build();
    }

    @PutMapping("/{boardId}/columns/{columnId}")
    @Tag(name = "Update the specified column")
    public ResponseEntity<Void> updateColumn(@PathVariable @Valid UUID boardId,
                                             @PathVariable @Valid UUID columnId,
                                             @RequestBody @Valid ColumnUpdateRequestDto dto) {
        service.renameColumn(boardId, columnId, dto.label());
        service.moveColumn(boardId, columnId, dto.index());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}/columns/{columnId}")
    @Tag(name = "Delete the specified column")
    public ResponseEntity<Void> deleteColumn(@PathVariable @Valid UUID boardId,
                                             @PathVariable @Valid UUID columnId) {
        service.deleteColumn(boardId, columnId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}/columns/{columnId}/cards")
    public ResponseEntity<List<Card>> getCardsByColumn(@PathVariable @Valid UUID boardId,
                                                       @PathVariable @Valid UUID columnId) {
        return ResponseEntity.ok(service.getCardsByColumn(columnId));
    }

    @PostMapping("/{boardId}/columns/{columnId}/cards")
    public ResponseEntity<Void> insertCardOnColumn(@PathVariable @Valid UUID boardId,
                                                   @PathVariable @Valid UUID columnId) {
        UUID newCardId = service.createCardOnColumn(boardId, columnId);
        URI location = createRelativeApiUri(boardId.toString(), "columns", columnId.toString(), "cards", newCardId.toString());
        return ResponseEntity.created(location).build();
    }
}
