package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.management.Organization;
import com.swl.models.people.Collaborator;
import com.swl.payload.request.OrganizationRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrganizationController {

    private final OrganizationService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationRequest organizationRequest) {

        service.verifyOrganization(organizationRequest);
        Organization organization = service.registerOrganization(organizationRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, organization));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> editOrganization(@PathVariable("idOrg") Integer idOrg,
                                              @RequestBody OrganizationRequest organizationRequest) {

        service.verifyOrganization(organizationRequest);
        Organization organization = service.editOrganization(idOrg, organizationRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, organization));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getOrganizationsByCollaborator() {

        List<Organization> organizations = service.getOrganizationsByCollaborator();
        if(organizations.isEmpty()){
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Organization.class, organizations));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, organizations));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idOrg}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getOrganization(@PathVariable("idOrg") Integer idOrg) {

        Organization organization = service.getOrganization(idOrg);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, organization));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/collaborators/{idOrg}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getCollaborators(@PathVariable("idOrg") Integer idOrg) {

        List<Collaborator> contributors = service.getCollaborators(idOrg);
        if(contributors.isEmpty()){
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Collaborator.class, contributors));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, contributors));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> deleteOrganization(@PathVariable("idOrg") Integer idOrg) {

        service.deleteOrganization(idOrg);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Organization.class));

    }
}
