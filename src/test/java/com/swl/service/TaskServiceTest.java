package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.models.project.Task;
import com.swl.payload.request.TaskRequest;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ColumnRepository;
import com.swl.repository.HistoryRepository;
import com.swl.repository.TaskRepository;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("TaskServiceTest")
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private ColumnRepository columnRepository;

    @Mock
    private HistoryRepository historyRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private UserService userService;

    @Mock
    private TaskRepository repository;

    private TaskService service;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new TaskService(columnRepository, historyRepository, collaboratorRepository, userService, repository);
    }


    @Test
    public void registerTask_Sucessfully() {
        Task task = BuilderUtil.buildTask();
        History history = BuilderUtil.buildHistory();
        history.setId(1);
        List<Integer> collaborators = new ArrayList<>();
        collaborators.add(1);

        TaskRequest taskRequest = TaskRequest.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .endDate("2021-10-10")
                .priority(task.getPriority())
                .duration(1)
                .estimate(1)
                .idColumn(1)
                .idHistory(1)
                .idCollaborators(collaborators)
                .build();

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));
        task.setCollaborators(collaboratorList);

        LocalDate formattedData = LocalDate.parse(taskRequest.getEndDate(), formatter);
        task.setEndDate(formattedData);

        Mockito.when(historyRepository.findById(1)).thenReturn(Optional.of(history));
        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Columns.class)));
        Mockito.when(collaboratorRepository.findAllById(collaborators))
                .thenReturn(collaboratorList);
        Mockito.when(repository.save(task)).thenReturn(task);

        var response = service.registerTask(taskRequest);
        Assertions.assertEquals(response, task);
    }


    @Test
    public void registerTask_Error() {
        Task task = BuilderUtil.buildTask();
        List<Integer> collaborators = new ArrayList<>();
        collaborators.add(1);

        TaskRequest taskRequest = TaskRequest.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .endDate("2021-10-10")
                .priority(task.getPriority())
                .duration(1)
                .estimate(1)
                .idColumn(1)
                .idCollaborators(collaborators)
                .build();

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));
        task.setCollaborators(collaboratorList);

        LocalDate formattedData = LocalDate.parse(taskRequest.getEndDate(), formatter);
        task.setEndDate(formattedData);

        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerTask(taskRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        task = BuilderUtil.buildTask();

        taskRequest = TaskRequest.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .endDate("123")
                .priority(task.getPriority())
                .duration(1)
                .estimate(1)
                .idColumn(1)
                .idCollaborators(collaborators)
                .build();

        thrownException.set(false);
        try {
            service.registerTask(taskRequest);
        } catch (InvalidFieldException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editTask_Sucessfully() {
        Task task = BuilderUtil.buildTask();
        List<Integer> collaborators = new ArrayList<>();
        collaborators.add(1);

        TaskRequest taskRequest = TaskRequest.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .endDate("2021-10-10")
                .priority(task.getPriority())
                .duration(1)
                .estimate(1)
                .idColumn(1)
                .idCollaborators(collaborators)
                .build();

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));
        task.setCollaborators(collaboratorList);

        LocalDate formattedData = LocalDate.parse(taskRequest.getEndDate(), formatter);
        task.setEndDate(formattedData);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(task));
        Mockito.when(repository.save(task)).thenReturn(task);

        var response = service.editTask(1, taskRequest);
        Assertions.assertEquals(response, task);
    }


    @Test
    public void getTask_Sucessfully() {
        Task task = BuilderUtil.buildTask();
        task.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(task));
        var response = service.getTask(1);

        Assertions.assertEquals(response, task);
    }


    @Test
    public void getTask_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getTask(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllTaskByColumn_Sucessfully() {
        Task task = BuilderUtil.buildTask();
        task.setId(1);
        Columns columns = BuilderUtil.buildColumn();
        columns.setTasks(new ArrayList<>(Collections.singletonList(task)));

        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.of(columns));
        Mockito.when(repository.findAllByColumnId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(task))));

        var response = service.getAllTaskByColumn(1);
        Assertions.assertEquals(response.get(0), task);
    }


    @Test
    public void getAllTaskByColumn_Error() {
        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllTaskByColumn(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllTaskByHistory_Sucessfully() {
        Task task = BuilderUtil.buildTask();
        task.setId(1);
        History history = BuilderUtil.buildHistory();
        history.setTasks(new ArrayList<>(Collections.singletonList(task)));

        Mockito.when(historyRepository.findById(1)).thenReturn(Optional.of(history));
        Mockito.when(repository.findAllByHistoryId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(task))));

        var response = service.getAllTaskByHistory(1);
        Assertions.assertEquals(response.get(0), task);
    }


    @Test
    public void getAllTaskByHistory_Error() {
        Mockito.when(historyRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllTaskByHistory(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllTaskByCollaborator_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        Task task = BuilderUtil.buildTask();
        task.setId(1);
        task.setCollaborators(new ArrayList<>(Collections.singletonList(collaborator)));

        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findAllByCollaboratorId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(task))));

        var response = service.getAllTaskByCollaborator(1);
        Assertions.assertEquals(response.get(0), task);
    }


    @Test
    public void getAllTaskByCollaborator_Error() {
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllTaskByCollaborator(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllTaskByCollaboratorActual_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);
        Task task = BuilderUtil.buildTask();
        task.setId(1);
        task.setCollaborators(new ArrayList<>(Collections.singletonList(collaborator)));

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findAllByCollaboratorId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(task))));

        var response = service.getAllTaskByCollaboratorActual();
        Assertions.assertEquals(response.get(0), task);
    }


    @Test
    public void getAllTaskByCollaboratorActual_Error() {
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());
        thrownException.set(false);
        try {
            service.getAllTaskByCollaboratorActual();
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }

    @Test
    public void deleteTask_Sucessfully() {
        Task task = BuilderUtil.buildTask();
        task.setId(1);
        Columns columns = BuilderUtil.buildColumn();
        columns.setId(1);
        columns.setTasks(new ArrayList<>(Collections.singletonList(task)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(task));

        service.deleteTask(1);
    }

}
