package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Columns;
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
import java.util.Objects;


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

        try {
            var response = service.verifyHistory(historyRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            History history = service.registerHistory(historyRequest);

            return !Objects.isNull(history) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, history)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{idHistory}/addTask")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> addTask(@PathVariable("idHistory") Integer idHistory, @RequestBody TaskRequest taskRequest) {

        try {
            var response = taskService.verifyTask(taskRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            History history = service.addTask(idHistory, taskRequest);

            return !Objects.isNull(history) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, history)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editHistory(@PathVariable("idHistory") Integer idHistory, @RequestBody HistoryRequest historyRequest) {

        try {
            var response = service.verifyHistory(historyRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            History editHistory = service.editHistory(idHistory, historyRequest);

            return !Objects.isNull(editHistory) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editHistory)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, History.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Columns.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getHistory(@PathVariable("idHistory") Integer idHistory) {

        try {
            History history = service.getHistory(idHistory);

            if (!Objects.isNull(history)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, history));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idHistory}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteHistory(@PathVariable("idHistory") Integer idHistory) {

        try {
            boolean deleteTask = service.deleteHistory(idHistory);

            return deleteTask ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, History.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, History.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, History.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllByColumns(@PathVariable(value = "idColumn") Integer idColumn) {

        try {
            List<History> histories = service.getAllHistoryByColumn(idColumn);

            return !Objects.isNull(histories) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, histories)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class, e.getMessage()));
        }
    }

}
