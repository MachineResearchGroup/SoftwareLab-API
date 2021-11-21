package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
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


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/requirement")
@RequiredArgsConstructor
public class RequirementController {

    private final RequirementService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> registerRequirement(@RequestBody RequirementRequest requirementRequest) {

        Requirement requirement = service.registerRequirement(requirementRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, requirement));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idRequirement}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> editRequirement(@PathVariable("idRequirement") Integer idRequirement, @RequestBody RequirementRequest requirementRequest) {

        Requirement editRequirement = service.editRequirement(idRequirement, requirementRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editRequirement));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idRequirement}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getRequirement(@PathVariable("idRequirement") Integer idRequirement) {

        Requirement requirement = service.getRequirement(idRequirement);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, requirement));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> getRequirements(@RequestParam(value = "idProject", required = false) Integer idProject) {

        List<Requirement> requirement = service.getAllRequirementByProject(idProject);
        if (requirement.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Requirement.class, requirement));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, requirement));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idRequirement}")
    @Secured({"ROLE_DEV", "ROLE_PO"})
    public ResponseEntity<?> deleteRequirement(@PathVariable("idRequirement") Integer idRequirement) {

        service.deleteRequirement(idRequirement);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Requirement.class));

    }

}
