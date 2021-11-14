package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Columns;
import com.swl.models.project.Task;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.MessageResponse;
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
@RequestMapping("/project/board/column/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerTask(@RequestBody TaskRequest taskRequest) {

        try {
            var response = service.verifyTask(taskRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Task task = service.registerTask(taskRequest);

            return !Objects.isNull(task) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, task)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editTask(@PathVariable("idTask") Integer idTask, @RequestBody TaskRequest taskRequest) {

        try {
            var response = service.verifyTask(taskRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Task editTask = service.editTask(idTask, taskRequest);

            return !Objects.isNull(editTask) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editTask)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Task.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Columns.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getTask(@PathVariable("idTask") Integer idTask) {

        try {
            Task task = service.getTask(idTask);

            if (!Objects.isNull(task)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, task));
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
    @DeleteMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteTask(@PathVariable("idTask") Integer idTask) {

        try {
            boolean deleteTask = service.deleteTask(idTask);

            return deleteTask ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Task.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Task.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Task.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllByColumns(@PathVariable(value = "idColumn") Integer idColumn) {

        try {
            List<Task> tasks = service.getAllTaskByColumn(idColumn);

            return !Objects.isNull(tasks) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, tasks)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class, e.getMessage()));
        }
    }

}
