package com.swl.repository;

import com.swl.models.project.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Integer> {

    Optional<Requirement> findRequirementByDescription(String description);
}
