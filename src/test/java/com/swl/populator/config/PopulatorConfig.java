package com.swl.populator.config;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopulatorConfig {

    private Integer numberOrganizations;

    private Integer maxNumberTeams;

    private Integer maxNumberCollaboratorsByTeam;

    private Integer maxNumberProjectsByTeam;

    private Integer maxNumberEpicsByProject;

    private Integer maxNumberSprintsByEpic;

    private Integer maxNumberLabels;

    private Integer maxNumberRedactionsByProject;

    private Integer maxNumberRequirementsByRedaction;

    private Integer maxNumberBoardsByProject;

    private Integer maxNumberDocumentsByProject;
}
