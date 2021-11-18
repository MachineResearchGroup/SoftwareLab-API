package com.swl.service;

import com.swl.models.management.Team;
import com.swl.models.project.Project;
import com.swl.models.user.Client;
import com.swl.payload.request.ProjectRequest;
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
import org.springframework.beans.factory.annotation.Autowired;
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
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private UserService userService;

    private ProjectService service;


    @BeforeEach
    public void initUseCase() {
        service = new ProjectService(repository, teamRepository, clientRepository, collaboratorRepository, teamService, userService);
    }


    @Test
    public void registerProject_Sucessfully() {
        Project project = BuilderUtil.buildProject();

        ProjectRequest projetoRequest = ProjectRequest.builder()
                .name(project.getName())
                .description(project.getDescription())
                .repository(project.getRepository())
                .idTeam(1)
                .build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Team.class)));
        Mockito.when(repository.save(project)).thenReturn(project);

        var response = service.registerProject(projetoRequest);
        Assertions.assertEquals(response, project);

        Team team = BuilderUtil.buildTeam();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        response = service.registerProject(projetoRequest);
        Assertions.assertEquals(response, project);
    }


    @Test
    public void registerProject_ErrorTeam() {
        Project project = BuilderUtil.buildProject();

        ProjectRequest projetoRequest = ProjectRequest.builder()
                .name(project.getName())
                .description(project.getDescription())
                .repository(project.getRepository())
                .idTeam(1)
                .build();

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        var response = service.registerProject(projetoRequest);
        Assertions.assertNull(response);
    }


    @Test
    public void editProject_Sucessfully() {
        Project project = BuilderUtil.buildProject();

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
        Project project = BuilderUtil.buildProject();

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


    @Test
    public void addClientInProject_Sucessfully() {
        Project project = BuilderUtil.buildProject();
        project.setClients(null);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findClientByUserEmail("client@gmail.com"))
                .thenReturn(Optional.of(Mockito.mock(Client.class)));

        var response = service.addClientInProject(1, "client@gmail.com");
        Assertions.assertTrue(response);

        project = BuilderUtil.buildProject();
        Client client = Mockito.mock(Client.class);
        project.setClients(new ArrayList<>(Collections.singletonList(client)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findClientByUserEmail("client@gmail.com"))
                .thenReturn(Optional.of(client));

        response = service.addClientInProject(1, "client@gmail.com");
        Assertions.assertTrue(response);
    }


    @Test
    public void addClientInProject_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(clientRepository.findClientByUserEmail("client@gmail.com"))
                .thenReturn(Optional.of(Mockito.mock(Client.class)));

        var response = service.addClientInProject(1, "client@gmail.com");
        Assertions.assertFalse(response);
    }


    @Test
    public void getAllProjectsByTeam_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setProjects(new ArrayList<>(Collections.singletonList(Mockito.mock(Project.class))));

        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.of(team));

        var response = service.getAllProjectsByTeam(1);
        Assertions.assertEquals(response.get(0), team.getProjects().get(0));
    }


    @Test
    public void getAllProjectsByOrganization_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setProjects(new ArrayList<>(Collections.singletonList(Mockito.mock(Project.class))));
        List<Team> teamList = new ArrayList<>(Collections.singletonList(team));

        Mockito.when(teamService.getAllTeamByOrganization(1)).thenReturn(teamList);

        var response = service.getAllProjectsByOrganization(1);
        Assertions.assertEquals(response.get(0), team.getProjects().get(0));
    }


    @Test
    public void getAllProjectsByOrganization_Error() {
        Mockito.when(teamService.getAllTeamByOrganization(1)).thenReturn(null);

        var response = service.getAllProjectsByOrganization(1);
        Assertions.assertNull(response);
    }
}
