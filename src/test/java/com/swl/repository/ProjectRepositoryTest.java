package com.swl.repository;

import com.swl.models.Project;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;


@DataJpaTest
public class ProjectRepositoryTest {


    @Autowired
    private ProjectRepository repository;


    @Test
    public void createProject() {
        Project project = BuilderUtil.buildProject();

        repository.save(project);
        var response = repository.findAll();
        Assertions.assertEquals(response.size(), 1);

    }


    @Test
    public void deleteProject() {
        Project project = BuilderUtil.buildProject();

        project = repository.save(project);
        repository.delete(project);

        var response = repository.findAll().size();
        Assertions.assertEquals(response, 0);

    }


    @Test
    public void searchProject() {
        Project project = BuilderUtil.buildProject();

        project = repository.save(project);
        Optional<Project> enderecoFound = repository.findById(project.getId());

        Assertions.assertTrue(enderecoFound.isPresent());
    }


    @Test
    public void updateProject() {
        Project project = BuilderUtil.buildProject();
        project = repository.save(project);

        Project projectUpdate = BuilderUtil.buildProject();
        projectUpdate.setId(project.getId());
        projectUpdate.setName("Projeto Update");

        repository.save(projectUpdate);
        Optional<Project> responseFinal = repository.findById(project.getId());
        Assertions.assertEquals(responseFinal.get().getName(), "Projeto Update");
    }

}
