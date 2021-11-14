package com.swl.populator;

import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.management.PopulatorOrganization;
import com.swl.populator.management.PopulatorTeam;
import com.swl.populator.project.PopulatorRequirement;
import com.swl.populator.user.PopulatorCollaborator;
import com.swl.repository.OrganizationRepository;
import com.swl.repository.OrganizationTeamRepository;
import com.swl.repository.RequirementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class PopulatorTest {


    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private OrganizationTeamRepository organizationTeamRepository;

    @Autowired
    private PopulatorCollaborator populatorCollaborator;

    @Autowired
    private PopulatorTeam populatorTeam;

    @Autowired
    private RequirementRepository requirementRepository;

    private PopulatorOrganization populatorOrganization;

    private PopulatorRequirement populatorRequirement;


    @BeforeEach
    public void initUseCase() {
        populatorOrganization = new PopulatorOrganization(organizationRepository, organizationTeamRepository,
                populatorCollaborator, populatorTeam);

        populatorRequirement = new PopulatorRequirement(requirementRepository);
    }


    @Test
    public void run() {
        PopulatorConfig populatorConfig = PopulatorConfig.builder()
                .numberOrganizations(5)
                .rangeNumberTeams(new int[]{2, 5})
                .rangeNumberCollaboratorsByTeam(new int[]{2, 3})
                .rangeNumberProjectsByTeam(new int[]{2, 2})
                .rangeNumberEpicsByProject(new int[]{2, 2})
                .rangeNumberSprintsByEpic(new int[]{2, 5})
                .rangeNumberLabels(new int[]{2, 2})
                .rangeNumberRedactionsByProject(new int[]{2, 5})
                .rangeNumberRequirementsByRedaction(new int[]{2, 5})
                .rangeNumberBoardsByProject(new int[]{1, 2})
                .rangeNumberColumnsByBoard(new int[]{4, 8})
                .rangeNumberHistoriesByColumn(new int[]{1, 6})
                .rangeNumberTasksByColumn(new int[]{1, 2})
                .rangeNumberTasksByHistory(new int[]{1, 4})
                .rangeNumberDocumentsByProject(new int[]{2, 5})
                .rangeNumberEventsByProject(new int[]{2, 5})
                .build();

        populatorRequirement.saveAll();

        populatorOrganization.save(populatorConfig);
    }

}
