package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
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

        sheduleService.verifyRedactionSheduled(redactionRequest);
        RedactionShedule redaction = sheduleService.registerRedactionShedule(redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/shedule/{idProject}")
    @Secured({"ROLE_PMO", "ROLE_PO"})
    public ResponseEntity<?> getRedactionShedule(@PathVariable("idProject") Integer idProject) {


        RedactionShedule redaction = sheduleService.getRedactionShedule(idProject);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> registerRedaction(@RequestBody RedactionRequest redactionRequest) {

        service.verifyRedaction(redactionRequest);
        Redaction redaction = service.registerRedaction(redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.REGISTERED, redaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{idRedaction}")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> editRedaction(@PathVariable("idRedaction") Integer idRedaction, @RequestBody RedactionRequest redactionRequest) {

        service.verifyRedaction(redactionRequest);
        Redaction editRedaction = service.editRedaction(idRedaction, redactionRequest);
        return ResponseEntity.ok(new MessageResponse(MessageEnum.EDITED, editRedaction));

    }


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    @Secured({"ROLE_CLIENT", "ROLE_PO"})
    public ResponseEntity<?> getAllRedactions(@RequestParam(value = "idProject", required = false) Integer idProject,
                                              @RequestParam(value = "idClient", required = false) Integer idClient) {

        List<Redaction> redactions = new ArrayList<>();

        if (!Objects.isNull(idProject)) {
            redactions = service.getAllRedactionByProject(idProject);
        } else if (!Objects.isNull(idClient)) {
            redactions = service.getAllRedactionByClient(idProject);
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
