package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.project.Project;
import com.swl.payload.request.ProjectRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> registerProject(@RequestBody ProjectRequest projectRequest) {

        Project project = service.registerProject(projectRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, project));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idProject}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> editProject(@PathVariable("idProject") Integer idProject, @RequestBody ProjectRequest projectRequest) {

        Project editProject = service.editProject(idProject, projectRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editProject));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idProject}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getProject(@PathVariable("idProject") Integer idProject) {

        Project project = service.getProject(idProject);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, project));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idProject}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> deleteProject(@PathVariable("idProject") Integer idProject) {

        service.deleteProject(idProject);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Project.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/addClientInProject/}")
    @Secured("ROLE_PMO")
    public ResponseEntity<?> addClientInProject(@RequestParam(name = "idProject") Integer idProject,
                                                @RequestParam(name = "clientEmail") String clientEmail) {

        service.addClientInProject(idProject, clientEmail);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.ADDED, Client.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllProjects(@RequestParam(value = "idTeam", required = false) Integer idTeam,
                                            @RequestParam(value = "idOrganization", required = false) Integer idOrganization,
                                            @RequestParam(value = "idCollaborator", required = false) Integer idCollaborator,
                                            @RequestParam(value = "idClient", required = false) Integer idClient) {

        List<Project> projects;

        if (!Objects.isNull(idOrganization)) {
            projects = service.getAllProjectsByOrganization(idOrganization);

        } else if (!Objects.isNull(idTeam)) {
            projects = service.getAllProjectsByTeam(idTeam);

        } else if (!Objects.isNull(idCollaborator)) {
            projects = service.getAllProjectsByCollaborator(idCollaborator);

        } else if (!Objects.isNull(idClient)) {
            projects = service.getAllProjectsByClient(idClient);

        } else {
            projects = service.getAllProjectsByCollaboratorActual();
        }

        if(projects.isEmpty()){
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Project.class, projects));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects));
    }

}
