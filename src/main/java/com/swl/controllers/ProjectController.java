package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.user.Client;
import com.swl.models.management.Organization;
import com.swl.models.project.Project;
import com.swl.models.management.Team;
import com.swl.models.enums.MessageEnum;
import com.swl.models.user.Collaborator;
import com.swl.payload.request.ProjectRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.QueryAnnotation;
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

        try {
            var response = service.verifyProject(projectRequest);

            if(!response.getStatusCode().equals(HttpStatus.OK)){
                return response;
            }

            Project project = service.registerProject(projectRequest);

            return !Objects.isNull(project) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, project)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idProject}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> editProject(@PathVariable("idProject") Integer idProject, @RequestBody ProjectRequest projectRequest) {

        try {
            var response = service.verifyProject(projectRequest);

            if(!response.getStatusCode().equals(HttpStatus.OK)){
                return response;
            }

            Project editProject = service.editProject(idProject, projectRequest);

            return !Objects.isNull(editProject) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editProject)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Project.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idProject}")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
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


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idProject}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
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


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/addClientInProject/}")
    @Secured("ROLE_PMO")
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


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> getAllProjects(@RequestParam(value="idTeam", required=false) Integer idTeam,
                                            @RequestParam(value="idOrganization", required=false) Integer idOrganization,
                                            @RequestParam(value="idCollaborator", required=false) Integer idCollaborator,
                                            @RequestParam(value="idClient", required=false) Integer idClient) {

        try {
            List<Project> projects;

            if(!Objects.isNull(idOrganization)){
                projects = service.getAllProjectsByOrganization(idOrganization);

                return !Objects.isNull(projects) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects)) :
                        ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Organization.class));
            } else if(!Objects.isNull(idTeam)){
                projects = service.getAllProjectsByTeam(idTeam);

                return !Objects.isNull(projects) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects)) :
                        ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Team.class));
            } else if(!Objects.isNull(idCollaborator)){
                projects = service.getAllProjectsByCollaborator(idCollaborator);

                return !Objects.isNull(projects) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects)) :
                        ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Collaborator.class));
            } else if(!Objects.isNull(idClient)){
                projects = service.getAllProjectsByClient(idClient);

                return !Objects.isNull(projects) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, projects)) :
                        ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Client.class));
            }

            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.INVALID_REQUEST));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class, e.getMessage()));
        }
    }

}
