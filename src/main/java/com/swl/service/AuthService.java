package com.swl.service;

import com.swl.config.security.jwt.JwtUtils;
import com.swl.config.security.services.UserDetailsImpl;
import com.swl.exceptions.business.AlreadyExistsException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.enums.FunctionEnum;
import com.swl.models.enums.MessageEnum;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.payload.request.ClientRequest;
import com.swl.payload.request.CollaboratorRequest;
import com.swl.payload.request.LoginRequest;
import com.swl.payload.request.RegisterRequest;
import com.swl.payload.response.JwtResponse;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.ClientRepository;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CollaboratorRepository collaboratorRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;


    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }


    public ResponseEntity<?> registerUser(RegisterRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            throw new AlreadyExistsException("username");
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw new AlreadyExistsException("email");
        }

        // Create new user's account
        User user;
        if (Objects.isNull(signUpRequest.getPhones()) || signUpRequest.getPhones().isEmpty()) {
            user = new User(
                    signUpRequest.getName(),
                    signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()));
        } else {
            user = new User(
                    signUpRequest.getName(),
                    signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    encoder.encode(signUpRequest.getPassword()),
                    signUpRequest.getPhones());
        }

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, User.class));

    }


    public ResponseEntity<?> registerCollaborator(CollaboratorRequest collaboratorRequest) {
        Optional<User> usuario = userRepository.findByEmail(collaboratorRequest.getEmail());
        if (usuario.isEmpty()) {
            throw new NotFoundException("email");
        }

        if (collaboratorRepository.findCollaboratorByUserId(usuario.get().getId()).isPresent()) {
            throw new AlreadyExistsException(Collaborator.class);
        }

        if (clientRepository.findClientByUserId(usuario.get().getId()).isPresent()) {
            throw new AlreadyExistsException(Client.class);
        }

        // Create new collaborator account
        if (FunctionEnum.exists(collaboratorRequest.getFunction())) {

            Collaborator collaborator = new Collaborator(collaboratorRequest.getRegister(), collaboratorRequest.getFunction());
            collaborator.setUser(usuario.get());

            collaboratorRepository.save(collaborator);

            return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, Collaborator.class));
        } else {
            throw new NotFoundException("function");
        }
    }


    public ResponseEntity<?> registerClient(ClientRequest clientRequest) {
        Optional<User> usuario = userRepository.findByEmail(clientRequest.getEmail());
        if (usuario.isEmpty()) {
            throw new NotFoundException("email");
        }

        if (collaboratorRepository.findCollaboratorByUserId(usuario.get().getId()).isPresent()) {
            throw new AlreadyExistsException(Collaborator.class);
        }

        if (clientRepository.findClientByUserId(usuario.get().getId()).isPresent()) {
            throw new AlreadyExistsException(Client.class);
        }

        // Create new client account
        Client client = Objects.isNull(clientRequest.getSegments()) ||
                clientRequest.getSegments().isEmpty() ?
                new Client(clientRequest.getCorporateName()) :
                new Client(clientRequest.getCorporateName(), clientRequest.getSegments());

        client.setUser(usuario.get());

        clientRepository.save(client);

        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, Client.class));
    }
}
