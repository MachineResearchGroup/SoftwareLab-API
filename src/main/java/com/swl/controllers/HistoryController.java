package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.History;
import com.swl.payload.request.HistoryRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerHistory(@RequestBody HistoryRequest historyRequest) {

        History history = service.registerHistory(historyRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, history));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editHistory(@PathVariable("idHistory") Integer idHistory, @RequestBody HistoryRequest historyRequest) {

        History editHistory = service.editHistory(idHistory, historyRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editHistory));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getHistory(@PathVariable("idHistory") Integer idHistory) {

        History history = service.getHistory(idHistory);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, history));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllHistories(@RequestParam(value = "idColumn", required = false) Integer idColumn,
                                             @RequestParam(value = "idCollaborator", required = false) Integer idCollaborator) {


        List<History> histories;

        if (!Objects.isNull(idColumn)) {
            histories = service.getAllHistoryByColumn(idColumn);

        } else if (!Objects.isNull(idCollaborator)) {
            histories = service.getAllHistoriesByCollaborator(idCollaborator);

        } else {
            histories = service.getAllHistoriesByCollaboratorActual();
        }

        if (histories.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, History.class, histories));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, histories));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteHistory(@PathVariable("idHistory") Integer idHistory) {

        service.deleteHistory(idHistory);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, History.class));

    }

}
