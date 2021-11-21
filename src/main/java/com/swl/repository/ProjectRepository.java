package com.swl.repository;

import com.swl.models.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    @Query(nativeQuery = true, value = "select * from project p join project_clients pc on clients_id =:idClient " +
            "where p.id = pc.project_id")
    Optional<List<Project>> findAllByClientId(@Param("idClient") Integer idClient);

    @Query(value = "select p from Project p join Board b on b.id =:idBoard where p.id = b.project.id")
    Optional<Project> findByBoardId(@Param("idBoard") Integer idBoard);

    @Query(nativeQuery = true, value = "select * from project p join project_requirements pr on pr.requirements_id =:idRequirement " +
            "where p.id = pr.project_id")
    Optional<List<Project>> findAllByRequirementId(@Param("idRequirement") Integer idRequirement);
}
