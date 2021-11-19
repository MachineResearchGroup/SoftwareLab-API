package com.swl.controllers;

import com.swl.config.swagger.ApiRoleAccessNotes;
import com.swl.payload.request.ClientRequest;
import com.swl.payload.request.CollaboratorRequest;
import com.swl.payload.request.LoginRequest;
import com.swl.payload.request.RegisterRequest;
import com.swl.service.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Autowired
    AuthService service;


    @ApiOperation(value = "login")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200, message = "User Login successfully!")
    })
    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        String message = "";

        try {
            return service.authenticateUser(loginRequest);

        } catch (Exception e) {
            message = "Login not done! " + e.getMessage();
            return new ResponseEntity<>(message, HttpStatus.NOT_FOUND);
        }

    }


    @ApiOperation(value = "register")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201, message = "User registered successfully!")
    })
    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest signUpRequest) {

        return service.registerUser(signUpRequest);

    }


    @ApiOperation(value = "collaborator register")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201, message = "User registered successfully!")
    })
    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registerCollaborator")
    public ResponseEntity<?> registerCollaborator(@RequestBody CollaboratorRequest signUpRequest) {

        return service.registerCollaborator(signUpRequest);

    }


    @ApiOperation(value = "client register")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201, message = "User registered successfully!")
    })
    @ApiRoleAccessNotes
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/registerClient")
    public ResponseEntity<?> registerClient(@RequestBody ClientRequest signUpRequest) {

        return service.registerClient(signUpRequest);

    }
}
