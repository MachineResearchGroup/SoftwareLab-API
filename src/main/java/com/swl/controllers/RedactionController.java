package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.exceptions.business.InvalidRequestException;
import com.swl.models.enums.MessageEnum;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/redaction")
@RequiredArgsConstructor
public class RedactionController {

    private final RedactionSheduleService sheduleService;

    private final RedactionService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/shedule")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> registerRedactionShedule(@RequestBody RedactionSheduleRequest redactionRequest) {

        RedactionShedule redaction = sheduleService.registerRedactionShedule(redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/shedule/{idRedactionShedule}")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> editRedactionShedule(@PathVariable("idRedactionShedule") Integer idRedactionShedule, @RequestBody RedactionSheduleRequest redactionRequest) {

        RedactionShedule redaction = sheduleService.editRedactionShedule(idRedactionShedule, redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/shedule/{idRedactionShedule}")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> getRedactionShedule(@PathVariable(value = "idRedactionShedule") Integer idRedactionShedule) {

        RedactionShedule redaction = sheduleService.getRedactionShedule(idRedactionShedule);
        if (Objects.isNull(redaction)) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, RedactionShedule.class, redaction));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/shedule/project/{idProject}")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> getRedactionSheduleByProject(@PathVariable(value = "idProject", required = false) Integer idProject) {

        RedactionShedule redaction = sheduleService.getRedactionSheduleByProject(idProject);
        if (Objects.isNull(redaction)) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, RedactionShedule.class, redaction));
        }
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/shedule/{idRedactionShedule}")
    @Secured({"ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> deleteRedactionShedule(@PathVariable("idRedactionShedule") Integer idRedactionShedule) {

        sheduleService.deleteRedactionShedule(idRedactionShedule);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, RedactionShedule.class));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> registerRedaction(@RequestBody RedactionRequest redactionRequest) {

        Redaction redaction = service.registerRedaction(redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> editRedaction(@PathVariable("idRedaction") Integer idRedaction, @RequestBody RedactionRequest redactionRequest) {

        Redaction editRedaction = service.editRedaction(idRedaction, redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editRedaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> getRedactions(@RequestParam(value = "idProject", required = false) Integer idProject,
                                           @RequestParam(value = "idClient", required = false) Integer idClient) {

        List<Redaction> redactions;

        if (!Objects.isNull(idProject)) {
            redactions = service.getAllRedactionByProject(idProject);
        } else if (!Objects.isNull(idClient)) {
            redactions = service.getAllRedactionByClient(idClient);
        }else{
            throw new InvalidRequestException("no parameters added");
        }

        if (redactions.isEmpty()) {
            return ResponseEntity.ok(new MessageResponse(MessageEnum.EMPTY, Redaction.class, redactions));
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redactions));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> getRedaction(@PathVariable("idRedaction") Integer idRedaction) {

        Redaction redaction = service.getRedaction(idRedaction);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.FOUND, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> deleteRedaction(@PathVariable("idRedaction") Integer idRedaction) {

        service.deleteRedaction(idRedaction);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.DELETED, Redaction.class));

    }

}
