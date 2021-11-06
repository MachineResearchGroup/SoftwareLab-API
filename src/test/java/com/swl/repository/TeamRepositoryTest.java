package com.swl.repository;

import com.swl.models.management.Team;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class TeamRepositoryTest {

    @Autowired
    private TeamRepository repository;


    @Test
    public void createTeam() {
        Team team = BuilderUtil.buildTeam();

        repository.save(team);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteTeam() {
        Team team = BuilderUtil.buildTeam();

        team = repository.save(team);
        repository.delete(team);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchTeam() {
        Team team = BuilderUtil.buildTeam();

        team = repository.save(team);
        Optional<Team> enderecoFound = repository.findById(team.getId());

        Assertions.assertTrue(enderecoFound.isPresent());
    }


    @Test
    public void updateTeam() {
        Team team = BuilderUtil.buildTeam();

        team = repository.save(team);

        Team teamUpdate = BuilderUtil.buildTeam();
        teamUpdate.setId(team.getId());
        teamUpdate.setName("Equipe Update");

        repository.save(teamUpdate);
        Optional<Team> responseFinal = repository.findById(team.getId());
        Assertions.assertEquals(responseFinal.get().getName(), "Equipe Update");
    }

}
