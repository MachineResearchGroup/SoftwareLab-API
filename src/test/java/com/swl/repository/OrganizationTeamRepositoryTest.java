package com.swl.repository;

import com.swl.models.Collaborator;
import com.swl.models.Team;
import com.swl.models.Organization;
import com.swl.models.OrganizationTeam;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
public class OrganizationTeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OrganizacaoRepository organizacaoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollaboratorRepository collaboratorRepository;

    @Autowired
    private OrganizationTeamRepository repository;


    @Test
    public void createOrganizationTeam() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = organizacaoRepository.save(organization);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .build();

        repository.save(organizationTeam);

        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteOrganizationTeam() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = organizacaoRepository.save(organization);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .build();

        organizationTeam = repository.save(organizationTeam);
        repository.delete(organizationTeam);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchOrganizationTeam() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = organizacaoRepository.save(organization);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .build();

        organizationTeam = repository.save(organizationTeam);
        Optional<OrganizationTeam> enderecoFound = repository.findById(organizationTeam.getId());

        Assertions.assertTrue(enderecoFound.isPresent());
    }


    @Test
    public void updateOrganizationTeam() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = organizacaoRepository.save(organization);

        Team team = BuilderUtil.buildEquipe();
        team = teamRepository.save(team);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .team(team)
                .build();

        organizationTeam = repository.save(organizationTeam);

        OrganizationTeam organizationTeamUpdate = OrganizationTeam.builder()
                .id(organizationTeam.getId())
                .organization(organization)
                .team(null)
                .build();

        repository.save(organizationTeamUpdate);
        Optional<OrganizationTeam> responseFinal = repository.findById(organizationTeam.getId());
        Assertions.assertNull(responseFinal.get().getTeam());
    }


    @Test
    public void findByTeamId() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = organizacaoRepository.save(organization);

        Team team = BuilderUtil.buildEquipe();
        team = teamRepository.save(team);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .team(team)
                .build();

        repository.save(organizationTeam);

        Optional<List<OrganizationTeam>> organizacaoResponse = repository.findAllByTeamId(team.getId());
        Assertions.assertTrue(organizacaoResponse.isPresent());
    }


    @Test
    public void findByTeamIdAndCollaboratorId() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = organizacaoRepository.save(organization);

        Team team = BuilderUtil.buildEquipe();
        team = teamRepository.save(team);

        Collaborator col = BuilderUtil.buildColaborador();
        col.setUser(userRepository.save(col.getUser()));
        col = collaboratorRepository.save(col);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .team(team)
                .collaborator(col)
                .build();
        repository.save(organizationTeam);

        Optional<OrganizationTeam> organizacaoResponse = repository.findByTeamIdAndCollaboratorId(team.getId(), col.getId());
        Assertions.assertTrue(organizacaoResponse.isPresent());
    }

}
