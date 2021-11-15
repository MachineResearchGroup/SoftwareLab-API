package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.models.enums.MessageEnum;
import com.swl.models.project.Columns;
import com.swl.models.project.Project;
import com.swl.models.project.Redaction;
import com.swl.models.project.RedactionShedule;
import com.swl.payload.request.RedactionRequest;
import com.swl.payload.request.RedactionSheduleRequest;
import com.swl.payload.response.MessageResponse;
import com.swl.service.RedactionService;
import com.swl.service.RedactionSheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/project/redaction")
@RequiredArgsConstructor
public class RedactionController {

    private final RedactionSheduleService sheduleService;

    private final RedactionService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/shedule")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> registerRedactionShedule(@RequestBody RedactionSheduleRequest redactionRequest) {

        try {
            var response = sheduleService.verifyRedactionSheduled(redactionRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            RedactionShedule redaction = sheduleService.registerRedactionShedule(redactionRequest);

            return !Objects.isNull(redaction) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/shedule/{idProject}")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> getRedactionShedule(@PathVariable("idProject") Integer idProject) {

        try {
            RedactionShedule redaction = sheduleService.getRedactionShedule(idProject);

            return !Objects.isNull(redaction) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> registerRedaction(@RequestBody RedactionRequest redactionRequest) {

        try {
            var response = service.verifyRedaction(redactionRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Redaction redaction = service.registerRedaction(redactionRequest);

            return !Objects.isNull(redaction) ? ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Project.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_REGISTERED, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> editRedaction(@PathVariable("idRedaction") Integer idRedaction, @RequestBody RedactionRequest redactionRequest) {

        try {
            var response = service.verifyRedaction(redactionRequest);

            if (!response.getStatusCode().equals(HttpStatus.OK)) {
                return response;
            }

            Redaction editRedaction = service.editRedaction(idRedaction, redactionRequest);

            return !Objects.isNull(editRedaction) ? ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editRedaction)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Redaction.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.UNEDITED, Columns.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> getAllRedactions(@RequestParam(value = "idProject", required = false) Integer idProject,
                                              @RequestParam(value = "idClient", required = false) Integer idClient) {

        try {
            List<Redaction> redactions;

            if (!Objects.isNull(idProject)) {
                redactions = service.getAllRedactionByProject(idProject);

                return !Objects.isNull(redactions) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redactions)) :
                        ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Redaction.class));
            } else if (!Objects.isNull(idClient)) {
                redactions = service.getAllRedactionByClient(idProject);

                return !Objects.isNull(redactions) ? ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redactions)) :
                        ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Redaction.class));
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


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> getRedaction(@PathVariable("idRedaction") Integer idRedaction) {

        try {
            Redaction redaction = service.getRedaction(idRedaction);

            if (!Objects.isNull(redaction)) {
                return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redaction));
            } else {
                return ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Redaction.class));
            }

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_FOUND, Redaction.class, e.getMessage()));
        }
    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> deleteRedaction(@PathVariable("idRedaction") Integer idRedaction) {

        try {
            boolean deleteRedaction = service.deleteRedaction(idRedaction);

            return deleteRedaction ? ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Redaction.class)) :
                    ResponseEntity.badRequest().body(new MessageResponse(MessageEnum.NOT_FOUND, Redaction.class));

        } catch (Exception e) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(MessageEnum.NOT_DELETED, Redaction.class, e.getMessage()));
        }
    }

}
