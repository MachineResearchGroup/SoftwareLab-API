package com.swl.service;

import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Project;
import com.swl.models.project.RedactionShedule;
import com.swl.payload.request.RedactionSheduleRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RedactionSheduleRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RedactionSheduleService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final RedactionSheduleRepository repository;


    public void verifyRedactionSheduled(RedactionSheduleRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        RedactionShedule redaction = new RedactionShedule();

        if (projectRepository.findById(redactionRequest.getIdProject()).isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        if (collaboratorRepository.findById(redactionRequest.getIdCollaborator()).isEmpty()) {
            throw new NotFoundException(Collaborator.class);
        }

        modelUtil.map(redactionRequest, redaction);
        ErrorResponse error = modelUtil.validate(redaction);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    public RedactionShedule registerRedactionShedule(RedactionSheduleRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Project> project = projectRepository.findById(redactionRequest.getIdProject());
        Optional<Collaborator> collaborator = collaboratorRepository.findById(redactionRequest.getIdCollaborator());

        if (project.isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        if (collaborator.isEmpty()) {
            throw new NotFoundException(Collaborator.class);
        }

        RedactionShedule redactionShedule = getRedactionShedule(project.get().getId());

        if (Objects.isNull(redactionShedule)) {
            redactionShedule = new RedactionShedule();
            modelUtil.map(redactionRequest, redactionShedule);

            redactionShedule.setProject(project.get());
        } else {
            modelUtil.map(redactionRequest, redactionShedule);
        }
        redactionShedule.setCollaborator(collaborator.get());

        return repository.save(redactionShedule);

    }


    public RedactionShedule getRedactionShedule(Integer idProject) {
        Optional<RedactionShedule> redactionShedule = repository.findById(idProject);
        if (redactionShedule.isPresent()) {
            return redactionShedule.get();
        } else {
            throw new NotFoundException(RedactionShedule.class);
        }
    }


    public void deleteRedactionShedule(Integer idRedaction) {
        RedactionShedule redactionShedule = getRedactionShedule(idRedaction);
        repository.deleteById(redactionShedule.getId());
    }


}
