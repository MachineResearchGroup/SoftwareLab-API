package com.swl.repository;

import com.swl.models.project.Redaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RedactionRepository extends JpaRepository<Redaction, Integer> {

}
