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
}
