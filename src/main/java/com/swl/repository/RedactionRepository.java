package com.swl.repository;

import com.swl.models.project.Redaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface RedactionRepository extends JpaRepository<Redaction, Integer> {

    Optional<List<Redaction>> findAllByProjectId(Integer idProject);

    Optional<List<Redaction>> findAllByClientId(Integer idClient);
}
