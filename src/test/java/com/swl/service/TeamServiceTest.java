package com.swl.service;

import com.swl.exceptions.business.EmptyException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.management.Team;
import com.swl.models.people.User;
import com.swl.payload.request.CollaboratorRequest;
import com.swl.payload.request.TeamRequest;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.OrganizationRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.TeamRepository;
import com.swl.util.BuilderUtil;
import org.checkerframework.checker.units.qual.C;
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
@DisplayName("TeamServiceTest")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository repository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private OrganizationTeamRepository organizationTeamRepository;

    @Mock
    private AuthService authService;

    @Mock
    private UserService userService;

    private TeamService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);


    @BeforeEach
    public void initUseCase() {
        service = new TeamService(repository, organizationRepository, collaboratorRepository, organizationTeamRepository,
                authService, userService);
    }


    @Test
    public void registerTeam_Sucessfully() {
        Team team = BuilderUtil.buildTeam();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));

        Collaborator gerente = Mockito.mock(Collaborator.class);
        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(equipeRequest.getSupervisorEmail()))
                .thenReturn(Optional.of(gerente));

        team.setSupervisor(gerente);

        Mockito.when(repository.save(team)).thenReturn(team);

        var response = service.registerTeam(equipeRequest);
        Assertions.assertNotNull(response);

        team = BuilderUtil.buildTeam();

        equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .idOrganization(1)
                .build();

        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));

        Mockito.when(repository.save(team)).thenReturn(team);

        response = service.registerTeam(equipeRequest);
        Assertions.assertNotNull(response);
    }


    @Test
    public void registerTeam_ErrorSupervisorEmail() {
        Team team = BuilderUtil.buildTeam();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));
        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(equipeRequest.getSupervisorEmail())).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerTeam(equipeRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void registerTeam_ErrorOrganization() {
        Team team = BuilderUtil.buildTeam();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerTeam(equipeRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editTeam_Sucessfully() {
        Team team = BuilderUtil.buildTeam();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name("Equipe Update")
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));
        Mockito.when(repository.save(team)).thenReturn(team);

        var response = service.editTeam(1, equipeRequest);

        team.setName("Equipe Update");
        Assertions.assertEquals(response, team);
    }


    @Test
    public void editTeam_Error() {
        TeamRequest equipeRequest = TeamRequest.builder()
                .name("Equipe Update")
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.editTeam(1, equipeRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getTeam_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));
        var response = service.getTeam(1);

        Assertions.assertEquals(response, team);
    }


    @Test
    public void deleteTeam_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setId(1);
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));
        Mockito.when(organizationTeamRepository.findAllByTeamId(1))
                .thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(Mockito.mock(OrganizationTeam.class)))));

       service.deleteTeam(1);
    }


    @Test
    public void deleteTeam_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.deleteTeam(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void addColaborador_Sucessfully() {
        User user = BuilderUtil.buildUser();
        user.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setUser(user);

        Team team = BuilderUtil.buildTeam();
        team.setId(1);

        CollaboratorRequest collaboratorRequest = CollaboratorRequest.builder()
                .email(collaborator.getUser().getEmail())
                .function(collaborator.getFunction())
                .build();

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(collaborator));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));

        Mockito.when(organizationRepository.findOrganizationByTeamId(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));

        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(collaboratorRequest.getEmail()))
                .thenReturn(Optional.of(Mockito.mock(Collaborator.class)));

        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.of(collaboratorList));

        var response = service.addCollaborator(1, new ArrayList<>(Collections.singletonList(collaboratorRequest)));
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void addColaborador_Error() {
        User user = BuilderUtil.buildUser();
        user.setId(1);

        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setUser(user);

        Team team = BuilderUtil.buildTeam();
        team.setId(1);

        CollaboratorRequest collaboratorRequest = CollaboratorRequest.builder()
                .email(collaborator.getUser().getEmail())
                .function(collaborator.getFunction())
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));

        Mockito.when(organizationRepository.findOrganizationByTeamId(1))
                .thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.addCollaborator(1, new ArrayList<>(Collections.singletonList(collaboratorRequest)));
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getColaboradores_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));

        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.of(collaboratorList));

        var response = service.getCollaborators(1);
        Assertions.assertEquals(response, collaboratorList);
    }


    @Test
    public void deleteColaborador_Sucessfully() {
        Team team = BuilderUtil.buildTeam();
        team.setId(1);

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(BuilderUtil.buildCollaborator()));
        collaboratorList.get(0).setId(1);
        String email = "teste@gmai.com";

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));

        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.of(collaboratorList)).thenReturn(Optional.empty());

        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(email))
                .thenReturn(Optional.of(collaboratorList.get(0)));

        Mockito.when(organizationTeamRepository.findByTeamIdAndCollaboratorId(1, 1))
                .thenReturn(Optional.of(Mockito.mock(OrganizationTeam.class)));

        var response = service.deleteCollaborators(1, new ArrayList<>(Collections.singletonList(email)));
        Assertions.assertEquals(response, new ArrayList<>());
    }


    @Test
    public void deleteColaborador_ErrorEmpty() {
        Team team = BuilderUtil.buildTeam();
        team.setId(1);

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(BuilderUtil.buildCollaborator()));
        collaboratorList.get(0).setId(1);
        String email = "teste@gmai.com";

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));
        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.deleteCollaborators(1, new ArrayList<>(Collections.singletonList(email)));
        } catch (EmptyException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllTeamByOrganization_Sucessfully() {
        List<Team> teamList = new ArrayList<>(Collections.singletonList(Mockito.mock(Team.class)));

        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));

        Mockito.when(repository.findAllByOrganizationId(1))
                .thenReturn(Optional.of(teamList));

        var response = service.getAllTeamByOrganization(1);
        Assertions.assertEquals(response, teamList);
    }


    @Test
    public void getAllTeamByOrganization_Error() {
        Mockito.when(organizationRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllTeamByOrganization(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getTeamsByCollaboratorActual_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        collaborator.setId(1);
        List<Team> teamList = new ArrayList<>(Collections.singletonList(Mockito.mock(Team.class)));

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when( repository.findAllByCollaboratorId(1))
                .thenReturn(Optional.of(teamList));

        var response = service.getTeamsByCollaborator();
        Assertions.assertEquals(response, teamList);
    }


    @Test
    public void getTeamsByCollaboratorActual_Error() {
        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getTeamsByCollaborator();
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }
}
