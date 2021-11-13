package com.swl.repository;

import com.swl.models.project.Requirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RequirementRepository extends JpaRepository<Requirement, Integer> {

}
