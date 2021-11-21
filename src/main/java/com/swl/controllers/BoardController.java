package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Board;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_PO"})
    public ResponseEntity<?> registerBoard(@RequestBody BoardRequest boardRequest) {

        Board board = service.registerBoard(boardRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, board));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idBoard}")
    @Secured({"ROLE_PO"})
    public ResponseEntity<?> editBoard(@PathVariable("idBoard") Integer idBoard, @RequestBody BoardRequest boardRequest) {

        Board editBoard = service.editBoard(idBoard, boardRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editBoard));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idBoard}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getBoard(@PathVariable("idBoard") Integer idBoard) {

        Board board = service.getBoard(idBoard);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, board));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idBoard}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> deleteBoard(@PathVariable("idBoard") Integer idBoard) {

        service.deleteBoard(idBoard);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Board.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idProject}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllBoardsByProject(@PathVariable(value = "idProject") Integer idProject) {

        List<Board> boards = service.getAllBoardByProject(idProject);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, boards));

    }

}
