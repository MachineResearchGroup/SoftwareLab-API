package com.swl.util;

import com.swl.models.management.Address;
import com.swl.models.management.Organization;
import com.swl.models.management.Team;
import com.swl.models.project.*;
import com.swl.models.people.Client;
import com.swl.models.people.Collaborator;
import com.swl.models.people.User;
import com.swl.service.RequirementService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;

public class BuilderUtil {

    public static User buildUser() {
        return User.builder()
                .name("User 1")
                .username("Username")
                .email("user@gmail.com")
                .password("12345678")
                .build();
    }

    public static Client buildClient() {
        return Client.builder()
                .corporateName("Cliente Teste")
                .segments(new ArrayList<>(Collections.singletonList("Industria")))
                .user(buildUser())
                .build();
    }


    public static Collaborator buildCollaborator() {
        return Collaborator.builder()
                .register("12345")
                .function("ROLE_PMO")
                .user(buildUser())
                .build();
    }


    public static Organization buildOrganization() {
        return Organization.builder()
                .cnpj("06.214.736/0001-49")
                .name("Empresa A")
                .build();
    }


    public static Organization buildOrganizationWithAddress() {
        return Organization.builder()
                .cnpj("06.214.736/0001-49")
                .name("Empresa A")
                .address(buildAddress())
                .build();
    }


    public static Team buildTeam() {
        return Team.builder()
                .name("Equipe Teste")
                .build();
    }


    public static Address buildAddress() {
        return Address.builder()
                .street("Rua teste")
                .number("123")
                .city("Cidade teste")
                .state("Estado teste")
                .build();
    }


    public static Project buildProject() {
        return Project.builder()
                .name("Projeto teste")
                .description("...")
                .repository("/git/project/")
                .build();
    }


    public static Board buildBoard() {
        return Board.builder()
                .title("Board Teste")
                .build();
    }


    public static Columns buildColumn() {
        return Columns.builder()
                .title("Column Teste")
                .build();
    }


    public static History buildHistory() {
        History history = new History();
        history.setTitle("History teste");
        history.setDescription("Descricao");
        history.setEndDate(LocalDate.now());
        history.setPriority(1);
        history.setWeight(1);
        return history;
    }


    public static Task buildTask() {
        Task task = new Task();
        task.setTitle("History teste");
        task.setDescription("Descricao");
        task.setEndDate(LocalDate.now());
        task.setPriority(1);
        task.setEstimate(1);
        task.setDuration(1);
        return task;
    }


    public static Redaction buildRedaction() {
        return Redaction.builder()
                .description("Como cliente eu quero...")
                .build();
    }


    public static Requirement buildRequirement() {
        return Requirement.builder()
                .category("F")
                .description("O sistema deve fazer algo importante..")
                .build();
    }

}
