package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
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

        service.verifyTask(taskRequest);
        Task task = service.registerTask(taskRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, task));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editTask(@PathVariable("idTask") Integer idTask, @RequestBody TaskRequest taskRequest) {

        service.verifyTask(taskRequest);
        Task editTask = service.editTask(idTask, taskRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editTask));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getTask(@PathVariable("idTask") Integer idTask) {

        Task task = service.getTask(idTask);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, task));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteTask(@PathVariable("idTask") Integer idTask) {

        service.deleteTask(idTask);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Task.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idColumn}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllByColumns(@PathVariable(value = "idColumn") Integer idColumn) {

        List<Task> tasks = service.getAllTaskByColumn(idColumn);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, tasks));

    }

}
