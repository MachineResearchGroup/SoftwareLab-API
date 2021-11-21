package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.exceptions.business.InvalidRequestException;
import com.swl.models.enums.MessageEnum;
import com.swl.payload.response.MessageResponse;
import com.swl.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class UploadController {

    private final UploadService service;


    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/image")
    @Secured({"ROLE_USER", "ROLE_CLIENT", "ROLE_DEV", "ROLE_PO", "ROLE_PMO"})
    public ResponseEntity<?> uploadImage(@RequestParam(value = "imageFile") MultipartFile imageFile,
                                         @RequestParam(value = "idTeam", required = false) Integer idTeam,
                                         @RequestParam(value = "idOrganization", required = false) Integer idOrganization,
                                         @RequestParam(value = "idProject", required = false) Integer idProject,
                                         @RequestParam(value = "idUser", required = false) Integer idUser) {

        if (!Objects.isNull(idOrganization)) {
            service.uploadImageOfOrganization(imageFile, idOrganization);

        } else if (!Objects.isNull(idTeam)) {
            service.uploadImageOfTeam(imageFile, idTeam);

        } else if (!Objects.isNull(idProject)) {
            service.uploadImageOfProject(imageFile, idProject);

        } else if (!Objects.isNull(idUser)) {
            service.uploadImageOfUser(imageFile, idUser);
        } else {
            throw new InvalidRequestException("no parameters added");
        }

        return ResponseEntity.ok(new MessageResponse(MessageEnum.ADDED, "image"));
    }

}
