package com.swl.populator.management;

import com.swl.models.management.Organization;
import com.swl.models.management.OrganizationTeam;
import com.swl.models.management.Team;
import com.swl.models.people.Collaborator;
import com.swl.repository.OrganizationTeamRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PopulatorOrganizationTeam {

    @Autowired
    private OrganizationTeamRepository organizationTeamRepository;


    public OrganizationTeam save(Organization organization, Collaborator collaborator, Team team) {
        return organizationTeamRepository.save(OrganizationTeam.builder()
                .organization(organization)
                .collaborator(collaborator)
                .team(team)
                .build());
    }

}
