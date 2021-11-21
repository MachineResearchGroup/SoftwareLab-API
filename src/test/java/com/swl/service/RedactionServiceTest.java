package com.swl.service;

import com.swl.exceptions.business.BlockedAdditionException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Client;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.models.project.Redaction;
import com.swl.models.project.RedactionShedule;
import com.swl.payload.request.RedactionRequest;
import com.swl.repository.ClientRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RedactionRepository;
import com.swl.repository.RedactionSheduleRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("ColumnServiceTest")
@ExtendWith(MockitoExtension.class)
public class RedactionServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private RedactionSheduleRepository redactionSheduleRepository;

    @Mock
    private RedactionRepository repository;

    private RedactionService service;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new RedactionService(projectRepository, clientRepository, redactionSheduleRepository,
                repository);
    }


    @Test
    public void registerRedaction_Sucessfully() {
        Redaction redaction = BuilderUtil.buildRedaction();
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        RedactionRequest redactionRequest = RedactionRequest.builder()
                .description(redaction.getDescription())
                .idProject(1)
                .idClient(1)
                .build();

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Client.class)));
        Mockito.when(redactionSheduleRepository.findByProjectId(1)).thenReturn(Optional.of(redactionShedule));

        var response = service.registerRedaction(redactionRequest);
    }


    @Test
    public void registerRedaction_Error() {
        Redaction redaction = BuilderUtil.buildRedaction();
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        RedactionRequest redactionRequest = RedactionRequest.builder()
                .description(redaction.getDescription())
                .idProject(1)
                .idClient(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());
        thrownException.set(false);
        try {
            service.registerRedaction(redactionRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.empty());
        thrownException.set(false);
        try {
            service.registerRedaction(redactionRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Client.class)));
        Mockito.when(redactionSheduleRepository.findByProjectId(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerRedaction(redactionRequest);
        } catch (BlockedAdditionException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editRedaction_Sucessfully() {
        Redaction redaction = BuilderUtil.buildRedaction();
        Project project = BuilderUtil.buildProject();
        project.setId(1);
        redaction.setProject(project);

        RedactionRequest redactionRequest = RedactionRequest.builder()
                .description(redaction.getDescription())
                .idProject(1)
                .idClient(1)
                .build();

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(redaction));
        Mockito.when(redactionSheduleRepository.findByProjectId(1)).thenReturn(Optional.of(redactionShedule));
        Mockito.when(repository.save(redaction)).thenReturn(redaction);

        var response = service.editRedaction(1, redactionRequest);
        Assertions.assertEquals(response, redaction);
    }


    @Test
    public void editRedaction_Error() {
        Redaction redaction = BuilderUtil.buildRedaction();
        Project project = BuilderUtil.buildProject();
        project.setId(1);
        redaction.setProject(project);

        RedactionRequest redactionRequest = RedactionRequest.builder()
                .description(redaction.getDescription())
                .idProject(1)
                .idClient(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(redaction));
        Mockito.when(redactionSheduleRepository.findByProjectId(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.editRedaction(1, redactionRequest);
        } catch (BlockedAdditionException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }

    @Test
    public void getRedaction_Sucessfully() {
        Redaction redaction = BuilderUtil.buildRedaction();
        redaction.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(redaction));

        var response = service.getRedaction(1);

        Assertions.assertEquals(response, redaction);
    }


    @Test
    public void getRedaction_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getRedaction(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllRedactionByProject_Sucessfully() {
        Redaction redaction = BuilderUtil.buildRedaction();
        redaction.setId(1);
        Project project = BuilderUtil.buildProject();
        project.setRedactions(new ArrayList<>(Collections.singletonList(redaction)));

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(repository.findAllByProjectId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(redaction))));

        var response = service.getAllRedactionByProject(1);
        Assertions.assertEquals(response.get(0), redaction);
    }


    @Test
    public void getAllRedactionByProject_Error() {
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllRedactionByProject(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllRedactionByClient_Sucessfully() {
        Redaction redaction = BuilderUtil.buildRedaction();
        redaction.setId(1);
        Client client = BuilderUtil.buildClient();

        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.of(client));
        Mockito.when(repository.findAllByClientId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(redaction))));

        var response = service.getAllRedactionByClient(1);
        Assertions.assertEquals(response.get(0), redaction);
    }


    @Test
    public void getAllRedactionByClient_Error() {
        Mockito.when(clientRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllRedactionByClient(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteRedactions_Sucessfully() {
        Redaction redaction = BuilderUtil.buildRedaction();
        redaction.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(redaction));

        service.deleteRedaction(1);
    }

}
