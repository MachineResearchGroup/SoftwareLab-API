package com.swl.service;

import com.swl.exceptions.business.AlreadyExistsException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.models.project.History;
import com.swl.models.project.Project;
import com.swl.models.project.RedactionShedule;
import com.swl.payload.request.RedactionSheduleRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.ProjectRepository;
import com.swl.repository.RedactionSheduleRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
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

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    private void verifyRedactionShedule(RedactionSheduleRequest redactionSheduleRequest) {
        formatter = formatter.withLocale(Locale.US);

        try {
            LocalDateTime formattedData = LocalDateTime.parse(redactionSheduleRequest.getInitDate(), formatter);
        } catch (Exception parseException) {
            throw new InvalidFieldException(ErrorResponse.builder()
                    .key("initDate")
                    .build());
        }

        try {
            LocalDateTime formattedData = LocalDateTime.parse(redactionSheduleRequest.getEndDate(), formatter);
        } catch (Exception parseException) {
            throw new InvalidFieldException(ErrorResponse.builder()
                    .key("endDate")
                    .build());
        }

    }


    public RedactionShedule registerRedactionShedule(RedactionSheduleRequest redactionRequest) {
        verifyRedactionShedule(redactionRequest);
        Optional<Project> project = projectRepository.findById(redactionRequest.getIdProject());
        Optional<Collaborator> collaborator = collaboratorRepository.findById(redactionRequest.getIdCollaborator());

        if (project.isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        if (collaborator.isEmpty()) {
            throw new NotFoundException(Collaborator.class);
        }

        if (repository.findByProjectId(project.get().getId()).isEmpty()) {
            RedactionShedule redactionShedule = new RedactionShedule();
            CopyUtil.copyProperties(redactionRequest, redactionShedule);
            redactionShedule.setProject(project.get());

            redactionShedule.setInitDate(LocalDateTime.parse(redactionRequest.getInitDate(), formatter));
            redactionShedule.setEndDate(LocalDateTime.parse(redactionRequest.getEndDate(), formatter));
            redactionShedule.setCollaborator(collaborator.get());

            return repository.save(redactionShedule);
        } else {
            throw new AlreadyExistsException(RedactionShedule.class);
        }
    }


    public RedactionShedule editRedactionShedule(Integer idRedaction, RedactionSheduleRequest redactionRequest) {
        verifyRedactionShedule(redactionRequest);
        Optional<Project> project = projectRepository.findById(redactionRequest.getIdProject());
        Optional<Collaborator> collaborator = collaboratorRepository.findById(redactionRequest.getIdCollaborator());

        if (project.isEmpty()) {
            throw new NotFoundException(Project.class);
        }

        if (collaborator.isEmpty()) {
            throw new NotFoundException(Collaborator.class);
        }

        RedactionShedule redactionShedule = getRedactionShedule(idRedaction);
        CopyUtil.copyProperties(redactionRequest, redactionShedule);
        redactionShedule.setProject(project.get());
        redactionShedule.setInitDate(LocalDateTime.parse(redactionRequest.getInitDate(), formatter));
        redactionShedule.setEndDate(LocalDateTime.parse(redactionRequest.getEndDate(), formatter));
        redactionShedule.setCollaborator(collaborator.get());

        return repository.save(redactionShedule);
    }


    public RedactionShedule getRedactionShedule(Integer idRedaction) {
        Optional<RedactionShedule> redactionShedule = repository.findById(idRedaction);
        if (redactionShedule.isPresent()) {
            return redactionShedule.get();
        } else {
            throw new NotFoundException(RedactionShedule.class);
        }
    }


    public RedactionShedule getRedactionSheduleByProject(Integer idProject) {
        Optional<RedactionShedule> redactionShedule = repository.findByProjectId(idProject);
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
