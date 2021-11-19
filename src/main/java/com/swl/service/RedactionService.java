package com.swl.service;

import com.swl.exceptions.business.BlockedAdditionException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Client;
import com.swl.models.project.Board;
import com.swl.models.project.Project;
import com.swl.models.project.Redaction;
import com.swl.models.project.RedactionShedule;
import com.swl.payload.request.RedactionRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.ClientRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RedactionRepository;
import com.swl.repository.RedactionSheduleRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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


    public void verifyRedaction(RedactionRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Redaction redaction = new Redaction();

        if (projectRepository.findById(redactionRequest.getIdProject()).isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        if (clientRepository.findById(redactionRequest.getIdClient()).isEmpty()) {
            throw new NotFoundException(Client.class);
        }

        modelUtil.map(redactionRequest, redaction);
        ErrorResponse error = modelUtil.validate(redaction);

        if (!Objects.isNull(error)) {
            throw new InvalidFieldException(error);
        }
    }


    public Redaction registerRedaction(RedactionRequest redactionRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Optional<Project> project = projectRepository.findById(redactionRequest.getIdProject());
        Optional<Client> client = clientRepository.findById(redactionRequest.getIdClient());

        if (project.isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        if (client.isEmpty()) {
            throw new NotFoundException(Client.class);
        }

        Optional<RedactionShedule> redactionShedule = redactionSheduleRepository.findByProjectId(project.get().getId());

        if (redactionShedule.isPresent() && verifyShedule(redactionShedule.get())) {

            Redaction redaction = new Redaction();
            modelUtil.map(redactionRequest, redaction);
            redaction.setProject(project.get());
            redaction.setClient(client.get());

            return repository.save(redaction);
        } else {
            throw new BlockedAdditionException(Redaction.class);
        }
    }


    public Redaction editRedaction(Integer idRedaction, RedactionRequest redactionRequest) {
        Redaction redactionAux = getRedaction(idRedaction);

        Optional<RedactionShedule> redactionShedule = redactionSheduleRepository.findByProjectId(redactionAux
                .getProject().getId());

        if (redactionShedule.isPresent() && verifyShedule(redactionShedule.get())) {
            CopyUtil.copyProperties(redactionRequest, redactionAux);
            return repository.save(redactionAux);
        } else {
            throw new BlockedAdditionException(Redaction.class);
        }
    }


    public Redaction getRedaction(Integer idRedaction) {
        Optional<Redaction> redaction = repository.findById(idRedaction);
        if (redaction.isPresent()) {
            return redaction.get();
        } else {
            throw new NotFoundException(Board.class);
        }
    }


    public List<Redaction> getAllRedactionByProject(Integer idProject) {
        Optional<Project> project = projectRepository.findById(idProject);
        if (project.isPresent()) {
            return repository.findAllByProjectId(idProject).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Project.class);
    }


    public List<Redaction> getAllRedactionByClient(Integer idClient) {
        Optional<Client> project = clientRepository.findById(idClient);
        if (project.isPresent()) {
            return repository.findAllByClientId(idClient).orElseGet(ArrayList::new);
        }
        throw new NotFoundException(Client.class);
    }


    public void deleteRedaction(Integer idRedaction) {
        Redaction redaction = getRedaction(idRedaction);
        repository.deleteById(redaction.getId());
    }


    private boolean verifyShedule(RedactionShedule redactionShedule) {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(redactionShedule.getInitDate()) && now.isBefore(redactionShedule.getEndDate());
    }

}
