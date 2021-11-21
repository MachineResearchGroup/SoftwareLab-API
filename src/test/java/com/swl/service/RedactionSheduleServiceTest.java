package com.swl.service;

import com.swl.exceptions.business.AlreadyExistsException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Project;
import com.swl.models.project.RedactionShedule;
import com.swl.payload.request.RedactionSheduleRequest;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ProjectRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("RedactionSheduleServiceTest")
@ExtendWith(MockitoExtension.class)
public class RedactionSheduleServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private RedactionSheduleRepository repository;

    private RedactionSheduleService service;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new RedactionSheduleService(projectRepository, collaboratorRepository, repository);
    }


    @Test
    public void registerRedactionShedule_Sucessfully() {
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .project(project)
                .collaborator(collaborator)
                .build();

        RedactionSheduleRequest redactionRequest = RedactionSheduleRequest.builder()
                .initDate("2021-11-21 09:00")
                .endDate("2021-11-30 09:00")
                .idProject(1)
                .idCollaborator(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.save(redactionShedule)).thenReturn(redactionShedule);

        service.registerRedactionShedule(redactionRequest);
    }


    @Test
    public void registerRedactionShedule_Error() {
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .project(project)
                .collaborator(collaborator)
                .build();

        RedactionSheduleRequest redactionRequest = RedactionSheduleRequest.builder()
                .initDate("2021-11-21 09:00")
                .endDate("2021-11-30 09:00")
                .idProject(1)
                .idCollaborator(1)
                .build();


        redactionRequest.setInitDate("123");
        thrownException.set(false);
        try {
            service.registerRedactionShedule(redactionRequest);
        } catch (InvalidFieldException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        redactionRequest.setInitDate("2021-11-21 09:00");
        redactionRequest.setEndDate("123");
        thrownException.set(false);
        try {
            service.registerRedactionShedule(redactionRequest);
        } catch (InvalidFieldException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        redactionRequest.setEndDate("2021-11-30 09:00");
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerRedactionShedule(redactionRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerRedactionShedule(redactionRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findByProjectId(1)).thenReturn(Optional.of(redactionShedule));

        thrownException.set(false);
        try {
            service.registerRedactionShedule(redactionRequest);
        } catch (AlreadyExistsException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

    }


    @Test
    public void editRedactionShedule_Sucessfully() {
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .project(project)
                .collaborator(collaborator)
                .build();

        RedactionSheduleRequest redactionRequest = RedactionSheduleRequest.builder()
                .initDate("2021-11-21 09:00")
                .endDate("2021-11-30 09:00")
                .idProject(1)
                .idCollaborator(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(redactionShedule));
        Mockito.when(repository.save(redactionShedule)).thenReturn(redactionShedule);

        var response = service.editRedactionShedule(1, redactionRequest);
    }


    @Test
    public void editRedactionShedule_Error() {
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .project(project)
                .collaborator(collaborator)
                .build();

        RedactionSheduleRequest redactionRequest = RedactionSheduleRequest.builder()
                .initDate("2021-11-21 09:00")
                .endDate("2021-11-30 09:00")
                .idProject(1)
                .idCollaborator(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.editRedactionShedule(1, redactionRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(collaboratorRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.editRedactionShedule(1, redactionRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }

    @Test
    public void getRedaction_Sucessfully() {
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);

        RedactionShedule redactionShedule = RedactionShedule.builder()
                .initDate(LocalDateTime.parse("2021-11-21 09:00", formatter))
                .endDate(LocalDateTime.parse("2021-11-30 09:00", formatter))
                .project(project)
                .collaborator(collaborator)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(redactionShedule));

        var response = service.getRedactionShedule(1);

        Assertions.assertEquals(response, redactionShedule);
    }


    @Test
    public void getRedactionShedule_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getRedactionShedule(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getRedactionSheduleByProject_Sucessfully() {
        Mockito.when(repository.findByProjectId(1)).thenReturn(Optional.of(Mockito.mock(RedactionShedule.class)));
       service.getRedactionSheduleByProject(1);
    }


    @Test
    public void getRedactionSheduleByProject_Error() {
        Mockito.when(repository.findByProjectId(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getRedactionSheduleByProject(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteRedactionShedule_Sucessfully() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(Mockito.mock(RedactionShedule.class)));
        service.deleteRedactionShedule(1);
    }

}
