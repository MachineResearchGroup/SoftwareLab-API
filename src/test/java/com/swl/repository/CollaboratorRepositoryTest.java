package com.swl.repository;

import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.management.Team;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;


@DataJpaTest
public class CollaboratorRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private CollaboratorRepository repository;


    @Test
    public void createCollaborator() {
        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));

        repository.save(col);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteCollaborator() {
        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));

        col = repository.save(col);
        repository.delete(col);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchCollaborator() {
        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));

        col = repository.save(col);
        Optional<Collaborator> clienteFound = repository.findById(col.getId());

        Assertions.assertTrue(clienteFound.isPresent());
    }


    @Test
    public void updateCollaborator() {
        Collaborator col = BuilderUtil.buildCollaborator();
        User user = userRepository.save(col.getUser());
        col.setUser(user);

        col = repository.save(col);

        Collaborator colUpdate = BuilderUtil.buildCollaborator();
        colUpdate.setId(col.getId());
        colUpdate.setUser(user);
        colUpdate.setFunction("ROLE_PO");

        repository.save(colUpdate);
        Optional<Collaborator> responseFinal = repository.findById(col.getId());
        Assertions.assertEquals(responseFinal.get().getFunction(), "ROLE_PO");
    }


    @Test
    public void findCollaboratorByUserId() {
        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));

        repository.save(col);

        Optional<Collaborator> clienteResponse = repository.findCollaboratorByUserId(col.getUser().getId());
        Assertions.assertEquals(col.getFunction(), clienteResponse.get().getFunction());
    }


    @Test
    public void findCollaboratorByUserEmail() {
        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));

        repository.save(col);

        Optional<Collaborator> clienteResponse = repository.findCollaboratorByUserEmail(col.getUser().getEmail());
        Assertions.assertTrue(clienteResponse.isPresent());
    }


    @Test
    public void findAllCollaboratorByOrganizationId() {
        Organization organization = BuilderUtil.buildOrganization();
        organization = organizationRepository.save(organization);

        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));
        col = repository.save(col);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .collaborator(col)
                .build();
        organizationTeamRepository.save(organizationTeam);

        Optional<List<Collaborator>> colResponse = repository.findAllCollaboratorByOrganizationId(organization.getId());
        Assertions.assertTrue(colResponse.isPresent());
    }


    @Test
    public void findAllCollaboratorByTeamId() {
        Organization organization = BuilderUtil.buildOrganization();
        organization = organizationRepository.save(organization);

        Team team = BuilderUtil.buildTeam();
        team = teamRepository.save(team);

        Collaborator col = BuilderUtil.buildCollaborator();
        col.setUser(userRepository.save(col.getUser()));
        col = repository.save(col);

        OrganizationTeam organizationTeam = OrganizationTeam.builder()
                .organization(organization)
                .team(team)
                .collaborator(col)
                .build();
        organizationTeamRepository.save(organizationTeam);

        Optional<List<Collaborator>> colResponse = repository.findAllCollaboratorByTeamId(team.getId());
        Assertions.assertTrue(colResponse.isPresent());
    }
}
