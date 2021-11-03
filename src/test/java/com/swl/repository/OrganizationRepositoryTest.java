package com.swl.repository;

import com.swl.models.Team;
import com.swl.models.Organization;
import com.swl.models.OrganizationTeam;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class OrganizationRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private OrganizacaoRepository repository;


    @Test
    public void createOrganization() {
        Organization organization = BuilderUtil.buildOrganizacao();

        repository.save(organization);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteOrganization() {
        Organization organization = BuilderUtil.buildOrganizacao();

        organization = repository.save(organization);
        repository.delete(organization);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchOrganization() {
        Organization organization = BuilderUtil.buildOrganizacao();

        organization = repository.save(organization);
        Optional<Organization> enderecoFound = repository.findById(organization.getId());

        Assertions.assertTrue(enderecoFound.isPresent());
    }


    @Test
    public void updateOrganization() {
        Organization organization = BuilderUtil.buildOrganizacao();

        organization = repository.save(organization);

        Organization organizationUpdate = BuilderUtil.buildOrganizacao();
        organizationUpdate.setId(organization.getId());
        organizationUpdate.setName("Organizacao Update");

        repository.save(organizationUpdate);
        Optional<Organization> responseFinal = repository.findById(organization.getId());
        Assertions.assertEquals(responseFinal.get().getName(), "Organizacao Update");
    }


    @Test
    public void findByTeamId() {
        Organization organization = BuilderUtil.buildOrganizacao();
        organization = repository.save(organization);

        Team team = BuilderUtil.buildEquipe();
        team = teamRepository.save(team);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .team(team)
                .build();
        organizationTeamRepository.save(organizationTeam);

        Optional<Organization> organizacaoResponse = repository.findOrganizacaoByEquipeId(team.getId());
        Assertions.assertTrue(organizacaoResponse.isPresent());
    }

}
