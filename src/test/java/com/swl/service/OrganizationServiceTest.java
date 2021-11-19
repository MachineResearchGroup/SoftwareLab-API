package com.swl.service;

import com.swl.models.management.Organization;
import com.swl.models.people.Collaborator;
import com.swl.payload.request.AddessRequest;
import com.swl.payload.request.OrganizationRequest;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.OrganizationRepository;
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

import java.util.*;


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

        var response = service.editOrganization(1, registerRequest);
        Assertions.assertNull(response);
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

        var response = service.getOrganization(1);
        Assertions.assertNull(response);
    }


    @Test
    public void getCollaborators_Sucessfully() {
        Mockito.when(collaboratorRepository.findAllCollaboratorByOrganizationId(1))
                .thenReturn(Optional.of(new ArrayList<>(Collections.singletonList(Mockito.mock(Collaborator.class)))));

        var response = service.getCollaborators(1);
        Assertions.assertEquals(response.size(), 1);
    }


    @Test
    public void deleteOrganization_Sucessfully() {
        Mockito.when(repository.existsById(1)).thenReturn(true);
        service.deleteOrganization(1);

//        Assertions.assertTrue(response);
    }


    @Test
    public void deleteOrganization_Error() {
        Mockito.when(repository.existsById(1)).thenReturn(false);

        service.deleteOrganization(1);
//        Assertions.assertFalse(response);
    }

}
