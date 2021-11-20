package com.swl.service;

import com.swl.exceptions.business.AlreadyExistsException;
import com.swl.exceptions.business.EmptyException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Organization;
import com.swl.models.management.Team;
import com.swl.models.people.Client;
import com.swl.models.project.Project;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
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
        Mockito.when(repository.existsById(1)).thenReturn(true);
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
        Mockito.when(teamService.getAllTeamByOrganization(1)).thenReturn(null);

        var response = service.getAllProjectsByOrganization(1);
        Assertions.assertNull(response);
    }
}
