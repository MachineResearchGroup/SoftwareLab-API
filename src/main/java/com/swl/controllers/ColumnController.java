package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Columns;
import com.swl.payload.request.ColumnRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.ColumnService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/column")
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerColumn(@RequestBody ColumnRequest columnRequest) {

        Columns column = service.registerColumn(columnRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, column));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editColumn(@PathVariable("idColumn") Integer idColumn, @RequestBody ColumnRequest columnRequest) {

        Columns editColumn = service.editColumn(idColumn, columnRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editColumn));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getColumn(@PathVariable("idColumn") Integer idColumn) {

        Columns column = service.getColumn(idColumn);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, column));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteColumn(@PathVariable("idColumn") Integer idColumn) {

        service.deleteColumn(idColumn);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Columns.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idBoard}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllColumns(@PathVariable(value = "idBoard") Integer idBoard) {

        List<Columns> columns = service.getAllColumnsByBoard(idBoard);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, columns));

    }

}
