package com.swl.repository;

import com.swl.models.management.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {

    @Query("select distinct(t) from Team t join OrganizationTeam ot on ot.team.id = t.id where ot.organization.id =:idOrganization")
    Optional<List<Team>> findAllByOrganizationId(@Param("idOrganization") Integer idOrganization);

    @Query("select distinct(t) from Team t join OrganizationTeam ot on ot.team.id = t.id where ot.collaborator.id =:idCollaborator")
    Optional<List<Team>> findAllByCollaboratorId(@Param("idCollaborator") Integer idCollaborator);

    @Query(nativeQuery = true, value = "select t.* from team_projects tp join team t on t.id = tp.team_id " +
            "where tp.projects_id =:idProject")
    Optional<List<Team>> findAllByProjectId(@Param("idProject") Integer idProject);
}
