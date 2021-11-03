package com.swl.config.security.services;

import com.swl.models.Client;
import com.swl.models.Collaborator;
import com.swl.models.User;
import com.swl.repository.ClientRepository;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    ClientRepository clientRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with email: " + email));

        Optional<Collaborator> colaborador = collaboratorRepository.findCollaboratorByUserId(user.getId());

        if (colaborador.isPresent()) {
            return UserDetailsImpl.build(colaborador.get());
        } else {
            Optional<Client> cliente = clientRepository.findClientByUserId(user.getId());
            if (cliente.isPresent())
                return UserDetailsImpl.build(cliente.get());
        }

        return UserDetailsImpl.build(user);
    }

}
