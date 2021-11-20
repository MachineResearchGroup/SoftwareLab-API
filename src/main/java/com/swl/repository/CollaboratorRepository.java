package com.swl.repository;

import com.swl.models.people.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollaboratorRepository extends JpaRepository<Collaborator, Integer> {

    @Query("select c from Collaborator c where c.user.id= :userId")
    Optional<Collaborator> findCollaboratorByUserId(@Param("userId") Integer id);

    Optional<Collaborator> findCollaboratorByUserEmail(String email);

    @Query("select distinct oe.collaborator from OrganizationTeam oe where oe.organization.id =:idOrg")
    Optional<List<Collaborator>> findAllCollaboratorByOrganizationId(@Param("idOrg") Integer idOrg);

    @Query("select distinct oe.collaborator from OrganizationTeam oe where oe.team.id =:idTeam")
    Optional<List<Collaborator>> findAllCollaboratorByTeamId(@Param("idTeam") Integer idTeam);

    @Query("select oe.collaborator from OrganizationTeam oe where oe.organization.id =:idOrg " +
            "order by oe.collaborator.register desc")
    Optional<List<Collaborator>> findLastCollaboratorByRegister(@Param("idOrg") Integer idOrg);

}
