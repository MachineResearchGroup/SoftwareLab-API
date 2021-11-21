package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.*;
import com.swl.payload.request.RequirementRequest;
import com.swl.payload.request.TaskRequest;
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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("RequirementServiceTest")
@ExtendWith(MockitoExtension.class)
public class RequirementServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private RequirementRepository repository;

    private RequirementService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new RequirementService(projectRepository, repository);
    }


    @Test
    public void registerRequirement_Sucessfully() {
        Requirement rec = BuilderUtil.buildRequirement();
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        RequirementRequest requirementRequest = RequirementRequest.builder()
                .description(rec.getDescription())
                .category(rec.getCategory())
                .idProject(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(repository.save(rec)).thenReturn(rec);

        var response = service.registerRequirement(requirementRequest);
        Assertions.assertEquals(response, rec);
    }


    @Test
    public void registerRequirement_Error() {
        Requirement rec = BuilderUtil.buildRequirement();
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        RequirementRequest requirementRequest = RequirementRequest.builder()
                .description(rec.getDescription())
                .category(rec.getCategory())
                .idProject(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.registerRequirement(requirementRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());

    }


    @Test
    public void editRequirement_Sucessfully() {
        Requirement rec = BuilderUtil.buildRequirement();
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        RequirementRequest requirementRequest = RequirementRequest.builder()
                .description(rec.getDescription())
                .category(rec.getCategory())
                .idProject(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(rec));
        Mockito.when(repository.save(rec)).thenReturn(rec);

        var response = service.editRequirement(1, requirementRequest);
        Assertions.assertEquals(response, rec);
    }


    @Test
    public void editRequirement_Error() {
        Requirement rec = BuilderUtil.buildRequirement();
        Project project = BuilderUtil.buildProject();
        project.setId(1);

        RequirementRequest requirementRequest = RequirementRequest.builder()
                .description(rec.getDescription())
                .category(rec.getCategory())
                .idProject(1)
                .build();

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());
        Mockito.when(repository.findById(1)).thenReturn(Optional.of(rec));

        thrownException.set(false);
        try {
            service.editRequirement(1, requirementRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getRequirement_Sucessfully() {
        Requirement rec = BuilderUtil.buildRequirement();
        rec.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(rec));
        var response = service.getRequirement(1);

        Assertions.assertEquals(response, rec);
    }


    @Test
    public void getRequirement_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getRequirement(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getAllRequirementByProject_Sucessfully() {
        Requirement rec = BuilderUtil.buildRequirement();
        Project project = BuilderUtil.buildProject();
        project.setId(1);
        project.setRequirements(new ArrayList<>(Collections.singletonList(rec)));

        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.of(project));
        Mockito.when(repository.findAllByProjectId(1)).thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(rec))));

        var response = service.getAllRequirementByProject(1);
        Assertions.assertEquals(response.get(0), rec);
    }


    @Test
    public void getAllRequirementByProject_Error() {
        Mockito.when(projectRepository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.getAllRequirementByProject(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void deleteRequirement_Sucessfully() {
        Requirement rec = BuilderUtil.buildRequirement();
        rec.setId(1);
        Project project = BuilderUtil.buildProject();
        project.setId(1);
        project.setRequirements(new ArrayList<>(Collections.singletonList(rec)));

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(rec));
        Mockito.when(projectRepository.findAllByRequirementId(1)).thenReturn(
                Optional.of(new ArrayList<>(Collections.singletonList(project))));

        service.deleteRequirement(1);
    }

}
