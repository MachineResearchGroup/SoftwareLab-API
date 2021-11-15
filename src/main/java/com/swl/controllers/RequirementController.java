package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Project;
import com.swl.models.project.Requirement;
import com.swl.payload.request.RequirementRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.RequirementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project/requirement")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerRequirement(@RequestBody RequirementRequest requirementRequest) {

        try {
            var response = service.verifyRequirement(requirementRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Requirement requirement = service.registerRequirement(requirementRequest);

            return !Objects.isNull(requirement) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, requirement)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idRequirement}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editRequirement(@PathVariable("idRequirement") Integer idRequirement, @RequestBody RequirementRequest requirementRequest) {

        try {
            var response = service.verifyRequirement(requirementRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Requirement editRequirement = service.editRequirement(idRequirement, requirementRequest);

            return !Objects.isNull(editRequirement) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editRequirement)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Requirement.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Project.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/all/{idProject}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getAllRequirements(@PathVariable("idProject") Integer idProject) {

        try {
            List<Requirement> requirements = service.getAllRequirementByProject(idProject);

            return !Objects.isNull(requirements) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, requirements)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Requirement.class));
        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idRequirement}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getRequirement(@PathVariable("idRequirement") Integer idRequirement) {

        try {
            Requirement requirement = service.getRequirement(idRequirement);

            if (!Objects.isNull(requirement)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, requirement));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Requirement.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Requirement.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idRequirement}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteRequirement(@PathVariable("idRequirement") Integer idRequirement) {

        try {
            boolean deleteRedaction = service.deleteRequirement(idRequirement);

            return deleteRedaction ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Requirement.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Requirement.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Requirement.class, e.getMessage()));
        }
    }

}
