package com.swl.service;

import com.swl.exceptions.business.EmptyException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Organization;
import com.swl.models.management.Team;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Project;
import com.swl.payload.request.ProjectRequest;
import com.swl.repository.*;
import com.swl.util.BuilderUtil;
import org.checkerframework.checker.units.qual.A;
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
import java.util.concurrent.atomic.AtomicBoolean;


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

    @Mock
    private OrganizationRepository organizationRepository;

    private ProjectService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);


    @BeforeEach
    public void initUseCase() {
        service = new ProjectService(repository, teamRepository, clientRepository, collaboratorRepository, teamService,
                userService, organizationRepository);
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

        thrownException.set(false);
        try {
            service.registerProject(projetoRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
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

        thrownException.set(false);
        try {
            service.editProject(1, projetoRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
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

        thrownException.set(false);
        try {
            service.getProject(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteProject_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        Project project = BuilderUtil.buildProject();
        project.setId(1);
        team.setProjects(new ArrayList<>(Collections.singletonList(project)));
        List<Team> teamList = new ArrayList<>(Collections.singletonList(team));

        Mockito.when(teamRepository.findAllByProjectId(1)).thenReturn(Optional.of(teamList));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));
        service.deleteProject(1);
    }


    @Test
    public void deleteProject_Error() {
        thrownException.set(false);
        try {
            service.deleteProject(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void addClientInProject_Sucessfully() {
        Project project = BuilderUtil.buildProject();
        project.setClients(null);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findClientByUserEmail("client@gmail.com"))
                .thenReturn(Optional.of(Mockito.mock(Client.class)));

        service.addClientInProject(1, "client@gmail.com");

        project = BuilderUtil.buildProject();
        Client client = Mockito.mock(Client.class);
        project.setClients(new ArrayList<>(Collections.singletonList(client)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(clientRepository.findClientByUserEmail("client@gmail.com"))
                .thenReturn(Optional.of(client));

        service.addClientInProject(1, "client@gmail.com");
    }


    @Test
    public void addClientInProject_Error() {
        Project project = BuilderUtil.buildProject();
        Mockito.when(repository.findById(1)).thenReturn(Optional.ofNullable(project));
        Mockito.when(clientRepository.findClientByUserEmail("client@gmail.com"))
                .thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.addClientInProject(1, "client@gmail.com");
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
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
    public void getAllProjectsByTeam_Error() {
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllProjectsByTeam(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
    }


    @Test
    public void getAllProjectsByOrganization_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setProjects(new ArrayList<>(Collections.singletonList(Mockito.mock(Project.class))));
        List<Team> teamList = new ArrayList<>(Collections.singletonList(team));

        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));
        Mockito.when(teamService.getAllTeamByOrganization(1)).thenReturn(teamList);

        var response = service.getAllProjectsByOrganization(1);
        Assertions.assertEquals(response.get(0), team.getProjects().get(0));
    }


    @Test
    public void getAllProjectsByOrganization_ErrorEmpty() {
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));
        Mockito.when(teamService.getAllTeamByOrganization(1)).thenReturn(new ArrayList<>());

        thrownException.set(false);
        try {
            service.getAllProjectsByOrganization(1);
        } catch (EmptyException e) {
            thrownException.set(true);
        }
    }


    @Test
    public void getAllProjectsByOrganization_Error() {
        thrownException.set(false);
        try {
            service.getAllProjectsByOrganization(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
    }


    @Test
    public void getAllProjectsByClient_Sucessfully() {
        Mockito.when(clientRepository.existsById(1)).thenReturn(true);
        Mockito.when(repository.findAllByClientId(1)).thenReturn(Optional.of(
                new ArrayList<>(Collections.singletonList(Mockito.mock(Project.class)))));

        var response = service.getAllProjectsByClient(1);
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void getAllProjectsByClient_Error() {
        Mockito.when(clientRepository.existsById(1)).thenReturn(false);
        thrownException.set(false);
        try {
            service.getAllProjectsByClient(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
    }


    @Test
    public void getAllProjectsByCollaborator_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setProjects(new ArrayList<>(Collections.singletonList(Mockito.mock(Project.class))));
        team.setId(1);
        List<Team> teamList = new ArrayList<>(Collections.singletonList(team));

        Mockito.when(collaboratorRepository.existsById(1)).thenReturn(true);
        Mockito.when(teamRepository.findAllByCollaboratorId(1)).thenReturn(Optional.of(teamList));
        Mockito.when(teamRepository.existsById(1)).thenReturn(true);
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.ofNullable(teamList.get(0)));

        var response = service.getAllProjectsByCollaborator(1);
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void getAllProjectsByCollaborator_Error() {
        Mockito.when(collaboratorRepository.existsById(1)).thenReturn(false);

        thrownException.set(false);
        try {
            service.getAllProjectsByCollaborator(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
    }


    @Test
    public void getAllProjectsByCollaboratorActual_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);

        Team team = BuilderUtil.buildTeam();
        team.setProjects(new ArrayList<>(Collections.singletonList(Mockito.mock(Project.class))));
        team.setId(1);
        List<Team> teamList = new ArrayList<>(Collections.singletonList(team));

        Mockito.when(collaboratorRepository.existsById(1)).thenReturn(true);
        Mockito.when(teamRepository.findAllByCollaboratorId(1)).thenReturn(Optional.of(teamList));
        Mockito.when(teamRepository.existsById(1)).thenReturn(true);
        Mockito.when(teamRepository.findById(1)).thenReturn(Optional.ofNullable(teamList.get(0)));

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));

        var response = service.getAllProjectsByCollaboratorActual();
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void getAllProjectsByCollaboratorActual_Error() {
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllProjectsByCollaboratorActual();
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
    }
}
