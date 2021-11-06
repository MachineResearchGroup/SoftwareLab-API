package com.swl.util;

import com.swl.models.*;

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
                .cnpj("12345678912345")
                .name("Empresa A")
                .build();
    }


    public static Organization buildOrganizationWithAddress() {
        return Organization.builder()
                .cnpj("12345678912345")
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
}
