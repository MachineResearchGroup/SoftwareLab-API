package com.swl.service;

import com.swl.models.Collaborator;
import com.swl.models.Address;
import com.swl.models.Organization;
import com.swl.models.OrganizationTeam;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.OrganizationRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.repository.CollaboratorRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.OrganizacaoRepository;
import com.swl.util.CopyUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class OrganizationService {

    @Autowired
    private final OrganizacaoRepository repository;

    @Autowired
    private final CollaboratorRepository collaboratorRepository;

    @Autowired
    private final OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private final UserService userService;


    public ResponseEntity<?> registerOrganization(OrganizationRequest registerRequest) {
        Organization organization = new Organization();
        OrganizationTeam organizationTeam = new OrganizationTeam();

        if (userService.getCurrentUser().isPresent() && userService.getCurrentUser().get() instanceof Collaborator) {
            organization.setSupervisor((Collaborator) userService.getCurrentUser().get());
            organizationTeam.setCollaborator((Collaborator) userService.getCurrentUser().get());
        }

        CopyUtil.copyProperties(registerRequest, organization);
        organization = repository.save(organization);

        if (!Objects.isNull(registerRequest.getAddress())) {
            Address address = new Address();
            CopyUtil.copyProperties(registerRequest.getAddress(), address);

            address.setOrganization(organization);
            organization.setAddress(address);

            organization = repository.save(organization);
        }

        organizationTeam.setOrganization(organization);

        organizationTeamRepository.save(organizationTeam);

        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, organization));

    }


    public Organization editOrganization(Integer idOrg, OrganizationRequest registerRequest) {
        Optional<Organization> org = repository.findById(idOrg);

        if (org.isPresent()) {
            Organization orgEdit = org.get();
            CopyUtil.copyProperties(registerRequest, orgEdit);
            orgEdit = repository.save(orgEdit);

            if (!Objects.isNull(registerRequest.getAddress())) {
                Address address = new Address();
                CopyUtil.copyProperties(registerRequest.getAddress(), address);
                address.setId(orgEdit.getId());

                address.setOrganization(orgEdit);
                orgEdit.setAddress(address);

                orgEdit = repository.save(orgEdit);
            }
            return orgEdit;
        }

        return null;
    }


    public Organization getOrganization(Integer idOrg) {
        return repository.findById(idOrg).orElse(null);
    }


    public List<Collaborator> getCollaborators(Integer idOrg) {
        return collaboratorRepository.findAllCollaboratorByOrganizationId(idOrg).orElse(null);
    }


    public boolean deleteOrganization(Integer idOrg) {
        if (repository.existsById(idOrg)) {
            Optional<List<OrganizationTeam>> organizationTeamList = organizationTeamRepository.findAllByOrganizationId(idOrg);
            organizationTeamList.ifPresent(organizationTeamRepository::deleteAll);

            repository.deleteById(idOrg);
            return true;
        }
        return false;
    }

}
