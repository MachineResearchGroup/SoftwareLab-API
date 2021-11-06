package com.swl.repository;

import com.swl.models.management.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface OrganizacaoRepository extends JpaRepository<Organization, Integer> {

    @Query("select oe.organization from OrganizationTeam oe where oe.team.id =:idEquipe")
    Optional<Organization> findOrganizacaoByEquipeId(@Param("idEquipe") Integer idEquipe);

    Optional<Organization> findOrganizationByCnpj(@Param("cnpj") String cnpj);
}
