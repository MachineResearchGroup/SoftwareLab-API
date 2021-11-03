package com.swl.controllers;

import com.swl.models.Collaborator;
import com.swl.models.Organization;
import com.swl.models.Team;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.TeamRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/team")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @PreAuthorize("hasRole('PMO')")
    public ResponseEntity<?> registerTeam(@RequestBody TeamRequest team) {

        try {
            return service.registerTeam(team);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idTeam}")
    @PreAuthorize("hasRole('PMO')")
    public ResponseEntity<?> editTeam(@PathVariable("idTeam") Integer idTeam, @RequestBody TeamRequest teamRequest) {

        try {
            Team editTeam = service.editTeam(idTeam, teamRequest);

            if (!Objects.isNull(editTeam)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editTeam));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Team.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idTeam}")
    @PreAuthorize("hasRole('DEV') or hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> getTeam(@PathVariable("idTeam") Integer idTeam) {

        try {
            Team team = service.getTeam(idTeam);

            if (!Objects.isNull(team)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, team));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idTeam}")
    @PreAuthorize("hasRole('PMO')")
    public ResponseEntity<?> deleteTeam(@PathVariable("idTeam") Integer idTeam) {

        try {
            boolean deleteTeam = service.deleteTeam(idTeam);

            return deleteTeam ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Team.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Team.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/collaborators/{idTeam}")
    @PreAuthorize("hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> addCollaborator(@PathVariable("idTeam") Integer idTeam, @RequestBody List<String> emails) {

        try {
            List<Collaborator> collaboratorList = service.addCollaborator(idTeam, emails);

            if (!Objects.isNull(collaboratorList)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.ADDED, collaboratorList));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_ADDED, "collaborator", e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/collaborators/{idTeam}")
    @PreAuthorize("hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> getCollaborators(@PathVariable("idTeam") Integer idTeam) {

        try {
            List<Collaborator> collaborators = service.getCollaborators(idTeam);

            return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, collaborators));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/collaborators/{idTeam}")
    @PreAuthorize("hasRole('PMO')")
    public ResponseEntity<?> deleteCollaborator(@PathVariable("idTeam") Integer idTeam, @RequestBody List<String> emails) {

        try {
            List<Collaborator> collaboratorList = service.deleteCollaborator(idTeam, emails);

            if (!Objects.isNull(collaboratorList)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, collaboratorList));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/teamsByOrganization/{idOrg}")
    @PreAuthorize("hasRole('PMO')")
    public ResponseEntity<?> getAllTeamByOrganization(@PathVariable("idOrg") Integer idOrg) {

        try {
            List<Team> teams = service.getAllTeamByOrganization(idOrg);

            if (!Objects.isNull(teams)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, teams));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class, e.getMessage()));
        }
    }
}
