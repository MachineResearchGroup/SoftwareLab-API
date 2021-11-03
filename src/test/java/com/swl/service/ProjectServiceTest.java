package com.swl.service;

import com.swl.models.*;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.ProjectRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.*;
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

import java.util.*;


@SpringBootTest
@DisplayName("ProjectServiceTest")
@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {

    @Mock
    private ProjectRepository repository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private TeamService teamService;

    private ProjectService service;


    @BeforeEach
    public void initUseCase() {
        service = new ProjectService(repository, teamRepository, clientRepository, teamService);
    }


    @Test
    public void registerProject_Sucessfully() {
        Project project = BuilderUtil.buildProjeto();

        ProjectRequest projetoRequest = ProjectRequest.builder()
                .name(project.getName())
                .description(project.getDescription())
                .repository(project.getRepository())
                .idTeam(1)
                .build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Team.class)));
        Mockito.when(repository.save(project)).thenReturn(project);

        var response = service.registerProject(projetoRequest);
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()), new MessageResponse(MessageEnum.REGISTERED, project));
    }


    @Test
    public void registerProject_ErrorTeam() {
        Project project = BuilderUtil.buildProjeto();

        ProjectRequest projetoRequest = ProjectRequest.builder()
                .name(project.getName())
                .description(project.getDescription())
                .repository(project.getRepository())
                .idTeam(1)
                .build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        var response = service.registerProject(projetoRequest);
        Assertions.assertEquals(Objects.requireNonNull(response.getBody()), new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
    }


    @Test
    public void editProject_Sucessfully() {
        Project project = BuilderUtil.buildProjeto();

        ProjectRequest projetoRequest = ProjectRequest.builder()
                .name(project.getName())
                .description(project.getDescription())
                .repository(project.getRepository())
                .idTeam(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(repository.save(project)).thenReturn(project);

        var response = service.editProject(1, projetoRequest);
        Assertions.assertEquals(response, project);
    }


    @Test
    public void editProject_Error() {
        Project project = BuilderUtil.buildProjeto();

        ProjectRequest projetoRequest = ProjectRequest.builder()
                .name(project.getName())
                .description(project.getDescription())
                .repository(project.getRepository())
                .idTeam(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        var response = service.editProject(1, projetoRequest);
        Assertions.assertNull(response);
    }


    @Test
    public void getProject_Sucessfully() {
        Project project = Mockito.mock(Project.class);
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));

        var response = service.getProject(1);
        Assertions.assertEquals(response, project);
    }


    @Test
    public void getProject_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        var response = service.getProject(1);
        Assertions.assertNull(response);
    }


    @Test
    public void deleteProject_Sucessfully() {
        Mockito.when(repository.existsById(1)).thenReturn(true);

        var response = service.deleteProject(1);
        Assertions.assertTrue(response);
    }


    @Test
    public void deleteProject_Error() {
        Mockito.when(repository.existsById(1)).thenReturn(false);

        var response = service.deleteProject(1);
        Assertions.assertFalse(response);
    }
}
