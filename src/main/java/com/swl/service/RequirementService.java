package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Project;
import com.swl.models.project.Redaction;
import com.swl.models.project.Requirement;
import com.swl.payload.request.RequirementRequest;
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


    public ResponseEntity<?> verifyRequirement(RequirementRequest requirementRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Redaction redaction = new Redaction();

        modelUtil.map(requirementRequest, redaction);
        List<MessageResponse> messageResponses = modelUtil.validate(redaction);

        if (projectRepository.findById(requirementRequest.getIdProject()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Requirement.class));
    }


    public Requirement registerRequirement(RequirementRequest requirementRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
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
            return null;
        }
    }


    public Requirement editRequirement(Integer idReq, RequirementRequest requirementRequest) {
        Optional<Requirement> redactionAux = repository.findById(idReq);

        if (redactionAux.isPresent()) {
            CopyUtil.copyProperties(requirementRequest, redactionAux);
            return repository.save(redactionAux.get());
        }
        return null;
    }


    public Requirement getRequirement(Integer idRequirement) {
        return repository.findById(idRequirement).orElse(null);
    }


    public List<Requirement> getAllRequirementByProject(Integer idProject) {
        return repository.findAllByProjectId(idProject).orElse(null);
    }


    public boolean deleteRequirement(Integer idRedaction) {
        if (repository.existsById(idRedaction)) {
            repository.deleteById(idRedaction);
            return true;
        }
        return false;
    }

}
