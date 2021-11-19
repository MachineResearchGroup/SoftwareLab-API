package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.Task;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.ErrorResponse;
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


    public void verifyTask(TaskRequest taskRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Task task = new Task();

        if (columnRepository.findById(taskRequest.getIdColumn()).isEmpty()) {
            throw new NotFoundException(Columns.class);
        }

        modelUtil.map(taskRequest, task);
        ErrorResponse error = modelUtil.validate(task);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    public Task registerTask(TaskRequest taskRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Columns> column = columnRepository.findById(taskRequest.getIdColumn());

        if (column.isPresent()) {
            Task task = new Task();
            modelUtil.map(taskRequest, task);
            task.setColumn(column.get());

            task = repository.save(task);

            if (Objects.isNull(column.get().getTasks()))
                column.get().setTasks(new ArrayList<>());
            column.get().getTasks().add(task);

            columnRepository.save(column.get());
            return task;
        } else {
            throw new NotFoundException(Columns.class);
        }
    }


    public Task editTask(Integer idTask, TaskRequest taskRequest) {
        Task taskAux = getTask(idTask);
        CopyUtil.copyProperties(taskRequest, taskAux);
        return repository.save(taskAux);
    }


    public Task getTask(Integer idTask) {
        Optional<Task> tasks = repository.findById(idTask);
        if (tasks.isPresent()) {
            return tasks.get();
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public List<Task> getAllTaskByColumn(Integer idColumn) {
        Optional<Columns> columns = columnRepository.findById(idColumn);
        if (columns.isPresent()) {
            return repository.findAllByColumnId(idColumn).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Columns.class);
    }


    public void deleteTask(Integer idTask) {
        Task task = getTask(idTask);
        repository.deleteById(task.getId());
    }


}
