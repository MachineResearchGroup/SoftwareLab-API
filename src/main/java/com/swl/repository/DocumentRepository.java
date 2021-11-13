package com.swl.repository;

import com.swl.models.project.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    Optional<Document> findByUrl(@Param("url") String url);
}
