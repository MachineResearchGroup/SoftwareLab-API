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
                .maxNumberTeams(5)
                .maxNumberCollaboratorsByTeam(3)
                .maxNumberProjectsByTeam(2)
                .maxNumberEpicsByProject(2)
                .maxNumberSprintsByEpic(10)
                .maxNumberLabels(2)
                .maxNumberRedactionsByProject(10)
                .maxNumberRequirementsByRedaction(10)
                .maxNumberDocumentsByProject(10)
                .build();

        populatorRequirement.saveAll();

        populatorOrganization.save(populatorConfig);
    }

}
