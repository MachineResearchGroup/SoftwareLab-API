package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Project;
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
@RequestMapping("/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerTask(@RequestBody TaskRequest taskRequest) {

        Task task = service.registerTask(taskRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, task));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idTask}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editTask(@PathVariable("idTask") Integer idTask, @RequestBody TaskRequest taskRequest) {

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
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllTasks(@RequestParam(value = "idHistory", required = false) Integer idHistory,
                                         @RequestParam(value = "idColumn", required = false) Integer idColumn,
                                         @RequestParam(value = "idCollaborator", required = false) Integer idCollaborator) {


        List<Task> tasks;

        if (!Objects.isNull(idHistory)) {
            tasks = service.getAllTaskByHistory(idHistory);

        } else if (!Objects.isNull(idColumn)) {
            tasks = service.getAllTaskByColumn(idColumn);

        } else if (!Objects.isNull(idCollaborator)) {
            tasks = service.getAllTaskByCollaborator(idCollaborator);

        } else {
            tasks = service.getAllTaskByCollaboratorActual();
        }

        if(tasks.isEmpty()){
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Task.class, tasks));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, tasks));

    }

}
