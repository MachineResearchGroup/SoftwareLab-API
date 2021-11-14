package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.Project;
import com.swl.payload.request.BoardRequest;
import com.swl.payload.request.ColumnRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.BoardService;
import com.swl.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project/board/column")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerColumn(@RequestBody ColumnRequest columnRequest) {

        try {
            var response = service.verifyColumn(columnRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Columns column = service.registerColumn(columnRequest);

            return !Objects.isNull(column) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, column)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editColumn(@PathVariable("idColumn") Integer idColumn, @RequestBody ColumnRequest columnRequest) {

        try {
            var response = service.verifyColumn(columnRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Columns editColumn = service.editColumn(idColumn, columnRequest);

            return !Objects.isNull(editColumn) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editColumn)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Board.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getColumn(@PathVariable("idColumn") Integer idColumn) {

        try {
            Columns column = service.getColumn(idColumn);

            if (!Objects.isNull(column)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, column));
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
    @DeleteMapping("/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteColumn(@PathVariable("idColumn") Integer idColumn) {

        try {
            boolean deleteColumn = service.deleteColumn(idColumn);

            return deleteColumn ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Columns.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Columns.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idBoard}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllColumns(@PathVariable(value = "idBoard") Integer idBoard) {

        try {
            List<Columns> columns= service.getAllColumnsByBoard(idBoard);

            return !Objects.isNull(columns) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, columns)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Board.class, e.getMessage()));
        }
    }

}
