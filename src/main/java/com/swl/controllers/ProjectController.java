package com.swl.controllers;

import com.swl.models.Client;
import com.swl.models.Organization;
import com.swl.models.Project;
import com.swl.models.Team;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.request.ProjectRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @PreAuthorize("hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> registerProject(@RequestBody ProjectRequest project) {

        try {
            return service.registerProject(project);

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idProject}")
    @PreAuthorize("hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> editProject(@PathVariable("idProject") Integer idProject, @RequestBody ProjectRequest project) {

        try {
            Project editProject = service.editProject(idProject, project);

            if (!Objects.isNull(editProject)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editProject));
            } else {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));
            }
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Project.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idProject}")
    @PreAuthorize("hasRole('DEV') or hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> getProject(@PathVariable("idProject") Integer idProject) {

        try {
            Project project = service.getProject(idProject);

            if (!Objects.isNull(project)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, project));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idProject}")
    @PreAuthorize("hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> deleteProject(@PathVariable("idProject") Integer idProject) {

        try {
            boolean deleteProject = service.deleteProject(idProject);

            return deleteProject ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Project.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Project.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/addClientInProject/}")
    @PreAuthorize("hasRole('PMO')")
    public ResponseEntity<?> addClientInProject(@RequestParam(name = "idProject") Integer idProject,
                                                @RequestParam(name = "idClient") String clientEmail) {

        try {
            boolean clientProject = service.addClientInProject(idProject, clientEmail);

            return clientProject ? ResponseEntity.ok(new MessageResponse(MessageEnum.ADDED, Client.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Client.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Project.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/projectsByTeam/{idTeam}")
    @PreAuthorize("hasRole('DEV') or hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> getAllProjectsByTeam(@PathVariable("idTeam") Integer idTeam) {

        try {
            List<Project> projects = service.getAllProjectsByTeam(idTeam);

            if (!Objects.isNull(projects)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class, e.getMessage()));
        }
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/projectsByOrganization/{idOrg}")
    @PreAuthorize("hasRole('DEV') or hasRole('PO') or hasRole('PMO')")
    public ResponseEntity<?> getAllProjectsByOrganization(@PathVariable("idOrg") Integer idOrg) {

        try {
            List<Project> projects = service.getAllProjectsByOrganization(idOrg);

            if (!Objects.isNull(projects)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class, e.getMessage()));
        }
    }

}
