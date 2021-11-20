package com.swl.service;

import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Team;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.repository.ClientRepository;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final ClientRepository clientRepository;


    @Transactional
    public Optional getCurrentUser() {
        Optional<User> userOptional = Optional.empty();

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            userOptional = userRepository.findByUsername(((UserDetails) principal).getUsername());
        }

        if (userOptional.isPresent()) {
            Optional<Collaborator> collaboratorOptional = collaboratorRepository.findCollaboratorByUserId(userOptional.get().getId());

            if (collaboratorOptional.isPresent()) {
                return collaboratorOptional;
            } else {
                Optional<Client> clientOptional = clientRepository.findClientByUserId(userOptional.get().getId());
                if (clientOptional.isPresent()) {
                    return clientOptional;
                }
            }
        }

        return userOptional;
    }


    @Transactional
    public User saveUser(User user){
        return userRepository.save(user);
    }


    @Transactional
    public User getUser(Integer idUser) {
        Optional<User> user = userRepository.findById(idUser);
        if (user.isPresent()) {
            return user.get();
        } else {
            throw new NotFoundException(User.class);
        }
    }
}
