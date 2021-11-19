package com.swl.repository;

import com.swl.models.management.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Integer> {

    @Query("select distinct oe.organization from OrganizationTeam oe where oe.team.id =:idTeam")
    Optional<Organization> findOrganizationByTeamId(@Param("idTeam") Integer idTeam);

    Optional<Organization> findOrganizationByCnpj(@Param("cnpj") String cnpj);

    @Query("select distinct oe.organization from OrganizationTeam oe where oe.collaborator.id =:idCollaborator")
    Optional<List<Organization>> findOrganizationByCollaboratorId(@Param("idCollaborator") Integer idCollaborator);
}
