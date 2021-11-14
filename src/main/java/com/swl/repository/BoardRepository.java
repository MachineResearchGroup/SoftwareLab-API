package com.swl.repository;

import com.swl.models.project.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<List<Board>> findAllByProjectId(Integer idProject);
}
