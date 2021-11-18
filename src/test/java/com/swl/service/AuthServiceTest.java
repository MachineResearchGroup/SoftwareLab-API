package com.swl.service;

import com.swl.config.security.jwt.JwtUtils;
import com.swl.config.security.services.UserDetailsImpl;
import com.swl.models.user.Client;
import com.swl.models.user.Collaborator;
import com.swl.models.user.User;
import com.swl.models.enums.FunctionEnum;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.LoginRequest;
import com.swl.payload.request.ClientRequest;
import com.swl.payload.request.CollaboratorRequest;
import com.swl.payload.request.RegisterRequest;
import com.swl.payload.response.JwtResponse;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.*;
import com.swl.util.BuilderUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;
import java.util.stream.Collectors;


@SpringBootTest
@DisplayName("AuthServiceTest")
@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private JwtUtils jwtUtils;

    private AuthService service;


    @BeforeEach
    public void initUseCase() {
        service = new AuthService(authenticationManager, userRepository, collaboratorRepository, clientRepository, encoder, jwtUtils);
    }


    @Test
    public void registerUser_Sucessfully() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("Usuario Teste")
                .username("User")
                .email("user@gmail.com")
                .password("1234")
                .build();

        Mockito.when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        Mockito.when(encoder.encode(registerRequest.getPassword())).thenReturn("abc123");

        var response = service.registerUser(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.REGISTERED, User.class));

        registerRequest = RegisterRequest.builder()
                .name("Usuario Teste")
                .username("User")
                .email("user@gmail.com")
                .password("1234")
                .phones(new ArrayList<>(Collections.singletonList("81987633333")))
                .build();

        Mockito.when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(false);
        Mockito.when(encoder.encode(registerRequest.getPassword())).thenReturn("abc123");

        response = service.registerUser(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.REGISTERED, User.class));
    }


    @Test
    public void registerUser_ErrorUsername() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("Usuario Teste")
                .username("User")
                .email("user@gmail.com")
                .password("1234")
                .build();

        Mockito.when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(true);

        var response = service.registerUser(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.ALREADY_USED, "username"));
    }


    @Test
    public void registerUser_ErrorEmail() {

        RegisterRequest registerRequest = RegisterRequest.builder()
                .name("Usuario Teste")
                .username("User")
                .email("user@gmail.com")
                .password("1234")
                .build();

        Mockito.when(userRepository.existsByUsername(registerRequest.getUsername())).thenReturn(false);
        Mockito.when(userRepository.existsByEmail(registerRequest.getEmail())).thenReturn(true);

        var response = service.registerUser(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.ALREADY_USED, "email"));
    }


    @Test
    public void registerCollaborator_Sucessfully() {
        User user = BuilderUtil.buildUser();

        CollaboratorRequest registerRequest = CollaboratorRequest.builder()
                .email(user.getEmail())
                .register("1234")
                .function("ROLE_PMO")
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var response = service.registerCollaborator(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.REGISTERED, Collaborator.class));
    }


    @Test
    public void registerCollaborator_ErrorEmail() {
        User user = BuilderUtil.buildUser();

        CollaboratorRequest registerRequest = CollaboratorRequest.builder()
                .email(user.getEmail())
                .register("1234")
                .function("ROLE_PMO")
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        var response = service.registerCollaborator(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.NOT_FOUND, "email"));
    }


    @Test
    public void registerCollaborator_ErrorFunction() {
        User user = BuilderUtil.buildUser();

        CollaboratorRequest registerRequest = CollaboratorRequest.builder()
                .email(user.getEmail())
                .register("1234")
                .function("PMO")
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var response = service.registerCollaborator(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.NOT_FOUND, "function"));
    }


    @Test
    public void registerClient_Sucessfully() {
        User user = BuilderUtil.buildUser();

        ClientRequest registerRequest = ClientRequest.builder()
                .email(user.getEmail())
                .corporateName("Cliente ABC")
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        var response = service.registerClient(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.REGISTERED, Client.class));

        registerRequest = ClientRequest.builder()
                .email(user.getEmail())
                .corporateName("Cliente ABC")
                .segments(new ArrayList<>(Collections.singletonList("Machine Learning")))
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        response = service.registerClient(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.REGISTERED, Client.class));
    }


    @Test
    public void registerClient_ErrorEmail() {
        User user = BuilderUtil.buildUser();

        ClientRequest registerRequest = ClientRequest.builder()
                .email(user.getEmail())
                .corporateName("Cliente ABC")
                .build();

        Mockito.when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());

        var response = service.registerClient(registerRequest);

        Assertions.assertEquals(response.getBody(), new MessageResponse(MessageEnum.NOT_FOUND, "email"));
    }


    @Test
    public void authenticateUser_Sucessfully() {
        User user = BuilderUtil.buildUser();

        LoginRequest loginRequest = LoginRequest.builder()
                .email(user.getUsername())
                .password(user.getPassword())
                .build();

        List<GrantedAuthority> authorities = new ArrayList<>(Collections.singletonList(
                new SimpleGrantedAuthority(FunctionEnum.ROLE_USER.name())));

        Authentication authentication = Mockito.mock(Authentication.class);

        UserDetailsImpl userDetails = UserDetailsImpl.builder()
                .id(1)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(authorities)
                .build();

        Mockito.when(authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())))
                .thenReturn(authentication);

        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("abc123");
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        var response = service.authenticateUser(loginRequest);

        Assertions.assertEquals(response.getBody(), new JwtResponse("abc123",
                userDetails.getId(),
                userDetails.getName(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

}
