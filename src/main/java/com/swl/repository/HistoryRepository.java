package com.swl.repository;

import com.swl.models.project.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface HistoryRepository extends JpaRepository<History, Integer> {

    Optional<List<History>> findAllByColumnId(Integer idColumn);
}
