package com.swl.models.management;

import com.swl.models.people.Collaborator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "organization_team_collaborator")
public class OrganizationTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    private Organization organization;

    @OneToOne
    private Team team;

    @OneToOne
    private Collaborator collaborator;

}
