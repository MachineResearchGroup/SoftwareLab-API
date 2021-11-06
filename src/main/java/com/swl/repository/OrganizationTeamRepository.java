package com.swl.repository;

import com.swl.models.management.OrganizationTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrganizationTeamRepository extends JpaRepository<OrganizationTeam, Integer> {

    Optional<List<OrganizationTeam>> findAllByTeamId(Integer idTeam);

    Optional<List<OrganizationTeam>> findAllByOrganizationId(Integer idOrg);

    Optional<OrganizationTeam> findByTeamIdAndCollaboratorId(Integer idTeam, Integer idCollaborator);

}
