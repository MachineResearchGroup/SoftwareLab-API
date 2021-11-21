package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.management.Team;
import com.swl.models.people.Collaborator;
import com.swl.payload.request.CollaboratorRequest;
import com.swl.payload.request.TeamRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> registerTeam(@RequestBody TeamRequest teamRequest) {

        Team team = service.registerTeam(teamRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, team));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idTeam}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> editTeam(@PathVariable("idTeam") Integer idTeam, @RequestBody TeamRequest teamRequest) {

        Team editTeam = service.editTeam(idTeam, teamRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editTeam));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idTeam}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getTeam(@PathVariable("idTeam") Integer idTeam) {

        Team team = service.getTeam(idTeam);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, team));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idTeam}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> deleteTeam(@PathVariable("idTeam") Integer idTeam) {

        service.deleteTeam(idTeam);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Team.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/collaborators/{idTeam}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> addCollaborator(@PathVariable("idTeam") Integer idTeam,
                                             @RequestBody List<CollaboratorRequest> collaboratorRequests) {

        List<Collaborator> collaboratorList = service.addCollaborator(idTeam, collaboratorRequests);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.ADDED, collaboratorList));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/collaborators/{idTeam}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getCollaborators(@PathVariable("idTeam") Integer idTeam) {

        List<Collaborator> collaborators = service.getCollaborators(idTeam);
        if (collaborators.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Collaborator.class, collaborators));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, collaborators));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/collaborators/{idTeam}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> deleteCollaborator(@PathVariable("idTeam") Integer idTeam, @RequestBody List<String> emails) {

        List<Collaborator> collaboratorList = service.deleteCollaborators(idTeam, emails);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, collaboratorList));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/teamsByOrganization/{idOrg}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> getAllTeamByOrganization(@PathVariable("idOrg") Integer idOrg) {

        List<Team> teams = service.getAllTeamByOrganization(idOrg);
        if (teams.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Team.class, teams));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, teams));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getTeamsByCollaborator() {

        List<Team> teams = service.getTeamsByCollaborator();
        if (teams.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Team.class, teams));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, teams));

    }
}
