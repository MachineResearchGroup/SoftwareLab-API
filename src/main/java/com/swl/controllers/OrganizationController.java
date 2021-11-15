package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.user.Collaborator;
import com.swl.models.management.Organization;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.OrganizationRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@RestController
@RequiredArgsConstructor
@RequestMapping("/organization")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrganizationController {

    private final OrganizationService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> getAllOrganizations() {

        try {
            List<Organization> organizations = service.getAllOrganizations();

            if (!Objects.isNull(organizations)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, organizations));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> registerOrganization(@RequestBody OrganizationRequest organizationRequest) {

        try {
            var response = service.verifyOrganization(organizationRequest);

            if(!response.getStatusCode().equals(HttpStatus.OK)){
                return response;
            }

            Organization organization = service.registerOrganization(organizationRequest);

            return !Objects.isNull(organization) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, organization)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_REGISTERED, Organization.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> editOrganization(@PathVariable("idOrg") Integer idOrg,
                                             @RequestBody OrganizationRequest organizationRequest) {

        try {
            var response = service.verifyOrganization(organizationRequest);

            if(!response.getStatusCode().equals(HttpStatus.OK)){
                return response;
            }

            Organization organization = service.editOrganization(idOrg, organizationRequest);

            return !Objects.isNull(organization) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, organization)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Organization.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> getOrganization(@PathVariable("idOrg") Integer idOrg) {

        try {
            Organization organization = service.getOrganization(idOrg);

            if (!Objects.isNull(organization)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, organization));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/collaborators/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> getCollaborators(@PathVariable("idOrg") Integer idOrg) {

        try {
            List<Collaborator> contributors = service.getCollaborators(idOrg);

            if (!Objects.isNull(contributors)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, contributors));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> deleteOrganization(@PathVariable("idOrg") Integer idOrg) {

        try {
            boolean organizationDeleted = service.deleteOrganization(idOrg);

            return organizationDeleted ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Organization.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Organization.class, e.getMessage()));
        }
    }
}
