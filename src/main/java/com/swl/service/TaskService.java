package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Board;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.models.project.Task;
import com.swl.payload.request.TaskRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ColumnRepository;
import com.swl.repository.HistoryRepository;
import com.swl.repository.TaskRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final HistoryRepository historyRepository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final UserService userService;

    @Autowired
    private final TaskRepository repository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private void verifyTask(TaskRequest taskRequest) {
        try {
            LocalDate formattedData = LocalDate.parse(taskRequest.getEndDate(), formatter);
        } catch (Exception parseException) {
            throw new InvalidFieldException(ErrorResponse.builder()
                    .key("endDate")
                    .build());
        }
    }


    public Task registerTask(TaskRequest taskRequest) {
        verifyTask(taskRequest);
        LocalDate formattedData = LocalDate.parse(taskRequest.getEndDate(), formatter);
        taskRequest.setEndDate(null);

        Optional<Columns> column = columnRepository.findById(taskRequest.getIdColumn());

        if (column.isPresent()) {
            List<Collaborator> collaborator = collaboratorRepository.findAllById(taskRequest.getIdCollaborators());
            if (!collaborator.isEmpty()) {

                Task task = new Task();
                CopyUtil.copyProperties(taskRequest, task);
                task.setColumn(column.get());
                task.setEndDate(formattedData);
                task.setCollaborators(collaborator);

                task = repository.save(task);

                if (Objects.isNull(column.get().getTasks()))
                    column.get().setTasks(new ArrayList<>());
                column.get().getTasks().add(task);

                if (!Objects.isNull(taskRequest.getIdHistory())) {
                    Optional<History> history = historyRepository.findById(taskRequest.getIdHistory());
                    if (history.isPresent()) {
                        if (Objects.isNull(history.get().getTasks()))
                            history.get().setTasks(new ArrayList<>());
                        history.get().getTasks().add(task);
                        historyRepository.save(history.get());
                        task.setHistory(history.get());
                    } else {
                        throw new NotFoundException(History.class);
                    }
                } else if (!Objects.isNull(taskRequest.getIdSuperTask())) {
                    Task superTask = getTask(taskRequest.getIdSuperTask());
                    task.setSuperTask(superTask);
                }

                columnRepository.save(column.get());
                return repository.save(task);
            } else {
                throw new NotFoundException(Collaborator.class);
            }
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
            throw new NotFoundException(Task.class);
        }
    }


    public List<Task> getAllTaskByColumn(Integer idColumn) {
        Optional<Columns> columns = columnRepository.findById(idColumn);
        if (columns.isPresent()) {
            return repository.findAllByColumnId(idColumn).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Columns.class);
    }


    public List<Task> getAllTaskByHistory(Integer idHistory) {
        Optional<History> history = historyRepository.findById(idHistory);
        if (history.isPresent()) {
            return repository.findAllByHistoryId(idHistory).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(History.class);
    }


    public List<Task> getAllTaskByCollaborator(Integer idCollaborator) {
        Optional<Collaborator> collaborator = collaboratorRepository.findById(idCollaborator);
        if (collaborator.isPresent()) {
            return repository.findAllByCollaboratorId(idCollaborator).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Collaborator.class);
    }


    public List<Task> getAllTaskByCollaboratorActual() {
        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            return getAllTaskByCollaborator(((Collaborator) userService.getCurrentUser().get()).getId());
        }
        throw new NotFoundException(Collaborator.class);
    }


    public void deleteTask(Integer idTask) {
        Task task = getTask(idTask);
        repository.deleteById(task.getId());
    }
}
