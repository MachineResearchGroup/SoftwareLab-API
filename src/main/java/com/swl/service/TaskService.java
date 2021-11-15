package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Columns;
import com.swl.models.project.Task;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.ColumnRepository;
import com.swl.repository.TaskRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TaskService {

    @Autowired
    private final ColumnRepository columnRepository;

    @Autowired
    private final TaskRepository repository;


    public ResponseEntity<?> verifyTask(TaskRequest taskRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Task task = new Task();

        modelUtil.map(taskRequest, task);
        List<MessageResponse> messageResponses = modelUtil.validate(task);

        if (columnRepository.findById(taskRequest.getIdColumn()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Columns.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Task.class));
    }


    public Task registerTask(TaskRequest taskRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Columns> column = columnRepository.findById(taskRequest.getIdColumn());

        if (column.isPresent()) {
            Task task = new Task();
            modelUtil.map(taskRequest, task);
            task.setColumn(column.get());

            task = repository.save(task);

            if(Objects.isNull(column.get().getTasks()))
                column.get().setTasks(new ArrayList<>());
            column.get().getTasks().add(task);

            columnRepository.save(column.get());
            return task;
        } else {
            return null;
        }
    }


    public Task editTask(Integer idTask, TaskRequest taskRequest) {
        Optional<Task> taskAux = repository.findById(idTask);

        if (taskAux.isPresent()) {
            CopyUtil.copyProperties(taskRequest, taskAux);
            return repository.save(taskAux.get());
        }
        return null;
    }


    public Task getTask(Integer idTask) {
        return repository.findById(idTask).orElse(null);
    }


    public List<Task> getAllTaskByColumn(Integer idColumn) {
        return repository.findAllByColumnId(idColumn).orElse(null);
    }


    public boolean deleteTask(Integer idTask) {
        if (repository.existsById(idTask)) {
            repository.deleteById(idTask);
            return true;
        }
        return false;
    }


}
