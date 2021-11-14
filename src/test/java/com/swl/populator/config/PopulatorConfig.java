package com.swl.populator.config;

import com.swl.populator.util.FakerUtil;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PopulatorConfig {

    private int numberOrganizations;

    private int[] rangeNumberTeams;

    private int[] rangeNumberCollaboratorsByTeam;

    private int[] rangeNumberProjectsByTeam;

    private int[] rangeNumberEpicsByProject;

    private int[] rangeNumberSprintsByEpic;

    private int[] rangeNumberLabels;

    private int[] rangeNumberRedactionsByProject;

    private int[] rangeNumberRequirementsByRedaction;

    private int[] rangeNumberBoardsByProject;

    private int[] rangeNumberColumnsByBoard;

    private int[] rangeNumberHistoriesByColumn;

    private int[] rangeNumberTasksByColumn;

    private int[] rangeNumberTasksByHistory;

    private int[] rangeNumberDocumentsByProject;

    private int[] rangeNumberEventsByProject;

    public int getNumberTeams(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberTeams[0], this.rangeNumberTeams[1]);
    }

    public int getNumberCollaboratorsByTeam(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberCollaboratorsByTeam[0], this.rangeNumberCollaboratorsByTeam[1]);
    }

    public int getNumberProjectsByTeam(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberProjectsByTeam[0], this.rangeNumberProjectsByTeam[1]);
    }

    public int getNumberEpicsByProject(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberEpicsByProject[0], this.rangeNumberEpicsByProject[1]);
    }

    public int getNumberSprintsByEpic(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberSprintsByEpic[0], this.rangeNumberSprintsByEpic[1]);
    }

    public int getNumberLabels(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberLabels[0], this.rangeNumberLabels[1]);
    }

    public int getNumberRedactionsByProjects(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberRedactionsByProject[0], this.rangeNumberRedactionsByProject[1]);
    }

    public int getNumberRequirementsByRedaction(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberRequirementsByRedaction[0], this.rangeNumberRequirementsByRedaction[1]);
    }

    public int getNumberBoardsByProject(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberBoardsByProject[0], this.rangeNumberBoardsByProject[1]);
    }

    public int getNumberColumnsByBoard(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberColumnsByBoard[0], this.rangeNumberColumnsByBoard[1]);
    }

    public int getNumberHistoriesByColumn(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberHistoriesByColumn[0], this.rangeNumberHistoriesByColumn[1]);
    }

    public int getNumberTasksByColumn(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberTasksByColumn[0], this.rangeNumberTasksByColumn[1]);
    }

    public int getNumberTasksByHistory(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberTasksByHistory[0], this.rangeNumberTasksByHistory[1]);
    }

    public int getNumberDocumentsByProject(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberDocumentsByProject[0], this.rangeNumberDocumentsByProject[1]);
    }

    public int getNumberEventsByProject(){
        return FakerUtil.getInstance().faker.number().numberBetween(this.rangeNumberEventsByProject[0], this.rangeNumberEventsByProject[1]);
    }
}
