package com.swl.populator.management;

import com.swl.models.management.Organization;
import com.swl.models.management.Team;
import com.swl.models.user.Collaborator;
import com.swl.populator.config.PopulatorConfig;
import com.swl.populator.project.PopulatorProject;
import com.swl.populator.user.PopulatorCollaborator;
import com.swl.populator.util.FakerUtil;
import com.swl.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.IntStream;

@Component
public class PopulatorTeam {

    @Autowired
    private PopulatorOrganizationTeam populatorOrganizationTeam;

    @Autowired
    private PopulatorCollaborator populatorCollaborator;

    @Autowired
    private PopulatorProject populatorProject;

    @Autowired
    private TeamRepository teamRepository;


    public Team create() {
        return Team.builder()
                .name(FakerUtil.getInstance().faker.lordOfTheRings().location())
                .build();
    }


    public void save(Organization organization, PopulatorConfig config) {
        Integer idOrg = organization.getId();
        Team team = create();

        int numberCollaborators = FakerUtil.getInstance().faker.number().numberBetween(2, config.getMaxNumberCollaboratorsByTeam());
        int numberProjects = FakerUtil.getInstance().faker.number().numberBetween(2, config.getMaxNumberProjectsByTeam());

        // Save PMO and PO
        Collaborator pmo = populatorCollaborator.savePMO(idOrg, organization.getSupervisor());
        team.setSupervisor(pmo);
        team = teamRepository.save(team);

        populatorOrganizationTeam.save(organization, pmo, team);
        populatorOrganizationTeam.save(organization, populatorCollaborator.savePO(idOrg, pmo), team);

        // Save DEVs
        Team finalTeam = team;
        IntStream.range(0, numberCollaborators).forEach(i ->
                populatorOrganizationTeam.save(organization, populatorCollaborator.saveDEV(idOrg, pmo), finalTeam));

        // Save Projects
        finalTeam.setProjects(new ArrayList<>());
        IntStream.range(0, numberProjects).forEach(i -> finalTeam.getProjects().add(populatorProject.save(config, finalTeam)));
        teamRepository.save(finalTeam);
    }

}
