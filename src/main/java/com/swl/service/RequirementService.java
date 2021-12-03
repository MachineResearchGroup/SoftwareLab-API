package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.*;
import com.swl.payload.request.RequirementRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RequirementRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RequirementService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final RequirementRepository repository;


    private void verifyRequirement(RequirementRequest requirementRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Requirement requirement = new Requirement();

        modelUtil.map(requirementRequest, requirement);
        ErrorResponse error = modelUtil.validate(requirement);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    public Requirement registerRequirement(RequirementRequest requirementRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        verifyRequirement(requirementRequest);
        Optional<Project> project = projectRepository.findById(requirementRequest.getIdProject());

        if (project.isPresent()) {
            Requirement requirement = new Requirement();
            modelUtil.map(requirementRequest, requirement);

            requirement = repository.save(requirement);
            if (Objects.isNull(project.get().getRequirements()))
                project.get().setRequirements(new ArrayList<>());

            project.get().getRequirements().add(requirement);
            projectRepository.save(project.get());

            return requirement;
        } else {
            throw new NotFoundException(Project.class);
        }
    }


    public Requirement editRequirement(Integer idReq, RequirementRequest requirementRequest) {
        Requirement redactionAux = getRequirement(idReq);
        verifyRequirement(requirementRequest);

        Optional<Project> project = projectRepository.findById(requirementRequest.getIdProject());
        if (project.isPresent()) {
            CopyUtil.copyProperties(requirementRequest, redactionAux);
        }else{
            throw new NotFoundException(Project.class);
        }
        return repository.save(redactionAux);
    }


    public Requirement getRequirement(Integer idRequirement) {
        Optional<Requirement> requirement = repository.findById(idRequirement);
        if (requirement.isPresent()) {
            return requirement.get();
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public List<Requirement> getAllRequirementByProject(Integer idProject) {
        Optional<Project> project = projectRepository.findById(idProject);
        if (project.isPresent()) {
            return repository.findAllByProjectId(idProject).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Project.class);
    }


    public void deleteRequirement(Integer idRec) {
        Requirement requirement = getRequirement(idRec);
        repository.deleteById(requirement.getId());
    }

}
