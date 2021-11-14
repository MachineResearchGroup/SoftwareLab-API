package com.swl.repository;

import com.swl.models.project.Columns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ColumnRepository extends JpaRepository<Columns, Integer> {

    Optional<List<Columns>> findAllByBoardId(Integer idBoard);
}
