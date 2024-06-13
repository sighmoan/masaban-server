package com.masagal.masaban_server.http;

import com.masagal.masaban_server.model.Board;
import com.masagal.masaban_server.model.Card;
import com.masagal.masaban_server.domain.BoardService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.util.UriBuilder;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping(MasabanController.API_ROOT)
@Tag(name="Main Board interface", description="Primary interface for CRUD'ing masaban boards.")
public class MasabanController {

    public static final String API_ROOT = "/api/v1/board";

    Logger logger = LogManager.getLogger();
    BoardService service;

    public MasabanController(BoardService service) {
        this.service = service;
    }

    private URI createRelativeApiUri(String ... args) {
        String completeString = API_ROOT + "/" + String.join("/", args);
        try {
            return new URI(completeString);
        } catch(URISyntaxException e) {
            logger.error("URI generation failed.");
            throw new HttpServerErrorException(HttpStatus.I_AM_A_TEAPOT, "URI generation failed");
        }
    }

    @PostMapping
    @Tag(name="Create board")
    public ResponseEntity<String> storeBoard() throws URISyntaxException {
        UUID id = service.createBoard();
        logger.info("created a board with id "+id.toString());
        URI location = createRelativeApiUri(id.toString());

        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{boardId}")
    @Tag(name="Get board by ID")
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
        service.updateCardOnBoard(boardId, cardId, reqDto.toCard(cardId));

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{boardId}/card/{cardId}")
    public ResponseEntity<Card> deleteCard(@PathVariable @Valid UUID boardId, @PathVariable @Valid UUID cardId) {
        service.deleteCardOnBoard(boardId, cardId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{boardId}/columns")
    public ResponseEntity<String[]> getColumns(@PathVariable @Valid UUID boardId) {
        return ResponseEntity.ok(service.getColumns(boardId));
    }
}
