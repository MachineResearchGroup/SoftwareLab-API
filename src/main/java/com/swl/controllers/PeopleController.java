package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.user.Client;
import com.swl.models.user.Collaborator;
import com.swl.payload.response.MessageResponse;
import com.swl.service.ClientService;
import com.swl.service.CollaboratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;


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

        try {
            Collaborator collaborator = collaboratorService.getCollaborator();

            return !Objects.isNull(collaborator) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, collaborator)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Collaborator.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/client")
    @Secured({"ROLE_CLIENT"})
    public ResponseEntity<?> getClientLogged() {

        try {
            Client client = clientService.getClient();

            return !Objects.isNull(client) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, client)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Client.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, e.getMessage()));
        }
    }

}
