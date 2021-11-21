package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Collaborator;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CollaboratorService {

    @Autowired
    private final CollaboratorRepository repository;

    @Autowired
    private final UserService userService;


    public Collaborator getCollaborator() {

        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            Optional<Collaborator> collaborator = repository.findCollaboratorByUserId(((Collaborator)
                    userService.getCurrentUser().get()).getUser().getId());
            return collaborator.get();
        }

        throw new NotFoundException(Collaborator.class);
    }

}
