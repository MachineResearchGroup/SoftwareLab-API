package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.payload.response.MessageResponse;
import com.swl.service.ClientService;
import com.swl.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/people")
@RequiredArgsConstructor
public class PeopleController {

    private final CollaboratorService collaboratorService;

    private final ClientService clientService;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/collaborator")
    @Secured({"ROLE_PO", "ROLE_PMO", "ROLE_DEV"})
    public ResponseEntity<?> getCollaboratorLogged() {

        Collaborator collaborator = collaboratorService.getCollaborator();
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, collaborator));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/client")
    @Secured({"ROLE_CLIENT"})
    public ResponseEntity<?> getClientLogged() {

        Client client = clientService.getClient();
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, client));

    }

}
