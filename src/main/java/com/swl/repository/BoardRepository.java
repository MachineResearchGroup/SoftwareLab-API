package com.swl.repository;

import com.swl.models.project.Board;
import com.swl.models.project.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    Optional<List<Board>> findAllByProjectId(Integer idProject);

    @Query(value = "select b from Board b join Columns c on c.id =:idColumn where b.id = c.board.id")
    Optional<Board> findByColumnId(@Param("idColumn") Integer idColumn);
}
