package com.swl.service;

import com.swl.exceptions.business.AlreadyExistsException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Organization;
import com.swl.models.people.Collaborator;
import com.swl.payload.request.AddessRequest;
import com.swl.payload.request.OrganizationRequest;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.OrganizationRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.util.BuilderUtil;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;


@SpringBootTest
@DisplayName("OrganizationServiceTest")
@ExtendWith(MockitoExtension.class)
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository repository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private OrganizationTeamRepository organizationTeamRepository;

    @Mock
    private UserService userService;

    private OrganizationService service;

    AtomicBoolean thrownException = new AtomicBoolean(false);

    @BeforeEach
    public void initUseCase() {
        service = new OrganizationService(repository, collaboratorRepository, organizationTeamRepository, userService);
    }


    @Test
    public void registerOrganization_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        Organization organization = BuilderUtil.buildOrganization();

        OrganizationRequest registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(null)
                .build();

        organization.setSupervisor(collaborator);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.save(organization)).thenReturn(organization);

        var response = service.registerOrganization(registerRequest);
        Assertions.assertEquals(response, organization);

        organization = BuilderUtil.buildOrganizationWithAddress();

        registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(AddessRequest.builder()
                        .street(organization.getAddress().getStreet())
                        .city(organization.getAddress().getCity())
                        .complement(organization.getAddress().getComplement())
                        .state(organization.getAddress().getState())
                        .number(organization.getAddress().getNumber())
                        .build())
                .build();

        organization.setSupervisor(collaborator);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.save(organization)).thenReturn(organization);

        response = service.registerOrganization(registerRequest);
        Assertions.assertEquals(response, organization);
    }


    @Test
    public void registerOrganization_ErrorField() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        Organization organization = BuilderUtil.buildOrganization();

        OrganizationRequest registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(null)
                .build();

        organization.setSupervisor(collaborator);
        Mockito.when(repository.findOrganizationByCnpj(organization.getCnpj())).thenReturn(Optional.of(organization));

        thrownException.set(false);
        try {
            service.registerOrganization(registerRequest);
        } catch (AlreadyExistsException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void registerOrganization_AlreadyExists() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        Organization organization = BuilderUtil.buildOrganization();

        OrganizationRequest registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(null)
                .build();

        organization.setSupervisor(collaborator);

        thrownException.set(false);
        try {
            service.registerOrganization(registerRequest);
        } catch (InvalidFieldException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void editOrganization_Sucessfully() {
        Organization organization = BuilderUtil.buildOrganization();

        OrganizationRequest registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(null)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(organization));
        Mockito.when(repository.save(organization)).thenReturn(organization);

        var response = service.editOrganization(1, registerRequest);
        Assertions.assertEquals(response, organization);

        organization = BuilderUtil.buildOrganizationWithAddress();

        registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(AddessRequest.builder()
                        .street(organization.getAddress().getStreet())
                        .city(organization.getAddress().getCity())
                        .complement(organization.getAddress().getComplement())
                        .state(organization.getAddress().getState())
                        .number(organization.getAddress().getNumber())
                        .build())
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(organization));
        Mockito.when(repository.save(organization)).thenReturn(organization);

        response = service.editOrganization(1, registerRequest);
        Assertions.assertEquals(response, organization);
    }


    @Test
    public void editOrganization_Error() {
        Organization organization = BuilderUtil.buildOrganization();

        OrganizationRequest registerRequest = OrganizationRequest.builder()
                .name(organization.getName())
                .cnpj(organization.getCnpj())
                .address(null)
                .build();

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.editOrganization(1, registerRequest);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getOrganization_Sucessfully() {
        Organization organization = BuilderUtil.buildOrganization();

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(organization));

        var response = service.getOrganization(1);
        Assertions.assertEquals(response, organization);
    }


    @Test
    public void getOrganization_Error() {
        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());
        thrownException.set(false);
        try {
            service.getOrganization(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }


    @Test
    public void getCollaborators_Sucessfully() {
        Organization organization = BuilderUtil.buildOrganizationWithAddress();
        organization.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(organization));
        Mockito.when(collaboratorRepository.findAllCollaboratorByOrganizationId(1))
                .thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)))));

        var response = service.getCollaborators(1);
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void getOrganizationsByCollaborator_Sucessfully() {
        Collaborator collaborator = BuilderUtil.buildCollaborator();
        Organization organization = BuilderUtil.buildOrganizationWithAddress();
        collaborator.setId(1);
        organization.setId(1);

        Mockito.when(userService.getCurrentUser()).thenReturn(Optional.of(collaborator));
        Mockito.when(repository.findOrganizationByCollaboratorId(1)).thenReturn(
                Optional.of(new ArrayList<>(Collections.singletonList(organization))));

        var response = service.getOrganizationsByCollaborator();
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void deleteOrganization_Sucessfully() {
        Organization organization = BuilderUtil.buildOrganizationWithAddress();
        organization.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.of(organization));
        service.deleteOrganization(1);
    }


    @Test
    public void deleteOrganization_Error() {
        Organization organization = BuilderUtil.buildOrganizationWithAddress();
        organization.setId(1);

        Mockito.when(repository.findById(1)).thenReturn(Optional.empty());

        thrownException.set(false);
        try {
            service.deleteOrganization(1);
        } catch (NotFoundException e) {
            thrownException.set(true);
        }
        Assertions.assertTrue(thrownException.get());
    }

}
