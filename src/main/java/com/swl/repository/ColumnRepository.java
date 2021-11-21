package com.swl.repository;

import com.swl.models.project.Columns;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ColumnRepository extends JpaRepository<Columns, Integer> {

    Optional<List<Columns>> findAllByBoardId(Integer idBoard);

    @Query(value = "select c from Columns c join History h on h.id =:idHistory where c.id = h.column.id")
    Optional<Columns> findByHistoryId(@Param("idHistory") Integer idHistory);
}
