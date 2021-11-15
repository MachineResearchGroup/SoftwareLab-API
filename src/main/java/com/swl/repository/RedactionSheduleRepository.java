package com.swl.repository;

import com.swl.models.project.RedactionShedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface RedactionSheduleRepository extends JpaRepository<RedactionShedule, Integer> {

    Optional<RedactionShedule> findByProjectId(Integer idProject);
}
