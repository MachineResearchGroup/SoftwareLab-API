package com.swl.service;

import com.swl.models.Collaborator;
import com.swl.models.Team;
import com.swl.models.Organization;
import com.swl.models.OrganizationTeam;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.TeamRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.TeamRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.OrganizacaoRepository;
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
import org.springframework.http.HttpStatus;

import java.util.*;


@SpringBootTest
@DisplayName("TeamServiceTest")
@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository repository;

    @Mock
    private OrganizacaoRepository organizacaoRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private OrganizationTeamRepository organizationTeamRepository;

    private TeamService service;


    @BeforeEach
    public void initUseCase() {
        service = new TeamService(repository, organizacaoRepository, collaboratorRepository, organizationTeamRepository);
    }


    @Test
    public void registerTeam_Sucessfully() {
        Team team = BuilderUtil.buildEquipe();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(organizacaoRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));

        Collaborator gerente = Mockito.mock(Collaborator.class);
        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(equipeRequest.getSupervisorEmail()))
                .thenReturn(Optional.of(gerente));

        team.setSupervisor(gerente);

        Mockito.when(repository.save(team)).thenReturn(team);

        var response = service.registerTeam(equipeRequest);
        Assertions.assertEquals(Objects.requireNonNull(response.getStatusCode()), HttpStatus.OK);
    }


    @Test
    public void registerTeam_ErrorSupervisorEmail() {
        Team team = BuilderUtil.buildEquipe();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(organizacaoRepository.findById(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));
        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(equipeRequest.getSupervisorEmail())).thenReturn(Optional.empty());

        var response = service.registerTeam(equipeRequest);

        Assertions.assertEquals(Objects.requireNonNull(response.getBody()),
                new MessageResponse(MessageEnum.NOT_FOUND, "email"));
    }


    @Test
    public void registerTeam_ErrorOrganization() {
        Team team = BuilderUtil.buildEquipe();

        TeamRequest equipeRequest = TeamRequest.builder()
                .name(team.getName())
                .supervisorEmail("gerente@gmail.com")
                .idOrganization(1)
                .build();

        Mockito.when(organizacaoRepository.findById(1)).thenReturn(Optional.empty());

        var response = service.registerTeam(equipeRequest);

        Assertions.assertEquals(Objects.requireNonNull(response.getBody()),
                new MessageResponse(MessageEnum.NOT_REGISTERED, Team.class));
    }


    @Test
    public void editTeam_Sucessfully() {
        Team team = BuilderUtil.buildEquipe();

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
        var response = service.editTeam(1, equipeRequest);

        Assertions.assertNull(response);
    }


    @Test
    public void getTeam_Sucessfully() {
        Team team = BuilderUtil.buildEquipe();
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));
        var response = service.getTeam(1);

        Assertions.assertEquals(response, team);
    }


    @Test
    public void deleteEquipe_Sucessfully() {

        Mockito.when(repository.existsById(1)).thenReturn(true);
        Mockito.when(organizationTeamRepository.findAllByTeamId(1))
                .thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(Mockito.mock(OrganizationTeam.class)))));

        var response = service.deleteTeam(1);

        Assertions.assertTrue(response);
    }


    @Test
    public void deleteEquipe_Error() {
        Mockito.when(repository.existsById(1)).thenReturn(false);
        var response = service.deleteTeam(1);

        Assertions.assertFalse(response);
    }


    @Test
    public void addColaborador_Sucessfully() {
        Team team = BuilderUtil.buildEquipe();
        team.setId(1);

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));
        String email = "teste@gmai.com";

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));

        Mockito.when(organizacaoRepository.findOrganizacaoByEquipeId(1)).thenReturn(Optional.of(Mockito.mock(Organization.class)));

        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(email))
                .thenReturn(Optional.of(Mockito.mock(Collaborator.class)));

        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.of(collaboratorList));

        var response = service.addCollaborator(1, new ArrayList<>(Collections.singletonList(email)));
        Assertions.assertEquals(response, collaboratorList);
    }


    @Test
    public void addColaborador_Error() {
        Team team = BuilderUtil.buildEquipe();
        team.setId(1);

        String email = "teste@gmai.com";

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        var response = service.addCollaborator(1, new ArrayList<>(Collections.singletonList(email)));
        Assertions.assertNull(response);
    }


    @Test
    public void getColaboradores_Sucessfully() {
        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)));

        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.of(collaboratorList));

        var response = service.getCollaborators(1);
        Assertions.assertEquals(response, collaboratorList);
    }


    @Test
    public void getColaboradores_Error() {
        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.empty());

        var response = service.getCollaborators(1);
        Assertions.assertEquals(response, new ArrayList<>());
    }


    @Test
    public void deleteColaborador_Sucessfully() {
        Team team = BuilderUtil.buildEquipe();
        team.setId(1);

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(BuilderUtil.buildColaborador()));
        collaboratorList.get(0).setId(1);
        String email = "teste@gmai.com";

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(team));

        Mockito.when(collaboratorRepository.findAllCollaboratorByTeamId(1))
                .thenReturn(Optional.of(collaboratorList)).thenReturn(Optional.empty());

        Mockito.when(collaboratorRepository.findCollaboratorByUserEmail(email))
                .thenReturn(Optional.of(collaboratorList.get(0)));

        Mockito.when(organizationTeamRepository.findByTeamIdAndCollaboratorId(1, 1))
                .thenReturn(Optional.of(Mockito.mock(OrganizationTeam.class)));

        var response = service.deleteCollaborator(1, new ArrayList<>(Collections.singletonList(email)));
        Assertions.assertEquals(response, new ArrayList<>());
    }


    @Test
    public void deleteColaborador_Error() {
        Team team = BuilderUtil.buildEquipe();
        team.setId(1);

        List<Collaborator> collaboratorList = new ArrayList<>(Collections.singletonList(BuilderUtil.buildColaborador()));
        collaboratorList.get(0).setId(1);
        String email = "teste@gmai.com";

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        var response = service.deleteCollaborator(1, new ArrayList<>(Collections.singletonList(email)));
        Assertions.assertNull(response);
    }
}
