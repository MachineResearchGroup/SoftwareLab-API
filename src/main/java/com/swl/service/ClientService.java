package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClientService {

    @Autowired
    private final ClientRepository repository;

    @Autowired
    private final UserService userService;


    public Client getClient() {

        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            Optional<Client> collaborator = repository.findClientByUserId(((Collaborator) userService.getCurrentUser().get()).getId());
            return collaborator.get();
        }

        throw new NotFoundException(Client.class);
    }

}
