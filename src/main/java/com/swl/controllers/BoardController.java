package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> registerBoard(@RequestBody BoardRequest boardRequest) {

        try {
            var response = service.verifyBoard(boardRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Board board = service.registerBoard(boardRequest);

            return !Objects.isNull(board) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, board)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idBoard}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> editBoard(@PathVariable("idBoard") Integer idBoard, @RequestBody BoardRequest boardRequest) {

        try {
            var response = service.verifyBoard(boardRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Board editBoard = service.editBoard(idBoard, boardRequest);

            return !Objects.isNull(editBoard) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editBoard)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Project.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idBoard}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getBoard(@PathVariable("idBoard") Integer idBoard) {

        try {
            Board board = service.getBoard(idBoard);

            if (!Objects.isNull(board)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, board));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idBoard}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> deleteBoard(@PathVariable("idBoard") Integer idBoard) {

        try {
            boolean deleteBoard = service.deleteBoard(idBoard);

            return deleteBoard ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Board.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Board.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idProject}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllBoards(@PathVariable(value = "idProject") Integer idProject) {

        try {
            List<Board> boards= service.getAllBoardByProject(idProject);

            return !Objects.isNull(boards) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, boards)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class, e.getMessage()));
        }
    }

}
