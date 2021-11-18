package com.swl.service;

import com.swl.models.user.Client;
import com.swl.models.user.Collaborator;
import com.swl.repository.ClientRepository;
import com.swl.repository.UserRepository;
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

        return null;
    }

}
