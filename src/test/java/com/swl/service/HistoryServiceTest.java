package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Columns;
import com.swl.models.project.History;
import com.swl.payload.request.HistoryRequest;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ColumnRepository;
import com.swl.repository.HistoryRepository;
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
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("HistoryServiceTest")
@ExtendWith(MockitoExtension.class)
public class HistoryServiceTest {

    @Mock
    private ColumnRepository columnRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private UserService userService;

    @Mock
    private HistoryRepository repository;

    private HistoryService service;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    AtomicBoolean thrownException = new AtomicBoolean(false);


    @BeforeEach
    public void initUseCase() {
        service = new HistoryService(columnRepository, collaboratorRepository, userService, repository);
    }


    @Test
    public void registerHistory_Sucessfully() {
        History history = BuilderUtil.buildHistory();
        List<Integer> collaborators = new ArrayList<>();
        collaborators.add(1);

        HistoryRequest historyRequest = HistoryRequest.builder()
                .title(history.getTitle())
                .description(history.getDescription())
                .endDate("2021-10-10")
                .priority(history.getPriority())
                .weight(history.getWeight())
                .idColumn(1)
                .idCollaborators(collaborators)
                .build();
        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));
        history.setCollaborators(collaboratorList);

        LocalDate formattedData = LocalDate.parse(historyRequest.getEndDate(), formatter);
        history.setEndDate(formattedData);

        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Columns.class)));
        Mockito.when(collaboratorRepository.findAllById(collaborators))
                .thenReturn(collaboratorList);
        Mockito.when(repository.save(history)).thenReturn(history);

        var response = service.registerHistory(historyRequest);
        Assertions.assertEquals(response, history);
    }


    @Test
    public void registerHistory_Error() {
        History history = BuilderUtil.buildHistory();

        HistoryRequest historyRequest = HistoryRequest.builder()
                .title(history.getTitle())
                .description(history.getDescription())
                .endDate("2021-10-10")
                .priority(history.getPriority())
                .weight(history.getWeight())
                .idColumn(1)
                .build();
        history.setId(1);

        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerHistory(historyRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        history = BuilderUtil.buildHistory();

        historyRequest = HistoryRequest.builder()
                .title(history.getTitle())
                .description(history.getDescription())
                .endDate("123")
                .priority(history.getPriority())
                .weight(history.getWeight())
                .idColumn(1)
                .build();
        history.setId(1);

        thrownException.set(false);
        try {
            service.registerHistory(historyRequest);
        } catch (InvalidFieldException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editHistory_Sucessfully() {
        History history = BuilderUtil.buildHistory();

        HistoryRequest historyRequest = HistoryRequest.builder()
                .title(history.getTitle())
                .description(history.getDescription())
                .endDate("2021-10-10")
                .priority(history.getPriority())
                .weight(history.getWeight())
                .idColumn(1)
                .build();
        history.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(history));
        Mockito.when(repository.save(history)).thenReturn(history);

        var response = service.editHistory(1, historyRequest);
        Assertions.assertEquals(response, history);
    }


    @Test
    public void getHistory_Sucessfully() {
        History history = BuilderUtil.buildHistory();
        history.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(history));
        var response = service.getHistory(1);

        Assertions.assertEquals(response, history);
    }


    @Test
    public void getHistory_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getHistory(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllHistoryByColumn_Sucessfully() {
        History history = BuilderUtil.buildHistory();
        history.setId(1);
        Columns columns = BuilderUtil.buildColumn();
        columns.setHistories(new ArrayList<>(Collections.singletonList(history)));

        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.of(columns));
        Mockito.when(repository.findAllByColumnId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(history))));

        var response = service.getAllHistoryByColumn(1);
        Assertions.assertEquals(response.get(0), history);
    }


    @Test
    public void getAllHistoryByColumn_Error() {
        Mockito.when(columnRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllHistoryByColumn(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllHistoryByCollaborator_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        History history = BuilderUtil.buildHistory();
        history.setId(1);
        history.setCollaborators(new ArrayList<>(Collections.singletonList(collaborator)));

        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findAllByCollaboratorId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(history))));

        var response = service.getAllHistoriesByCollaborator(1);
        Assertions.assertEquals(response.get(0), history);
    }


    @Test
    public void getAllHistoryByCollaborator_Error() {
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllHistoriesByCollaborator(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllHistoryByCollaboratorActual_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);
        History history = BuilderUtil.buildHistory();
        history.setId(1);
        history.setCollaborators(new ArrayList<>(Collections.singletonList(collaborator)));

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findAllByCollaboratorId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(history))));

        var response = service.getAllHistoriesByCollaboratorActual();
        Assertions.assertEquals(response.get(0), history);
    }


    @Test
    public void getAllHistoryByCollaboratorActual_Error() {
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());
        thrownException.set(false);
        try {
            service.getAllHistoriesByCollaboratorActual();
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteHistory_Sucessfully() {
        History history = BuilderUtil.buildHistory();
        history.setId(1);
        Columns columns = BuilderUtil.buildColumn();
        columns.setId(1);
        columns.setHistories(new ArrayList<>(Collections.singletonList(history)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(history));
        Mockito.when(columnRepository.findByHistoryId(1)).thenReturn(Optional.of(columns));

        service.deleteHistory(1);
    }

}
