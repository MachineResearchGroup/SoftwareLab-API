package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.History;
import com.swl.payload.request.HistoryRequest;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.HistoryService;
import com.swl.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project/board/column/history")
@RequiredArgsConstructor
public class HistoryController {

    private final TaskService taskService;

    private final HistoryService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerHistory(@RequestBody HistoryRequest historyRequest) {

        service.verifyHistory(historyRequest);
        History history = service.registerHistory(historyRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, history));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editHistory(@PathVariable("idHistory") Integer idHistory, @RequestBody HistoryRequest historyRequest) {

        service.verifyHistory(historyRequest);
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
    @GetMapping("/all/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllByColumns(@PathVariable(value = "idColumn") Integer idColumn) {

        List<History> histories = service.getAllHistoryByColumn(idColumn);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, histories));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{idHistory}/addTask")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> addTask(@PathVariable("idHistory") Integer idHistory, @RequestBody TaskRequest taskRequest) {

        taskService.verifyTask(taskRequest);
        History history = service.addTask(idHistory, taskRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, history));

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
