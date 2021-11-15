package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Project;
import com.swl.models.project.RedactionShedule;
import com.swl.models.user.Collaborator;
import com.swl.payload.request.RedactionSheduleRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RedactionSheduleRepository;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
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


    public ResponseEntity<?> verifyRedactionSheduled(RedactionSheduleRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        RedactionShedule redaction = new RedactionShedule();

        modelUtil.map(redactionRequest, redaction);
        List<MessageResponse> messageResponses = modelUtil.validate(redaction);

        if (projectRepository.findById(redactionRequest.getIdProject()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));
        }

        if (collaboratorRepository.findById(redactionRequest.getIdCollaborator()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, CollaboratorRepository.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, RedactionShedule.class));
    }


    public RedactionShedule registerRedactionShedule(RedactionSheduleRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Project> project = projectRepository.findById(redactionRequest.getIdProject());
        Optional<Collaborator> collaborator = collaboratorRepository.findById(redactionRequest.getIdCollaborator());

        if (project.isPresent() && collaborator.isPresent()) {
            RedactionShedule redactionShedule = getRedactionShedule(project.get().getId());

            if(Objects.isNull(redactionShedule)) {
                redactionShedule = new RedactionShedule();
                modelUtil.map(redactionRequest, redactionShedule);

                redactionShedule.setProject(project.get());
            }else{
                modelUtil.map(redactionRequest, redactionShedule);
            }
            redactionShedule.setCollaborator(collaborator.get());

            return repository.save(redactionShedule);
        } else {
            return null;
        }
    }


    public RedactionShedule getRedactionShedule(Integer idProject) {
        return repository.findByProjectId(idProject).orElse(null);
    }


    public boolean deleteRedactionShedule(Integer idRedaction) {
        if (repository.existsById(idRedaction)) {
            repository.deleteById(idRedaction);
            return true;
        }
        return false;
    }


}
