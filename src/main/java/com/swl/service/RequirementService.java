package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Team;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.models.project.Requirement;
import com.swl.payload.request.RequirementRequest;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RequirementRepository;
import com.swl.util.CopyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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


    public Requirement registerRequirement(RequirementRequest requirementRequest) {
        Optional<Project> project = projectRepository.findById(requirementRequest.getIdProject());

        if (project.isPresent()) {
            Requirement requirement = new Requirement();
            CopyUtil.copyProperties(requirementRequest, requirement);

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
        Optional<Project> project = projectRepository.findById(requirementRequest.getIdProject());

        if (project.isPresent()) {
            CopyUtil.copyProperties(requirementRequest, redactionAux);
            return repository.save(redactionAux);
        } else {
            throw new NotFoundException(Project.class);
        }
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
        Optional<List<Project>> project = projectRepository.findAllByRequirementId(requirement.getId());
        project.ifPresent(projects -> projects.forEach(p -> {
            p.getRequirements().remove(requirement);
            projectRepository.save(p);
        }));
        repository.deleteById(requirement.getId());
    }

}
