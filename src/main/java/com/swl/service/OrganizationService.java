package com.swl.service;

import com.swl.exceptions.business.AlreadyExistsException;
import com.swl.exceptions.business.InvalidFieldException;
import com.swl.exceptions.business.NotFoundException;
import com.swl.models.management.Address;
import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.payload.request.OrganizationRequest;
import com.swl.payload.response.ErrorResponse;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.OrganizationRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.util.CopyUtil;
import com.swl.util.ModelUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrganizationService {

    @Autowired
    private final OrganizationRepository repository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private final UserService userService;


    public void verifyOrganization(OrganizationRequest registerRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Organization organization = new Organization();

        modelUtil.map(registerRequest, organization);
        ErrorResponse error = modelUtil.validate(organization);

        if(!Objects.isNull(error)){
            throw new InvalidFieldException(error);
        }
    }


    @Transactional
    public Organization registerOrganization(OrganizationRequest registerRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();
        Organization organization = new Organization();
        OrganizationTeam organizationTeam = new OrganizationTeam();

        if (repository.findOrganizationByCnpj(registerRequest.getCnpj()).isPresent()) {
            throw new AlreadyExistsException("cnpj");
        }

        modelUtil.map(registerRequest, organization);

        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            organization.setSupervisor((Collaborator) userService.getCurrentUser().get());
            organizationTeam.setCollaborator((Collaborator) userService.getCurrentUser().get());
        }
        organization.setAddress(null);
        organization = saveOrganization(organization);

        if (!Objects.isNull(registerRequest.getAddress())) {
            Address address = new Address();
            modelUtil.map(registerRequest.getAddress(), address);

            address.setOrganization(organization);
            organization.setAddress(address);

            organization = saveOrganization(organization);
        }

        organizationTeam.setOrganization(organization);

        organizationTeamRepository.save(organizationTeam);

        return organization;

    }


    public Organization saveOrganization(Organization organization){
        return repository.save(organization);
    }


    @Transactional
    public Organization editOrganization(Integer idOrg, OrganizationRequest registerRequest) {
        ModelUtil modelUtil = ModelUtil.getInstance();

        Optional<Organization> org = repository.findById(idOrg);

        if (org.isPresent()) {
            Organization orgEdit = org.get();
            modelUtil.map(registerRequest, orgEdit);

            orgEdit = saveOrganization(orgEdit);

            if (!Objects.isNull(registerRequest.getAddress())) {
                Address address = new Address();
                CopyUtil.copyProperties(registerRequest.getAddress(), address);
                address.setId(orgEdit.getId());

                address.setOrganization(orgEdit);
                orgEdit.setAddress(address);

                orgEdit = saveOrganization(orgEdit);
            }
            return orgEdit;
        }

        throw new NotFoundException(Organization.class);
    }


    @Transactional
    public List<Organization> getOrganizationsByCollaborator() {
        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            Optional<List<Organization>> organization = repository.findOrganizationByCollaboratorId(((Collaborator) userService.getCurrentUser().get()).getId());
            return organization.orElseGet(ArrayList::new);
        }
        throw new NotFoundException(((User) userService.getCurrentUser().get()).getEmail());
    }


    @Transactional
    public Organization getOrganization(Integer idOrg) {
        Optional<Organization> organization = repository.findById(idOrg);
        if (organization.isPresent()) {
            return organization.get();
        } else {
            throw new NotFoundException(Organization.class);
        }
    }


    public List<Collaborator> getCollaborators(Integer idOrg) {
        Organization organization = getOrganization(idOrg);
        Optional<List<Collaborator>> collaborators = collaboratorRepository.findAllCollaboratorByOrganizationId(organization.getId());
        return collaborators.orElseGet(ArrayList::new);
    }


    public void deleteOrganization(Integer idOrg) {
        Organization organization = getOrganization(idOrg);
        Optional<List<OrganizationTeam>> organizationTeamList = organizationTeamRepository.findAllByOrganizationId(organization.getId());
        organizationTeamList.ifPresent(organizationTeamRepository::deleteAll);
        repository.deleteById(idOrg);
    }

}
