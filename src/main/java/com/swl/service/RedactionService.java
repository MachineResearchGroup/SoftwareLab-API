package com.swl.service;

import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Project;
import com.swl.models.project.Redaction;
import com.swl.models.project.RedactionShedule;
import com.swl.models.user.Client;
import com.swl.payload.request.RedactionRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.ClientRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RedactionRepository;
import com.swl.repository.RedactionSheduleRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RedactionService {

    @Autowired
    private final ProjectRepository projectRepository;

    @Autowired
    private final ClientRepository clientRepository;

    @Autowired
    private final RedactionSheduleRepository redactionSheduleRepository;

    @Autowired
    private final RedactionRepository repository;


    public ResponseEntity<?> verifyRedaction(RedactionRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Redaction redaction = new Redaction();

        modelUtil.map(redactionRequest, redaction);
        List<MessageResponse> messageResponses = modelUtil.validate(redaction);

        if (projectRepository.findById(redactionRequest.getIdProject()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));
        }

        if (clientRepository.findById(redactionRequest.getIdClient()).isEmpty()) {
            messageResponses.add(new MessageResponse(MessageEnum.NOT_FOUND, Client.class));
        }

        if (!messageResponses.isEmpty()) {
            return ResponseEntity.badRequest().body(messageResponses);
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.VALID, Redaction.class));
    }


    public Redaction registerRedaction(RedactionRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Project> project = projectRepository.findById(redactionRequest.getIdProject());
        Optional<Client> client = clientRepository.findById(redactionRequest.getIdClient());

        if (project.isPresent() && client.isPresent()) {
            Optional<RedactionShedule> redactionShedule = redactionSheduleRepository.findByProjectId(project.get().getId());

            if (redactionShedule.isPresent() && verifyShedule(redactionShedule.get())) {

                Redaction redaction = new Redaction();
                modelUtil.map(redactionRequest, redaction);
                redaction.setProject(project.get());
                redaction.setClient(client.get());

                return repository.save(redaction);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }


    public Redaction editRedaction(Integer idRedaction, RedactionRequest redactionRequest) {
        Optional<Redaction> redactionAux = repository.findById(idRedaction);

        if (redactionAux.isPresent()) {
            Optional<RedactionShedule> redactionShedule = redactionSheduleRepository.findByProjectId(redactionAux.get()
                    .getProject().getId());

            if (redactionShedule.isPresent() && verifyShedule(redactionShedule.get())) {
                CopyUtil.copyProperties(redactionRequest, redactionAux);
                return repository.save(redactionAux.get());
            } else {
                return null;
            }
        }
        return null;
    }


    public Redaction getRedaction(Integer idRedaction) {
        return repository.findById(idRedaction).orElse(null);
    }


    public List<Redaction> getAllRedactionByProject(Integer idProject) {
        return repository.findAllByProjectId(idProject).orElse(null);
    }


    public List<Redaction> getAllRedactionByClient(Integer idClient) {
        return repository.findAllByClientId(idClient).orElse(null);
    }


    public boolean deleteRedaction(Integer idRedaction) {
        if (repository.existsById(idRedaction)) {
            repository.deleteById(idRedaction);
            return true;
        }
        return false;
    }


    private boolean verifyShedule(RedactionShedule redactionShedule) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(redactionShedule.getInitDate()) && now.isBefore(redactionShedule.getEndDate());
    }


}
